package com.kasite.client.crawler.modules.utils;

/**
 * @created Created by Air on 2015/6/2.
 */
public class HexEncode {
    static public String toHexString(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte aByte : bytes) {
            if (Integer.toHexString(0xFF & aByte).length() == 1) {
                stringBuffer.append("0").append(Integer.toHexString(0xFF & aByte));
            } else {
                stringBuffer.append(Integer.toHexString(0xFF & aByte));
            }
        }

        return stringBuffer.toString();
    }

    static public byte[] toBytes(String hexString) {
        byte[] bytes;
        bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }
    
    /**
	 * 字符串转换为16进制字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
 
	/**
	 * 16进制字符串转换为字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "gbk");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

}
