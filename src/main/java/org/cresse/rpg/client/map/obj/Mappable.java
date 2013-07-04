package org.cresse.rpg.client.map.obj;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.List;

import org.cresse.rpg.client.map.event.Updatable;
import org.cresse.rpg.client.map.model.Map;

public interface Mappable extends Updatable, Serializable{
	
	int getX();
	int getY();
	String getName();
	
	void draw(Graphics2D g);

	boolean contains(int x, int y);
	void move(int x, int y);

	void setAngle(double theta);
	double getAngle();
	
	void setId(String id);
	String getId();
	
	List<ConfigurableProperties> getConfigurableProperties();
	void setConfiguredValue(ConfigurableProperties key, String value);
	String getConfiguredValue(ConfigurableProperties key);
	void setMap(Map map);


}
