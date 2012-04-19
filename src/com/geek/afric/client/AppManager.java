/**
 * 
 */
package com.geek.afric.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.geek.afric.shared.Mail;
import com.geek.afric.shared.User;
import com.geek.afric.client.event.AcceuilEvent;
import com.geek.afric.client.event.AcceuilEventHandler;
import com.geek.afric.client.event.FichierEvent;
import com.geek.afric.client.event.FichierEventHandler;
import com.geek.afric.client.event.FichierListEvent;
import com.geek.afric.client.event.FichierListEventHandler;
import com.geek.afric.client.event.MailEvent;
import com.geek.afric.client.event.MailEventHandler;
import com.geek.afric.client.event.UserLoggedEvent;
import com.geek.afric.client.event.UserLoggedEventHandler;
import com.geek.afric.client.event.UserLogoutEvent;
import com.geek.afric.client.event.UserLogoutEventHandler;
import com.geek.afric.client.presenter.AcceuilPresenter;
import com.geek.afric.client.presenter.FichierListPresenter;
import com.geek.afric.client.presenter.FichierPresenter;
import com.geek.afric.client.presenter.LoginPresenter;
import com.geek.afric.client.presenter.MailPresenter;
import com.geek.afric.client.presenter.Presenter;
import com.geek.afric.client.view.Acceuil;
import com.geek.afric.client.view.FichierList;
import com.geek.afric.client.view.FichierView;
import com.geek.afric.client.view.Images;
import com.geek.afric.client.view.Login;
import com.geek.afric.client.view.MailView;
import com.geek.afric.client.view.PasswordUpdate;
/**
 * @author Cheick Mahady SISSOKO
 *
 */
public class AppManager implements Presenter, ValueChangeHandler<String>  {

	/**
	 * 
	 */
	private final HandlerManager eventBus;
	private final GeekServiceAsync rpcService;
	private HasWidgets topContainer;
	private HasWidgets leftContainer;
	private HasWidgets container;
	private User user;
	public static Images images = GWT.create(Images.class);
	/**
	 * @param eventBus
	 * @param rpcService
	 * @param topContainer
	 * @param leftContainer
	 * @param container
	 */
	public AppManager(HandlerManager eventBus, GeekServiceAsync rpcService,
			HasWidgets topContainer, HasWidgets leftContainer, User user) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.topContainer = topContainer;
		this.leftContainer = leftContainer;
		this.user = user;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(AcceuilEvent.TYPE, new AcceuilEventHandler(){

			@Override
			public void onAcceuil(AcceuilEvent event) {
				doShowAcceuil();
			}
		});
		
		eventBus.addHandler(FichierEvent.TYPE, new FichierEventHandler() {

			@Override
			public void onFichier(FichierEvent event) {
				doShowFichier(event.getFileId(), event.getParentId(), event.getPath());
			}
		});
		
		eventBus.addHandler(FichierListEvent.TYPE, new FichierListEventHandler() {

			@Override
			public void onFichierList(FichierListEvent event) {
				doShowFichierList(event.getProprio(), event.getStartIndex());
			}
		});
		
		eventBus.addHandler(UserLoggedEvent.TYPE, new UserLoggedEventHandler(){

			@Override
			public void onUserLogged(UserLoggedEvent event) {
				doLogin(event.getUser());
			}
		});
		
		eventBus.addHandler(UserLogoutEvent.TYPE, new UserLogoutEventHandler(){

			@Override
			public void onUserLogout(UserLogoutEvent event) {
				doShowLogout(event.getUser());
			}
		});
		
