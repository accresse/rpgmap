package org.cresse.rpg.client.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class MapToolBar extends JToolBar implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private ButtonGroup group;
	private List<ActionListener> listeners;
	
	public MapToolBar(){
		super(JToolBar.HORIZONTAL);
		group=new ButtonGroup();
		listeners=new ArrayList<ActionListener>();
		addButton("move").setSelected(true);
		addButton("rotate");
		addButton("measure");
	}
	
	private JToggleButton addButton(String name){
		JToggleButton button=new JToggleButton(new ImageIcon("icons/"+name+".png"));
		button.setToolTipText(name);
		button.setActionCommand(name);
		group.add(button);
		this.add(button);
		button.addActionListener(this);
		return button;
	}

	public void actionPerformed(ActionEvent e) {
		for (ActionListener listener : listeners) {
			listener.actionPerformed(e);
		}
	}
	
	public void addActionListener(ActionListener listener){
		this.listeners.add(listener);
	}
	
	public void removeActionListener(ActionListener listener){
		this.listeners.remove(listener);
	}

}
