package org.cresse.rpg.client.map.procs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;

import org.cresse.rpg.client.map.MapPanel;
import org.cresse.rpg.client.map.obj.Mappable;
import org.cresse.rpg.client.map.util.FormatUtils;

public class MoveEventProcessor extends DefaultEventProcessor {
	
	public MoveEventProcessor(MapPanel panel, JLabel statusBar) {
		super(panel, statusBar);
	}

	private List<Mappable> selected;
	private int origX=-1;
	private int origY=-1;
	private int lastX=-1;
	private int lastY=-1;

	
	public void mousePressed(MouseEvent e) {
		List<Mappable> newSelected=panel.getSelected();
		if(newSelected!=selected){
			origX=e.getX();
			origY=e.getY();
			lastX=origX;
			lastY=origY;
			selected=newSelected;
		}
	}
		
	public void mouseDragged(MouseEvent e){
		if(selected!=null){
			int dx=e.getX()-lastX;
			int dy=e.getY()-lastY;
			for (Mappable mappable : selected) {
				mappable.move(mappable.getX()+dx,mappable.getY()+dy);
			}
			lastX=e.getX();
			lastY=e.getY();
			panel.getSelectionRectangle().translate(dx,dy);
			panel.repaint();
		}
	}

	public void draw(Graphics g){
		double dist=Point.distance(origX,origY,lastX,lastY)/panel.getMap().getPixelsPerFoot();
		String distTxt=FormatUtils.formatDist(dist);
		statusBar.setText("Distance: "+distTxt);
		g.setColor(Color.WHITE);
		g.drawLine(origX, origY, lastX, lastY);
		g.drawString(distTxt, lastX, lastY);		
	}

	public void reset(){
		origX=-1;
		origY=-1;
		lastX=-1;
		lastY=-1;
	}

}
