package com.twitterconnection;

import java.io.File;
import java.io.IOException;

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
