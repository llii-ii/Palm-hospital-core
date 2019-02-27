package com.kasite.client.order.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: OrderDataCollection
 * @author: lcz
 * @date: 2018年8月1日 下午2:29:37
 */
@Component
public class OrderDataCollectionJob {
	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private static boolean flag = true;

	@Autowired
	private IReportFormsService reportFormService;
	@Autowired
	IOrderMapper orderMapper;

	@Autowired
	KasiteConfigMap config;
	/**
	 * 数据采集-线上门诊数的收集 
	 * 每天23点55分执行
	 * @Description:
	 */
	public void doCollectData() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				log.info(">>>开始执行线上门诊数的收集。");
				int count = collectionData();
				log.info(">>>线上门诊数的收集完毕,共收集" + count + "个记录。");
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}

	private int collectionData() {
		try {
			String orderId = "OrderDataCollection_"+System.currentTimeMillis();
			InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(OrderDataCollectionJob.class,ApiModule.ReportForms.DataCollection.getName(), 
					null, orderId, "doCollectData", null, null,null);
			List<Map<String, Object>> list = orderMapper.getOrderDataCollection();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String channelId =map.get("CHANNELID").toString();
					Integer dataValue = new Integer(map.get("CARDNOCOUNT").toString());
					reportFormService.dataCollection(msg,channelId, "", 
							ApiModule.ReportForms.DataCollection.getName(), 4,dataValue, "门诊人数收集");
				}
				return list.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return 0;
	}
}
