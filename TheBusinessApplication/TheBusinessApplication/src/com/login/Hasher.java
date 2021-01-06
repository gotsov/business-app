package com.login;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hasher {
	
	private static String algorithm = "PBKDF2WithHmacSHA1";

//	public static byte[] createHash(String password)  {
//		SecureRandom random = new SecureRandom();
//		byte[] salt = new byte[16];
//		random.nextBytes(salt);
//
//		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt , 65536, 128); 
//
//		try {
//			
//			SecretKeyFactory factory  = SecretKeyFactory.getInstance(algorithm);
//			byte[] hash = factory .generateSecret(spec).getEncoded();
//			return hash;
//			
//		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return null;
//	}
	
	public static int createHash(String password)  {
	
		return password.hashCode();
	}
	
//	public static String getSaltedHash(String password, byte[] salt) {
//		try {
//			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt , 20000, 128); 
//			SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
//			byte[] hash = factory.generateSecret(spec).getEncoded();
//			return Base64.getEncoder().encodeToString(hash);
//		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		return null;
//	}
	
	protected static String createHashedPass(String password) {
		byte[] salt =  createSalt();
		return getSaltedHash(password,  salt);
	}
	
	 public static String getSaltedHash(String password, byte[] salt) {
		    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 20000, 128);
		    try {
		      SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
		      byte[] hash = skf.generateSecret(spec).getEncoded();
		      return Base64.getEncoder().encodeToString(hash);
		    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
		    	e.printStackTrace();
		    }
			return null;
		  }
	 
	 public static String getSaltedHash(String password, String salt) {
			try {
				byte[] saltDecoded = Base64.getDecoder().decode(salt);
				KeySpec spec = new PBEKeySpec(password.toCharArray(), saltDecoded , 20000, 128); 
				SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
				
				byte[] hash = factory.generateSecret(spec).getEncoded();
				return Base64.getEncoder().encodeToString(hash);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
		    	e.printStackTrace();
		    }
			
			
			return null;
		}
	 
	public static byte[] createSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];		
		random.nextBytes(salt);
		return salt;
	}
	
	
	
}
