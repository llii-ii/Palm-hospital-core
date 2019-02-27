package com.kasite.client.business.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kasite.client.basic.job.BasicDataJob;
import com.kasite.client.basic.job.DoctorLocalCacheJob;
import com.kasite.client.core.msg.job.BackupMsgPushJob;
import com.kasite.client.core.msg.job.MsgQueueJob;
import com.kasite.client.netpay.job.SyncNetPayPublicKeyJob;
import com.kasite.client.order.job.BackupOrderJob;
import com.kasite.client.order.job.OrderDataCollectionJob;
import com.kasite.client.order.job.TimeOutOrderJob;
import com.kasite.client.pay.job.DownloadBillJob;
import com.kasite.client.pay.job.MerchantNotifyFailureRetryJob;
import com.kasite.client.pay.job.MerchantNotifyUnknowWarnJob;
import com.kasite.client.pay.job.MerchantOrderCheckJob;
import com.kasite.client.queue.job.QueueReMindNoJob;
import com.kasite.client.yy.job.BackupLockJob;
import com.kasite.client.yy.job.BackupUnLockJob;
import com.kasite.client.yy.job.BackupWaterJob;
import com.kasite.client.yy.job.DealLockJob;
/**
 * 封装各个模块所有的定时任务
 * 将定时任务的时间上浮到business进行配置下面每个定时任务都只做业务不做触发
 * @author daiyanshui
 *
 */
@Component
public class BusinessAllJob {

	@Autowired
	private BasicDataJob basicDataJob;
	@Autowired
	private DoctorLocalCacheJob doctorLocalCacheJob;
	@Autowired
	private BackupMsgPushJob backupMsgPushJob;
	@Autowired
	private BackupOrderJob backupOrderJob;
	@Autowired
	private OrderDataCollectionJob orderDataCollectoinJob;
	@Autowired
	private TimeOutOrderJob timeOutOrderJob;
	@Autowired
	private MerchantNotifyFailureRetryJob merchantNotifyFailureRetryJob;
	@Autowired
	private MerchantNotifyUnknowWarnJob merchantNotifyUnknowWarnJob;
	@Autowired
	private MerchantOrderCheckJob merchantOrderCheckJob;
	@Autowired
	private QueueReMindNoJob queueReMindNoJob;
	@Autowired
	private BackupLockJob backupLockJob;
	@Autowired
	private BackupUnLockJob backupUnLockJob;
	@Autowired
	private BackupWaterJob backupWaterJob;
	@Autowired
	private DealLockJob dealLockJob;
	
	@Autowired
	private DownloadBillJob downLoadBillJob;
	
	@Autowired
	private DownloadHisBillJob downloadHisBillJob;
	
	@Autowired
	private DownloadOrderCheckJob downloadOrderCheckJob;  
	
	@Autowired
	private GenBillCheckJob genBillCheckJob;
	
	@Autowired
	private GenBankMoneyCheckJob genBankMoneyCheckJob;
	
	@Autowired
	private BillCheckSybchroJob billCheckSybchroJob;
	
	@Autowired
	private GenPayWayDayStatisticsJob genPayWayDayStatisticsJob;
	
	@Autowired
	private ScheduleStopNoticeJob sheduleStopNoticeJob;
	
	@Autowired
	private GenChannelBankCheckJob genChannelBankCheckJob;
	
	@Autowired
	private MsgQueueJob msgQueueJob;

	@Autowired
	private SyncNetPayPublicKeyJob syncNetPayPublicKeyJob;
	
	@Scheduled(cron = "0/30 * * * * ?")
	public void msgQueueJob() {
		msgQueueJob.MsgCenterQueue();
	}
	
	/**
	 * 同步基础科室医生！
	 * 每天12点执行
	 * @Description:
	 */
	@Scheduled(cron = "0 0 12 * * ?")
	public void syncBasicDataJob() {
		// 这里捕获异常这么写，为了某些数据引起异常不影响其他数据继续新增或更新
		basicDataJob.syncBasicDataJob();
	}
	
