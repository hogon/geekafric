/**
 * 
 */
package com.geek.afric.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geek.afric.client.AppManager;
import com.geek.afric.client.presenter.FichierListPresenter.Display;
import com.geek.afric.shared.AbstractFichier;
import com.geek.afric.shared.Dossier;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.User;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author Mcicheick
 * 
 */
public class FichierList extends Composite implements Display {

	public interface Listener extends StandardListener {

		void onClickTitle(Fichier file);

		void onClickDelete(ArrayList<AbstractFichier> deleteFiles);

		void onClickCreateFile(Dossier parent);

		void onClickBack();

		void onClickNext();

		void setOrder(String order);

		void clearCache();

		void onClickDossier(Dossier file);

		void onClickCreateDossier(Dossier dossier);

		void onClickUpdate(Dossier parent);

		void deleteFile(AbstractFichier toMove);

		void updateFile(Dossier file);

		void updateAll(Dossier dossier, ArrayList<AbstractFichier> selectedFiles);

	}

	public static final int VISIBLE_FILE = 15;

	Listener listener;
	TopPanel topPanel = new TopPanel();
	ScrollPanel leftPanel = new ScrollPanel();
	VerticalPanel leftContent = new VerticalPanel();
	VerticalPanel panel = new VerticalPanel();
	VerticalPanel content = new VerticalPanel();
	Button delete = new Button();
	Button createFile = new Button();
	Button createDossier = new Button();
	Button editDossier = new Button("Modifier");
	Image loader = new Image(AppManager.images.defaultLoader());

	ArrayList<AbstractFichier> selectedFiles = new ArrayList<AbstractFichier>();
	Map<String, Dossier> subFolders = new HashMap<String, Dossier>();
	Map<String, Map<String, Dossier>> mapFolder = new HashMap<String, Map<String, Dossier>>();
	private int startIndex;

	private NavBar navBar = new NavBar();
	private final ListBox listBox = new ListBox();
	Label clearCache = new Label();
	private final FlexTable flexTable = new FlexTable();
	ScrollPanel scrollPanel = new ScrollPanel();
	User user;

	Dossier parent;
	AddDossier addDossier;

	ArrayList<Dossier> history = new ArrayList<Dossier>();
	int historyIndex = 0;
	Button back = new Button();
	Button next = new Button();

	boolean update = false;

	public FichierList(User user) {
		this.user = user;
		initWidget(panel);
		panel.setSize("600px", "470px");
		panel.setSpacing(2);
		leftPanel.setWidth("155px");
		leftPanel.setHeight("420px");
		leftPanel.setWidget(leftContent);
		leftContent.setWidth("99%");
		panel.add(loader);
		loader.setVisible(false);

		panel.add(flexTable);
		flexTable.setWidth("100%");

		HorizontalPanel buttonPanel = new HorizontalPanel();
		flexTable.setWidget(0, 0, buttonPanel);
		createFile.setText("Nouveau");
		createFile.setStyleName("button-fichier-menu");
		createFile.setTitle("Ajouter un fichier");
		buttonPanel.add(createFile);

		createDossier.setText("Nouveau dossier");
		createDossier.setStyleName("button-fichier-menu");
		createDossier.setTitle("Ajouter un dossier");
		buttonPanel.add(createDossier);

		editDossier.setStyleName("button-fichier-menu");
		editDossier.setTitle("Modifie le nom du dossier courant");
		buttonPanel.add(editDossier);

		delete.setText("Supprimer");
		delete.setStyleName("button-fichier-menu");
		buttonPanel.add(delete);

		flexTable.setWidget(0, 1, listBox);
		flexTable.getCellFormatter().setStyleName(0, 1, "style-left");
		listBox.setStyleName("button-fichier-menu");
		listBox.addItem("Déplacer dans:");
		flexTable.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);

		panel.add(scrollPanel);
		scrollPanel.setSize("600px", "420px");

		scrollPanel.setWidget(content);
		content.setWidth("99%");
		panel.add(navBar);
		panel.setCellHorizontalAlignment(navBar,
				HasHorizontalAlignment.ALIGN_CENTER);
		clearCache.setText("Vider le cache");
		topPanel.insert(clearCache, 0);
		clearCache.setVisible(user.isAdmin());

