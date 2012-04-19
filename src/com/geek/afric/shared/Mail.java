package com.geek.afric.shared;

import java.io.Serializable;

/**
 * 
 * @author Baya Demba, Yacouba Sagara
 * Model présentant un utilisateur
 *
 */

@SuppressWarnings("serial")
public class Mail extends ObjetStandard implements Serializable{
	
	private String sender;
	private String receiver;
	private String object;
	private String message;
	private boolean read,senderDelete,receiverDelete;
	/**
	 * @param receiverDelete the receiverDelete to set
	 */
	public void setReceiverDelete(boolean receiverDelete) {
		this.receiverDelete = receiverDelete;
	}

	private String senderName = "Sender";
	private String receiverName = "Receiver";
	
	public Mail() {
	}
	
	public Mail(String sender, String receiver, String object, String message) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.object = object;
		this.message = message;
		this.read = false;
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
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
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
	 * return true if mail can delete i.e sender and receiver delete it.
	 */
	public boolean canDelete(){
		return senderDelete && receiverDelete;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		if (!(obj instanceof Mail)) {
			return false;
		}
		Mail other = (Mail) obj;
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
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
	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		String mail = getMessage();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<br><hr>");
		stringBuilder.append(getDateCreation().toLocaleString());
		stringBuilder.append(" ");
		stringBuilder.append(getSenderName());
		stringBuilder.append(" wrote:<br><br>");
		stringBuilder.append(getMessage());
		mail = stringBuilder.toString();
		return mail;
	}

	public String getContentResume() {
		String resume = "";
		resume = this.message.substring(0,Math.min(100,message.length()));
		return resume+"...";
	}
	

}
