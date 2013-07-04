package org.cresse.rpg.client.map;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Properties;

import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.ConfigurableProperties;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapObjectDef {
	
	private static final Class<?>[] PARAMETERS={Map.class,int.class,int.class};
	
	private String name;
	private String className;
	private Properties props;
	
	public MapObjectDef(Properties props){
		this(props.getProperty(ConfigurableProperties.NAME.name()),props.getProperty(ConfigurableProperties.CLASS.name()));
		this.props=props;
	}
	
	public MapObjectDef(String name, String className){
		this.name=name;
		this.className=className;
	}
	
	public String toString(){
		return name;
	}
	
	public Mappable createInstance(Map map, int x, int y) throws Exception{
		Constructor<?> constructor=Class.forName(className).getConstructor(PARAMETERS);
		Mappable mappable=(Mappable)constructor.newInstance(new Object[]{map,new Integer(x),new Integer(y)});
		List<ConfigurableProperties> configs=mappable.getConfigurableProperties();
		if(configs!=null && props!=null){
			for (ConfigurableProperties key : configs) {
				String value = props.getProperty(key.name());
				if(value!=null){
					mappable.setConfiguredValue(key, value);
				}
			}
		}
		return mappable;
	}

}
