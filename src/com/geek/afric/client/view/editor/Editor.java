package com.geek.afric.client.view.editor;

import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RichTextArea;

public class Editor extends Composite {
	private RichTextArea area = new RichTextArea();
	Grid grid = new Grid(2, 1);

	public Editor() {
		// Create the text area and toolbar
		RichTextToolbar toolbar = new RichTextToolbar(area);
		area.setWidth("100%");
		area.setHeight("100%");
		toolbar.setWidth("100%");
		area.setStyleName("editor");
		// Add the components to a panel
		grid.setWidget(0, 0, toolbar);
		grid.setWidget(1, 0, area);
		initWidget(grid);
	}

	public String getHTML() {
		return area.getHTML();
	}

	public String getText() {
		return area.getText();
	}

	public void setHTML(String html) {
		area.setHTML(html);
	}

	public void setText(String text) {
		area.setText(text);
	}

	public void setEnabled(boolean b) {
		area.setEnabled(b);
	}

	@Override
	public void addStyleName(String style) {
		area.addStyleName(style);
	}

	public void addKeyUpHandler(KeyUpHandler handler) {
		area.addKeyUpHandler(handler);
	}

	public void setTextArea(RichTextArea area) {
		area.setStyleName("editor");
		grid.setWidget(1, 0, area);
	}
	
	public void refraish() {
		grid.setWidget(1, 0, area);
	}
	
	@Override
	public void setWidth(String width) {
		grid.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		grid.setHeight(height);
	}
	
	@Override
	public void setSize(String width, String height) {
		grid.setSize(width, height);
	}

	public void setAreaSize(String width, String height) {
		area.setSize(width, height);
	}
}
