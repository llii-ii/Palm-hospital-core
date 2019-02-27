package com.kasite.client.crawler.modules.manage.business.workTime;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.crawler.modules.manage.bean.workTime.dbo.WorkTime;
import com.kasite.client.crawler.modules.manage.dao.workTime.WorkTimeDao;
import com.kasite.client.crawler.modules.utils.DateUtils;

/**
 * 作业时间控制类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年8月7日 上午17:54:30
 */
@Controller
@RequestMapping(value = "/workTime")
public class WorkTimeController {
	
	private final static Log log = LogFactory.getLog(WorkTimeController.class);
	
	
	/**
	 * 新增作业时间
	 * @param hospital_id 医院id 
	 * @param business_id 表id(例：HDSD00_77)
	 * @param report_time 上报时间
	 * @param check_time 校验时间
	 * @param convert_time 转换时间
	 * @param collect_time 采集时间
	 * @return "success"/"fail"
	 * */
	@RequestMapping(value = "/addWorkTime.do")
	@ResponseBody
	public String addWorkTime(String date,String hospital_id,int report_time,int check_time,int convert_time,int collect_time) {
		WorkTime workTime = new WorkTime();
		workTime.setHospital_id(hospital_id);
		workTime.setWork_date(DateUtils.formatStringtoDate(date, "yyyy-MM-dd"));
		try {
			WorkTime select = WorkTimeDao.selectWorkTime(workTime);
			if (select == null) {
				workTime.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				workTime.setReport_time(report_time);
				workTime.setCheck_time(check_time);
				workTime.setCollect_time(collect_time);
				workTime.setConvert_time(convert_time);
				WorkTimeDao.addWorkTime(workTime);
			}else {
				WorkTime update = new WorkTime();
				update.setId(select.getId());
				report_time += select.getReport_time();
				check_time += select.getCheck_time();
				convert_time += select.getConvert_time();
				collect_time += select.getCollect_time();
				update.setReport_time(report_time);
				update.setCheck_time(check_time);
				update.setCollect_time(collect_time);
				update.setConvert_time(convert_time);
				WorkTimeDao.updateWorkTime(update);
			}

			return "success";
		} catch (Exception e) {
			return "fail";
		}
	}
}
