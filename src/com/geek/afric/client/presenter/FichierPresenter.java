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
import com.geek.afric.client.event.UserLogoutEvent;
import com.geek.afric.client.view.FichierView.Listener;
import com.geek.afric.shared.Comment;
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
public class FichierPresenter implements Presenter {

	public interface Display {

		void setListener(Listener listener);

		void close(boolean load);

		void setFichier(Fichier file);

		void setSaveLoader(boolean load);

		void setError(String error);

		Widget asWidget();

		void setloadLoder(boolean load);

		String getSelectToken();

		void setTokens(Set<String> tokens);

		void setFichiers(ArrayList<Fichier> files);

		void setEmails(Set<String> emails);

		void setComments(ArrayList<Comment> result);

		Fichier getFile();

	}

	private final GeekServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private final User user;
	private final String fileId;
	private final String parentId;
	private final String path;
	
	String myStyleButton = "color: rgba(0, 0, 0, 0.63);" +
			"font-size: 17px;font-weight:" +
			" bold;font-family: Arial,'Helvetica Neue',Helvetica,sans-serif;" +
			"text-shadow: 0 1px 1px rgba(255, 255,255,0.37);" +
			"background-color: darkgray;" +
			"border: 1px solid #2A3F56;" +
			"border-radius: 2px;" +
			"cursor: pointer;" +
			"display: block;" +
			"-moz-background-clip: padding-box;" +
			"-webkit-background-clip: padding-box;" +
			"background-clip: padding-box;" +
			"opacity: .9;" +
			"white-space: nowrap;";

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 * @param fileId
	 */
	public FichierPresenter(GeekServiceAsync rpcService,
			HandlerManager eventBus, Display display, User user, String fileId) {
		this(rpcService, eventBus, display, user, fileId, null, "");
	}

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 * @param fileId
	 * @param parentId
	 * @param path
	 */
	public FichierPresenter(GeekServiceAsync rpcService,
			HandlerManager eventBus, Display display, User user,
			String fileId, String parentId, String path) {
			super();
			this.rpcService = rpcService;
			this.eventBus = eventBus;
			this.display = display;
			this.user = user;
			this.fileId = fileId;
			this.parentId = parentId;
			this.path = path;
			bind();
	}

