package org.cresse.rpg.client.map;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorPickerButton extends JButton implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel colorPanel;

	public ColorPickerButton() {
		colorPanel = new JPanel();
		this.add(colorPanel);
		this.addActionListener(this);
	}
	
	public void setColor(Color color) {
		this.colorPanel.setBackground(color);
	}
	
	public Color getColor() {
		return this.colorPanel.getBackground();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Color color = JColorChooser.showDialog(this, "Color chooser", getColor());
		if(color!=null) {
			setColor(color);
		}
	}

}
