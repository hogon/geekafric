/**
 * 
 */
package com.geek.afric.client.view;

import java.util.Date;

import com.geek.afric.client.AppManager;
import com.geek.afric.client.presenter.LoginPresenter;
import com.geek.afric.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;

/**
 * @author ok
 *
 */
public class Login extends Composite implements 
	LoginPresenter.Display, KeyUpHandler{
	
	public interface Listener{
		void onClickSubmit(String email, String password);
		void onClickDialogValidateForgot(String text);
		void onClickValidateIns(User user);
	}
	Listener listener;
	FieldVerifier verifier = new FieldVerifier();
	AbsolutePanel panel = new AbsolutePanel();
	Button inscription = new Button("Inscrption");
	Label title = new Label();
	TextBox usernameTextBox = new TextBox();
	PasswordTextBox passwordTextBox = new PasswordTextBox();
	Button pwForgot = new Button();
	Button submit = new Button();
	Label labelError = new Label();
	Image loginLoader = new Image(AppManager.images.loginLoader());
	MyDialog dialog;
	
	FormPanel form = new FormPanel();
	
	public Login() {
//		form.setEncoding(FormPanel.ENCODING_MULTIPART);
//		form.setMethod(FormPanel.METHOD_POST);
//		form.addStyleName("table-center");
//		form.addStyleName("demo-FormPanel");
		
//		HorizontalPanel hpanel = new HorizontalPanel();
//		hpanel.setSpacing(15);
//		hpanel.add(usernameTextBox);
//		hpanel.add(passwordTextBox);
//		panel.add(hpanel, 108, 158);
		
		panel.setStyleName("login-panel");
		initWidget(panel);
		panel.setSize("600px", "400px");
		usernameTextBox.getElement().setAttribute("placeholder", "Email");
		passwordTextBox.getElement().setAttribute("placeholder", "Mot de passe");
		usernameTextBox.getElement().setAttribute("autocomplete", "on");
		labelError = verifier.getLabel();
		panel.add(title, 170, 100);
		
		title.setText("Identifiez vous.");
		title.setStyleName("login-title");
		
		panel.add(usernameTextBox, 108, 158);
		usernameTextBox.setSize("185px", "30px");
		usernameTextBox.setStyleName("login-field");
		usernameTextBox.setName("Le nom d'utilisateur");
		usernameTextBox.setTitle("Tapez votre votre e-mail");
		
		panel.add(passwordTextBox, 339, 158);
		passwordTextBox.setSize("172px", "30px");
		passwordTextBox.setStyleName("login-field");
		passwordTextBox.setName("Le mot de passe");
		passwordTextBox.setTitle("Tapez votre votre mot de passe");
		
		submit.setStyleName("login-submit");
		panel.add(submit, 339, 210);
		submit.setSize("178px", "42px");
		submit.setText("Connexion");
		submit.setStyleName("login-submit");
		
		labelError.setStyleName("label-error");
		panel.add(labelError, 10, 282);
		labelError.setSize("580px", "30px");
		pwForgot.setStyleName("login-submit");
		
		pwForgot.setText("Mot de passe oublié?");
		panel.add(pwForgot, 106, 210);
		pwForgot.setSize("190px", "22px");
		
		panel.add(inscription, 446, 10);
		
		panel.add(loginLoader, 285, 284);
		loginLoader.setVisible(false);
		bind();
	}
	
	@SuppressWarnings("deprecation")
	private void bind() {
		submit.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				boolean r = verifier.requireTextBox(usernameTextBox);
				if(!r)
					return;
				r = verifier.requireTextBox(passwordTextBox);
				if(!r)
					return;
				//form.submit();
				String email = usernameTextBox.getText();
				String password = passwordTextBox.getText();
				setEnabled(false);
				listener.onClickSubmit(email, password);
			}
		});
		
		pwForgot.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				dialog = new MyDialog();
				dialog.setGlassEnabled(true);
				dialog.center();
				panel.setVisible(false);
			}
			
		});
		
		usernameTextBox.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				setError("");
			}
		});
		usernameTextBox.addKeyUpHandler(this);
		
		passwordTextBox.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				setError("");
			}
		});
		passwordTextBox.addKeyUpHandler(this);
		
		inscription.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog = new MyDialog(false);
				dialog.setGlassEnabled(true);
				dialog.center();
				panel.setVisible(false);
			}
		});
		
