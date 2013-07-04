package org.cresse.rpg.client.map;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapObjectTable extends JScrollPane implements MapListener, KeyListener{
	
	private static final long serialVersionUID = 1L;

	private MapObjectTableModel model;
	private JTable table;
	private Map map;
	
	public MapObjectTable(){
		super();
		model=new MapObjectTableModel();
		table=new JTable(model);
		table.addKeyListener(this);
		this.setViewportView(table);
	}
	
	@Override
	public void onNewMap(MapEvent<Map> event) {
		this.map=event.getUpdatedObject();
		model=new MapObjectTableModel();
		for (Mappable mappable : map.getAllMapObjects()) {
			model.addMapObject(mappable);			
		}
		table.setModel(model);
		this.setPreferredSize(table.getPreferredSize());
		map.addMapListener(this);
	}

	@Override
	public void onMapObjectAdd(MapEvent<Mappable> event) {
		model.addMapObject(event.getUpdatedObject());
	}

	@Override
	public void onMapObjectChange(MapEvent<Mappable> event) {}

	@Override
	public void onMapObjectRemove(MapEvent<Mappable> event) {
		model.removeMapObject(event.getUpdatedObject());
	}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar()==KeyEvent.VK_DELETE){
			int row=table.getSelectedRow();
			int col=table.getSelectedColumn();
			map.removeMapObject((Mappable) table.getValueAt(row, col), MapObjectTable.class);
		}
	}

	class MapObjectTableModel extends AbstractTableModel{
		
		private static final long serialVersionUID = 1L;

		protected List<Mappable> objects;
		
		public MapObjectTableModel(){
			objects=new ArrayList<Mappable>();
		}

		public int getColumnCount() {
			return 1;
		}

		public int getRowCount() {
			return objects.size();
		}

		public String getColumnName(int column) {
			return "Name";
		}

		public Mappable getValueAt(int rowIndex, int columnIndex) {
			return objects.get(rowIndex);
		}
		
		public void addMapObject(Mappable mappable){
			objects.add(mappable);
			this.fireTableDataChanged();
		}

		public void removeMapObject(Mappable mappable){
			objects.remove(mappable);
			this.fireTableDataChanged();
		}

	}

}
