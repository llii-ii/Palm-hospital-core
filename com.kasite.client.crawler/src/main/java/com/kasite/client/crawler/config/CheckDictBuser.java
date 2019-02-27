package com.kasite.client.crawler.config;

import com.kasite.core.common.validator.CheckDict;
import com.kasite.core.common.validator.CheckDictInf;

public class CheckDictBuser implements CheckDictInf{

	@Override
	public boolean checkValue(String version, String type, String code, CheckDict dict) {
		return false;
	}
//	private static final Logger logger = LoggerFactory.getLogger(CheckDictBuser.class);
//	private DictBus bus;
//	private DictBus1_5 bus2;
//	private Data1_5Bus dataBus;
//	
//	public static CheckDictBuser create() {
//		return new CheckDictBuser();
//	}
//	
//	public CheckDictBuser() {
//		bus = DictBus.getInstall();
//		bus2 = DictBus1_5.getInstall();
//		dataBus = Data1_5Bus.getInstall();
//	}
//	public String getValue(String type, String code) {
//		return bus.getValue(type, code);
//	}
//	
//	public String get1_5Value(String type,String code) {
//		return bus2.getValue(type, code);
//	}
//	
//	public Map<String, String> get1_5DictMap(String type){
//		return bus2.getDictMap(type);
//	}
//	
//	public Map<String, Data15PkVo> getData15Map(String tableName){
//		return dataBus.getData15Map(tableName);
//	}
//	
//	@Override
//	public boolean checkValue(String version,String type, String code,CheckDict dict) {
//		if("1.5".equals(version)) {
//			String val = get1_5Value(type, code);
//			if(Convent.isDebug() && StringUtil.isNotBlank(code)&& null == val) {
//				logger.debug("字典值异常：V="+version+"\tDictName="+type+"\tDictValue="+code  );
//			}
//			if(null != get1_5Value(type, code)) {
//				return true;
//			}
//		}else {
//			if(null != getValue(type, code)) {
//				return true;
//			}
//		}
//		
//		return false;
//	}

}
