package com.kasite.client.zfb.util;

import com.kasite.core.common.config.KasiteConfig;

/**
 * 
 * @author MECHREV
 *
 */
/**
 * @author linjf
 * TODO
 */
public class Base64 {
	 static private final int     BASELENGTH           = 128;
	    static private final int     LOOKUPLENGTH         = 64;
	    static private final int     TWENTYFOURBITGROUP   = 24;
	    static private final int     EIGHTBIT             = 8;
	    static private final int     SIXTEENBIT           = 16;
	    static private final int     FOURBYTE             = 4;
	    static private final int     SIGN                 = -128;
	    static private final char    PAD                  = '=';
	    static private final boolean FDEBUG               = false;
	    static final private byte[]  BASE64ALPHABET       = new byte[BASELENGTH];
	    static final private char[]  LOOKUPBASE64ALPHABET = new char[LOOKUPLENGTH];
	    static final private char CHAR_A = 'A';
	    static final private char CHAR_LOWERCASE_A = 'a';
	    static final private char CHAR_0 = '0';
	    static final private char CHAR_9 = '9';
	    static final private char CHAR_Z = 'Z';
	    static final private char CHAR_LOWERCASE_Z = 'z';
	    static final private int INT_52 = 52;
	    static final private int INT_26 = 26;
	    static final private int INT_51 = 51;
	    static final private int INT_25 = 25;
	    static final private char INT_61 = 61;
	    static final private byte BYTE_0XF = 0xf;
	    static final private byte BYTE_0X3 = 0x3;

	    static {
	        for (int i = 0; i < BASELENGTH; ++i) {
	            BASE64ALPHABET[i] = -1;
	        }
	        for (int i = CHAR_Z; i >= CHAR_A; i--) {
	            BASE64ALPHABET[i] = (byte) (i - CHAR_A);
	        }
	        for (int i = CHAR_LOWERCASE_Z; i >= CHAR_LOWERCASE_A; i--) {
	            BASE64ALPHABET[i] = (byte) (i - CHAR_LOWERCASE_A + INT_26);
	        }

	        for (int i = CHAR_9; i >= CHAR_0; i--) {
	            BASE64ALPHABET[i] = (byte) (i - CHAR_0 + INT_52);
	        }

	        BASE64ALPHABET['+'] = 62;
	        BASE64ALPHABET['/'] = 63;

	        for (int i = 0; i <= INT_25; i++) {
	            LOOKUPBASE64ALPHABET[i] = (char) (CHAR_A + i);
	        }

	        for (int i = 26, j = 0; i <= INT_51; i++, j++) {
	            LOOKUPBASE64ALPHABET[i] = (char) (CHAR_LOWERCASE_A + j);
	        }

	        for (int i = INT_52, j = 0; i <= INT_61; i++, j++) {
	            LOOKUPBASE64ALPHABET[i] = (char) (CHAR_0 + j);
	        }
	        LOOKUPBASE64ALPHABET[62] = (char) '+';
	        LOOKUPBASE64ALPHABET[63] = (char) '/';

	    }

	    private static boolean isWhiteSpace(char octect) {
	        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
	    }

	    private static boolean isPad(char octect) {
	        return (octect == PAD);
	    }

	    private static boolean isData(char octect) {
	        return (octect < BASELENGTH && BASE64ALPHABET[octect] != -1);
	    }

	    /**
	     * Encodes hex octects into Base64
	     *
	     * @param binaryData Array containing binaryData
	     * @return Encoded Base64 array
	     */
	    public static String encode(byte[] binaryData) {

	        if (binaryData == null) {
	            return null;
	        }

	        int lengthDataBits = binaryData.length * EIGHTBIT;
	        if (lengthDataBits == 0) {
	            return "";
	        }

	        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
	        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
	        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
	        char encodedData[] = null;

	        encodedData = new char[numberQuartet * 4];

	        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

	        int encodedIndex = 0;
	        int dataIndex = 0;
	        if (FDEBUG) {
	        	KasiteConfig.print("number of triplets = " + numberTriplets);
	        }

	        for (int i = 0; i < numberTriplets; i++) {
	            b1 = binaryData[dataIndex++];
	            b2 = binaryData[dataIndex++];
	            b3 = binaryData[dataIndex++];

	            if (FDEBUG) {
	            	KasiteConfig.print("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
	            }

	            l = (byte) (b2 & 0x0f);
	            k = (byte) (b1 & 0x03);

	            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
	            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
	            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

	            if (FDEBUG) {
	            	KasiteConfig.print("val2 = " + val2);
	            	KasiteConfig.print("k4   = " + (k << 4));
	            	KasiteConfig.print("vak  = " + (val2 | (k << 4)));
	            }

	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val1];
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val2 | (k << 4)];
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[(l << 2) | val3];
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[b3 & 0x3f];
	        }

