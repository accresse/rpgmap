package org.cresse.rpg.client.map.procs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.cresse.rpg.client.map.MapPanel;
import org.cresse.rpg.client.map.obj.Mappable;
import org.cresse.rpg.client.map.util.FormatUtils;

public class GroupRotateEventProcessor extends DefaultEventProcessor {
	
	public GroupRotateEventProcessor(MapPanel panel, JLabel statusBar) {
		super(panel, statusBar);
	}

	private List<Mappable> selected;
	private List<Point> origPoints;
	private List<Double> origThetas;
	private int lastX=-1;
	private int lastY=-1;
	private int centerX=-1;
	private int centerY=-1;

	
	public void mousePressed(MouseEvent e) {
		List<Mappable> newSelected=panel.getSelected();
		//if(newSelected!=selected){
			centerX=(int)panel.getSelectionRectangle().getCenterX();
			centerY=(int)panel.getSelectionRectangle().getCenterY();
			lastX=centerX;
			lastY=centerY;
			selected=newSelected;
			origPoints=new ArrayList<Point>();
			origThetas=new ArrayList<Double>();
			for (Mappable mappable : selected) {
				origPoints.add(new Point(mappable.getX(),mappable.getY()));
				origThetas.add(new Double(mappable.getAngle()));
			}
		//}
	}
		
	public void mouseDragged(MouseEvent e){
		if(selected!=null){
			lastX=e.getX();
			lastY=e.getY();
			double theta=calculateAngle(lastX, lastY, centerX,centerY);
			AffineTransform rotate=AffineTransform.getRotateInstance(theta, centerX, centerY);
			int i=0;
			for (Mappable mappable : selected) {
				Point2D point=rotate.transform((Point2D)origPoints.get(i), null);
				mappable.move((int)point.getX(), (int)point.getY());
				double origTheta=((Double)origThetas.get(i)).doubleValue();
				mappable.setAngle(origTheta+theta);
				i++;
			}
			panel.repaint();
		}
	}
	
	protected double calculateAngle(int x, int y, int centerX, int centerY){
		double dx=x-centerX;
		double dy=y-centerY;
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
		double theta=calculateAngle(lastX, lastY, centerX,centerY);
		String degrees=FormatUtils.formatDegrees(theta);
		statusBar.setText("Angle: "+degrees);
		g.setColor(Color.WHITE);
		g.drawLine(centerX, centerY, lastX, lastY);
		g.drawString(degrees, lastX, lastY);	
	}

	public void reset(){
		centerX=-1;
		centerY=-1;
		lastX=-1;
		lastY=-1;
	}

}
