package org.cresse.rpg.client.map;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.cresse.rpg.client.map.model.Map;
import org.cresse.rpg.client.map.util.Globals;
import org.cresse.rpg.client.map.util.ImageUtils;

public class NewMapDialog extends JDialog implements PropertyChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	private Map map = null;
	private JFormattedTextField pixelsPerFoot;
	private JFormattedTextField feetPerGrid;
	private JFormattedTextField width;
	private JFormattedTextField height;
	private ColorPickerButton colorButton;
	private FileChooserButton fileChooserButton;
	
	public NewMapDialog(JFrame parent) {
		super(parent, "New Map...", true);
		Container panel = this.getContentPane();
		panel.setLayout(new GridLayout(0,2));
		
		panel.add(new JLabel("Background:"));
		fileChooserButton = new FileChooserButton(Globals.getWorkingDir(),new ImageFileFilter());
		fileChooserButton.addPropertyChangeListener(FileChooserButton.FILE_CHANGED_PROP,this);
		panel.add(fileChooserButton);
		
		panel.add(new JLabel("Color:"));
		colorButton = new ColorPickerButton();
		colorButton.setColor(Color.BLACK);
		panel.add(colorButton);
		
		panel.add(new JLabel("Width (ft):"));
		width = new JFormattedTextField(NumberFormat.getIntegerInstance());
		width.setValue(125);
		panel.add(width);
		
		panel.add(new JLabel("Height (ft):"));
		height = new JFormattedTextField(NumberFormat.getIntegerInstance());
		height.setValue(75);
		panel.add(height);
				
		panel.add(new JLabel("Pixels/Foot:"));
		pixelsPerFoot = new JFormattedTextField(NumberFormat.getIntegerInstance());
		pixelsPerFoot.setValue(10);
		panel.add(pixelsPerFoot);
		
		panel.add(new JLabel("Feet/Grid:"));
		feetPerGrid = new JFormattedTextField(NumberFormat.getIntegerInstance());
		feetPerGrid.setValue(5);
		panel.add(feetPerGrid);
		
		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				createMap();
				NewMapDialog.this.setVisible(false);
			}

		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				clearMap();
				NewMapDialog.this.setVisible(false);
			}
		});
		panel.add(createButton);
		panel.add(cancelButton);
				
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	private void clearMap() {
		this.map = null;
	}

	private void createMap() {
		this.map = new Map(getBgImage(), getBgColor(), getWidthInPixels(), getHeightInPixels(), getPixelsPerFoot(), getFeetPerGridLine());
	}

	private String getBgImage() {
		File f = this.fileChooserButton.getFile();
		return f==null?null:f.getAbsolutePath();
	}

	private Color getBgColor() {
		return this.colorButton.getColor();
	}

	public Map getMap() {
		return this.map;
	}
	
	private int getWidthInPixels() {
		int width = getWidthInFeet();
		int ppf = getPixelsPerFoot();
		return width*ppf;
	}

	private int getHeightInPixels() {
		int height = getHeightInFeet();
		int ppf = getPixelsPerFoot();
		return height*ppf;
	}
	
	private int getWidthInFeet() {
		return convertToInt(this.width.getValue());
	}

	private int getHeightInFeet() {
		return convertToInt(this.height.getValue());
	}
	
	private int getPixelsPerFoot() {
		return convertToInt(this.pixelsPerFoot.getValue());
	}

	private int getFeetPerGridLine() {
		return convertToInt(this.feetPerGrid.getValue());
	}
	
	private int convertToInt(Object obj) {
		Number number = (Number)obj;
		return number.intValue();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		File file = this.fileChooserButton.getFile();
		if(file!=null) {
			BufferedImage image = ImageUtils.loadImage(file.getAbsolutePath());
			this.width.setValue(image.getWidth()/getPixelsPerFoot());
			this.height.setValue(image.getHeight()/getPixelsPerFoot());
		}
	}

}
