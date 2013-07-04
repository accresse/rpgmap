package org.cresse.rpg.client.map.event;

import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;


public interface MapListener {
	
	void onNewMap(MapEvent<Map> event);
	void onMapObjectAdd(MapEvent<Mappable> event);
	void onMapObjectRemove(MapEvent<Mappable> event);
	void onMapObjectChange(MapEvent<Mappable> event);

}
