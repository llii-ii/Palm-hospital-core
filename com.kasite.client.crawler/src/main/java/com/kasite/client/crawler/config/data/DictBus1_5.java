package com.kasite.client.crawler.config.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.modules.utils.CheckTheSame;
import com.kasite.client.crawler.modules.utils.IKAnalyzerUtil;


public class DictBus1_5 {
	private static final Logger logger = LoggerFactory.getLogger(DictBus1_5.class);
	public static DictBus1_5 install;
	private static File file;
	private String filePath;
	/**字典：code-value*/
	private Map<String, Map<String,String> > dictMaps = new HashMap<>();
	/**字典：value-code*/
	private Map<String, Map<String,String> > dictMaps2 = new HashMap<>();
	
	private DictBus1_5() {
		this.filePath= System.getProperty("user.dir")+ File.separator + Convent.getSysDictFilePath();
		if(null == file) {
			file = new File(filePath);
			logger.info("加载字典配置文件："+ filePath);
			if(!file.exists() || !file.isFile()) {
				logger.error("数据表文件加载异常。请核实文件是否存在："+filePath);
				return;
			}
		}
		init();
//		FileWatch1_5.getInstall(this).start();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public DictBus1_5 setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	
	public String getValue(String tableName,String privateName,String type,String code) {
		Map<String,String> dict = dictMaps.get(type);
		if(null != dict) {
			return dict.get(code);
		}else {
//			if(Convent.getIsCheck()) {
//				logger.error("未找到表：["+tableName+"]["+ privateName +"] 对应的字典表："+ type);
//			}
			return null;
		}
	}
	
	/**
	 * 取出名称对应的code  没有取【其他】的code 若有~
	 * @param tableName
	 * @param privateName
	 * @param type
	 * @param value
	 * @return
	 */
	public String getValue2(String tableName,String privateName,String type,String value) {
		Map<String,String> dict = dictMaps2.get(type);
		if(null != dict) {
			String code = dict.get(value);
			if (null != code) {
				return code;
			} else {
				return dict.get("其他");
			}
		}
		return null;
	}
	
	/**
	 * 取出其他选项的code  若有~
	 * @param type
	 * @return
	 */
	public String getValueOther(String type) {
		Map<String, String> dict = dictMaps2.get(type);
		if (null != dict) {
			return dict.get("其他");
		}
		return null;
	}
	
	/**
	 * 获取相似度最高的字典值
	 *
	 * @param tableName
	 * @param privateName
	 * @param type
	 * @param value
	 * @return
	 * @author 無
	 * @date 2018年6月4日 上午10:13:49
	 */
	public String getMaxSimilarityDictCode(String tableName,String privateName,String type,String value) {
		Map<String,String> dict = dictMaps.get(type);
		/** 根据value进行相似度匹配，返回key */
		if(null != dict) {
			/**value分词*/
//			System.out.println("分词1="+value);
			Vector<String> strs1 = CheckTheSame.participle(value);
			Vector<String> strs2 = null;
			/** 相似度最高的Key */
			String maxSimilarityKey = "";
			/** 相似度值-最高的 */
			double similarity = 0;
			/** 相似度值-临时 */
			double similarityTemp = 0;
			for (String key : dict.keySet()) {
				/**字典值分词*/
//				System.out.println("分词2="+dict.get(key));
				strs2 = CheckTheSame.participle(dict.get(key));
				try {
					/**比较相似度-百分比*/
					similarityTemp = IKAnalyzerUtil.getSimilarity(strs1, strs2);
//					System.out.println("相似度：" + key + "=" + similarityTemp);
					/**完全匹配 直接返回*/
					if(similarityTemp==1.0){
						return key;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				/** 保存最高相似度 */
				if (similarityTemp > similarity) {
					similarity = similarityTemp;
					maxSimilarityKey = key;
					similarityTemp = 0;
				}
			}
			if (similarity > 0 && StringUtil.isNotBlank(maxSimilarityKey)) {
//				System.out.println("最高相似度：" + maxSimilarityKey + "=" + dict.get(maxSimilarityKey));
				return maxSimilarityKey;
			}
			return null;
		}else {
			if(Convent.getIsCheck()) {
				logger.error("未找到表：["+tableName+"]["+ privateName +"] 对应的字典表："+ type);
			}
			return null;
		}
	}

	
	public Map<String, String> getDictMap(String type){
		return dictMaps.get(type);
	}
	
	public Map<String, String> getDictMap2(String type){
		return dictMaps2.get(type);
	}
	
	public Map<String, Map<String,String> > getDictMap(){
		return dictMaps;
	}
	
	public Map<String, Map<String,String> > getDictMap2(){
		return dictMaps2;
	}
	
	public void init() {
		if (null != file) {
			try {
				if (!file.exists()) {
					return;
				}
				try {
					Workbook wb = WorkbookFactory.create(file);
					int sheetNum = wb.getNumberOfSheets();
					Sheet sheet = null;
					for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {// 遍历sheet(index
																					// 0开始)
						sheet = wb.getSheetAt(sheetIndex);
						Row row = null;
						String sheetName = sheet.getSheetName();
						int firstRowNum = sheet.getFirstRowNum();
						int lastRowNum = sheet.getLastRowNum();
						String type = "";
						String name = "";
						for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {// 遍历row(行
																								// 0开始)
							row = sheet.getRow(rowIndex);
							if (null != row) {
								int firstCellNum = row.getFirstCellNum();
								int lastCellNum = row.getLastCellNum();
								// 遍历cell（列0开始）
								for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {
									Cell cell = row.getCell(cellIndex, Row.RETURN_BLANK_AS_NULL);
									if (null != cell) {
										/** 全部以字符串方式获取 */
//										System.out.println(cell.getRichStringCellValue());
//										String val = cell.getRichStringCellValue().getString();
										 String val = "";
										 switch (cell.getCellType()) {
										 case Cell.CELL_TYPE_STRING:{
										 val = cell.getRichStringCellValue().getString();
										 break;
										 }
										 case Cell.CELL_TYPE_NUMERIC:{
										 Object o = cell.getNumericCellValue();
										 val = o.toString();
										 break;
										 }
										 }
										if (null != val && !"".equals(val)) {
											val = val.trim();
										}
										String code = "";
										String value = "";
										if (rowIndex == 0 && cellIndex == 1) {
											type = val;
										}
										name = sheetName;
										if (StringUtil.isNotBlank(type) && StringUtil.isNotBlank(name) && cellIndex == 3) {
											Cell cell2 = row.getCell(cellIndex + 1, Row.RETURN_BLANK_AS_NULL);
											if (null != cell2) {
												String val2 = cell2.getRichStringCellValue().getString();
												if (null != val2 && !"".equals(val2)) {
													val2 = val2.trim();
												}
												code = val;
												value = val2;
												Map<String, String> dict = dictMaps.get(type);
												if (null == dict) {
													dict = new HashMap<>();
													dictMaps.put(type, dict);
												}
												Map<String, String> dict2 = dictMaps2.get(type);
												if (null == dict2) {
													dict2 = new HashMap<>();
													dictMaps2.put(type, dict2);
												}
												if (com.kasite.core.common.util.StringUtil.isNotBlank(code)) {
													dict.put(code, value);
													dict2.put(value, code);
												}
												// sbf.append("<dic type=\""+
												// type +"\" code=\""+ code +"\"
												// value=\""+value+"\"
												// name=\""+name+"\"/>");
											}
										}

									} else {
										// System.out.println("***");
									}
								} // end cell
							} else {
								// row is null
							}
						} // end row
					} // end sheet
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static synchronized DictBus1_5 getInstall() {
		if(null == install) {
			install = new DictBus1_5();
		}
		return install;
	}
	
}
