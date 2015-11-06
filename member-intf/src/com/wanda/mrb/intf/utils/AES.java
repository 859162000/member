package com.wanda.mrb.intf.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * AES加解密工具类
 */
public class AES {
	private static final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
	private static final int HASH_ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	private static byte[] salt = { 1, 3, 9, 6, 9, 4, 4, 4, 0, 2, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };
	private static final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";
	private static IvParameterSpec ivSpec = new IvParameterSpec(new byte[] { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91 });
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			String s = "lhdV+wj177YyjirsxeikaNestM/YafgOxrpiGbMVxzvefarFNbQfGHXauqmTIJk+upexqehjv8xK+mlx2SbnDg==" + System.currentTimeMillis();
			String key = "wandayuanxian100008";
			String t = "";
			try {
				t = AES.encrypt(s.getBytes("UTF-8"), key);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				System.out.println(t);
				String de = AES.decrypt(t, key);
				System.out.println(de);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static SecretKeyFactory createSecretKeyFactory() {
		try {
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(AES.KEY_GENERATION_ALG);
			return keyfactory;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("no key factory support for PBEWITHSHAANDTWOFISH-CBC");
		}
		return null;

	}

	public static SecretKeySpec aesKeyConvert(String key) {
		try {
			PBEKeySpec myKeyspec = new PBEKeySpec(key.toCharArray(), AES.salt, AES.HASH_ITERATIONS, AES.KEY_LENGTH);
			SecretKeyFactory keyfactory = createSecretKeyFactory();
			SecretKey sk = keyfactory.generateSecret(myKeyspec);
			byte[] skAsByteArray = sk.getEncoded();
			SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, "AES");
			return skforAES;
		} catch (InvalidKeySpecException ikse) {
			System.out.println("invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
		}
		return null;
	}

	public static String encrypt(byte[] plaintext, String password) {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		SecretKeySpec skforAES = aesKeyConvert(password);
		byte[] ciphertext = encrypt(AES.CIPHERMODEPADDING, skforAES, AES.ivSpec, plaintext);
		try {
			System.out.println("加密原串为：" + new String(ciphertext, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String base64_ciphertext = Base64Encoder.encode(ciphertext);
		return base64_ciphertext;
	}

	public static String decrypt(String ciphertext_base64, String password) {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
		SecretKeySpec skforAES = aesKeyConvert(password);
		String decrypted = new String(decrypt(AES.CIPHERMODEPADDING, skforAES, AES.ivSpec, s));
		return decrypted;
	}

	public static String decrypt(byte[] data, String password) {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		SecretKeySpec skforAES = aesKeyConvert(password);
		String decrypted = new String(decrypt(AES.CIPHERMODEPADDING, skforAES, AES.ivSpec, data));
		return decrypted;
	}

	/**
	 * 加密
	 * 
	 * @param cmp
	 *            填充方式
	 * @param sk
	 *            密钥
	 * @param IV
	 *            向量
	 * @param msg
	 *            需要加密的内容
	 * @return 返回加密结果
	 */
	public static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.ENCRYPT_MODE, sk, IV);
			return c.doFinal(msg);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchPaddingException e) {
			System.out.println(e.getMessage());
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
		} catch (BadPaddingException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param cmp
	 *            填充函数
	 * @param sk
	 *            密钥
	 * @param IV
	 *            向量
	 * @param ciphertext
	 *            需要解密内容
	 * @return 返回解密结果
	 */
	public static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.DECRYPT_MODE, sk, IV);
			return c.doFinal(ciphertext);
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println(nsae.getMessage());
		} catch (NoSuchPaddingException nspe) {
			System.out.println(nspe.getMessage());
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
		} catch (BadPaddingException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
