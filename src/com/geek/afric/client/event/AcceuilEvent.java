/**
 * 
 */
package com.geek.afric.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Mcicheick
 *
 */
public class AcceuilEvent extends
	GwtEvent<AcceuilEventHandler> {

	public static Type<AcceuilEventHandler> TYPE = new Type<AcceuilEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AcceuilEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AcceuilEventHandler handler) {
		handler.onAcceuil(this);
	}

}
