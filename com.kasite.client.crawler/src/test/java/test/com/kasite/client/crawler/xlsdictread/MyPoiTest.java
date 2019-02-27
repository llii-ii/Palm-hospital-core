package test.com.kasite.client.crawler.xlsdictread;
import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.modules.utils.FileOper;
 
 
/**
 * POI：支持xls/xlsx文件格式按cell类型解析相关内容
 * 1.支持xls和xlsx文件格式的解析（exls 2003/2007 兼容）
 * 2.遍历sheet总数
 * 3.遍历row总数
 * 4.遍历cell总数
 * 5.可以判断常见数据类型
 * 6.日期格式化显示
 * @author Administrator
 * 
 */
public class MyPoiTest {
    public void getMyXLS() { 
//      ArrayList<Map<String,Object>> xlsMapList = new ArrayList<Map<String,Object>>();
    		String path = "/Users/daiyanshui/Desktop/上饶/";
    		String xlsFilePath = path + "健康之路字典V1.50.xls";
    		String outFilePath = path +"dict_1_5.xml";
        File xlsOrxlsxFile = new File(xlsFilePath);
        if(!xlsOrxlsxFile.exists()){
            return ;
        }
        try { 
        	    StringBuffer sbf = new StringBuffer("<dict>");
            Workbook wb = WorkbookFactory.create(xlsOrxlsxFile);
            int sheetNum = wb.getNumberOfSheets();
            Sheet sheet = null;
            for(int sheetIndex = 0;sheetIndex<sheetNum;sheetIndex++){//遍历sheet(index 0开始)
                sheet = wb.getSheetAt(sheetIndex);
                Row row = null;
                String sheetName = sheet.getSheetName();
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                String type = "";
                String name = "";
                for (int rowIndex = firstRowNum;rowIndex<=lastRowNum;rowIndex++ ) {//遍历row(行 0开始)
                    row = sheet.getRow(rowIndex);
                    if(null != row){
                        int firstCellNum = row.getFirstCellNum();
                        int lastCellNum = row.getLastCellNum();
                        for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {//遍历cell（列 0开始）
                            Cell cell = row.getCell(cellIndex, Row.RETURN_BLANK_AS_NULL);
                            if (null != cell) {
                                Object cellValue = null;//cellValue的值
                             	String val = "";
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
	                           
	                            	String code = "";
	                            	String value = "";
	                            	
	                            	if(rowIndex == 0 && cellIndex == 1) {
	                            		type = val;
	                            	}
	                        		name = sheetName;
	                            	if(StringUtil.isNotBlank(type)&&StringUtil.isNotBlank(name) && cellIndex == 3) {
	                            		 Cell cell2 = row.getCell(cellIndex + 1, Row.RETURN_BLANK_AS_NULL);
	                                  if (null != cell2) {
	                                		String val2 = cell2.getRichStringCellValue()
	                                                .getString();
	                                		code = val;
	                                		value = val2;
	                                		sbf.append("<dic type=\""+ type +"\" code=\""+ code +"\" value=\""+value+"\" name=\""+name+"\"/>");
	                                  }
	                            	}
                                
//                                switch (cell.getCellType()) {
//                                case Cell.CELL_TYPE_STRING:{
//                                	// type = 0,1 = CV04.10.015  name = 1,1 = 足背动脉搏动代码    
//                                 // cell = 3,4  code value
//                                	String val = cell.getRichStringCellValue()
//                                            .getString();
//                                	String code = "";
//                                	String value = "";
//                                	
//                                	if(rowIndex == 0 && cellIndex == 1) {
//                                		type = val;
//                                	}
//                            		name = sheetName;
//                                	if(StringUtil.isNotBlank(type)&&StringUtil.isNotBlank(name) && cellIndex == 3) {
//                                		 Cell cell2 = row.getCell(cellIndex + 1, Row.RETURN_BLANK_AS_NULL);
//                                      if (null != cell2) {
//                                    		String val2 = cell2.getRichStringCellValue()
//                                                    .getString();
//                                    		code = val;
//                                    		value = val2;
//                                    		System.out.println("<dic type=\""+ type +"\" code=\""+ code +"\" value=\""+value+"\" name=\""+name+"\"/>");
//                                    		
//                                      }
//                                	}
//                                break;
//                                }
//                                case Cell.CELL_TYPE_NUMERIC:
//                                    if (DateUtil.isCellDateFormatted(cell)) {
//                                        System.out.println(cell.getDateCellValue());
//                                        cellValue= cell.getDateCellValue();
//                                        //TODO 可以按日期格式转换
//                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//                                        String time = formatter.format(cellValue);
//                                        System.out.println("formater time:"+time);
//                                    } else {
//                                        System.out.println(cell.getNumericCellValue());
//                                        cellValue=cell.getNumericCellValue();
//                                    }
//                                    break;
//                                case Cell.CELL_TYPE_BOOLEAN:
//                                    System.out.println(cell.getBooleanCellValue());
//                                    cellValue = cell.getBooleanCellValue();
//                                    break;
//                                case Cell.CELL_TYPE_FORMULA:{
//	                                	String val = cell.getRichStringCellValue()
//	                                    .getString();
//			                        	String code = "";
//			                        	String value = "";
//			                        	
//			                        	if(rowIndex == 0 && cellIndex == 1) {
//			                        		type = val;
//			                        	}
//			                    		name = sheetName;
//			                        	if(StringUtil.isNotBlank(type)&&StringUtil.isNotBlank(name) && cellIndex == 3) {
//			                        		 Cell cell2 = row.getCell(cellIndex + 1, Row.RETURN_BLANK_AS_NULL);
//			                              if (null != cell2) {
//			                            		String val2 = cell2.getRichStringCellValue()
//			                                            .getString();
//			                            		code = val;
//			                            		value = val2;
//			                            		System.out.println("<dic type=\""+ type +"\" code=\""+ code +"\" value=\""+value+"\" name=\""+name+"\"/>");
//			                            		
//			                              }
//			                        	}
//			                        break;
//		                        }
//                                default:
//                                    System.out.println("not find match type.");
//                                }
                               // System.out.println("value:"+cellValue);
                            } else {
                                //TODO cell is null 用 *** 代替输出
//                                System.out.println("***");
                            }
                        }//end cell
                    }else{
                        //TODO row is null
                    }
                }//end row
            }//end sheet
            sbf.append("</dict>");
            FileOper.write(outFilePath, sbf.toString());
            
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}