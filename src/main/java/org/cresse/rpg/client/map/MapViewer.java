package org.cresse.rpg.client.map;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;

import org.cresse.rpg.client.map.event.MapEvent;
import org.cresse.rpg.client.map.event.MapEvent.EventType;
import org.cresse.rpg.client.map.event.MapListener;
import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.net.MapClient;
import org.cresse.rpg.client.map.net.MapServer;
import org.cresse.rpg.client.map.obj.Mappable;
import org.cresse.rpg.client.map.procs.GroupRotateEventProcessor;
import org.cresse.rpg.client.map.procs.MapEventProcessor;
import org.cresse.rpg.client.map.procs.MeasureEventProcessor;
import org.cresse.rpg.client.map.procs.MoveEventProcessor;
import org.cresse.rpg.client.map.procs.RotateEventProcessor;
import org.cresse.rpg.client.map.procs.SelectEventProcessor;
import org.cresse.rpg.client.map.util.Globals;

public class MapViewer extends JFrame implements ActionListener, MapListener{
	
	private static final long serialVersionUID = 1L;
	
	private List<MapListener> listeners;
	private Map map;
	private MapSaver saver;
	private JFileChooser chooser;
	private File mapFile=null;
	private boolean hasUnsavedChanges=false;
	private FileFilter mapFilter;
	private NewMapDialog newMapDialog;
	private ShareMapDialog shareMapDialog;
	private ConnectMapDialog connectMapDialog;
	private MapServer server;
	private MapClient client;

	private MapPanel mapPanel;
	
