package com.wanda.mrb.intf.utils;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;


public class PwdUtil {
    //非对称密钥算法  
    public static final String KEY_ALGORITHM="RSA";  

    //Base64格式编码的公钥(客户端使用)
    private static String publicKeyBase64Str = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJrE2X6P6Q9r2d4ukr0vtvTWmMBlJfJ8Of3THnI8PG2JRpnqM50RApJ5C6+ny2lA7JdjvaKnlTrWRpf2+dyP8bUCAwEAAQ==";
    //Base64格式的私钥(服务端使用)
    private static String privateKeyBase64Str = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAmsTZfo/pD2vZ3i6SvS+29NaYwGUl8nw5/dMecjw8bYlGmeoznRECknkLr6fLaUDsl2O9oqeVOtZGl/b53I/xtQIDAQABAkEAhfHdxeIos1cE/HxUNPWpouZi1wfzOpR24A0qKXWmWqj7VHWM8BhR0oAqH1jdq4Tegt7yxcJD3F1MCzUXKRJJVQIhAOOZfN3ewHoPSfui8gJFSYmK3KKdbyf/LXYhbIAO88r/AiEArhTWa6PQleVNauPfSFVGD6wwo0+LMAOOYaxEzdfph0sCIQDEQA75SR9yoEhM4H4JEzGLld8VoOa9+Dx9BekGqt3whQIgP2K4/HKmhZ37xXr5l0Xtf9Fr2FsjTDVPR8NJPkZq8ucCIB+eZjaOtBGS9E6tOnGFWrHkA2vtrDUc6cu4j/X/XzGR";
	
	/**
	 * 私钥解密
     * 输入：Base64格式的密文字符串
     * 输出：明文字符串
     */
	public static String decryptByPrivateKey(String data) throws Exception{
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBase64Str));
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		//return new String(cipher.doFinal(Base64.decodeBase64(data)));
		return new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(data)));
	}

	/**
	 * 公钥加密
     * 输入：明文字符串
     * 输出：Base64格式的秘文字符串
     */
	public static String encryptByPublicKey(String data) throws Exception{
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBase64Str));
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

		// 数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		//return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
		return new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
	}

	/**
	 * 私钥加密
     * 输入：明文字符串
     * 输出：Base64格式的密文字符串
     */
    public static String encryptByPrivateKey(String data) throws Exception{  
          
        //取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBase64Str));  
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);  
        //生成私钥  
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);  
        //数据加密  
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        //return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));  
        return new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
    }  

    /** 
     * 公钥解密 
     * 输入：Base64格式的密文字符串
     * 输出：明文字符串 
     */  
    public static String decryptByPublicKey(String data) throws Exception{  
          
        //实例化密钥工厂  
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);  
        //初始化公钥  
        //密钥材料转换  
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBase64Str));  
        //产生公钥  
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);  
        //数据解密  
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, pubKey);  
        //return new String(cipher.doFinal(Base64.decodeBase64(data)));  
        return new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
    }  
    
    /**
     * MD5加密, 目前大都会有自己实现过程，用于用户密码，由于不可转，所以一般生成密文比较
     * 返回：Base64格式的秘文字符串
     */
//    public static String md5Encrypt(String obj) {
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//                md5.update(obj.getBytes());
//System.out.println("md5.digest.length="+md5.digest().length+"|"+md5.digest());                
//            return Base64.encodeBase64String(md5.digest());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
 
	public static void main(String[] argv) {
//		System.err.println("privatekey length="+Base64.decodeBase64(privateKeyBase64Str).length);
//		//"乙方向甲方发送数据RSA算法97487408534985038"对应的Base64密文 
//		System.err.println("公钥加密后Base64格式:ECyUhKu86V+61dGW8vTuaFGz+f3IFRB4TJC/fpq3CbojC7fDY2OqK0M3GbCwlXdlkfJWlpeJmb/51lF66197Gw==");
//		try {
//			System.err.println("明文:"+decryptByPrivateKey("ECyUhKu86V+61dGW8vTuaFGz+f3IFRB4TJC/fpq3CbojC7fDY2OqK0M3GbCwlXdlkfJWlpeJmb/51lF66197Gw=="));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		String md5EncStr = md5Encrypt("乙方向甲方发送数据RSA算法97487408534985038");
//		System.err.println("md5密文:"+md5EncStr.length()+"|"+md5EncStr+"|");
		try {
			System.err.println("pri:"+encryptByPublicKey("12345678901234567890123456"));
			
			System.err.println("md5:"+md5Encrypt("111111"));
			System.err.println("V:"+VoucherNumberEncoder.md5Encrypt("12010500000002150001000080"));
			
			System.err.println("dec by pub:"+encryptByPublicKey("37035901ftp37035901"));
			System.err.println("dec by pri:"+decryptByPrivateKey("hxCJbTn/6R2tD7YWMOvl8qBW1kUpV7T7IUBRPjp17+mi1V8YvWDDpzpIv0kk3g5UKTqhsyyb9el4LQV/D1D6TA=="));
			System.err.println("md5:"+md5Encrypt("37035901ftp37035901"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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

}
