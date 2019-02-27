package com.kasite.client.crawler.modules.manage.business.report;

import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.crawler.modules.manage.bean.report.dbo.Report;
import com.kasite.client.crawler.modules.manage.dao.online.OnlineDao;
import com.kasite.client.crawler.modules.manage.dao.report.ReportDao;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 数据占比控制类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年6月25日 上午16:54:30
 */
@Controller
@RequestMapping(value = "/report")
public class ReportController {
	
	private final static Log log = LogFactory.getLog(ReportController.class);
	
	//按 hospital_id 获得上报数；
	@RequestMapping(value = "/reportByhosId.do")
	@ResponseBody
	public String reportByhosId(HttpServletRequest request, HttpServletResponse response,String date) {
		String municipal = request.getParameter("municipal");
		String county = request.getParameter("county");
		String hosName = request.getParameter("hosName");
		JSONObject json = null;
		try {
			json = ReportDao.reportByhosId(municipal,county,hosName,date);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	//按 business_id 获得上报数；
	@RequestMapping(value = "/reportByBusId.do")
	@ResponseBody
	public String reportByBusId(HttpServletRequest request, HttpServletResponse response) {
		Integer PageSize = StringUtil.intEmptyToNull(request.getParameter("PageSize"));
		Integer PageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		String municipal = request.getParameter("municipal");
		String county = request.getParameter("county");
		String hosName = request.getParameter("hosName");

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		JSONObject json = null;
		try {
			json = ReportDao.reportByBusId(PageSize,PageStart,municipal,county,hosName,startTime,endTime);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	//按 date 获得上报数；
	@RequestMapping(value = "/reportByDate.do")
	@ResponseBody
	public String reportByDate(HttpServletRequest request, HttpServletResponse response) {
		Integer PageSize = StringUtil.intEmptyToNull(request.getParameter("PageSize"));
		Integer PageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		String municipal = request.getParameter("municipal");
		String county = request.getParameter("county");
		String hosName = request.getParameter("hosName");

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		JSONObject json = null;
		try {
			json = ReportDao.reportByDate(PageSize,PageStart,municipal,county,hosName,startTime,endTime);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	//按 hospital_id,date 获得上报数；
	@RequestMapping(value = "/reportByhosIdAndDate.do")
	@ResponseBody
	public String reportByhosIdAndDate(HttpServletRequest request, HttpServletResponse response) {
		Integer PageSize = StringUtil.intEmptyToNull(request.getParameter("PageSize"));
		Integer PageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		String municipal = request.getParameter("municipal");
		String county = request.getParameter("county");
		String hosName = request.getParameter("hosName");

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		JSONObject json = null;
		try {
			json = ReportDao.reportByhosIdAndDate(PageSize,PageStart,municipal,county,hosName,startTime,endTime);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	//获取作业效率-表
	@RequestMapping(value = "/getEfficiency.do")
	@ResponseBody
	public String getEfficiency(HttpServletRequest request, HttpServletResponse response) {
		Integer PageSize = StringUtil.intEmptyToNull(request.getParameter("PageSize"));
		Integer PageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		String municipal = request.getParameter("municipal");
		String county = request.getParameter("county");
		String hosName = request.getParameter("hosName");

		String date = request.getParameter("date");
		JSONObject json = null;
		try {
			json = ReportDao.getEfficiency(PageSize,PageStart,municipal,county,hosName,date);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	//获取作业效率-图
	@RequestMapping(value = "/getChartEfficiency.do")
	@ResponseBody
	public String getChartEfficiency(HttpServletRequest request, HttpServletResponse response) {

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		JSONObject json = null;
		try {
			json = ReportDao.getChartEfficiency(startTime,endTime);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	/**
	 * 新增上报
	 * @param hospital_id 医院id 
	 * @param business_id 表id(例：HDSD00_77) business_name 表名(例：检验-报告单)
	 * @param report_false report_true 错误/正确上报数
	 * @param check_num 校验数 
	 * @param convert_num 转换数 
	 * @return "success"/"fail"
	 * */
	@RequestMapping(value = "/addReport.do")
	@ResponseBody
	public String addReport(String date,String hospital_id,String business_id,String business_name,int report_false,int report_true,int check_num,int convert_num) {
		Report report = new Report();
		report.setHospital_id(hospital_id);
		report.setBusiness_id(business_id);
		report.setDate(DateUtils.formatStringtoDate(date, "yyyy-MM-dd"));
		try {
			Report reportSelect =  ReportDao.reportSelect(report);
			if (reportSelect == null ) {
				report.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				String hosName = OnlineDao.getOnlineById(hospital_id).getName();
				report.setHospital_name(hosName);
				report.setBusiness_name(business_name);
				report.setReport_false(report_false);
				report.setReport_true(report_true);
				report.setCheck_num(check_num);
				report.setConvert_num(convert_num);
				ReportDao.addReport(report);				
			}else {
				Report updateReport = new Report();
				updateReport.setId(reportSelect.getId());
				report_true += reportSelect.getReport_true();
				report_false += reportSelect.getReport_false();
				check_num += reportSelect.getCheck_num();
				convert_num += reportSelect.getConvert_num();
				updateReport.setReport_false(report_false);
				updateReport.setReport_true(report_true);
				updateReport.setCheck_num(check_num);
				updateReport.setConvert_num(convert_num);
				ReportDao.updateReport(updateReport);
			}
			return "success";
		} catch (SQLException e) {
			return "fail";
		}
	}
	
}
