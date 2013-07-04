package org.cresse.rpg.client.map.procs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;

import org.cresse.rpg.client.map.MapPanel;
import org.cresse.rpg.client.map.obj.Mappable;
import org.cresse.rpg.client.map.util.FormatUtils;

public class RotateEventProcessor extends DefaultEventProcessor {
	
	public RotateEventProcessor(MapPanel panel, JLabel statusBar) {
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
			lastX=e.getX();
			lastY=e.getY();
			for (Mappable mappable : selected) {
				double theta=calculateAngle(lastX, lastY, mappable);
				mappable.setAngle(theta);
			}
			panel.repaint();
		}
	}
	
	protected double calculateAngle(int x, int y, Mappable mappable){
		double dx=x-mappable.getX();
		double dy=y-mappable.getY();
		double dz=Math.sqrt(dx*dx+dy*dy);
		//double ratio=dy/dx;
		
		//sin(theta)=dy/dz
		double theta=Math.asin(dy/dz);
		//tan(theta)=dy/dx
		//double theta=sign*Math.atan(dy/dx);

		if(dx<=0 && dy<0){
			theta=-Math.PI-theta;
		} else if(dx<=0 && dy>0){
			theta=Math.PI-theta;
		}
		
		return theta;
	}

	public void draw(Graphics g){
		Mappable mappable=(Mappable)selected.get(0);
		double theta=calculateAngle(lastX, lastY, mappable);
		String degrees=FormatUtils.formatDegrees(theta);
		statusBar.setText("Angle: "+degrees);
		g.setColor(Color.WHITE);
		g.drawLine(mappable.getX(), mappable.getY(), lastX, lastY);
		g.drawString(degrees, lastX, lastY);		
	}

	public void reset(){
		origX=-1;
		origY=-1;
		lastX=-1;
		lastY=-1;
	}

}
