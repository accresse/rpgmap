package org.cresse.rpg.client.map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.cresse.rpg.client.map.obj.ConfigurableProperties;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapObjectOptionDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES={"Name","Value"};
	private JTable table;
	private MapObjectOptionModel model;
	
	public MapObjectOptionDialog(JFrame parent){
		super(parent, "Object Options", true);
		model=new MapObjectOptionModel();
		table=new JTable(model);
		this.setModal(true);
		this.getContentPane().add(table);
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	public void setMapObject(Mappable selected) {
		model.setMapObject(selected);
	}
		
	class MapObjectOptionModel extends AbstractTableModel{
				
		private static final long serialVersionUID = 1L;

		private Mappable mapObj;
		
		public void setMapObject(Mappable mapObj){
			this.mapObj=mapObj;
			this.fireTableDataChanged();
		}

		public int getColumnCount() {
			return 2;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex==1;
		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			ConfigurableProperties key=mapObj.getConfigurableProperties().get(rowIndex);
			mapObj.setConfiguredValue(key, (String)aValue);
			super.setValueAt(aValue, rowIndex, columnIndex);
		}

		public int getRowCount() {
			if(mapObj==null){
				return 0;
			} else {
				return mapObj.getConfigurableProperties().size();
			}
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			ConfigurableProperties key=mapObj.getConfigurableProperties().get(rowIndex);
			if(columnIndex==0){
				return key.getDisplayName();
			} else {
				return mapObj.getConfiguredValue(key);
			}
		}
		
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}
		
	}

}
