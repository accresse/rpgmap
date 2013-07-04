package org.cresse.rpg.client.map.event;

import java.io.Serializable;

public class MapEvent<T extends Serializable> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static enum EventType implements Serializable{
		NEW_MAP,
		ADD_OBJECT,
		REMOVE_OBJECT,
		CHANGE_OBJECT
	}
	
	private EventType eventType;
	private T updatedObject;
	private Object source;
	
	public MapEvent(EventType eventType, T updatedObject) {
		this.eventType = eventType;
		this.updatedObject = updatedObject;
	}
	
	public EventType getEventType() {
		return this.eventType;
	}
	
	public T getUpdatedObject() {
		return this.updatedObject;
	}
	
	public void setSource(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public String toString() {
		return eventType + ": " + updatedObject;
	}

}
