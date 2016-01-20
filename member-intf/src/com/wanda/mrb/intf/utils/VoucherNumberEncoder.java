package com.wanda.mrb.intf.utils;

import java.security.MessageDigest;

public class VoucherNumberEncoder {
  	public static String md5Encrypt(String str) {
		byte[] bytes = null;
		try {
			bytes = str.getBytes("UTF-8");
		} catch (Exception e) {
			bytes = str.getBytes();
		}

		byte messageDigest[] = null;
		synchronized (m_md5) {
			m_md5.update(bytes);
			messageDigest = m_md5.digest();
		}

		return new sun.misc.BASE64Encoder().encode(messageDigest);
	}

  	private static MessageDigest m_md5;
	static {
		try {
			m_md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
		}
	}
	
	public static void main(String args[]){
		System.out.println(VoucherNumberEncoder.md5Encrypt("3653426611101307285"));
	}
}
