package com.kasite.client.crawler.modules.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;

/**
 * RSA加密辅助类,采用Base64编码
 *
 * @created Air  2015/6/02.
 */
public class RSA {
    public static final String PUBLIC_KEY = "public";
    public static final String PRIVATE_KEY = "private";
    public static final String KEY_ALGORITHM = "RSA";
//    private static Logger logger = LoggerFactory.getLogger(RSA.class);

    /**
     * 生成公钥和私钥 
     *
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<String, Key> generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        HashMap<String, Key> map = new HashMap<>();
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, privateKey);

        return map;
    }

    public static String encodeKey(Key key) {
//        return HexEncode.toHexString(key.getEncoded());
        return Base64.encode(key.getEncoded());
    }
    public static Key genPrivateKey(String key) {
        byte[] bytes = Base64.decode(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        	ex.printStackTrace();
        }

        return null;
    }

    public static Key genPublicKey(String key) {
        byte[] bytes = Base64.decode(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePublic(x509KeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        	ex.printStackTrace();
        }

        return null;
    }


    /**
     * @param data 明文
     * @param key  密钥
     * @return HexString密文
     * @throws Exception
     */
    public static String encrypt(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

//        return HexEncode.toHexString(cipher.doFinal(data.getBytes()));
        return Base64.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * @param data HexString密文
     * @param key  密钥
     * @return 明文
     * @throws Exception
     */
    public static String decrypt(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

//        return new String(cipher.doFinal(HexEncode.toBytes(data)));
        return new String(cipher.doFinal(Base64.decode(data)));
    }

    /**
     * 私钥加密
     * @param data       明文
     * @param privateKey 私钥
     * @return 密文
     * @throws Exception
     */
    public static String encryptByPriKey(String data, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, genPrivateKey(privateKey));
        return Base64.encode(cipher.doFinal(data.getBytes()));
    }


}