		eventBus.addHandler(MailEvent.TYPE, new MailEventHandler(){

			@Override
			public void doMailView(MailEvent event) {
				doShowMails(event.getBox(), event.getStartIndex());
			}
			
		});
	}

	private void doShowLogout(User user) {
		this.user = null;
		History.newItem("login");
	}

	private void doLogin(User user) {
		this.user = user;
		doShowAcceuil();
	}
	
	private void doShowFichierList(String proprio, int startIndex) {
		History.newItem("files=proprio?" + proprio + "&startIndex?" + startIndex);
	}

	private void doShowFichier(String fileId, String parentId, String path) {
		History.newItem("file=id?" + fileId + "&parentId?" + parentId + "&path?" + path);
	}

	private void doShowAcceuil() {
		History.newItem("home");
	}
	
	private void doShowMails(String box, int startIndex) {
		History.newItem("mails=box?" + box + "&" + "startIndex?" + startIndex);
	}
	
	String part1;
	String part2;
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		if(token != null) {
			if(token.equals("login")) {
				if(isLogged())
					doShowAcceuil();
				else {
					GWT.runAsync(new RunAsyncCallback() {
						
						@Override
						public void onFailure(Throwable reason) {
							Window.alert("Code download failed");
						}
						
						@Override
						public void onSuccess() {
							LoginPresenter presenter = new LoginPresenter(rpcService, eventBus, new Login());
							presenter.go(container);
							topContainer.clear();
							leftContainer.clear();
							return;
						}
					});
				}
			} else if(token.equals("home")) {
				if(!isLogged()) {
					History.newItem("login");
					return;
				}
				else {
					GWT.runAsync(new RunAsyncCallback() {

						@Override
						public void onFailure(Throwable reason) {
							Window.alert("Code download failed");
						}
						
						@Override
						public void onSuccess() {
							Acceuil acceuil = new Acceuil(user);
							AcceuilPresenter presenter = new AcceuilPresenter(rpcService, eventBus, acceuil, user);
							presenter.go(container);
							topContainer.clear();
							topContainer.add(acceuil.getTopPanel());
							leftContainer.clear();
							leftContainer.add(acceuil.getLeftPanel());
						}
					});
				}
			} else {
				String[] lestring = token.split("=");
				part1 = lestring[0];
				part2 = lestring[lestring.length - 1];
				
				if(part1.equals("files")) {
					if(!isLogged()) {
						History.newItem("login");
						return;
					}
					else {
						GWT.runAsync(new RunAsyncCallback() {

							@Override
							public void onFailure(Throwable reason) {
								Window.alert("Code download failed");
							}
							
							@Override
							public void onSuccess() {
								String proprio = "";
								int startIndex = 0;
								try{
									String[] list = part2.split("&");
									String first = list[0];
									String second = list[1];
									proprio = first.substring(first.indexOf("?") + 1);
									second = second.substring(second.indexOf("?") + 1);
									try {
										startIndex = Integer.parseInt(second);
									} catch (IllegalArgumentException iae) {
										startIndex = -1;
									}
									FichierList fichierList = new FichierList(user);
									FichierListPresenter presenter = new FichierListPresenter(rpcService, eventBus, fichierList, 
											user, proprio, startIndex); 
									presenter.go(container);
									topContainer.clear();
									topContainer.add(fichierList.getTopPanel());
									leftContainer.clear();
									leftContainer.add(fichierList.getLeftPanel());
									return;
								} catch(Exception e) {
									Window.alert("Page non trouvée");
									return;
								}
							}
						});
					}
				} else if(part1.equals("file")) {
					if(!isLogged()) {
						History.newItem("login");
						return;
					}
					else {
						GWT.runAsync(new RunAsyncCallback() {

							@Override
							public void onFailure(Throwable reason) {
								Window.alert("Code download failed");
							}
							
							@Override
							public void onSuccess() {
								String[] list = part2.split("&");
								String first = list[0];
								String second = list[1];
								String third = list[2];
								String id = first.substring(first.indexOf("?") + 1);
								String parentId = second.substring(second.indexOf("?") + 1);
								String path = third.substring(third.indexOf("?") + 1);
								if(id == null || 
										id.equals("") || 
										id.equals("null"))
									id = null;
								if(parentId == null || 
										parentId.equals("") || 
										parentId.equals("null"))
									parentId = null;
								if(path == null || 
										path.equals("") || 
										path.equals("null"))
									path = null;
								FichierView fichier = new FichierView(user);
								FichierPresenter presenter = new FichierPresenter(rpcService, eventBus, fichier, user, id, parentId, path);
								presenter.go(container);
								topContainer.clear();
								topContainer.add(fichier.getTopPanel());
								leftContainer.clear();
								leftContainer.add(fichier.getLeftPanel());
								return;
							}
						});
					}
				} else if (part1.equals("mails")) {
					if (!isLogged())
						History.newItem("login");
					else {
						GWT.runAsync(new RunAsyncCallback() {

							@Override
							public void onFailure(Throwable reason) {
								Window.alert("Code download failed");
							}

							@Override
							public void onSuccess() {
								String box = "inbox";
								int startIndex = 0;
								String[] list = part2.split("&");
								String first = list[0];
								String second = list[1];
								box = first.substring(first.indexOf("?") + 1);
								second = second.substring(second.indexOf("?") + 1);
								try {
									startIndex = Integer.parseInt(second);
								} catch (IllegalArgumentException iae) {
									startIndex = -1;
								}
								MailView mailInfo = new MailView(user);
								Presenter presenter = new MailPresenter(
										rpcService, eventBus, mailInfo, user,
										box, startIndex);
								topContainer.clear();
								topContainer.add(mailInfo.getTopPanel());
								leftContainer.clear();
								leftContainer.add(mailInfo.getLeftPanel());
								presenter.go(container);
							}

						});
					}
				} else if(part1.equals("password")) {
					GWT.runAsync(new RunAsyncCallback() {
						
						@Override
						public void onFailure(Throwable reason) {
							Window.alert("Code download failed");
						}
						
						@Override
						public void onSuccess() {
							PasswordUpdate passUpdate = new PasswordUpdate(part2, rpcService, eventBus);
							passUpdate.go(container);
						}
					});
				} else if(part1.equals("confirm")) {
					GWT.runAsync(new RunAsyncCallback() {
						
						@Override
						public void onFailure(Throwable reason) {
							Window.alert("Code download failed");
						}
						
						@Override
						public void onSuccess() {
							PasswordUpdate passUpdate = new PasswordUpdate(part2, rpcService, eventBus);
							passUpdate.go(container);
						}
					});
				}
			}
		}
	}
	
	@Override
	public void go(HasWidgets container) {
		this.container = container;
		if ("".equals(History.getToken())) {
			if (isLogged())
				History.newItem("home");
			else
				History.newItem("login");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	private boolean isLogged() {
		return user != null;
	}
	
	static int count = 0;
	static Timer timer;
	
	@SuppressWarnings("deprecation")
	public static void runTimer(int j, int m, int a, int h, int mn, final Mail mail, final GeekServiceAsync rpcService) {
		final Date expires = new Date(a - 1900, m-1, j, h, mn);
		mail.setDateCreation(expires);
		mail.setDateDerniereModif(expires);
		timer = new Timer() {
			
			@Override
			public void run() {
				Date current = new Date();
				if(current.after(expires)) {
					rpcService.sendMail(mail, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							Window.alert("Votre message a été envoyé!");
							timer.cancel();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Votre message n'a pas pu être envoyé!");
							timer.cancel();
						}
					});
					timer.cancel();
				}
			}
		};
		timer.scheduleRepeating(1000);
		timer.run();
	}

}
