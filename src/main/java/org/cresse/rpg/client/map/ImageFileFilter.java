package org.cresse.rpg.client.map;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		String path = f.getAbsolutePath().toLowerCase();
		return f.isDirectory() || (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".bmp"));
	}

	@Override
	public String getDescription() {
		return "Images";
	}

}
