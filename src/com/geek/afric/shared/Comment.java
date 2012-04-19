package com.geek.afric.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class Comment extends ObjetStandard implements Serializable {
	private String userId;
	private int score;
	private String displayName;
	private String text;
	private Date dateCreation = new Date();
	private String objectId;
	private List<String> likeIds = new ArrayList<String>();
	private List<String> notLikeIds = new ArrayList<String>();

	// int nbLike = 0;
	// int nbNotLike = 0;

	public Comment() {
		super();
	}

	public Comment(String userId, String displayName, String text,
			String commentedid) {
		super();
		this.userId = userId;
		this.displayName = displayName;
		this.text = text;
		this.objectId = commentedid;
	}

	public Comment(String userId, String displayName, int score,
			String text, Date dateCreation, String commentedid,
			List<String> likeIds, List<String> notLikeIds) {
		super();
		this.userId = userId;
		this.displayName = displayName;
		this.score = score;
		this.text = text;
		this.dateCreation = dateCreation;
		this.objectId = commentedid;
		this.likeIds = likeIds;
		this.notLikeIds = notLikeIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void increScore() {
		this.score = this.score + 1;
	}

	public void decreScore() {
		this.score = this.score - 1;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getCommentedid() {
		return objectId;
	}

	public void setCommentedid(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the likeIds
	 */
	public List<String> getLikeIds() {
		return likeIds;
	}

	/**
	 * @param likeIds
	 *            the likeIds to set
	 */
	public void setLikeIds(List<String> likeIds) {
		// nbLike = likeIds.size();
		this.likeIds = likeIds;
	}

	/**
	 * 
	 * @param personId
	 */
	public void addLike(String personId) {
		final List<String> likes = new ArrayList<String>(likeIds);
		if (!likeIds.contains(personId)) {
			if (likes.add(personId))
				increScore();
			if (notLikeIds.remove(personId))
				decreScore();
			setLikeIds(likes);
		}
	}

	/**
	 * @return the notLikeIds
	 */
	public List<String> getNotLikeIds() {
		return notLikeIds;
	}

	/**
	 * @param notLikeIds
	 *            the notLikeIds to set
	 */
	public void setNotLikeIds(List<String> notLikeIds) {
		// nbNotLike = notLikeIds.size();
		this.notLikeIds = notLikeIds;
	}

	/**
	 * 
	 * @param personId
	 */
	public void addNotLike(String personId) {
		final List<String> notLikes = new ArrayList<String>(notLikeIds);
		if (!notLikeIds.contains(personId)) {
			if (notLikes.add(personId))
				decreScore();
			if (likeIds.remove(personId))
				increScore();
			setNotLikeIds(notLikes);
		}
	}

	/**
	 * @return the nbLike
	 */
	public int getNbLike() {
		return likeIds.size();
	}

	/**
	 * @return the nbNotLike
	 */
	public int getNbNotLike() {
		return notLikeIds.size();
	}

	public int getRealScore() {
		return 2 + getNbLike() - getNbNotLike();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

}
