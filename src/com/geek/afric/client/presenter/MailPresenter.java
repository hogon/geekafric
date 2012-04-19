/**
 * 
 */
package com.geek.afric.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.geek.afric.client.AppManager;
import com.geek.afric.client.GeekServiceAsync;
import com.geek.afric.client.event.AcceuilEvent;
import com.geek.afric.client.event.FichierEvent;
import com.geek.afric.client.event.FichierListEvent;
import com.geek.afric.client.event.UserLogoutEvent;
import com.geek.afric.client.view.MailView.Listener;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.Mail;
import com.geek.afric.shared.User;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author Mcicheick
 *
 */
public class MailPresenter implements Presenter {

	public interface Display {
		void setListener(Listener listener);

		Widget asWidget();

		void setLoader(boolean load);

		void setError(String message);

		void setMails(ArrayList<Mail> result);

		void newMessage();

		String getSelectToken();

		void setTokens(Set<String> tokens);

		void setEmails(Set<String> mails);

		void setLeftFichiers(ArrayList<Fichier> files);
	}

	private final GeekServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private final User user;
	int startIndex = 0;
	String box = "all";
	String order;
	
	Map<String, ArrayList<Mail>> mapped = new HashMap<String, ArrayList<Mail>>();
	
	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 */
	public MailPresenter(GeekServiceAsync rpcService, HandlerManager eventBus,
			Display display, User user) {
		super();
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		this.user = user;
		order = "dateCreation desc";
		bind();
	}
	
	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 * @param startIndex
	 * @param box
	 */
	public MailPresenter(GeekServiceAsync rpcService,
			HandlerManager eventBus, Display display, User user, String box,
			int startIndex) {
		this(rpcService, eventBus, display, user);
		this.box = (box == null || box.isEmpty()) ? "all" : box;
		this.startIndex = startIndex;
	}

	private void bind() {
		display.setListener(new Listener() {
			
			@Override
			public void onClickSearch(String token) {
				if(token != null && !token.isEmpty())
					setSearch(token);
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
						Window.alert("Vous n'avez pas été déconnecté à cause des erreurs suivante:\n"
								+ caught.getMessage());
					}
				});
			}
			
			@Override
			public void onClickAcceuil() {
				eventBus.fireEvent(new AcceuilEvent());
			}
			
