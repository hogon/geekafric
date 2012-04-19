/**
 * 
 */
package com.geek.afric.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mcicheick
 *
 */
@SuppressWarnings("serial")
public class Fichier extends AbstractFichier implements Serializable {
	private String contenu;
	private ArrayList<String> commentIds = new ArrayList<String>();
	public Fichier() {
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
	public Fichier(String author, String title, String contenu, String userId,
			Boolean publique, Boolean directory, String parentId) {
		super(author, title, userId, publique, directory, parentId);
		this.contenu = contenu;
		setDirectory(false);
	}
	
	/**
	 * @param file
	 */
	public Fichier(Fichier file) {
		this(file.getAuthor(), file.getTitle(), file.getContenu(), file.getUserId(), 
				file.isPublique(), file.isDirectory(), file.getParentId());
	}
	/**
	 * @return the contenu
	 */
	public String getContenu() {
		return contenu;
	}
	
	/**
	 * @param contenu the contenu to set
	 */
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contenu == null) ? 0 : contenu.hashCode());
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
		if (!(obj instanceof Fichier)) {
			return false;
		}
		Fichier other = (Fichier) obj;
		if (contenu == null) {
			if (other.contenu != null) {
				return false;
			}
		} else if (!contenu.equals(other.contenu)) {
			return false;
		}
		return true;
	}

	public void addComment(String id) {
		if(commentIds.contains(id))
			return;
		commentIds.add(id);
	}
	public void deleteComment(Comment comment) {
		commentIds.remove(comment.getId());
	}
	
	public ArrayList<String> getCommentIds() {
		return commentIds;
	}
	
	public void setCommentIds(ArrayList<String> commentIds) {
		this.commentIds = commentIds;
	}
}
