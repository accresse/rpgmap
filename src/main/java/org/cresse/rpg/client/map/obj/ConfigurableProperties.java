package org.cresse.rpg.client.map.obj;

public enum ConfigurableProperties {
	
	ID("ID"),
	NAME("Name"),
	CLASS("Class"),
	HEX_COLOR("Hex Color"),
	TRANSPARENCY("Transparency %"),
	RADIUS("Radius (ft)"),
	WIDTH("Width (ft)"),
	HEIGHT("Height (ft)"), 
	IMAGE("Image"), 
	MAX_HP("Max HP"), 
	HP("HP"); 
	
	private String displayName;
	
	ConfigurableProperties(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName(){
		return displayName;
	}

}