//		form.addFormHandler(new FormHandler() {
//			
//			@Override
//			public void onSubmitComplete(FormSubmitCompleteEvent event) {
//				Window.alert(event.getResults());
//			}
//			
//			@Override
//			public void onSubmit(FormSubmitEvent event) {
//				boolean r = verifier.requireTextBox(usernameTextBox);
//				if(!r) {
//					event.setCancelled(true);
//					return;
//				}
//				r = verifier.requireTextBox(passwordTextBox);
//				if(!r) {
//					event.setCancelled(true);
//					return;
//				}
//				String email = usernameTextBox.getText();
//				String password = passwordTextBox.getText();
//				setEnabled(false);
//				listener.onClickSubmit(email, password);
//			}
//		});
		
	}
	
	@Override
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	@Override
	public void setError(String error) {
		if(dialog != null) {
			dialog.labelError.setText(error);
		}
		else {
			labelError.setText(error);
			passwordTextBox.setText("");
			setEnabled(true);
		}
	}
	
	@Override
	public void setConfirmation(String message) {
		dialog.labelConfirmation.setText(message);
	}
	
	@Override
	public void setLoginLoader(boolean load){
		loginLoader.setVisible(load);
	}
	
	@Override
	public void setDefaultLoader(boolean load){
		if(dialog != null)
			dialog.defaultLoader.setVisible(load);
	}
	
	@Override
	public void close() {
		if(dialog != null) {
			dialog.hide();
			panel.setVisible(true);
			dialog = null;
		}
	}
	
	private void setEnabled(boolean enabled) {
		usernameTextBox.setEnabled(enabled);
		passwordTextBox.setEnabled(enabled);
		pwForgot.setEnabled(enabled);
		submit.setEnabled(enabled);
	}
	
	class MyDialog extends DialogBox implements KeyUpHandler{
		FieldVerifier verifier = new FieldVerifier();
		AbsolutePanel absolutePanel = new AbsolutePanel();
		Button cancel = new Button("Fermer");
		Button validate = new Button("Valider");
		TextBox remailTextBox = new TextBox();
		Label labelConfirmation = new Label();
		Label labelError = new Label();
		Image defaultLoader = new Image(AppManager.images.defaultLoader());
		
		public MyDialog() {
			setGlassStyleName("glass");
			setHTML("<center>Mot de passe oublié</center>");
			labelError = verifier.getLabel();
			defaultLoader.setVisible(false);

			FlexTable flexTable = new FlexTable();
			setWidget(flexTable);
			flexTable.setCellPadding(2);
			flexTable.setSize("385px", "173px");

			flexTable.setWidget(0, 0, remailTextBox);
			flexTable.getCellFormatter().setHeight(0, 0, "40px");
			remailTextBox.setWidth("375px");
			remailTextBox.setName("Le champ email");
			remailTextBox.setTitle("Tapez votre e-mail");
			remailTextBox.getElement().setAttribute("placeholder", "E-mail");
			remailTextBox.getElement().setAttribute("autocomplete", "on");
			remailTextBox.setStyleName("login-field");

			flexTable.setWidget(1, 0, defaultLoader);
			flexTable.getCellFormatter().setHeight(1, 0, "10px");
			defaultLoader.setHeight("");

			flexTable.setWidget(2, 0, labelConfirmation);
			flexTable.getCellFormatter().setHeight(2, 0, "30px");
			labelConfirmation.setStyleName("label-confirmation");
			labelConfirmation.setWidth("100%");
			flexTable.getCellFormatter().setVerticalAlignment(0, 0,
					HasVerticalAlignment.ALIGN_TOP);
			flexTable.getCellFormatter().setHorizontalAlignment(4, 0,
					HasHorizontalAlignment.ALIGN_RIGHT);
			flexTable.getCellFormatter().setHorizontalAlignment(1, 0,
					HasHorizontalAlignment.ALIGN_CENTER);
			flexTable.getCellFormatter().setVerticalAlignment(1, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			flexTable.getCellFormatter().setVerticalAlignment(2, 0,
					HasVerticalAlignment.ALIGN_BOTTOM);
			labelError.setStyleName("label-error");
			flexTable.setWidget(3, 0, labelError);
			flexTable.getCellFormatter().setHeight(3, 0, "20px");

			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			horizontalPanel
					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			flexTable.setWidget(4, 0, horizontalPanel);
			flexTable.getCellFormatter().setHeight(4, 0, "");
			cancel.setStyleName("button-fichier-menu");
			horizontalPanel.add(cancel);
			validate.setStyleName("button-fichier-menu");
			horizontalPanel.add(validate);
			flexTable.getCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_TOP);
			bind();
		}

		private void bind() {
			cancel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					clear();
					hide();
					panel.setVisible(true);
					dialog = null;
				}
			});
			
			validate.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					boolean r = verifier.requireTextBox(remailTextBox);
					if(!r) 
						return;
					String email = remailTextBox.getText();
					r = verifier.isValidUsername(email);
					if(!r) 
						return;
					listener.onClickDialogValidateForgot(email);
				}
			});
			
			remailTextBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			
			remailTextBox.addKeyUpHandler(new KeyUpHandler() {
				
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						validate.click();
					}
				}
			});
		}
		
		//Inscription
		TextBox prenom = new TextBox();
		TextBox nom = new TextBox();
		TextBox email = new TextBox();
		TextBox confirmation = new TextBox();
		PasswordTextBox password = new PasswordTextBox();
		Button valider = new Button("Valider");
		Button annuler = new Button("Annuler");
		
		public MyDialog(boolean ins) {
			super(ins);
			setGlassStyleName("glass");
			setHTML("<center>Inscription</center>");
			labelError = verifier.getLabel();
			setWidget(absolutePanel);
			absolutePanel.setSize("404px", "457px");
			defaultLoader.setVisible(false);
			FlexTable flexTable = new FlexTable();
			absolutePanel.add(flexTable, 50, 10);
			flexTable.setSize("303px", "384px");
			
			flexTable.setWidget(0, 0, prenom);
			prenom.setWidth("100%");
			prenom.getElement().setAttribute("placeholder", "Prénom");
			prenom.getElement().setAttribute("autocomplete", "on");
			prenom.setName("Le prénom");
			prenom.setTitle("Tapez votre prénom");
			prenom.setStyleName("login-field");
			
			flexTable.setWidget(1, 0, nom);
			nom.setWidth("100%");
			nom.getElement().setAttribute("placeholder", "Nom");
			nom.getElement().setAttribute("autocomplete", "on");
			nom.setName("Le nom");
			nom.setTitle("Tapez votre nom");
			nom.setStyleName("login-field");
			
			flexTable.setWidget(2, 0, email);
			email.setWidth("100%");
			email.getElement().setAttribute("placeholder", "E-mail");
			email.getElement().setAttribute("autocomplete", "on");
			email.setName("L'email");
			email.setTitle("Tapez votre e-mail");
			email.setStyleName("login-field");
			
			flexTable.setWidget(3, 0, confirmation);
			confirmation.setWidth("100%");
			confirmation.getElement().setAttribute("placeholder", "Confirmez votre e-mail");
			confirmation.getElement().setAttribute("autocomplete", "on");
			confirmation.setName("La confirmation email");
			confirmation.setTitle("Tapez la confirmation de votre e-mail");
			confirmation.setStyleName("login-field");
			
			flexTable.setWidget(4, 0, password);
			password.setWidth("100%");
			password.getElement().setAttribute("placeholder", "Mot de passe");
			password.setName("Le mot de passe");
			password.setTitle("Tapez votre mot de passe");
			password.setStyleName("login-field");
			
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			flexTable.setWidget(5, 0, horizontalPanel);
			horizontalPanel.setWidth("130px");


			annuler.setStyleName("button-fichier-menu");
			horizontalPanel.add(annuler);
			valider.setStyleName("button-fichier-menu");
			horizontalPanel.add(valider);
			
			flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			
			flexTable.setWidget(6, 0, defaultLoader);
			flexTable.getCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			labelError.setStyleName("label-error");
			absolutePanel.add(labelError, 10, 400);
			labelError.setSize("384px", "18px");
			bindIns();
		}
		
		private void bindIns() {
			prenom.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			prenom.addKeyUpHandler(this);
			
			nom.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			nom.addKeyUpHandler(this);
			
			email.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			email.addKeyUpHandler(this);
			
			confirmation.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			confirmation.addKeyUpHandler(this);
			
			password.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			password.addKeyUpHandler(this);
			
			valider.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					User user = new User();
					boolean r = verifier.requireTextBox(prenom);
					if(!r) 
						return;
					r = verifier.requireTextBox(nom);
					if(!r) 
						return;
					r = verifier.requireTextBox(email);
					if(!r) 
						return;
					r = verifier.requireTextBox(confirmation);
					if(!r) 
						return;
					r = verifier.requireTextBox(password);
					if(!r) 
						return;
					r = verifier.isValidUsername(email.getText());
					if(!r) 
						return;
					r = verifier.checkEmail(email.getText(), confirmation.getText());
					if(!r) 
						return;
					r = verifier.isValidPassword(password.getText());
					if(!r) 
						return;
					user.setPrenom(prenom.getText());
					user.setNom(nom.getText());
					user.setEmail(email.getText());
					user.setPassword(password.getText());
					user.setDateCreation(new Date());
					user.setDateDerniereModif(new Date());
					listener.onClickValidateIns(user);
				}
			});
			
			annuler.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					clearIns();
					hide();
					panel.setVisible(true);
					dialog = null;
				}
			});
		}
		
		public void clear() {
			labelError.setText("");
			labelConfirmation.setText("");
			remailTextBox.setText("");
		}
		
		public void resetIns() {
			confirmation.setText("");
			password.setText("");
		}
		
		public void clearIns() {
			labelError.setText("");
			nom.setText("");
			prenom.setText("");
			email.setText("");
			confirmation.setText("");
			password.setText("");
		}
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				if (event.getSource().equals(prenom)) {
					nom.setFocus(true);
				} else if (event.getSource().equals(nom)) {
					email.setFocus(true);
				} else if(event.getSource().equals(email)){
					confirmation.setFocus(true);
				} else if (event.getSource().equals(confirmation)) {
					password.setFocus(true);
				} else if (event.getSource().equals(password)) {
					valider.click();
				}
			}
		}
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			if (event.getSource().equals(usernameTextBox)) {
				passwordTextBox.setFocus(true);
			} else if (event.getSource().equals(passwordTextBox)) {
				submit.click();
			}
		}
	}
}
