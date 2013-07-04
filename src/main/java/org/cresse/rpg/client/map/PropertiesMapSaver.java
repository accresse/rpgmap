package org.cresse.rpg.client.map;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.ConfigurableProperties;
import org.cresse.rpg.client.map.obj.Mappable;

public class PropertiesMapSaver implements MapSaver, Serializable {

	private static final String MAPOBJECT_PREFIX = "mapobject";
	private static final String SEP = ".";
	private static final String POS_X = "X";
	private static final String POS_Y = "Y";
	private static final String ID = "ID";
	private static final String POS_ANGLE = "ANGLE";
	private static final String MAP_OBJECT_COUNT = "map.OBJECT_COUNT";
	private static final String MAP_HEIGHT = "map.HEIGHT";
	private static final String MAP_WIDTH = "map.WIDTH";
	private static final String MAP_BG_COLOR = "map.BG_COLOR";
	private static final String MAP_FEET_PER_GRID_LINE = "map.FEET_PER_GRID_LINE";
	private static final String MAP_PIXELS_PER_FOOT = "map.PIXELS_PER_FOOT";
	private static final String MAP_BG_NAME = "map.BG_NAME";
	
	private static final Class<?>[] MAPOBJ_PARAMS = new Class[]{Map.class,int.class,int.class};

	public Map loadMap(String fileName) throws Exception{
		return loadMap(new FileInputStream(fileName));
	}

	public Map loadMap(File file) throws Exception{
		return loadMap(new FileInputStream(file));
	}

	public Map loadMap(InputStream in) throws Exception{
		Properties props=new Properties();
		props.load(in);
		in.close();
		return convertProperties(props);
	}
  	
	public void saveMap(Map map, String fileName) throws Exception{
		OutputStream out=new FileOutputStream(fileName);
		saveMap(map, out);
		out.close();
	}

	public void saveMap(Map map, File file) throws Exception{
		OutputStream out=new FileOutputStream(file);
		saveMap(map, out);
		out.close();
	}

	public void saveMap(Map map, OutputStream out) throws Exception{
		Properties props=convertMap(map);
		props.store(out, "Saved by RPG Map Viewer");
	}
	
	public Map convertProperties(Properties props) throws Exception{
		String bgName=addHomeDir(props.getProperty(MAP_BG_NAME));
		Color bgColor = getPropAsColor(props, MAP_BG_COLOR, Color.BLACK);
		int width = getPropAsInt(props, MAP_WIDTH, 1000);
		int height = getPropAsInt(props, MAP_HEIGHT, 500);
		double pixelsPerFoot=getPropAsDouble(props,MAP_PIXELS_PER_FOOT,10.0);
		double feetPerGridLine=getPropAsDouble(props,MAP_FEET_PER_GRID_LINE,5.0);
		Map map=new Map(bgName,bgColor,width,height,pixelsPerFoot,feetPerGridLine);
		
		int mapObjects=getPropAsInt(props,MAP_OBJECT_COUNT,0);
		for(int i=0; i< mapObjects; i++){
			Mappable mappable = convertMappable(props, map, i);
			map.addMapObject(mappable, PropertiesMapSaver.class);
		}
		
		return map;
	}

