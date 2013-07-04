package org.cresse.rpg.client.map.util;

import java.io.File;

public class Globals {

	private static File workingDir = null;
	
	public static void setWorkingDir(File workingDir) {
		Globals.workingDir = workingDir;
	}
	
	public static File getWorkingDir() {
		return workingDir;
	}

}
