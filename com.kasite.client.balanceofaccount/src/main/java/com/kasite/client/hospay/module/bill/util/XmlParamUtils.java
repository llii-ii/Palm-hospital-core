package com.kasite.client.hospay.module.bill.util;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.util.StringUtil;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.*;
import net.sf.json.xml.XMLSerializer;

/**
 * 对接口的参数xml进行快速读写拼装
 *
 * @author cc
 * @date
 */
public final class XmlParamUtils {

     private static Map<Character, String> char2StringMap = new HashMap<Character, String>();

     static {
          char2StringMap.put('<', "&lt;");
          char2StringMap.put('>', "&gt;");
     }

     public static String xml2JSON(String xml) {
          if (StringUtil.isEmpty(xml)) {
               return "";
          } else {
               return new XMLSerializer().read(xml).toString();
          }
     }

     /**
      * 根据传入的xml组装参数，节点名称为空key，节点内容为value
      *
      * @param reqXml 传入的xml参数
      * @param XPath  获取xpath下的所有第一级节点及其对应的内容
      * @param isMust 是否验证 true 是，如果不存在报异常
      *
      * @return
      * @throws DocumentException
      */
     public static Map<String, String> getMapFromXml(String reqXml, String XPath, boolean isMust) throws DocumentException {
          Map<String, String> map = new LinkedHashMap<String, String>();
          if (StringUtils.isBlank(XPath)) {
               return map;
          }
          Document document = null;
          //先直接转换传入的xml字符串，如果转换错误，再处理特殊字符，加快解析速度
          try {
               document = DocumentHelper.parseText(reqXml);
          } catch (DocumentException e) {
               System.err.println("========传入的原始xml【" + reqXml + "】不符合xml规范，采用特殊方法处理不规范字符串，如果再报错，则输入的xml字符串无可救药。。。");
               document = DocumentHelper.parseText(dealSpeclZifu(reqXml));
          }
          Node node = document.selectSingleNode(XPath);
          if (isMust && node == null) {
               throw new RuntimeException("传入的参数【" + reqXml + "】节点没有对应【" + XPath + "】参数！");
          }
          if (node != null) {
               if (node instanceof Element) {
                    Element element = (Element) node;
                    if (element != null) {
                         List<Element> children = element.elements();
                         if (children != null && children.size() > 0) {
                              for (Element e : children) {
                                   map.put(e.getName(), e.getText().trim());
                              }
                         }
                    }
               } else if (node instanceof Document) {
                    List<Element> children = node.getDocument().getRootElement().elements();
                    if (children != null && children.size() > 0) {
                         for (Element e : children) {
                              map.put(e.getName(), e.getText().trim());
                         }
                    }
               }
          }
          return map;
     }


     /**
      * 根据传入的xml组装参数，节点名称为空key，节点内容为value
      *
      * @param reqXml 传入的xml参数
      * @param XPaths 获取多个xpath下的所有第一级节点及其对应的内容
      *
      * @return
      * @throws DocumentException
      */
     public static Map<String, String> getMapFromXml(String reqXml, String... XPaths) throws DocumentException {
          Map<String, String> map = new LinkedHashMap<String, String>();
          if (XPaths == null || XPaths.length == 0) {
               return map;
          }
          Document document = null;
          //先直接转换传入的xml字符串，如果转换错误，再处理特殊字符，加快解析速度
          try {
               document = DocumentHelper.parseText(reqXml);
          } catch (DocumentException e) {
               System.err.println("========传入的原始xml【" + reqXml + "】不符合xml规范，采用特殊方法处理不规范字符串，如果再报错，则输入的xml字符串无可救药。。。");
               document = DocumentHelper.parseText(dealSpeclZifu(reqXml));
          }
          for (String XPath : XPaths) {
               Node node = document.selectSingleNode(XPath);
               if (node != null) {
                    if (node instanceof Element) {
                         Element element = (Element) node;
                         if (element != null) {
                              List<Element> children = element.elements();
                              if (children != null && children.size() > 0) {
                                   for (Element e : children) {
                                        map.put(e.getName(), e.getText().trim());
                                   }
                              }
                         }
                    } else if (node instanceof Document) {
                         List<Element> children = ((Document) node).getRootElement().elements();
                         if (children != null && children.size() > 0) {
                              for (Element e : children) {
                                   map.put(e.getName(), e.getText().trim());
                              }
                         }
                    }
               }
          }
          return map;
     }

