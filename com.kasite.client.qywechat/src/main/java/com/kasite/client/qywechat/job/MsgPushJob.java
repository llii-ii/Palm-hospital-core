package com.kasite.client.qywechat.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kasite.client.qywechat.dao.MsgPushDao;
import com.kasite.client.qywechat.service.SendMessageService;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MsgPush;
import com.yihu.hos.util.JSONUtil;

import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 企微消息推送作业
 * 
 * @author 無
 *
 */
@Component
public class MsgPushJob {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private boolean flag = true;

	@Autowired
	KasiteConfigMap config;

	@Autowired
	MsgPushDao msgPushDao;

	@Autowired
	SendMessageService sendMessageService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void msgPushJob() {
		execute();
	}

	public void execute() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = true;
				String startDate = DateUtils.getYesterdayStartString(new Date(), "yyyy-MM-dd HH:mm:ss");
				String endDate = DateUtils.getEndForDayString(new Date());
				deal(startDate, endDate);
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

	public void deal(String startDate, String endDate) throws Exception {
		Example example = new Example(MsgPush.class);
		Criteria criteria = example.createCriteria();
		criteria.andBetween("inserttime", startDate, endDate);
		criteria.andNotEqualTo("result", 10000);
		criteria.andLessThan("retry", 3);
		// 查询昨天到今天 发送结果不为成功 或者 重试次数小于3次的数据
		List<MsgPush> list = msgPushDao.selectByExample(example);
		System.out.println("待推送消息数:" + list.size());
		for (MsgPush msgPush : list) {
			System.out.println("推送:" + msgPush.getMsgtitle());
			String result = "";
			String respCode = "0";
			String respMessage = "0";
			try {
				if (msgPush.getMsgtype() == 2) {
					// 图文消息
					result = sendMessageService.SendNewsMessageService(msgPush.getMsgtitle(), msgPush.getMsgcontent(),
							msgPush.getMsgurl(), msgPush.getPicurl(), msgPush.getTouser(), msgPush.getToparty(),
							msgPush.getConfigkey());
				} else if (msgPush.getMsgtype() == 1) {
					// 文本卡片消息
					result = sendMessageService.SendTextCardMessage(msgPush.getMsgtitle(), msgPush.getMsgcontent(),
							msgPush.getMsgurl(), msgPush.getTouser(), msgPush.getToparty(), msgPush.getConfigkey());
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getLocalizedMessage();
			}
			if (StringUtil.isNotBlank(result)) {
				JSONObject json = JSONObject.fromObject(result);
				respCode = JSONUtil.getJsonString(json, "RespCode", false);
				respMessage = JSONUtil.getJsonString(json, "RespMessage", false);
			}
			if (!"10000".equals(respCode)) {
				respCode = respMessage;
			}
			msgPush.setResult(respCode);
			msgPush.setRetry(msgPush.getRetry() + 1);
			msgPushDao.updateByPrimaryKeySelective(msgPush);
		}

	}

}
