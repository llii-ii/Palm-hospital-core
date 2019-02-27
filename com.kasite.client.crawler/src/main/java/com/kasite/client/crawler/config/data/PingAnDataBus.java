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
import com.kasite.client.crawler.config.data.vo.PingAnPkVo;

/**
 * 平安健康配置.xls 的解析方法
 * @author daiyanshui
 *
 */
public class PingAnDataBus {
	private static final Logger logger = LoggerFactory.getLogger(PingAnDataBus.class);
	public static PingAnDataBus install;
	private static File file;
	private String filePath;
	//存每个exls 中 sheet 对应的表： key = 表名   value = map[ 字段，字段描述]
	private Map<String, Map<String,PingAnPkVo> > dictMaps = new HashMap<>();
	
	private PingAnDataBus() {
		this.filePath= System.getProperty("user.dir")+ File.separator + "standard/dev/pingan.xlsx";//Convent.getPingAnFilePath();
		if(file == null) {
			file = new File(filePath);
			logger.info("加载数据配置文件："+ filePath);
			if(!file.exists() || !file.isFile()) {
				logger.error("数据表文件加载异常。请核实文件是否存在："+filePath);
				return;
			}
		}
		init();
//		FileWatchData1_5.getInstall(this).start();
	}
	
	public static void main(String[] args) {
		PingAnDataBus.getInstall();
	}
	
	public String getFilePath() {
		return filePath;
	}

	public PingAnDataBus setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	/**
	 * 获取指定表的指定字段的字段描述
	 * @param type
	 * @param code
	 * @return
	 */
	public PingAnPkVo getValue(String type,String code) {
		Map<String,PingAnPkVo> dict = dictMaps.get(type);
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
	public Map<String,PingAnPkVo> getData15Map(String tableName){
		Map<String,PingAnPkVo>  map = dictMaps.get(tableName);
		if(null == map) {
			logger.error("未查到对应的表，请确认标准文件中是否配置错误："+tableName);
			return null;
		}
		return map;
	}

	public String getValue(Row row,int cellIndex) {
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
	            		if(o instanceof Double) {
	            			val = ((Double)o).intValue()+"";
	            			return val;
	            		}
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
//			                StringBuffer sbf = new StringBuffer();
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
			                        			String no = getValue(row, 0);//	
			                					String dictName = getValue(row,1);//名称
			                					String privateName = getValue(row, 2);//字段类型	
			                					String privateType = getValue(row, 3);//字段属性
			                					String isNotNull = getValue(row, 4);//是否非空
			                					String privateDes = getValue(row, 5);//备注
			                					String dataType = getValue(row, 6);//内部字段映射
			                					String isKey = getValue(row, 7);//扩展主键
			                					PingAnPkVo vo = new PingAnPkVo();
			                					vo.setInQueryPrivate(dataType);
			                					vo.setIsNull(isNotNull);
			                					vo.setName(privateName);
			                					vo.setPrivateType(privateType);
			                					vo.setRemark(privateDes);
			                					vo.setNo(no);
			                					vo.setKey(isKey);
			                					
//			                					String headIndex = getValue(row,8);
			                					
			                					if(StringUtil.isNotBlank(dictName) && dictName.equals("-")) {
			                						dictName = null;
			                					}
//			                					sbf.append("pname=").append(privateName).append("\t");
//			                					sbf.append("privateType=").append(privateType).append("\t");
//			                					sbf.append("isKey=").append(isKey).append("\t");
//			                					sbf.append("isNotNull=").append(isNotNull).append("\t");
//			                					sbf.append("privateDes=").append(privateDes).append("\t");
//			                					sbf.append("dictName=").append(dictName).append("\t");
//			                    				sbf.append("\r\n");
		                                		Map<String,PingAnPkVo> dict = dictMaps.get(type);
		                    					if(null == dict) {
		                    						dict = new HashMap<>();
		                    						dictMaps.put(type, dict);
		                    					}
		                    					String key = privateName;
		                    					if(com.kasite.core.common.util.StringUtil.isNotBlank(key)) {
		                    						dict.put(key, vo);
		                    					}
			                    			}
			                        }
			                    }
			                }
			            }//end sheet
			            
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
	
	
	public static synchronized PingAnDataBus getInstall() {
		if(null == install) {
			install = new PingAnDataBus();
		}
		return install;
	}
	
}
