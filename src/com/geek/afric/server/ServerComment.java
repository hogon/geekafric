package com.geek.afric.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.geek.afric.shared.ObjetStandard;
import com.google.appengine.api.datastore.Text;


@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ServerComment extends ObjetStandard implements Serializable {

	@Persistent
	private String userId;
	@Persistent
	private int score;
	@Persistent
	private String displayName;
	@Persistent
	private Text text;
	@Persistent
	private String objectId;
	@Persistent(defaultFetchGroup = "true")
	@Element(dependent = "true")
	private List<String> likeIds = new ArrayList<String>();
	@Persistent(defaultFetchGroup = "true")
	@Element(dependent = "true")
	private List<String> notLikeIds = new ArrayList<String>();
	
	public ServerComment(){
		super();
	}
	
	public ServerComment(String personid,String displayName,int score,
			Text text, String commentedid,List<String> likeIds,List<String> notLikeIds){
		super();
		this.userId=personid;
		this.displayName=displayName;
		this.score=score;
		this.text=text;
		this.objectId=commentedid;
		this.likeIds = likeIds;
		this.notLikeIds = notLikeIds;
	}
	
	public String getPersonid() {
		return userId;
	}
	public void setPersonid(String personid) {
		this.userId = personid;
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
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}

	public String getCommentedid() {
		return objectId;
	}
	public void setCommentedid(String commentedid) {
		this.objectId = commentedid;
	}
	/**
	 * @return the likeIds
	 */
	public List<String> getLikeIds() {
		return likeIds;
	}

	/**
	 * @param likeIds the likeIds to set
	 */
	public void setLikeIds(List<String> likeIds) {
		this.likeIds = likeIds;
	}

	/**
	 * @return the notLikeIds
	 */
	public List<String> getNotLikeIds() {
		return notLikeIds;
	}

	/**
	 * @param notLikeIds the notLikeIds to set
	 */
	public void setNotLikeIds(List<String> notLikeIds) {
		this.notLikeIds = notLikeIds;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		ServerComment other = (ServerComment) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
}
