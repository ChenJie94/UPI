package com.example.pay.util;
import java.security.MessageDigest;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CryptoSampleImage {
	
	private static byte[] digestBytes(String algorithm, byte[] bytes) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(bytes);
			return messageDigest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String encrypt(String cipherAlgorithm, byte[] keyBytes, String keyAlgorithm, String contentDigest) {
		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, keyAlgorithm);
			byte[] iv = Long.toHexString(UUID.randomUUID().getLeastSignificantBits()).getBytes();
			IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
			byte[] encyptedBytes = cipher.doFinal(contentDigest.getBytes());
			byte[] resultBytes = new byte[encyptedBytes.length + 16];
			System.arraycopy(iv, 0, resultBytes, 0, 16);
			System.arraycopy(encyptedBytes, 0, resultBytes, 16, encyptedBytes.length);
			return DatatypeConverter.printBase64Binary(resultBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String signBytes(byte[] bytes, byte[] secretBytes) {
		String contentDigest = DatatypeConverter.printBase64Binary(digestBytes("SHA-1", bytes));
		byte[] keyDigestBytes = digestBytes("SHA-256", secretBytes);
		byte[] keyBytes = new byte[16];
		System.arraycopy(keyDigestBytes, 0, keyBytes, 0, 16);
		String cipherAlgorithm = "AES/CBC/PKCS5Padding";
		String keyAlgorithm = "AES";
		return encrypt(cipherAlgorithm, keyBytes, keyAlgorithm, contentDigest);
	}


	public static void main(String[] args) {
		
		String content = "{\"AccountId\":\"iaYSSMGHz2lqYWUa83ltTmlPSQIPNBXRrtaiaictZfafibc\"}";
		String secret = "oK3qH3mE8aY8bH1aQ0dP0dF1eS1cA2gI1eN6kU0vH1sB0dW1hR";
		System.out.println(signBytes(content.getBytes(), secret.getBytes()));
	}
}