	private void bind() {
		display.setListener(new Listener() {

			@Override
			public void saveFile(final Fichier file) {
				display.setSaveLoader(true);
				if (file.getId() == null) {
					rpcService.saveFile(file, new AsyncCallback<String>() {

						@Override
						public void onSuccess(String result) {
							file.setId(result);
							display.setFichier(file);
							display.setSaveLoader(false);
							setFichiers();
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Nous avons rencontré un problème lors de l'enrégistrement du fichier");
							display.setSaveLoader(false);
						}
					});
				} else {
					rpcService.updateFile(file, new AsyncCallback<Fichier>() {

						@Override
						public void onSuccess(Fichier result) {
							display.setFichier(file);
							display.setSaveLoader(false);
							setFichiers();
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Nous avons rencontré un problème lors de la mise à jour du fichier");
							display.setSaveLoader(false);
						}
					});
				}
			}

			@Override
			public void onClickSendFichier(final Fichier file,
					final String email) {
				final Fichier fichier = new Fichier(file);
				fichier.setParentId(null);
				fichier.setPath("");
				fichier.setPublique(false);
				display.close(false);
				rpcService.getUserByEmail(email, new AsyncCallback<User>() {

					@Override
					public void onSuccess(final User u) {
						if (u != null) {
							fichier.setUserId(u.getId());

							rpcService.saveFile(fichier,
									new AsyncCallback<String>() {

										@Override
										public void onSuccess(String result) {
											if (result != null) {
												String url = "<a href=\"http://apps.geekafric.com/#file=" + result
														+ "\"> <b>"
														+ file.getTitle()
														+ "</b></a>";

												String message = "Bonjour "
														+ u.getDisplayname()
														+ ", <br>"
														+ user.getDisplayname()
														+ " vient de vous envoyer un nouveau fichier<br>"
														+ "Pour le voir veuillez cliquer sur ce lien :"
														+ url + "<br><a href=\"http://apps.geekafric.com/\"> <b>"
																+ "Geek afric"
																+ "</b></a>";
												String em = "admin@geekafric.com";
												if(user.getEmail().endsWith("@geekafric.com")) {
													em = user.getEmail();
												}
												Mail mail = new Mail(em, email,
														"Nouveau fichier " + file.getTitle(),
														message);
												mail.setSenderName(user
														.getDisplayname());
												mail.setReceiverName(u
														.getDisplayname());
												mail.setRead(false);
												rpcService
														.sendMail(
																mail,
																new AsyncCallback<String>() {

																	@Override
																	public void onSuccess(
																			String result) {
																		display.close(true);
																	}

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		Window.alert("Il y a eu un problème lors de l'envoi de l'email..");
																	}
																});
											} else {
												Window.alert("Fichier non transféré car il n'a pu être enrégistré.");
												display.close(false);
											}
										}

										@Override
										public void onFailure(Throwable caught) {
											String err = caught.getMessage()
													.equals("0") ? "Problème de connexion"
													: caught.getMessage();
											Window.alert("Fichier non transféré à cause des erreurs suivante: "
													+ err);
										}
									});
						} else {
							String em = "admin@geekafric.com";
							if(user.getEmail().endsWith("@geekafric.com")) {
								em = user.getEmail();
							}
							String url = "<a href=\"http://apps.geekafric.com/\"> <b>Je m'inscris!</b></a>";
							Mail mail = new Mail(em, email, file.getTitle(), file.getContenu());
							mail.setMessage(file.getContenu() 
									+ "<br>" 
									+ file.getInfoHTML() 
									+ "<br> <center class='button-fichier-menu'>" 
									+ url 
									+ "</center>");
							mail.setSenderName(user.getDisplayname());
							rpcService.sendMail(mail, new AsyncCallback<String>() {
								
								@Override
								public void onSuccess(String result) {
									display.close(true);
								}
								
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Fichier non transféré à cause des erreurs suivante: "
											+ caught.getMessage());
									display.close(true);
								}
							});
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Fichier non transféré à cause des erreurs suivante: "
								+ caught.getMessage());
						display.close(true);
					}
				});
			}

