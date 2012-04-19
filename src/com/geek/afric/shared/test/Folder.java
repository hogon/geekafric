/**
 * 
 */
package com.geek.afric.shared.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class Folder extends AbstractFichier implements Serializable {
	@Persistent
	List<Folder> folders = new ArrayList<Folder>();
	@Persistent
	List<File> files = new ArrayList<File>();
	
	public Folder() {
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
	public Folder(String author, String title, String userId, Boolean publique,
			Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		setDirectory(true);
	}

	/**
	 * @return the folders
	 */
	public List<Folder> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(ArrayList<Folder> folders) {
		this.folders = folders;
	}

	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}
	
	/**
	 * add the file in folder
	 */
	public boolean add(AbstractFichier file) {
		if(file == null) {
			return false;
		}
		if(file instanceof File) {
			return files.add((File) file);
		} 
		if(file instanceof Folder) {
			return folders.add((Folder) file);
		}
		return false;
	}
	
	/**
	 * remove the file in folder
	 */
	public boolean remove(AbstractFichier file) {
		if(file == null){
			return false;
		}
		if(file instanceof File) {
			return files.remove((File) file);
		}
		if(file instanceof Folder) {
			return folders.remove((Folder) file);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		result = prime * result + ((folders == null) ? 0 : folders.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Folder)) {
			return false;
		}
		Folder other = (Folder) obj;
		if (files == null) {
			if (other.files != null) {
				return false;
			}
		} else if (!files.equals(other.files)) {
			return false;
		}
		if (folders == null) {
			if (other.folders != null) {
				return false;
			}
		} else if (!folders.equals(other.folders)) {
			return false;
		}
		return true;
	}
	
}
