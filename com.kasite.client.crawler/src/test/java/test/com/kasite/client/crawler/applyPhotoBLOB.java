package test.com.kasite.client.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import oracle.sql.BLOB;

public class applyPhotoBLOB {
 final static String sDBDriver = "oracle.jdbc.driver.OracleDriver";
 
 
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  Connection connSDC = null;
  Connection conn = null;
  String sConnStrSDC = "jdbc:oracle:thin:@172.18.20.163:1521:basic";
  String sDBUidSDC = "yygh";
  String sDBPwdSdc = "jkzl123456";
  
  try
  {
   applyPhotoBLOB apply = new applyPhotoBLOB();
   connSDC = apply.getConn(sConnStrSDC,sDBUidSDC,sDBPwdSdc);

   if(connSDC!=null)
   {
    apply.testBOLB(connSDC);
   }
   
   System.out.println("处理完成！");
  }
  catch(Exception e)
  {
   System.out.println(e.getMessage());
  }
  finally
     {
     try
     {
      if(conn!=null) conn.close();
      if(connSDC!=null) connSDC.close();
     }
     catch(Exception e)
     {
      System.out.println(e.getMessage());
     }
     }
 }

 

 public void testBOLB(Connection conn) throws Exception
 {
  String strSQL = "Insert Into HDSD00_09_01(HDSD00_09_085,DATA) Values('ZY010000153475',empty_blob())";
  updateTable1(strSQL,conn);
  conn.setAutoCommit(false);
  strSQL = "Select DATA from HDSD00_09_01 where HDSD00_09_085='ZY010000153475' For Update";
  
  Statement stmt = null;
  ResultSet rs = null;
  stmt = conn.createStatement();
  rs = stmt.executeQuery(strSQL);

  rs.next();
  BLOB blob = (BLOB) rs.getBlob("DATA");
  OutputStream os = blob.getBinaryOutputStream();// 建立输出流
  BufferedOutputStream output = new BufferedOutputStream(os);
  BufferedInputStream input = new BufferedInputStream(new File("C:/Users/無/Desktop/新建文件夹/sys.xml").toURL().openStream());
  byte[] buff = new byte[2048000];  //用做文件写入的缓冲
  int bytesRead;
  while(-1 != (bytesRead = input.read(buff, 0, buff.length)))
  {
     output.write(buff, 0, bytesRead);
     //System.out.println(bytesRead);
  }

  output.close();
  input.close();
  rs.close();
  conn.commit();
  conn.setAutoCommit(true);
  stmt.close();
 }

 

 private int updateTable1(String strSQL,Connection conn) throws Exception
 {
    PreparedStatement stmt = null;
    int result = 0;
    try
    {
    
     stmt = conn.prepareStatement(strSQL);
           result = stmt.executeUpdate();
    }
    catch(Exception e)
    {
     throw new Exception(e.getMessage());
    }
    finally
    {
   stmt.close();
    }
    return result ;
 }

 

    public Connection getConn(String StrConn,String uid,String pwd) throws Exception
    {
    Connection conn = null;
    try
    {
     Class.forName(sDBDriver);
     conn = DriverManager.getConnection(StrConn,uid,pwd);
    }
    catch (Exception e)
    {
     throw new Exception(e.getMessage());
    }
    return conn;
    }

    public static String ConvertBLOBtoString(oracle.sql.BLOB BlobContent)  
    {  
       byte[] msgContent= BlobContent.getBytes(); //BLOB转换为字节数组  
      
       byte[] bytes;       //BLOB临时存储字节数组  
       String newStr = ""; //返回字符串  
       int i=1;            //循环变量  
       long BlobLength;    //BLOB字段长度  
       try        
       {  
         BlobLength=BlobContent.length();  //获取BLOB长度  
         if (msgContent == null || BlobLength==0)  //如果为空，返回空值  
         {  
           return "";  
         }  
         else                              //处理BLOB为字符串  
        {  
           /* 
           while(i<BlobLength)             //循环处理字符串转换，每次1024；Oracle字符串限制最大4k 
           { 
             bytes= BlobContent.getBytes(i,1024) ; 
             i=i+1024; 
             newStr = newStr+new String(bytes,"gb2312" ;          
           } 
           */  
           newStr = new String(BlobContent.getBytes(1,900),"gb2312" +"....");  //简化处理，只取前900字节  
           return newStr;  
         }  
       }  
       catch(Exception e)     //oracle异常捕获  
       {  
         e.printStackTrace();  
       }       
       return newStr;  
    }  

}