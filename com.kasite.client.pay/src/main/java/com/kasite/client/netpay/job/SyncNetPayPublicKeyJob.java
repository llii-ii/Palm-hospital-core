package com.kasite.client.netpay.job;


import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.netpay.constants.NetPayConstant;
import com.kasite.client.netpay.util.NetPay;
import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
/**
 * @author linjf
 * TODO
 */
@Component
public class SyncNetPayPublicKeyJob {
	
	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private static boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;

	@Autowired
	IBillMapper billMapper;
	
	/**
	 * 银行到账勾兑账单
	 * 			--每天12点执行
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				Map<String, ChannelTypeEnum> configkeyMap = KasiteConfig.getAllConfigKey();
				if(configkeyMap != null) {
					for (Entry<String, ChannelTypeEnum> entry : configkeyMap.entrySet()) {
						String configKey = entry.getKey();
						ChannelTypeEnum channelType = entry.getValue();
						if( ChannelTypeEnum.netpay.equals(channelType)) {
							//刷新缓存
							NetPayConstant.publicKeyMap.put(configKey, NetPay.getPubKey(configKey,true));
						}
					}
				}
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
}
