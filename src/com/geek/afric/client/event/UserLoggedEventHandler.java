package com.geek.afric.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserLoggedEventHandler extends EventHandler  {
	
	void onUserLogged(UserLoggedEvent event);

}
