/**
 * 
 */
package com.geek.afric.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.geek.afric.client.AppManager;
import com.geek.afric.client.presenter.FichierPresenter.Display;
import com.geek.afric.client.view.editor.Editor;
import com.geek.afric.shared.Comment;
import com.geek.afric.shared.Fichier;
import com.geek.afric.shared.StandardMethod;
import com.geek.afric.shared.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Mcicheick
 * 
 */
public class FichierView extends Composite implements Display {

	public interface Listener extends StandardListener{

		void onClickSendFichier(com.geek.afric.shared.Fichier file,
				String email);

		void saveFile(Fichier file);

		void onClickDeleteFile(Fichier file);

		void onClickTitle(Fichier file);

		void onClickSearch(String text);

		void setEmails();

		void onDeleteComment(Comment comment);

		void onClickAddComment(Comment comment);
	}

	Listener listener;
	SendTo sendtoDialog;
	FieldVerifier verifier;
	boolean edit = true;

	VerticalPanel panel = new VerticalPanel();
	HorizontalPanel menus = new HorizontalPanel();
	Button deleteFile = new Button("Supprimer");
	Button editSave = new Button("Modifier");
	Button searchOk = new Button("OK");
	Button infoFichier = new Button("Info fichier");
	Button sendTo = new Button("Envoyer à");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	SuggestBox searchIn = new SuggestBox(oracle);
	ScrollPanel scrollPanel = new ScrollPanel();
	HTML content = new HTML();
	Editor contentEditable = new Editor();
	CheckBox publique = new CheckBox("Publique");
	Label dateLastEdit = new Label("");
	Fichier file;
	Fichier newFile;

	TopPanel topPanel = new TopPanel();
	ScrollPanel leftPanel = new ScrollPanel();
	private VerticalPanel leftContent = new VerticalPanel();
	private final TextBox labelTitle = new TextBox();
	private final Image saveLoader = new Image(
			AppManager.images.defaultLoader());
	private final Image loadLoader = new Image(
			AppManager.images.defaultLoader());
	private final Label labelError;
	private final Button createFile = new Button();
	private final Button retour = new Button("Retour");
	User user;

	private final VerticalPanel commentsPanel = new VerticalPanel();
	private final VerticalPanel addCommentPanel = new VerticalPanel();
	private final RichTextArea commentaire = new RichTextArea();
	private final Button addCommentButton = new Button();
	Button hideAddComment = new Button();
	Label showAddComment = new Label();
	Label showComments = new Label();
	

	boolean isShowComments = false;
	
	String parentId;
	private String path;
	
	FileUpload upload = new FileUpload();

