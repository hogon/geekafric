package com.geek.afric.client.event;

import com.geek.afric.shared.User;
import com.google.gwt.event.shared.GwtEvent;

public class UserLoggedEvent extends GwtEvent<UserLoggedEventHandler> {
	
	private final User user;
	
	public static Type<UserLoggedEventHandler> TYPE = new Type<UserLoggedEventHandler>();
	
	
	public UserLoggedEvent(User user) {
		super();
		this.user = user;
	}
	

	public User getUser() {
		return user;
	}


	@Override
	protected void dispatch(UserLoggedEventHandler handler) {
		handler.onUserLogged(this);	
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UserLoggedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
