/**
 * 
 */
package com.geek.afric.shared;

import java.io.Serializable;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author Mcicheick
 *
 */
@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class AbstractFichier extends ObjetStandard implements Serializable {
	@Persistent
	protected String author;
	@Persistent
	protected String title;
	@Persistent
	protected String userId;
	@Persistent
	protected Boolean publique;
	@Persistent
	protected Boolean directory;
	@Persistent
	protected String parentId = null;
	@Persistent
	protected String path;
	
	public AbstractFichier() {
		super();
	}

	/**
	 * @param author
	 * @param title
	 * @param userId
	 * @param publique
	 * @param directory
	 */
	public AbstractFichier(String author, String title, String userId,
			Boolean publique, Boolean directory, String parentId) {
		super();
		this.author = author;
		this.title = title;
		this.userId = userId;
		this.publique = publique;
		this.directory = directory;
		this.parentId = parentId;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the publique
	 */
	public Boolean isPublique() {
		return publique;
	}

	/**
	 * @param publique the publique to set
	 */
	public void setPublique(Boolean publique) {
		this.publique = publique;
	}

	/**
	 * @return the directory
	 */
	public Boolean isDirectory() {
		return directory;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(Boolean directory) {
		this.directory = directory;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((directory == null) ? 0 : directory.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result
				+ ((publique == null) ? 0 : publique.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (!(obj instanceof AbstractFichier)) {
			return false;
		}
		AbstractFichier other = (AbstractFichier) obj;
		if (author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!author.equals(other.author)) {
			return false;
		}
		if (directory == null) {
			if (other.directory != null) {
				return false;
			}
		} else if (!directory.equals(other.directory)) {
			return false;
		}
		if (parentId == null) {
			if (other.parentId != null) {
				return false;
			}
		} else if (!parentId.equals(other.parentId)) {
			return false;
		}
		if (publique == null) {
			if (other.publique != null) {
				return false;
			}
		} else if (!publique.equals(other.publique)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}
	
	public String getInfoHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<b><u>Propriétés</u></b>");
		sb.append("<br>");
		sb.append("<b>Titre:</b> " + title);
		sb.append("<br>");
		sb.append("<b>Auteur:</b> " + author);
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	public String toString() {
		return "Titre: " + title
				+ "\n"
				+ "Auteur: " + author
				+ "\n"
				+ "Date création: " + dateCreation.toLocaleString()
				+ "\n"
				+ "Dernière modification: " + dateDerniereModif.toLocaleString()
				+ "\n"
				+ "Confidentialité: " + (publique ? "publique" : "privé")
				+ "\n"
				+ "Type: " + (directory ? "Dossier" : "Fichier")
				+ "\n" 
				+ "Chemin: " + ((path == null) ? "" : path) + "/";
	}
}
