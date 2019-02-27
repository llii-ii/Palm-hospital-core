package com.kasite.client.hospay.module.bill.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.common.dao.MySqlEnum;
import com.kasite.client.hospay.common.dao.OracleSqlEnum;

/**
 * @author cc
 * 判断当前数据库类型，并使用相应的枚举执行SQL
 */
@Component
public class DBTypeUtils {

     /**
      * 在初始化spring容器时为静态变量赋值
      */
     public static String dbType;

     @Value("${dataBase.dbType}")
     public String type;
       
     @PostConstruct
     public void init(){
    	 //设置数据库的超时时间
    	 System.setProperty("DB_TIMEOUT", "300000");
    	 dbType = type;
    	 
     }
     
     /**
      * 根据sqlName调用相应数据的sql执行语句
      *
      * @param sqlName
      *
      * @return
      */
//     public static SqlNameEnum getDBType(String sqlName) {
//          Object daType = null;
//          if (Constant.MYSQL.equals(dbType)) {
//               //1.MySqlNameEnum
//               daType = Enum.valueOf(MySqlEnum.class, "mysql"+sqlName);
//          } else if (Constant.ORACLE.equals(dbType)) {
//               //2.OracleSqlNameEnum
//               daType = Enum.valueOf(OracleSqlEnum.class, "oracle"+sqlName);
//          }
//          return (SqlNameEnum) daType;
//     }
}
