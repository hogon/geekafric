package com.geek.afric.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.geek.afric.client.GeekService;
import com.geek.afric.server.test.ServeurFile;
import com.geek.afric.server.test.ServeurFolder;
import com.geek.afric.shared.AbstractFichier;
import com.geek.afric.shared.Comment;
import com.geek.afric.shared.Dossier;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.Mail;
import com.geek.afric.shared.User;
import com.geek.afric.shared.test.File;
import com.geek.afric.shared.test.Folder;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GeekServiceImpl extends RemoteServiceServlet implements
		GeekService {
	Backup backup = new Backup();
	private final ServiceMail serviceMail = new ServiceMail();
	private final ServiceCache cache = new ServiceCache(
			MemcacheServiceFactory.getMemcacheService());
	private ArrayList<String> fileKeys = new ArrayList<String>();

	@Override
	public User login(String email, String password) {
		if (!email.contains("@")) {
			email = email + "@geekafric.com";
		}
		try {
			List<User> list = backup.getUserByEmail(email);
			if (list.size() < 1)
				return null;
			User u = list.get(0);
			boolean notConfirm = BCrypt.checkpw("ping", u.getPassword());
			if(notConfirm) {
				User use = new User();
				use.setNom(u.getNom());
				use.setPrenom(u.getPrenom());
				use.setId(null);
				return use;
			}
			boolean valid = BCrypt.checkpw(password, u.getPassword());
			if (valid) {
				// On incremmente le nombre de connexion
				User user = getUserFromSession(u.getId());
				if(user != null)
					return user;
				
				storeUserInSession(u);
				return u;
			} else
				return null;
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
	
	@Override
	public void logout(User user) {
		user.setDateDerniereModif(new Date());
		updateUser(user);
		deleteUserFromSession(user);
	}

	private void storeUserInSession(User user) {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute(user.getId(), user);
	}
	
	private void deleteUserFromSession(User user) {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute(user.getId());
		session.invalidate();
	}

	@Override
	public User getUserFromSession(String id) {
		User user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute(id);
		if (userObj != null && userObj instanceof User) {
			user = (User) userObj;
		}
		return user;
	}

	@Override
	public User getUser(String id) {
		return backup.getUserByKey(id);
	}
	
	@Override
	public User getUserByEmail(String email) {
		User user;
		try {
			user = backup.getUserByEmail(email).get(0);
		} catch (Exception e) {
			return null;
		}
		return user;
	}

	@Override
	public Fichier getFichier(String fileId) {
		Fichier fromCache = null;
		fromCache = cache.getFile(fileId);
		if(fromCache != null) {
			return fromCache;
		}
		Fichier file;
		try {
			file = clientFichier(backup.getFile(fileId));
			return cache.putFile(file);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String saveDossier(Dossier dossier) {
		String parentId = dossier.getParentId();
		if(parentId == null) {
			cache.clear();
			fileKeys.clear();
			return backup.saveDossier(serveurDossier(dossier));
		}
		String sdid = backup.saveDossier(serveurDossier(dossier));
		dossier.setId(sdid);
		Dossier parent = getDossier(parentId);
		parent.add(dossier);
		ServeurDossier sd = serveurDossier(parent);
		try {
			backup.updateDossier(sd);
			cache.clear();
			return sdid;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Dossier updateDossier(Dossier dossier) {
		if(dossier.getId() == null) {
			return null;
		}
		cache.clear();
		try {
			return clientDossier(backup.updateDossier(serveurDossier(dossier)));
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Dossier getDossier(String dossierId) {
		if(dossierId == null)
			return null;
		Dossier fromCache = null;
		fromCache = cache.getDossier(dossierId);
		if(fromCache != null) {
			return fromCache;
		}
		Dossier dossier;
		try {
			dossier = clientDossier(backup.getDossier(dossierId));
			return cache.putDossier(dossier);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void deleteDossier(Dossier dossier) {
		String parentId = dossier.getParentId();
		if(parentId == null) {
			for (Iterator<Fichier> iterator = dossier.getFiles().iterator(); iterator.hasNext();) {
				Fichier file = iterator.next();
				file.setParentId(null);
				deleteFile(file.getId());
			}
			for (Iterator<Dossier> iterator = dossier.getFolders().iterator(); iterator.hasNext();) {
				Dossier file = iterator.next();
				file.setParentId(null);
				deleteDossier(file);
			}
			try {
				backup.deleteDossier(dossier.getId());
				cache.clear();
				return;
			} catch (Exception e) {
				// Do nothing
			}
		}
		for (Iterator<Fichier> iterator = dossier.getFiles().iterator(); iterator.hasNext();) {
			Fichier file = iterator.next();
			deleteFile(file.getId());
		}
		for (Iterator<Dossier> iterator = dossier.getFolders().iterator(); iterator.hasNext();) {
			Dossier file = iterator.next();
			deleteDossier(file);
		}
		Dossier sd = getDossier(parentId);
		sd.remove(dossier);
		try {
			backup.updateDossier(serveurDossier(sd));
			backup.deleteDossier(dossier.getId());
		} catch (Exception e) {
			// Do nothing
		}
		cache.clear();
	}
	
	@Override
	public ArrayList<Dossier> getDossierByUser(User user, String order) {
		ArrayList<Dossier> fromCache = null;
		fromCache = cache.getDossiers(user.getId() + order);
		if(fromCache != null) {
			return fromCache;
		}
		if(!fileKeys.contains(user.getId() + order))
			fileKeys.add(user.getId() + order);
		return cache.putDossiers(user.getId() + order, fetchDossiers(backup.getDossiersByUserId(user.getId(), order)));
	}

	private ArrayList<Dossier> fetchDossiers(List<ServeurDossier> dossiers) {
		ArrayList<Dossier> dossier = new ArrayList<Dossier>();
		for(int i = 0; i < dossiers.size(); i++) {
			dossier.add(clientDossier(dossiers.get(i)));
		}
		return dossier;
	}

	private Fichier clientFichier(ServeurFichier file) {
		if(file == null) {
			return null;
		}
		Fichier cf = new Fichier(file.getAuthor(), file.getTitle(), file.getContenu().getValue(), 
				file.getUserId(), file.isPublique(), file.isDirectory(), file.getParentId());
		cf.setDateCreation(file.getDateCreation());
		cf.setDateDerniereModif(file.getDateDerniereModif());
		cf.setId(file.getId());
		cf.setPath(file.getPath());
		return cf;
	}
	
	private ServeurFichier serveurFichier(Fichier file) {
		ServeurFichier sf = new ServeurFichier(file.getAuthor(), file.getTitle(), new Text(file.getContenu()), 
				file.getUserId(), file.isPublique(), file.isDirectory(), file.getParentId());
		sf.setId(file.getId());
		sf.setDateCreation(file.getDateCreation());
		sf.setDateDerniereModif(file.getDateDerniereModif());
		sf.setPath(file.getPath());
		return sf;
	}
	
	private ServeurDossier serveurDossier(Dossier folder) {
		ArrayList<String> folders = new ArrayList<String>();
		for (Iterator<Dossier> iterator = folder.getFolders().iterator(); iterator.hasNext();) {
			Dossier sfolder = iterator.next();
			folders.add(sfolder.getId());
		}
		
		ArrayList<String> files = new ArrayList<String>();
		for (Iterator<Fichier> iterator = folder.getFiles().iterator(); iterator.hasNext();) {
			Fichier sfolder = iterator.next();
			files.add(sfolder.getId());
		}
		
		ServeurDossier sfolder = new ServeurDossier(folder.getAuthor(), folder.getTitle(), folder.getUserId(), 
				folder.isPublique(), folder.isDirectory(), folder.getParentId());
		sfolder.setFolders(folders);
		sfolder.setFiles(files);
		sfolder.setDateCreation(folder.getDateCreation());
		sfolder.setDateDerniereModif(folder.getDateDerniereModif());
		sfolder.setId(folder.getId());
		sfolder.setPath(folder.getPath());
		return sfolder;
	}
	
	private Dossier clientDossier(ServeurDossier folder) {
		ArrayList<Dossier> folders = new ArrayList<Dossier>();
		for (Iterator<String> iterator = folder.getFolders().iterator(); iterator.hasNext();) {
			String sfolder = iterator.next();
			Dossier dossier = getDossier(sfolder);
			if(dossier != null) folders.add(dossier);
		}
		ArrayList<Fichier> files = new ArrayList<Fichier>();
		for (Iterator<String> iterator = folder.getFiles().iterator(); iterator.hasNext();) {
			String sfolder = iterator.next();
			Fichier file = getFichier(sfolder);
			if(file != null) files.add(file);
		}
		Dossier cfolder = new Dossier(folder.getAuthor(), folder.getTitle(), folder.getUserId(), 
				folder.isPublique(), folder.isDirectory(), folder.getParentId());
		cfolder.setFiles(files);
		cfolder.setDateCreation(folder.getDateCreation());
		cfolder.setDateDerniereModif(folder.getDateDerniereModif());
		cfolder.setId(folder.getId());
		cfolder.setFolders(folders);
		cfolder.setPath(folder.getPath());
		return cfolder;
	}
	
	private static Comment clientComment(ServerComment comment) {
		Comment cc = new Comment(comment.getPersonid(),
				comment.getDisplayName(), comment.getScore(), comment.getText()
						.getValue(), comment.getDateCreation(),
				comment.getCommentedid(), comment.getLikeIds(),
				comment.getNotLikeIds());
		cc.setScore(comment.getScore());
		cc.setId(comment.getId());
		return cc;
	}

	private static ServerComment serveurComment(Comment comment) {
		ServerComment sc = new ServerComment(comment.getUserId(), comment.getDisplayName(),
				comment.getScore(), new Text(comment.getText()),
				comment.getCommentedid(), comment.getLikeIds(),
				comment.getNotLikeIds());
		sc.setScore(comment.getScore());
		sc.setId(comment.getId());
		return sc;
	}
	
	@SuppressWarnings("unused")
	private List<ServeurFichier> serveurFichiers(List<Fichier> files) {
		List<ServeurFichier> sfs = new ArrayList<ServeurFichier>();
		for(int i = 0; i < files.size(); i++) {
			sfs.add(serveurFichier(files.get(i)));
		}
		return sfs;
	}
	
	@SuppressWarnings("unused")
	private List<Fichier> clientFichiers(List<ServeurFichier> files) {
		ArrayList<Fichier> cfs = new ArrayList<Fichier>();
		for(int i = 0; i < files.size(); i++) {
			cfs.add(clientFichier(files.get(i)));
		}
		return cfs;
	}

	@Override
	public ArrayList<Fichier> getFilesByUserId(String userId) {
		ArrayList<Fichier> fromCache = cache.getFiles(userId);
		if(fromCache != null)
			return fromCache;
		return cache.putFiles(userId, fetchFiles(userId));
	}

	private ArrayList<Fichier> fetchFiles(String userId) {
		ArrayList<Fichier> files = new ArrayList<Fichier>();
		List<ServeurFichier> sfiles = new ArrayList<ServeurFichier>();
		if(!userId.equals("shared"))
			sfiles = backup.getFilesByUserId(userId);
		else 
			sfiles = backup.getFilesShared();
		if(sfiles == null)
			return files;
		for(int i = 0; i < sfiles.size(); i++) {
			files.add(clientFichier(sfiles.get(i)));
		}
		return files;
	}
	
	@Override
	public ArrayList<Fichier> getFilesByUserId(String userId, int startIndex, String order) {
		ArrayList<Fichier> fromCache = cache.getFiles(userId + order + startIndex);
		if(fromCache != null)
			return fromCache;
		if(!fileKeys.contains(userId + order + startIndex))
			fileKeys.add(userId + order + startIndex);
		return cache.putFiles(userId + order + startIndex, fetchFiles(userId, startIndex, order));
	}
	
	private ArrayList<Fichier> fetchFiles(String userId, int startIndex, String order) {
		ArrayList<Fichier> files = new ArrayList<Fichier>();
		List<ServeurFichier> sfiles = new ArrayList<ServeurFichier>();
		if(!userId.equals("shared"))
			sfiles = backup.getFilesByUserId(userId, startIndex, order);
		else 
			sfiles = backup.getFilesShared(startIndex, order);
		if(sfiles == null)
			return files;
		for(int i = 0; i < sfiles.size(); i++) {
			files.add(clientFichier(sfiles.get(i)));
		}
		return files;
	}

	@Override
	public ArrayList<Fichier> getFileShared() {
		ArrayList<Fichier> fromCache = cache.getFiles("shared");
		if(fromCache != null)
			return fromCache;
		return cache.putFiles("shared", fetchFiles("shared"));
	}

	@Override
	public User saveUser(User user) {
		List<User> list = backup.getUserByEmail(user.getEmail());
		if(list.size() > 0)
			return null;
		User u = new User(user.getNom(), user.getPrenom(), user.getEmail(), 
				BCrypt.hashpw("ping", BCrypt.gensalt()));
		backup.saveUser(u);
		Mail mail = new Mail("admin@geekafric.com", u.getEmail(), serviceMail.getWelcomeObject(), 
				serviceMail.getWelcomeMessage(u.getPrenom()));
		mail.setSenderName("Geekafric");
		mail.setReceiverName(u.getDisplayname());
		mail.setSenderDelete(true);
		mail.setReceiverDelete(false);
		mail.setRead(false);
		sendMail(mail);
		return u;
	}

	@Override
	public User updateUser(User user) {
		user.setDateDerniereModif(new Date());
		backup.updateUser(user);
		return user;
	}

	@Override
	public void deleteUser(String userId) {
		try {
			backup.deleteFiles(userId);
			backup.deleteUser(userId);
			cache.clear();
			fileKeys.clear();
		} catch (Exception e) {
			// Do nothing
		}
	}

	@Override
	public String saveFile(Fichier file) {
		String parentId = file.getParentId();
		if(parentId == null) {
			cache.clear();
			fileKeys.clear();
			return backup.saveFile(serveurFichier(file));
		}
		String sdid = backup.saveFile(serveurFichier(file));
		file.setId(sdid);
		Dossier parent = getDossier(parentId);
		parent.add(file);
		ServeurDossier sd = serveurDossier(parent);
		try {
			backup.updateDossier(sd);
			cache.clear();
			fileKeys.clear();
			return sdid;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Fichier updateFile(Fichier file) {
		if(file.getId() == null) {
			return null;
		}
		cache.clear();
		fileKeys.clear();
		try {
			return getFichier(backup.updateFile(serveurFichier(file)));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void deleteFile(String fileId) {
		fileKeys.clear();
		cache.clear();
		deleteCommentByFile(fileId);
		Fichier file = getFichier(fileId);
		String parentId = file.getParentId();
		try {
			if(parentId == null) {
					backup.deleteFile(fileId);
				return;
			}
			Dossier sd = getDossier(parentId);
			sd.remove(file);
			backup.updateDossier(serveurDossier(sd));
			backup.deleteFile(fileId);
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	private void deleteCommentByFile(String fileId) {
		Fichier file = getFichier(fileId);
		for (String commentid : file.getCommentIds()) {
			try {
				backup.deleteComment(commentid);
			} catch (Exception e) {
				// Do nothing
			}
		}
	}
	
	@Override
	public void deleteFiles(ArrayList<AbstractFichier> files) {
		for(int i = 0; i < files.size(); i++) {
			if(files.get(i) instanceof Fichier) {
				deleteFile(files.get(i).getId());
			}
			else if(files.get(i) instanceof Dossier) {
					deleteDossier((Dossier) files.get(i));
			}
		}
		fileKeys.clear();
		cache.clear();
	}
	
	@Override
	public ArrayList<Fichier> searchByToken(User user, String token) {
		ArrayList<Fichier> result = new ArrayList<Fichier>();
		ArrayList<Fichier> files = getFilesByUserId(user.getId());
		files.addAll(getFilesByUserId("shared"));
		for(int i = 0; i < files.size(); i ++) {
			if(files.get(i).getAuthor().toLowerCase().contains(token.toLowerCase()) || 
					files.get(i).getTitle().toLowerCase().contains(token.toLowerCase())) {
				result.add(files.get(i));
			}
		}
		return result;
	}
	
	@Override
	public Set<String> getSearchTokens(User user) {
		Set<String> tokens = new HashSet<String>();
		ArrayList<AbstractFichier> files = getFilesByUser(user.getId());
		for(int i = 0; i < files.size(); i ++) {
			tokens.add(files.get(i).getAuthor());
			tokens.add(files.get(i).getTitle());
		}
		return tokens;
	}
	
	@Override
	public ArrayList<Fichier> searchInFiles(User user, String token) {
		ArrayList<Fichier> result = new ArrayList<Fichier>();
		ArrayList<Fichier> files = getFilesByUserId(user.getId());
		files.addAll(getFilesByUserId("shared"));
		for(int i = 0; i < files.size(); i ++) {
			if(files.get(i).getAuthor().toLowerCase().contains(token.toLowerCase()) || 
					files.get(i).getTitle().toLowerCase().contains(token.toLowerCase()) ||
					files.get(i).getContenu().toLowerCase().contains(token.toLowerCase())) {
				result.add(files.get(i));
			}
		}
		return result;
	}
	
	@Override
	public void clearCache(User user) {
		cache.clear();
	}
	
	private static Mail toClientMail(ServerMail mail) {
		Mail cmail = new Mail(mail.getSender(), mail.getReceiver(),
				mail.getObject(), mail.getMessage().getValue());
		cmail.setDateCreation(mail.getDateCreation());
		cmail.setId(mail.getId());
		cmail.setSenderName(mail.getSenderName());
		cmail.setReceiverName(mail.getReceiverName());
		cmail.setSenderDelete(mail.isSenderDelete());
		cmail.setReceiverDelete(mail.isReceiverDelete());
		cmail.setRead(mail.isRead());
		return cmail;
	}

	private static ServerMail toServerMail(Mail mail) {
		ServerMail smail = new ServerMail(mail.getSender(), mail.getReceiver(),
				mail.getObject(), new Text(mail.getMessage()));
		smail.setDateCreation(mail.getDateCreation());
		smail.setId(mail.getId());
		smail.setSenderName(mail.getSenderName());
		smail.setReceiverName(mail.getReceiverName());
		smail.setSenderDelete(mail.isSenderDelete());
		smail.setReceiverDelete(mail.isReceiverDelete());
		smail.setRead(mail.isRead());
		return smail;
	}
	
	@Override
	public void retrievePassWord(User user) {
		String message = serviceMail.sendRetrievePasswordLink(user.getEmail(),
						user.getId());
		Mail mail = new Mail("admin@geekafric.com", user.getEmail(), "Votre mot de passe sur Geekafric!", message);
		mail.setSenderDelete(true);
		mail.setSenderName("Geekafric");
		mail.setRead(false);
		saveMessage(mail);
	}
	
	@Override
	public void confirmEmail(User user) {
		String message = serviceMail.sendConfirmEmailLink(user.getEmail(),
						user.getId());
		Mail mail = new Mail("admin@geekafric.com", user.getEmail(), "Confirmation de ton email!", message);
		mail.setSenderDelete(true);
		mail.setSenderName("Geekafric");
		mail.setRead(false);
		saveMessage(mail);
	}
	
	@Override
	public User updatePassword(User user, String password) {
		password = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(password);
		return updateUser(user);
	}
	
	@Override
	public String sendMail(Mail mail) {
		serviceMail.sendMail(mail);
		return saveMessage(mail);
	}
	
	@Override
	public String saveMessage(Mail mail) {
		return backup.saveMessage(toServerMail(mail));
	}
	
	@Override
	public Mail updateMail(Mail mail) {
		try {
			return toClientMail(backup.updateMail(toServerMail(mail)));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public ArrayList<Mail> getMyMessages(User user, int lastIndex) {
		ArrayList<Mail> msgs = new ArrayList<Mail>();
		List<ServerMail> msgsAll = backup.getAllMessage(lastIndex);
		for (ServerMail m : msgsAll) {
			Mail mail = toClientMail(m);
			if (user.isReceiver(mail) || user.isSender(mail)) {
				msgs.add(mail);
			}
		}
		return msgs;
	}

	@Override
	public ArrayList<Mail> getMyReceivedMail(User user, int startIndex, String order) {
		List<ServerMail> smails = backup.findMailByReceiver(
				user.getEmail(), user.isAdmin(), startIndex, order);
		ArrayList<Mail> mails = new ArrayList<Mail>();
		if(smails == null || smails.isEmpty())
			return mails;
		for (int i = 0; i < smails.size(); i++) {
			mails.add(toClientMail(smails.get(i)));
		}
		return mails;
	}

	@Override
	public ArrayList<Mail> getMySentMail(User user, int startIndex, String order) {
		List<ServerMail> smails = backup.findMailBySender(user.getEmail(),
				user.isAdmin(), startIndex, order);
		ArrayList<Mail> mails = new ArrayList<Mail>();
		if(smails == null || smails.isEmpty())
			return mails;
		for (int i = 0; i < smails.size(); i++) {
			mails.add(toClientMail(smails.get(i)));
		}
		return mails;
	}

	@Override
	public ArrayList<Mail> getMyMessages(User user, String order) {
		ArrayList<Mail> msgs = new ArrayList<Mail>();
		List<ServerMail> msgsAll = backup.getAllMessage(order);

		if(msgsAll == null || msgsAll.isEmpty())
			return msgs;
		for (ServerMail m : msgsAll) {
			Mail mail = toClientMail(m);
			if ((user.isReceiver(mail) && !mail.isReceiverDelete())
					|| user.isSender(mail) && !mail.isSenderDelete()) {
				msgs.add(mail);
			}
		}
		return msgs;
	}

	@Override
	public Mail getMail(String id) {
		Mail mail = null;
		try {
			mail = toClientMail(backup.findMessageByKey(id));
			return mail;
		} catch (Exception e) {
			return mail;
		}
	}
	
	@Override
	public ArrayList<Mail> deleteMails(ArrayList<Mail> mails) {
		for (int i = 0; i < mails.size(); i++) {
			Mail mail = mails.get(i);
			deleteMail(mail);
		}
		return mails;
	}

	@Override
	public Mail deleteMail(Mail mail) {
		try{
			if (mail.canDelete())
				backup.deleteMail(mail.getId());
			else
				backup.updateMail(toServerMail(mail));
			return mail;
		}catch(Exception e) {
			return mail;
		}
	}
	
	@Override
	public int getUnreadCount(User user) {
		return backup.findUnreadMailByReceiver(user.getEmail(),
				user.isAdmin()).size();
	}

	@Override
	public Set<String> getEmails() {
		Set<String> set = new HashSet<String>();
		List<User> users = backup.getAllUsers();
		for(int i = 0; i < users.size(); i++) {
			set.add(users.get(i).getEmail());
		}
		return set;
	}

	@Override
	public ArrayList<Comment> getObjectComments(String objectId) {
		ArrayList<Comment> fromCache = cache.getObjectComments(objectId);
		if (fromCache != null)
			return fromCache;
		return cache.putObjectComments(objectId, fetchObjectComments(objectId));
	}

	private ArrayList<Comment> fetchObjectComments(String objectId) {
		List<ServerComment> comments = backup.findCommentByObject(objectId);
		ArrayList<Comment> res = new ArrayList<Comment>();
		for (ServerComment comment : comments) {
			res.add(clientComment(comment));
		}
		return res;
	}
	
	@Override
	public Comment addComment(Fichier file, Comment c) {
		try{
			cache.deleteFile(file.getId());
			cache.deleteComments(file.getId());
			ServerComment comment = backup.saveComment(serveurComment(c));
			file.addComment(comment.getId());
			backup.updateFile(serveurFichier(file));
			return clientComment(comment);
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public Comment deleteComment(Fichier file, Comment comment) {
		try{
			cache.deleteFile(file.getId());
			cache.deleteComments(comment.getCommentedid());
			file.deleteComment(comment);
			file.getCommentIds().remove(comment.getId());
			backup.deleteComment(comment.getId());
			backup.updateFile(serveurFichier(file));
			return comment;
		} catch (Exception e) {
			return null;
		}
	}
	
	private List<ServeurFile> serveurFiles(List<File> files) {
		ArrayList<ServeurFile> sfs = new ArrayList<ServeurFile>();
		for(int i = 0; i < files.size(); i++) {
			sfs.add(serveurFile(files.get(i)));
		}
		return sfs;
	}
	
	private List<File> clientFiles(List<ServeurFile> files) {
		ArrayList<File> cfs = new ArrayList<File>();
		for(int i = 0; i < files.size(); i++) {
			cfs.add(clientFile(files.get(i)));
		}
		return cfs;
	}
	
	private ServeurFolder serveurFolder(Folder folder) {
//		ArrayList<ServeurFolder> folders = new ArrayList<ServeurFolder>();
//		for (Iterator<Folder> iterator = folder.getFolders().iterator(); iterator.hasNext();) {
//			Folder sfolder = iterator.next();
//			folders.add(serveurFolder(sfolder));
//		}
		ArrayList<String> folders = new ArrayList<String>();
		for (Iterator<Folder> iterator = folder.getFolders().iterator(); iterator.hasNext();) {
			Folder sfolder = iterator.next();
			folders.add(sfolder.getId());
		}
		ServeurFolder sfolder = new ServeurFolder(folder.getAuthor(), folder.getTitle(), folder.getUserId(), 
				folder.isPublique(), folder.isDirectory(), folder.getParentId());
		sfolder.setFolders(folders);
		sfolder.setFiles(serveurFiles(folder.getFiles()));
		sfolder.setDateCreation(folder.getDateCreation());
		sfolder.setDateDerniereModif(folder.getDateDerniereModif());
		sfolder.setId(folder.getId());
		return sfolder;
	}
	
	private Folder clientFolder(ServeurFolder folder) {
//		ArrayList<Folder> folders = new ArrayList<Folder>();
//		for (Iterator<ServeurFolder> iterator = folder.getFolders1().iterator(); iterator.hasNext();) {
//			ServeurFolder sfolder = iterator.next();
//			folders.add(clientFolder(sfolder));
//		}
		ArrayList<Folder> folders = new ArrayList<Folder>();
		for (Iterator<String> iterator = folder.getFolders().iterator(); iterator.hasNext();) {
			String sfolder = iterator.next();
			folders.add(getFolder(sfolder));
		}
		Folder cfolder = new Folder(folder.getAuthor(), folder.getTitle(), folder.getUserId(), 
				folder.isPublique(), folder.isDirectory(), folder.getParentId());
		cfolder.setFiles(clientFiles(folder.getFiles()));
		cfolder.setDateCreation(folder.getDateCreation());
		cfolder.setDateDerniereModif(folder.getDateDerniereModif());
		cfolder.setId(folder.getId());
		cfolder.setFolders(folders);
		return cfolder;
	}
	
	@Override
	public String saveFolder(Folder folder) {
		return backup.saveFolder(serveurFolder(folder));
	}
	
	public Folder updateFolder(Folder folder){
		backup.updateFolder(serveurFolder(folder));
		return folder;
	}
	
	public void deleteFolder(Folder folder) {
		backup.deleteFolder(folder);
	}
	
	@Override
	public Folder getFolder(String id) {
		return clientFolder(backup.findFolder(id));
	}
	
	@Override
	public void test(User user) {
		Dossier folder = new Dossier(user.getDisplayname(), "Mon Dossier", user.getId(), false, true, null);
		String folderId = saveDossier(folder);
		Dossier folder1 = new Dossier(user.getDisplayname(), "Dossier1", user.getId(), false, true, folderId);
		String id1 = saveDossier(folder1);
		saveFile(new Fichier(user.getDisplayname(), "Untitled", "Ceci est un autre texte", user.getId(), false, true, id1));
		saveFile(new Fichier(user.getDisplayname(), "Untitled1", "Ceci est un texte", user.getId(), false, true, folderId));
		saveFile(new Fichier(user.getDisplayname(), "Untitled2", "Ceci est un autre texte", user.getId(), false, true, folderId));
	}

	private File clientFile(ServeurFile file) {
		if(file == null) {
			return null;
		}
		File cf = new File(file.getAuthor(), file.getTitle(), file.getContent().getValue(), 
				file.getUserId(), file.isPublique(), file.isDirectory(), file.getParentId());
		cf.setDateCreation(file.getDateCreation());
		cf.setDateDerniereModif(file.getDateDerniereModif());
		cf.setId(file.getId());
		return cf;
	}
	
	private ServeurFile serveurFile(File file) {
		ServeurFile sf = new ServeurFile(file.getAuthor(), file.getTitle(), new Text(file.getContent()), 
				file.getUserId(), file.isPublique(), file.isDirectory(), file.getParentId());
		sf.setId(file.getId());
		sf.setDateCreation(file.getDateCreation());
		sf.setDateDerniereModif(file.getDateDerniereModif());
		return sf;
	}
	
	@Override
	public ArrayList<AbstractFichier> getFiles(String userId, String parentId, String order) {
		ArrayList<AbstractFichier> all = cache.getRacines(userId + parentId + order);
		if(all != null) {
			return all;
		}
		if(!userId.equals("shared")) {
			all = (ArrayList<AbstractFichier>) backup.getFiles(userId, parentId, order);
		} else {
			all = (ArrayList<AbstractFichier>) backup.getSharedFiles(userId, parentId, order);
		}
		ArrayList<AbstractFichier> alls = new ArrayList<AbstractFichier>();
		for(int i = 0; i < all.size(); i++) {
			AbstractFichier file = all.get(i);
			if(file instanceof ServeurDossier) {
				alls.add(clientDossier((ServeurDossier) file));
			} else if(file instanceof ServeurFichier) {
				alls.add(clientFichier((ServeurFichier) file));
			}
		}
		return cache.putRacines(userId + parentId + order, alls);
	}
	
	@Override
	public ArrayList<AbstractFichier> searchFiles(User user, String token) {
		ArrayList<AbstractFichier> result = cache.getRacines(user.getId() + token);
		if(result != null) {
			return result;
		}
		result = getFilesByUser(user.getId());
		ArrayList<AbstractFichier> alls = new ArrayList<AbstractFichier>();
		for(int i = 0; i < result.size(); i ++) {
			AbstractFichier file = result.get(i);
			if(file.getAuthor().toLowerCase().contains(token.toLowerCase()) || 
					file.getTitle().toLowerCase().contains(token.toLowerCase())) {
				alls.add(file);
			} else if(file instanceof Fichier) {
				if(((Fichier) file).getContenu().toLowerCase().contains(token.toLowerCase())) {
					alls.add(file);
				}
			}
		}
		return cache.putRacines(user.getId() + token, alls);
	}

	@Override
	public ArrayList<AbstractFichier> getFilesByUser(String userId) {
		ArrayList<AbstractFichier> all = cache.getRacines(userId);
		if(all != null) {
			return all;
		}
		all = (ArrayList<AbstractFichier>) backup.getFilesByUser(userId);
		ArrayList<AbstractFichier> alls = new ArrayList<AbstractFichier>();
		for(int i = 0; i < all.size(); i++) {
			AbstractFichier file = all.get(i);
			if(file instanceof ServeurDossier) {
				alls.add(clientDossier((ServeurDossier) file));
			} else if(file instanceof ServeurFichier) {
				alls.add(clientFichier((ServeurFichier) file));
			}
		}
		return cache.putRacines(userId, alls);
	}
	
	@Override
	public void updateAll(Dossier dossier, ArrayList<AbstractFichier> selectedFiles) {
		cache.clear();
		updateDossier(dossier);
		for(int i = 0; i < selectedFiles.size(); i++) {
			AbstractFichier file = selectedFiles.get(i);
			if(file instanceof Fichier) updateFile((Fichier) file);
			else if(file instanceof Dossier) updateDossier((Dossier) file);
		}
		cache.clear();
	}
}