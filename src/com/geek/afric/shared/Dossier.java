/**
 * 
 */
package com.geek.afric.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Persistent;

/**
 * @author Mcicheick
 * 
 */
@SuppressWarnings("serial")
public class Dossier extends AbstractFichier implements Serializable {
	@Persistent
	List<Dossier> folders = new ArrayList<Dossier>();
	@Persistent
	List<Fichier> files = new ArrayList<Fichier>();
	
	public Dossier() {
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
	public Dossier(String author, String title, String userId, Boolean publique,
			Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		setDirectory(true);
	}

	/**
	 * @return the folders
	 */
	public List<Dossier> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(ArrayList<Dossier> folders) {
		this.folders = folders;
	}

	/**
	 * @return the files
	 */
	public List<Fichier> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<Fichier> files) {
		this.files = files;
	}
	
	/**
	 * add the file in folder
	 */
	public boolean add(AbstractFichier file) {
		if(file == null) {
			return false;
		}
		if(contains(file.getTitle())) {
			return false;
		}
		if(file instanceof Fichier) {
			return files.add((Fichier) file);
		} 
		if(file instanceof Dossier) {
			return folders.add((Dossier) file);
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
		if(file instanceof Fichier) {
			for(int i = 0; i < files.size(); i++) {
				if(files.get(i).getId().equals(file.getId()))
					return files.remove(i) != null;
			}
		}
		if(file instanceof Dossier) {
			for(int i = 0; i < folders.size(); i++) {
				if(folders.get(i).getId().equals(file.getId()))
					return folders.remove(i) != null;
			}
		}
		return false;
	}

	public boolean removeAll(ArrayList<AbstractFichier> files) {
		if (files == null || files.isEmpty())
			return false;
		boolean b = true;
		for(int i = 0; i < files.size(); i++) {
			b = b && remove(files.get(i));
		}
		return b;
	}

	public boolean contains(AbstractFichier file) {
		if(file.getId() == null)
			return false;
		if (file.getId().equals(getId()))
			return false;
		if (files.isEmpty() && folders.isEmpty()) {
			return false;
		}

		for (int i = 0; i < files.size(); i++) {
			if (file.getId().equals(files.get(i)))
				return true;
		}

		for (int i = 0; i < folders.size(); i++) {
			if (file.getId().equals(folders.get(i).getId()))
				return true;
		}
		return false;
	}

	public String toString() {
		return super.toString()
				+ "\n"
				+ ((files.size() <= 1) ? files.size() + " fichier" : files.size() + " fichiers") 
				+ "\n"
				+ ((folders.size() <= 1) ? folders.size() + " dossier" : folders.size() + " dossiers");
	}

	public ArrayList<AbstractFichier> getAllFiles() {
		ArrayList<AbstractFichier> files = new ArrayList<AbstractFichier>();
		files.addAll(getFiles());
		files.addAll(getFolders());
		return files;
	}
	
	public boolean contains(String title) {
		if(title == null || title.isEmpty()) {
			return false;
		}
		for(int i = 0; i < files.size(); i++) {
			if(title.equals(files.get(i).getTitle()))
				return true;
		}
		for(int i = 0; i < folders.size(); i++) {
			if(title.equals(folders.get(i).getTitle()))
				return true;
		}
		return false;
	}
	
	public boolean isChild(AbstractFichier file) {
		if(file.getId() == null)
			return false;
		if (file.getId().equals(getId()))
			return false;
		if (files.isEmpty() && folders.isEmpty()) {
			return false;
		}

		for (int i = 0; i < files.size(); i++) {
			if (file.getId().equals(files.get(i)))
				return true;
		}
		
		for (int i = 0; i < folders.size(); i++) {
			if (file.getId().equals(folders.get(i).getId()))
				return true;
			 boolean child = folders.get(i).isChild(file);
			 if(child) {
				 return true;
			 }
		}
		return false;
	}
}
