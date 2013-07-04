package org.cresse.rpg.client.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.cresse.rpg.client.map.model.Map;


public class MapCone extends WidthHeightMapObject {
	
	public MapCone(Map map, int x, int y){
		super("Cone",map,x,y);
		this.setColor(Color.CYAN);
	}

	public boolean contains(int px, int py) {
		double w=getWidthInPixels();
		double h=getHeightInPixels();
		Polygon tri=new Polygon(new int[]{x,x+(int)(w/2),x-(int)(w/2)},new int[]{y+(int)h/2,y-(int)h/2,y-(int)h/2},3);

		AffineTransform transform;
		try {
			transform = AffineTransform.getRotateInstance(theta,this.x,this.y).createInverse();
			Point2D transPoint=transform.transform(new Point(px,py), null);
			return tri.contains(transPoint);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void drawDuringTransform(Graphics2D g) {
		double w=getWidthInPixels();
		double h=getHeightInPixels();
		Polygon tri=new Polygon(new int[]{x,x+(int)(w/2),x-(int)(w/2)},new int[]{y+(int)h/2,y-(int)h/2,y-(int)h/2},3);
		Color color = getColor();
		Color trans=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(255-255*getTransparency()));
		g.setColor(trans);
		g.fill(tri);
		g.setColor(color);
		g.draw(tri);
	}
	
}