     /**
      * 根据传入的xml组装成对象信息的集合，对象以map形式保存信息
      *
      * @param reqXml 传入的xml参数
      * @param XPath  获取xpath下的所有第一级节点及其对应的内容
      *
      * @return
      * @throws DocumentException
      */
     public static List<Map<String, String>> getListMapFromXml(String reqXml, String XPath) throws DocumentException {
          List<Map<String, String>> list = new ArrayList<Map<String, String>>();
          if (XPath == null) {
               return list;
          }
          Document document = null;
          //先直接转换传入的xml字符串，如果转换错误，再处理特殊字符，加快解析速度
          try {
               document = DocumentHelper.parseText(reqXml);
          } catch (DocumentException e) {
               System.err.println("========传入的原始xml【" + reqXml + "】不符合xml规范，采用特殊方法处理不规范字符串，如果再报错，则输入的xml字符串无可救药。。。");
               document = DocumentHelper.parseText(dealSpeclZifu(reqXml));
          }
          List<Node> nodes = document.selectNodes(XPath);
          if (nodes != null && nodes.size() > 0) {
               for (Node node : nodes) {
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    if (node instanceof Element) {
                         Element element = (Element) node;
                         if (element != null) {
                              List<Element> children = element.elements();
                              if (children != null && children.size() > 0) {
                                   for (Element e : children) {
                                        map.put(e.getName(), e.getText().trim());
                                   }
                              }
                         }
                    } else if (node instanceof Document) {
                         List<Element> children = ((Document) node).getRootElement().elements();
                         if (children != null && children.size() > 0) {
                              for (Element e : children) {
                                   map.put(e.getName(), e.getText().trim());
                              }
                         }
                    }
                    list.add(map);
               }
          }
          return list;
     }

     /**
      * 根据传入的xml获取指定路径节点内容的集合
      *
      * @param reqXml 传入的xml参数
      * @param XPath  获取xpath下节点内容
      *
      * @return
      * @throws DocumentException
      */
     public static List<String> getListFromXml(String reqXml, String XPath) throws DocumentException {
          List<String> list = new ArrayList<String>();
          if (XPath == null) {
               return list;
          }
          Document document = null;
          //先直接转换传入的xml字符串，如果转换错误，再处理特殊字符，加快解析速度
          try {
               document = DocumentHelper.parseText(reqXml);
          } catch (DocumentException e) {
               System.err.println("========传入的原始xml【" + reqXml + "】不符合xml规范，采用特殊方法处理不规范字符串，如果再报错，则输入的xml字符串无可救药。。。");
               document = DocumentHelper.parseText(dealSpeclZifu(reqXml));
          }
          List<Node> nodes = document.selectNodes(XPath);
          if (nodes != null && nodes.size() > 0) {
               for (Node node : nodes) {
                    if (node instanceof Element) {
                         Element element = (Element) node;
                         if (element != null) {
                              list.add(element.getText().trim());
                         }
                    }
               }
          }
          return list;
     }

