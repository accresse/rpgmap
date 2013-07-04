package org.cresse.rpg.client.map;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.event.Updatable;
import org.cresse.rpg.client.map.event.UpdateListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.obj.Mappable;
import org.cresse.rpg.client.map.procs.DefaultEventProcessor;
import org.cresse.rpg.client.map.procs.MapEventProcessor;
import org.cresse.rpg.client.map.util.ImageUtils;

public class MapPanel extends JPanel implements MouseListener,MouseMotionListener,Updatable,MapListener,ActionListener{
	
	private static final long serialVersionUID = 1L;

	private BufferedImage bi;
	private BufferedImage gridlessBi;
	private Graphics2D g2d;
	private Graphics2D gridlessG2d;
	private MapEventProcessor eventProc;
	private List<UpdateListener> listeners;
	private List<Mappable> selected;
	private Rectangle selectRect;
	
	private int w;
	private int h;
	private Map map;
	private BufferedImage bg;
	private double pixelsPerFoot;
	private double feetPerGridLine;
	//private JLabel statusBar;
	private java.util.Map<String, MapEventProcessor> eventProcessors;
	private MapObjectOptionDialog optionDialog;
	private MapObjectPopupMenu popupMenu;
	private boolean shouldShowGrid = true;
	
	public MapPanel(JFrame parent, JLabel statusBar){
		//this.statusBar=statusBar;
		this.listeners=new ArrayList<UpdateListener>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		setupKeyStrokes();
		eventProc=new DefaultEventProcessor(null,null);
		optionDialog=new MapObjectOptionDialog(parent);
		popupMenu=new MapObjectPopupMenu();
		popupMenu.addActionListener(this);
		this.setOpaque(false);
	}
	
