package org.cresse.rpg.client.map;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapObjectSelectionPanel extends JScrollPane implements MouseListener, MapListener{
	
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private Map map;
	private MapPanel panel;
	
	public MapObjectSelectionPanel(IMapObjectSource objSource, MapPanel panel){
		super();
		tree=new JTree(objSource.getMapObjectSelection());
		tree.addMouseListener(this);
		this.setViewportView(tree);
		this.panel=panel;
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()>1){
			DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			MapObjectDef def=(MapObjectDef)node.getUserObject();
			try {
				Point2D center=panel.getVisibleCenter();
				Mappable mappable=def.createInstance(map, (int)center.getX(), (int)center.getY());
				map.addMapObject(mappable, MapObjectSelectionPanel.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	@Override
	public void onNewMap(MapEvent<Map> event) {
		this.map=event.getUpdatedObject();
	}

	@Override
	public void onMapObjectAdd(MapEvent<Mappable> event) {}

	@Override
	public void onMapObjectRemove(MapEvent<Mappable> event) {}

	@Override
	public void onMapObjectChange(MapEvent<Mappable> event) {}
	
}
