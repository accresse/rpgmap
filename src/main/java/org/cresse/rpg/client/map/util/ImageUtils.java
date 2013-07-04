package org.cresse.rpg.client.map.util;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class ImageUtils {

	public static BufferedImage loadImage(String img) {
		ImageIcon ii=new ImageIcon(img);
		if(ii.getIconWidth()>0) {
			return ImageUtils.bufferImage(ii.getImage(), ii.getIconWidth(), ii.getIconHeight());
		} else {
			return null;
		}
	}

	public static BufferedImage bufferImage(Image img, int w, int h){
		BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d=bi.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		return bi;
	}

	public static BufferedImage getBlankImage(int w, int h, Color color){
		BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d=bi.createGraphics();
		g2d.setColor(color);
		g2d.fillRect(0,0,w,h);
		return bi;
	}

	public static BufferedImage loadImageWithBgColor(String img, int w, int h, Color bgColor){
		BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d=bi.createGraphics();
		g2d.setColor(bgColor);
		g2d.fillRect(0,0,w,h);
		if(img!=null) {
			g2d.drawImage(loadImage(img),0,0,null);
		}
		return bi;
	}

	public static BufferedImage copyImage(BufferedImage bi){
		BufferedImage copy=new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		copy.getGraphics().drawImage(bi,0,0,null);
		return copy;
	}
	
	public static Color getColorGradient(double percent){
		float r=0f;
		float g=0f;
		float b=0f;

		if(percent<0.5){
			r=1f;
			g=(float)(2.0*percent);
		} else{
			g=1f;
			r=(float)(1.0-2.0*(percent-.5));
		}
		
		return new Color(r,g,b);
	}
}
