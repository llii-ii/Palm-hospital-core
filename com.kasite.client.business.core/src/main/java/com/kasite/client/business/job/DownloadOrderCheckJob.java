package com.kasite.client.business.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.order.dao.IOrderCheckMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IDownloadOutQLCOrder;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryOrderCheck;
import com.kasite.core.serviceinterface.module.order.dbo.OrderCheck;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * 下载本地全流程账单(只用于对账)
 * 
 * @author zhaoy
 *
 */
@Component
public class DownloadOrderCheckJob {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	private boolean flag = true;
	/**重试3次*/
	private static int RETTIMES = 3;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	IOrderCheckMapper orderCheckMapper;
	
	/**
	 * 下载第三方本地全流程订单信息
	 * 		--每天11点30分执行
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = true;
				log.info("开始下载本地全流程订单列表信息");
				
				String startDate = DateUtils.getYesterdayStartString(new Date(), "yyyy-MM-dd");
				String endDate = DateUtils.format(new Date(), "yyyy-MM-dd");
				String date = DateUtils.getYesterdayEndString(new Date(), "yyyy-MM-dd");

				deal(startDate, endDate, date);
				
				flag = true;
				log.info("结束下载本地全流程订单");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
	
	public void deal(String startDate, String endDate, String date) throws Exception{
		Example example = new Example(OrderCheck.class);
		example.createCriteria().andBetween("orderDatetime", startDate, endDate);
		orderCheckMapper.deleteByExample(example);
		int times = 0;  //循环次数
		String hosid = KasiteConfig.getOrgCode();
		IDownloadOutQLCOrder service = HandlerBuilder.get().getCallHisService(hosid, IDownloadOutQLCOrder.class);
		if(service != null) {
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("startDate", date);
			paramMap.put("endDate", date);
			InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadOrderCheckJob.class, "queryOutOrderList", 
					KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
			while (times++ < RETTIMES) {
				CommonResp<RespQueryOrderCheck> resp = service.queryOutOrderList(msg, paramMap);
				if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
					List<RespQueryOrderCheck> respList = resp.getList();
					if(respList != null && respList.size() > 0) {
						for (RespQueryOrderCheck obj : respList) {
							OrderCheck orderCheck = new OrderCheck();
							orderCheck.setId(StringUtil.getUUID());
							BeanCopyUtils.copyProperties(obj, orderCheck, null);
							orderCheckMapper.insertSelective(orderCheck);
						}
						break;
					}
				}
			}
		}
	}
	
//	public static void main(String[] args) {
//		String endDate = DateUtils.format(new Date(), "yyyy-MM-dd");
//		System.out.println(endDate);
//	}
}
