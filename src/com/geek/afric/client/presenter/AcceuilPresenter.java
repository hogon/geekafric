/**
 * 
 */
package com.geek.afric.client.presenter;

import java.util.ArrayList;
import java.util.Set;

import com.geek.afric.client.GeekServiceAsync;
import com.geek.afric.client.event.AcceuilEvent;
import com.geek.afric.client.event.FichierEvent;
import com.geek.afric.client.event.FichierListEvent;
import com.geek.afric.client.event.MailEvent;
import com.geek.afric.client.event.UserLogoutEvent;
import com.geek.afric.client.view.Acceuil.Listener;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.User;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Mcicheick
 *
 */
public class AcceuilPresenter implements Presenter {

	public interface Display{

		void setListener(Listener listener);

		void setTokens(Set<String> tokens);

		void setLeftFichiers(ArrayList<Fichier> files);

		String getSelectToken();

		Widget asWidget();

		void setFichiers(ArrayList<Fichier> files);

		void setLoader(boolean b);

		void setMailCount(int count);
		
	}
	
	private final GeekServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private final User user;
	
	
	
	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 */
	public AcceuilPresenter(GeekServiceAsync rpcService,
			HandlerManager eventBus, Display display, User user) {
		super();
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		this.user = user;
		bind();
	}

	private void bind() {
		display.setListener(new Listener() {
			
			@Override
			public void onClickSearch(String token) {
				if(token != null && !token.isEmpty())
					eventBus.fireEvent(new FichierListEvent(token, 0));
			}
			
			@Override
			public void onClickPartages() {
				eventBus.fireEvent(new FichierListEvent("shared", 0));
			}
			
			@Override
			public void onClickFichiers() {
				eventBus.fireEvent(new FichierListEvent(user.getId(), 0));
			}
			
			@Override
			public void onClickDeconnexion() {
				rpcService.logout(user, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						eventBus.fireEvent(new UserLogoutEvent(user));
						Window.setTitle("Geek afric");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Vous n'avez pas été déconnecté à cause des erreurs suivante: <br>"
								+ caught.getMessage());
					}
				});
			}
			
			@Override
			public void onClickAcceuil() {
				eventBus.fireEvent(new AcceuilEvent());
			}
			
			@Override
			public void onClickTitle(Fichier file) {
				if(file == null)
					eventBus.fireEvent(new FichierEvent(null));
				else
					eventBus.fireEvent(new FichierEvent(file.getId()));
			}
			
			@Override
			public void onClickShowFiles(String id) {
				eventBus.fireEvent(new FichierListEvent(user.getId(), 0));
			}

			@Override
			public SelectionHandler<Suggestion> getRechercheSelectHandler() {
				SelectionHandler<Suggestion> selection = new SelectionHandler<SuggestOracle.Suggestion>() {
					
					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						String token = display.getSelectToken();
						if(token != null && !token.isEmpty())
							eventBus.fireEvent(new FichierListEvent(token, 0));
					}
				};
				return selection;
			}

			@Override
			public void onClickMails() {
				eventBus.fireEvent(new MailEvent(user, 0, "inbox"));
			}
		});
	}
	
	private void setTokens() {
		rpcService.getSearchTokens(user, new AsyncCallback<Set<String>>() {
			
			@Override
			public void onSuccess(Set<String> result) {
				display.setTokens(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Il y a eu un problème lors de la récuperation des tokens de recherche: " +
						caught.getMessage());
			}
		});
	}
	
	private void setFichiers(final String order) {
		display.setLoader(true);
		rpcService.getFilesByUserId(user.getId(), 0, order, new AsyncCallback<ArrayList<Fichier>>() {
			
			@Override
			public void onSuccess(ArrayList<Fichier> result) {
				display.setFichiers(result);
				display.setLoader(false);
				Window.setTitle("Geek afric");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Il y a eu un problème lors de la récuperation des fichiers: " +
						caught.getMessage());
				display.setLoader(false);
			}
		});
	}
	
	private void setMailCount() {
		rpcService.getUnreadCount(user, new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer result) {
				display.setMailCount(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.setMailCount(0);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.geek.afric.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		setTokens();
		setFichiers("dateDerniereModif desc");
		setMailCount();
	}

}