	public MapViewer(){
		super("Map Viewer");
		
		this.listeners=new ArrayList<MapListener>();
		this.saver=new PropertiesMapSaver();
		this.server = new MapServer(this);
		this.client = new MapClient(this);
		this.addMapListener(server);
		this.addMapListener(client);
		
		JLabel statusBar = new JLabel("Status");
		MapMenuBar menuBar=new MapMenuBar();
		menuBar.addActionListener(this);
		
		mapPanel = new MapPanel(this, statusBar);
		java.util.Map<String, MapEventProcessor> eventProcessors=new HashMap<String, MapEventProcessor>();
		eventProcessors.put("move", new MoveEventProcessor(mapPanel,statusBar));
		eventProcessors.put("measure", new MeasureEventProcessor(mapPanel,statusBar));
		eventProcessors.put("rotate", new GroupRotateEventProcessor(mapPanel,statusBar));
		eventProcessors.put("face", new RotateEventProcessor(mapPanel,statusBar));
		eventProcessors.put("select", new SelectEventProcessor(mapPanel,statusBar));
		mapPanel.setEventProcessors(eventProcessors);
		this.addMapListener(mapPanel);
		
//		MapToolBar mapToolBar = new MapToolBar();
//		mapToolBar.addActionListener(mapPanel);
		File workingDir = getWorkingDir();
		Globals.setWorkingDir(workingDir);
		IMapObjectSource objSource=new HardCodedMapObjectSource(workingDir);
		MapObjectSelectionPanel mapObjSelectionPanel=new MapObjectSelectionPanel(objSource, mapPanel);
		this.addMapListener(mapObjSelectionPanel);
		
		MapPreviewPanel mapPreviewPanel=new MapPreviewPanel(mapPanel);
		
		MapMenuPanel mapMenuPanel = new MapMenuPanel(mapPreviewPanel,mapObjSelectionPanel);
		
		this.setJMenuBar(menuBar);

		JScrollPane scroll=new JScrollPane(mapPanel);
		
		JSplitPane leftRight=new JSplitPane();
		leftRight.setDividerSize(0);
		leftRight.setDividerLocation(200);
		leftRight.setLeftComponent(mapMenuPanel);
		leftRight.setRightComponent(scroll);

		JPanel total=new JPanel();
		total.setLayout(new BorderLayout());
		total.add(leftRight,BorderLayout.CENTER);
		//total.add(mapToolBar,BorderLayout.NORTH);
		total.add(statusBar,BorderLayout.SOUTH);
		
		mapFilter = new FileFilter(){
		
			public boolean accept(File f) {
				return f.isDirectory() ||
				f.getName().endsWith("properties") ||
				f.getName().endsWith("map");
			}
			
			public String getDescription() {
				return "Map files";
			}
		};
							
		chooser=new JFileChooser(workingDir);
		chooser.setFileFilter(mapFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		this.newMapDialog = new NewMapDialog(this);
		this.shareMapDialog = new ShareMapDialog(this);
		this.connectMapDialog = new ConnectMapDialog(this);

		this.setContentPane(total);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();

	}
	
	private File getWorkingDir() {
		return new File(System.getProperty("user.home"),"RpgMapViewer");
//		JFileChooser chooser = new JFileChooser();
//		chooser.setDialogTitle("Where are RPG Map Resources?");
//		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//
//		int result = -1;
//		while(result != JFileChooser.APPROVE_OPTION) {
//			result = chooser.showOpenDialog(this);
//		}
//		return chooser.getSelectedFile();
	}
	
	public Map getMap() {
		return map;
	}

	public void addMapListener(MapListener listener) {
		listeners.add(listener);
	}

	public void removeMapViewerListener(MapListener listener) {
		listeners.remove(listener);
	}

	public void loadMap(Map map) {
		if(this.map!=null){
			this.map.removeMapListener(this);
		}
		this.map=map;
		map.addMapListener(this);
		onNewMap(new MapEvent<Map>(EventType.NEW_MAP, map));
		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		String cmd=e.getActionCommand();
		
		if(MapMenuBar.MENU_OPTION_SAVE.equals(cmd) && mapFile==null){
			cmd=MapMenuBar.MENU_OPTION_SAVE_AS;
		}
		
		if(MapMenuBar.MENU_OPTION_EXIT.equals(cmd)){
			int option=JOptionPane.OK_OPTION;
			if(hasUnsavedChanges){
				option=JOptionPane.showConfirmDialog(this, "Changes will be lost.  Exit anyway?","Choose Option...",JOptionPane.OK_CANCEL_OPTION);
			}
			if(option==JOptionPane.OK_OPTION){
				System.exit(0);
			}
		} else if(MapMenuBar.MENU_OPTION_SAVE.equals(cmd)){
			try {
				saver.saveMap(map, mapFile);
				hasUnsavedChanges=false;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(MapMenuBar.MENU_OPTION_SAVE_AS.equals(cmd)){
			try {
				int action=chooser.showSaveDialog(this);
				if(action==JFileChooser.APPROVE_OPTION){
					chooser.setFileFilter(mapFilter);
					mapFile=chooser.getSelectedFile();
					saver.saveMap(map, mapFile);
					hasUnsavedChanges=false;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(MapMenuBar.MENU_OPTION_OPEN.equals(cmd)){
			int option=JOptionPane.OK_OPTION;
			if(hasUnsavedChanges){
				option=JOptionPane.showConfirmDialog(this, "Changes will be lost.  Open new map anyway?","Choose Option...",JOptionPane.OK_CANCEL_OPTION);
			}
			if(option!=JOptionPane.OK_OPTION){
				return;
			}

			Map map;
			try {
				chooser.setFileFilter(mapFilter);
				int action=chooser.showOpenDialog(this);
				if(action==JFileChooser.APPROVE_OPTION){
					mapFile=chooser.getSelectedFile();
					map = saver.loadMap(mapFile);
					this.loadMap(map);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(MapMenuBar.MENU_OPTION_NEW.equals(cmd)){
			int option=JOptionPane.OK_OPTION;
			if(hasUnsavedChanges){
				option=JOptionPane.showConfirmDialog(this, "Changes will be lost.  Create new map anyway?","Choose Option...",JOptionPane.OK_CANCEL_OPTION);
			}
			if(option!=JOptionPane.OK_OPTION){
				return;
			}

			try {
				newMapDialog.setVisible(true);
				Map map = newMapDialog.getMap();
				if(map!=null) {
					this.hasUnsavedChanges=true;
					this.loadMap(map);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(MapMenuBar.MENU_OPTION_GRID.equals(cmd)) {
			JCheckBoxMenuItem checkbox = (JCheckBoxMenuItem) e.getSource();
			this.mapPanel.shouldShowGrid(checkbox.isSelected());
		} else if(MapMenuBar.MENU_OPTION_SHARE.equals(cmd)) {
			try {
				shareMapDialog.setVisible(true);
				ShareAction shareAction = shareMapDialog.getShareAction();
				switch(shareAction) {
					case CONNECT:
						shareMap(shareMapDialog.getPort());
						break;
					case DISCONNECT:
						unshareMap();
						break;
					default:
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} else if(MapMenuBar.MENU_OPTION_CONNECT.equals(cmd)) {
			try {
				connectMapDialog.setVisible(true);
				ShareAction shareAction = connectMapDialog.getShareAction();
				switch(shareAction) {
					case CONNECT:
						connectMap(connectMapDialog.getHost(), connectMapDialog.getPort());
						break;
					case DISCONNECT:
						disconnectMap();
						break;
					default:
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	private void unshareMap() {
		this.server.stop();
	}

	private void shareMap(int port) {
		this.server.setPort(port);
		new Thread(this.server).start();
	}

	private void disconnectMap() {
		this.client.stop();
	}
	
	private void connectMap(String host, int port) {
		this.client.setHost(host);
		this.client.setPort(port);
		new Thread(this.client).start();
	}
	
	@Override
	public void onMapObjectAdd(MapEvent<Mappable> event) {
		hasUnsavedChanges=true;
		for (MapListener listener : listeners) {
			listener.onMapObjectAdd(event);
		}
	}

	@Override
	public void onMapObjectChange(MapEvent<Mappable> event) {
		hasUnsavedChanges=true;
		for (MapListener listener : listeners) {
			listener.onMapObjectChange(event);
		}
	}

	@Override
	public void onMapObjectRemove(MapEvent<Mappable> event) {
		hasUnsavedChanges=true;
		for (MapListener listener : listeners) {
			listener.onMapObjectRemove(event);
		}
	}

	@Override
	public void onNewMap(MapEvent<Map> event) {
		for (MapListener listener : listeners) {
			listener.onNewMap(event);
		}
	}
	
	public static void main(String[] args) {
		MapViewer frame=new MapViewer();
		//frame.setSize(800,600);
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

}
