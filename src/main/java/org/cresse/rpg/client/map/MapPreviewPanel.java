package org.cresse.rpg.client.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.cresse.rpg.client.map.event.UpdateListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;

public class MapPreviewPanel extends JPanel implements MouseListener, UpdateListener {
	
	private static final long serialVersionUID = 1L;

	private Map lastMap=null;
	private MapPanel panel;
	private BufferedImage mapImg;
	private double scale;
	private BufferedImage img;
	private Graphics2D g2d;
	private Image scaled;
	private int panelWidth=200;
	private int panelHeight=panelWidth;
	private int imgWidth=panelWidth;
	private int imgHeight=panelHeight;
	private int cornerX=0;
	private int cornerY=0;
	
	public MapPreviewPanel(MapPanel panel){
		this.panel=panel;
		initMapImg();
		img=new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
		g2d=img.createGraphics();
		this.setPreferredSize(new Dimension(panelWidth,panelHeight));
		this.addMouseListener(this);
		panel.addUpdateListener(this);
	}
	
	private void initMapImg(){
		if(mapImg==null){
			mapImg=panel.getGridlessImage();
		}
		if(mapImg!=null){
			if(mapImg.getWidth()>mapImg.getHeight()){
				scale=(double)panelWidth/(double)mapImg.getWidth();
				imgWidth=panelWidth;
				imgHeight=(int)(mapImg.getHeight()*scale);
			} else{
				scale=(double)panelHeight/(double)mapImg.getHeight();
				imgWidth=(int)(mapImg.getWidth()*scale);
				imgHeight=panelHeight;
			}
			scaled=getScaledMap(mapImg);
		}
	}
	
	public void paint(Graphics g){
		g.drawImage(img,0,0,null);
	}
	
	private void makeImage(){
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, panelWidth, panelHeight);
		drawImage();
		drawBox();
	}
	
	private void drawImage(){
		if(mapImg==null){
			initMapImg();
		} else {
			scaled=getScaledMap(mapImg);
			cornerX = (panelWidth-scaled.getWidth(this))/2;
			cornerY = (panelHeight-scaled.getHeight(this))/2;
			g2d.drawImage(scaled, cornerX, cornerY, null);//use this for real-time updates
		}
	}
	
	private void drawBox(){
		if(mapImg!=null){
			g2d.setColor(Color.WHITE);
			Rectangle rect=panel.getVisibleMap();
			g2d.drawRect((int)(rect.getX()*scale)+cornerX, (int)(rect.getY()*scale+cornerY),
					(int)(rect.getWidth()*scale), (int)(rect.getHeight()*scale));
		}
	}

	private Image getScaledMap(BufferedImage bi){
		Image img=bi.getScaledInstance(imgWidth,imgHeight, BufferedImage.SCALE_DEFAULT);
		return img;
	}
	
	public void mouseClicked(MouseEvent e) {
		double x=(e.getX()-cornerX)/scale;
		double y=(e.getY()-cornerY)/scale;
		panel.centerOnPoint((int)x, (int)y);
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void onUpdate(Object updated) {
		if(lastMap!=panel.getMap()){
			lastMap=panel.getMap();
			mapImg=null;
		}
		makeImage();
		repaint();
	}


}
