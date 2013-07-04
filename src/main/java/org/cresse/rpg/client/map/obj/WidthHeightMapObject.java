package org.cresse.rpg.client.map.obj;

import java.util.List;

import org.cresse.rpg.client.map.model.Map;

public abstract class WidthHeightMapObject extends AbstractMapObject {

	private double widthInFeet=5.0;
	private double heightInFeet=10.0;
	private double widthInPixels=-1;
	private double heightInPixels=-1;
	
	public WidthHeightMapObject(String name, Map map, int x, int y) {
		super(name, map, x, y);
	}

	@Override
	protected void addConfigurableProperties(List<ConfigurableProperties> configurableProperties) {
		configurableProperties.add(ConfigurableProperties.WIDTH);
		configurableProperties.add(ConfigurableProperties.HEIGHT);
	}
		
	@Override
	public void setConfiguredValue(ConfigurableProperties key, String value){
		switch(key) {
			case WIDTH:
				widthInFeet=Double.parseDouble(value);
				widthInPixels=-1;
				onResize();
				break;
			case HEIGHT:
				heightInFeet=Double.parseDouble(value);
				heightInPixels=-1;
				onResize();
				break;
			default:
		}
		super.setConfiguredValue(key, value);
	}
	
	@Override
	public String getConfiguredValue(ConfigurableProperties key){
		switch(key) {
			case WIDTH:
				return String.valueOf(widthInFeet);
			case HEIGHT:
				return String.valueOf(heightInFeet);
			default:
		}
		return super.getConfiguredValue(key);
	}
	
	protected final double getWidthInFeet() {
		return this.widthInFeet;
	}

	protected final double getWidthInPixels(){
		if(widthInPixels<0) {
			calculateWidthInPixels();
		}
		return widthInPixels;
	}

	private void calculateWidthInPixels() {
		widthInPixels = widthInFeet*map.getPixelsPerFoot();
	}
	
	protected final double getHeightInFeet() {
		return this.heightInFeet;
	}
	
	protected final double getHeightInPixels(){
		if(heightInPixels<0) {
			calculateHeightInPixels();
		}
		return heightInPixels;
	}

	private void calculateHeightInPixels() {
		heightInPixels = heightInFeet*map.getPixelsPerFoot();
	}
	
}