     /**
      * 根据传入的xml获取指定路径节点内容的集合
      *
      * @param reqXml 传入的xml参数
      * @param XPath  获取xpath下节点内容
      *
      * @return
      * @throws DocumentException
      */
     public static Set<String> getSetFromXml(String reqXml, String XPath) throws DocumentException {
          Set<String> set = new TreeSet<String>();
          if (XPath == null) {
               return set;
          }
          Document document = null;
          //先直接转换传入的xml字符串，如果转换错误，再处理特殊字符，加快解析速度
          try {
               document = DocumentHelper.parseText(reqXml);
          } catch (DocumentException e) {
               System.err.println("========传入的原始xml【" + reqXml + "】不符合xml规范，采用特殊方法处理不规范字符串，如果再报错，则输入的xml字符串无可救药。。。");
               document = DocumentHelper.parseText(dealSpeclZifu(reqXml));
          }
          List<Node> nodes = document.selectNodes(XPath);
          if (nodes != null && nodes.size() > 0) {
               for (Node node : nodes) {
                    if (node instanceof Element) {
                         Element element = (Element) node;
                         if (element != null) {
                              set.add(element.getText().trim());
                         }
                    }
               }
          }
          return set;
     }

     /**
      * 根据传入的xml组装对象信息的集合，对象以map形式保存信息
      *
      * @param reqXml         传入的xml参数
      * @param XPath          获取xpath下的所有第一级节点及其对应的内容
      * @param containsParent 是否包含父节点的相关信息
      *
      * @return
      * @throws DocumentException
      */
     public static List<Map<String, String>> getListMapFromXml(String reqXml, String XPath, boolean containsParent) throws DocumentException {
          List<Map<String, String>> list = new ArrayList<Map<String, String>>();
          if (XPath == null) {
               return list;
          }
          Document document = null;
          //先直接转换传入的xml字符串，如果转换错误，再处理特殊字符，加快解析速度
          try {
               document = DocumentHelper.parseText(reqXml);
          } catch (DocumentException e) {
               System.err.println("========传入的原始xml【" + reqXml + "】不符合xml规范，采用特殊方法处理不规范字符串，如果再报错，则输入的xml字符串无可救药。。。");
               document = DocumentHelper.parseText(dealSpeclZifu(reqXml));
          }
          List<Node> nodes = document.selectNodes(XPath);
          if (nodes != null && nodes.size() > 0) {
               Map<Element, Map<String, String>> tempMap = new HashMap<Element, Map<String, String>>();//父节点，对应具体信息
               for (Node node : nodes) {
                    Element parentElement = node.getParent();
                    Map<String, String> parentMap = null;
                    //包含父节点的标签信息
                    if (containsParent) {
                         if (parentElement != null) {
                              parentMap = tempMap.get(parentElement);
                              if (parentMap == null) {
                                   parentMap = new LinkedHashMap<String, String>();
                                   List<Element> elements = parentElement.elements();
                                   if (elements != null && elements.size() > 0) {
                                        for (Element e : elements) {
                                             parentMap.put(e.getName(), e.getText().trim());
                                        }
                                   }
                                   tempMap.put(parentElement, parentMap);
                              }
                         }
                    }
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    if (node instanceof Element) {
                         Element element = (Element) node;
                         if (element != null) {
                              List<Element> children = element.elements();
                              if (children != null && children.size() > 0) {
                                   for (Element e : children) {
                                        map.put(e.getName(), e.getText().trim());
                                   }
                              }
                         }
                    } else if (node instanceof Document) {
                         List<Element> children = ((Document) node).getRootElement().elements();
                         if (children != null && children.size() > 0) {
                              for (Element e : children) {
                                   map.put(e.getName(), e.getText().trim());
                              }
                         }
                    }
                    if (parentMap != null) {
                         map.putAll(parentMap);
                    }
                    list.add(map);
               }
          }
          return list;
     }

     /**
      * 根据传入的xml组装参数
      *
      * @param reqXml 传入的xml参数
      * @param XPath  获取xpath下的所有第一级节点及其对应的内容
      *
      * @return
      * @throws DocumentException
      */
     public static Map<String, String> getMapFromXml(String reqXml, String XPath) throws DocumentException {
          return getMapFromXml(reqXml, XPath, false);
     }

     /**
      * 把map转换成xml形式
      *
      * @param root 根节点标签
      * @param map  要转换的map对象，key是标签，value是节点内容
      *
      * @return
      */
     public static String toXML(String root, Map<String, String> map) {
          return toXML(root, map, "");
     }