			@Override
			public SelectionHandler<Suggestion> getRechercheSelectHandler() {
				SelectionHandler<Suggestion> selection = new SelectionHandler<SuggestOracle.Suggestion>() {
					
					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						String token = display.getSelectToken();
						if(token != null && !token.isEmpty())
							setSearch(token);
					}
				};
				return selection;
			}

			@Override
			public void onClickSupprimer(final ArrayList<Mail> mailsToDelete) {
				display.setLoader(true);
				for(int i = 0; i < mailsToDelete.size(); i++) {
					Mail mail = mailsToDelete.get(i);
					if(user.isReceiver(mail))
						mail.setReceiverDelete(true);
					if(user.isSender(mail))
						mail.setSenderDelete(true);
				}
				rpcService.deleteMails(mailsToDelete, new AsyncCallback<ArrayList<Mail>>() {
					
					@Override
					public void onSuccess(ArrayList<Mail> result) {
						mapped.clear();
						setMails(box, order);
						display.setLoader(false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						display.setLoader(false);
					}
				});
			}

			@Override
			public void onClickEnvoyer(final Mail mail) {
				display.setLoader(true);
				rpcService.getUserByEmail(mail.getReceiver(), new AsyncCallback<User>() {
					
					@Override
					public void onSuccess(final User u) {
						if(u == null) {
							rpcService.sendMail(mail, new AsyncCallback<String>() {
								
								@Override
								public void onSuccess(String result) {
									display.setError("close");
									mapped.clear();
									setMails(box, order);
									display.setLoader(false);
								}
								
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Votre message n'a pas pu être envoyé à cause des erreurs suivante:\n"
											+ caught.getMessage());
									display.setLoader(false);
								}
							});
							return;
						}
						mail.setReceiverName(u.getDisplayname());
						rpcService.sendMail(mail, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								display.setError("close");
								mapped.clear();
								setMails(box, order);
								display.setLoader(false);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Votre message n'a pas pu être envoyé à cause des erreurs suivante:\n"
										+ caught.getMessage());
								display.setLoader(false);
							}
						});
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Votre message n'a pas pu être envoyé à cause des erreurs suivante:\n"
								+ caught.getMessage());
						display.setLoader(false);
					}
				});
			}

			@Override
			public void onItemSelected(final Mail item) {
				if(item.isRead())
					return;
				else if(user.isReceiver(item)) {
					item.setRead(true);
					rpcService.updateMail(item, new AsyncCallback<Mail>() {
						
						@Override
						public void onSuccess(Mail result) {
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("La mise à jour du mail n'a pas réussi à cause des erreurs suivante:\n"
									+ caught.getMessage());
						}
					});
				}
			}

			@Override
			public void onClickEnvoiDiff(final Integer jour, final Integer mois,
					final Integer annee, final Integer heure, final Integer min, final Mail mail) {
				display.setLoader(true);
				rpcService.getUserByEmail(mail.getReceiver(), new AsyncCallback<User>() {
					
					@Override
					public void onSuccess(User result) {
						if(result != null) {
							mail.setReceiverName(result.getDisplayname());
							AppManager.runTimer(jour, mois, annee, heure, min, mail, rpcService);
							display.setError("close");
							display.setLoader(false);
						} else {
							AppManager.runTimer(jour, mois, annee, heure, min, mail, rpcService);
							display.setError("close");
							display.setLoader(false);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Votre message n'a pas pu être envoyé à cause des erreurs suivante:\n"
								+ caught.getMessage());
						display.setLoader(false);
					}
				});
			}

			@Override
			public void onClickInbox() {
				setMails(box = "inbox", order = "dateCreation desc");
			}

			@Override
			public void onClickWrite() {
				display.newMessage();
			}

			@Override
			public void onClickSent() {
				setMails(box = "outbox", order = "dateCreation desc");
			}
			
			@Override
			public void update() {
				mapped.clear();
				setMails(box, order);
			}

			@Override
			public void onClickRoot() {
				setMails(box = "all", order = "dateCreation desc");
			}
			
			@Override
			public void onClickHeader(String order1) {
				setMails(box, order = order1);
			}
			
			@Override
			public void setEmails() {
				rpcService.getEmails(new AsyncCallback<Set<String>>() {

					@Override
					public void onSuccess(Set<String> result) {
						display.setEmails(result);
					}

					@Override
					public void onFailure(Throwable caught) {
					}
					
				});
			}

			@Override
			public void onClickTitle(Fichier file) {
				eventBus.fireEvent(new FichierEvent(file.getId()));
			}
		});
	}
	
	private void setMails(final String box, final String order) {
		display.setLoader(true);
		ArrayList<Mail> fromMap = mapped.get(box + order);
		if(fromMap != null) {
			display.setMails(fromMap);
			display.setLoader(false);
			return;
		}
		if(box.equals("all")) {
			rpcService.getMyMessages(user, order, new AsyncCallback<ArrayList<Mail>>() {
				
				@Override
				public void onSuccess(ArrayList<Mail> result) {
					display.setMails(result);
					mapped.put(box + order, result);
					setToken();
					display.setLoader(false);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Il y a eu un problème lors de la récupération des emails à cause des erreurs suivante:\n"
							+ caught.getMessage());
					display.setLoader(false);
				}
			});
		} else if(box.equals("inbox")) {
			rpcService.getMyReceivedMail(user, startIndex, order, new AsyncCallback<ArrayList<Mail>>() {
				
				@Override
				public void onSuccess(ArrayList<Mail> result) {
					display.setMails(result);
					mapped.put(box + order, result);
					setToken();
					display.setLoader(false);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Il y a eu un problème lors de la récupération des emails réçus à cause des erreurs suivante:\n"
							+ caught.getMessage());
					display.setLoader(false);
				}
			});
		} else if(box.equals("outbox")) {
			rpcService.getMySentMail(user, startIndex, order, new AsyncCallback<ArrayList<Mail>>() {
				
				@Override
				public void onSuccess(ArrayList<Mail> result) {
					display.setMails(result);
					mapped.put(box + order, result);
					setToken();
					display.setLoader(false);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Il y a eu un problème lors de la récupération des emails envoyés à cause des erreurs suivante:\n"
							+ caught.getMessage());
					display.setLoader(false);
				}
			});
		}
	}

	private void setSearch(String token) {
		ArrayList<Mail> fromMap = mapped.get(box + order);
		ArrayList<Mail> search = new ArrayList<Mail>();
		if(fromMap != null) {
			for(int i = 0; i < fromMap.size(); i++) {
				Mail m = fromMap.get(i);
				if(m.getObject().contains(token) ||
						m.getSenderName().contains(token) ||
						m.getReceiverName().contains(token)) {
					search.add(m);
				}	
			}
			display.setMails(search);
		}
	}
	
	private void setToken() {
		Set<String> tokens = new HashSet<String>();
		ArrayList<Mail> fromMap = mapped.get(box + order);
		if(fromMap != null) {
			for(int i = 0; i < fromMap.size(); i++) {
				Mail m = fromMap.get(i);
				tokens.add(m.getObject());
				tokens.add(m.getSenderName());
				tokens.add(m.getReceiverName());
			}
			display.setTokens(tokens);
		}
	}
	
	private void setLeftFichiers(final String order) {
		rpcService.getFilesByUserId(user.getId(), 0, order, new AsyncCallback<ArrayList<Fichier>>() {
			
			@Override
			public void onSuccess(ArrayList<Fichier> result) {
				display.setLeftFichiers(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Il y a eu un problème lors de la récuperation des fichiers: " +
						caught.getMessage());
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
		setMails(box, order);
		setLeftFichiers("dateCreation desc");
	}

}
