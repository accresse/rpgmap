package org.cresse.rpg.client.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileChooserButton extends JButton implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	public static final String FILE_CHANGED_PROP = "fileChanged";
	
	private JFileChooser fileChooser;
	private File file;
	

	public FileChooserButton(File startDir, FileFilter filter) {
		super("...");
		this.addActionListener(this);
		fileChooser = new JFileChooser(startDir);
		fileChooser.setFileFilter(filter);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int returnVal = fileChooser.showOpenDialog(this);
		File oldVal = this.file;
		if(returnVal==JFileChooser.APPROVE_OPTION) {
			this.setText("Yes");
			this.file = fileChooser.getSelectedFile();
			this.setToolTipText(file.getAbsolutePath());
		} else {
			this.setText("No");
			this.setToolTipText(null);
			this.file = null;
		}
		firePropertyChange(FILE_CHANGED_PROP, oldVal, this.file);
	}

	public File getFile() {
		return this.file;
	}
}
