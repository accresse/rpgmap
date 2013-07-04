package org.cresse.rpg.client.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MapMenuBar extends JMenuBar implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	public static final String MENU_OPTION_NEW="New Map...";
	public static final String MENU_OPTION_OPEN="Open Map...";
	public static final String MENU_OPTION_SAVE="Save";
	public static final String MENU_OPTION_SAVE_AS="Save As...";
	public static final String MENU_OPTION_EXIT="Exit";

	public static final String MENU_OPTION_GRID = "Show Grid";

	public static final String MENU_OPTION_SHARE="Share...";
	public static final String MENU_OPTION_CONNECT = "Connect...";
	
	private List<ActionListener> listeners;
	
	public MapMenuBar(){
		listeners=new ArrayList<ActionListener>();
		this.add(getFileMenu());
		this.add(getViewMenu());
		this.add(getNetworkMenu());
	}

	private JMenu getFileMenu() {
		JMenu file=new JMenu("File");
		file.setMnemonic('F');
		addItem(file,MENU_OPTION_NEW,'n');
		addItem(file,MENU_OPTION_OPEN,'o');
		addItem(file,MENU_OPTION_SAVE,'s');
		addItem(file,MENU_OPTION_SAVE_AS,'a');
		addItem(file,MENU_OPTION_EXIT,'x');
		return file;
	}
	
	private JMenu getViewMenu() {
		JMenu view=new JMenu("View");
		view.setMnemonic('V');
		addCheckBoxItem(view,MENU_OPTION_GRID,'g');
		return view;
	}
	
	private JMenu getNetworkMenu() {
		JMenu view=new JMenu("Network");
		view.setMnemonic('N');
		addItem(view,MENU_OPTION_SHARE,'h');
		addItem(view,MENU_OPTION_CONNECT,'c');
		return view;
	}
	
	private void addItem(JMenu menu, String disp, char mnemonic){
		JMenuItem item=new JMenuItem(disp);
		item.setMnemonic(mnemonic);
		item.addActionListener(this);
		menu.add(item);
	}
	
	private void addCheckBoxItem(JMenu menu, String disp, char mnemonic){
		JCheckBoxMenuItem item=new JCheckBoxMenuItem(disp);
		item.setMnemonic(mnemonic);
		item.addActionListener(this);
		item.setSelected(true);
		menu.add(item);
	}
	
	
	public void addActionListener(ActionListener listener){
		listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener){
		listeners.remove(listener);
	}

	public void actionPerformed(ActionEvent e) {
		for (ActionListener listener : listeners) {			
			listener.actionPerformed(e);
		}
	}

}
