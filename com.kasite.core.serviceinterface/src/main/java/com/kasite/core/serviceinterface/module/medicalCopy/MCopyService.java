package com.kasite.core.serviceinterface.module.medicalCopy;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.FengQiaoLogisticsVo;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqConsigneeAddress;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyContent;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyPurpose;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqMCopyUser;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqOrderCase;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqPriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqQueryMedicalRecords;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqQueryPatientInfoByNos;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyQuestion;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqSetting;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqTransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqVerification;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespConsigneeAddress;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespCopyContent;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespCopyPurpose;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespMCopyUser;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespOrderCase;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespPriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespQueryMedicalRecords;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespQueryPatientInfoByNos;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespCopyQuestion;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespSetting;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespTransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespVerification;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.wechat.req.ReqMcopyWechat;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author cjy
 * TODO
 */
public interface MCopyService{
	/**
	 * 根据病案号获取病人信息
	 * @param msg
	 * @return
	 */
	public String QueryPatientInfoByNos(InterfaceMessage msg) throws Exception;
	CommonResp<RespQueryPatientInfoByNos> queryPatientInfoByNos(CommonReq<ReqQueryPatientInfoByNos> req) throws Exception;
	
	/**
	 * 根据病案号获取病人病历
	 * @param msg
	 * @return
	 * */
	public String QueryMedicalRecords(InterfaceMessage msg) throws Exception;
	CommonResp<RespQueryMedicalRecords> queryMedicalRecords(CommonReq<ReqQueryMedicalRecords> req) throws Exception;
	
	
	/**
	 * 根据id获取病人病历
	 * @param msg
	 * @return
	 * */
	public String GetMedicalRecordsById(InterfaceMessage msg) throws Exception;
	CommonResp<RespQueryMedicalRecords> getMedicalRecordsById(CommonReq<ReqQueryMedicalRecords> req) throws Exception;
	
	
	/**
	 * 根据id获取快递订单信息
	 * @param msg
	 * @return
	 * */
	public String QueryExpressOrderById(InterfaceMessage msg) throws Exception;
	CommonResp<RespExpressOrder> queryExpressOrderById(CommonReq<ReqExpressOrder> req) throws Exception;	
	
	/**
	 * 通过id获得病人信息
	 * @param msg
	 * @return
	 */
	public String QueryPatientInfoById(InterfaceMessage msg) throws Exception;
	CommonResp<RespMCopyUser> queryPatientInfoById(CommonReq<ReqMCopyUser> req) throws Exception;
	
	/**
	 * 新增一条快递单
	 * @param msg
	 * @return
	 */
	public String AddExpressOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespExpressOrder> addExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 新增一条复印单
	 * @param msg
	 * @return
	 */
	public String CreateExpressOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespExpressOrder> createExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 统一下单后续操作
	 * @param msg
	 * @return
	 */
	public String AfterUnderOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> afterUnderOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;

