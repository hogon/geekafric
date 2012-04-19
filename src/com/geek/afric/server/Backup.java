/**
 * 
 */
package com.geek.afric.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.geek.afric.server.test.ServeurFile;
import com.geek.afric.server.test.ServeurFolder;
import com.geek.afric.shared.AbstractFichier;
import com.geek.afric.shared.Dossier;
import com.geek.afric.shared.User;
import com.geek.afric.shared.test.Folder;

/**
 * @author Mcicheick
 * 
 */
public class Backup {

	public String saveUser(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			pm.makePersistent(user);
			tx.commit();
			return user.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public User getUserByKey(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user;
		try {
			user = pm.getObjectById(User.class, id);
			return pm.detachCopy(user);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserByEmail(String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class);
		query.setFilter("email== \"" + email + "\"");
		try {

			List<User> res = (List<User>) query.execute();
			res.size();
			return (List<User>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public String updateUser(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			User u = pm.getObjectById(User.class, user.getId());
			u.setDateDerniereModif(new Date());
			u.setEmail(user.getEmail());
			u.setNom(user.getNom());
			u.setPassword(user.getPassword());
			u.setPrenom(user.getPrenom());
			tx.commit();
			return user.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public User deleteUser(String userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		User u;
		try {
			tx.begin();
			u = pm.getObjectById(User.class, userId);
			pm.deletePersistent(u);
			tx.commit();
			return u;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public String saveFile(ServeurFichier file) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			pm.makePersistent(file);
			tx.commit();
			return file.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurFichier getFile(String fileId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServeurFichier sf;
		try {
			sf = pm.getObjectById(ServeurFichier.class, fileId);
			return pm.detachCopy(sf);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServeurFichier> getFilesByUserId(String userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServeurFichier.class);
		query.setFilter("userId== \"" + userId + "\" && publique== false");
		query.setOrdering("dateCreation asc");
		try {
			List<ServeurFichier> res = (List<ServeurFichier>) query.execute();
			res.size();
			return (List<ServeurFichier>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServeurFichier> getFilesByUserId(String userId, int startIndex,
			String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (order == null)
			order = "dateCreation asc";
		int index = 15 * startIndex;
		String q = "select from " + ServeurFichier.class.getName()
				+ " order by " + order + " range " + index + "," + (index + 15);
		Query query = pm.newQuery(q);
		query.setFilter("userId== \"" + userId + "\" && publique== false");
		try {
			List<ServeurFichier> res = (List<ServeurFichier>) query.execute();
			res.size();
			return (List<ServeurFichier>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServeurFichier> getFilesShared() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServeurFichier.class);
		query.setFilter("publique== true");
		query.setOrdering("dateCreation asc");
		try {
			List<ServeurFichier> res = (List<ServeurFichier>) query.execute();
			res.size();
			return (List<ServeurFichier>) pm.detachCopyAll(res);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServeurFichier> getFilesShared(int startIndex, String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (order == null)
			order = "dateCreation asc";
		int index = 15 * startIndex;
		String q = "select from " + ServeurFichier.class.getName()
				+ " order by " + order + " range " + index + "," + (index + 15);
		Query query = pm.newQuery(q);
		query.setFilter("publique== true");
		try {
			List<ServeurFichier> res = (List<ServeurFichier>) query.execute();
			res.size();
			return (List<ServeurFichier>) pm.detachCopyAll(res);
		} finally {
			pm.close();
		}
	}

	public String updateFile(ServeurFichier file) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			ServeurFichier sf = pm.getObjectById(ServeurFichier.class,
					file.getId());
			sf.setDateDerniereModif(new Date());
			sf.setContenu(file.getContenu());
			sf.setTitle(file.getTitle());
			sf.setPublique(file.isPublique());
			sf.setUserId(file.getUserId());
			sf.setPath(file.getPath());
			sf.setParentId(file.getParentId());
			tx.commit();
			return file.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurFichier deleteFile(String fileId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		ServeurFichier sf;
		try {
			tx.begin();
			sf = pm.getObjectById(ServeurFichier.class, fileId);
			pm.deletePersistent(sf);
			tx.commit();
			return sf;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public void deleteFiles(String userId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			List<ServeurFichier> sf = getFilesByUserId(userId);
			pm.deletePersistentAll(sf);
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public String saveDossier(ServeurDossier dossier) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			pm.makePersistent(dossier);
			dossier.getFolders();
			dossier.getFiles();
			tx.commit();
			return dossier.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurDossier updateDossier(ServeurDossier folder) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			ServeurDossier sf = pm.getObjectById(ServeurDossier.class,
					folder.getId());
			sf.setAuthor(folder.getAuthor());
			sf.setDateDerniereModif(new Date());
			sf.setDirectory(folder.isDirectory());
			sf.setFiles(folder.getFiles());
			sf.setFolders(folder.getFolders());
			sf.setParentId(folder.getParentId());
			sf.setPublique(folder.isPublique());
			sf.setTitle(folder.getTitle());
			sf.setUserId(folder.getUserId());
			sf.setPath(folder.getPath());
			tx.commit();
			return folder;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurDossier deleteDossier(String id) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			ServeurDossier d = pm.getObjectById(ServeurDossier.class, id);
			pm.deletePersistent(d);
			tx.commit();
			return d;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurDossier getDossier(String id) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServeurDossier sd;
		try {
			sd = pm.getObjectById(ServeurDossier.class, id);
			sd.getFolders();
			sd.getFiles();
			return pm.detachCopy(sd);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServeurDossier> getDossiersByUserId(String userId, String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (order == null || order.isEmpty())
			order = "title asc";
		String q = "select from " + Dossier.class.getName() + " order by "
				+ order;
		Query query = pm.newQuery(q);
		query.setFilter("userId== \"" + userId + "\"");
		try {
			List<ServeurDossier> res = (List<ServeurDossier>) query.execute();
			res.size();
			return (List<ServeurDossier>) pm.detachCopyAll(res);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<AbstractFichier> getFiles(String userId, String parentId,
			String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<AbstractFichier> files = new ArrayList<AbstractFichier>();
		if (order == null || order.isEmpty())
			order = "title asc";
		String filtre = ((parentId == null) ? "userId== \"" + userId
				+ "\"  && parentId == " + parentId : "userId== \"" + userId
				+ "\" && parentId == \"" + parentId + "\"")
				+ "&& publique == false";
		String q1 = "select from " + ServeurFichier.class.getName()
				+ " order by " + order;
		Query query1 = pm.newQuery(q1);
		query1.setFilter(filtre);
		String q2 = "select from " + ServeurDossier.class.getName()
				+ " order by " + order;
		Query query2 = pm.newQuery(q2);
		query2.setFilter(filtre);
		try {
			List<ServeurFichier> res1 = (List<ServeurFichier>) query1.execute();
			res1.size();
			List<ServeurDossier> res2 = (List<ServeurDossier>) query2.execute();
			res2.size();
			files.addAll((List<ServeurDossier>) pm.detachCopyAll(res2));
			files.addAll((List<ServeurFichier>) pm.detachCopyAll(res1));
			return files;
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ServeurFichier> getFilesByUserId(String userId, String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (order == null || order.isEmpty())
			order = "title asc";
		String q = "select from " + ServeurFichier.class.getName()
				+ " order by " + order;
		Query query = pm.newQuery(q);
		query.setFilter("userId== \"" + userId + "\"");
		try {
			List<ServeurFichier> res = (List<ServeurFichier>) query.execute();
			res.size();
			return (List<ServeurFichier>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public String saveMessage(ServerMail msg) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(msg);
			tx.commit();
			return msg.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public List<ServerMail> getAllMessage(int lastIndex) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServerMail.class);
		try {
			@SuppressWarnings("unchecked")
			List<ServerMail> res = (List<ServerMail>) query.execute();
			res.size();
			return (List<ServerMail>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public ServerMail findMessageByKey(String id) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServerMail msg;
		try {

			msg = pm.getObjectById(ServerMail.class, id);
			return pm.detachCopy(msg);
		} finally {
			pm.close();
		}
	}

	public ServerMail updateMail(ServerMail msg) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			ServerMail m = pm.getObjectById(ServerMail.class, msg.getId());
			m.setMessage(msg.getMessage());
			m.setReceiver(msg.getReceiver());
			m.setSender(msg.getSender());
			m.setDateDerniereModif(new Date());
			m.setRead(msg.isRead());
			m.setSenderDelete(msg.isSenderDelete());
			m.setReceiverDelete(msg.isReceiverDelete());
			tx.commit();
			return msg;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}

	}

	public void deleteMail(String id) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();

		try {
			ServerMail mail = pm.getObjectById(ServerMail.class, id);
			pm.deletePersistent(mail);
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public List<ServerMail> getAllMessage(String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServerMail.class);
		query.setOrdering(order);
		try {
			@SuppressWarnings("unchecked")
			List<ServerMail> res = (List<ServerMail>) query.execute();
			res.size();
			return (List<ServerMail>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public List<ServerMail> findMailBySender(String sender, boolean admin,
			int startIndex, String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String q = "select from " + ServerMail.class.getName() + " order by "
				+ order;
		Query query = pm.newQuery(q);
		String filtre = "sender == \"" + sender
				+ "\"  && senderDelete == false";
		if (admin)
			filtre = "(sender == \""
					+ sender
					+ "\" || sender == \"admin@geekafric.com\") && senderDelete == false";
		query.setFilter(filtre);
		try {
			@SuppressWarnings("unchecked")
			List<ServerMail> res = (List<ServerMail>) query.execute();
			res.size();
			return (List<ServerMail>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public List<ServerMail> findMailByReceiver(String receiver, boolean admin,
			int startIndex, String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String q = "select from " + ServerMail.class.getName() + " order by "
				+ order;
		Query query = pm.newQuery(q);
		String filtre = "receiver == \"" + receiver
				+ "\" && receiverDelete == false";
		if (admin)
			filtre = "(receiver == \""
					+ receiver
					+ "\" || receiver == \"admin@geekafric.com\") && receiverDelete == false";
		query.setFilter(filtre);
		try {
			@SuppressWarnings("unchecked")
			List<ServerMail> res = (List<ServerMail>) query.execute();
			res.size();
			return (List<ServerMail>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public List<ServerMail> findUnreadMailByReceiver(String receiver,
			boolean admin) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServerMail.class);
		String filtre = "receiver == \"" + receiver + "\" && read == false";
		if (admin)
			filtre = "(receiver == \""
					+ receiver
					+ "\" || receiver == \"admin@geekafric.com\") && read == false";
		query.setFilter(filtre);
		try {
			@SuppressWarnings("unchecked")
			List<ServerMail> res = (List<ServerMail>) query.execute();
			res.size();
			return (List<ServerMail>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public List<User> getAllUsers() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class);
		query.setOrdering("dateCreation asc");
		try {
			@SuppressWarnings("unchecked")
			List<User> res = (List<User>) query.execute();
			res.size();
			return (List<User>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	public ServerComment saveComment(ServerComment comment) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			pm.makePersistent(comment);
			comment.getLikeIds();
			comment.getNotLikeIds();
			tx.commit();
			return comment;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServerComment updateComment(ServerComment comment) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			ServerComment c = pm.getObjectById(ServerComment.class,
					comment.getId());
			c.setDisplayName(comment.getDisplayName());
			c.setCommentedid(comment.getCommentedid());
			c.setDateDerniereModif(new Date());
			c.setLikeIds(comment.getLikeIds());
			c.setNotLikeIds(comment.getNotLikeIds());
			c.setPersonid(comment.getPersonid());
			c.setScore(comment.getScore());
			c.setText(comment.getText());
			tx.commit();
			return comment;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}

	}

	public void deleteComment(String id) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			ServerComment co = pm.getObjectById(ServerComment.class, id);

			pm.deletePersistent(co);

			tx.commit();

		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServerComment findCommentByKey(String id) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServerComment comment;
		try {
			comment = pm.getObjectById(ServerComment.class, id);
			comment.getLikeIds();
			comment.getNotLikeIds();
			return pm.detachCopy(comment);
		} finally {
			pm.close();
		}
	}

	public List<ServerComment> findCommentByObject(String objectId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String q = "select from " + ServerComment.class.getName()
				+ " order by " + "dateCreation desc";
		Query query = pm.newQuery(q);
		query.setFilter("objectId== \"" + objectId + "\"");
		try {
			@SuppressWarnings("unchecked")
			List<ServerComment> res = (List<ServerComment>) query.execute();
			res.size();
			return (List<ServerComment>) pm.detachCopyAll(res);

		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteCommentForUser(String userId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();

		Query query = pm.newQuery(ServerComment.class);
		query.setFilter("id== \"" + userId + "\"");

		try {
			tx.begin();
			List<ServerComment> res = (List<ServerComment>) query.execute();
			for (ServerComment u : res) {

				pm.deletePersistent(u);
			}
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public String saveFile(ServeurFile file) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			pm.makePersistent(file);
			tx.commit();
			return file.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurFile findFile(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServeurFile sf;
		try {
			sf = pm.getObjectById(ServeurFile.class, id);
			return pm.detachCopy(sf);
		} finally {
			pm.close();
		}
	}

	public String saveFolder(ServeurFolder folder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			pm.makePersistent(folder);
			folder.getFiles();
			folder.getFolders();
			tx.commit();
			return folder.getId();
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public ServeurFolder findFolder(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServeurFolder sf;
		try {
			sf = pm.getObjectById(ServeurFolder.class, id);
			sf.getFiles();
			sf.getFolders();
			return pm.detachCopy(sf);
		} finally {
			pm.close();
		}
	}

	public ServeurFolder updateFolder(ServeurFolder folder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			ServeurFolder sf = pm.getObjectById(ServeurFolder.class,
					folder.getId());
			sf.setAuthor(folder.getAuthor());
			sf.setDateDerniereModif(new Date());
			sf.setDirectory(folder.isDirectory());
			sf.setFiles(folder.getFiles());
			sf.setFolders(folder.getFolders());
			sf.setParentId(folder.getParentId());
			sf.setPublique(folder.isPublique());
			sf.setTitle(folder.getTitle());
			sf.setUserId(folder.getUserId());
			tx.commit();
			return folder;
		} finally {
			if (tx.isActive())
				tx.rollback();
			pm.close();
		}
	}

	public void deleteFolder(Folder folder) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public List<AbstractFichier> getSharedFiles(String userId, String parentId, String order) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<AbstractFichier> files = new ArrayList<AbstractFichier>();
		if (order == null || order.isEmpty())
			order = "title asc";
		String q1 = "select from " + ServeurFichier.class.getName()
				/*+ " order by " + order*/;
		Query query1 = pm.newQuery(q1);
		query1.setFilter("publique== true");
		String q2 = "select from " + ServeurDossier.class.getName()
				/*+ " order by " + order*/;
		Query query2 = pm.newQuery(q2);
		query2.setFilter("publique== true");
		try {
			List<ServeurFichier> res1 = (List<ServeurFichier>) query1.execute();
			res1.size();
			List<ServeurDossier> res2 = (List<ServeurDossier>) query2.execute();
			res2.size();
			files.addAll((List<ServeurDossier>) pm.detachCopyAll(res2));
			files.addAll((List<ServeurFichier>) pm.detachCopyAll(res1));
			return files;
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<AbstractFichier> getFilesByUser(String userId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<AbstractFichier> files = new ArrayList<AbstractFichier>();
		String order;
		order = "title asc";
		String filtre = "userId== \"" + userId + "\"";
		String q1 = "select from " + ServeurFichier.class.getName()
				+ " order by " + order;
		Query query1 = pm.newQuery(q1);
		query1.setFilter(filtre);
		String q2 = "select from " + ServeurDossier.class.getName()
				+ " order by " + order;
		Query query2 = pm.newQuery(q2);
		query2.setFilter(filtre);
		try {
			List<ServeurFichier> res1 = (List<ServeurFichier>) query1.execute();
			res1.size();
			List<ServeurDossier> res2 = (List<ServeurDossier>) query2.execute();
			res2.size();
			files.addAll((List<ServeurDossier>) pm.detachCopyAll(res2));
			files.addAll((List<ServeurFichier>) pm.detachCopyAll(res1));
			return files;
		} finally {
			pm.close();
		}
	}
}
