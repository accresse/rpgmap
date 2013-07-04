package org.cresse.rpg.client.map.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.cresse.rpg.client.map.MapViewer;
import org.cresse.rpg.client.map.PropertiesMapSaver;
import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapEvent.EventType;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;

public class Protocol {

	public void onMapEvent(MapEvent<? extends Serializable> event, PropertiesMapSaver saver, ObjectOutputStream oos) throws IOException {
		System.out.println(event);
		EventType eventType = event.getEventType();
		Properties props = null;
		switch (eventType) {
			case NEW_MAP:
				props = saver.convertMap((Map) event.getUpdatedObject());
				break;
			default:
				props = new Properties();
				saver.saveMappable(props, (Mappable) event.getUpdatedObject());
				break;
		}
		oos.writeObject(eventType);
		oos.writeObject(props);
	}

	public Map processIncomingEvent(Map map, MapViewer mapViewer, PropertiesMapSaver saver, ObjectInputStream ois, Object source) throws IOException, ClassNotFoundException, Exception, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		MapEvent.EventType eventType = (EventType) ois.readObject();
		Properties props = (Properties) ois.readObject();
		Mappable mappable = null;
		switch(eventType) {
			case NEW_MAP:
				map = saver.convertProperties(props);
				mapViewer.loadMap(map);
				break;
			case ADD_OBJECT:
				mappable = saver.convertMappable(props, map);
				mapViewer.getMap().addMapObject(mappable, source);
				break;
			case REMOVE_OBJECT:
				mappable = saver.convertMappable(props, map);
				mapViewer.getMap().removeMapObject(mappable, source);
				break;
			case CHANGE_OBJECT:
				mappable = saver.convertMappable(props, map==null?mapViewer.getMap():map);
				mapViewer.getMap().removeMapObject(mappable, source);
				mapViewer.getMap().addMapObject(mappable, source);
				//mapViewer.getMap().updateMapObject(mappable, source);
				break;
		}
		
		return map;
	}
	
}
