package com.kasite.core.serviceinterface.module.handler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;

import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.service.CallHis;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.common.sys.handler.IWarnNotifyHandler;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.validator.Assert;
import com.kasite.core.serviceinterface.module.his.ICallHisService;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.pay.IBizPayStartOrderCheckHandler;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;

public class HandlerBuilder {
	private static Logger logger = LoggerFactory.getLogger(HandlerBuilder.class);	
	private static HandlerBuilder install;
	
	/**
	 * 系统初始化注入
	 * 支付后的业务执行器
	 */
	private Map<String,IBizExecuteHandler> bizExecuteHandlerMap = new HashMap<>();
	
	/**
	 * 系统初始化
	 * 支付网关实现类，专门对接第三方支付平台
	 */
	private Map<String,IPaymentGateWayService> payGateWayMap = new HashMap<String,IPaymentGateWayService>(16);
	/**
	 * 获取接口校验处理类
	 */
	private IBizPayStartOrderCheckHandler payOrderCheckHandler;
	/**
	 * 需要通知的告警接收的列表
	 */
	private List<IWarnNotifyHandler> notifyList = new ArrayList<>();
	/**
	 * 新增通知告警处理程序
	 * @param notify
	 */
	private void addWarnNotify(IWarnNotifyHandler notify) {
		logger.info("新增告警消息处理程序："+ notify.getClass().getName());
		notifyList.add(notify);
	}
	/**
	 * 发送告警消息
	 * @param json {title:'标题',url:'链接地址',remark:'备注',level,'告警级别'}
	 */
	public void notifyWarn(JSONObject json) {
		for (IWarnNotifyHandler notify : notifyList) {
			try {
				notify.notify(json);
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(logger,"发送告警消息异常：",e);
			}
		}
	}
	/**
	 * 发送告警消息
	 * @param json {title:'标题',url:'链接地址',remark:'备注',level,'告警级别'}
	 */
	public void sendNotify(String title,String level,String remark,String url) {
		JSONObject json = new JSONObject();
		json.put("title", title);
		json.put("level", level);
		json.put("remark", remark);
		json.put("url", url);
		notifyWarn(json);
	}
	/**
	 * 医院对应调用业务逻辑实现类
	 * 通过spring注入的方式在启动的时候注入到服务中
	 * YYModuleStartUp
	 */
	private Map<String, Map<String,ICallHis>> callHisServiceMap = new HashMap<>();
	
	private Map<String, IMsgService> msgService = new HashMap<>();
	/**
	 * 新增默认的消息处理类
	 * @param iMsgService
	 */
	private void addDefaultMsgService(IMsgService iMsgService) {
		IMsgService.Type t = iMsgService.accept();
		IMsgService ls = msgService.get(t.name());
		logger.info("加载消息处理插件，默认实现："+ t.getClass().getName());
		if(null != ls) {
			logger.info(MessageFormat.format(KstHosConstant.ERROR_SERVICEIMPLREP, t.name(),ls.getClass().getName()));
		}
		msgService.put(t.name(), iMsgService);
	}
	
	public IMsgService getMsgService(AuthInfoVo vo) {
		return msgService.get(IMsgService.Type.DEFAULT.name());
	}
	
//	
	private synchronized HandlerBuilder addCallHisServiceHandler(List<ICallHis> list) {
		for (ICallHis service : list) {
			Class<?>[] clazzs = service.getClass().getInterfaces();
			for (Class<?> clazz : clazzs) {
				CallHis callHis =service.accept();
				if(null != callHis) {
					String orgCode = callHis.getOrgCode();
					logger.info("加载HIS接口实现处理插件，医院【{}】class【{}】", orgCode,clazz.getName());
					Map<String,ICallHis> map = callHisServiceMap.get(clazz.getName());
					if(null == map) {
						map = new HashMap<>();
					}
					ICallHis existService = map.get(orgCode);
					if(null != existService) {
						logger.error("医院的接口实现类有重复的，请检查后删除重复的接口实现。医院【{}】class【{}】", orgCode,existService.getClass().getName());
						continue ;
					}
					map.put(orgCode, service);
					callHisServiceMap.put(clazz.getName(), map);
				}
			}
		}
		return this;
	}
	/**
	 * 如果机构有实现锁号，则返回锁号接口
	 * @param vo
	 * @return
	 */
	public <T extends ICallHis> T getCallHisService(AuthInfoVo vo,Class<T> clazz) {
		return getCallHisService(vo.getClientVersion(), clazz);
	}
	/**
	 * 如果机构有实现锁号，则返回锁号接口
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ICallHis> T getCallHisService(String orgCode,Class<T> clazz) {
		Assert.isBlank(orgCode, "机构ID 不能为空");
		Map<String,ICallHis> map = callHisServiceMap.get(clazz.getName());
		if(null != map) {
			Object o = map.get(orgCode);
			if(null != o) {
				return (T) o;
			}
		}
		return null;
	}
	/**
	 * 如果机构有实现锁号，则返回锁号接口
	 * @param vo
	 * @return
	 */
	public ICallHisService getCallHisService(String orgCode) {
		Assert.isBlank(orgCode, "机构ID 不能为空");
		Map<String,ICallHis> map = callHisServiceMap.get(ICallHisService.class.getName());
		if(null != map) {
			Object o = map.get(orgCode);
			if(null != o) {
				return (ICallHisService) o;
			}
		}
		return null;
	}
	/**
	 * 如果机构有实现锁号，则返回锁号接口
	 * @param vo
	 * @return
	 */
	public ICallHisService getCallHisService(AuthInfoVo vo) {
		return getCallHisService(vo.getClientVersion());
	}
	