	@SuppressWarnings("deprecation")
	public FichierView(User user) {
		this.user = user;
		verifier = new FieldVerifier();
		labelError = verifier.getLabel();
		initWidget(panel);
		panel.setSize("600px", "480px");
		leftPanel.setWidth("155px");
		leftPanel.setHeight("420px");
		leftPanel.setWidget(leftContent);
		leftContent.setWidth("99%");
		labelTitle.setTextAlignment(TextBoxBase.ALIGN_CENTER);
		labelTitle.setAlignment(TextAlignment.CENTER);
		labelTitle.setStyleName("label-title-fichier");
		labelTitle.setName("Le titre");
		panel.add(loadLoader);
		panel.add(labelTitle);
		labelTitle.setWidth("100%");
		panel.setCellHorizontalAlignment(labelTitle,
				HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellWidth(labelTitle, "100%");
		panel.add(menus);
		menus.setWidth("100%");
		
		createFile.setText("Nouveau");
		createFile.setStyleName("button-fichier-menu");
		createFile.setTitle("Créer un nouveau fichier");
		menus.add(createFile);

		deleteFile.setStyleName("button-fichier-menu");
		menus.add(deleteFile);
		deleteFile.setTitle("Supprimer le fichier");
		deleteFile.setVisible(false);

		editSave.setStyleName("button-fichier-menu");
		menus.add(editSave);
		editSave.setVisible(false);
		
		retour.setStyleName("button-fichier-menu");
		retour.setTitle("Retourner au fichier précédent");
		retour.setVisible(false);
		menus.add(retour);
		
		searchOk.setStyleName("button-fichier-menu");
		menus.add(searchOk);
		searchOk.setVisible(false);

		infoFichier.setStyleName("button-fichier-menu");
		infoFichier.setTitle("Obtenir des informations sur le fichier");
		//infoFichier.setVisible(false);
		//menus.add(infoFichier);

		sendTo.setStyleName("button-fichier-menu");
		sendTo.setTitle("Envoyer une copie du fichier à quelqu'un");
		menus.add(sendTo);
		searchIn.setAnimationEnabled(true);

		searchIn.setStyleName("search-fichier");
		searchIn.getElement().setAttribute("placeholder", "Rechercher dans");
		searchIn.getElement().setAttribute("type", "search");
		searchIn.getTextBox().setEnabled(true);
		searchIn.setTitle("Chercher un mot, une expression dans ce fichier");
		menus.add(searchIn);
		menus.setCellVerticalAlignment(searchIn,
				HasVerticalAlignment.ALIGN_MIDDLE);
		menus.setCellHorizontalAlignment(searchIn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		panel.add(scrollPanel);
		scrollPanel.setHeight("385px");

		scrollPanel.setWidget(content);
		content.setSize("100%", "100%");
		contentEditable.setSize("100%", "100%");
		contentEditable.setAreaSize("100%", "310px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();

		panel.add(horizontalPanel);
		horizontalPanel.setWidth("100%");
		horizontalPanel.add(saveLoader);
		saveLoader.setVisible(false);

		horizontalPanel.add(publique);

		horizontalPanel.add(dateLastEdit);
		horizontalPanel.setCellHorizontalAlignment(dateLastEdit,
				HasHorizontalAlignment.ALIGN_RIGHT);
		dateLastEdit.setStyleName("date");
		labelError.setStyleName("label-error");
		
		panel.add(labelError);
		panel.add(new HTML("<hr style = 'width: 100%'/>"));

		HorizontalPanel commentPanel = new HorizontalPanel();
		commentPanel.add(addCommentButton);
		commentPanel.add(hideAddComment);
		addCommentPanel.add(commentaire);
		addCommentPanel.setWidth("100%");
		commentaire.setWidth("99%");
		commentaire.setHeight("50px");
		commentaire.setStyleName("editor");
		addCommentPanel.add(commentPanel);
		addCommentPanel.setCellHorizontalAlignment(commentPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);
		addCommentButton.setText("Commenter");
		addCommentButton.setStyleName("button-fichier-menu");
		hideAddComment.setText("Annuler");
		hideAddComment.setStyleName("button-fichier-menu");
		
		panel.add(showComments);
		showComments.setStyleName("show-comments");
		showComments.setVisible(false);
		panel.add(commentsPanel);
		commentsPanel.setVisible(false);
		commentsPanel.setWidth("100%");
		panel.add(new HTML("<br>"));
		panel.add(addCommentPanel);
		addCommentPanel.setVisible(false);
		showAddComment.setText("Ajouter un commentaire");
		showAddComment.setStyleName("show-comments");
		panel.add(showAddComment);
		
		upload.setStyleName("button-fichier-menu");
		panel.add(upload);
		panel.add(new Button("Valider", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println(upload.getFilename());
			}
		}));
		
		bind();
	}

	private void bind() {
		deleteFile.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean b = Window.confirm("Voulez vous vraiment supprimer \""
						+ file.getTitle() + "\"?");
				if (b)
					if (listener != null) {
						listener.onClickDeleteFile(file);
					}
			}
		});

		editSave.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				if (edit) {
					scrollPanel.setWidget(contentEditable);
					contentEditable.setHTML(file.getContenu());
					contentEditable.setVisible(true);
					content.setVisible(false);
					contentEditable.refraish();
					setEnabled(false);
					newFile = null;
				} else {
					scrollPanel.setWidget(content);
					contentEditable.setVisible(false);
					content.setVisible(true);
					boolean r = verifier.requireTextBox(labelTitle);
					if (!r)
						return;
					labelError.setText("");
					if(newFile != null)
						file = newFile;
					r = contentEditable.getHTML().equals(file.getContenu())
							&& labelTitle.getText().equals(file.getTitle())
							&& publique.getValue() == file.isPublique();
					file.setContenu(contentEditable.getHTML());
					file.setTitle(labelTitle.getText());
					file.setPublique(publique.isChecked());
					file.setUserId(user.getId());
					setEnabled(true);
					if (listener != null && !r) {
						file.setDateDerniereModif(new Date());
						listener.saveFile(file);
					}
					newFile = null;
				}
				setSearchInToken();
			}
		});

		infoFichier.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new InfoFichier().center();
			}
		});

		sendTo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sendtoDialog = new SendTo();
				sendtoDialog.center();
				listener.setEmails();
			}
		});

		labelTitle.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				labelError.setText("");
			}
		});
		
		createFile.addClickHandler(new ClickHandler() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				newFile = new Fichier(user.getDisplayname(), "Untitled", "", user.getId(), false, false, parentId);
				newFile.setPath(path);
				newFile.setId(null);
				setEnabled(false);
				labelTitle.setText(newFile.getTitle());
				publique.setChecked(newFile.isPublique());
				dateLastEdit.setText(newFile.getDateDerniereModif().toLocaleString());
				contentEditable.setText("");
				scrollPanel.setWidget(contentEditable);
				contentEditable.setVisible(true);
				content.setVisible(false);
				edit = false;
				editSave.setVisible(true);
				setSearchInToken();
			}
		});
		
		searchIn.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String token = searchIn.getText();
				String html = content.isVisible() ? content.getHTML() : contentEditable.getHTML();
				if(content.isVisible()) {
					HTML search = new HTML(StandardMethod.searchIn(token, html));
					scrollPanel.setWidget(search);
					search.setSize("100%", "100%");
				}
				else {
					RichTextArea search = new RichTextArea();
					search.setHTML(StandardMethod.searchIn(token, html));
					contentEditable.setTextArea(search);
					search.setSize("100%", "310px");
					searchOk.setVisible(true);
				}
			}
		});
		
		searchIn.setLimit(5);
		
		searchIn.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String token = searchIn.getText();
				String html = content.isVisible() ? content.getHTML() : contentEditable.getHTML();
				if (listener != null &&
						event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if(content.isVisible()) {
						HTML search = new HTML(StandardMethod.searchIn(token, html));
						scrollPanel.setWidget(search);
						search.setSize("100%", "100%");
					}
					else {
						RichTextArea search = new RichTextArea();
						search.setHTML(StandardMethod.searchIn(token, html));
						contentEditable.setTextArea(search);
						search.setSize("100%", "310px");
						searchOk.setVisible(true);
					}
				}
			}
		});
		
		searchIn.getTextBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(contentEditable.isVisible())
					setSearchInToken();
			}
		});
		
		searchOk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				contentEditable.refraish();
				searchOk.setVisible(false);
			}
		});
		
		retour.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(file == null) {
					listener.onClickFichiers();
					return;
				}
				boolean r = contentEditable.getHTML().equals(file.getContenu())
						&& labelTitle.getText().equals(file.getTitle())
						&& publique.getValue() == file.isPublique();
				if(!r)
					r = Window.confirm("Voulez vous retourner sans enrégistrer ?");
				if(r) {
					if(file != null)
						setFichier(file);
				}
			}
		});
		
		commentaire.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				RichTextArea ctr = (RichTextArea)(event.getSource());
				int len = ctr.getText().length() + 1;
				if (len > 100)
					ctr.setHeight("150px");
				else 
					ctr.setHeight("50px");
			}
		});
		
		addCommentButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final String cmt = commentaire.getHTML();
				boolean r = verifier.isValidTextArea(commentaire);
				if(!r)
					return;
				Comment comment = new Comment(user.getId(), user
						.getDisplayname(), cmt, file.getId());
				comment.setScore(2);
				commentsPanel.add(createComment(comment));
				listener.onClickAddComment(comment);
				commentsPanel.setVisible(true);
				commentaire.setText("");
				commentaire.setHeight("50px");
				isShowComments = true;
			}
		});
		
		showComments.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Label lbl = (Label)(event.getSource());
				if(isShowComments) {
					commentsPanel.setVisible(false);
					lbl.setStyleName("show-comments");
				} else {
					commentsPanel.setVisible(true);
					lbl.setStyleName("hide-comments");
				}
				isShowComments = !isShowComments;
			}
		});
		
		showAddComment.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addCommentPanel.setVisible(true);
				showAddComment.setVisible(false);
			}
		});
		
		hideAddComment.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addCommentPanel.setVisible(false);
				showAddComment.setVisible(true);
			}
		});
		
	}

	private void setEnabled(boolean enabled) {
		publique.setEnabled(!enabled);
		dateLastEdit.setVisible(enabled);
		labelTitle.setEnabled(!enabled);
		deleteFile.setVisible(enabled);
		infoFichier.setVisible(false);
		sendTo.setVisible(enabled);
		createFile.setVisible(enabled);
		retour.setVisible(!enabled);
		editSave.setText((edit = enabled) ? "Modifier" : "Enrégistrer");
		editSave.setTitle((edit = enabled) ? "Modifier le fichier" : "Enrégistrer le fichier");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setFichier(Fichier file) {
		parentId = file.getParentId();
		String path = "";
		if (file.getPath() != null)
			path = file.getPath();
		this.path = path;
		if(file.getId() == null) {
			createFile.click();
			retour.setVisible(true);
			return;
		}
		Window.setTitle(path + "/" + file.getTitle());
		this.file = file;
		labelTitle.setText(file.getTitle());
		labelTitle.setTitle(file.toString());
		content.setHTML(file.getContenu());
		contentEditable.setVisible(false);
		content.setVisible(true);
		scrollPanel.setWidget(content);
		searchOk.setVisible(false);
		setSearchInToken();
		publique.setChecked(file.isPublique());
		dateLastEdit.setText(file.getDateDerniereModif().toLocaleString());
		setEnabled(true);
		editSave.setVisible(user.isMine(file));
		deleteFile.setVisible(user.isMine(file));
		sendTo.setVisible(user.isMine(file));
	}

	@Override
	public void setSaveLoader(boolean load) {
		saveLoader.setVisible(load);
	}

	@Override
	public void setloadLoder(boolean load) {
		loadLoader.setVisible(load);
	}

	@Override
	public void setError(String error) {
		if(sendtoDialog == null)
			labelError.setText(error);
		else {
			sendtoDialog.lblError.setText(error);
		}
			
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	@Override
	public void setListener(final Listener listener) {
		this.listener = listener;
		topPanel = new TopPanel();
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
			public void onClickSearch(String text) {
				listener.onClickSearch(text);
			}

			@Override
			public SelectionHandler<Suggestion> getRechercheSelectHandler() {
				return listener.getRechercheSelectHandler();
			}
		});
		topPanel.recherche.addSelectionHandler(listener
				.getRechercheSelectHandler());
	}
	
	@Override
	public void setTokens(Set<String> tokens) {
		if(topPanel != null) {
			topPanel.setTokens(tokens);
		}
	}
	
	private void setSearchInToken() {
		HTML html = new HTML(content.isVisible() ? content.getHTML() : contentEditable.getHTML());
		String text = html.getText();
		String arr[] = text.split(" ");
		Set<String> tokens = new HashSet<String>();
		for(int i = 0; i < arr.length; i++) {
			tokens.add(arr[i]);
		}
		oracle.clear();
		oracle.addAll(tokens);
	}

	@Override
	public void close(boolean load) {
		if (sendtoDialog != null) {
			sendtoDialog.setLoader(load);
		}
	}
	
	@Override
	public String getSelectToken() {
		String token = null;
		if(topPanel != null)
			token = topPanel.recherche.getText();
		return token;
	}

	@Override
	public void setFichiers(ArrayList<Fichier> files) {
		if (files == null || files.isEmpty())
			return;
		leftContent.clear();
		for (int i = 0; i < files.size(); i++) {
			leftContent.add(createLeftFile(files.get(i)));
		}
	}

	public Widget getTopPanel() {
		return topPanel;
	}

	@SuppressWarnings("deprecation")
	private Widget createLeftFile(final Fichier file) {
		VerticalPanel panel = new VerticalPanel();
		Label title = new Label();
		title.setText(file.getTitle());
		title.setTitle(file.toString());
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
		panel.add(title);
		panel.add(date);
		panel.setCellHorizontalAlignment(date, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(new HTML("<hr style='border:1px solid #d0def2;'>"));
		panel.setWidth("100%");
		return panel;
	}

	public Widget getLeftPanel() {
		return leftPanel;
	}

	class InfoFichier extends DialogBox {
		@SuppressWarnings("deprecation")
		public InfoFichier() {
			super(true);
			setGlassEnabled(true);
			setGlassStyleName("glass");
			setHTML("<center>Information</center>");
			FlexTable flexTable = new FlexTable();
			setWidget(flexTable);
			flexTable.setSize("307px", "163px");

			Label labelAuthor = new Label("L'auteur: ");
			flexTable.setWidget(0, 0, labelAuthor);

			Label lblAuthor = new Label(file.getAuthor());
			flexTable.setWidget(0, 1, lblAuthor);

			Label labelCreateDate = new Label("Date création: ");
			flexTable.setWidget(1, 0, labelCreateDate);

			Label date = new Label(file.getDateCreation().toLocaleString());
			flexTable.setWidget(1, 1, date);

			Label labelLastEdit = new Label("Dernière vue: ");
			flexTable.setWidget(2, 0, labelLastEdit);

			Label lastEditDate = new Label(file.getDateDerniereModif()
					.toLocaleString());
			flexTable.setWidget(2, 1, lastEditDate);

			Label labelConfident = new Label("Confidentialité: ");
			flexTable.setWidget(3, 0, labelConfident);

			Label publique = new Label();
			publique.setText(file.isPublique() ? "Publique" : "Privé");
			flexTable.setWidget(3, 1, publique);

			Button close = new Button("Fermer");
			close.setStyleName("button-fichier-menu");
			flexTable.setWidget(4, 1, close);
			flexTable.getCellFormatter().setHorizontalAlignment(4, 1,
					HasHorizontalAlignment.ALIGN_RIGHT);
			close.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});
		}
	}
	
	@Override
	public void setEmails(Set<String> emails) {
		if(sendtoDialog != null) {
			sendtoDialog.setEmails(emails);
		}
	}

	class SendTo extends DialogBox {
		FieldVerifier verifier = new FieldVerifier();
		private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		SuggestBox emailTo = new SuggestBox(oracle);
		Image loader = new Image(AppManager.images.defaultLoader());
		Label lblError = new Label();

		Button btnValider = new Button("Valider");
		Button btnAnnuler = new Button("Retour");

		public SendTo() {
			setGlassEnabled(true);
			setGlassStyleName("glass");
			FlexTable flexTable = new FlexTable();
			setWidget(flexTable);
			flexTable.setSize("330px", "180px");

			flexTable.setWidget(0, 0, emailTo);
			emailTo.setWidth("320px");
			emailTo.getTextBox().setName("Le champ email");
			emailTo.getElement().setAttribute("placeholder", "Email");
			emailTo.getElement().setAttribute("autocomplete", "on");
			emailTo.setStyleName("login-field");
			flexTable.getCellFormatter().setVerticalAlignment(0, 0,
					HasVerticalAlignment.ALIGN_TOP);
			loader.setVisible(false);
			flexTable.setWidget(1, 0, loader);
			lblError = verifier.getLabel();
			lblError.setStyleName("label-error");
			flexTable.setWidget(2, 0, lblError);

			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setSpacing(5);
			flexTable.setWidget(3, 0, horizontalPanel);

			btnAnnuler.setStyleName("button-fichier-menu");
			btnAnnuler.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});
			horizontalPanel.add(btnAnnuler);

			btnValider.setStyleName("button-fichier-menu");
			btnValider.addClickHandler(new ClickHandler() {

				@Override
			 	public void onClick(ClickEvent event) {
					emailTo.getTextBox().setEnabled(false);
					btnValider.setEnabled(false);
					String email = emailTo.getText();
					boolean r = verifier.isValidEmail(emailTo);
					if (!r) {
						btnValider.setEnabled(true);
						return;
					}
					if (listener != null) {
						listener.onClickSendFichier(file, email);
					}
				}
			});
			
			emailTo.addKeyUpHandler(new KeyUpHandler() {
				
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						btnValider.click();
					}
				}
			});
			horizontalPanel.add(btnValider);
			flexTable.getCellFormatter().setHorizontalAlignment(3, 0,
					HasHorizontalAlignment.ALIGN_RIGHT);
		}
		
		public void setEmails(Set<String> emails) {
			oracle.addAll(emails);
		}
		
		public void setLoader(boolean load) {
			if(load) {
				hide();
				sendtoDialog = null;
			}
			loader.setVisible(!load);
		}
	}
	
	@SuppressWarnings("deprecation")
	private Widget createComment(final Comment comment) {
		final VerticalPanel panel = new VerticalPanel();
		HorizontalPanel hpanel = new HorizontalPanel();

		Anchor name = new Anchor();
		name.setText(comment.getDisplayName());
		name.setStyleName("contact-popup-email");
		name.setTitle(comment.getDisplayName());
		
		VerticalPanel commentPanel = new VerticalPanel();
		final HTML html = new HTML();
		final Anchor readFollow = new Anchor();
		readFollow.setHTML("<u>Lire la suite...</u>");
		readFollow.setStyleName("contact-popup-email");
		commentPanel.add(html);
		commentPanel.add(readFollow);
		
		readFollow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				html.setHTML(comment.getText());
				readFollow.setVisible(false);
			}
		});
		String resumeText = (comment.getText().length() >= 400) ? comment.getText().substring(0, 400) : comment.getText();
		
		readFollow.setVisible(comment.getText().length() != resumeText.length());
		html.setHTML(resumeText);
		Button delete = new Button();
		delete.setText("X");
		delete.setTitle("Supprimer ce commentaire");
		delete.setStyleName("contact-popup-supp");
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onDeleteComment(comment);
					if(comment.getId() != null && !comment.getId().isEmpty())
						panel.setVisible(false);
					else 
						Window.alert("Veuillez actualiser la page pour pouvoir réaliser votre suppression merci.");
				}
			}
		});
		VerticalPanel content = new VerticalPanel();
		content.add(name);
		content.add(commentPanel);
		hpanel.add(content);
		if (!user.isAdmin()
				&& !user.getId().equals(comment.getUserId())) {
			delete.setVisible(false);
		}
		hpanel.setWidth("100%");
		panel.setWidth("100%");
		Label date = new Label();
		date.setText(comment.getDateCreation().toLocaleString());
		date.setStyleName("date");
		hpanel.add(delete);
		hpanel.setCellHorizontalAlignment(delete,
				HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(hpanel);
		panel.add(date);
		panel.setCellHorizontalAlignment(date,
				HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(new HTML("<hr>"));
		return panel;
	}

	@Override
	public void setComments(ArrayList<Comment> result) {
		commentsPanel.clear();
		if (result == null || result.isEmpty()) {
			isShowComments = false;
			showComments.setVisible(false);
			return;
		}
		String text = (result.size() > 1) ? (result.size() + " commentaires") : (result.size() + " commentaire");
		showComments.setText(text);
		showComments.setVisible(true);
		showComments.setStyleName("show-comments");
		for (int i = 0; i < result.size(); i++) {
			commentsPanel.add(createComment(result.get(i)));
		}
	}

	@Override
	public Fichier getFile() {
		return file;
	}
	
}
