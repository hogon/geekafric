/**
 * 
 */
package com.geek.afric.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.geek.afric.client.GeekServiceAsync;
import com.geek.afric.client.event.AcceuilEvent;
import com.geek.afric.client.event.FichierEvent;
import com.geek.afric.client.event.FichierListEvent;
import com.geek.afric.client.event.UserLogoutEvent;
import com.geek.afric.client.view.FichierList.Listener;
import com.geek.afric.shared.AbstractFichier;
import com.geek.afric.shared.Dossier;
import com.geek.afric.shared.Fichier;
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
public class FichierListPresenter implements Presenter {

	public interface Display {

		void setListener(Listener listener);

		void setLoader(boolean load);

		void setStartIndex(int startIndex);

		int getStratIndex();

		void setFichiers(ArrayList<Fichier> files);

		void setLeftFichiers(ArrayList<Fichier> files);

		Widget asWidget();

		String getSelectToken();

		void setTokens(Set<String> tokens);

		void setAbstractFichiers(ArrayList<AbstractFichier> files);

		void setDossier(Dossier file);

	}

	private final GeekServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private final User user;
	String proprio;
	int startIndex;
	String order;

	Map<String, ArrayList<Fichier>> mapped = new HashMap<String, ArrayList<Fichier>>();
	Map<String, ArrayList<AbstractFichier>> abMapped = new HashMap<String, ArrayList<AbstractFichier>>();

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 * @param proprio
	 * @param startIndex
	 */
	public FichierListPresenter(GeekServiceAsync rpcService,
			HandlerManager eventBus, Display display, User user,
			String proprio, int startIndex) {
		super();
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		this.user = user;
		if (proprio == null)
			proprio = user.getId();
		this.proprio = proprio;
		this.startIndex = startIndex;
		bind();
	}

	/**
	 * @param rpcService
	 * @param eventBus
	 * @param display
	 * @param user
	 */
	public FichierListPresenter(GeekServiceAsync rpcService,
			HandlerManager eventBus, Display display, User user) {
		this(rpcService, eventBus, display, user, user.getId(), 0);
	}

