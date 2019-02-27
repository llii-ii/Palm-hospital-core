package test.com.kasite.client.crawler.xlsdictread;
import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
 
 
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
public class MyPoiTest2 {
	
	
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
		return val;
	}
	
    public void getMyXLS() { 
//      ArrayList<Map<String,Object>> xlsMapList = new ArrayList<Map<String,Object>>();
    		String path = "/Users/daiyanshui/Desktop/上饶/";
    		String xlsFilePath = path + "健康之路数据集V1.50.xls";
    		String tableName = "HDSA00_01";
        File xlsOrxlsxFile = new File(xlsFilePath);
        if(!xlsOrxlsxFile.exists()){
            return ;
        }
        try { 
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
                StringBuffer sbf = new StringBuffer();
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
                        		if(tableName.equals(type) && rowIndex > 4 ) {
                					String privateName = getValue(row, 1);
                					String privateType = getValue(row, 9);
                					String privateDes = getValue(row, 3);
                					String isKey = getValue(row, 11);
                					String isNotNull = getValue(row, 12);
                					String dictName = getValue(row,7);
                					sbf.append("pname=").append(privateName).append("\t");
                					sbf.append("privateType=").append(privateType).append("\t");
                					sbf.append("isKey=").append(isKey).append("\t");
                					sbf.append("isNotNull=").append(isNotNull).append("\t");
                					sbf.append("privateDes=").append(privateDes).append("\t");
                					sbf.append("dictName=").append(dictName).append("\t");
                    				sbf.append("\r\n");
                    			}
                        }
                    }
                }
                if(tableName.equals(type) ) {
                		System.out.println(sbf.toString());
                		break;
                }
            }//end sheet
            
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}