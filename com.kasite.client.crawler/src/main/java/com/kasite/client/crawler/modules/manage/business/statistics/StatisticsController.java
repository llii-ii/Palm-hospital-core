package com.kasite.client.crawler.modules.manage.business.statistics;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.crawler.modules.manage.bean.statistics.dbo.Statistics;
import com.kasite.client.crawler.modules.manage.dao.statistics.StatisticsDao;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 数据统计控制类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月26日 上午16:54:30
 */
@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {
	
	private final static Log log = LogFactory.getLog(StatisticsController.class);
	
	@RequestMapping(value = "/getStatisticsList.do")
	@ResponseBody
	public String getOnlineList(HttpServletRequest request, HttpServletResponse response) {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String hospitalName = request.getParameter("hospitalName");
		
		Integer pageSize = StringUtil.intEmptyToNull(request.getParameter("pageSize"));
		Integer pageStart = StringUtil.intEmptyToNull(request.getParameter("PageStart"));
		JSONObject json = null;
		try {
			json = StatisticsDao.getStatisticsList(hospitalName, startDate, endDate, pageStart,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return CommonUtil.getRetVal(RetCode.Success.RET_10000.getCode(), RetCode.Success.RET_10000.getMessage(), json);
	}
	
	
	
	public static void addStatistics(Statistics statistics) throws Exception {
		String date = DateUtils.formatDateToString(statistics.getDate(),"yyyy-MM-dd");
		//int num = StatisticsDao.getStatiByHEidAndDate(statistics.getHospital_id(),statistics.getBusiness_id(),date).size();
		//if (num == 0) {
		statistics.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		Map map = (Map) StatisticsDao.getHos(statistics.getHospital_id()).get(0);
		statistics.setHospital_name((String)map.get("name"));
		StatisticsDao.addStatistics(statistics);
	}
}
