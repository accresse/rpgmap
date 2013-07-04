package org.cresse.rpg.client.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import org.cresse.rpg.client.map.model.Map;

public class MapCircle extends AbstractMapObject {
	
	private double radiusInFeet=2.5;
	private double radiusInPixels=-1;

	public MapCircle(Map map, int x, int y){
		super("Circle", map,x,y);
	}

	@Override
	protected void addConfigurableProperties(List<ConfigurableProperties> configurableProperties) {
		configurableProperties.add(ConfigurableProperties.RADIUS);
	}

	@Override
	public boolean contains(int x, int y) {
		Point p=new Point(x,y);
		Point center=new Point(this.x,this.y);
		return (p.distance(center)<getRadiusInPixels());
	}

	@Override
	protected void drawBeforeTransform(Graphics2D g) {
		Color color = getColor();
		Color trans=new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(255-255*getTransparency()));
		g.setColor(trans);
		g.fillOval((int)(x-getRadiusInPixels()), (int)(y-getRadiusInPixels()),
				   (int)(2*getRadiusInPixels()), (int)(2*getRadiusInPixels()));
	}

	@Override
	protected void drawAfterTransform(Graphics2D g) {
		g.setColor(getColor());
		g.drawOval((int)(x-getRadiusInPixels()), (int)(y-getRadiusInPixels()),
				   (int)(2*getRadiusInPixels()), (int)(2*getRadiusInPixels()));
	}
	
	protected double getRadiusInPixels(){
		if(radiusInPixels<0) {
			radiusInPixels = radiusInFeet*map.getPixelsPerFoot();
		}
		return radiusInPixels;
	}
	
	private void setRadiusInFeet(double feet){
		this.radiusInFeet=feet;
		this.radiusInPixels=-1;
		onResize();
	}
	
	@Override
	public void setConfiguredValue(ConfigurableProperties key, String value){
		switch(key) {
			case RADIUS:
				setRadiusInFeet(Double.parseDouble(value));
				break;
			default:
		}
		super.setConfiguredValue(key, value);
	}
	
	@Override
	public String getConfiguredValue(ConfigurableProperties key){
		switch(key) {
			case RADIUS:
				return String.valueOf(radiusInFeet);
			default:
		}
		return super.getConfiguredValue(key);
	}

}
