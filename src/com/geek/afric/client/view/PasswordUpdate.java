/**
 * 
 */
package com.geek.afric.client.view;

import com.geek.afric.client.GeekServiceAsync;
import com.geek.afric.client.event.UserLoggedEvent;
import com.geek.afric.client.presenter.Presenter;
import com.geek.afric.shared.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Mcicheick
 *
 */
public class PasswordUpdate extends Composite implements KeyUpHandler, Presenter {

	FieldVerifier verifier = new FieldVerifier();
	Button btnValider = new Button("Valider");
	Button btnAnnuler = new Button("Annuler");
	TextBox txtbxEmail = new TextBox();
	PasswordTextBox passwordBox = new PasswordTextBox();
	Label labelError = new Label();
	String userId;
	GeekServiceAsync rpcService;
	HandlerManager eventBus;
	User user;
	
	public PasswordUpdate(String userId, GeekServiceAsync rpcService, HandlerManager eventBus) {
		this.userId = userId;
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		Window.setTitle("Changement de mot de passe");
		labelError = verifier.getLabel();
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(2);
		initWidget(flexTable);
		flexTable.setSize("330px", "150px");
		
		txtbxEmail.setStyleName("login-field");
		flexTable.setWidget(0, 0, txtbxEmail);
		txtbxEmail.setEnabled(false);
		flexTable.getCellFormatter().setHeight(0, 0, "25px");
		txtbxEmail.setWidth("323px");
		flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		
		passwordBox.setStyleName("login-field");
		passwordBox.setName("Le champ mot de passe");
		passwordBox.getElement().setAttribute("placeholder", "Tapez votre nouveau mot de passe");
		flexTable.setWidget(1, 0, passwordBox);
		passwordBox.setWidth("323px");
		flexTable.getCellFormatter().setHeight(1, 0, "25px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(2, 0, horizontalPanel);
		horizontalPanel.setHeight("30px");
		flexTable.getCellFormatter().setHeight(2, 0, "30px");
		
		btnValider.setStyleName("button-fichier-menu");
		horizontalPanel.add(btnValider);
		
		btnAnnuler.setStyleName("button-fichier-menu");
		horizontalPanel.add(btnAnnuler);
		
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
		
		flexTable.setWidget(3, 0, labelError);
		labelError.setWidth("100%");
		bind();
	}
	
	private void bind() {
		btnValider.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				boolean r = verifier.isValidPassword(passwordBox);
				if(!r) {
					return;
				}
				if(user == null) {
					setError("Aucun utitilisateur trouvé.");
					return;
				}
				btnValider.setEnabled(false);
				passwordBox.setEnabled(false);
				rpcService.updatePassword(user, passwordBox.getText(), new AsyncCallback<User>() {
					
					@Override
					public void onSuccess(User result) {
						eventBus.fireEvent(new UserLoggedEvent(result));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						setError("Il y a eu un problème lors de la mise à jour du mot de passe.");
					}
				});
			}
		});
		
		btnAnnuler.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("login");
			}
		});
		
		passwordBox.addKeyUpHandler(this);
	}

	public void setError(String error) {
		btnValider.setEnabled(true);
		passwordBox.setEnabled(true);
		labelError.setText(error);
	}
	
	public void show() {
		rpcService.getUser(userId, new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				user = result;
				if(result == null) {
					setError("Utilisateur non trouvé");
					return;
				}
				txtbxEmail.setText(result.getDisplayname());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				setError("Un problème s'est produit. On n'a pas pu retrouver l'utilisateur correspondant");
			}
		});
	}
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			btnValider.click();
		}
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this);
		show();
	}
}