	/**
	 * 获取订单支付前做校验的接口实现
	 * 如果没有该接口实现则支付的时候会抛出异常
	 */
	public IBizPayStartOrderCheckHandler getBizPayStartOrderCheckHandler() {
		return this.payOrderCheckHandler;
	}
	
	static {
		install = new HandlerBuilder();
	}
	
	private HandlerBuilder() {
		
	}
	public static HandlerBuilder get() {
		return install;
	}

	public HandlerBuilder init(ContextRefreshedEvent event) {
		
		logger.info("-------------------------------初始化注入系统插件 start-----------------------------------");
		//注入支付部分的事件执行器
		List<IBizExecuteHandler> bizExecuteList = new LinkedList<>(event.getApplicationContext().
				getBeansOfType(IBizExecuteHandler.class).values());
		addBizExecuteHandlerMap(bizExecuteList);
		
		//注入支付网关的事件执行器
		List<IPaymentGateWayService> payGateWayList = new LinkedList<IPaymentGateWayService>(event.getApplicationContext().
				getBeansOfType(IPaymentGateWayService.class).values());
		addPayGateWayMap(payGateWayList);
		
		//HIS接口实现启动的时候加载需要注入的方法
		List<ICallHis> callHisService = new LinkedList<>(event.getApplicationContext().
				getBeansOfType(ICallHis.class).values());
		addCallHisServiceHandler(callHisService);
		
		//支付／微信消息，需要调用 IMsgService 接口的时候解除依赖
		List<IMsgService> msgServiceList = new LinkedList<>(event.getApplicationContext().
				getBeansOfType(IMsgService.class).values());
		for (IMsgService msgService : msgServiceList) {
			if(msgService.accept().equals(IMsgService.Type.DEFAULT)) {
				addDefaultMsgService(msgService);
			}
		}
		
		//HIS接口实现启动的时候加载需要注入的方法
		List<IWarnNotifyHandler> warnNotifyList = new LinkedList<>(event.getApplicationContext().
				getBeansOfType(IWarnNotifyHandler.class).values());
		if(null != warnNotifyList && warnNotifyList.size() > 0) {
			for (IWarnNotifyHandler notify : warnNotifyList) {
				addWarnNotify(notify);
			}
		}
		/**
		 * 订单支付前确认接口
		 */
		List<IBizPayStartOrderCheckHandler> bizPayStartOrderCheckHandlerList = new LinkedList<>(event.getApplicationContext().
				getBeansOfType(IBizPayStartOrderCheckHandler.class).values());
		if(bizPayStartOrderCheckHandlerList != null && bizPayStartOrderCheckHandlerList.size() == 1) {
			payOrderCheckHandler = bizPayStartOrderCheckHandlerList.get(0);
		}
		
		logger.info("-------------------------------初始化注入系统插件 end-----------------------------------");
		return this;
	}
	
	private HandlerBuilder addBizExecuteHandlerMap(List<IBizExecuteHandler> list) {
		for (IBizExecuteHandler execute : list) {
			logger.info("加载业务执行器："+ execute.getClass());
			BusinessTypeEnum bte = execute.accept();
			String key = bte.getCode();
			if(null != bizExecuteHandlerMap.get(key)) {
				logger.info("业务执行器被覆盖：serviceId = "+bte.getCode() +" 覆盖陈："+execute.getClass().getName());
			}
			bizExecuteHandlerMap.put(key, execute);
		}
		return this;
	}
	
	private HandlerBuilder addPayGateWayMap(List<IPaymentGateWayService> list) {
		for (IPaymentGateWayService payGateWay : list) {
			logger.info("加载支付网关实现类："+ payGateWay.getClass());
			String mchType = payGateWay.mchType();
			if(null != bizExecuteHandlerMap.get(mchType)) {
				logger.info("业务执行器被覆盖：mchType = "+mchType+" 覆盖成："+payGateWay.getClass().getName());
			}
			payGateWayMap.put(mchType, payGateWay);
		}
		return this;
	}

	public IBizExecuteHandler getBizExecuteHandler(String serviceId) {
		return bizExecuteHandlerMap.get(serviceId);
	}
	
	
	public IPaymentGateWayService getPayGateWayInstance(String mechType) {
		return payGateWayMap.get(mechType);
	}
	
}
