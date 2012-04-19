/**
 * 
 */
package com.geek.afric.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import com.geek.afric.client.AppManager;
import com.geek.afric.client.presenter.MailPresenter.Display;
import com.geek.afric.client.view.editor.Editor;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.Mail;
import com.geek.afric.shared.User;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Mcicheick
 * 
 */
@SuppressWarnings("deprecation")
public class MailView extends Composite implements Display {

	public interface Listener extends StandardListener{

		void onClickSupprimer(ArrayList<Mail> mailsToDelete);

		void onClickEnvoyer(Mail mail);

		void onItemSelected(Mail item);

		void onClickEnvoiDiff(Integer jour, Integer mois, Integer annee,
				Integer heure, Integer min, Mail mail);

		void onClickInbox();

		void onClickWrite();

		void onClickSent();

		void update();

		void onClickRoot();

		void onClickHeader(String order1);

		void setEmails();

		void onClickTitle(Fichier file);

	}
	
	private class SelectionStyle {
		String selectedRow() {
			return "maillist-selectedRow";
		}

		String unRead() {
			return "maillist-unRead";
		}

		String date() {
			return "maillist-date";
		}
		
		String selectedHeader() {
			return "header-selection";
		}
	}
	

	Listener listener;
	TopPanel topPanel = new TopPanel();
	ScrollPanel leftPanel = new ScrollPanel();
	VerticalPanel leftContent = new VerticalPanel();

