package com.geek.afric.client;

import java.util.ArrayList;
import java.util.Set;

import com.geek.afric.shared.AbstractFichier;
import com.geek.afric.shared.Comment;
import com.geek.afric.shared.Dossier;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.Mail;
import com.geek.afric.shared.User;
import com.geek.afric.shared.test.Folder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author Cheick Mahady SISSOKO
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GeekService extends RemoteService {
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static GeekServiceAsync instance;

		public static GeekServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(GeekService.class);
			}
			return instance;
		}
	}
	
	User login(String email, String password);
	
	User getUser(String id);
	
	Fichier getFichier(String fileId);
	
	ArrayList<Fichier> getFilesByUserId(String userId);
	
	ArrayList<Fichier> getFileShared();
	
	User saveUser(User user);
	
	User updateUser(User user);
	
	void deleteUser(String userId);
	
	String saveFile(Fichier user);
	
	Fichier updateFile(Fichier user);
	
	void deleteFile(String fileId);

	User getUserFromSession(String id);

	User getUserByEmail(String email);

	void logout(User user);

	ArrayList<Fichier> searchByToken(User user, String token);

	Set<String> getSearchTokens(User user);

	ArrayList<Fichier> searchInFiles(User user, String token);

	void deleteFiles(ArrayList<AbstractFichier> files);

	ArrayList<Fichier> getFilesByUserId(String userId, int startIndex,
			String order);

	void clearCache(User user);

	String saveDossier(Dossier dossier);

	Dossier updateDossier(Dossier dossier);

	Dossier getDossier(String dossierId);

	void deleteDossier(Dossier dossier);

	ArrayList<Dossier> getDossierByUser(User user, String order);

//	ArrayList<AbstractFichier> getAllFiles(User user, String order);
//
//	ArrayList<AbstractFichier> getFilesByDossier(String dossierId, String order);

	void retrievePassWord(User user);

	String sendMail(Mail mail);

	String saveMessage(Mail mail);

	Mail updateMail(Mail mail);

	ArrayList<Mail> getMyMessages(User user, String order);

	ArrayList<Mail> getMyMessages(User user, int startIndex);

	Mail getMail(String id);

	ArrayList<Mail> getMyReceivedMail(User user, int startIndex, String order);

	ArrayList<Mail> getMySentMail(User user, int startIndex, String order);

	ArrayList<Mail> deleteMails(ArrayList<Mail> mails);

	Mail deleteMail(Mail mail);

	User updatePassword(User user, String password);

	void confirmEmail(User user);

	int getUnreadCount(User user);

	Set<String> getEmails();

	ArrayList<Comment> getObjectComments(String objectId);

	Comment addComment(Fichier file, Comment comment);

	Comment deleteComment(Fichier file, Comment comment);

	String saveFolder(Folder folder);

	Folder getFolder(String id);

	void test(User user);

	ArrayList<AbstractFichier> getFiles(String userId, String parentId, String order);

	ArrayList<AbstractFichier> searchFiles(User user, String token);

	ArrayList<AbstractFichier> getFilesByUser(String userId);

	void updateAll(Dossier dossier, ArrayList<AbstractFichier> selectedFiles);
}
