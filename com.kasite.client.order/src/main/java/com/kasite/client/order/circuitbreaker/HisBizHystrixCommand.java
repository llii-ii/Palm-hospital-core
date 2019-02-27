package com.kasite.client.order.circuitbreaker;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * @author linjf
 * 自定义HIS业务执行的熔断器
 * 同步执行
 */
public class HisBizHystrixCommand  extends HystrixCommand<RespPayEndBizOrderExecute> {

	private IBizExecuteHandler handler;
	
	private ReqPayEndBizOrderExecute reqPayEndBizOrderExecute;
	
	/**
	 * @param group
	 */
	public HisBizHystrixCommand(IBizExecuteHandler handler,ReqPayEndBizOrderExecute reqPayEndBizOrderExecute) {
		//分组名称HisBizExecuteGroup。Hystrix一个分组，使用一个线程池
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(KasiteHystrixCommandKey.HisBizExecuteGroup.name()))
				//Hystrix支持4种配置，该配置方法，优先级排第二。代码配置，方便实现动态修改配置（yml配置也支持）
    	        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
    	               .withCircuitBreakerEnabled(KasiteHystrixConfig.getBooleanValue(KasiteHystrixCommandKey.HisBizExecuteGroup.name(), "CircuitBreakerEnabled"))//开启断路器功能
    	               .withCircuitBreakerRequestVolumeThreshold(9)//该属性设置滚动窗口中将使断路器跳闸的最小请求数量。如果此属性值为20，则在窗口时间内（如10s内），如果只收到19个请求且都失败了，则断路器也不会开启。
    	               .withCircuitBreakerErrorThresholdPercentage(50)//设置失败百分比的阈值，如果失败比率超过这个值，则断路器跳闸并且进入fallback逻辑
    	               .withMetricsRollingStatisticalWindowInMilliseconds(60*1000)//1分钟窗口时间，即一个统计周期。
    	               .withCircuitBreakerForceOpen(KasiteHystrixConfig.getBooleanValue(KasiteHystrixCommandKey.HisBizExecuteGroup.name(), "CircuitBreakerForceOpen"))//是否强制开启断路器，开启则所有屏蔽所有请求
    	               .withExecutionTimeoutEnabled(false))//关闭超时限制
    	        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
    	        				   .withMaximumSize(20)//分组线程池最大值，默认10
    	        				   )
    	        );
		this.handler = handler;
		this.reqPayEndBizOrderExecute = reqPayEndBizOrderExecute;
		KasiteHystrixConfig.setValue(KasiteHystrixCommandKey.HisBizExecuteGroup.name(), "IsCircuitBreakerOpen", this.isCircuitBreakerOpen()+"");
	}
	
	

	/**
	 * @return
	 * @throws Exception
	 */
	@Override
	protected RespPayEndBizOrderExecute run() throws Exception {
		return handler.bizPayEndExecute(new CommonReq<ReqPayEndBizOrderExecute>(reqPayEndBizOrderExecute));
	}
	
	/**
	 * 重写此方法，将导致run()的异常不会抛出。除非降级需要，否则不建议做此处理。
	 * @return
	 */
//	@Override
//    protected RespPayEndBizOrderExecute getFallback() {
//		 //上面的run方法执行异常，将会执行此方法
//		RespPayEndBizOrderExecute respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
//		respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
//		return respPayEndBizOrderExecute;
//    }
}
