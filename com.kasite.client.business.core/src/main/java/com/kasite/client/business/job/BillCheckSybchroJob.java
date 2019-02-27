package com.kasite.client.business.job;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.business.module.backstage.bill.dao.BillCheckDao;
import com.kasite.client.pay.job.DownloadBillJob;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckVo;

/**
 * 异常账单同步JOB
 * 
 * @author zhaoy
 *
 */
@Component
public class BillCheckSybchroJob {

	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private static boolean flag = false;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	DownloadOrderCheckJob downloadOrderCheckJob;
	
	@Autowired
	DownloadHisBillJob downloadHisBillJob;
	
	@Autowired
	DownloadBillJob downloadBillJob;
	
	@Autowired
	GenBillCheckJob billCheckJob;
	
	@Autowired
	BillCheckDao billCheckDao;
	
	/**
	 * 异常账单同步作业(只有单边账的账单才可以同步)
	 * 		--
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				log.info("----------开始同步--------------");
				
				String startDate = DateUtils.getYesterdayStartString(new Date());
				String endDate = DateUtils.getYesterdayEndString(new Date());
				String date = DateUtils.getYesterdayString(new Date());
				Timestamp checkDate = DateOper.getNowDateTime();
				
				deal(startDate, endDate, date, checkDate);
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
	 * 重新下载当日账单,并重新对账
	 * 
	 * @param startDateDel
	 * @param endDateDel
	 * @param billDate
	 * @param checkDate
	 * @throws Exception
	 */
	public void deal(String startDateDel, String endDateDel, String billDate, Timestamp checkDate) throws Exception{
		List<BillCheckVo> billCheckList = billCheckDao.findExceptBillCheck(startDateDel, endDateDel);
		if(billCheckList != null && billCheckList.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(startDateDel);
			downloadBillJob.downloadBill(date);
			String downHisBillDate = DateOper.formatDate(date, "yyyyMMdd");
			downloadHisBillJob.deal(downHisBillDate, downHisBillDate, startDateDel, endDateDel);
			billCheckJob.deal(startDateDel, endDateDel, billDate, checkDate);
		}
		
		log.info("同步结束:共重新对账明细" + billCheckList.size() + "条记录!");
	}
}
