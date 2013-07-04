package org.cresse.rpg.client.map.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cresse.rpg.client.map.MapViewer;
import org.cresse.rpg.client.map.PropertiesMapSaver;
import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapEvent.EventType;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapServer implements Runnable, MapListener{
	
	private boolean isRunning = false;
	private int port = 24601;
	private List<MapServerConnection> conns;
	private PropertiesMapSaver saver = new PropertiesMapSaver();
	private MapViewer mapViewer;
	
	public MapServer(MapViewer mapViewer) {
		this.mapViewer = mapViewer;
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
		ServerSocket ss = null;
		conns = new LinkedList<MapServerConnection>();
		try {
			ss = new ServerSocket(port);
			ss.setSoTimeout(1000);
			System.out.println("Listening...");
			while(isRunning) {
				try {
					Socket connSocket = ss.accept();
					System.out.println("Got connection.");
					MapServerConnection conn = new MapServerConnection(connSocket, saver, mapViewer);
					conn.start();
					Map map = mapViewer.getMap();
					if(map!=null) {
						conn.onMapEvent(new MapEvent<Serializable>(EventType.NEW_MAP, map));
					}
					conns.add(conn);
				} catch(SocketTimeoutException stoe) {}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Stopping...");
		for (MapServerConnection conn : conns) {
			conn.close();
		}
		System.out.println("Stopped.");
	}

	@Override
	public void onMapObjectAdd(MapEvent<Mappable> event) {
		notifyConnections(event);
	}

	@Override
	public void onMapObjectRemove(MapEvent<Mappable> event) {
		notifyConnections(event);
	}

	@Override
	public void onMapObjectChange(MapEvent<Mappable> event) {
		notifyConnections(event);
	}

	@Override
	public void onNewMap(MapEvent<Map> event) {
		notifyConnections(event);
	}

	private void notifyConnections(MapEvent<? extends Serializable> event) {
		if(isRunning && conns!=null) {
			Iterator<MapServerConnection> it = conns.iterator();
			while(it.hasNext()) {
				MapServerConnection conn = it.next();
				try {
					if(conn!=event.getSource()) {
						conn.onMapEvent(event);
					}
				} catch(IOException ex) {
					ex.printStackTrace();
					conn.close();
					it.remove();
				}
			}
		}
	}
	
}
