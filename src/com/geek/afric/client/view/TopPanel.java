/**
 * 
 */
package com.geek.afric.client.view;

import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Mcicheick
 *
 */
public class TopPanel extends Composite {
	public interface Listener extends StandardListener{
	}
	Listener listener;
	HorizontalPanel horizontalPanel = new HorizontalPanel();
	Label partages = new Label("Partages");
	Label fichiers = new Label("Mes fichiers");
	Label acceuil = new Label("Acceuil");
	Label deconnexion = new Label("Déconnexion");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	SuggestBox recherche = new SuggestBox(oracle);

	public TopPanel() {

		horizontalPanel.setSpacing(10);
		horizontalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(horizontalPanel);

		partages.setStyleName("label-toppanel");
		horizontalPanel.add(partages);

		fichiers.setStyleName("label-toppanel");
		horizontalPanel.add(fichiers);

		acceuil.setStyleName("label-toppanel");
		horizontalPanel.add(acceuil);

		deconnexion.setStyleName("label-toppanel");
		horizontalPanel.add(deconnexion);

		recherche.getElement().setAttribute("placeholder", "Rechercher");
		recherche.getElement().setAttribute("type", "search");
		horizontalPanel.add(recherche);
		bind();
	}

	private void bind() {
		partages.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickPartages();
				}
			}
		});

		fichiers.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickFichiers();
				}
			}
		});

		acceuil.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickAcceuil();
				}
			}
		});

		deconnexion.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onClickDeconnexion();
				}
			}
		});
		
		recherche.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String token = recherche.getText();
				if (listener != null &&
						event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					listener.onClickSearch(token);
				}
			}
		});

	}
	
	public void insert(Widget w, int beforeIndex) {
		w.setStyleName("label-toppanel");
		horizontalPanel.insert(w, beforeIndex);
	}
	
	public void remove(int index) {
		horizontalPanel.remove(index);
	}

	public void setTokens(Set<String> tokens) {
		oracle.addAll(tokens);
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
		recherche.addSelectionHandler(listener.getRechercheSelectHandler());
	}
}
