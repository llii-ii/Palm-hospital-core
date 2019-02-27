package com.kasite.core.common.sys.info;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.DateOper;
import com.sun.management.OperatingSystemMXBean;


public class SystemInfoUtil {
    private static SystemInfoUtil instance;
    private SystemInfoUtil (){
    }
    public static SystemInfoUtil getInstance(){
    	if(null == instance){
    		instance = new SystemInfoUtil();
    		//startGetSysInfoThread();
    	}
    	return instance;
    }
    
    
    /**java虚拟机的基础信息*/
    public JvmInfo getJvmInfo(){
    	JvmInfo info = new JvmInfo();
    	//内存池
    	List<MemoryPoolMXBean> mpmxbs = ManagementFactory.getMemoryPoolMXBeans();
    	for (MemoryPoolMXBean mpmxb : mpmxbs) {
    		if(mpmxb.getName().equals("Eden Space") || mpmxb.getName().equals("PS Eden Space")){
    			MemoryUsage musage = mpmxb.getUsage();
    			Usage usage = new Usage();
    			usage.setCommitted(musage.getCommitted());
    			usage.setInit(musage.getInit());
    			usage.setMax(musage.getMax());
    			usage.setUsed(musage.getUsed());
    			info.setEdenSpace(usage);
    			continue;
    		}
    		if(mpmxb.getName().equals("Tenured Gen") || mpmxb.getName().equals("PS Old Gen")){
    			MemoryUsage musage = mpmxb.getUsage();
    			Usage usage = new Usage();
    			usage.setCommitted(musage.getCommitted());
    			usage.setInit(musage.getInit());
    			usage.setMax(musage.getMax());
    			usage.setUsed(musage.getUsed());
    			info.setTenuredGen(usage);
    			continue;
    		}
    		if(mpmxb.getName().equals("Perm Gen") || mpmxb.getName().equals("PS Perm Gen")){
    			MemoryUsage musage = mpmxb.getUsage();
    			Usage usage = new Usage();
    			usage.setCommitted(musage.getCommitted());
    			usage.setInit(musage.getInit());
    			usage.setMax(musage.getMax());
    			usage.setUsed(musage.getUsed());
    			info.setPermGen(usage);
    			continue;
    		}
		}
    	//堆大小的最大值HeapMemoryUsage  max 分配的内存committed 当前堆大小used
    	//堆信息
    	MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
    	MemoryUsage musage = memory.getHeapMemoryUsage();
    	Usage usage = new Usage();
		usage.setCommitted(musage.getCommitted());
		usage.setInit(musage.getInit());
		usage.setMax(musage.getMax());
		usage.setUsed(musage.getUsed());
		info.setHeapMemoryUsage(usage);
		
		//非堆信息
		musage = memory.getNonHeapMemoryUsage();
		usage = new Usage();
		usage.setCommitted(musage.getCommitted());
		usage.setInit(musage.getInit());
		usage.setMax(musage.getMax());
		usage.setUsed(musage.getUsed());
		info.setNonHeapMemoryUsage(usage);
		
		RuntimeMXBean rtmxb = ManagementFactory.getRuntimeMXBean();
		//系统运行时间 Uptime
		long timespan = rtmxb.getUptime();
		
		info.setUptime(timespan);
		
		info.setName(rtmxb.getName());
		//线程信息
		ThreadMXBean tbean = ManagementFactory.getThreadMXBean();
		int threadCount = tbean.getThreadCount();
		
		info.setThreadCount(threadCount);
		
    	return info;
    }
    
