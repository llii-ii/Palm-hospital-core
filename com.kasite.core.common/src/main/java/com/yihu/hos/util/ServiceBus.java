//package com.yihu.hos.util;
//
//import java.util.Random;
//
//import com.coreframework.db.DatabaseEnum;
//import com.coreframework.remoting.Url;
//import com.coreframework.remoting.reflect.Rpc;
//import com.coreframework.remoting.reflect.RpcException;
//import com.kasite.core.common.config.KasiteConfig;
//import com.yihu.hos.IApiModule;
//import com.yihu.hos.constant.IConstant;
//import com.yihu.wsgw.api.InterfaceStandard;
//
//import net.sf.json.JSONObject;
//
//public class ServiceBus {
//	private DatabaseEnum db;
//	private ServiceBus(DatabaseEnum db,String clientId){
//		this.db = db;
//		this.clientId = clientId;
//	}
//	
//	private static ServiceBus instance;
//	
//	public static ServiceBus getInstance(DatabaseEnum db,String clientId){
//		if(null == instance){
//			instance = new ServiceBus(db,clientId);
//		}
//		return instance;
//	}
////	private static Object _lock=new Object();
//	private String clientip;
//	private String clientId;
//	private static Random r = new Random();
//	public String call(String api ,String params,int paramType,int outType,String v)
//	{
//		
//			JSONObject authInfo = new JSONObject();
//			authInfo.put("ClientId", this.clientId);
//			authInfo.put("ClientVersion", "1");
//			authInfo.put("Sign", "");
//			authInfo.put("SessionKey", "");
//			String seq=Long.toString(System.currentTimeMillis());
//			long start=System.currentTimeMillis();
//			String result=null;
//			Url url=null;
//			String className=null;
//			String methodName=null;
//			try
//			{
//				if(api==null||api.split("\\.").length!=3)
//				{
//					result= getRetVal(outType,20000,"api名称格式错误");
//					return result;
//				}
//				MappingRouteVo vo=null;
//				try
//				{
//					 vo=RouteParseUtil.getInstance(db).queryMappingRouteVo(api, params,authInfo);
//				}
//				catch(Exception e1)
//				{
//					e1.printStackTrace();
//					return getRetVal(outType,-14444,"读取路由配置失败,"+e1.getMessage());
//				}
//				if(vo == null){
//					return getRetVal(outType,-14444,"读取路由配置失败;路由地址为空.api="+api +" params="+params +" authInfo="+authInfo);
//				}
//				className=vo.getRemoteClass();//远程实现类
//				String[] tmp=api.split("\\.");
//				methodName=tmp[2];
//				Url[] urls = vo.getUrl();
//				int i = r.nextInt(urls.length);
//				url=urls[i];//实际的URL
//				
////				url=vo.getUrl();//实际的URL
//				if(className==null||className.equals(""))
//				{
//					result= getRetVal(outType,20000,"未找到"+api+"的远程实现类配置");
//					return result;
//				}
//				if(url==null)
//				{
//					result= getRetVal(outType,20000,"未找到"+api+"的服务地址配置");
//					return result;
//				}
//				//否则走正常的直接调用
//				InterfaceStandard  is=Rpc.get(InterfaceStandard.class, url);
//				result= is.onMessage(seq,authInfo.toString(), api,className, methodName,params, paramType, outType);
//				if(result==null)
//				{
//					KasiteConfig.print("rpc,result is null:"+clientip+"->"+url);
//					result=  getRetVal(outType,30000, "应用系统返回空结果集");
//					return result;
//				}
//				KasiteConfig.print("rpc,ok:"+clientip+"->"+url);
//				return result;
//				
//				
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//				String msg=api+" "+url+" error:"+RpcException.getException(e);
//				result= getRetVal(outType,30000,msg);
//				return result;
//			}
//			finally
//			{
//				//KasiteConfig.print(al.getAndIncrement());
//				if(System.currentTimeMillis()-start>1000)
//				{
//					KasiteConfig.print(api+",times="+(System.currentTimeMillis()-start)
//						+",url="+(url==null?"":url.toString())+",AuthInfo="+authInfo);
//				}
//			}
//	}
//	public String call(String api ,String params)
//	{
//		return this.call(api, params, 1, 1, "1");
//	}
//	public String call(IApiModule api ,String params)
//	{
//			return this.call(api.getName(), params, 1, 1, "1");
//	}
//	private  String getRetVal(int outType,int code,String message)
//	{
//		if(outType==0)
//		{
//			JSONObject  json=new JSONObject();
//			json.put(IConstant.RESPCODE, code);
//			json.put(IConstant.RESPMESSAGE,message);
//			return json.toString();
//		}
//		else
//		{
//			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><" +
//			IConstant.RESP+"><"+IConstant.RESPCODE+">"+code+"</"+IConstant.RESPCODE+"><"+IConstant.RESPMESSAGE+">"+message+"</"+IConstant.RESPMESSAGE+"></"+IConstant.RESP+">";
//		}
//	}
//}
