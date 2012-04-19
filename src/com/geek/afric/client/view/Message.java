/**
 * 
 */
package com.geek.afric.client.view;

import com.geek.afric.client.AppManager;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author Mcicheick
 *
 */
public class Message extends DialogBox {

	VerticalPanel panel = new VerticalPanel();
	Button btnEnvoyer = new Button("Envoyer");
	Button btnAnnuler = new Button("Annuler");
	TextBox emailTo = new TextBox();
	TextBox objet = new TextBox();
	RichTextArea content = new RichTextArea();
	private final Label lblError = new Label("error");
	private final Image sendLoader = new Image(AppManager.images.defaultLoader());
	private final HorizontalPanel horizontalPanel = new HorizontalPanel();
	private final Button btnEnvoiDiffr = new Button("Envoi différé");
	private final HorizontalPanel timePanel = new HorizontalPanel();
	private final ListBox dayListBox = new ListBox();
	private final ListBox monthListBox = new ListBox();
	private final ListBox yearListBox = new ListBox();
	private final ListBox hourListBox = new ListBox();
	private final ListBox minListBox = new ListBox();
	
	public Message() {
		super(false);
		minListBox.setStyleName("button-fichier-menu");
		minListBox.setSelectedIndex(0);
		hourListBox.setStyleName("button-fichier-menu");
		hourListBox.setSelectedIndex(0);
		yearListBox.setStyleName("button-fichier-menu");
		yearListBox.setSelectedIndex(0);
		monthListBox.setStyleName("button-fichier-menu");
		monthListBox.setSelectedIndex(0);
		dayListBox.setStyleName("button-fichier-menu");
		dayListBox.setSelectedIndex(0);
		setHTML("<center>Envoie d'email</center>");
		setWidget(panel);
		panel.setSize("600px", "460px");
		sendLoader.setVisible(false);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		panel.add(buttonPanel);
		panel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		btnEnvoyer.setStyleName("button-fichier-menu");
		buttonPanel.add(btnEnvoyer);
		
		btnAnnuler.setStyleName("button-fichier-menu");
		buttonPanel.add(btnAnnuler);
		
		FlexTable flexTable = new FlexTable();
		panel.add(flexTable);
		flexTable.setWidth("100%");
		
		Label label = new Label("À:");
		flexTable.setWidget(0, 0, label);
		flexTable.getCellFormatter().setWidth(0, 0, "20px");
		
		flexTable.setWidget(0, 1, emailTo);
		emailTo.setSize("99%", "20px");
		
		Label lblObject = new Label("Objet:");
		flexTable.setWidget(1, 0, lblObject);
		
		flexTable.setWidget(1, 1, objet);
		objet.setSize("99%", "20px");
		
		ScrollPanel scrollPanel = new ScrollPanel();
		panel.add(scrollPanel);
		scrollPanel.setHeight("280px");
		
		scrollPanel.setWidget(content);
		content.setSize("99%", "97%");
		lblError.setStyleName("label-error");
		
		panel.add(lblError);
		lblError.setWidth("100%");
		
		panel.add(horizontalPanel);
		horizontalPanel.setWidth("100%");
		btnEnvoiDiffr.setStyleName("button-fichier-menu");
		
		horizontalPanel.add(btnEnvoiDiffr);
		
		horizontalPanel.add(timePanel);
		horizontalPanel.setCellHorizontalAlignment(timePanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		timePanel.add(dayListBox);
		
		timePanel.add(monthListBox);
		
		timePanel.add(yearListBox);
		
		timePanel.add(hourListBox);
		
		timePanel.add(minListBox);
		
		panel.add(sendLoader);
	}

}
