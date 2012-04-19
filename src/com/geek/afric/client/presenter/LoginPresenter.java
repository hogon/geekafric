package com.geek.afric.client.presenter;

import java.util.Date;

import com.geek.afric.client.GeekServiceAsync;
import com.geek.afric.client.event.UserLoggedEvent;
import com.geek.afric.client.view.Login.Listener;
import com.geek.afric.shared.User;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginPresenter implements Presenter {

	public interface Display {
		void setListener(Listener listener);

		Widget asWidget();

		void setError(String error);

		void setConfirmation(String message);

		void setLoginLoader(boolean load);

		void setDefaultLoader(boolean load);

		void close();
	}

	private final GeekServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 */
	public LoginPresenter(GeekServiceAsync rpcService, HandlerManager eventBus,
			Display display) {
		super();
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	private void bind() {
		display.setListener(new Listener() {

			@Override
			public void onClickSubmit(String email, String password) {
				display.setLoginLoader(true);
				rpcService.login(email, password, new AsyncCallback<User>() {

					@Override
					public void onSuccess(User result) {
						if (result == null) {
							display.setError("Login ou mot de passe incorrect");
							display.setLoginLoader(false);
							return;
						}
						if(result.getId() == null) {
							Window.alert(result.getPrenom() + " Votre email n'a pas été confirmé. Rendez vous sur votre boite email.\n" +
									"Et cliquez sur le lien qu'on vous a envoyé lors de l'inscriprion.\n" +
									"Merci pour votre compréhension");
							display.setError("");
							display.setLoginLoader(false);
							return;
						}
						final long DURATION = 1000 * 60 * 60 * 24 * 7 * 2; // Le
																			// cookie
																			// dure
																			// une
																			// semaine
						Date expires = new Date(System.currentTimeMillis()
								+ DURATION);
						Cookies.setCookie("sid", result.getId(), expires, null,
								"/", false);
						eventBus.fireEvent(new UserLoggedEvent(result));
						display.setLoginLoader(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						display.setError("Login ou mot de passe incorrect");
						display.setLoginLoader(false);
					}
				});
			}

			@Override
			public void onClickDialogValidateForgot(String email) {
				display.setDefaultLoader(true);
				rpcService.getUserByEmail(email, new AsyncCallback<User>() {

					@Override
					public void onSuccess(final User user) {
						if (user == null) {
							display.setDefaultLoader(false);
							Window.alert("User not found");
							return;
						}
						rpcService.retrievePassWord(user, new AsyncCallback<Void>() {
							
							@Override
							public void onSuccess(Void result) {
								display.setConfirmation("Nous venons de vous envoyer un email "
										+ "pour réinitiaser votre mot de passe");
								display.setDefaultLoader(false);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								display.setDefaultLoader(false);
								Window.alert("Il s'est produit un problème lors de l'envoie de l'email de réinitialisation");
							}
						});
					}

					@Override
					public void onFailure(Throwable caught) {
						display.setDefaultLoader(false);
						Window.alert("Il s'est produit un problème lors de la recherche de l'utilisateur");
					}
				});
			}

			@Override
			public void onClickValidateIns(final User user) {
				display.setDefaultLoader(true);
				rpcService.saveUser(user, new AsyncCallback<User>() {

					@Override
					public void onSuccess(User result) {
						// TODO Auto-generated method stub
						if (result == null) {
							display.setError("Ce nom d'utilisateur est utilisé");
							display.setDefaultLoader(false);
							return;
						}
						rpcService.confirmEmail(result, new AsyncCallback<Void>() {
							
							@Override
							public void onSuccess(Void result) {
								
								Window.alert("Nous venons de vous envoyer un email "
										+ "pour confirmer votre email");
								display.close();
								display.setDefaultLoader(false);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								display.setDefaultLoader(false);
								Window.alert("Il s'est produit un problème lors de l'envoie de l'email de confirmation");
							}
						});
					}

					@Override
					public void onFailure(Throwable caught) {
						display.setDefaultLoader(false);
						Window.alert("Il s'est produit un problème lors de l'inscription");
					}
				});
			}

		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
