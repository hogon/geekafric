/**
 * 
 */
package com.geek.afric.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Mcicheick
 *
 */
public class FichierEvent extends  GwtEvent<FichierEventHandler>{

	public static Type<FichierEventHandler> TYPE = new Type<FichierEventHandler>();
	private final String fileId;
	private final String parentId;
	private final String path;
	
	/**
	 * @param fileId
	 * @param parentId
	 * @param path 
	 */
	public FichierEvent(String fileId, String parentId, String path) {
		super();
		this.fileId = fileId;
		this.parentId = parentId;
		this.path = path;
	}

	/**
	 * @param fileId
	 */
	public FichierEvent(String fileId) {
		this(fileId, null, "");
	}
	
	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FichierEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FichierEventHandler handler) {
		handler.onFichier(this);
	}

}
