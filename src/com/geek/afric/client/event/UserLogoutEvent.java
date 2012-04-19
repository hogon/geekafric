package com.geek.afric.client.event;

import com.geek.afric.shared.User;
import com.google.gwt.event.shared.GwtEvent;

public class UserLogoutEvent extends GwtEvent<UserLogoutEventHandler> {
	
	private final User user;
	
	public static Type<UserLogoutEventHandler> TYPE = new Type<UserLogoutEventHandler>();
	
	
	public UserLogoutEvent(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	protected void dispatch(UserLogoutEventHandler handler) {
			handler.onUserLogout(this);	
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UserLogoutEventHandler> getAssociatedType() {
		return TYPE;
	}

}
