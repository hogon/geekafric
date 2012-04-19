/**
 * 
 */
package com.geek.afric.client.view;

import java.util.ArrayList;
import java.util.Set;

import com.geek.afric.client.AppManager;
import com.geek.afric.client.presenter.AcceuilPresenter.Display;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author Cheick Mahady SISSOKO
 * 
 */
public class Acceuil extends Composite implements Display {
	public interface Listener extends StandardListener {

		void onClickTitle(Fichier file);

		void onClickShowFiles(String id);

		void onClickMails();

	}

	Listener listener;
	TopPanel topPanel = new TopPanel();

	Label mails = new Label();

	VerticalPanel leftPanel = new VerticalPanel();

	VerticalPanel panel = new VerticalPanel();
	FlexTable flexTable = new FlexTable();

	VerticalPanel topLeft = new VerticalPanel();
	VerticalPanel topRight = new VerticalPanel();
	VerticalPanel bottomLeft = new VerticalPanel();
	VerticalPanel bottomRight = new VerticalPanel();
	Image loader = new Image(AppManager.images.defaultLoader());

	User user;

	/**
	 * 
	 */
	public Acceuil(User user) {
		super();
		this.user = user;
		panel.add(loader);
		panel.setCellHorizontalAlignment(loader,
				HasHorizontalAlignment.ALIGN_CENTER);
		loader.setVisible(false);
		panel.add(flexTable);
		initWidget(panel);
		flexTable.setSize("600px", "480px");
		topLeft.setStyleName("home-panel");

		flexTable.setWidget(0, 0, topLeft);
		topLeft.setSize("100%", "100%");
		topRight.setStyleName("home-panel");

		flexTable.setWidget(0, 1, topRight);
		topRight.setSize("100%", "100%");
		bottomLeft.setStyleName("home-panel");

		flexTable.setWidget(1, 0, bottomLeft);
		bottomLeft.setSize("100%", "100%");
		bottomRight.setStyleName("home-panel");

		flexTable.setWidget(1, 1, bottomRight);
		bottomRight.setSize("100%", "100%");

		mails.setText("Mails");
		mails.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onClickMails();
			}
		});
		topPanel.insert(mails, 0);
	}

	private Widget createTopLeft() {
		VerticalPanel panel = new VerticalPanel();
		Label welcome = new Label();
		welcome.setText("Bienvenue dans ton space fichier");
		welcome.setStyleName("welcome-message");
		welcome.setWidth("100%");
		Label userName = new Label();
		userName.setText(user.getDisplayname());
		userName.setStyleName("style-name");
		userName.setWidth("100%");
		Button showFiles = new Button("Afficher vos fichiers");
		showFiles.setTitle("Cliquer pour voir la liste de vos fichiers");
		showFiles.setStyleName("button-fichier-menu");
		panel.setCellVerticalAlignment(showFiles,
				HasVerticalAlignment.ALIGN_BOTTOM);
		panel.add(welcome);
		panel.add(userName);
		panel.setCellHorizontalAlignment(userName,
				HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(showFiles);
		panel.setCellHorizontalAlignment(showFiles,
				HasHorizontalAlignment.ALIGN_RIGHT);
		showFiles.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onClickShowFiles(user.getId());
			}
		});
		panel.setWidth("100%");
		panel.setHeight("100%");
		return panel;

	}

	@SuppressWarnings("deprecation")
	private Widget createFichier(final Fichier file) {
		VerticalPanel panel = new VerticalPanel();
		Label title = new Label();
		title.setText(file.getTitle());
		title.setStyleName("label-title-acceuil");
		title.setWidth("100%");
		title.setTitle("Aller au fichier " + file.getTitle());
		title.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onClickTitle(file);
			}
		});
		panel.add(title);
		panel.setCellVerticalAlignment(title, HasVerticalAlignment.ALIGN_TOP);
		HTML html = new HTML();
		HTML content = new HTML(file.getContenu());
		String text = content.getText();
		html.setHTML(text.length() < 100 ? text : text.substring(0, 100)
				+ "...");
		html.setSize("100%", "100%");
		panel.add(html);
		Label date = new Label();
		date.setText(file.getDateDerniereModif().toLocaleString());
		date.setStyleName("date");
		panel.setCellHorizontalAlignment(date,
				HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(date);
		panel.setCellVerticalAlignment(date, HasVerticalAlignment.ALIGN_BOTTOM);
		panel.setWidth("100%");
		panel.setHeight("100%");
		return panel;
	}

	private Widget createLeftFile(final Fichier file) {
		VerticalPanel panel = new VerticalPanel();
		Label title = new Label();
		title.setText(file.getTitle());
		title.setTitle(file.toString());
		title.setStyleName("left-menu-fichier");
		title.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickTitle(file);
				}
			}
		});
		panel.add(title);
		Label date = new Label();
		date.setStyleName("date");
		panel.add(date);
		panel.setWidth("100%");
		return panel;
	}

	private Widget createEmptyFile() {
		VerticalPanel panel = new VerticalPanel();
		Label welcome = new Label();
		welcome.setText("Créer un nouveau fichier");
		welcome.setStyleName("label-title-acceuil");
		welcome.setWidth("100%");
		panel.add(welcome);
		// Label userName = new Label();
		// userName.setText(user.getDisplayname());
		// userName.setStyleName("style-name");
		// userName.setWidth("100%");
		// panel.add(userName);
		// panel.setCellHorizontalAlignment(userName,
		// HasHorizontalAlignment.ALIGN_CENTER);
		welcome.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onClickTitle(null);
			}
		});
		panel.setWidth("100%");
		panel.setHeight("100%");
		return panel;
	}

	@Override
	public void setFichiers(ArrayList<Fichier> files) {
		topLeft.clear();
		topLeft.add(createTopLeft());
		if (files == null || files.isEmpty()) {
			topRight.clear();
			topRight.add(createEmptyFile());
			bottomLeft.setVisible(false);
			bottomRight.setVisible(false);
			return;
		}
		if (files.size() == 1) {
			topRight.clear();
			topRight.add(createFichier(files.get(0)));
			topRight.setVisible(true);
			bottomLeft.clear();
			bottomLeft.add(createEmptyFile());
			bottomLeft.setVisible(true);
			bottomRight.setVisible(false);

		} else if (files.size() == 2) {
			topRight.clear();
			topRight.add(createFichier(files.get(0)));
			bottomLeft.clear();
			bottomLeft.add(createFichier(files.get(1)));
			bottomRight.clear();
			bottomRight.add(createEmptyFile());
			bottomLeft.setVisible(true);
			bottomRight.setVisible(true);
		} else if (files.size() >= 3) {
			topRight.clear();
			bottomLeft.clear();
			bottomRight.clear();
			topRight.add(createFichier(files.get(0)));
			bottomLeft.add(createFichier(files.get(1)));
			bottomRight.add(createFichier(files.get(2)));
		}
	}

	@Override
	public void setListener(final Listener listener) {
		this.listener = listener;
		topPanel.setListener(new TopPanel.Listener() {

			@Override
			public void onClickSearch(String token) {
				listener.onClickSearch(token);
			}

			@Override
			public void onClickPartages() {
				listener.onClickPartages();
			}

			@Override
			public void onClickFichiers() {
				listener.onClickFichiers();
			}

			@Override
			public void onClickDeconnexion() {
				listener.onClickDeconnexion();
			}

			@Override
			public void onClickAcceuil() {
				listener.onClickAcceuil();
			}

			@Override
			public SelectionHandler<Suggestion> getRechercheSelectHandler() {
				return listener.getRechercheSelectHandler();
			}
		});
	}

	@Override
	public void setTokens(Set<String> tokens) {
		if (topPanel != null) {
			topPanel.setTokens(tokens);
		}
	}

	@Override
	public String getSelectToken() {
		String token = null;
		if (topPanel != null)
			token = topPanel.recherche.getText();
		return token;
	}

	@Override
	public void setLeftFichiers(ArrayList<Fichier> files) {
		if (files == null || files.isEmpty())
			return;
		leftPanel.clear();
		for (int i = 0; i < files.size(); i++) {
			leftPanel.add(createLeftFile(files.get(i)));
		}
	}

	@Override
	public void setLoader(boolean load) {
		loader.setVisible(load);
		flexTable.setVisible(!load);
	}
	
	@Override
	public void setMailCount(int count) {
		mails.setText((count > 1) ? "Mails (" + count + " non lus)" : "Mails (" + count +" non lu)");
	}

	public Widget getTopPanel() {
		return topPanel;
	}

	public Widget getLeftPanel() {
		return leftPanel;
	}
}