		HorizontalPanel historyNav = new HorizontalPanel();
		back.setTitle("Retourner au dossier précédent");
		back.setStyleName("button-fichier-menu");
		back.setText("<");
		back.setEnabled(false);
		historyNav.add(back);
		next.setTitle("Aller au dossier suivant");
		next.setStyleName("button-fichier-menu");
		next.setText(">");
		next.setEnabled(false);
		historyNav.add(next);
		buttonPanel.add(historyNav);
		buttonPanel.setCellHorizontalAlignment(historyNav,
				HasHorizontalAlignment.ALIGN_RIGHT);
		bind();
	}

	private void bind() {
		createFile.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onClickCreateFile(parent);
			}
		});

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedFiles.isEmpty()) {
					Window.alert("Veuillez sélectionner un fichier");
				} else {
					String msg = (selectedFiles.size() > 1) ? "ces "
							+ selectedFiles.size() + " fichiers cochés ?"
							: "ce fichier ?";
					boolean b = Window
							.confirm("Voulez vous vraiment supprimer " + msg);
					if (b) {
						history.clear();
						historyIndex = 0;
						setHistoryState(historyIndex);
						listener.onClickDelete(selectedFiles);
					} else {
						setDossier(parent);
					}
					selectedFiles.clear();
				}
			}
		});

		listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String path = listBox.getItemText(listBox.getSelectedIndex());
				if (listBox.getSelectedIndex() == 0)
					return;
				if (selectedFiles.isEmpty()) {
					if(path.equals("/")) {
						listener.onClickDossier(null);
					}
					listBox.setSelectedIndex(0);
					return;
				}
				Dossier dossier = subFolders.get(path);
				if (path.equals("/")) {
					dossier = new Dossier(user.getDisplayname(), "", user
							.getId(), false, true, null);
				}
				if (dossier == null) {
					listBox.setSelectedIndex(0);
					return;
				}
				for (int i = 0; i < selectedFiles.size(); i++) {
					AbstractFichier file = selectedFiles.get(i);
					if (file.getId().equals(dossier.getId())) {
						listBox.setSelectedIndex(0);
						return;
					}
					if (file instanceof Dossier
							&& ((Dossier) file).isChild(dossier)) {
						Window.alert("Déplacement impossible."
								+ "\nLe dossier \""
								+ file.getTitle()
								+ "\" "
								+ "ne peut pas être déplacer dans un de ses sous-dossier");
						return;
					}
				}

				for (int i = 0; i < selectedFiles.size(); i++) {
					AbstractFichier file = selectedFiles.get(i);
					if (!dossier.contains(file)) {
						file.setParentId(dossier.getId());
						String chemin = "";
						if (dossier.getPath() != null)
							chemin = dossier.getPath();
						if (dossier.getTitle() != null
								&& !dossier.getTitle().isEmpty())
							chemin = chemin + dossier.getTitle();
						file.setPath(chemin);
						dossier.add(file);
						parent.remove(file);
					} else {
						selectedFiles.remove(file);
					}
				}
				mapFolder.clear();
				if (selectedFiles.isEmpty()) {
					if (dossier.getId() == null) {
						setDossier(parent);
					} else {
						setDossier(dossier);
					}
					return;
				}
				selectedFiles.add(parent);
				parent = null;
				listener.updateAll(dossier, selectedFiles);
				selectedFiles.clear();
			}
		});

		clearCache.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.clearCache();
			}
		});

		createDossier.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addDossier = new AddDossier(parent);
				addDossier.center();
			}
		});

		editDossier.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (parent.getId() == null)
					return;
				update = true;
				addDossier = new AddDossier(parent);
				addDossier.center();
			}
		});

		back.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Dossier dosssier = history.get(--historyIndex);
				setDossier(dosssier);
				setHistoryState(historyIndex);
			}
		});

		next.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Dossier dosssier = history.get(++historyIndex);
				setDossier(dosssier);
				setHistoryState(historyIndex);
			}
		});
	}

	@SuppressWarnings("deprecation")
	private Widget createFichierPanel(final Fichier file) {
		HorizontalPanel panel = new HorizontalPanel();
		CheckBox check = new CheckBox();
		check.setWidth("10px");
		check.setEnabled(user.isMine(file));
		VerticalPanel vpanel = new VerticalPanel();
		Label title = new Label();
		title.setText(file.getTitle());
		title.setTitle(file.toString());
		title.setWidth("100%");
		HorizontalPanel info = new HorizontalPanel();
		info.setWidth("100%");
		info.setSpacing(4);
		Label name = new Label();
		name.setText(file.getAuthor());
		name.setTitle("L'éditeur");
		name.setStyleName("date");
		info.add(name);
		Label date = new Label();
		date.setText(file.getDateCreation().toLocaleString());
		date.setTitle("Dernière modification "
				+ file.getDateDerniereModif().toLocaleString());
		date.setStyleName("date");
		info.setCellHorizontalAlignment(date,
				HasHorizontalAlignment.ALIGN_RIGHT);
		info.add(date);
		panel.add(check);
		panel.add(vpanel);
		vpanel.add(title);
		vpanel.add(info);
		vpanel.setWidth("100%");
		panel.addStyleName("left-menu-fichier");
		panel.setWidth("100%");
		title.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickTitle(file);
				}
			}
		});

		check.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).isChecked();
				if (checked) {
					selectedFiles.add(file);
				} else {
					selectedFiles.remove(file);
				}
				if (selectedFiles.isEmpty())
					delete.setEnabled(false);
				else
					delete.setEnabled(true);
			}
		});
		return panel;
	}

	@SuppressWarnings("deprecation")
	private Widget createLeftFile(final Fichier file) {
		VerticalPanel panel = new VerticalPanel();
		Label title = new Label();
		title.setText(file.getTitle());
		title.setTitle(file.toString());
		title.setWidth("100%");
		panel.setStyleName("left-menu-fichier");
		title.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickTitle(file);
				}
			}
		});
		Label date = new Label();
		date.setStyleName("date");
		date.setText(file.getDateCreation().toLocaleString());
		date.setWidth("100%");
		panel.add(title);
		panel.add(date);
		panel.setCellHorizontalAlignment(date,
				HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(new HTML("<hr style='border:1px solid #d0def2;'>"));
		panel.setWidth("100%");
		return panel;
	}

	private Widget createFileIcon(final Fichier file) {
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		CheckBox check = new CheckBox();
		check.setWidth("10px");
		check.setEnabled(user.isMine(file));
		check.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).isChecked();
				if (checked) {
					selectedFiles.add(file);
				} else {
					selectedFiles.remove(file);
				}
				if (selectedFiles.isEmpty())
					delete.setEnabled(false);
				else
					delete.setEnabled(true);
			}
		});
		hpanel.add(check);
		Image ico = new Image(AppManager.images.fichier());
		if(file.getTitle().endsWith(".java")) {
			ico.setResource(AppManager.images.javaIcon());
		}
		ico.setTitle(file.toString());
		ico.setWidth("50px");
		ico.setHeight("50px");
		ico.setStyleName("photo");
		ico.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickTitle(file);
				}
			}
		});
		hpanel.add(ico);
		panel.add(hpanel);
		Label title = new Label();
		title.setText(file.getTitle());
		panel.add(title);
		panel.setWidth("60px");
		panel.setHeight("80px");
		return panel;
	}

	private Widget createDossierIcon(final Dossier file) {
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		CheckBox check = new CheckBox();
		check.setWidth("10px");
		check.setEnabled(user.isMine(file));
		check.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).isChecked();
				if (checked) {
					selectedFiles.add(file);
				} else {
					selectedFiles.remove(file);
				}
				if (selectedFiles.isEmpty())
					delete.setEnabled(false);
				else
					delete.setEnabled(true);
			}
		});
		hpanel.add(check);
		Image ico = new Image(AppManager.images.dossier());
		ico.setTitle(file.toString());
		ico.setWidth("50px");
		ico.setHeight("50px");
		ico.setStyleName("photo");
		ico.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (history.contains(file)) {
					history.remove(file);
					history.add(file);
					historyIndex = history.size() - 1;
					setHistoryState(historyIndex);
				} else {
					history.add(file);
					historyIndex = history.size() - 1;
					setHistoryState(historyIndex);
				}
				setDossier(file);
			}
		});
		hpanel.add(ico);
		Button addDos = new Button("+");
		addDos.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addDossier = new AddDossier(file);
				addDossier.center();
			}
		});
		// hpanel.add(addDos);
		panel.add(hpanel);
		Label title = new Label();
		title.setText(file.getTitle());
		panel.add(title);
		panel.setWidth("60px");
		panel.setHeight("80px");
		return panel;
	}

	private void setHistoryState(int index) {
		if (index > 0) {
			back.setEnabled(true);
		} else {
			back.setEnabled(false);
		}

		if (index < history.size() - 1) {
			next.setEnabled(true);
		} else {
			next.setEnabled(false);
		}
	}

	@SuppressWarnings("unused")
	private Widget createDossierPanel(ArrayList<AbstractFichier> all) {
		Grid grid = new Grid();
		int i = 0;
		int j = 0;
		for (int k = 0; k < all.size(); k++) {
			AbstractFichier file = all.get(k);
			if (file instanceof Fichier) {
				grid.setWidget(i, j, createFileIcon((Fichier) file));
			} else if (file instanceof Dossier) {
				grid.setWidget(i, j, createDossierIcon((Dossier) file));
			}
			j = (j + 1) % 8;
			i = (j % 8 == 0) ? i + 1 : i;

		}
		grid.setSize("99%", "99%");
		return grid;
	}

	private void update(ArrayList<Fichier> files) {
		if (files == null || files.isEmpty()) {
			navBar.update(0, 0, 1);
			content.clear();
			leftContent.removeStyleName("left-menu-list");
			return;
		}
		leftContent.addStyleName("left-menu-list");
		int count = files.size();
		int max = startIndex + VISIBLE_FILE;
		if (max > startIndex + count) {
			max = startIndex + count;
		}
		if (count > VISIBLE_FILE)
			max = count;
		// Update the nav bar.
		navBar.update(startIndex, count, max);
		int i = 0;
		content.clear();
		for (; i < files.size(); i++) {
			content.add(createFichierPanel(files.get(i)));
		}
		scrollPanel.setWidget(content);
	}

	private void initMap(Dossier dossier) {
		ArrayList<Dossier> folders = (ArrayList<Dossier>) dossier.getFolders();
		for (int i = 0; i < folders.size(); i++) {
			Dossier dos = folders.get(i);
			String title = ((dos.getPath() == null) ? "" : dos.getPath()) + "/"
					+ dos.getTitle();
			subFolders.put(title, dos);
			listBox.addItem(title);
			initMap(dos);
		}
	}

	@Override
	public void setFichiers(ArrayList<Fichier> files) {
		update(files);
	}

	@Override
	public void setAbstractFichiers(ArrayList<AbstractFichier> files) {
		FlexTable grid = new FlexTable();
		if (parent == null) {
			parent = new Dossier(user.getDisplayname(), "", user.getId(),
					false, true, null);
			parent.setPath("");
			for (int i = 0; i < files.size(); i++) {
				parent.add(files.get(i));
			}
			history.add(parent);
			historyIndex = history.size() - 1;
			setHistoryState(historyIndex);
			setDossier(parent);
		}
		if (files == null || files.isEmpty()) {
			navBar.update(0, 0, 1);
			scrollPanel.setWidget(grid);
			leftContent.removeStyleName("left-menu-list");
			return;
		}
		leftContent.addStyleName("left-menu-list");
		int count = files.size();
		int max = startIndex + VISIBLE_FILE;
		if (max > startIndex + count) {
			max = startIndex + count;
		}
		if (count > VISIBLE_FILE)
			max = count;
		// Update the nav bar.
		navBar.update(startIndex, count, max);
		int i = 0;
		int j = 0;
		for (int k = 0; k < files.size(); k++) {
			AbstractFichier file = files.get(k);
			if (file instanceof Fichier) {
				grid.setWidget(i, j, createFileIcon((Fichier) file));
			} else if (file instanceof Dossier) {
				grid.setWidget(i, j, createDossierIcon((Dossier) file));
			}
			j = (j + 1) % 6;
			i = (j % 6 == 0) ? i + 1 : i;
		}
		grid.setSize("100%", "100%");
		scrollPanel.setWidget(grid);
	}

	@Override
	public void setDossier(Dossier dossier) {
		Window.setTitle(((dossier.getPath() == null) ? "" : dossier.getPath())
				+ "/" + dossier.getTitle());
		parent = dossier;
		editDossier.setVisible(user.isMine(parent));
		subFolders.clear();
		listBox.clear();
		listBox.addItem("Déplacer dans:");
		listBox.addItem("/");
		initMap(parent);
		listBox.setSelectedIndex(0);
		List<Dossier> folders = dossier.getFolders();
		List<Fichier> files = dossier.getFiles();
		ArrayList<AbstractFichier> all = new ArrayList<AbstractFichier>();
		all.addAll(folders);
		all.addAll(files);
		setAbstractFichiers(all);
	}

	@Override
	public void setLeftFichiers(ArrayList<Fichier> files) {
		if (files == null || files.isEmpty()) {
			leftContent.clear();
			return;
		}
		leftContent.clear();
		for (int i = 0; i < files.size(); i++) {
			leftContent.add(createLeftFile(files.get(i)));
		}
	}

	@Override
	public void setLoader(boolean load) {
		if (addDossier == null) {
			loader.setVisible(load);
			content.setVisible(!load);
		} else {
			addDossier.setLoader(load);
		}
	}

	@Override
	public void setListener(final Listener listener) {
		this.listener = listener;
		topPanel.setListener(new TopPanel.Listener() {

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
			public void onClickSearch(String token) {
				listener.onClickSearch(token);
			}

			@Override
			public SelectionHandler<Suggestion> getRechercheSelectHandler() {
				return listener.getRechercheSelectHandler();
			}
		});
		topPanel.recherche.addSelectionHandler(listener
				.getRechercheSelectHandler());
	}

	public Widget getTopPanel() {
		return topPanel;
	}

	public Widget getLeftPanel() {
		return leftPanel;
	}

	@Override
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public int getStratIndex() {
		return startIndex;
	}

	@Override
	public void setTokens(Set<String> tokens) {
		if (topPanel != null) {
			topPanel.setTokens(tokens);
		}
	}

	@Override
	public String getSelectToken() {
		if (topPanel != null)
			return topPanel.recherche.getText().trim();
		return null;
	}

	class NavBar extends Composite {
		HorizontalPanel panel = new HorizontalPanel();
		Label countLabel;
		Anchor newerButton;
		Anchor olderButton;

		public NavBar() {
			countLabel = new Label();
			newerButton = new Anchor();
			olderButton = new Anchor();

			newerButton.setStyleName("navbar-anchor");
			olderButton.setStyleName("navbar-anchor");

			panel.add(olderButton);
			panel.add(countLabel);
			panel.add(newerButton);

			newerButton.setText("Suivant >");
			olderButton.setText("< Précédent");

			newerButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					listener.onClickNext();
				}
			});

			olderButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					listener.onClickBack();
				}
			});
			initWidget(panel);
		}

		public void update(int startIndex, int count, int max) {
			setVisibility(olderButton, startIndex != 0);
			setVisibility(newerButton, max == startIndex + VISIBLE_FILE);
			countLabel.setText("" + (startIndex + 1) + " - " + max);
		}

		private void setVisibility(Widget widget, boolean visible) {
			widget.getElement()
					.getStyle()
					.setVisibility(
							visible ? Visibility.VISIBLE : Visibility.HIDDEN);
		}

		/**
		 * @return the newerButton
		 */
		public Anchor getNewerButton() {
			return newerButton;
		}

		/**
		 * @return the olderButton
		 */
		public Anchor getOlderButton() {
			return olderButton;
		}
	}

	@SuppressWarnings("unused")
	private AbstractFichier getFileByPath(String path, Dossier dossier) {
		AbstractFichier file = null;
		ArrayList<AbstractFichier> folders = (ArrayList<AbstractFichier>) dossier
				.getAllFiles();
		for (int i = 0; i < folders.size(); i++) {
			AbstractFichier dos = folders.get(i);
			file = ((dos.getPath() + "/") + dos.getTitle()).equals(path) ? dos
					: null;
			if (file != null)
				return file;
			if (dos instanceof Dossier) {
				file = getFileByPath(path, (Dossier) dos);
			}
		}
		return file;
	}

	class AddDossier extends DialogBox implements KeyUpHandler {
		FieldVerifier verifier = new FieldVerifier();
		TextBox titleBox = new TextBox();
		CheckBox publique = new CheckBox("publique");
		Label parentName = new Label();
		Label labelError = new Label();
		Button btnAnnuler = new Button("Annuler");
		Button btnValider = new Button("Valider");
		Image defaultLoader = new Image(AppManager.images.defaultLoader());
		Dossier parent;

		@SuppressWarnings("deprecation")
		public AddDossier(Dossier parent) {
			setHTML("<center>Nouveau Dossier</center>");
			setGlassStyleName("glass");
			labelError = verifier.getLabel();
			this.parent = parent;
			if (parent != null)
				parentName.setText(((parent.getPath() == null) ? "" : parent
						.getPath()) + "/" + parent.getTitle());
			FlexTable flexTable = new FlexTable();
			setWidget(flexTable);
			flexTable.setWidth("300px");

			titleBox.setStyleName("login-field");
			titleBox.getElement().setAttribute("placeholder", "Titre");
			titleBox.getElement().setAttribute("autocomplete", "on");
			titleBox.setName("Un titre");
			titleBox.setWidth("100%");
			titleBox.addKeyUpHandler(this);
			if (update) {
				titleBox.setText(parent.getTitle());
				publique.setChecked(parent.isPublique());
			}
			flexTable.setWidget(0, 0, titleBox);
			flexTable.getCellFormatter().setVerticalAlignment(0, 0,
					HasVerticalAlignment.ALIGN_TOP);

			flexTable.setWidget(1, 0, parentName);
			flexTable.getCellFormatter().setVerticalAlignment(1, 0,
					HasVerticalAlignment.ALIGN_TOP);

			flexTable.setWidget(2, 0, publique);
			flexTable.getCellFormatter().setVerticalAlignment(2, 0,
					HasVerticalAlignment.ALIGN_TOP);

			labelError.setStyleName("label-error");
			flexTable.setWidget(3, 0, labelError);

			HorizontalPanel buttonPanel = new HorizontalPanel();
			flexTable.setWidget(4, 0, buttonPanel);

			btnAnnuler.setStyleName("button-fichier-menu");
			buttonPanel.add(btnAnnuler);

			btnValider.setStyleName("button-fichier-menu");
			buttonPanel.add(btnValider);
			flexTable.getCellFormatter().setHorizontalAlignment(4, 0,
					HasHorizontalAlignment.ALIGN_RIGHT);

			flexTable.setWidget(5, 0, defaultLoader);
			flexTable.getCellFormatter().setVerticalAlignment(5, 0,
					HasVerticalAlignment.ALIGN_TOP);
			defaultLoader.setVisible(false);
			bind();
		}

		private void bind() {
			btnAnnuler.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					titleBox.setText("");
					setLoader(false);
				}
			});

			btnValider.addClickHandler(new ClickHandler() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(ClickEvent event) {
					boolean r = verifier.requireTextBox(titleBox);
					if (!r)
						return;
					if (!update) {
						String title = titleBox.getText();
						r = parent.contains(title);
						if (r) {
							labelError
									.setText("Ce dossier contient ce nom de fichier");
							return;
						}
						boolean pblq = publique.isChecked();
						Dossier dossier = new Dossier(user.getDisplayname(),
								title, user.getId(), pblq, true,
								(parent == null) ? null : parent.getId());
						String path = "";
						if (parent.getPath() != null)
							path = parent.getPath();
						if (parent.getTitle() != null
								&& !parent.getTitle().equals(""))
							path = path + "/" + parent.getTitle();
						dossier.setPath(path);
						history.clear();
						historyIndex = 0;
						setHistoryState(historyIndex);
						mapFolder.remove((parent.getId() == null) ? "" : parent
								.getId());
						listener.onClickCreateDossier(dossier);
						return;
					}
					String title = titleBox.getText();
					boolean pblq = publique.isChecked();
					if (title.equals(parent.getTitle())
							&& pblq == parent.isPublique()) {
						setLoader(false);
						return;
					}
					parent.setTitle(title);
					parent.setPublique(pblq);
					history.clear();
					historyIndex = 0;
					setHistoryState(historyIndex);
					listener.onClickUpdate(parent);
				}
			});
		}

		public void setLoader(boolean load) {
			defaultLoader.setVisible(load);
			if (!load) {
				hide();
				update = false;
				addDossier = null;
			}
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				btnValider.click();
			}
		}
	}
}
