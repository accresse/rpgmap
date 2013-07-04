package org.cresse.rpg.client.map.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapEvent.EventType;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.event.UpdateListener;
import org.cresse.rpg.client.map.obj.ConfigurableProperties;
import org.cresse.rpg.client.map.obj.Mappable;
import org.cresse.rpg.client.map.util.ImageUtils;

public class Map implements UpdateListener, Serializable{
	
	private static final long serialVersionUID = 1L;

	private String bgName;
	private Color bgColor;
	private int width;
	private int height;
	private BufferedImage bgImage;

	private double pixelsPerFoot;
	private double feetPerGridLine;
	
	private List<Mappable> mapObjects;
	private java.util.Map<String, Mappable> mapObjectMap;
	private List<MapListener> listeners;
	
	public Map(String bgName, Color bgColor, int width, int height, double pixelsPerFoot, double feetPerGridLine){
		this.bgName=bgName;
		this.bgColor = bgColor;
		this.width=width;
		this.height=height;
		this.bgImage=ImageUtils.loadImageWithBgColor(bgName, width,height, bgColor);
		
		this.pixelsPerFoot=pixelsPerFoot;
		this.feetPerGridLine=feetPerGridLine;
		
		this.mapObjects=new ArrayList<Mappable>();
		this.mapObjectMap=new HashMap<String, Mappable>();
		this.listeners=new ArrayList<MapListener>();
	}
	
	public String getBgName() {
		return bgName;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BufferedImage getBgImage() {
		return bgImage;
	}

	public double getPixelsPerFoot() {
		return pixelsPerFoot;
	}

	public double getFeetPerGridLine() {
		return feetPerGridLine;
	}

	public void addMapObject(Mappable mappable, Object source){
		mappable.setMap(this);
		mapObjects.add(0,mappable);
		mapObjectMap.put(mappable.getId(), mappable);
		mappable.addUpdateListener(this);
		MapEvent<Mappable> event = new MapEvent<Mappable>(EventType.ADD_OBJECT,mappable);
		event.setSource(source);
		for (MapListener listener : listeners) {
			listener.onMapObjectAdd(event);
		}
	}

	public void removeMapObject(Mappable mappableToRemove, Object source){
		Mappable mappable = mapObjectMap.get(mappableToRemove.getId());
		if(mappable!=null) {
			mapObjects.remove(mappable);
			mappable.removeUpdateListener(this);
			MapEvent<Mappable> event = new MapEvent<Mappable>(EventType.REMOVE_OBJECT, mappable);
			event.setSource(source);
			for (MapListener listener : listeners) {
				listener.onMapObjectRemove(event);
			}
		}
	}
	
	public void updateMapObject(Mappable templateObj, Object source){
		Mappable mappable = mapObjectMap.get(templateObj.getId());
		List<ConfigurableProperties> configurableProperties = mappable.getConfigurableProperties();
		for (ConfigurableProperties key : configurableProperties) {
			mappable.setConfiguredValue(key, templateObj.getConfiguredValue(key));
		}
	}
	
	public void sendMapObjectToFront(Mappable mappable, Object source){
		mapObjects.remove(mappable);
		mapObjects.add(0, mappable);
		for (MapListener listener : listeners) {
			listener.onMapObjectChange(new MapEvent<Mappable>(EventType.CHANGE_OBJECT,mappable));
		}
	}

	public void sendMapObjectToBack(Mappable mappable, Object source){
		mapObjects.remove(mappable);
		mapObjects.add(mappable);
		for (MapListener listener : listeners) {
			listener.onMapObjectChange(new MapEvent<Mappable>(EventType.CHANGE_OBJECT, mappable));
		}
	}

	public Mappable getMapObjectAt(int x, int y){
		for (Mappable mapObj : mapObjects) {
			if(mapObj.contains(x, y)){
				return mapObj;
			}
		}
		return null;
	}
	
	public List<Mappable> getAllMapObjects(){
		return mapObjects; 
	}

	public void addMapListener(MapListener listener) {
		listeners.add(listener);
	}

	public void removeMapListener(MapListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void onUpdate(Object updated) {
		for (MapListener listener : listeners) {
			listener.onMapObjectChange(new MapEvent<Mappable>(EventType.CHANGE_OBJECT, (Mappable)updated));
		}
	}

	public int getCenterX() {
		return this.width/2;
	}

	public int getCenterY() {
		return this.height/2;
	}

}