     /**
      * 把map转换成xml形式
      *
      * @param root 根节点标签
      * @param map  要转换的map对象，key是标签，value是节点内容
      *
      * @return
      */
     public static String toXML(String root, Map<String, String> map, boolean CDATA) {
          return toXML(root, map, "", CDATA);
     }

     /**
      * 把map转换成xml形式
      *
      * @param root      根节点标签
      * @param map       要转换的map对象，key是标签，value是节点内容
      * @param appendXml 追加的xml片段内容
      *
      * @return 拼装好的xml<br>
      * &lt;root><br>
      * &lt;key1>value1&lt;/key1><br>
      * &lt;key2>value2&lt;/key2><br>
      * ...<br>
      * appendXml<br>
      * &lt;/root><br>
      */
     public static String toXML(String root, Map<String, String> map, String appendXml) {
          StringBuffer buffer = new StringBuffer("");
          if (map != null) {
               for (Entry<String, String> entry : map.entrySet()) {
                    buffer.append("<").append(entry.getKey()).append("><![CDATA[");
                    buffer.append(StringUtils.defaultString(entry.getValue()).trim());
                    buffer.append("]]></").append(entry.getKey()).append(">");
               }
          }
          if (StringUtils.isNotBlank(appendXml)) {
               buffer.append(appendXml);
          }
          if (StringUtils.isNotBlank(root)) {
               return new StringBuffer("<").append(root.trim()).append(">")
                       .append(buffer)
                       .append("</").append(root.trim()).append(">").toString();
          }
          return buffer.toString();
     }

     /**
      * 把map转换成xml形式
      *
      * @param root      根节点标签
      * @param map       要转换的map对象，key是标签，value是节点内容
      * @param appendXml 追加的xml片段内容
      * @param CDATA     拼装的xml是否拼装<![CDATA[...]]>内容,true 是,false 否
      *
      * @return 拼装好的xml<br>
      * &lt;root><br>
      * &lt;key1>value1&lt;/key1><br>
      * &lt;key2>value2&lt;/key2><br>
      * ...<br>
      * appendXml<br>
      * &lt;/root><br>
      */
     public static String toXML(String root, Map<String, String> map, String appendXml, boolean CDATA) {
          StringBuffer buffer = new StringBuffer("");
          if (map != null) {
               if (CDATA) {
                    for (Entry<String, String> entry : map.entrySet()) {
                         buffer.append("<").append(entry.getKey()).append("><![CDATA[");
                         buffer.append(StringUtils.defaultString(entry.getValue()).trim());
                         buffer.append("]]></").append(entry.getKey()).append(">");
                    }
               } else {
                    for (Entry<String, String> entry : map.entrySet()) {
                         buffer.append("<").append(entry.getKey()).append(">");
                         buffer.append(StringUtils.defaultString(entry.getValue()).trim());
                         buffer.append("</").append(entry.getKey()).append(">");
                    }
               }
          }
          if (StringUtils.isNotBlank(appendXml)) {
               buffer.append(appendXml);
          }
          if (StringUtils.isNotBlank(root)) {
               return new StringBuffer("<").append(root.trim()).append(">")
                       .append(buffer)
                       .append("</").append(root.trim()).append(">").toString();
          }
          return buffer.toString();
     }

     /**
      * 获取map的指定key(忽略大小写)的值,如果取出为空，则默认为空串
      *
      * @param map map对象
      * @param key key键值
      *
      * @return
      */
     public static String getValueFromMap(Map<String, String> map, String key) {
          return getValueFromMap(map, key, false, true, "", "");
     }

     /**
      * 获取map的指定key(忽略大小写)的值
      *
      * @param map      map对象
      * @param key      key键值
      * @param isMust   是否是必须，如果是必须的，当取到的值是空或空串，则抛出相关提示的异常
      * @param errorMsg 自定义的提示信息
      *
      * @return
      */
     public static String getValueFromMap(Map<String, String> map, String key, boolean isMust, String errorMsg) {
          return getValueFromMap(map, key, isMust, true, "", errorMsg);
     }

