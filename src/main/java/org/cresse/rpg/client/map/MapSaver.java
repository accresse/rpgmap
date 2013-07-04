package org.cresse.rpg.client.map;

import java.io.File;

import org.cresse.rpg.client.map.model.Map;

public interface MapSaver {
	
	Map loadMap(String fileName) throws Exception;
	Map loadMap(File file) throws Exception;
	
	void saveMap(Map map, String fileName) throws Exception;	
	void saveMap(Map map, File file) throws Exception;

}