			@Override
			public void onClickDeleteFile(Fichier file) {
				rpcService.deleteFile(file.getId(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						eventBus.fireEvent(new FichierListEvent(user.getId(), 0));
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Fichier non supprimé à cause des erreurs suivante: \n"
								+ caught.getMessage());
					}
				});
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
			public void onClickTitle(Fichier file) {
				eventBus.fireEvent(new FichierEvent(file.getId()));
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
			public SelectionHandler<Suggestion> getRechercheSelectHandler() {
				SelectionHandler<Suggestion> selection = new SelectionHandler<SuggestOracle.Suggestion>() {

					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						String token = display.getSelectToken();
						if (token != null && !token.isEmpty())
							eventBus.fireEvent(new FichierListEvent(token, 0));
					}
				};
				return selection;
			}

			@Override
			public void onClickSearch(String token) {
				if (token != null && !token.isEmpty())
					eventBus.fireEvent(new FichierListEvent(token, 0));
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
			public void onDeleteComment(Comment comment) {
				deleteComment(comment);
			}

			@Override
			public void onClickAddComment(Comment comment) {
				addComment(comment);
			}
		});
	}

	private void setFichier() {
		display.setloadLoder(true);
		if (fileId == null) {
			Fichier fichier = new Fichier();
			fichier.setParentId(parentId);
			fichier.setPath(path);
			display.setFichier(fichier);
			display.setloadLoder(false);
		} else
			rpcService.getFichier(fileId, new AsyncCallback<Fichier>() {

				@Override
				public void onSuccess(Fichier result) {
					display.setFichier(result);
					display.setloadLoder(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Le fichier n'a pas pu être chargé à cause des erreurs suivantes: "
							+ caught.getMessage());
					display.setloadLoder(false);
				}
			});
	}

	private void setFichiers() {
		display.setloadLoder(true);
		rpcService.getFilesByUserId(user.getId(), 0, "dateDerniereModif desc",
				new AsyncCallback<ArrayList<Fichier>>() {

					@Override
					public void onSuccess(ArrayList<Fichier> result) {
						display.setFichiers(result);
						display.setloadLoder(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("La liste des fichiers est introuvable à cause des erreurs suivantes: "
								+ caught.getMessage());
						display.setloadLoder(false);
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
				Window.alert("Il y a eu une erreur lors de la recupération des tokens de recherche: "
						+ caught.getMessage());
			}
		});
	}
	
	public void setComments() {
		rpcService.getObjectComments(fileId,
				new AsyncCallback<ArrayList<Comment>>() {
					@Override
					public void onSuccess(ArrayList<Comment> result) {
						display.setComments(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Il y a eu un problème lors de la récuperation des commentaires à cause de: " 
					+ (caught.getMessage().equals("0") ? "perte de connexion" : caught.getMessage()));
					}
				});

	}
	
	private void deleteComment(Comment comment) {
		if (comment.getId() != null && !comment.getId().isEmpty())
			rpcService.deleteComment(display.getFile(), comment,
					new AsyncCallback<Comment>() {
						@Override
						public void onSuccess(Comment result) {
							setComments();
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Il y a eu un problème lors de la suppression de ce commentaire à cause de: " 
									+ (caught.getMessage().equals("0") ? "perte de connexion" : caught.getMessage()));
						}
					});
	}
	
	private void addComment(Comment comment) {
		rpcService.addComment(display.getFile(), comment, new AsyncCallback<Comment>() {

			@Override
			public void onSuccess(final Comment comment) {
				rpcService.getUser(display.getFile().getUserId(),
						new AsyncCallback<User>() {

							@Override
							public void onSuccess(User result) {
								String url = "<a href=\"http://apps.geekafric.com/#file=" + display.getFile().getId()
										+ "\"> <b>"
										+ "Lire la suite..."
										+ "</b></a>";
								Mail mail = new Mail();
								mail.setSender("admin@geekafric.com");
								mail.setSenderName("Geekafric");
								mail.setReceiver(result.getEmail());
								mail.setObject(display.getFile().getTitle());
								String text = (comment.getText().length() >= 400) ? comment.getText().substring(0, 400) + url : comment.getText() + 
										"<a href=\"http://apps.geekafric.com/#file=" + display.getFile().getId()
										+ "\"> <b>"
										+ "Voir le fichier"
										+ "</b></a>";
								text = text + "<br><a href=\"http://apps.geekafric.com/\"> <b>"
										+ "Geek afric"
										+ "</b></a>";
								mail.setMessage(text);
								rpcService.sendMail(mail, new AsyncCallback<String>() {
									
									@Override
									public void onSuccess(String result) {
										setComments();
									}
									
									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Il y a eu un problème lors de l'envoi d'un message d'information à cause de: " 
												+ (caught.getMessage().equals("0") ? "perte de connexion" : caught.getMessage()));
									}
								});
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors de l'enrégistrement de votre commentaire à cause de: " 
										+ (caught.getMessage().equals("0") ? "perte de connexion" : caught.getMessage()));
							}
						});
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Il y a eu un problème lors de l'enrégistrement de votre commentaire à cause de: " 
						+ (caught.getMessage().equals("0") ? "perte de connexion" : caught.getMessage()));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.geek.afric.client.presenter.Presenter#go(com.google.gwt.user.
	 * client.ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		setFichier();
		setFichiers();
		setTokens();
		if(fileId != null)
			setComments();
	}

}
