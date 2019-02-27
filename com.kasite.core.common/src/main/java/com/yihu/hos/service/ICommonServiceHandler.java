package com.yihu.hos.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 
 * 所有实现类加上代理通过代理实现日志搜集和一些其他操作
 * 
 * 注: 这里最好不要加上业务逻辑.
 * 
 * @author daiys
 * 
 */
public class ICommonServiceHandler<T extends ICommonService> extends CommonServiceReturnMessage implements InvocationHandler {

	/**
	 * 需要代理的接口
	 */
	private T instance;

	public ICommonServiceHandler(T instance) {
		this.instance = instance;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {
		return null;
//		String methodName = method.getName();
//		IEvent event = instance.getEvent(methodName);
//		DModule module = instance.getModule();
//		String seq = null;
//		if(null!=event){
//			event.before(module,args);
//		}
//		if(null != args && null != args[0] && args[0] instanceof InterfaceMessage){
//			InterfaceMessage msg = (InterfaceMessage) args[0];
//			if(null == msg || null == msg.getSeq()){
//				return getRetVal(CommonServiceRetCode.Common.ERROR_PARAM,"请求的参数中Seq不能为空。");
//			}
//			seq = msg.getSeq();
//			MDC.put(IConstant.SEQ,seq);
//		}
//		
//		Object o = null; 
//		try{
//			if(!methodName.equals("setDebug")&&module.isDebug() && StringUtil.isNotBlank(module.getTestid()) && module.getTestid().equals(seq)){
//				StubPushRequest req = new StubPushRequest();
//				req.setPushMode();
//				req.setName(module.getTestid());
//				req.setSendTimeOutMills(30000);
//				Class<?> obj = Class.forName(module.getInterfaceClass());
//				Object service = Rpc.stub(obj, req);
//				o = ReflectionUtils.invokeMethod(service, method.getName(), method.getParameterTypes(), args);
//			}else{
//				o = method.invoke(instance, args);
//			}
//		}catch (Throwable e) {
//			e.printStackTrace();
//			throw getExcetpion(e);
//		}
//		if(null!=event){
//			event.after(module,args);
//		}
//		return o;
	}

	public Throwable getExcetpion(Throwable e){
		Throwable e2 = e.getCause();
		if(null != e2.getCause()){
			e2.printStackTrace();
			return getExcetpion(e2);
		}
		return e2;
	}
}
