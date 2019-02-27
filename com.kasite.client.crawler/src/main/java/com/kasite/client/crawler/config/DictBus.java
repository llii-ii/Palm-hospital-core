package com.kasite.client.crawler.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DictBus {

	private static final Logger logger = LoggerFactory.getLogger(DictBus.class);
	public static DictBus install;
	public static File file;
	private String filePath;
	
	private Map<String, Map<String,String> > dictMaps = new HashMap<>();
	
	private DictBus() {
		this.filePath= System.getProperty("user.dir")+ File.separator +Convent.getSysMyDictFilePath();
		if(file == null) {
			file = new File(filePath);
			if(!file.exists() || !file.isFile()) {
				logger.error("数据表文件加载异常。请核实文件是否存在："+filePath);
				return;
			}
		}
		init();
//		FileWatch.getInstall(this).start();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public DictBus setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	
	public String getValue(String type,String code) {
		Map<String,String> dict = dictMaps.get(type);
		if(null != dict) {
			return dict.get(code);
		}else {
			return null;
		}
	}
	
	public void init() {
		if(null != file) {
			try {
				SAXReader reader = new SAXReader();
				Document doc = reader.read(file);
				List<Element> elements = doc.getRootElement().elements("dic");
				for (Element ele : elements) {
					String code = ele.attributeValue("code");
					String type = ele.attributeValue("type");
					String value = ele.attributeValue("value");
					Map<String,String> dict = dictMaps.get(type);
					if(null == dict) {
						dict = new HashMap<>();
						dictMaps.put(type, dict);
					}
					dict.put(code, value);
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static synchronized DictBus getInstall() {
		if(null == install) {
			install = new DictBus();
		}
		return install;
	}
	
}
