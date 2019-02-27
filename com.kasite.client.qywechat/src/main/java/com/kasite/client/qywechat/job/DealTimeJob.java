package com.kasite.client.qywechat.job;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kasite.client.qywechat.dao.ActivityDao;
import com.kasite.client.qywechat.dao.MeetingDao;
import com.kasite.client.qywechat.dao.MemberCreditsInfoDao;
import com.kasite.client.qywechat.dao.ToMemberDao;
import com.kasite.client.qywechat.dao.VoteQuestionDao;
import com.kasite.client.qywechat.service.SendMessageService;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Activity;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Meeting;
import com.kasite.core.serviceinterface.module.qywechat.dbo.MemberCreditsInfo;
import com.kasite.core.serviceinterface.module.qywechat.dbo.ToMember;
import com.kasite.core.serviceinterface.module.qywechat.dbo.VoteQuestion;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 处理问卷、投票、活动、会议等截止时间后的状态更新作业
 * 
 * @author 無
 * @author cjy
 *
 */
@Component
public class DealTimeJob {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private boolean flag = true;

	@Autowired
	KasiteConfigMap config;

	@Autowired
	VoteQuestionDao voteQuestionDao;

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	SendMessageService sendMessageService;
	
	@Autowired
	private MeetingDao meetingDao;
	
	@Autowired
	private ToMemberDao toMemberDao;
	
	@Autowired
	private MemberCreditsInfoDao memberCreditsInfoDao;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void dealTimeJob() {
		execute();
	}
	
	public void execute() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = true;
				String endDate = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
				dealVoteQuestionEnd2Start(endDate);
				dealVoteQuestionEnd(endDate);
				
				dealMeetingStart(endDate.substring(0, 10),endDate.substring(11,16));
				dealMeetingEnd(endDate.substring(0, 10),endDate.substring(11,16));
				
				endDate = DateUtils.format(new Date());
				dealActivityStart(endDate);
				dealActivityEnd(endDate);
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

	/**
	 * 处理投票问卷 修改截止时间 结束-》发布中
	 * 
	 * @param endDate
	 * @throws Exception
	 */
	public void dealVoteQuestionEnd2Start(String endDate) throws Exception {
		Example example = new Example(VoteQuestion.class);
		Criteria criteria = example.createCriteria();
		criteria.andGreaterThan("endTime", endDate);
		criteria.andEqualTo("status", 2);
		// 查询状态=结束 并且结束时间>当前时间的记录
		List<VoteQuestion> list = voteQuestionDao.selectByExample(example);
		for (VoteQuestion voteQuestion : list) {
			// 状态设为发布中
			VoteQuestion v = new VoteQuestion();
			v.setId(voteQuestion.getId());
			v.setStatus(1);
			voteQuestionDao.updateByPrimaryKeySelective(v);
		}

	}

	/**
	 * 处理投票问卷 发布中-》结束
	 * 
	 * @param endDate
	 * @throws Exception
	 */
	public void dealVoteQuestionEnd(String endDate) throws Exception {
		Example example = new Example(VoteQuestion.class);
		Criteria criteria = example.createCriteria();
		criteria.andLessThan("endTime", endDate);
		criteria.andEqualTo("status", 1);
		// 查询状态=发布中 并且结束时间<当前时间的记录
		List<VoteQuestion> list = voteQuestionDao.selectByExample(example);
		for (VoteQuestion voteQuestion : list) {
			// 状态设为结束
			VoteQuestion v = new VoteQuestion();
			v.setId(voteQuestion.getId());
			v.setStatus(2);
			voteQuestionDao.updateByPrimaryKeySelective(v);
		}

	}

