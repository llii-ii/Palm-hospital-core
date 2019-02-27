package com.kasite.core.common.sys.info;


/**
 * @author chenw
 *
 */
public  class StatisticsProperty implements java.io.Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	public long getErrorCount() {
//		return errorCount;
//	}
//	public void setErrorCount(long errorCount) {
//		this.errorCount = errorCount;
//	}
//	private long errorCount;
	private long maxActiveTime;
	public long getMaxActiveTime() {
		return maxActiveTime;
	}
//	private long overtime;
//	
//	public long getOvertime() {
//		return overtime;
//	}
//	public void setOvertime(long overtime) {
//		this.overtime = overtime;
//	}
	public void setMaxActiveTime(long maxActiveTime) {
		this.maxActiveTime = maxActiveTime;
	}
	private long maxActive;
	public long getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(long maxActive) {
		this.maxActive = maxActive;
	}
	private long actives;
	public long getActives() {
		return actives;
	}
	public void setActives(long actives) {
		this.actives = actives;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	private long total;
//	 
//	 private static ServerStatistics instance = null;
//	 private static final Object lock = new Object();
//	 //private static  ConcurrentHashMap<String, List<ServerMethodRunPerformance>> methodRunMap;	
//	 private static List<ServerMethodRunPerformance> methodRunList;
//	 //protected static  ConcurrentHashMap<String, ServerInterfaceRunPerformance> interfaceRunMap;
//
//	 public ServerMethodRunPerformance[]  getMethodRunPerformance()
//	 {
//		 
//		// List<ServerMethodRunPerformance> list= methodRunMap.get(remoteInterface);
//		 if(methodRunList!=null)
//		 {
//			 ServerMethodRunPerformance[] ret= methodRunList.toArray(new ServerMethodRunPerformance[methodRunList.size()]);
//			 methodRunList.clear();
//			 return ret;
//		 }
//		 return null;
//	 }
//	// public ServerInterfaceRunPerformance[]  getInterfaceRunPerformance()
//	// {
//		// return interfaceRunMap.values().toArray(new ServerInterfaceRunPerformance[interfaceRunMap.size()]);
//	// }
//	// public int getInterfaceMapSize()
//	// {
//		// return interfaceRunMap.size();
//	// }
//	// public int getMethodMapSize()
//	 //{
//	//	 return methodRunMap.size();
//	// }
//	 public void add(ServerInvokeResult r)
//	 {
//		//ServerRunner.getInstance().remove(r.getReq().getReqId());
//		///if(!r.getReq().getRemoteInterfaceClassName().equals(com.coreframework.log.LogStandard.class.getName()))
//		// {
//			// Logger.get().info("RPC_Response",r.toString());
//		 //}
//		 //if(r.getReq().getRemoteInterfaceClassName().equals(com.coreframework.remoting.standard.Standard.class.getName()))
//		 //{
//			// return;
//		// }
//		 
//		 //处理接口
//		// ServerInterfaceRunPerformance interfaceRunPerformance=interfaceRunMap.get(r.getReq().getRemoteInterfaceClassName());
//		// if(interfaceRunPerformance==null)
//		 //{
//		//	 interfaceRunMap.putIfAbsent(r.getReq().getRemoteInterfaceClassName(),new ServerInterfaceRunPerformance());
//		//	 interfaceRunPerformance=interfaceRunMap.get(r.getReq().getRemoteInterfaceClassName());
//		// }
//		 //interfaceRunPerformance.setServerInterface(r.getReq().getRemoteInterfaceClassName());
//		 //interfaceRunPerformance.addUrl(r.getUrl());
//		 //interfaceRunPerformance.getInvokeTotal().incrementAndGet();
//		 
//		 //处理方法
//		// String key=r.getReq().getRemoteInterfaceClassName();
//		 //List<ServerMethodRunPerformance> methodRunPerformanceList=methodRunMap.get(key);
//		 ServerMethodRunPerformance current=new ServerMethodRunPerformance(r.getReq().getRemoteInterfaceClassName()+"."+r.getReq().getMethodName());
//		 current.setSourceIp(r.getUrl().getIp());
//		 int x=methodRunList.indexOf(current);
//		 if(x==-1)
//		 {
//			 methodRunList.add(current);
//		 }
//		 x=methodRunList.indexOf(current);
//		 current=methodRunList.get(x);
//		 current.getTotalResponse().incrementAndGet();
//		 if(r.getInvoke_mills()>=0)
//		 {
//			 if(r.isSuccess())
//			 {
//				 if(current.getMaxInvokeMillsValue()<r.getInvoke_mills())
//				 {
//					 current.setMaxInvokeMillsValue(r.getInvoke_mills());
//				 }
//				 if(current.getMinInvokeMillsValue()<=0)
//				 {
//					 current.setMinInvokeMillsValue(r.getInvoke_mills());
//				 }
//				 else
//				 {
//					 if(current.getMinInvokeMillsValue()>r.getInvoke_mills())
//					 {
//						 current.setMinInvokeMillsValue(r.getInvoke_mills());
//					 }
//				 }
//			 }
//			 current.getTotalInvokeMills().addAndGet(r.getInvoke_mills());
//		 }
//		 if(!r.isSuccess())
//		 {
//			 current.getTotalFail().incrementAndGet();
//		 }
//		 if(r.getInvoke_mills()>5000)
//		 {
//			 Logger.get().info("5秒以上的响应",r.getReq().getMethodName(),new LogBody().set("req"
//					 ,r.getReq()));
//		 }
//		 /*
//		 if(methodRunPerformanceList==null)
//		 {
//			 methodRunPerformanceList=new ArrayList<ServerMethodRunPerformance>();
//			 methodRunPerformanceList.add(current);
//			 methodRunMap.putIfAbsent(key,methodRunPerformanceList);
//			 methodRunPerformanceList=methodRunMap.get(key);
//		 }
//		 else
//		 {
//			 int x=methodRunPerformanceList.indexOf(current);
//			 if(x==-1)
//			 {
//				 methodRunPerformanceList.add(current);
//			 }
//		 }
//		 int index=methodRunPerformanceList.indexOf(current);
//		 if(index==-1)
//		 {
//			 //KasiteConfig.print("未找到"+current.getServerInterface()+"."+current.getMethodName());
//			 return;
//		 }
//		 current=methodRunPerformanceList.get(index);
//		 current.getTotalResponse().incrementAndGet();
//		 if(r.getInvoke_mills()>=0)
//		 {
//			
//			 if(current.getMaxInvokeMillsValue()<r.getInvoke_mills())
//			 {
//				 current.setMaxInvokeMillsValue(r.getInvoke_mills());
//			 }
//			 current.getTotalInvokeMills().addAndGet(r.getInvoke_mills());
//			 //KasiteConfig.print(r.getRpc_mills());
//			// current.getTotalRpcMills().addAndGet(r.getRpc_mills());
//		 }
//		 if(!r.isSuccess())
//		 {
//			 current.getTotalFail().incrementAndGet();
//		 }
//		 */
//	 }
//	 private ServerStatistics(){
//		 
//		 methodRunList = new java.util.Vector<ServerMethodRunPerformance>();	
//		 //interfaceRunMap= new ConcurrentHashMap<String, ServerInterfaceRunPerformance>();	
//		 		
//	 }
//	 public static ServerStatistics getInstance() 
//	 {
//		 synchronized (lock) {
//			 
//			 if(instance == null) {
//                 instance = new ServerStatistics();
//
//			 }
//			 return instance;
//			
//		}
//	  }
//

}
