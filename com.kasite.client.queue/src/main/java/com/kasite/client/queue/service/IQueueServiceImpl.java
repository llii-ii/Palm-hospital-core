package com.kasite.client.queue.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.StringUtil;
import com.kasite.client.queue.bean.dbo.QueueInfo;
import com.kasite.client.queue.constant.Constant;
import com.kasite.client.queue.dao.IQueueInfoMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.HisGetQueueInfo;
import com.kasite.core.serviceinterface.module.queue.IQueueService;
import com.kasite.core.serviceinterface.module.queue.req.ReqGetQueueInfo;
import com.kasite.core.serviceinterface.module.queue.req.ReqQueryLocalQueue;
import com.kasite.core.serviceinterface.module.queue.req.ReqSetReMindNo;
import com.kasite.core.serviceinterface.module.queue.resp.RespGetQueueInfo;
import com.kasite.core.serviceinterface.module.queue.resp.RespQueryLocalQueue;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author linjf TODO
 */
@Service("queue.QueueWs")
public class IQueueServiceImpl  implements IQueueService {

	private final static Log log = LogFactory.getLog(Constant.LOG4J_QUEUE);
	
	@Autowired
	IQueueInfoMapper queueInfoMapper;
	@Autowired
	IBasicService basicService;
	
	
	/**
	 * 获取排队信息
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String GetQueueInfo(InterfaceMessage msg) throws Exception{
		return this.getQueueInfo(new CommonReq<ReqGetQueueInfo>(new ReqGetQueueInfo(msg))).toResult();
	}

	/**
	 * 设置提醒号序
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String SetReMindNo(InterfaceMessage msg) throws Exception{
		return this.setReMindNo(new CommonReq<ReqSetReMindNo>(new ReqSetReMindNo(msg))).toResult();
	}

	@Override
	public CommonResp<RespGetQueueInfo> getQueueInfo(CommonReq<ReqGetQueueInfo> commReq) throws Exception {
		ReqGetQueueInfo req = commReq.getParam();
		
		CommonResp<RespQueryMemberList> memberResp = basicService.queryMemberListToCache(new CommonReq<ReqQueryMemberList>(new ReqQueryMemberList(req.getMsg(),
				null,req.getCardNo(), String.valueOf(req.getCardType()), req.getOpenId())));
		
		if(!KstHosConstant.SUCCESSCODE.equals(memberResp.getCode()) || memberResp.getResultData()==null){
			return new CommonResp<RespGetQueueInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CANNOTEXIST, "根据卡号没有找到用户信息。"); 
		}
		RespQueryMemberList member = memberResp.getResultData();
		// 入参xml转map格式
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(ApiKey.HisGetQueueInfo.cardType.name(), String.valueOf(req.getCardType()));
		map.put(ApiKey.HisGetQueueInfo.cardNo.name(), req.getCardNo());
		map.put(ApiKey.HisGetQueueInfo.memberName.name(), member.getMemberName());
		List<HisGetQueueInfo> lists = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryQueue(req.getMsg(),map)
				.getListCaseRetCode();
		List<RespGetQueueInfo> respList = new ArrayList<RespGetQueueInfo>();
		if(null != lists) {
			for (HisGetQueueInfo info : lists) {
				RespGetQueueInfo resp = new RespGetQueueInfo();
				resp.setCardNo(info.getCardNo());
				resp.setCardType(info.getCardType());
				resp.setDeptName(info.getDeptName());
				resp.setDoctorName(info.getDoctorName());
				resp.setIfMsg(info.getIfMsg());
				resp.setLocation(info.getLocation());
				resp.setMaxNo(info.getMaxNo());
				resp.setNextNo(info.getNextNo());
				resp.setNo(info.getNo());
				resp.setPatientName(info.getPatientName());
				resp.setPhoneNo(info.getPhoneNo());
				resp.setQueryId(info.getQueryId());
				resp.setRegisterDate(info.getRegisterDate());
				resp.setReMindNo(info.getReMindNo());
				resp.setTimeId(info.getTimeId());
				respList.add(resp);
			}
		}
		return new CommonResp<RespGetQueueInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespMap> setReMindNo(CommonReq<ReqSetReMindNo> commReq) throws Exception {
		
		ReqSetReMindNo req = commReq.getParam();
		
		QueueInfo queue = new QueueInfo();
		queue.setQueryId(req.getQueryId());
		queue.setIfMsg(req.getIfMsg());
		queue.setReMindNo(req.getReMindNo());
		queue.setChannelId(req.getChannelId());
		
		Example example = new Example(QueueInfo.class);
		example.createCriteria()
		.andEqualTo("queryId", req.getQueryId())
		.andEqualTo("cardNo", req.getCardNo());
		QueueInfo Info = queueInfoMapper.selectOneByExample(example);
		if(Info!=null) {
			//修改
			//update Q_QUEUEINFO set IFMSG=?, REMINDNO=?,CHANNELID = ? where QUERYID = ?  and CARDNO=?
			queueInfoMapper.updateByExampleSelective(queue, example);
		}else {
			//新增
			queue.setCardNo(req.getCardNo());
			queue.setCardType(req.getCardType());
			queue.setDeptName(req.getDeptName());
			queue.setDoctorName(req.getDoctorName());
			queue.setLocation(req.getLocation());
			queue.setMaxNo(req.getMaxNo());
			queue.setNextNo(req.getNextNo());
			queue.setNo(req.getNo());
			queue.setPatientName(req.getPatientName());
			queue.setPhoneNo(req.getPhoneNo());
			
			if(StringUtil.isNotBlank(req.getRegisterDate())) {
				queue.setRegisterDate(DateOper.parse(req.getRegisterDate()));
			}
			queue.setTimeId(req.getTimeId());
			queueInfoMapper.insertSelective(queue);
		}
		RespMap resp = new RespMap();
		resp.put(ApiKey.SetReMindNo.QueryId, queue.getQueryId());
		resp.put(ApiKey.SetReMindNo.IfMsg, queue.getIfMsg());
		resp.put(ApiKey.SetReMindNo.ReMindNo, queue.getReMindNo());
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespQueryLocalQueue> queryLocalQueue(CommonReq<ReqQueryLocalQueue> commReq) throws Exception {
		ReqQueryLocalQueue req = commReq.getParam();
		Example example = new Example(QueueInfo.class);
		Criteria criteria = example.createCriteria();
		if(req.getCardType()!=null && req.getCardType()>0) {
			criteria.andEqualTo("cardType", req.getCardType());
		}
		if(StringUtil.isNotBlank(req.getCardNo())) {
			criteria.andEqualTo("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getQueryId())) {
			criteria.andEqualTo("queryId", req.getQueryId());
		}
		List<QueueInfo> list = queueInfoMapper.selectByExample(example);
		List<RespQueryLocalQueue> respList = new ArrayList<RespQueryLocalQueue>();
		for (QueueInfo info : list) {
			RespQueryLocalQueue resp = new RespQueryLocalQueue();
			BeanCopyUtils.copyProperties(info, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

}
