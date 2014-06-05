package com.wanda.mrb.intf.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.apache.catalina.util.Base64;

public class KeyGenerator {
	private byte[] privateKey;
	private byte[] publicKey;

	public void generate() {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			random.setSeed("".getBytes());
			keygen.initialize(1024, random);
			KeyPair pair = keygen.genKeyPair();
			
			PublicKey publicK = pair.getPublic();
			PrivateKey privateK = pair.getPrivate();
			
			publicKey = Base64.encode(publicK.getEncoded());
			privateKey = Base64.encode(privateK.getEncoded());
			
			System.out.println("poulicK.length="+publicK.getEncoded().length + "|publicKey.length="+publicKey.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("PublicKey="+new String(publicKey));
		System.out.println("PrivateKey="+new String(privateKey));
	}
	
	public byte[] getPrivateKey() {
		return privateKey;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}
	
	public static void main(String[] a){
		KeyGenerator k = new KeyGenerator();
		k.generate();
	}
}
