package org.cresse.rpg.client.map.procs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.cresse.rpg.client.map.MapPanel;
import org.cresse.rpg.client.map.util.FormatUtils;

public class MeasureEventProcessor extends DefaultEventProcessor {

	private int origX=-1;
	private int origY=-1;
	private int lastX=-1;
	private int lastY=-1;
	
	public MeasureEventProcessor(MapPanel panel, JLabel statusBar) {
		super(panel, statusBar);
	}

	public void mousePressed(MouseEvent e) {
		origX=e.getX();
		origY=e.getY();
		lastX=e.getX();
		lastY=e.getY();
	}
	
	public void mouseReleased(MouseEvent e) {
		origX=-1;
		origY=-1;
		lastX=-1;
		lastY=-1;
	}
	
	public void mouseDragged(MouseEvent e){
		lastX=e.getX();
		lastY=e.getY();
		panel.repaint();
	}
	
	public void draw(Graphics g) {
		double dist=Point.distance(origX,origY,lastX,lastY)/panel.getMap().getPixelsPerFoot();
		String distTxt=FormatUtils.formatDist(dist);
		statusBar.setText("Distance: "+distTxt);
		g.setColor(Color.WHITE);
		g.drawLine(origX, origY, lastX, lastY);
		g.drawString(distTxt, lastX, lastY);		
	}

}