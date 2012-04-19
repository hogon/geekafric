/**
 * 
 */
package com.geek.afric.shared.test;

import java.io.Serializable;

import com.geek.afric.shared.AbstractFichier;

/**
 * @author Mcicheick
 *
 */
@SuppressWarnings("serial")
public class File extends AbstractFichier implements Serializable {
	String content;
	public File() {
		super();
		setDirectory(false);
	}
	
	/**
	 * @param author
	 * @param title
	 * @param content
	 * @param userId
	 * @param publique
	 * @param directory
	 * @param parentId
	 */
	public File(String author, String title, String content, String userId,
			Boolean publique, Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		this.content = content;
		setDirectory(false);
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		if (!(obj instanceof File)) {
			return false;
		}
		File other = (File) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return content;
	}
}