	        // form integral number of 6-bit groups
	        if (fewerThan24bits == EIGHTBIT) {
	            b1 = binaryData[dataIndex];
	            k = (byte) (b1 & 0x03);
	            if (FDEBUG) {
	            	KasiteConfig.print("b1=" + b1);
	            	KasiteConfig.print("b1<<2 = " + (b1 >> 2));
	            }
	            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val1];
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[k << 4];
	            encodedData[encodedIndex++] = PAD;
	            encodedData[encodedIndex++] = PAD;
	        } else if (fewerThan24bits == SIXTEENBIT) {
	            b1 = binaryData[dataIndex];
	            b2 = binaryData[dataIndex + 1];
	            l = (byte) (b2 & 0x0f);
	            k = (byte) (b1 & 0x03);

	            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
	            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val1];
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[val2 | (k << 4)];
	            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[l << 2];
	            encodedData[encodedIndex++] = PAD;
	        }

	        return new String(encodedData);
	    }

	    /**
	     * Decodes Base64 data into octects
	     *
	     * @param encoded string containing Base64 data
	     * @return Array containind decoded data.
	     */
	    public static byte[] decode(String encoded) {

	        if (encoded == null) {
	            return null;
	        }

	        char[] base64Data = encoded.toCharArray();
	        // remove white spaces
	        int len = removeWhiteSpace(base64Data);
	      //should be divisible by four
	        if (len % FOURBYTE != 0) {
	            return null;
	        }

	        int numberQuadruple = (len / FOURBYTE);

	        if (numberQuadruple == 0) {
	            return new byte[0];
	        }

	        byte decodedData[] = null;
	        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
	        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

	        int i = 0;
	        int encodedIndex = 0;
	        int dataIndex = 0;
	        decodedData = new byte[(numberQuadruple) * 3];

	        for (; i < numberQuadruple - 1; i++) {

	            if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))
	                || !isData((d3 = base64Data[dataIndex++]))
	                || !isData((d4 = base64Data[dataIndex++]))) {
	                return null;
	            }//if found "no data" just return null

	            b1 = BASE64ALPHABET[d1];
	            b2 = BASE64ALPHABET[d2];
	            b3 = BASE64ALPHABET[d3];
	            b4 = BASE64ALPHABET[d4];

	            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
	            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
	            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
	        }
	      //if found "no data" just return null
	        if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
	            return null;
	        }

	        b1 = BASE64ALPHABET[d1];
	        b2 = BASE64ALPHABET[d2];

	        d3 = base64Data[dataIndex++];
	        d4 = base64Data[dataIndex++];
	      //Check if they are PAD characters
	        if (!isData((d3)) || !isData((d4))) {
	            if (isPad(d3) && isPad(d4)) {
	            	//last 4 bits should be zero
	                if ((b2 & BYTE_0XF) != 0)
	                {
	                    return null;
	                }
	                byte[] tmp = new byte[i * 3 + 1];
	                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
	                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
	                return tmp;
	            } else if (!isPad(d3) && isPad(d4)) {
	                b3 = BASE64ALPHABET[d3];
	              //last 2 bits should be zero
	                if ((b3 & BYTE_0X3) != 0)
	                {
	                    return null;
	                }
	                byte[] tmp = new byte[i * 3 + 2];
	                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
	                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
	                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
	                return tmp;
	            } else {
	                return null;
	            }
	        } else { //No PAD e.g 3cQl
	            b3 = BASE64ALPHABET[d3];
	            b4 = BASE64ALPHABET[d4];
	            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
	            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
	            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

	        }

	        return decodedData;
	    }

	    /**
	     * remove WhiteSpace from MIME containing encoded Base64 data.
	     *
	     * @param data  the byte array of base64 data (with WS)
	     * @return      the new length
	     */
	    private static int removeWhiteSpace(char[] data) {
	        if (data == null) {
	            return 0;
	        }

	        // count characters that's not whitespace
	        int newSize = 0;
	        int len = data.length;
	        for (int i = 0; i < len; i++) {
	            if (!isWhiteSpace(data[i])) {
	                data[newSize++] = data[i];
	            }
	        }
	        return newSize;
	    }
}
