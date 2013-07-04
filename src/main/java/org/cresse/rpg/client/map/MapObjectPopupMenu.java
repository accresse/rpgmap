package org.cresse.rpg.client.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MapObjectPopupMenu extends JPopupMenu implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	public static final String TO_BACK = "Send to Back";
	public static final String TO_FRONT = "Bring to Front";
	
	private List<ActionListener> listeners;
	
	public MapObjectPopupMenu(){
		this.listeners=new ArrayList<ActionListener>();
		addItem(TO_BACK);
		addItem(TO_FRONT);
	}
	
	private void addItem(String name){
		JMenuItem item=new JMenuItem(name);
		item.addActionListener(this);
		this.add(item);
	}
	
	public void addActionListener(ActionListener listener){
		this.listeners.add(listener);
	}
	
	public void removeActionListener(ActionListener listener){
		this.listeners.remove(listener);
	}

	public void actionPerformed(ActionEvent e) {
		for (ActionListener listener : listeners) {
			listener.actionPerformed(e);
		}
	}

}
