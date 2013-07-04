package org.cresse.rpg.client.map;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Properties;

import org.cresse.rpg.client.map.obj.ConfigurableProperties;
import org.cresse.rpg.client.map.obj.MapUnit;

public class HardCodedMapObjectSource implements IMapObjectSource {
	
	private Hashtable<String, Object> model;
	
	public HardCodedMapObjectSource(File workingDir){
		model=new Hashtable<String, Object>();

		File dir=new File(workingDir, "RpgMapObjects");
		model.put("RpgMapObjects", getModelForDir(dir));

	}
	
	private Object getModelForDir(File dir){
		Hashtable<Object, Object> model=new Hashtable<Object, Object>();
		File[] files=dir.listFiles(new MapObjectFilenameFilter());
		if(files!=null) {
			for (File f : files) {
				if(f.isDirectory()){
					model.put(formatName(f.getName()), getModelForDir(f));
				} else if(f.getName().endsWith(".properties")){
					loadObjectFromProperties(f, model);
				} else {
					loadObjectFromImage(f, model);
				}
			}
		}
		return model;
	}

	private void loadObjectFromImage(File f, Hashtable<Object, Object> model) {
		Properties props=new Properties();
		props.setProperty(ConfigurableProperties.NAME.name(), formatName(f.getName()));
		props.setProperty(ConfigurableProperties.CLASS.name(), MapUnit.class.getName());
		props.setProperty(ConfigurableProperties.HEX_COLOR.name(), "000000");
		props.setProperty(ConfigurableProperties.IMAGE.name(),f.getAbsolutePath());
		MapObjectDef def=new MapObjectDef(props);
		model.put(def,def);
	}
	
	private Properties loadObjectFromProperties(File f, Hashtable<Object, Object> model) {
		Properties props = new Properties();
		FileReader reader = null;
		try {
			reader = new FileReader(f);
			props.load(reader);
			//if no name, use filename w/o extension
			if(!props.containsKey(ConfigurableProperties.NAME.name())) {
				props.setProperty(ConfigurableProperties.NAME.name(), f.getName().replaceAll("\\..*", ""));
			}
			MapObjectDef def=new MapObjectDef(props);
			model.put(def,def);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try{ reader.close(); } catch(Exception ex){}
		}
		return props;
	}

	protected static String formatName(String orig){
		String name=orig.replaceAll("\\....","");
		name=name.replaceAll("_", " ");
		return name;
	}

	public Hashtable<String, Object> getMapObjectSelection() {
		return model;
	}
	
	class MapObjectFilenameFilter implements FileFilter{

		public boolean accept(File pathname) {
			String lowercase=pathname.getName().toLowerCase();
			boolean accepts=( pathname.isDirectory()
					|| lowercase.endsWith("jpg")
					|| lowercase.endsWith("gif")
					|| lowercase.endsWith("png")
					|| lowercase.endsWith("bmp")
					|| lowercase.endsWith("properties"));
			return accepts;
		}
		
	}

}
