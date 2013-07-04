package org.cresse.rpg.client.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.cresse.rpg.client.map.event.UpdateListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.util.FormatUtils;

public abstract class AbstractMapObject implements Mappable {
	
	protected String id = generateUniqueId();
	protected Map map;
	protected int x;
	protected int y;
	protected double theta;
	protected List<UpdateListener> listeners;
	protected List<ConfigurableProperties> configurableProperties;
	
	private String name="Unknown";
	private Color color=Color.RED;
	private double transparency=.667;

	public AbstractMapObject(String name, Map map, int x, int y){
		this.name = name;
		this.map=map;
		this.x=x;
		this.y=y;
		listeners=new ArrayList<UpdateListener>();
		configurableProperties=new ArrayList<ConfigurableProperties>();
		configurableProperties.add(ConfigurableProperties.NAME);
		configurableProperties.add(ConfigurableProperties.HEX_COLOR);
		configurableProperties.add(ConfigurableProperties.TRANSPARENCY);
		addConfigurableProperties(configurableProperties);
	}
	
	@Override
	public final void setMap(Map map){
		this.map = map;
	}

	private String generateUniqueId() {
		String host = "UNKNOWN";
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return host + ":" + System.currentTimeMillis() + ":" + Math.random();
	}

	protected abstract void addConfigurableProperties(List<ConfigurableProperties> configurableProperties);


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void move(int x, int y) {
		this.x=x;
		this.y=y;
		notifyUpdateListeners(this);
	}
	
	public void setAngle(double theta){
		this.theta=theta;
		notifyUpdateListeners(this);
	}
	
	public double getAngle(){
		return theta;
	}
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name=name;
		notifyUpdateListeners(this);
	}
	
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id=id;
	}
	
	public final Color getColor() {
		return color;
	}
	
	public final void setColor(Color color) {
		this.color = color;
	}

	public final double getTransparency() {
		return transparency;
	}

	protected final void setTransparency(double percent){
		this.transparency=Math.min(Math.max(percent, 0.0), 1.0);
	}
	
	public final void draw(Graphics2D g){
		drawBeforeTransform(g);
		AffineTransform transform=g.getTransform();
		g.rotate(theta,x,y);
		drawDuringTransform(g);
		g.setTransform(transform);
		drawAfterTransform(g);
	}
	
	void drawBeforeTransform(Graphics2D g){}
	void drawDuringTransform(Graphics2D g){}
	void drawAfterTransform(Graphics2D g){}
	void onResize(){}


	public void addUpdateListener(UpdateListener listener) {
		listeners.add(listener);
	}

	public void removeUpdateListener(UpdateListener listener) {
		listeners.remove(listener);
	}
	
	protected void notifyUpdateListeners(Mappable updated){
		for (UpdateListener listener : listeners) {
			listener.onUpdate(updated);
		}
	}

	public String toString(){
		return name;
	}

	protected String color2string(Color color){
		String r=Integer.toHexString(color.getRed());
		if(r.length()<2) r="0"+r;
		String g=Integer.toHexString(color.getGreen());
		if(g.length()<2) g="0"+g;
		String b=Integer.toHexString(color.getBlue());
		if(b.length()<2) b="0"+b;
		return r+g+b;
	}
	
	protected Color string2color(String hex){
		return Color.decode("0x"+hex);
	}

	@Override
	public final List<ConfigurableProperties> getConfigurableProperties(){
		return configurableProperties;
	}
	
	@Override
	public void setConfiguredValue(ConfigurableProperties key, String value){
		switch(key) {
			case NAME:
				this.name = value;
				break;
			case HEX_COLOR:
				color=string2color(value);
				break;
			case TRANSPARENCY:
				setTransparency(Double.parseDouble(value)/100.0);
				break;
			default:
		}
		notifyUpdateListeners(this);
	}
	
	@Override
	public String getConfiguredValue(ConfigurableProperties key){
		switch(key) {
			case NAME:
				return this.name;
			case HEX_COLOR:
				return color2string(color);
			case TRANSPARENCY:
				return FormatUtils.formatDecimal(transparency*100.0);
			default:
		}
		return null;
	}


}
