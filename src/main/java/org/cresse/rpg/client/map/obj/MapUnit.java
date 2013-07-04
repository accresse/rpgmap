package org.cresse.rpg.client.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.util.ImageUtils;

public class MapUnit extends MapCircle {

	protected String image;
	protected BufferedImage fullSizeImage;
	protected Image scaledImage;
	protected int maxHp=100;
	protected int hp=100;
	protected int border=2;

	public MapUnit(Map map, int x, int y){
		super(map,x,y);
		this.setName("Unit");
		this.setTransparency(0.0);
	}
	
	@Override
	protected void addConfigurableProperties(List<ConfigurableProperties> configurableProperties) {
		super.addConfigurableProperties(configurableProperties);
		configurableProperties.add(ConfigurableProperties.IMAGE);
		configurableProperties.add(ConfigurableProperties.MAX_HP);
		configurableProperties.add(ConfigurableProperties.HP);
	}

	public void setImage(String image){
		this.image=image;
		fullSizeImage=ImageUtils.loadImage(image);
		resizeImage();
	}
	
	protected void resizeImage(){
		if(fullSizeImage!=null){
			int w = fullSizeImage.getWidth();
			int h = fullSizeImage.getHeight();
			int max = Math.max(w, h);
			BufferedImage blank = ImageUtils.getBlankImage(max, max, new Color(0,0,0,0));
			Graphics2D g2d = blank.createGraphics();
			g2d.drawImage(fullSizeImage, (max-w)/2, (max-h)/2, null);
			scaledImage=blank.getScaledInstance((int)(2*getRadiusInPixels()), (int)(2*getRadiusInPixels()), BufferedImage.SCALE_SMOOTH);
		}
	}
	
	public String getImage(){
		return image;
	}

	@Override
	protected void drawDuringTransform(Graphics2D g) {
		int size=(int)getRadiusInPixels()/10;
		int[] xPoints={(int)(x+getRadiusInPixels()+size),(int)(x+getRadiusInPixels()+size*4),(int)(x+getRadiusInPixels()+size)};
		int[] yPoints={y+2*size,y,y-2*size};
		//g.setColor(getColor());
		g.setColor(Color.RED);
		g.fillPolygon(xPoints,yPoints,3);
		g.setColor(Color.BLACK);
		g.drawPolygon(xPoints,yPoints,3);
	}

	@Override
	protected void drawAfterTransform(Graphics2D g) {
		drawImage(g);
		//super.drawAfterTransform(g);//uncomment for border
		drawHealth(g);
	}
	
	protected void drawImage(Graphics2D g){
		if(scaledImage==null){
			resizeImage();
		}
		if(scaledImage!=null){
			Shape origShape=g.getClip();
			Shape circle=new Ellipse2D.Double(x-getRadiusInPixels()+border,y-getRadiusInPixels()+border,2*getRadiusInPixels()-2*border,2*getRadiusInPixels()-2*border);
			g.setClip(circle);
			g.drawImage(scaledImage, (int)(x-getRadiusInPixels()),(int)(y-getRadiusInPixels()),null);
			g.setClip(origShape);
		}
	}
	
	protected void drawHealth(Graphics2D g){
		double health = getHealthPercent();
		
		int thick=(int)(getRadiusInPixels()/5);
		g.setColor(Color.BLACK);
		g.fillRect((int)(x-getRadiusInPixels()), (int)(y-getRadiusInPixels()-2*thick), (int)(2*getRadiusInPixels()), thick);
		
		g.setColor(ImageUtils.getColorGradient(health));
		g.fillRect((int)(x-getRadiusInPixels()), (int)(y-getRadiusInPixels()-2*thick), (int)(2*getRadiusInPixels()*health), thick);
		
		g.setColor(Color.BLACK);
		g.drawRect((int)(x-getRadiusInPixels()), (int)(y-getRadiusInPixels()-2*thick), (int)(2*getRadiusInPixels()), thick);
	}
	
	protected void setMaxHp(int maxHp){
		this.maxHp = maxHp;
	}

	protected void setHp(int hp){
		this.hp = hp;
	}
	
	protected double getHealthPercent() {
		return (double)hp / (double)maxHp;
	}

	@Override
	void onResize() {
		this.resizeImage();
	}
	
	@Override
	public void setConfiguredValue(ConfigurableProperties key, String value){
		switch(key) {
			case IMAGE:
				setImage(value);
				break;
			case HP:
				setHp(Integer.parseInt(value));
				break;
			case MAX_HP:
				setMaxHp(Integer.parseInt(value));
				break;
			default:
		}
		super.setConfiguredValue(key, value);
	}
	
	@Override
	public String getConfiguredValue(ConfigurableProperties key){
		switch(key) {
			case IMAGE:
				return image;
			case HP:
				return String.valueOf(hp);
			case MAX_HP:
				return String.valueOf(maxHp);
			default:
		}
		return super.getConfiguredValue(key);
	}
	
}
