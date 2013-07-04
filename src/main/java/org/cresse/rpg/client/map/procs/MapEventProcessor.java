package org.cresse.rpg.client.map.procs;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface MapEventProcessor extends MouseListener, MouseMotionListener{
	
	void draw(Graphics g);
	
	void reset();

}
