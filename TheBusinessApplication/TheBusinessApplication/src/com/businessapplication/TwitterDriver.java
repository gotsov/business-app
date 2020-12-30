package com.businessapplication;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import twitter4j.*;

public class TwitterDriver {
	
	private static Twitter twitter = TwitterFactory.getSingleton();
	
	public static void tweetOut(String message, String picToPost) throws IOException, TwitterException
	{		
		StatusUpdate status = new StatusUpdate(message);
		
		status.setMedia(new File("D:\\businessAppPhotos\\" + picToPost + ".jpg"));// BY SPECIFYING FILE PATH 
		
		Status updateStatus = twitter.updateStatus(status);
		
		System.out.println("Successful: " + updateStatus.getText());
	}
		
}
