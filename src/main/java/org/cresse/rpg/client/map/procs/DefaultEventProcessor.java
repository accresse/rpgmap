package org.cresse.rpg.client.map.procs;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.cresse.rpg.client.map.MapPanel;

public class DefaultEventProcessor implements MapEventProcessor {
	
	protected MapPanel panel;
	protected JLabel statusBar;
	
	public DefaultEventProcessor(MapPanel panel,JLabel statusBar){
		this.panel=panel;
		this.statusBar=statusBar;
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void draw(Graphics g) {}
	
	public void reset(){}
	
}
