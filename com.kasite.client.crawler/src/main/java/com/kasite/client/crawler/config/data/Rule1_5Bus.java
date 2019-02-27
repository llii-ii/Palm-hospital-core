package com.kasite.client.crawler.config.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.kasite.client.crawler.config.data.vo.Data15PkVo;

/**
 * 健康档案数据集.xls的采集规则 解析方法
 * 
 * @author 無
 *
 */
public class Rule1_5Bus {
	private static final Logger logger = LoggerFactory.getLogger(Rule1_5Bus.class);
	public static Rule1_5Bus install;
	private static File file;
	private String filePath;
	//存每个exls 中 sheet 对应的表： key = 表名   value = map[ 字段，字段描述]
	private Map<String, Map<String,Data15PkVo> > ruleMaps = new HashMap<>();
	
	private Rule1_5Bus() {
		this.filePath= System.getProperty("user.dir")+ File.separator + Convent.getSysRuleFilePath();
		if(file == null) {
			file = new File(filePath);
			logger.info("加载规则配置文件："+ filePath);
			if(!file.exists() || !file.isFile()) {
				logger.error("数据表文件加载异常。请核实文件是否存在："+filePath);
				return;
			}
		}
		init();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public Rule1_5Bus setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	/**
	 * 获取指定表的指定字段的字段描述
	 * @param type
	 * @param code
	 * @return
	 */
	public Data15PkVo getValue(String type,String code) {
		Map<String,Data15PkVo> dict = ruleMaps.get(type);
		if(null != dict) {
			return dict.get(code);
		}else {
			return null;
		}
	}
	/**
	 * 获取指定表的 表结构
	 * @param tableName
	 * @return
	 */
	public Map<String,Data15PkVo> getData15Map(String tableName){
		Map<String,Data15PkVo>  map = ruleMaps.get(tableName);
		if(null == map) {
			logger.error("未查到对应的表，请确认标准文件中是否配置错误："+tableName);
			return null;
		}
		return map;
	}

	public static String getValue(Row row,int cellIndex) {
		Cell cell = row.getCell(cellIndex, Row.RETURN_BLANK_AS_NULL);
		String val = "";
		if(null != cell) {
			switch (cell.getCellType()) {
	            case Cell.CELL_TYPE_STRING:{
	                	val = cell.getRichStringCellValue()
	                     .getString();
	            	break;
	            }
	            case Cell.CELL_TYPE_NUMERIC:{
	            		Object o = cell.getNumericCellValue();
	            		val = o.toString();
	            	break;
	            }
			}
		}
		if(null != val && !"".equals(val)) {
			val = val.trim();
		}
		return val;
	}
	
	public void init() {
		if(null != file) {
			try {
				 File xlsOrxlsxFile = file;
			        if(!xlsOrxlsxFile.exists()){
			            return ;
			        }
			        try { 
			            Workbook wb = WorkbookFactory.create(xlsOrxlsxFile);
			            int sheetNum = wb.getNumberOfSheets();
			            Sheet sheet = null;
			            String type = "";
			            for(int sheetIndex = 0;sheetIndex<sheetNum;sheetIndex++){//遍历sheet(index 0开始)
			                sheet = wb.getSheetAt(sheetIndex);
			                Row row = null;
			                int firstRowNum = sheet.getFirstRowNum();
			                int lastRowNum = sheet.getLastRowNum();
			                for (int rowIndex = firstRowNum;rowIndex<=lastRowNum;rowIndex++ ) {//遍历row(行 0开始)
			                    row = sheet.getRow(rowIndex);
			                    if(null != row){
			                        int firstCellNum = row.getFirstCellNum();
			                        int lastCellNum = row.getLastCellNum();
			                        int cellIndex = firstCellNum;
			                        if(cellIndex < lastCellNum) {
			                        		if(rowIndex == 1) {
			                        			type =  getValue(row, 1);
			                        		}
			                        		if(rowIndex > 4 ) {
			                					String privateName = getValue(row, 1);
			                					String privateType = getValue(row, 9);
			                					String privateDes = getValue(row, 3);
			                					String name = getValue(row, 4);
			                					String dataType = getValue(row, 5);
			                					String isKey = getValue(row, 11);
			                					String isNotNull = getValue(row, 12);
			                					String dictName = getValue(row,7);
			                					String headIndex = getValue(row,8);
			                					String sql = getValue(row,10);
			                					if(StringUtil.isNotBlank(dictName) && dictName.equals("-")) {
			                						dictName = null;
			                					}
		                                		Map<String,Data15PkVo> dict = ruleMaps.get(type);
		                    					if(null == dict) {
		                    						dict = new HashMap<>();
		                    						ruleMaps.put(type, dict);
		                    					}
		                    					String key = privateName;
		                    					Data15PkVo vo = new Data15PkVo();
		                    					vo.setIsKey(0);
		                    					vo.setIsNotNum(0);
		                    					vo.setDataType(dataType);
		                    					vo.setPrivateDes(privateDes);
		                    					vo.setPrivateName(privateName);
		                    					vo.setPrivateType(privateType);
		                    					vo.setPrivateDictName(dictName);
		                    					vo.setHeadIndex(headIndex);
		                    					vo.setName(name);
		                    					vo.setSql("");
		                    					if(StringUtil.isNotBlank(sql)) {
		                    						vo.setSql(sql.trim());
		                    					}
		                    					if(StringUtil.isNotBlank(isKey)) {
		                    						try {
		                    							Float f = Float.parseFloat(isKey);
				                    					vo.setIsKey(f.intValue());
		                    						}catch (Exception e) {
												}
		                    					}
		                    					if(StringUtil.isNotBlank(isNotNull)) {
		                    						try {
		                    							Float f = Float.parseFloat(isNotNull);
				                    					vo.setIsNotNum(f.intValue());
		                    						}catch (Exception e) {
												}
		                    					}
		                    					if(com.kasite.core.common.util.StringUtil.isNotBlank(key)) {
		                    						dict.put(key, vo);
		                    					}
			                    			}
			                        }
			                    }
			                }
			            }
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
	
	public static synchronized Rule1_5Bus getInstall() {
		if(null == install) {
			install = new Rule1_5Bus();
		}
		return install;
	}
	
}
