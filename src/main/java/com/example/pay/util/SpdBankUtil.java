package com.example.pay.util;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * ClassName: SpdBankUtil
 * Description:
 * date: 2019/7/23 14:50
 *
 * @author 陈杰
 * @version 1.0
 * @since JDK 1.8
 * .........┌─┐              ┌─┐
 * ...┌──┘  ┴───────┘  ┴──┐
 * ...│                                  │
 * ...│          ───                  │
 * ...│     ─┬┘       └┬─          │
 * ...│                                  │
 * ...│           ─┴─                 │
 * ...│                                  │
 * ...└───┐                  ┌───┘
 * ...........│                  │
 * ...........│                  │
 * ...........│                  │
 * ...........│                  └──────────────┐
 * ...........│                                                │
 * ...........│                                                ├─┐
 * ...........│                                                ┌─┘
 * ...........│                                                │
 * ...........└─┐    ┐    ┌───────┬──┐    ┌──┘
 * ...............│  ─┤  ─┤              │  ─┤  ─┤
 * ...............└──┴──┘              └──┴──┘
 * --------------------------------神兽保佑--------------------------------
 * --------------------------------代码无BUG!------------------------------
 */
public class SpdBankUtil {


    public static String digest(String algorithm, String content,
                                 String charSet) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes(charSet));
            byte[] digestBytes = messageDigest.digest();
            return DatatypeConverter.printBase64Binary(digestBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String cipherAlgorithm, byte[] keyBytes,
                                  String keyAlgorithm, String content, String charSet) {
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, keyAlgorithm);
            byte[] iv = UUID.randomUUID().toString().substring(0, 16)
                    .getBytes(charSet);
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encyptedBytes = cipher.doFinal(content.getBytes(charSet));
            byte[] contentBytes = new byte[encyptedBytes.length + 16];
            System.arraycopy(iv, 0, contentBytes, 0, 16);
            System.arraycopy(encyptedBytes, 0, contentBytes, 16,
                    encyptedBytes.length);
            return DatatypeConverter.printBase64Binary(contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String cipherAlgorithm, byte[] keyBytes,
                                  String keyAlgorithm, String signature, String charSet) {
        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, keyAlgorithm);
            byte[] iv = UUID.randomUUID().toString().substring(0, 16)
                    .getBytes(charSet);
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            byte[] signatureBytes = DatatypeConverter
                    .parseBase64Binary(signature);
            byte[] decyptedBytes = cipher.doFinal(signatureBytes);
            byte[] contentBytes = new byte[decyptedBytes.length - 16];
            System.arraycopy(decyptedBytes, 16, contentBytes, 0,
                    decyptedBytes.length - 16);
            return new String(contentBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 签名
     *
     * @param content  请求报文体
     * @param secret   密钥
     * @return
     */
    public static String sign(String content, String secret) {
        String charSet = "UTF-8";
        String contentDigest = digest("SHA-1", content, charSet);
        String keyDigest = digest("SHA-256", secret, charSet);
        byte[] keyDigestBytes = DatatypeConverter.parseBase64Binary(keyDigest);
        byte[] keyBytes = new byte[16];
        System.arraycopy(keyDigestBytes, 0, keyBytes, 0, 16);
        String cipherAlgorithm = "AES/CBC/PKCS5Padding";
        String keyAlgorithm = "AES";
        return encrypt(cipherAlgorithm, keyBytes, keyAlgorithm, contentDigest,
                charSet);
    }

    /**
     * 验签
     *
     * @param content  请求报文体
     * @param secret  密钥
     * @param signature 签名
     * @return
     */
    public static boolean validateSign(String content, String secret,
                                        String signature) {
        String charSet = "UTF-8";
        String contentDigest = digest("SHA-1", content, charSet);
        String keyDigest = digest("SHA-256", secret, charSet);
        byte[] keyDigestBytes = DatatypeConverter.parseBase64Binary(keyDigest);
        byte[] keyBytes = new byte[16];
        System.arraycopy(keyDigestBytes, 0, keyBytes, 0, 16);
        String cipherAlgorithm = "AES/CBC/PKCS5Padding";
        String keyAlgorithm = "AES";
        String decryptStr = decrypt(cipherAlgorithm, keyBytes, keyAlgorithm,
                signature, charSet);
        return contentDigest.equals(decryptStr);
    }


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


}