	public Mappable convertMappable(Properties props, Map map) throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return convertMappable(props, map, 0);
	}
	
	private Mappable convertMappable(Properties props, Map map, int i) throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String objPrefix=MAPOBJECT_PREFIX+SEP+i+SEP;
		String className=props.getProperty(objPrefix+ConfigurableProperties.CLASS);
		int x=getPropAsInt(props,objPrefix+POS_X,100);
		int y=getPropAsInt(props,objPrefix+POS_Y,100);
		String id=props.getProperty(objPrefix+ID);
		double angle=getPropAsDouble(props,objPrefix+POS_ANGLE,0.0);
		Constructor<?> constructor=Class.forName(className).getConstructor(MAPOBJ_PARAMS);
		Mappable mappable=(Mappable)constructor.newInstance(new Object[]{map,new Integer(x),new Integer(y)});
		mappable.setAngle(angle);
		if(id!=null) {
			mappable.setId(id);
		}
		List<ConfigurableProperties> list=mappable.getConfigurableProperties();
		for (ConfigurableProperties key : list) {
			String val=props.getProperty(objPrefix+key);
			val = addHomeDir(val);
			if(val!=null){
				mappable.setConfiguredValue(key, val);
			}
		}
		return mappable;
	}
	
	private String getHomeDir() {
		String home = System.getProperty("user.home");
		if(home.endsWith("\\") || home.endsWith("/")) {
			home = home.substring(0,home.length()-1);
		}
		return home;
	}
	
	private String addHomeDir(String val) {
		String home = getHomeDir();
		return val.replace("USER.HOME", home);
	}

	private String removeHomeDir(String val) {
		String home = getHomeDir();
		return val.replace(home,"USER.HOME");
	}

	public Properties convertMap(Map map){
		Properties props = new Properties() {
			private static final long serialVersionUID = 1L;

			@Override
		    public Set<Object> keySet(){
		        return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
		    }

		    @Override
		    public synchronized Enumeration<Object> keys() {
		        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		    }
		};
		
		if(map.getBgName()!=null) {
			props.setProperty(MAP_BG_NAME,removeHomeDir(map.getBgName()));
		}
		if(map.getBgColor()!=null) {
			String hex = Integer.toHexString(map.getBgColor().getRGB());
			hex = hex.substring(2, hex.length());
			props.setProperty(MAP_BG_COLOR, hex);
		}
		props.setProperty(MAP_WIDTH, Integer.toString(map.getWidth()));
		props.setProperty(MAP_HEIGHT, Integer.toString(map.getHeight()));
		props.setProperty(MAP_PIXELS_PER_FOOT, Double.toString(map.getPixelsPerFoot()));
		props.setProperty(MAP_FEET_PER_GRID_LINE, Double.toString(map.getFeetPerGridLine()));
		
		List<Mappable> mapObjects=map.getAllMapObjects();
		props.setProperty(MAP_OBJECT_COUNT, Integer.toString(mapObjects.size()));
		
		for (int i=0; i<mapObjects.size(); i++) {
			Mappable mappable=(Mappable) mapObjects.get(i);
			saveMappable(props, i, mappable);
		}
		
		return props;
	}

	public void saveMappable(Properties props, Mappable mappable) {
		saveMappable(props, 0, mappable);
	}
	
	private void saveMappable(Properties props, int i, Mappable mappable) {
		String objPrefix=MAPOBJECT_PREFIX+SEP+i+SEP;
		props.setProperty(objPrefix+ConfigurableProperties.ID, mappable.getId());
		props.setProperty(objPrefix+ConfigurableProperties.CLASS, mappable.getClass().getName());
		props.setProperty(objPrefix+ConfigurableProperties.NAME, mappable.getName());
		props.setProperty(objPrefix+POS_X, Integer.toString(mappable.getX()));
		props.setProperty(objPrefix+POS_Y, Integer.toString(mappable.getY()));
		props.setProperty(objPrefix+POS_ANGLE, Double.toString(mappable.getAngle()));
		
		List<ConfigurableProperties> list=mappable.getConfigurableProperties();
		for (ConfigurableProperties key : list) {
			String val=mappable.getConfiguredValue(key);
			if(val!=null){
				props.setProperty(objPrefix+key, removeHomeDir(val));
			}
		}
	}

	private static int getPropAsInt(Properties props, String key, int defaultProp){
		String val=props.getProperty(key);
		if(val==null){
			return defaultProp;
		} else {
			return Integer.parseInt(val);
		}
	}

	private static double getPropAsDouble(Properties props, String key, double defaultProp){
		String val=props.getProperty(key);
		if(val==null){
			return defaultProp;
		} else {
			return Double.parseDouble(val);
		}
	}
	
	private static Color getPropAsColor(Properties props, String key, Color defaultProp) {
		String val=props.getProperty(key);
		if(val==null){
			return defaultProp;
		} else {
			return new Color(Integer.parseInt(val, 16));
		}

	}


}
