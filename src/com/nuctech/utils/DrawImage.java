package com.nuctech.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class DrawImage {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public static  void draw(Image image) throws IOException{
		BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D)bi.getGraphics();
		graphics.drawImage(image,0,0,null,null);
		graphics.setColor(Color.RED);
	    //graphics.drawRect(680, 380, 500, 300);
	    //graphics.draw3DRect(680, 380, 500, 300, false);
		graphics.setStroke(new BasicStroke(5));
		graphics.drawRect(880, 380, 500, 300);
//        FileOutputStream fos = new FileOutputStream(new File("D:/bak/86574MB01201401080026/test.jpg"));
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
//        encoder.encode(bi);
  
       // bi.flush();
        ImageIO.write(bi, "JPEG", new File("D:/bak/86574MB01201401080026/test.jpg"));
//        fos.flush();
//        fos.close();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new Date().toLocaleString());
		Image a = ImageIO.read(new File("D:/bak/86574MB01201401080026/86574MB01201401080026.jpg"));
		DrawImage.draw(a);
		System.out.println(new Date().toLocaleString());

	}

}
