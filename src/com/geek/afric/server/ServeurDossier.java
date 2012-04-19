/**
 * 
 */
package com.geek.afric.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.geek.afric.shared.AbstractFichier;

/**
 * @author Mcicheick
 *
 */
@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ServeurDossier extends AbstractFichier implements Serializable {
	
	@Persistent(defaultFetchGroup = "false")
	@Element(dependent = "true")
	List<String> folders = new ArrayList<String>();
	
	@Persistent(defaultFetchGroup = "false")
	@Element(dependent = "true")
	List<String> files = new ArrayList<String>();
	
	public ServeurDossier() {
		super();
		setDirectory(true);
	}

	/**
	 * @param author
	 * @param title
	 * @param userId
	 * @param publique
	 * @param directory
	 * @param parentId
	 */
	public ServeurDossier(String author, String title, String userId,
			Boolean publique, Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		setDirectory(true);
	}

	/**
	 * @return the folders
	 */
	public List<String> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List<String> folders) {
		this.folders = folders;
	}

	/**
	 * @return the files
	 */
	public List<String> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<String> files) {
		this.files = files;
	}
}
