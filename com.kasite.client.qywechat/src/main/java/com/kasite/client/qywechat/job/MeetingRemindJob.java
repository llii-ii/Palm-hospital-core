package com.kasite.client.qywechat.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kasite.client.qywechat.dao.MeetingDao;
import com.kasite.client.qywechat.dao.MsgPushDao;
import com.kasite.client.qywechat.dao.ToMemberDao;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Meeting;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MsgPush;
import com.kasite.core.serviceinterface.module.qywechat.dbo.ToMember;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 会议提醒作业
 * 
 * @author cjy
 *
 */
@Component
public class MeetingRemindJob {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	private final static String qyConfigKey = "1000004";
			
	private final static String qyClientId = "200001";
	
	private boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	private MeetingDao meetingDao;
	
	@Autowired
	private MsgPushDao msgPushDao;
	
	@Autowired
	private ToMemberDao toMemberDao;
	
	//@Scheduled(cron = "0 0/1 * * * ?")
	public void meetingRemindJob() {
		execute();
	}
	
	public void execute() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = true;
				dealMeetingRemind();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
	
	
	public void dealMeetingRemind() throws Exception {
		//当前时间
		String nowDate = DateUtils.format(new Date(), DateUtils.DATE_TIME14_PATTERN);
		
		//获得未开始且需要提醒的会议
		Example example = new Example(Meeting.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("status", 1);
		criteria.andCondition("remind != 0");
		List<Meeting> list = meetingDao.selectByExample(example);
		
		//判断时间是否到了
		for (int i = 0; i < list.size(); i++) {
			Integer remind = Integer.parseInt(list.get(i).getRemind());
			String meetingStartTime = list.get(i).getMeetingdate() + " " + list.get(i).getTimestart();
			Integer diffMin = diffMin(nowDate,meetingStartTime,DateUtils.DATE_TIME14_PATTERN);
			if (diffMin == remind) {
				msgPush(list.get(i),diffMin);
			}
		}
	}
	
	//消息推送
	public void msgPush(Meeting meeting,Integer diffMin) throws Exception {
		if (meeting.getIsremind() == 0) {
			//获取该会议的参与人
			String memberIds = "";
			String uid = meeting.getMeetingid();
			ToMember toMember = new ToMember();
			toMember.setUid(uid);
			toMember.setMembertype(0);
			List<ToMember> toMembers = toMemberDao.select(toMember);
			for (ToMember member : toMembers) {
				if (member.getIsDept() == 0) {
					memberIds = memberIds + member.getMemberid() + ",";
				}else if (member.getIsDept() == 1) {
					memberIds = memberIds + "d_" + member.getMemberid() + ",";
				}
			}
			
			
			String toUrl = "/business/qywechat/wechat/html/meeting/detail.html?meetingId=" + meeting.getId();
			String content = "<div class=\"normal\">发起人：" + meeting.getOperatorname() + "</div>"
					+"<div class=\"normal\">会议学分：" + meeting.getCredits() + "分</div>"
					+ "<div class=\"normal\">会议时间：" + meeting.getMeetingdate() + " " + meeting.getTimestart() + "~"
					+ meeting.getTimeend() + "</div>" 
					+ "<div class=\"normal\">会议地点：" + meeting.getAddress() + "</div>"
					+ "<div class=\"normal\">请注意：会议开始时间已不足" + diffMin + "分钟！</div>";
			PushMsg(qyConfigKey, qyClientId, memberIds, toUrl, uid, "会议通知：" + meeting.getTitle(), content,"", 1);
			
			//将会议提醒修改成1-已推送
			Meeting record = new Meeting();
			record.setId(meeting.getId());
			record.setIsremind(1);
			meetingDao.updateByPrimaryKeySelective(record);
		}
	}
	
	
	//获得相差的分钟数
	public Integer diffMin(String startDate, String endDate,String format) {
		Integer min = 0;
		SimpleDateFormat sdi = new SimpleDateFormat(format);
		try {
			Date start = sdi.parse(startDate);
			Date end = sdi.parse(endDate);
			min = (int) ((end.getTime() - start.getTime()) / (60 * 1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return min;
	}
	
	/**
	 * 消息推送
	 * 
	 * @param qyConfigKey
	 * @param qyClientId
	 * @param memberIds
	 * @param toUrl
	 * @param uid
	 * @param title
	 * @param content       文本卡片消息格式参考如下： String content="<div
	 *                      class=\"gray\">2018年12月10日</div> " + "<div
	 *                      class=\"normal\">恭喜你抽中纸巾一包，领奖码：3523</div>" + "<div
	 *                      class=\"highlight\">请于2018年12月31日前联系黄嘤嘤领取</div>" + "<div
	 *                      class=\"highlight\">本消息由spring boot admin发送</div>";
	 * @param attachmentUrl
	 * @param msgType
	 * @throws Exception
	 */
	private void PushMsg(String qyConfigKey, String qyClientId, String memberIds, String toUrl, String uid,
			String title, String content, String attachmentUrl, int msgType) throws Exception {
		if (!StringUtil.isEmpty(memberIds)) {
			String[] memberIdStr = memberIds.split(",");
			String users = "";
			String depts = "";
			for (String member : memberIdStr) {
				if (StringUtil.isNotEmpty(member)) {
					if (member.startsWith("d_")) {
						depts = member + "|" + depts;
					} else {
						users = member + "|" + users;
					}
				}
			}
			if (StringUtil.isNotEmpty(users)) {
				users = users.substring(0, users.length() - 1);
			}
			if (StringUtil.isNotEmpty(depts)) {
				depts = depts.substring(0, depts.length() - 1);
				depts = depts.replace("d_", "");
			}
			// 保存消息
			MsgPush msg = new MsgPush();
			msg.setMsgPush(users, depts, uid, title, content, toUrl, qyConfigKey, qyClientId, attachmentUrl, msgType);
			msgPushDao.insertSelective(msg);
		}
	}
}
