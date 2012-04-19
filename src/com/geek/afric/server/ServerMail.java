package com.geek.afric.server;

import java.io.Serializable;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.geek.afric.shared.ObjetStandard;
import com.google.appengine.api.datastore.Text;

/**
 * 
 * @author Cheick Mahady Sissoko
 * Model présentant un utilisateur
 *
 */

@SuppressWarnings("serial")
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ServerMail extends ObjetStandard implements Serializable{
	
	@Persistent
	private String sender;
	@Persistent
	private String receiver;
	@Persistent
	private String object;
	@Persistent
	private Text message;
	@Persistent
	private boolean read,senderDelete,receiverDelete;
	@Persistent
	private String senderName;
	@Persistent
	private String receiverName;
	
	
	public ServerMail() {
	}
	
	public ServerMail(String sender, String receiver, String object, Text message) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.object = object;
		this.message = message;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the object
	 */
	public String getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}

	/**
	 * @return the message
	 */
	public Text getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(Text message) {
		this.message = message;
	}

	/**
	 * @return the read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read the read to set
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * @return the senderDelete
	 */
	public boolean isSenderDelete() {
		return senderDelete;
	}

	/**
	 * @param senderDelete the senderDelete to set
	 */
	public void setSenderDelete(boolean senderDelete) {
		this.senderDelete = senderDelete;
	}

	/**
	 * @return the receiverDelete
	 */
	public boolean isReceiverDelete() {
		return receiverDelete;
	}

	/**
	 * @param receiverDelete the receiverDelete to set
	 */
	public void setReceiverDelete(boolean receiverDelete) {
		this.receiverDelete = receiverDelete;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		if (!(obj instanceof ServerMail)) {
			return false;
		}
		ServerMail other = (ServerMail) obj;
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		if (object == null) {
			if (other.object != null) {
				return false;
			}
		} else if (!object.equals(other.object)) {
			return false;
		}
		if (receiver == null) {
			if (other.receiver != null) {
				return false;
			}
		} else if (!receiver.equals(other.receiver)) {
			return false;
		}
		if (sender == null) {
			if (other.sender != null) {
				return false;
			}
		} else if (!sender.equals(other.sender)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerMail [sender=" + sender + ", receiver=" + receiver
				+ ", object=" + object + ", message=" + message + "]";
	}

}