	private void bind() {
		display.setListener(new Listener() {

			@Override
			public void onClickSearch(String token) {
				eventBus.fireEvent(new FichierListEvent(token, 0));
			}

			@Override
			public void onClickPartages() {
				eventBus.fireEvent(new FichierListEvent("shared", 0));
			}

			@Override
			public void onClickNext() {
				eventBus.fireEvent(new FichierListEvent(proprio, ++startIndex));
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
			public void onClickDelete(ArrayList<AbstractFichier> deleteFiles) {
				deleteFiles(deleteFiles);
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
			public void onClickCreateFile(Dossier parent) {
				if(parent == null) {
					eventBus.fireEvent(new FichierEvent(null));
					return;
				}
				String path = null;
				if (parent.getPath() != null)
					path = parent.getPath();
				if (parent.getTitle() != null 
						&& !parent.getTitle().equals("")) 
					path = path + "/" + parent.getTitle();
				eventBus.fireEvent(new FichierEvent(null, parent.getId(), path));
			}

			@Override
			public void onClickBack() {
				eventBus.fireEvent(new FichierListEvent(proprio, --startIndex));
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
			public void setOrder(String order) {
				String ordre = "dateCreation desc";
				if (order.equals("Titre")) {
					ordre = "title asc";
				} else if (order.equals("Date")) {
					ordre = "dateCreation desc";
				} else if (order.equals("Modification")) {
					ordre = "dateDerniereModif asc";
				} else if (order.equals("Titre desc")) {
					ordre = "title desc";
				} else if (order.equals("Date desc")) {
					ordre = "dateCreation asc";
				} else if (order.equals("Modification desc")) {
					ordre = "dateDerniereModif desc";
				}
				display.setStartIndex(startIndex);
				setAllFiles(null, ordre);
			}

			@Override
			public void clearCache() {
				rpcService.clearCache(user, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Window.alert("Je pense que le cache a été vidé!.");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Désolé mais je n'ai pas pu vider le cache.");
					}
				});
			}

			@Override
			public void onClickDossier(Dossier file) {
				if(file == null) {
					setAllFiles(null, order);
					return;
				}
				display.setDossier(file);
			}

			@Override
			public void onClickCreateDossier(final Dossier dossier) {
				rpcService.saveDossier(dossier, new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						abMapped.clear();
						dossier.setId(result);
						display.setLoader(false);
						display.setDossier(dossier);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Il y a eu un problème lors de la récuperation des fichiers du dossier à cause de: "
								+ (caught.getMessage().equals("0") ? "perte de connexion"
										: caught.getMessage()));
					}
				});
			}

			@Override
			public void onClickUpdate(Dossier parent) {
				display.setLoader(true);
				rpcService.updateDossier(parent, new AsyncCallback<Dossier>() {

					@Override
					public void onSuccess(Dossier result) {
						display.setLoader(false);
						display.setDossier(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Il y a eu un problème lors de la mise à jour de ce dossier à cause de: "
								+ (caught.getMessage().equals("0") ? "perte de connexion"
										: caught.getMessage()));
						display.setLoader(false);
					}
				});
			}

			@Override
			public void deleteFile(AbstractFichier toMove) {
				display.setLoader(true);
				if (toMove instanceof Dossier) {
					rpcService.deleteDossier((Dossier) toMove,
							new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									//display.setLoader(false);
									setAllFiles(null, order);
								}

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Il y a eu un problème lors de la suppression de ce dossier à cause de: "
											+ (caught.getMessage().equals("0") ? "perte de connexion"
													: caught.getMessage()));
									display.setLoader(false);
								}
							});
				} else if (toMove instanceof Fichier) {
					rpcService.deleteFile(toMove.getId(),
							new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void result) {
									display.setLoader(false);
									setAllFiles(null, order);
								}

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Il y a eu un problème lors de la suppression de ce dossier à cause de: "
											+ (caught.getMessage().equals("0") ? "perte de connexion"
													: caught.getMessage()));
									display.setLoader(false);
								}
							});
				}
			}

			@Override
			public void updateFile(Dossier file) {
				display.setLoader(true);
				rpcService.updateDossier(file, new AsyncCallback<Dossier>() {

					@Override
					public void onSuccess(Dossier result) {
						display.setDossier(result);
						display.setLoader(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Il y a eu un problème lors de la mise à jour de ce dossier à cause de: "
								+ (caught.getMessage().equals("0") ? "perte de connexion"
										: caught.getMessage()));
						display.setLoader(false);
					}
				});
			}

			@Override
			public void updateAll(final Dossier dossier,
					final ArrayList<AbstractFichier> selectedFiles) {
				display.setLoader(true);
				rpcService.updateAll(dossier, selectedFiles,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								if (dossier.getId() != null) {
									display.setDossier(dossier);
									display.setLoader(false);
								} else {
									abMapped.clear();
									setAllFiles(null, order);
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors du déplacement des éléments à cause de: "
										+ (caught.getMessage().equals("0") ? "perte de connexion"
												: caught.getMessage()));
								display.setLoader(false);
							}
						});
			}
		});
	}

	private void deleteFiles(ArrayList<AbstractFichier> deleteFiles) {
		display.setLoader(true);
		rpcService.deleteFiles(deleteFiles, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				mapped.clear();
				abMapped.clear();
				setAllFiles(null, order);
				setLeftFichiers("dateDerniereModif desc");
				display.setLoader(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Il y a eu un problème lors de la suppression des fichiers");
				display.setLoader(false);
			}
		});
	}

	@SuppressWarnings("unused")
	private void setFichiers(final String order) {
		if (proprio == null) {
			return;
		}
		this.order = order;
		display.setLoader(true);
		ArrayList<Fichier> fromMap = null;
		fromMap = mapped.get(proprio + startIndex + order);
		if (fromMap != null) {
			display.setFichiers(fromMap);
			display.setLoader(false);
			return;
		} else {
			if (proprio.equals(user.getId())) {
				rpcService.getFilesByUserId(user.getId(), startIndex, order,
						new AsyncCallback<ArrayList<Fichier>>() {

							@Override
							public void onSuccess(ArrayList<Fichier> result) {
								display.setFichiers(result);
								mapped.put(proprio + startIndex + order, result);
								display.setLoader(false);
								Window.setTitle("Geek afric - Mes fichiers");
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors de la récuperation des fichiers: "
										+ caught.getMessage());
								display.setLoader(false);
							}
						});
			} else if (proprio.equals("shared")) {
				rpcService.getFilesByUserId("shared", startIndex, order,
						new AsyncCallback<ArrayList<Fichier>>() {

							@Override
							public void onSuccess(ArrayList<Fichier> result) {
								display.setFichiers(result);
								mapped.put(proprio + startIndex + order, result);
								display.setLoader(false);
								Window.setTitle("Geek afric - Partages");
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors de la récuperation des fichiers: "
										+ caught.getMessage());
								display.setLoader(false);
							}
						});
			} else {
				rpcService.searchInFiles(user, proprio,
						new AsyncCallback<ArrayList<Fichier>>() {

							@Override
							public void onSuccess(ArrayList<Fichier> result) {
								display.setFichiers(result);
								mapped.put(proprio + startIndex + order, result);
								display.setLoader(false);
								Window.setTitle("Geek afric - Recherches");
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors de la récuperation des fichiers: "
										+ caught.getMessage());
								display.setLoader(false);
							}
						});
			}
		}
	}

	private void setLeftFichiers(final String order) {
		ArrayList<Fichier> fromMap = null;
		fromMap = mapped.get("left" + order);
		if (fromMap != null) {
			display.setFichiers(fromMap);
			return;
		} else {
			rpcService.getFilesByUserId("shared", 0, order,
					new AsyncCallback<ArrayList<Fichier>>() {

						@Override
						public void onSuccess(ArrayList<Fichier> result) {
							display.setLeftFichiers(result);
							mapped.put("left" + order, result);
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Il y a eu un problème lors de la récuperation des fichiers: "
									+ caught.getMessage());
						}
					});
		}
	}

	private void setTokens() {
		rpcService.getSearchTokens(user, new AsyncCallback<Set<String>>() {

			@Override
			public void onSuccess(Set<String> result) {
				display.setTokens(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Il y a eu un problème lors de la récuperation des tokens de recherche: "
						+ caught.getMessage());
			}
		});
	}

	private void setAllFiles(final String parentId, final String order) {
		this.order = order;
		display.setLoader(true);
		ArrayList<AbstractFichier> fromMap = null;
		fromMap = abMapped.get(parentId + proprio + startIndex + order);
		if (fromMap != null) {
			display.setAbstractFichiers(fromMap);
			display.setLoader(false);
			return;
		} else {
			if (!(proprio.equals("shared") || proprio.equals(user.getId()))) {
				rpcService.searchFiles(user, proprio,
						new AsyncCallback<ArrayList<AbstractFichier>>() {

							@Override
							public void onSuccess(
									ArrayList<AbstractFichier> result) {
								display.setAbstractFichiers(result);
								abMapped.put(parentId + proprio + startIndex
										+ order, result);
								display.setLoader(false);
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors de la recherche des fichiers à cause "
										+ (caught.getMessage().equals("0") ? "d'une perte de connexion"
												: "de " + caught.getMessage()));
								display.setLoader(false);
							}
						});
			} else
				rpcService.getFiles(proprio, parentId, order,
						new AsyncCallback<ArrayList<AbstractFichier>>() {

							@Override
							public void onSuccess(
									ArrayList<AbstractFichier> result) {
								display.setAbstractFichiers(result);
								abMapped.put(parentId + proprio + startIndex
										+ order, result);
								display.setLoader(false);
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Il y a eu un problème lors de la récuperation des fichiers à cause : "
										+ (caught.getMessage().equals("0") ? "d'une perte de connexion"
												: "de " + caught.getMessage()));
								display.setLoader(false);
							}
						});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.geek.afric.client.presenter.Presenter#go(com.google.gwt.user.client
	 * .ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		display.setStartIndex(startIndex * 15);
		// setFichiers("dateCreation desc");
		setAllFiles(null, "dateCreation desc");
		setLeftFichiers("dateDerniereModif desc");
		setTokens();
	}

}
