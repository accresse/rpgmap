package org.cresse.rpg.client.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.cresse.rpg.client.map.model.Map;


public class MapRect extends WidthHeightMapObject {
	
	public MapRect(Map map, int x, int y){
		super("Rectangle", map,x,y);
		this.setColor(Color.YELLOW);
	}

	@Override
	public boolean contains(int x, int y) {
		double w=getWidthInPixels();
		double h=getHeightInPixels();
		AffineTransform transform;
		try {
			transform = AffineTransform.getRotateInstance(theta,this.x,this.y).createInverse();
			Point2D transPoint=transform.transform(new Point(x,y), null);
			Rectangle rect=new Rectangle((int)(this.x-w/2.0),(int)(this.y-h/2.0),(int)w,(int)h);
			return rect.contains(transPoint);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void drawDuringTransform(Graphics2D g) {
		Color color = getColor();
		Color trans=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(255-255*getTransparency()));
		g.setColor(trans);
		double w=getWidthInPixels();
		double h=getHeightInPixels();
		g.fillRect((int)(this.x-w/2.0),(int)(this.y-h/2.0),(int)w,(int)h);
		g.setColor(color);
		g.drawRect((int)(this.x-w/2.0),(int)(this.y-h/2.0),(int)w,(int)h);
	}
	
}
