/**
 * 
 */
package com.geek.afric.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Mcicheick
 *
 */
public class FichierListEvent extends GwtEvent<FichierListEventHandler>{

	public static Type<FichierListEventHandler> TYPE = new Type<FichierListEventHandler>();
	private final String proprio;
	private final int startIndex;
	/**
	 * 
	 */
	public FichierListEvent() {
		this(null, 0);
	}

	/**
	 * @param proprio
	 * @param startIndex
	 */
	public FichierListEvent(String proprio, int startIndex) {
		super();
		this.proprio = proprio;
		this.startIndex = startIndex;
	}

	/**
	 * @return the proprio
	 */
	public String getProprio() {
		return proprio;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	@Override
	public Type<FichierListEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FichierListEventHandler handler) {
		handler.onFichierList(this);
	}

}
