/**
 * 
 */
package com.geek.afric.server.test;

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
public class ServeurFile extends AbstractFichier implements Serializable {
	
	@Persistent
	Text content;
	@Persistent
	ServeurFolder folder;
	
	public ServeurFile() {
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
	public ServeurFile(String author, String title, Text content, String userId,
			Boolean publique, Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		this.content = content;
		setDirectory(false);
	}

	/**
	 * @return the content
	 */
	public Text getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Text content) {
		this.content = content;
	}

	/**
	 * @return the foldier
	 */
	public ServeurFolder getFolder() {
		return folder;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(ServeurFolder foldier) {
		this.folder = foldier;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((folder == null) ? 0 : folder.hashCode());
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
		if (!(obj instanceof ServeurFile)) {
			return false;
		}
		ServeurFile other = (ServeurFile) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (folder == null) {
			if (other.folder != null) {
				return false;
			}
		} else if (!folder.equals(other.folder)) {
			return false;
		}
		return true;
	}
	
}
