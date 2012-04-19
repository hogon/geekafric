/**
 * 
 */
package com.geek.afric.client.view;

import com.geek.afric.client.AppManager;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author Mcicheick
 * 
 */
public class MyDialog extends DialogBox implements KeyUpHandler {
	FieldVerifier verifier = new FieldVerifier();
	TextBox titleBox = new TextBox();
	CheckBox publique = new CheckBox("publique");
	Label parentName = new Label();
	Label labelError = new Label();
	Button btnAnnuler = new Button("Annuler");
	Button btnValider = new Button("Valider");
	Image defaultLoader = new Image(AppManager.images.defaultLoader());
	
	public MyDialog() {
		setHTML("<center>Nouveau Dossier</center>");
		setGlassStyleName("glass");
		labelError = verifier.getLabel();
		FlexTable flexTable = new FlexTable();
		setWidget(flexTable);
		flexTable.setWidth("300px");
		
		titleBox.setStyleName("login-field");
		titleBox.getElement().setAttribute("placeholder", "Titre");
		titleBox.getElement().setAttribute("autocomplete", "on");
		titleBox.setName("Un titre");
		flexTable.setWidget(0, 0, titleBox);
		titleBox.setWidth("100%");
		flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		
		flexTable.setWidget(1, 0, parentName);
		flexTable.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		
		flexTable.setWidget(2, 0, publique);
		flexTable.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);
		
		labelError.setStyleName("label-error");
		flexTable.setWidget(3, 0, labelError);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		flexTable.setWidget(4, 0, buttonPanel);
		
		btnAnnuler.setStyleName("button-fichier-menu");
		buttonPanel.add(btnAnnuler);
		
		btnValider.setStyleName("button-fichier-menu");
		buttonPanel.add(btnValider);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		flexTable.setWidget(5, 0, defaultLoader);
		flexTable.getCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_TOP);
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		// TODO Auto-generated method stub
		
	}
}
