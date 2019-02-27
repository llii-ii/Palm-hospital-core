package com.kasite.client.queue.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coreframework.util.DateOper;
import com.kasite.client.queue.bean.dbo.QueueInfo;
import com.kasite.client.queue.dao.IQueueInfoMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.HisGetQueueInfo;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgQueue;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.queue.IQueueService;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * 候诊队列提醒作业
 *
 */
/**
 * @author linjf TODO
 */
@Component
public class QueueReMindNoJob{

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	@Autowired
	IQueueService queueService;
	
	@Autowired
	IBasicService basicService;
	
	@Autowired
	IQueueInfoMapper queueInfoMapper;
	
	@Autowired
	IYYService yyService;
	
	@Autowired
	IMsgService msgService;
	
	@Autowired
	KasiteConfigMap config;
	
	private static boolean flag = true;
	/**
	 * 候诊队列提醒
	 * 每5分钟执行
	 * @Description:
	 */
	public void queryQueueInfoListByToday() {
		try {
			if( flag && config.isStartJob(this.getClass())) {
				flag = false;
				
				Example example = new Example(QueueInfo.class);
				example.createCriteria()
				.andEqualTo("ifMsg", "1")
				.andEqualTo("registerDate", DateOper.getNow("yyyy-MM-dd"));
				List<QueueInfo> list = queueInfoMapper.selectByExample(example);
				String orderId = "QueueReMindNoJob_"+System.currentTimeMillis();
				for (QueueInfo localQueue : list) {
					try {
						int reMindNo = Integer.parseInt(localQueue.getReMindNo());
						Map<String, String> map = new HashMap<String, String>(16);
						map.put("cardNo", localQueue.getCardNo());
						map.put("cardType", localQueue.getCardType());
						map.put("memberName", localQueue.getPatientName());
						InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(QueueReMindNoJob.class,"queryQueue", null, 
								orderId, "queryQueueInfoListByToday", null, null,localQueue.getOperatorId());
						List<HisGetQueueInfo> qiList = HandlerBuilder.get().getCallHisService(KasiteConfig.getOrgCode()).queryQueue(msg,map)
								.getListCaseRetCode();
						for (HisGetQueueInfo hisQueue : qiList) {
							if(StringUtil.isBlank(hisQueue.getMaxNo())) {
								//当前序号为空，则可能还没有进入队列，不做处理
								continue;
							}
							// 当前序号
							int maxNo = Integer.parseInt(hisQueue.getMaxNo());
							// 就诊序号
							int no = Integer.parseInt(hisQueue.getNo());
							if (maxNo >= reMindNo && maxNo <= no && hisQueue.getQueryId().equals(localQueue.getQueryId())) {
								// 调用消息推送
								try { // 如抛出异常循环不终止
									hisQueue.setChannelId(localQueue.getChannelId());
									
									// 查询OPID
									ReqQueryMemberList memberReq = new ReqQueryMemberList(msg, null, localQueue.getCardNo(), 
											localQueue.getCardType(), null, localQueue.getChannelId(), null, null,null,true);
									CommonResp<RespQueryMemberList> memberResp = basicService.queryMemberList(new CommonReq<ReqQueryMemberList>(memberReq));
									if(memberResp==null || !KstHosConstant.SUCCESSCODE.equals(memberResp.getCode()) || memberResp.getData()==null || memberResp.getData().size()<=0) {
										log.error("没有找到成员信息：cardType="+memberReq.getCardType()+"|||cardNo="+memberReq.getCardNo()+"|||channelId="+memberReq.getChannelId());
										continue;
									}
									List<RespQueryMemberList> ll = memberResp.getData();
									if (ll == null || ll.size()<=0) {
										log.error("ID为" + localQueue.getQueryId() + "未查询出OPID，无法推送");
										continue;
									}
									//是否发生成功，当多个有一个成功时，就算成功
									boolean isSuccess = false;
											
									for (RespQueryMemberList member : ll) {
										if(StringUtil.isBlank(member.getOpId())) {
											log.error("MEMBERID为" + member.getMemberId() + "未查询出OPID，无法推送");
											continue;
										}
										//尊敬的<memberName>！您好，您前面只有<no>个人，您的号序为<sqNo>号，请及时就诊~
										Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
										XMLUtil.addElement(data1, ApiKey.MODETYPE_10101122.memberName, hisQueue.getPatientName());
										XMLUtil.addElement(data1, ApiKey.MODETYPE_10101122.no, hisQueue.getMaxNo());
										XMLUtil.addElement(data1, ApiKey.MODETYPE_10101122.sqNo, hisQueue.getNo());
										
										ReqSendMsg queue = new ReqSendMsg(msg,localQueue.getCardNo(), 1, localQueue.getChannelId(), "", "",
												"", KstHosConstant.MODETYPE_10101122, data1.asXML(), member.getOpId(), 1, member.getOpId(), member.getOpId(), "", "", "", "");
										CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
										CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
										if(KstHosConstant.SUCCESSCODE.equals(addMsgQueue.getCode())) {
											isSuccess = true;
										}
									}
									if(isSuccess) {
										QueueInfo update = new QueueInfo();
										update.setMsgStatus(1);
										update.setQueryId(localQueue.getQueryId());
										queueInfoMapper.updateByPrimaryKeySelective(update);
										// 清空提醒序号
										queueInfoMapper.removeReMindNo("0", "", localQueue.getChannelId(), localQueue.getQueryId());
									}
								} catch (Exception e) {
									e.printStackTrace();
									log.error(e);
								}
							}
						}
					} catch (Exception e) {
						log.error(e);
						e.printStackTrace();
					}
				}
				
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		}finally {
			flag = true;
		}
	}
	public static void main(String[] args) {
		
		KasiteConfig.print(CommonUtil.getUUID());
	}
}
