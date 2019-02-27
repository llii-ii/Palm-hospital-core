package com.kasite.client.qywechat.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.kasite.client.qywechat.bean.department.vo.Department;
import com.kasite.client.qywechat.bean.user.vo.User;
import com.kasite.client.qywechat.dao.MemberDao;
import com.kasite.client.qywechat.service.DepartmentService;
import com.kasite.client.qywechat.service.UserService;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.qywechat.dbo.Member;

import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 同步企微用户作业
 * 
 * @author 無
 *
 */
@Component
public class SynQyUserJob {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private boolean flag = true;

	@Autowired
	KasiteConfigMap config;

	@Autowired
	MemberDao memberDao;

	//@Scheduled(cron = "0 0/10 * * * ?")
	public void synQyUserJob() {
		execute();
	}

	public void execute() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = true;
				String wxkey = "1000002";
				System.out.println("开始同步企微用户数据...");
				deal(wxkey);
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

	public void deal(String wxkey) throws Exception {
		// 获取部门列表
		Map<String, Department> deptMap = new HashMap<String, Department>(16);
		JSONObject deptObject = DepartmentService.getDepartmentList("1", wxkey);
		net.sf.json.JSONArray deptArray = deptObject.getJSONArray("department");
		for (Object object : deptArray) {
			Gson gson = new Gson();
			Department department = gson.fromJson(object.toString(), Department.class);
			deptMap.put(department.getId() + "", department);
		}
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取成员列表
		JSONObject userObject = UserService.getDepartmentUserDetails(wxkey, "1", "1");
		net.sf.json.JSONArray userarray = userObject.getJSONArray("userlist");
		for (Object object : userarray) {
			Gson gson = new Gson();
			User user = gson.fromJson(object.toString(), User.class);
			// 判断是否存在
			Example example = new Example(Member.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("memberid", user.getUserid());
			Member member = memberDao.selectOneByExample(example);

			Member record = new Member();
			record.setMemberid(user.getUserid());
			record.setMembername(user.getName());
			record.setMemberavatar(user.getAvatar());
			record.setSex(user.getGender());
			record.setMobile(user.getMobile());
			record.setPosition(user.getPosition());
//			record.setDeptname(StringUtils.join(user.getDepartment(), ","));
			// 获取部门串
			String deptNames = "";
			String deptIds = "";
			for (String key : user.getDepartment()) {
				if (null != deptMap.get(key)) {
					Department department = deptMap.get(key);
					if (StringUtils.isEmpty(deptNames)) {
						deptNames = department.getName();
						deptIds = department.getId() + "";
					} else {
						deptNames = deptNames + "," + department.getName();
						deptIds = deptIds + "," + department.getId();
					}
				}
			}
			record.setDeptid(deptIds);
			record.setDeptname(deptNames);
			record.setUpdatetime(dateFormat.format(date));
			// 存在更新、否则插入
			if (member != null) {
				record.setId(member.getId());
				memberDao.updateByPrimaryKeySelective(record);
			} else {
				record.setInserttime(dateFormat.format(date));
				memberDao.insert(record);
			}
		}
	}

}
