package com.geek.afric.client;

import com.geek.afric.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Cheick Mahady SISSOKO
 */
public class GeekAfric implements EntryPoint {

	private User user;
	private final GeekServiceAsync rpcService = GWT
			.create(GeekService.class);
	private final HandlerManager eventBus = new HandlerManager(null);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		com.google.gwt.user.client.Element loading = DOM
				.getElementById("loading");
		DOM.removeChild(RootPanel.getBodyElement(), loading);
		String sessionID = Cookies.getCookie("sid");
		if (sessionID == null) {
			user = null;
			AppManager appViewer = new AppManager(eventBus, rpcService,
					RootPanel.get("top"), RootPanel.get("left"), user);
			appViewer.go(RootPanel.get("content"));
		} else {
			rpcService.getUserFromSession(sessionID, new AsyncCallback<User>() {

				@Override
				public void onSuccess(User result) {
					user = result;

					AppManager appViewer = new AppManager(eventBus,
							rpcService, RootPanel.get("top"), RootPanel
									.get("left"), user);

					if (user != null) {
						//TODO
					}
					appViewer.go(RootPanel.get("content"));
				}

				@Override
				public void onFailure(Throwable caught) {
					user = null;
					AppManager appViewer = new AppManager(eventBus,
							rpcService, RootPanel.get("top"), RootPanel
									.get("left"), user);
					appViewer.go(RootPanel.get("content"));
				}
			});
		}
	}
}