	/**
	 * 获得快递单列表 后台有连表
	 * @param msg
	 * @return
	 */
	public String GetExpressOrderList(InterfaceMessage msg) throws Exception;
	CommonResp<RespExpressOrder> getExpressOrderList(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 获得快递单列表 微信端无连表
	 * @param msg
	 * @return
	 */
	String GetOrderList(InterfaceMessage msg) throws Exception;
	CommonResp<RespExpressOrder> getOrderList(CommonReq<ReqExpressOrder> commonReq) throws Exception;	
	
	/**
	 * 导出复印申请表
	 * @param msg
	 * @return
	 */
	String ExportExpressOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> exportExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	/**
	 * 修改快递单
	 * @param msg
	 * @return
	 */
	String UpdateOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespExpressOrder> updateOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 审核复印单
	 * @param msg
	 * @return
	 */
	String CheckExpressOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> checkExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 确认订单费用
	 * @param msg
	 * @return
	 */
	String CheckExpressOrderFee(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> checkExpressOrderFee(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 发送订单
	 * @param msg
	 * @return
	 */
	String SendExpressOrder(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> sendExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 获取费用管理信息
	 * @param msg
	 * @return
	 */
	String GetPriceManageInfo(InterfaceMessage msg) throws Exception;
	CommonResp<RespPriceManage> getPriceManageInfo(CommonReq<ReqPriceManage> commonReq) throws Exception;	
	
	/**
	 * 修改费用管理信息
	 * @param msg
	 * @return
	 */
	String UpdatePriceManage(InterfaceMessage msg) throws Exception;
	CommonResp<RespPriceManage> updatePriceManage(CommonReq<ReqPriceManage> commonReq) throws Exception;
	
	/**
	 * 获得交易记录
	 * @param msg
	 * @return
	 */
	String GetTransactionRecord(InterfaceMessage msg) throws Exception;
	CommonResp<RespTransactionRecord> getTransactionRecord(CommonReq<ReqTransactionRecord> commonReq) throws Exception;
	
	/**
	 * 身份证和手机验证码验证判断
	 * @param msg
	 * @return
	 */
	String Verification(InterfaceMessage msg) throws Exception;
	CommonResp<RespVerification> verification(CommonReq<ReqVerification> commonReq) throws Exception;
	
	/**
	 * 判断病历是否在30内已经有复印
	 * @param msg
	 * @return
	 */
	String IsCopyBy30Day(InterfaceMessage msg) throws Exception;
	CommonResp<RespOrderCase> isCopyBy30Day(CommonReq<ReqOrderCase> commonReq) throws Exception;
	
	/**
	 * 退款
	 * @param msg
	 * @return
	 */
	String Refund(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> refund(CommonReq<ReqOrderIsCancel> reqRefund) throws Exception;
	
	/**
	 * 新增交易记录
	 * @param msg
	 * @return
	 */
	String AddTransactionRecord(InterfaceMessage msg) throws Exception;
	CommonResp<RespTransactionRecord> addTransactionRecord(CommonReq<ReqTransactionRecord> reqRefund) throws Exception;
	
	/**
	 * 修改交易记录
	 * @param msg
	 * @return
	 */
	String UpdateTransactionRecord(InterfaceMessage msg) throws Exception;
	CommonResp<RespTransactionRecord> updateTransactionRecord(CommonReq<ReqTransactionRecord> commonReq)
			throws Exception;
	
	/**
	 * 获得wxconfig配置签名
	 * */
	String GetWxConfigInfo(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> getWxConfigInfo(CommonReq<ReqMcopyWechat> commonReq)throws Exception;
	
	
	/**
	 * 微信发送图文信息
	 * */
	String WxSendMsg(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> wxSendMsg(CommonReq<ReqExpressOrder> commonReq)throws Exception;
	
	/**
	 * 添加收件人地址
	 * */
	String AddConsigneeAddress(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> addConsigneeAddress(CommonReq<ReqConsigneeAddress> commonReq)throws Exception;
	
	/**
	 * 修改收件人地址
	 * */
	String UpdateConsigneeAddress(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> updateConsigneeAddress(CommonReq<ReqConsigneeAddress> commonReq)throws Exception;
	
	/**
	 * 获取收件人地址列表
	 * */
	String GetConsigneeAddressList(InterfaceMessage msg) throws Exception;
	CommonResp<RespConsigneeAddress> getConsigneeAddressList(CommonReq<ReqConsigneeAddress> commonReq)throws Exception;
	/**
	 * 下载微信服务器上的临时素材
	 * */
	//String GetWechatMedia(InterfaceMessage msg) throws Exception;
	//CommonResp<RespMap> getWechatMedia(CommonReq<ReqMcopyWechat> commonReq)throws Exception;
	
	/**
	 * 验证身份证及户口本
	 * @param msg
	 * @return
	 */
	String IdCardVerification(InterfaceMessage msg) throws Exception;
	CommonResp<RespVerification> idCardVerification(CommonReq<ReqVerification> commonReq) throws Exception;
	
	/**
	 * 添加复印内容
	 * */
	String AddCopyContent(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> addCopyContent(CommonReq<ReqCopyContent> commonReq)throws Exception;
	
	/**
	 * 修改复印内容
	 * */
	String UpdateCopyContent(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> updateCopyContent(CommonReq<ReqCopyContent> commonReq)throws Exception;
	
	/**
	 * 获取复印内容列表
	 * */
	String GetCopyContentList(InterfaceMessage msg) throws Exception;
	CommonResp<RespCopyContent> getCopyContentList(CommonReq<ReqCopyContent> commonReq)throws Exception;
	
	/**
	 * 添加复印用途
	 * */
	String AddCopyPurpose(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> addCopyPurpose(CommonReq<ReqCopyPurpose> commonReq)throws Exception;
	
	/**
	 * 修改复印用途
	 * */
	String UpdateCopyPurpose(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> updateCopyPurpose(CommonReq<ReqCopyPurpose> commonReq)throws Exception;
	
	/**
	 * 获取复印用途列表
	 * */
	String GetCopyPurposeList(InterfaceMessage msg) throws Exception;
	CommonResp<RespCopyPurpose> getCopyPurposeList(CommonReq<ReqCopyPurpose> commonReq)throws Exception;
	
	/**
	 * 前端获取收费信息
	 * */
	String FrontGetPriceManage(InterfaceMessage msg) throws Exception;
	CommonResp<RespPriceManage> frontGetPriceManage(CommonReq<ReqPriceManage> commonReq)throws Exception;
	
	/**
	 * 根据id获取基本设置
	 * */
	String GetSettingById(InterfaceMessage msg) throws Exception;
	CommonResp<RespSetting> getSettingById(CommonReq<ReqSetting> commonReq)throws Exception;
	
	/**
	 * 修改基本设置
	 * */
	String UpdateSetting(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> updateSetting(CommonReq<ReqSetting> commonReq)throws Exception;
	
	/**
	 * 添加常用问题
	 * */
	String AddQuestion(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> addQuestion(CommonReq<ReqCopyQuestion> commonReq)throws Exception;
	
	/**
	 * 修改常用问题
	 * */
	String UpdateQuestion(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> updateQuestion(CommonReq<ReqCopyQuestion> commonReq)throws Exception;
	
	/**
	 * 获取常用问题列表
	 * */
	String GetQuestionList(InterfaceMessage msg) throws Exception;
	CommonResp<RespCopyQuestion> getQuestionList(CommonReq<ReqCopyQuestion> commonReq)throws Exception;
	
	/**
	 * 获取签名
	 * */
	String GetSignature(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> getSignature(CommonReq<ReqExpressOrder> commonReq)throws Exception;
	
	/**
	 * 获取枫桥物流信息
	 * @param msg
	 * @return
	 */
	String GetFengQiaoLogistics(InterfaceMessage msg) throws Exception;
	CommonResp<FengQiaoLogisticsVo> getFengQiaoLogistics(CommonReq<ReqExpressOrder> commonReq) throws Exception;
	
	/**
	 * 根据病案号(身份证)获取病人信息
	 * @param msg
	 * @return
	 */
	public String QueryPatientInfo(InterfaceMessage msg) throws Exception;
	CommonResp<RespQueryPatientInfoByNos> queryPatientInfo(CommonReq<ReqQueryPatientInfoByNos> req) throws Exception;
	
	/**
	 * 根据病案号(身份证)获取病人病历
	 * @param msg
	 * @return
	 * */
	public String GetMedicalRecords(InterfaceMessage msg) throws Exception;
	CommonResp<RespQueryMedicalRecords> getMedicalRecords(CommonReq<ReqQueryMedicalRecords> req) throws Exception;
	
	/**
	 * 统计订单流水
	 * @param msg
	 * @return
	 * */
	public String CountTransactionRecord(InterfaceMessage msg) throws Exception;
	CommonResp<RespTransactionRecord> countTransactionRecord(CommonReq<ReqTransactionRecord> req) throws Exception;
	
	/**
	 * 导出订单流水
	 * @param msg
	 * @return
	 * */
	public String ExportTransactionRecord(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> exportTransactionRecord(CommonReq<ReqTransactionRecord> req) throws Exception;
	
	/**
	 * 订单补缴
	 * @param msg
	 * @return
	 * */
	public String SupplementaryPay(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> supplementaryPay(CommonReq<ReqExpressOrder> req) throws Exception;
}
