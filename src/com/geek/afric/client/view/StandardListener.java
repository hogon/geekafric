/**
 * 
 */
package com.geek.afric.client.view;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author Mcicheick
 *
 */
public interface StandardListener {
	
	SelectionHandler<Suggestion> getRechercheSelectHandler();
	
	void onClickSearch(String token);

	void onClickPartages();

	void onClickFichiers();

	void onClickDeconnexion();

	void onClickAcceuil();
}