	VerticalPanel panel = new VerticalPanel();
	HorizontalPanel northPanel = new HorizontalPanel();
	SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
	StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.EM);
	DockLayoutPanel dockPanel = new DockLayoutPanel(Unit.EM);
	FlexTable table = new FlexTable();
	FlexTable bodyInfoTable = new FlexTable();
	ScrollPanel scrollBody = new ScrollPanel();
	Label labelSubject;
	Label labelDate;
	Label labelFrom;
	Label labelTo;
	Label subject;
	Label date;
	Label from;
	Label to;
	HTML bodyHtml = new HTML("", true);
	Button btnSupprimer = new Button("Supprimer");
	Button btnRpondre = new Button("Répondre");
	Button btnTransferer = new Button("Transférer");
	Button btnEnvoyer = new Button("Envoyer");
	Button btnNouveauMessage = new Button("Nouveau Message");
	Button btnUpdate = new Button("Actualiser");

	User user;
	ArrayList<Mail> selectedMails = new ArrayList<Mail>();
	SelectionStyle selectionStyle;
	private ArrayList<Mail> items = new ArrayList<Mail>();
	private int selectedRow = -1;
	Message mail;
	Images images = AppManager.images;
	Image loader = new Image(AppManager.images.loaderData());
	//private final VerticalPanel verticalPanel = new VerticalPanel();
	private final FlexTable header = new FlexTable();
	private Widget mailHeader;
	
	Mailboxes mailboxes = new Mailboxes();
	
	public MailView(User user) {
		this.user = user;

		selectionStyle = new SelectionStyle();
		initWidget(panel);
		panel.setSize("720px", "470px");
		leftPanel.setWidth("155px");
		leftPanel.setHeight("420px");
		leftPanel.setWidget(leftContent);
		leftContent.setWidth("99%");

		panel.add(loader);
		panel.setStyleName("maillist-panel");
		loader.setVisible(false);
		
		panel.add(northPanel);
		northPanel.setWidth("100%");
		
		btnSupprimer.setStyleName("button-fichier-menu");
		btnSupprimer.setTitle("Supprimer les messages sélectionnés.");
		northPanel.add(btnSupprimer);
		
		btnRpondre.setStyleName("button-fichier-menu");
		btnRpondre.setTitle("Répondre au premier message sélectionné");
		northPanel.add(btnRpondre);
		
		btnTransferer.setStyleName("button-fichier-menu");
		btnTransferer.setTitle("Transférer le premier message sélectionné");
		northPanel.add(btnTransferer);
		
		btnEnvoyer.setStyleName("button-fichier-menu");
		btnEnvoyer.setTitle("Envoi du message");
		btnEnvoyer.setVisible(false);
		northPanel.add(btnEnvoyer);
		
		btnNouveauMessage.setStyleName("button-fichier-menu");
		btnNouveauMessage.setTitle("Écrire un nouveau message");
		northPanel.add(btnNouveauMessage);
		
		btnUpdate.setStyleName("button-fichier-menu");
		btnUpdate.setTitle("Relever les messages");
		northPanel.add(btnUpdate);

		panel.add(splitLayoutPanel);
		splitLayoutPanel.setSize("100%", "440px");

		splitLayoutPanel.addWest(stackPanel, 130.0);
		stackPanel.setSize("100%", "100%");

		splitLayoutPanel.addNorth(dockPanel, 135.0);
		dockPanel.setWidth("100%");
//		dockPanel.setHeight("100%");
		dockPanel.addNorth(header, 2);
//		header.setWidth("100%");
//		header.setHeight("25px");
		header.getCellFormatter().setWidth(0, 0, "192px");
		header.getCellFormatter().setWidth(0, 1, "192px");
		header.setText(0, 0, "De");
		header.setText(0, 1, "Objet");
		header.setText(0, 2, "Date");

		ScrollPanel tableScrollPanel = new ScrollPanel();
//		tableScrollPanel.setSize("100%", "110px");
		tableScrollPanel.setWidget(table);
		dockPanel.add(tableScrollPanel);
//		verticalPanel.setCellVerticalAlignment(tableScrollPanel, HasVerticalAlignment.ALIGN_TOP);
//		table.setSize("100%", "100%");

		ScrollPanel bodyScrollPanel = new ScrollPanel();
		splitLayoutPanel.add(bodyScrollPanel);
		bodyScrollPanel.setWidth("100%");

		VerticalPanel bodyPanel = new VerticalPanel();
		bodyScrollPanel.setWidget(bodyPanel);
		bodyPanel.setSize("100%", "100%");

		bodyPanel.add(bodyInfoTable);
		bodyInfoTable.setWidth("100%");

		bodyPanel.add(bodyHtml);

		table.setStyleName("maillist-table");
		header.setStyleName("maillist-header");
		table.setCellPadding(0);
		table.setCellSpacing(0);
		header.setCellPadding(0);
		header.setCellSpacing(0);
		addStyle(2);

		table.getColumnFormatter().setWidth(0, "192px");
		table.getColumnFormatter().setWidth(1, "192px");
		
		labelFrom = new Label(); from = new Label();
		labelSubject = new Label(); subject = new Label();
		labelDate = new Label(); date = new Label();
		labelTo = new Label(); to = new Label();
		
		bodyInfoTable.setWidget(0, 0, labelFrom);    bodyInfoTable.setWidget(0, 1, from);
		bodyInfoTable.setWidget(1, 0, labelSubject); bodyInfoTable.setWidget(1, 1, subject);
		bodyInfoTable.setWidget(2, 0, labelDate);    bodyInfoTable.setWidget(2, 1, date);
		bodyInfoTable.setWidget(3, 0, labelTo);      bodyInfoTable.setWidget(3, 1, to);
		bodyInfoTable.getColumnFormatter().setWidth(0, "35px");
		
		labelSubject.setText("Objet: ");
		labelDate.setText("Date: ");
		labelFrom.setText("De: ");
		labelTo.setText("À: ");
		
		labelFrom.setStyleName("maildetail-label");
		labelSubject.setStyleName("maildetail-label");
		labelDate.setStyleName("maildetail-label");
		labelTo.setStyleName("maildetail-label");
		date.setStyleName("maildetail-date");
		bodyInfoTable.setStyleName("maildetail-header");
		bodyHtml.setStyleName("maildetail-body");
		
		mailHeader = createHeaderWidget("Boites Mails",
				images.mailgroup());
		stackPanel.add(mailboxes, mailHeader, 3);
		
		bind();
	}

	private void bind() {
		btnSupprimer.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedMails.isEmpty()) {
					Window.alert("Veuillez selectionnez un message s'il vous plait");
					return;
				}
				String msg = selectedMails.size() == 1 ? "Voulez vous supprimer ce message ?"
						: "Voulez vous supprimer ces " + selectedMails.size()
								+ " messages ?";
				boolean b = Window.confirm(msg);
				if (b) {
					listener.onClickSupprimer(selectedMails);
					selectedMails.clear();
				}
			}
		});

		btnRpondre.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedMails.isEmpty()) {
					Window.alert("Veuillez selectionnez un message s'il vous plait");
					return;
				}
				Mail ml = selectedMails.get(0);
				String msg = ml.toString();
				mail = new Message();
				mail.content.setHTML(msg);
				mail.emailTo.setText(ml.getSender());
				mail.objet.setText("RE: " + ml.getObject());
				mail.center();
				selectedMails.clear();
				listener.setEmails();
			}
		});
		
		btnTransferer.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (selectedMails.isEmpty()) {
					Window.alert("Veuillez selectionnez un message s'il vous plait");
					return;
				}
				Mail ml = selectedMails.get(0);
				String msg = ml.toString();
				mail = new Message();
				mail.content.setHTML(msg);
				mail.emailTo.setText("");
				mail.objet.setText("FW: " + ml.getObject());
				mail.center();
				selectedMails.clear();
				listener.setEmails();
			}
		});
		
		btnNouveauMessage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				newMessage();
			}
		});
		
		btnUpdate.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				listener.update();
			}
		});
		
		table.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				click(event);
			}
		});
		
		header.addTableListener(new TableListener() {
			boolean de = false, object = false, date = false;
			
			@Override
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
				if(row == 0 && cell == 0) {
					addStyle(0);
					listener.onClickHeader((de = !de) ? "senderName asc" : "senderName desc");
				} else if(row == 0 && cell == 1) {
					addStyle(1);
					listener.onClickHeader((object = !object) ? "object asc" : "object desc");
				} else if(row == 0 && cell == 2) {
					addStyle(2);
					listener.onClickHeader((date = !date) ? "dateCreation desc" : "dateCreation asc");
				} else {
					addStyle(-1);
				}
			}
		});
	}
	
	private void addStyle(int column) {
		for(int i = 0; i < 3;  i++) {
			if(i != column)
				header.getCellFormatter().removeStyleName(0, i, selectionStyle.selectedHeader());
			else 
				header.getCellFormatter().addStyleName(0, i, selectionStyle.selectedHeader());
		}
	}
	
	public void setItem(Mail item) {
		subject.setText(item.getObject());
		from.setText(item.getSenderName().equals("Sender") ? item.getSender() : item.getSenderName());
		to.setText(item.getReceiverName().equals("Receiver") ? item.getReceiver() : item.getReceiverName());
		date.setText(item.getDateCreation().toLocaleString());
		bodyHtml.setHTML(item.getMessage());
	}

	public void clear() {
		subject.setText("");
		from.setText("");
		to.setText("");
		date.setText("");
		bodyHtml.setText("");
	}

	private void styleRow(int row, boolean selected) {
		if (row != -1) {
			String style = selectionStyle.selectedRow();
			String style1 = selectionStyle.unRead();
			if (selected) {
				table.getRowFormatter().addStyleName(row, style);
				table.getRowFormatter().removeStyleName(row, style1);
			} else {
				table.getRowFormatter().removeStyleName(row, style);
			}
		}
	}
	/**
	 * Selects the given row (relative to the current page).
	 * 
	 * @param row
	 *            the row to be selected
	 */
	private void selectRow(int row) {
		// When a row (other than the first one, which is used as a header) is
		// selected, display its associated MailItem.
		Mail item = getMail(row);
		for(int i = 0; i < items.size(); i++) {
			styleRow(i, false);
		}
		if (item == null) {
			return;
		}
		styleRow(row, true);

		selectedRow = row;
		
		setItem(item);

		if (listener != null) {
			listener.onItemSelected(item);
		}
	}
	
	private Mail getMail(int index) {
		if (index >= items.size() || index < 0) {
			return null;
		}
		return items.get(index);
	}
	
	private void selectRows(int row) {
		// When a row (other than the first one, which is used as a header) is
		// selected, display its associated MailItem.
		if(row == -1) {
			for(int i = 0; i < items.size(); i++) {
				styleRow(i, false);
			}
			return;
		}
		
		Mail item = getMail(row);
		if (item == null) {
			return;
		}
		
		if(!selectedMails.contains(item)) {
			styleRow(row, true);
			selectedMails.add(item);
		} else {
			styleRow(row, false);
			selectedMails.remove(item);
		}
	}

	public Mail getRowMail() {
		return getMail(selectedRow);
	}
	
	private void click(ClickEvent event) {
		Cell cell = table.getCellForEvent(event);
		if (cell != null) {
			int row = cell.getRowIndex();
			if(event.isControlKeyDown() || event.isMetaKeyDown()) {
				selectRows(row);
			} else {
				selectRow(row);
				selectedMails.clear();
				selectedMails.add(getRowMail());
			}
		} else {
			selectRows(-1);
			selectedMails.clear();
		}
	}
	
	private void update(int count) {
		int i = 0;
		for (; i < count; i++) {

			Mail item = getMail(i);
			// Add a new row to the table, then set each of its columns to the
			// email's sender and subject values.
			table.setText(i, 0, item.getSenderName());
			table.setText(i, 1, item.getObject());
			table.setText(i, 2, item.getDateCreation().toString());
			String style1 = selectionStyle.date();
			table.getCellFormatter().addStyleName(i, 2, style1);
			String style = selectionStyle.unRead();
			if (!item.isRead() && user.isReceiver(item)) {
				table.getRowFormatter().addStyleName(i, style);
			} else {
				table.getRowFormatter().removeStyleName(i, style);
			}
			table.getRowFormatter().addStyleName(i, "maillist-row");
		}

		// Clear any remaining slots.
		for (; table.getRowCount() > count; ) {
			table.removeRow(table.getRowCount() - 1);
		}
	}
	
	private Widget createHeaderWidget(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setHeight("100%");
		hPanel.setSpacing(0);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Image img = new Image(image);
		hPanel.add(img);
		HTML headerText = new HTML(text);
		hPanel.add(headerText);
		return new SimplePanel(hPanel);
	}
	
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
		panel.setCellHorizontalAlignment(date, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(new HTML("<hr style='border:1px solid #d0def2;'>"));
		panel.setWidth("100%");
		return panel;
	}
	
	@SuppressWarnings("unused")
	private Widget createMailItem(Mail item) {
		HorizontalPanel panel = new HorizontalPanel();
		return panel;
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
	public void setLoader(boolean load) {
		if(mail != null) {
			mail.sendLoader.setVisible(load);
			mail.btnEnvoyer.setEnabled(!load);
		} else {
			loader.setVisible(load);
			splitLayoutPanel.setVisible(!load);
			northPanel.setVisible(!load);
		}
	}
	
	
	@Override
	public void setError(String message) {
		if(mail != null) {
			mail.setMessage(message);
		}
	}
	
	
	@Override
	public void setMails(ArrayList<Mail> mails) {
		items = mails;
		selectedMails.clear();
		selectedRow = -1;
		selectRow(selectedRow);
		update(mails.size());
		clear();
	}
	
	@Override
	public void newMessage() {
		mail = new Message();
		mail.center();
		listener.setEmails();
	}
	
	@Override
	public void setTokens(Set<String> tokens) {
		topPanel.setTokens(tokens);
	}

	@Override
	public String getSelectToken() {
		return topPanel.recherche.getText();
	}
	
	@Override
	public void setEmails(Set<String> mails) {
		if(mail != null) {
			mail.setEmails(mails);
		}
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
	
	class Message extends DialogBox {
		
		FieldVerifier verifier = new FieldVerifier();
		VerticalPanel panel = new VerticalPanel();
		Button btnEnvoyer = new Button("Envoyer");
		Button btnAnnuler = new Button("Annuler");
		private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		SuggestBox emailTo = new SuggestBox(oracle);
		TextBox objet = new TextBox();
		Editor content = new Editor();
		Label labelError;
		Image sendLoader = new Image(AppManager.images.defaultLoader());

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
			setHTML("<center>Envoie d'email</center>");
			setWidget(panel);
			panel.setSize("600px", "435px");
			
			HorizontalPanel buttonPanel = new HorizontalPanel();
			panel.add(buttonPanel);
			panel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
			
			btnEnvoyer.setStyleName("button-fichier-menu");
			btnEnvoyer.setTitle("Envoi du message");
			buttonPanel.add(btnEnvoyer);
			
			btnAnnuler.setStyleName("button-fichier-menu");
			btnAnnuler.setTitle("Fermer la fenêtre");
			buttonPanel.add(btnAnnuler);
			
			FlexTable flexTable = new FlexTable();
			panel.add(flexTable);
			flexTable.setWidth("100%");
			
			Label label = new Label("À:");
			flexTable.setWidget(0, 0, label);
			flexTable.getCellFormatter().setWidth(0, 0, "20px");
			
			flexTable.setWidget(0, 1, emailTo);
			emailTo.setWidth("99%");
			emailTo.getTextBox().setName("Le champ email");
			//emailTo.setStyleName("login-field");
			
			Label lblObject = new Label("Objet:");
			flexTable.setWidget(1, 0, lblObject);
			
			flexTable.setWidget(1, 1, objet);
			objet.setWidth("99%");
			//objet.setStyleName("login-field");
			objet.setName("Le champ object");
			
			ScrollPanel scrollPanel = new ScrollPanel();
			panel.add(scrollPanel);
			scrollPanel.setHeight("280px");
			
			scrollPanel.setWidget(content);
			content.setSize("100%", "100%");
			content.setAreaSize("100%", "205px");
			
			labelError = verifier.getLabel();
			labelError.setStyleName("label-error");
			panel.add(labelError);
			labelError.setWidth("100%");
			
			minListBox.setStyleName("button-fichier-menu");
			minListBox.setSelectedIndex(0);
			minListBox.setName("Le champ minutes");
			minListBox.setTitle("Les minutes");
			
			hourListBox.setStyleName("button-fichier-menu");
			hourListBox.setSelectedIndex(0);
			hourListBox.setName("Le champ heures");
			hourListBox.setTitle("Les heures");
			
			yearListBox.setStyleName("button-fichier-menu");
			yearListBox.setSelectedIndex(0);
			yearListBox.setName("Le champ années");
			yearListBox.setTitle("Les années");
			
			monthListBox.setStyleName("button-fichier-menu");
			monthListBox.setSelectedIndex(0);
			monthListBox.setName("Le champ mois");
			monthListBox.setTitle("Les mois");
			
			dayListBox.setStyleName("button-fichier-menu");
			dayListBox.setSelectedIndex(0);
			dayListBox.setName("Le champ jours");
			dayListBox.setTitle("Les jours");
			
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

			sendLoader.setVisible(false);
			panel.add(sendLoader);
			bind();
		}
		
		private void bind() {
			btnAnnuler.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					hide();
					emailTo.setText("");
					objet.setText("");
					content.setText("");
					mail = null;
				}
			});
			
			btnEnvoyer.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					boolean r = verifier.isValidEmail(emailTo);
					if(!r)
						return;
					String email = emailTo.getText();
					String obj = objet.getText().isEmpty() ? "Aucun object" : objet.getText();
					String message = content.getHTML();
					Mail mail = new Mail(user.getEmail(), email, obj, message);
					mail.setRead(false);
					mail.setSenderName(user.getDisplayname());
					listener.onClickEnvoyer(mail);
				}
			});
			
			emailTo.getTextBox().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					labelError.setText("");
				}
			});
			
			btnEnvoiDiffr.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					boolean r = verifier.isValidEmail(emailTo);
					if(!r)
						return;
					r = verifier.isValidListBox(dayListBox);
					if(!r)
						return;
					r =  verifier.isValidListBox(monthListBox);
					if(!r)
						return;
					r =  verifier.isValidListBox(yearListBox);
					if(!r)
						return;
					r =  verifier.isValidListBox(hourListBox);
					if(!r)
						return;
					r =  verifier.isValidListBox(minListBox);
					if(!r)
						return;
					String email = emailTo.getText();
					String obj = objet.getText().isEmpty() ? "Aucun object" : objet.getText();
					String message = content.getHTML();
					Mail ml = new Mail(user.getEmail(), email, obj, message);
					ml.setRead(false);
					ml.setSenderName(user.getDisplayname());
					Integer jour = new Integer(dayListBox.getItemText(dayListBox.getSelectedIndex()));
					Integer mois = new Integer(monthListBox.getItemText(monthListBox.getSelectedIndex()));
					Integer annee = new Integer(yearListBox.getItemText(yearListBox.getSelectedIndex()));
					Integer heure = new Integer(hourListBox.getItemText(hourListBox.getSelectedIndex()));
					Integer min = new Integer(minListBox.getItemText(minListBox.getSelectedIndex()));
					listener.onClickEnvoiDiff(jour, mois, annee, heure, min, ml);
				}
			});
			
			dayListBox.addItem("jours");
			for(int i = 1; i <= 31; i++) {
				dayListBox.addItem((i < 10) ? "0"+i : ""+i);
			}
			
			monthListBox.addItem("mois");
			for(int i = 1; i <= 12; i++) {
				monthListBox.addItem((i < 10) ? "0"+i : ""+i);
			}
			
			yearListBox.addItem("année");
			for(int i = 2012; i <= 2020; i++) {
				yearListBox.addItem(""+i);
			}
			
			hourListBox.addItem("heures");
			for(int i = 0; i < 24; i++) {
				hourListBox.addItem((i < 10) ? "0"+i : ""+i);
			}
			
			minListBox.addItem("mins");
			for(int i = 0; i < 60; i += 5) {
				minListBox.addItem((i < 10) ? "0"+i : ""+i);
			}
			
			Date date = new Date();
			int j = date.getDate();
			int m = date.getMonth();
			int a = date.getYear() + 1900;
			int h = date.getHours();
			int mn = date.getMinutes();
			minListBox.setSelectedIndex(mn/5 + 1);
			hourListBox.setSelectedIndex(h + 1);
			yearListBox.setSelectedIndex(a - 2012 + 1);
			monthListBox.setSelectedIndex(m + 1);
			dayListBox.setSelectedIndex(j);
		}
		
		public void setMessage(String message) {
			if(message.equals("close")) {
				hide();
				mail = null;
			} else {
				labelError.setText(message);
			}
		}
		
		public void setEmails(Set<String> mails) {
			oracle.addAll(mails);
		}
	}

	class Mailboxes extends Composite {

		Tree tree;
		TreeItem root;
		TreeItem inbox;
		TreeItem write;
		TreeItem sent;

		/**
		 * Constructs a new mailboxes widget with a bundle of images.
		 * 
		 * @param images
		 *            a bundle that provides the images for this widget
		 */

		public Mailboxes() {

			tree = new Tree(images);
			root = new TreeItem(imageItemHTML(images.home(),
					"mailboxes"));
			tree.addItem(root);

			inbox = creatImageItem(root, "Réçus", images.inbox());
			write = creatImageItem(root, "Écrire", images.write());
			sent = creatImageItem(root, "Envoyés", images.sent());
			
			root.addItem(inbox);
			tree.setSelectedItem(inbox);
			root.addItem(write);
			root.addItem(sent);
			root.setState(true);
			initWidget(tree);
			bind();
		}
		
		private void bind() {
			tree.addTreeListener(new TreeListener() {
				
				@Override
				public void onTreeItemStateChanged(TreeItem item) {
				}
				
				@Override
				public void onTreeItemSelected(TreeItem item) {
					if(item.equals(inbox)) {
						listener.onClickInbox();
					} else if(item.equals(write)) {
						listener.onClickWrite();
					} else if(item.equals(sent)) {
						listener.onClickSent();
					} else if(item.equals(root)) {
						listener.onClickRoot();
					}
				}
			});
		}
		
		/**
		 * A helper method to simplify adding tree items that have attached images.
		 * {@link #creatImageItem(TreeItem, String, ImageResource) code}
		 * 
		 * @param root
		 *            the tree item to which the new item will be added.
		 * @param title
		 *            the text associated with this item.
		 */
		private TreeItem creatImageItem(TreeItem root, String title,
				ImageResource imageProto) {
			TreeItem item = new TreeItem(imageItemHTML(imageProto, title));
			return item;
		}

		/**
		 * Generates HTML for a tree item with an attached icon.
		 * 
		 * @param imageProto
		 *            the image prototype to use
		 * @param title
		 *            the title of the item
		 * @return the resultant HTML
		 */
		private String imageItemHTML(ImageResource imageProto, String title) {
			return AbstractImagePrototype.create(imageProto).getHTML() + " "
					+ title;
		}
	}
	
	public Widget getTopPanel() {
		return topPanel;
	}
	
	public Widget getLeftPanel() {
		return leftPanel;
	}

}