    public static void getThreadAll() {
    	ManagementFactory.getThreadMXBean().getThreadCount();
//    	clientStatus.setThreadCount(ManagementFactory.getThreadMXBean().getThreadCount())
//    	ThreadGroup group = Thread.currentThread().getThreadGroup();  
//    	ThreadGroup topGroup = group;  
//    	// 遍历线程组树，获取根线程组  
//    	while (group != null) {  
//    	    topGroup = group;  
//    	    group = group.getParent();  
//    	}  
//    	// 激活的线程数加倍  
//    	int estimatedSize = topGroup.activeCount() * 2;  
//    	Thread[] slackList = new Thread[estimatedSize];  
//    	// 获取根线程组的所有线程  
//    	int actualSize = topGroup.enumerate(slackList);  
//    	// copy into a list that is the exact size  
//    	Thread[] list = new Thread[actualSize];  
//    	System.arraycopy(slackList, 0, list, 0, actualSize);  
//    	KasiteConfig.print("Thread list size == " + list.length);  
//    	for (Thread thread : list) {  
//    	   
//    	} 

    }
    
    
    @SuppressWarnings("restriction")
	private static SysInfo getsysinfo(){
    	SysInfo sysInfo = new SysInfo();
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		//RuntimeMXBean rtmxb = ManagementFactory.getRuntimeMXBean();
		/*long systime = System.currentTimeMillis();
		if(systime - readtime > 60*1000*20){
			cpuRatioForWindows = getCpuRatioForWindows();
			readtime = System.currentTimeMillis();
		}*/
		//(总的物理内存 + 虚拟内存)
		long totalSwapSpaceSize = osmxb.getTotalSwapSpaceSize();
		//系统物理内存
		long totalPhysicalMemorySize = osmxb.getTotalPhysicalMemorySize();
		//剩余的物理内存
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
		//分配的虚拟内存
		long committedVirtualMemorySize = osmxb.getCommittedVirtualMemorySize();
		
		//内存使用百分比
		Double compare=(Double)(1-freePhysicalMemorySize*1.0/totalSwapSpaceSize)*100;
		String time = DateOper.getNowDateTime().toString();
		
		sysInfo.setTotalPhysicalMemorySize(totalPhysicalMemorySize);
		sysInfo.setTotalSwapSpaceSize(totalSwapSpaceSize);
		sysInfo.setMemoryPoint(compare.intValue());
		sysInfo.setCommittedVirtualMemorySize(committedVirtualMemorySize);
		sysInfo.setFreePhysicalMemorySize(freePhysicalMemorySize);
		//sysInfo.setUptime(formatTimeSpan(timespan));
		sysInfo.setTotalSwapSpaceSize(totalSwapSpaceSize);
//		sysInfo.setCpuUsePoint(cpuRatioForWindows);
		sysInfo.setName(osmxb.getName()+ " " +osmxb.getArch());
		sysInfo.setTime(time);
		sysInfo.setDiskInfos(getDiskInfos());
		return sysInfo;
    }
    /**获取操作系统基础信息*/
	public SysInfo getSysInfo(){
//		if(sysInfo == null){
//			sysInfo = getsysinfo();
//		}
		return getsysinfo();
//		return sysInfo;
	}
	/**获取系统磁盘信息*/
	private static DiskInfo[] getDiskInfos(){
		 // 操作系统
		List<DiskInfo> list = new ArrayList<DiskInfo>();
//		  List<String> list=new ArrayList<String>();
		  for (char c = 'A'; c <= 'Z'; c++) {
		   String dirName = c + ":/";
		   File win = new File(dirName);
		         if(win.exists()){
		          long total=(long)win.getTotalSpace();
		          long free=(long)win.getFreeSpace();
		          Double compare=(Double)(1-free*1.0/total)*100;
		         /* String str=c+":盘 "+ total/1024/1024/1024 +"G  剩余:"+ free/1024/1024/1024 + "G 已使用" +compare.intValue()+"%";
		          KasiteConfig.print(str);*/
		          DiskInfo info = new DiskInfo();
		          info.setDiskname(String.valueOf(c));
		          info.setFreeSize(free);
		          info.setTotalSize(total);
		          info.setUsedPoint(compare.intValue());
		          list.add(info);
		         }
		     }
		  return list.toArray(new DiskInfo[list.size()]);
	}
 
}
