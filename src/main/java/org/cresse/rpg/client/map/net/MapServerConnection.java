package org.cresse.rpg.client.map.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.cresse.rpg.client.map.MapViewer;
import org.cresse.rpg.client.map.PropertiesMapSaver;
import org.cresse.rpg.client.map.event.MapEvent;

public class MapServerConnection extends Thread {
	
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private PropertiesMapSaver saver;
	private Protocol protocol = new Protocol();
	private boolean isRunning = true;
	private MapViewer mapViewer;

	public MapServerConnection(Socket clientSocket, PropertiesMapSaver saver, MapViewer mapViewer) throws Exception {
		this.clientSocket = clientSocket;
		this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
		this.ois = new ObjectInputStream(clientSocket.getInputStream());
		this.saver = saver;
		this.mapViewer = mapViewer;
	}
	
	public void run() {
		isRunning = true;
		try {
			while(isRunning) {
				protocol.processIncomingEvent(mapViewer.getMap(), mapViewer, saver, ois, this);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			close();
		}
	}

	public void close() {
		isRunning = false;
		try {clientSocket.close();} catch(Exception ex){}
		try {oos.close();} catch(Exception ex){}
	}

	public void onMapEvent(MapEvent<? extends Serializable> event) throws IOException {
		protocol.onMapEvent(event, saver, oos);
	}

}