     /**
      * 获取map的指定key(忽略大小写)的值
      *
      * @param map          map对象
      * @param key          key键值
      * @param isMust       是否是必须，如果是必须的，当取到的值是空或空串，则抛出相关提示的异常
      * @param defaultValue 当取到的值为null时，指定的默认值
      * @param errorMsg     自定义的提示信息
      *
      * @return
      */
     public static String getValueFromMap(Map<String, String> map, String key, boolean isMust, String defaultValue, String errorMsg) {
          return getValueFromMap(map, key, isMust, true, defaultValue, errorMsg);
     }

     /**
      * 获取map的指定key的值
      *
      * @param map          map对象
      * @param key          key键值
      * @param isMust       是否是必须，如果是必须的，当取到的值是空或空串，则抛出相关提示的异常
      * @param ignoreCase   是否忽略key的大小写 ,true:是 ，false：否
      * @param defaultValue 当取到的值为null时，指定的默认值
      * @param errorMsg     自定义的提示信息
      *
      * @return
      */
     public static String getValueFromMap(Map<String, String> map, String key, boolean isMust, boolean ignoreCase, String defaultValue, String errorMsg) {
          if (map == null || key == null) {
               throw new RuntimeException("传入的map和key不能为空！");
          }
          String value = null;
          if (ignoreCase) {//忽略key大小写
               CaseInsensitiveMap map2 = new CaseInsensitiveMap(map);
               value = (String) map2.get(key);
          } else {
               value = map.get(key);
          }

          if (isMust && StringUtils.isBlank(value)) {
               throw new RuntimeException(StringUtils.defaultString(errorMsg, "传入的参数【" + key + "】不能为空！"));
          }
          if (StringUtils.isBlank(value)) {
               value = defaultValue;
          }
          return value;
     }

     /**
      * 获取map的指定key的整数值
      *
      * @param map          map对象
      * @param key          key键值
      * @param isMust       是否是必须，如果是必须的，当取到的值是空或空串，则抛出相关提示的异常
      * @param defaultValue 当取到的值为null时，指定的默认值
      * @param errorMsg     自定义的提示信息
      *
      * @return
      */
     public static Integer getIntValueFromMap(Map<String, String> map, String key, boolean isMust, Integer defaultValue, String errorMsg) {
          return getIntValueFromMap(map, key, isMust, false, defaultValue, errorMsg);
     }

     /**
      * 获取map的指定key的整数值
      *
      * @param map          map对象
      * @param key          key键值
      * @param isMust       是否是必须，如果是必须的，当取到的值是空或空串，则抛出相关提示的异常
      * @param ignoreCase   是否忽略key的大小写 ,true:是 ，false：否
      * @param defaultValue 当取到的值为null时，指定的默认值
      * @param errorMsg     自定义的提示信息
      *
      * @return
      */
     public static Integer getIntValueFromMap(Map<String, String> map, String key, boolean isMust, boolean ignoreCase, Integer defaultValue, String errorMsg) {
          if (map == null || key == null) {
               throw new RuntimeException("传入的map和key不能为空！");
          }
          String value = null;
          if (ignoreCase) {//忽略key大小写
               CaseInsensitiveMap map2 = new CaseInsensitiveMap(map);
               value = (String) map2.get(key);
          } else {
               value = map.get(key);
          }
          if (isMust && StringUtils.isBlank(value)) {
               throw new RuntimeException(StringUtils.defaultString(errorMsg, "传入的参数【" + key + "】不能为空！"));
          }
          if (StringUtils.isBlank(value)) {
               return defaultValue;
          }
          if (!value.trim().matches("\\d+")) {
               throw new RuntimeException("传入的参数" + key + "【" + value + "】不符合整数规则！");
          }
          try {
               return Integer.valueOf(value.trim());
          } catch (NumberFormatException e) {
               throw new RuntimeException("传入的参数" + key + "【" + value + "】长度超出整数范围！");
          }
     }

