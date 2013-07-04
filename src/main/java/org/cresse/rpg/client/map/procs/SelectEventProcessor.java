package org.cresse.rpg.client.map.procs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.cresse.rpg.client.map.MapPanel;

public class SelectEventProcessor extends DefaultEventProcessor {
	
	public SelectEventProcessor(MapPanel panel, JLabel statusBar) {
		super(panel, statusBar);
	}

	private int origX=-1;
	private int origY=-1;
	private int lastX=-1;
	private int lastY=-1;

	
	public void mousePressed(MouseEvent e) {
		origX=e.getX();
		origY=e.getY();
		lastX=origX;
		lastY=origY;
	}
		
	public void mouseDragged(MouseEvent e){
		lastX=e.getX();
		lastY=e.getY();
		panel.repaint();
	}
	
	public void mouseReleased(MouseEvent e){
		panel.selectObjectsInArea(Math.min(origX,lastX), Math.min(origY, lastY), Math.abs(lastX-origX), Math.abs(lastY-origY));
		reset();
		panel.repaint();
	}
	
	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.drawRect(Math.min(origX,lastX), Math.min(origY, lastY), Math.abs(lastX-origX), Math.abs(lastY-origY));
	}

	public void reset(){
		origX=-1;
		origY=-1;
		lastX=-1;
		lastY=-1;
	}

}
