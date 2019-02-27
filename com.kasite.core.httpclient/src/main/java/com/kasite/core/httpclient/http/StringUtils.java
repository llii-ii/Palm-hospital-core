package com.kasite.core.httpclient.http;

import java.util.Collection;

public class StringUtils {
	
	   /**
     * 检查指定的字符串列表是否不为空。
     */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	public static String join(Object[] array, String sep)
	  {
	    return join(array, sep, null);
	  }
	  
	  public static String join(Collection<?> list, String sep)
	  {
	    return join(list, sep, null);
	  }
	  
	  public static String join(Collection<?> list, String sep, String prefix)
	  {
	    Object[] array = list == null ? null : list.toArray();
	    return join(array, sep, prefix);
	  }
	  
	  public static String join(Object[] array, String sep, String prefix)
	  {
	    if (array == null) {
	      return "";
	    }
	    int arraySize = array.length;
	    if (arraySize == 0) {
	      return "";
	    }
	    if (sep == null) {
	      sep = "";
	    }
	    if (prefix == null) {
	      prefix = "";
	    }
	    StringBuilder buf = new StringBuilder(prefix);
	    for (int i = 0; i < arraySize; i++)
	    {
	      if (i > 0) {
	        buf.append(sep);
	      }
	      buf.append(array[i] == null ? "" : array[i]);
	    }
	    return buf.toString();
	  }
	  
	  public static String jsonJoin(String[] array)
	  {
	    int arraySize = array.length;
	    int bufSize = arraySize * (array[0].length() + 3);
	    StringBuilder buf = new StringBuilder(bufSize);
	    for (int i = 0; i < arraySize; i++)
	    {
	      if (i > 0) {
	        buf.append(',');
	      }
	      buf.append('"');
	      buf.append(array[i]);
	      buf.append('"');
	    }
	    return buf.toString();
	  }
	  
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	 public static String replace(String text, String repl, String with)
	 /*      */   {
	 /* 3134 */     return replace(text, repl, with, -1);
	 /*      */   }
	 /*      */     public static boolean isEmpty(String str)
	 /*      */   {
		 /*  185 */     return (str == null) || (str.length() == 0);
		 /*      */   }
		 /*      */   
	  public static String replace(String text, String repl, String with, int max)
	  /*      */   {
	  /* 3166 */     if ((isEmpty(text)) || (isEmpty(repl)) || (with == null) || (max == 0)) {
	  /* 3167 */       return text;
	  /*      */     }
	  /* 3169 */     int start = 0;
	  /* 3170 */     int end = text.indexOf(repl, start);
	  /* 3171 */     if (end == -1) {
	  /* 3172 */       return text;
	  /*      */     }
	  /* 3174 */     int replLength = repl.length();
	  /* 3175 */     int increase = with.length() - replLength;
	  /* 3176 */     increase = increase < 0 ? 0 : increase;
	  /* 3177 */     increase *= (max > 64 ? 64 : max < 0 ? 16 : max);
	  /* 3178 */     StringBuffer buf = new StringBuffer(text.length() + increase);
	  /* 3179 */     while (end != -1) {
	  /* 3180 */       buf.append(text.substring(start, end)).append(with);
	  /* 3181 */       start = end + replLength;
	  /* 3182 */       max--; if (max == 0) {
	  /*      */         break;
	  /*      */       }
	  /* 3185 */       end = text.indexOf(repl, start);
	  /*      */     }
	  /* 3187 */     buf.append(text.substring(start));
	  /* 3188 */     return buf.toString();
	  /*      */   }
	  /*      */   
	  /*      */ 
	public static boolean isBlank(String str) {
		int strLen;

		if ((str == null) || ((strLen = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
