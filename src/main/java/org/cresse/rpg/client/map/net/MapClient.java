package org.cresse.rpg.client.map.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.cresse.rpg.client.map.MapViewer;
import org.cresse.rpg.client.map.PropertiesMapSaver;
import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapClient implements Runnable, MapListener{
	
	private boolean isRunning = false;
	private String host = "localhost";
	private int port = 24601;
	private PropertiesMapSaver saver = new PropertiesMapSaver();
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private MapViewer mapViewer;
	private Map map;
	private Protocol protocol = new Protocol();
	
	public MapClient(MapViewer mapViewer) {
		this.mapViewer = mapViewer;
	}

	public void setHost(String host) {
		this.host = host;
	}
		
	public void setPort(int port) {
		this.port = port;
	}
	
	public void stop() {
		this.isRunning = false;
	}

	@Override
	public void run() {
		this.isRunning = true;
		Socket sock = null;
		try {
			System.out.println("Connecting...");
			sock = new Socket(host, port);
			ois = new ObjectInputStream(sock.getInputStream());
			oos = new ObjectOutputStream(sock.getOutputStream());
			System.out.println("Connected.");
			while(isRunning) {
				processIncomingEvent();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void processIncomingEvent() throws IOException, ClassNotFoundException, Exception, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		map = protocol.processIncomingEvent(map, mapViewer, saver, ois, MapClient.class);
	}

	@Override
	public void onMapObjectAdd(MapEvent<Mappable> event) {
		onMapEvent(event);
	}

	@Override
	public void onMapObjectRemove(MapEvent<Mappable> event) {
		onMapEvent(event);
	}

	@Override
	public void onMapObjectChange(MapEvent<Mappable> event) {
		onMapEvent(event);
	}

	@Override
	public void onNewMap(MapEvent<Map> event) {
	}

	public void onMapEvent(MapEvent<? extends Serializable> event) {
		if(isRunning && event.getSource()!=MapClient.class) {
			try {
				protocol.onMapEvent(event, saver, oos);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
}
