/**
 * 
 */
package com.geek.afric.client.event;

import com.geek.afric.shared.User;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Mcicheick
 *
 */
public class MailEvent extends GwtEvent<MailEventHandler> {
	
	private final User user;
	private final int startIndex;
	private final String box;
	public MailEvent(User user) {
		this(user, 1, "inbox");
	}
	public MailEvent(User user, int startIndex, String box) {
		super();
		this.user = user;
		this.startIndex = startIndex;
		this.box = box;
	}
	/**
	 * 
	 * @return user
	 */

	public User getUser() {
		return user;
	}
	
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	/**
	 * @return the box
	 */
	public String getBox() {
		return box;
	}


	public static Type<MailEventHandler> TYPE = new Type<MailEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MailEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MailEventHandler handler) {
		handler.doMailView(this);
	}

}