	/**
	 * 处理活动开始
	 * 
	 * @param endDate
	 * @throws Exception
	 */
	public void dealActivityStart(String starttime) throws Exception {
		Example example = new Example(Activity.class);
		Criteria criteria = example.createCriteria();
		criteria.andLessThan("starttime", starttime);
		criteria.andEqualTo("status", 0);
		// 查询状态=未开始 并且开始时间<当前时间的记录
		List<Activity> list = activityDao.selectByExample(example);
		for (Activity activity : list) {
			// 状态设为开始
			Activity v = new Activity();
			v.setId(activity.getId());
			v.setStatus(1);
			activityDao.updateByPrimaryKeySelective(v);
		}

	}

	/**
	 * 处理活动结束
	 * 
	 * @param endDate
	 * @throws Exception
	 */
	public void dealActivityEnd(String endDate) throws Exception {
		Example example = new Example(Activity.class);
		Criteria criteria = example.createCriteria();
		criteria.andLessThan("endtime", endDate);
		criteria.andEqualTo("status", 1);
		// 查询状态=进行中 并且结束时间<当前时间的记录
		List<Activity> list = activityDao.selectByExample(example);
		for (Activity activity : list) {
			// 状态设为结束
			Activity v = new Activity();
			v.setId(activity.getId());
			v.setStatus(2);
			activityDao.updateByPrimaryKeySelective(v);
		}

	}

	
	
	/**
	 * 处理会议开始
	 * @author chenjy
	 * @param endDate
	 * @throws Exception
	 */
	public void dealMeetingStart(String date,String startTime) throws Exception {
		Example example = new Example(Meeting.class);
		Criteria criteria = example.createCriteria();
		
		//进行中
		criteria.andEqualTo("meetingdate", date);
		criteria.andEqualTo("status", 1);
		criteria.andLessThan("timestart", startTime);
		criteria.andGreaterThan("timeend", startTime);
		
		List<Meeting> list = meetingDao.selectByExample(example);
		for (Meeting meeting : list) {
			// 状态设为开始
			Meeting m = new Meeting();
			m.setId(meeting.getId());
			m.setStatus(2);
			meetingDao.updateByPrimaryKeySelective(m);
		}

	}

	/**
	 * 处理会议结束
	 * @author chenjy
	 * @param endDate
	 * @throws Exception
	 */
	public void dealMeetingEnd(String date,String endTime) throws Exception {
		//已结束
		Example example = new Example(Meeting.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("meetingdate", date);
		criteria.andEqualTo("status", 2);
		criteria.andLessThan("timeend", endTime);
		List<Meeting> list = meetingDao.selectByExample(example);
		
		Example example1 = new Example(Meeting.class);
		Criteria criteria1 = example1.createCriteria();
		criteria1.andEqualTo("status", 2);
		criteria1.andLessThan("meetingdate", date);
		List<Meeting> list1 = meetingDao.selectByExample(example1);
		
		list.addAll(list1);
		
		for (Meeting meeting : list) {
			Meeting m = new Meeting();
			m.setId(meeting.getId());
			m.setStatus(3);
			meetingDao.updateByPrimaryKeySelective(m);
			addCredits(meeting);
		}

	}
	
	
	/**
	 * 会议结束后新增学分
	 * @author chenjy
	 * @param endDate
	 * @throws Exception
	 */
	public void addCredits(Meeting meeting) {
		//获取参加会议的所有参与人
		ToMember record = new ToMember();
		record.setUid(meeting.getMeetingid());
		record.setMembertype(2);
		List<ToMember> members = toMemberDao.select(record);
		for (ToMember member : members) {
			MemberCreditsInfo credits = new MemberCreditsInfo();
			credits.setMemberid(member.getMemberid());
			credits.setMeetingid(meeting.getMeetingid());
			credits.setMeetingtitle(meeting.getTitle());
			credits.setCredits(meeting.getCredits());
			credits.setMeetingdate(meeting.getMeetingdate());
			credits.setTimestart(meeting.getTimestart());
			credits.setTimeend(meeting.getTimeend());
			credits.setInserttime(new Timestamp(System.currentTimeMillis()).toString());
			credits.setUpdatetime(new Timestamp(System.currentTimeMillis()).toString());
			memberCreditsInfoDao.insert(credits);
		}
	}
}
