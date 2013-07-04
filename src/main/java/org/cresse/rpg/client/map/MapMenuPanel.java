package org.cresse.rpg.client.map;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class MapMenuPanel extends JSplitPane {
	
	private static final long serialVersionUID = 1L;

	public MapMenuPanel(MapPreviewPanel mapPreviewPanel, MapObjectSelectionPanel mapObjSelectionPanel){
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.setDividerSize(2);
		JTabbedPane tabs=new JTabbedPane();
		//tabs.addTab("Tools", mapToolBar);
		tabs.addTab("Object Selection", mapObjSelectionPanel);
		this.setBottomComponent(tabs);
		this.setTopComponent(mapPreviewPanel);
	}

}
