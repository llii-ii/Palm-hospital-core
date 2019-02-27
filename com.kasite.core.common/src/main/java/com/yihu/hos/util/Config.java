package com.yihu.hos.util;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.coreframework.util.FileWatchdog;
public abstract class Config extends FileWatchdog{
	protected Document doc;
	protected Element root;
	protected void init(String filename) {
		try {
			super.init(filename);
			SAXReader reader = new SAXReader();
			reader.setEncoding("UTF-8");
	 		doc = reader.read(file);
	 		root = doc.getRootElement();
			Element delay = root.element("DELAY");
			if(null != delay){
				try{
					this.delay = Integer.parseInt(delay.getText());
				}catch (Exception e) {
					// TODO: handle exception
					throw new Exception("DELAY 节点必须为一个整数");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