	/**
	 * 医生缓存更新
	 * 每1小时执行一次
	 * @Description:
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void syncSingleDoctorCache() {
		doctorLocalCacheJob.syncSingleDoctorCache();
	}

	/**
	 * 备份清理锁号
	 * 每晚0点执行
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void backupMsgPushData() {
		backupMsgPushJob.backupMsgPushData();
	}
	
	/**
	 * 备份清理锁号
	 * 每晚0点执行
	 */
	@Transactional
	@Scheduled(cron="0 0 0 * * ?")
	public void backupOrderData() {
		backupOrderJob.backupOrderData();
	}
	/**
	 * 数据采集-线上门诊数的收集 
	 * 每天23点55分执行
	 * @Description:
	 */
	@Scheduled(cron = "0 55 23 * * ? ")
	public void doCollectData() {
		orderDataCollectoinJob.doCollectData();
	}
	
	
	/**
	 * 每10分钟执行一次
	 */
	@Transactional
	@Scheduled(cron="0 0/10 * * * ?")
	//@Scheduled(fixedDelay=5000,initialDelay =1000)测试用
	public void cancelTimeOutOrder() {
		timeOutOrderJob.cancelTimeOutOrder();
	}
	
	/**
	 * 上一次执行完毕，延迟5秒再执行
	 */
	@Scheduled(fixedDelay=5000,initialDelay =5000)
	public void merchantNotifyFailureRetry() {
		merchantNotifyFailureRetryJob.merchantNotifyFailureRetry();
	}
	/**
	 *每2小时执行一次
	 */
	@Scheduled(cron="0 0 0/2 * * ? ")
	public void merchantNotifyUnknowWarn() {
		merchantNotifyUnknowWarnJob.merchantNotifyUnknowWarn();
	}
	
	/**
	 * 系统启动5秒启动job,上一次执行完毕，延迟5秒再执行
	 */
	@Scheduled(fixedDelay=5000,initialDelay =5000)
	public void merchantOrderCheck() {
		merchantOrderCheckJob.merchantOrderCheck();
	}
	/**
	 * 候诊队列提醒
	 * 每5分钟执行
	 * @Description:
	 */
	@Scheduled(cron="0 0/5 * * * ?")
	public void queryQueueInfoListByToday() {
		queueReMindNoJob.queryQueueInfoListByToday();
	}
	/**
	 * 备份清理锁号
	 * 每晚0点执行
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void backupLockData() {
		backupLockJob.backupLockData();
	}
		
	/**
	 * 备份清理解锁表
	 * 每晚0点执行
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void backupUnLockData() {
		backupUnLockJob.backupUnLockData();
	}
	/**
	 * 备份清理锁号每晚0点执行
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void backupWaterData() {
		backupWaterJob.backupWaterData();
	}
	/**
	 * 解锁
	 * 每5分钟执行
	 * @Description:
	 */
	@Scheduled(cron="0 0/5 * * * ?")
	public void dealLockData() {
		dealLockJob.dealLockData();
	}
	
	/**
	 * 本地全流程订单下载
	 * 			--每天中午12点10执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 30 11 * * ? ")
	public void downloadOrderCheck(){
		downloadOrderCheckJob.execute();
	}
	
	/**
	 * 商户原始账单下载
	 * 			--每天10点00执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 00 10 * * ? ")
	public void downLoadBillJob_execute(){
		downLoadBillJob.execute();
	}
	
	/**
	 * His原始账单下载
	 * 			--每天中午10点20执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 20 10 * * ? ")
	public void downloadHisBill(){
		downloadHisBillJob.execute();
	}
	
	/**
	 * 开启对账
	 * 			--每天中午10点45执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 45 10 * * ? ")
	public void genBillCheck(){
		genBillCheckJob.execute();
	}
	
	/**
	 * 银行到账勾兑
	 * 			--每天中午11点00执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 00 11 * * ? ")
	public void genBankMoneyCheck(){
		genBankMoneyCheckJob.execute();
	}
	
	/**
	 * 首页交易统计
	 * 			--每天中午11点10执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 10 11 * * ? ")
	public void genPayWayDayStatistics(){
		genPayWayDayStatisticsJob.execute();
	}
	
	/**
	 * 渠道下银行账号金额统计JOB
	 * 			--每天中午11点20执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 20 11 * * ? ")
	public void genChannelBankCheckJob(){
		genChannelBankCheckJob.execute();
	}
	
	/**
	 * 异常账单同步
	 * 			--每天中午14点执行
	 * 
	 * @Description:
	 */
	@Scheduled(cron = "0 00 14 * * ? ")
	public void billCheckSybchro(){
		billCheckSybchroJob.execute();
	}
	
	@Scheduled(cron="0 0/30 * * * ?")
	public void sheduleStopNoticeJob() {
		sheduleStopNoticeJob.execute();
	}
	
	@Scheduled(cron="0  16 2 * * ? ")
	public void syncNetPayPublicKeyJob() {
		//招行一网通建议每天凌晨2:15发起查询并更新本地公钥
		syncNetPayPublicKeyJob.execute();
	}
	
}
