package org.cresse.rpg.client.map.event;

public interface Updatable {
	
	void addUpdateListener(UpdateListener listener);
	void removeUpdateListener(UpdateListener listener);

}