     /**
      * 构建指定key集合的map对象
      *
      * @param dest      指定节点名称集合，如果需要指定另外名称的值则用“=别名(忽略大小写)”，比如“A=B”表示将取map的key为B的值赋值到A
      *                  例子：copyToMap(list['A','B=D','C'],Map[A=12,D=23]) => map[A=12,B=23,C=]
      * @param sourceMap 要取值的map
      *
      * @return
      */
     public static Map<String, String> copyToMap(List<String> dest, Map<String, String> sourceMap) {
          Map<String, String> map = new LinkedHashMap<String, String>();
          CaseInsensitiveMap map2 = new CaseInsensitiveMap(sourceMap);
          if (dest != null && sourceMap != null) {
               for (String key : dest) {
                    if (key != null) {
                         String[] array = key.trim().split("=");
                         if (array.length == 1) {//没有指定，则取同名对应的值
                              map.put(array[0].trim(), StringUtils.defaultString((String) map2.get(array[0].trim())).trim());
                         } else if (array.length == 2) {//取指定名称对应的值
                              map.put(array[0].trim(), StringUtils.defaultString((String) map2.get(array[1].trim())).trim());
                         }
                    }
               }
          }
          return map;
     }

     /**
      * 处理返回的xml字符串，把"<xx>"标签外的特殊字符转义，比如“&”，“<”，“>”
      *
      * @param xml 要处理的xml字符串
      *
      * @return
      */
     public static String filterSpeclZifu(String xml) {
          if (StringUtils.isBlank(xml)) {
               return "";
          }
          xml = xml.replace("&amp;", "&").replace("&", "&amp;");
          int length = xml.length();
          List<Integer> wzList = new ArrayList<Integer>();
          for (int i = 0; i < length; i++) {
               if (xml.charAt(i) == '<' || xml.charAt(i) == '>') {
                    wzList.add(i);
               }
          }
          List<Integer> replaceList = new ArrayList<Integer>();
          boolean beginFlag = false;
          int tempBegin = 0;
          for (int i = 0, size = wzList.size(); i < size; i++) {
               int wz = wzList.get(i);
               //没有开始标记‘<’
               if (!beginFlag) {
                    //此位置上的字符不是‘<’，要过滤
                    if (xml.charAt(wz) == '>') {
                         replaceList.add(wz);
                    } else {
                         tempBegin = wz;
                         beginFlag = true;
                    }
               } else {//已有开始标记'<'
                    //已有开始标记，又碰到第二个开始标记，则前一个开始标记要作废
                    if (xml.charAt(wz) == '<') {
                         replaceList.add(tempBegin);//把前一个标记放入要过滤的集合
                         tempBegin = wz;//把当前的位置赋予临时变量
                    } else {//遇到结束标记‘>’，则标记beginFlag=false
                         beginFlag = false;
                    }
               }
          }
          StringBuffer buffer = new StringBuffer();
          for (int i = 0, size = xml.length(); i < size; i++) {
               if (replaceList.contains(i)) {
                    buffer.append(char2StringMap.get(xml.charAt(i)));
               } else {
                    buffer.append(xml.charAt(i));
               }
          }
          return buffer.toString();
     }


