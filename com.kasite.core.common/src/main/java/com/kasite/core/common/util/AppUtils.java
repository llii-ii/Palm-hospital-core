package com.kasite.core.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import com.kasite.core.common.config.KasiteConfig;

public class AppUtils {
	/**
		 * 获取进程编号
		 * @return
		 */
		public static String getProcId(){
			RuntimeMXBean rtmxb = ManagementFactory.getRuntimeMXBean();
			String procName=rtmxb.getName();
			if(procName==null){
				return null;
			}
			int index=procName.indexOf("@");
			return index>0?procName.substring(0, index):procName;
		}
		
		public static void main(String[] args) {
			KasiteConfig.print(getProcId());
		}
}

