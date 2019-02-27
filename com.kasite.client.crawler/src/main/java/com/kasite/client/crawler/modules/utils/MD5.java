package com.kasite.client.crawler.modules.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @created  Air 2015/6/2.
 */
public class MD5 {
    static public String hash(String str) throws Exception {
        MessageDigest messageDigest = null;
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(str.getBytes("UTF-8"));

        return HexEncode.toHexString(messageDigest.digest());
    }

//    // 可逆的加密算法
//    public static String encrypt(String inStr) {
//        char[] a = inStr.toCharArray();
//        for (int i = 0; i < a.length; i++) {
//            a[i] = (char) (a[i] ^ 't');
//        }
//        String encryptStr = new String(a);
//        return encryptStr;
//    }
//    // 加密后解密
//    public static String decrypt(String inStr) {
//        char[] a = inStr.toCharArray();
//        for (int i = 0; i < a.length; i++) {
//            a[i] = (char) (a[i] ^ 't');
//        }
//        String decryptStr = new String(a);
//        return decryptStr;
//    }

    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        MappedByteBuffer byteBuffer =null;
        try {
            byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
//            BigInteger bi = new BigInteger(1, md5.digest());
//            value = bi.toString(16);
            value= HexEncode.toHexString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                    clean(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
    
    /**
	 * sign 签名生成  （   md5(secret + params拼接字符串 + secret)   ）
	 *
	 * @return
	 */
	public static String signParam(TreeMap<String, String> paramMap,String secret) {
	    Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
	    StringBuilder builder = new StringBuilder();
	    builder.append(secret);
	    while (iterator.hasNext()) {
	        Map.Entry<String, String> next = iterator.next();
	        String key = next.getKey();
	        String value = next.getValue();
	        builder.append(key);
	        builder.append(value);
	    }
	    builder.append(secret);
	    try {
	        return hash(builder.toString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}
}