     /**
      * 处理返回的xml字符串，把"<xx>"标签外的特殊字符转义，比如“&”，“<”，“>”,注意“<![CDATA[xxx]]>”里面的字符串不转义
      *
      * @param xml 要处理的xml字符串
      *
      * @return
      */
     public static String dealSpeclZifu(String xml) {
          if (StringUtils.isBlank(xml)) {
               return "";
          }
          String p1 = "(?i)(<\\!\\[CDATA\\[[\\s\\S]*?\\]\\]>)";
          //第一层
          String[] array = xml.split(p1);
          int length = array.length;
          StringBuffer buffer = new StringBuffer();
          //只有一个，说明没有“<![CDATA[xxx]]>”
          if (length == 1) {
               return filterSpeclZifu(xml);
          } else {
               Pattern pattern = Pattern.compile(p1);
               Matcher matcher = pattern.matcher(xml);
               List<String> list = new ArrayList<String>();
               while (matcher.find()) {
                    list.add(matcher.group(1));
               }
               int size = list.size();
               if (length <= size) {
                    for (int i = 0; i < size; i++) {
                         if (i + 1 <= length) {
                              buffer.append(filterSpeclZifu(array[i]));
                         }
                         buffer.append(list.get(i));
                    }
               } else {
                    for (int i = 0; i < length; i++) {
                         buffer.append(filterSpeclZifu(array[i]));
                         if (i + 1 <= size) {
                              buffer.append(list.get(i));
                         }
                    }
               }
          }
          return buffer.toString();
     }

     /**
      * 把泛型map<String,Object> ==> map<String,String>
      *
      * @param map
      *
      * @return
      */
     public static Map<String, String> change(Map<String, Object> map) {
          Map<String, String> map2 = new HashMap<String, String>();
          if (map != null && !map.isEmpty()) {
               for (String key : map.keySet()) {
                    Object value = map.get(key);
                    if (value == null || value instanceof String) {
                         map2.put(key, (String) value);
                    } else if (value instanceof Date) {
                         map2.put(key, DateFormatUtils.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                    } else if (value instanceof Number || value instanceof Boolean) {
                         map2.put(key, value.toString());
                    } else {
                         map2.put(key, value.toString());
                    }
               }
          }
          return map2;
     }

     /**
      * String 转 org.dom4j.Document
      *
      * @param xml
      *
      * @return
      * @throws DocumentException
      */
     public static Document strToDocument(String xml) throws DocumentException {
          return DocumentHelper.parseText(xml);
     }

     /**
      * org.dom4j.Document 转  com.alibaba.fastjson.JSONObject
      *
      * @param xml
      *
      * @return
      * @throws DocumentException
      */
     public static JSONObject documentToJSONObject(String xml)  {
          JSONObject json = null;
          try {
               json = elementToJSONObject(strToDocument(xml).getRootElement());
          } catch (DocumentException e) {
               e.printStackTrace();
          }
          return json;
     }

     /**
      * org.dom4j.Element 转  com.alibaba.fastjson.JSONObject
      *
      * @param node
      *
      * @return
      */
     public static JSONObject elementToJSONObject(Element node) {
          JSONObject result = new JSONObject();
          // 当前节点的名称、文本内容和属性 当前节点的所有属性的list
          List<Attribute> listAttr = node.attributes();
          // 遍历当前节点的所有属性
          for (Attribute attr : listAttr) {
               result.put(attr.getName(), attr.getValue());
          }
          // 递归遍历当前节点所有的子节点 所有一级子节点的list
          List<Element> listElement = node.elements();
          if (!listElement.isEmpty()) {
               // 遍历所有一级子节点
               for (Element e : listElement) {
                    // 判断一级节点是否有属性和子节点
                    if (e.attributes().isEmpty() && e.elements().isEmpty()) {
                         // 沒有则将当前节点作为上级节点的属性对待
                         result.put(e.getName(), e.getTextTrim());
                    }else {
                         // 判断父节点是否存在该一级节点名称的属性
                         if (!result.containsKey(e.getName())) {
                              // 没有则创建
                              result.put(e.getName(), new JSONArray());
                         }
                         // 将该一级节点放入该节点名称的属性对应的值中
                         ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));
                    }
               }
          }
          return result;
     }

     public static String arrayToXml(SortedMap<String, String> arr) {
          String xml = "<xml>";
          Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
          while (iter.hasNext()) {
               Entry<String, String> entry = iter.next();
               String key = entry.getKey();
               String val = entry.getValue();
               xml += "<" + key + ">" + val + "</" + key + ">";
          }

          xml += "</xml>";
          return xml;
     }
}