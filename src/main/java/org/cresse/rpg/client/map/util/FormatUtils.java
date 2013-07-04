package org.cresse.rpg.client.map.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatUtils {
	
	private static NumberFormat format=DecimalFormat.getNumberInstance();
	
	public static String formatDist(double feet){
		StringBuffer buff=new StringBuffer();
		if(feet>5280){
			double miles=feet/5280.0;
			buff.append(format.format(miles)).append(" mi.");
		} 
		else if(feet>0){
			buff.append(format.format(feet)).append(" ft.");
		} 
		return buff.toString();
	}
	
	public static String formatDegrees(double radians){
		double degrees=radians/Math.PI*180.0;
		return format.format(degrees)+"°";
	}

	public static String formatDecimal(double d) {
		return format.format(d);
	}

}
