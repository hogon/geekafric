/**
 * 
 */
package com.geek.afric.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author ok
 *
 */
public class FieldVerifier {
	Label labelError = new Label();
	private final static String EMAIL_VALIDATION_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+"
			+ "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*"
			+ "[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	//private final static String PW_VALIDATION_REGEX = "((?=.*\\d)" + "(?=.*[a-z])" + "(?=.*[A-Z])(?=.*[@#$%]).{6,20}) ";
	
	public FieldVerifier() {
		labelError.setStyleName("label-error");
	}
	
	public Label getLabel() {
		labelError.setStyleName("label-error");
		return labelError;
	}
	
	public boolean isValidUsername(String username) {
		if(!username.matches(EMAIL_VALIDATION_REGEX)) {
			labelError.setText("Votre email est incorrect");
			return false;
		}
		return true;
	}
	
	public boolean isValidName(String name) {
		if(name == null || name.length() < 2) {
			labelError.setText("Votre nom est trop court");
			return false;
		}
		return true;
	}
	
	public boolean isValidPassword(String password) {
		if(password.length() < 6) {
			labelError.setText("Votre mot de passe doit avoir au moins 6 caractères");
			return false;
		}
		return true;
	}
	
	public boolean isValidPassword(TextBox textBox) {
		String text = textBox.getText();
		if(text == null || text.equals("")) {
			labelError.setText(textBox.getName() + " est obligatoire.");
			textBox.addStyleName("error-field");
			textBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		if(text.length() < 6) {
			labelError.setText("Votre mot de passe est trop court! 6 caractères minimum");
			textBox.addStyleName("error-field");
			textBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		return true;
	}
	
	public boolean checkPassword(String password, String check) {
		if(!password.equals(check)) {
			labelError.setText("Vos mots de passe ne correspondent pas.");
			return false;
		}
		return true;
	}
	
	public boolean checkEmail(String email, String confirmation) {
		if(!email.equals(confirmation)) {
			labelError.setText("Vos emails ne correspondent pas.");
			return false;
		}
		return true;
	}
	
	public boolean requireTextBox(TextBox textBox) {
		String text = textBox.getText();
		if(text == null || text.equals("")) {
			labelError.setText(textBox.getName() + " est obligatoire.");
			textBox.setEnabled(true);
			textBox.addStyleName("error-field");
			textBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		return true;
	}
	
	public boolean isValidEmail(TextBox textBox) {
		String text = textBox.getText();
		if(text == null || text.equals("")) {
			labelError.setText(textBox.getName() + " est obligatoire.");
			textBox.addStyleName("error-field");
			textBox.setEnabled(true);
			textBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		if(!text.matches(EMAIL_VALIDATION_REGEX)) {
			labelError.setText("Votre email est incorrect");
			textBox.addStyleName("error-field");
			textBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		return true;
	}
	
	public boolean isValidListBox(ListBox listBox) {
		if(listBox.isItemSelected(0)) {
			labelError.setText(listBox.getName() + " est obligatoire.");
			listBox.addStyleName("error-field");
			listBox.setEnabled(true);
			listBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					ListBox textBoxc = (ListBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		return true;
	}

	public boolean isValidEmail(SuggestBox textBox) {
		String text = textBox.getText();
		if(text == null || text.equals("")) {
			labelError.setText(textBox.getTextBox().getName() + " est obligatoire.");
			textBox.addStyleName("error-field");
			textBox.getTextBox().setEnabled(true);
			textBox.getTextBox().addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		if(!text.matches(EMAIL_VALIDATION_REGEX)) {
			labelError.setText("Votre email est incorrect");
			textBox.addStyleName("error-field");
			textBox.getTextBox().addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					TextBox textBoxc = (TextBox)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		return true;
	}

	public boolean isValidTextArea(RichTextArea textBox) {
		String text = textBox.getText();
		if(text == null || text.equals("")) {
			labelError.setText("Écris quelque chose au moins.");
			textBox.addStyleName("error-field");
			textBox.setEnabled(true);
			textBox.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					RichTextArea textBoxc = (RichTextArea)(event.getSource());
					textBoxc.removeStyleName("error-field");
					labelError.setText("");
				}
			});
			return false;
		}
		return true;
	}
}