	private void setupKeyStrokes(){
		Action delete=new AbstractAction(){

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		};
		this.getActionMap().put("delete", delete);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), "delete");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0), "delete");
	}
	
	public void setEventProcessors(java.util.Map<String, MapEventProcessor> eventProcessors){
		this.eventProcessors=eventProcessors;
		this.eventProc=(MapEventProcessor) eventProcessors.get("move");
	}
	
	private void init(Map map){
		this.map=map;
		this.bg=map.getBgImage();
		this.w=bg.getWidth();
		this.h=bg.getHeight();
		this.setPreferredSize(new Dimension(w,h));
		this.pixelsPerFoot=map.getPixelsPerFoot();
		this.feetPerGridLine=map.getFeetPerGridLine();
		bi=ImageUtils.getBlankImage(w, h, Color.BLACK);
		gridlessBi=ImageUtils.getBlankImage(w, h, Color.BLACK);
		g2d=bi.createGraphics();
		gridlessG2d=gridlessBi.createGraphics();
		map.addMapListener(this);
		notifyUpdateListeners(bi);
		this.invalidate();
		this.centerOnPoint(0, 0);
	}
	
	public Map getMap(){
		return map;
	}
			
	public BufferedImage getImage(){
		return bi;
	}
	
	public BufferedImage getGridlessImage(){
		return gridlessBi;
	}
	
	public BufferedImage getBGImage(){
		return bg;
	}
	
	public Graphics2D getG2d(){
		return g2d;
	}
	
	public Rectangle getVisibleMap(){
		Rectangle visibleRect = this.getVisibleRect();
		return visibleRect;
	}
	
	public Point2D getVisibleCenter() {
		Rectangle visibleRect = getVisibleMap();
		Point2D.Double center = new Point2D.Double(visibleRect.getCenterX(),visibleRect.getCenterY());
		if(visibleRect.getWidth()>map.getWidth()) {
			center.x=map.getCenterX();
		}
		if(visibleRect.getHeight()>map.getHeight()) {
			center.y=map.getCenterY();
		}
		return center;
	}

	@Override
	public void paintComponent(Graphics g){
		generateMap();
		if(bi!=null){
			g.drawImage(bi,0,0,this);
		}
	}
	
	protected void generateMap() {
		if(g2d!=null){
			//draw bg
			g2d.drawImage(bg, 0, 0, this);
			gridlessG2d.drawImage(bg, 0, 0, this);
			
			//draw grid
			if(shouldShowGrid) {
				drawGridLines(g2d);
			}
			
			//draw objects
			drawMapObjects(g2d);
			drawMapObjects(gridlessG2d);
			
			//draw proc details
			eventProc.draw(g2d);
			
			if(selectRect!=null){
				g2d.draw(selectRect);
			}
			
			notifyUpdateListeners(bi);
		}
	}
	
	private void drawMapObjects(Graphics2D g2d) {
		int size=map.getAllMapObjects().size();
		for (int i = size-1; i >= 0; i--) {
			Mappable obj = (Mappable) map.getAllMapObjects().get(i);
			obj.draw(g2d);
		}
	}

	private void drawGridLines(Graphics2D g2d){
		g2d.setColor(Color.GRAY);
		double pixelsPerGridLine=pixelsPerFoot*feetPerGridLine;
		double i=0;
		while(i<w){
			g2d.drawLine((int)i, 0, (int)i, h);
			i+=pixelsPerGridLine;
		}

		i=0;
		while(i<h){
			g2d.drawLine(0, (int)i, w, (int)i);
			i+=pixelsPerGridLine;
		}
	}
		
	public void centerOnPoint(int x, int y){
		Rectangle rect=this.getVisibleRect();
		rect.setLocation((int)(x-rect.getWidth()/2), (int)(y-rect.getHeight()/2));
		this.scrollRectToVisible(rect);
	}
	
	private void setSelected(Mappable mappable){
		if(mappable==null){
			selected=null;
			selectRect=null;
		} else if(selected==null || !selected.contains(mappable)){
			selected=new ArrayList<Mappable>();
			selected.add(mappable);
			selectRect=new Rectangle(mappable.getX(),mappable.getY(),1,1);
		}
	}
	
	public List<Mappable> getSelected(){
		return selected;
	}
	
	public Rectangle getSelectionRectangle(){
		return selectRect;
	}

	public void mouseClicked(MouseEvent e) {
		Mappable mappable=map.getMapObjectAt(e.getX(), e.getY());
		setSelected(mappable);
		if(mappable!=null && e.getClickCount()>1){
			showOptionDialog(mappable);
		}
	}

	public void mouseEntered(MouseEvent e) {
		eventProc.mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		eventProc.mouseExited(e);
	}

	public void mousePressed(MouseEvent e) {
		Mappable mappable=map.getMapObjectAt(e.getX(), e.getY());
		setSelected(mappable);
		
		boolean ctrlDown=(e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) !=0;
		boolean altDown=(e.getModifiersEx() & InputEvent.ALT_DOWN_MASK) !=0;
		
		if(mappable!=null && SwingUtilities.isRightMouseButton(e)){
			popupMenu.show(this,e.getX(),e.getY());
		} else if(selected!=null && ctrlDown){
			eventProc=eventProcessors.get("rotate");
		} else if(selected!=null && altDown){
			eventProc=eventProcessors.get("face");
		} else if(selected!=null){
			eventProc=eventProcessors.get("move");
		} else if(SwingUtilities.isRightMouseButton(e)){
			eventProc=eventProcessors.get("measure");
		} else{
			eventProc=eventProcessors.get("select");
		}
		
		eventProc.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		eventProc.mouseReleased(e);
	}

	public void mouseDragged(MouseEvent e) {
		eventProc.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {
		eventProc.mouseMoved(e);
	}

	public void addUpdateListener(UpdateListener listener) {
		this.listeners.add(listener);
	}

	public void removeUpdateListener(UpdateListener listener) {
		this.listeners.remove(listener);
	}
	
	private void notifyUpdateListeners(Object updated){
		for (UpdateListener listener : listeners) {
			listener.onUpdate(updated);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd=e.getActionCommand();
		if(MapObjectPopupMenu.TO_BACK.equals(cmd)){
			for (Mappable mappable : selected) {
				map.sendMapObjectToBack(mappable, MapPanel.class);
			}
		} else if(MapObjectPopupMenu.TO_FRONT.equals(cmd)){
			for (Mappable mappable : selected) {
				map.sendMapObjectToFront(mappable, MapPanel.class);			
			}
		} else {
			this.eventProc=(MapEventProcessor)eventProcessors.get(cmd);
		}
	}

	@Override
	public void onNewMap(MapEvent<Map> event) {
		init(event.getUpdatedObject());
	}

	@Override
	public void onMapObjectAdd(MapEvent<Mappable> event) {
		repaint();
	}

	@Override
	public void onMapObjectChange(MapEvent<Mappable> event) {
		repaint();
	}

	@Override
	public void onMapObjectRemove(MapEvent<Mappable> event) {
		repaint();
	}

	public void deleteSelected() {
		if(selected!=null){
			for (Mappable mappable : selected) {
				map.removeMapObject(mappable, MapPanel.class);
			}
		}
	}

	private void showOptionDialog(Mappable mappable){
		optionDialog.setMapObject(mappable);
		optionDialog.pack();
		optionDialog.setVisible(true);
	}

	public void selectObjectsInArea(int x, int y, int w, int h) {
		selectRect=new Rectangle(x,y,w,h);
		List<Mappable> mapObjects=map.getAllMapObjects();
		selected=new ArrayList<Mappable>();
		for (Mappable mappable : mapObjects) {
			if(selectRect.contains(mappable.getX(),mappable.getY())){
				selected.add(mappable);
				System.out.println(mappable);
			}
		}
	}

	public void shouldShowGrid(boolean shouldShowGrid) {
		this.shouldShowGrid = shouldShowGrid;
		repaint();
	}

}
