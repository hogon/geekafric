/**
 * 
 */
package com.geek.afric.server;

import java.io.Serializable;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.geek.afric.shared.AbstractFichier;
import com.google.appengine.api.datastore.Text;

/**
 * @author Mcicheick
 * 
 */
@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ServeurFichier extends AbstractFichier implements Serializable {
	@Persistent
	Text contenu;
	@Persistent
	ServeurDossier folder;
	
	public ServeurFichier() {
		super();
		setDirectory(false);
	}

	/**
	 * @param author
	 * @param title
	 * @param userId
	 * @param publique
	 * @param directory
	 * @param parentId
	 */
	public ServeurFichier(String author, String title, Text contenu, String userId,
			Boolean publique, Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		this.contenu = contenu;
		setDirectory(false);
	}

	/**
	 * @return the content
	 */
	public Text getContenu() {
		return contenu;
	}

	/**
	 * @param content the content to set
	 */
	public void setContenu(Text contenu) {
		this.contenu = contenu;
	}

	/**
	 * @return the folder
	 */
	public ServeurDossier getFolder() {
		return folder;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(ServeurDossier foldier) {
		this.folder = foldier;
	}
}
