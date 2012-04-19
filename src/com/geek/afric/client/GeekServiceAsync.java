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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Mcicheick
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GeekServiceAsync {

	void login(String email, String password, AsyncCallback<User> callback);

	void getUser(String id, AsyncCallback<User> callback);

	void deleteFile(String fileId, AsyncCallback<Void> callback);

	void deleteUser(String userId, AsyncCallback<Void> callback);

	void getFichier(String fileId, AsyncCallback<Fichier> callback);

	void getFilesByUserId(String userId,
			AsyncCallback<ArrayList<Fichier>> callback);

	void getFileShared(AsyncCallback<ArrayList<Fichier>> callback);

	void saveFile(Fichier user, AsyncCallback<String> callback);

	void saveUser(User user, AsyncCallback<User> callback);

	void updateFile(Fichier user, AsyncCallback<Fichier> callback);

	void updateUser(User user, AsyncCallback<User> callback);

	void getUserFromSession(String id, AsyncCallback<User> callback);

	void getUserByEmail(String email, AsyncCallback<User> callback);

	void logout(User user, AsyncCallback<Void> callback);

	void searchByToken(User user, String token, AsyncCallback<ArrayList<Fichier>> callback);

	void getSearchTokens(User user, AsyncCallback<Set<String>> callback);

	void searchInFiles(User user, String token,
			AsyncCallback<ArrayList<Fichier>> callback);

	void deleteFiles(ArrayList<AbstractFichier> files, AsyncCallback<Void> callback);

	void getFilesByUserId(String userId, int startIndex, String order,
			AsyncCallback<ArrayList<Fichier>> callback);

	void clearCache(User user, AsyncCallback<Void> callback);

	void saveDossier(Dossier dossier, AsyncCallback<String> callback);

	void updateDossier(Dossier dossier, AsyncCallback<Dossier> callback);

	void getDossier(String dossierId, AsyncCallback<Dossier> callback);

	void deleteDossier(Dossier dossier, AsyncCallback<Void> callback);

	void getDossierByUser(User user, String order,
			AsyncCallback<ArrayList<Dossier>> callback);

//	void getAllFiles(User user, String order,
//			AsyncCallback<ArrayList<AbstractFichier>> callback);
//
//	void getFilesByDossier(String dossierId, String order,
//			AsyncCallback<ArrayList<AbstractFichier>> callback);

	void retrievePassWord(User user, AsyncCallback<Void> callback);

	void getMail(String id, AsyncCallback<Mail> callback);

	void getMyMessages(User user, int lastIndex,
			AsyncCallback<ArrayList<Mail>> callback);

	void getMyMessages(User user, String order, AsyncCallback<ArrayList<Mail>> callback);

	void saveMessage(Mail mail, AsyncCallback<String> callback);

	void sendMail(Mail mail, AsyncCallback<String> callback);

	void updateMail(Mail mail, AsyncCallback<Mail> callback);

	void getMyReceivedMail(User user, int startIndex, String order,
			AsyncCallback<ArrayList<Mail>> callback);

	void getMySentMail(User user, int startIndex, String order,
			AsyncCallback<ArrayList<Mail>> callback);

	void deleteMails(ArrayList<Mail> mails, AsyncCallback<ArrayList<Mail>> callback);

	void deleteMail(Mail mail, AsyncCallback<Mail> callback);

	void updatePassword(User user, String password, AsyncCallback<User> asyncCallback);

	void confirmEmail(User user, AsyncCallback<Void> callback);

	void getUnreadCount(User user, AsyncCallback<Integer> callback);

	void getEmails(AsyncCallback<Set<String>> asyncCallback);

	void getObjectComments(String objectId,
			AsyncCallback<ArrayList<Comment>> callback);

	void addComment(Fichier file, Comment comment,
			AsyncCallback<Comment> callback);

	void deleteComment(Fichier file, Comment comment,
			AsyncCallback<Comment> callback);

	void saveFolder(Folder folder, AsyncCallback<String> callback);

	void getFolder(String id, AsyncCallback<Folder> callback);

	void test(User user, AsyncCallback<Void> callback);

	void getFiles(String userId, String parentId, String order,
			AsyncCallback<ArrayList<AbstractFichier>> callback);

	void searchFiles(User user, String token,
			AsyncCallback<ArrayList<AbstractFichier>> callback);

	void getFilesByUser(String userId,
			AsyncCallback<ArrayList<AbstractFichier>> callback);

	void updateAll(Dossier dossier, ArrayList<AbstractFichier> selectedFiles,
			AsyncCallback<Void> callback);
}
