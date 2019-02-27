package com.kasite.core.common.resp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.github.pagehelper.Page;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.constant.RetCode.Success;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.util.LogUtil;
import com.yihu.hos.IRetCode;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:28:03 
 * TODO 公共回参对象
 */
public class CommonResp<T>{
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_DEFAULT);
	protected String transactionCode;
	protected String code;
	protected String message;
	protected CommonReq<?> reqParam;
	// 当前页数量
	private Integer PSize = 0;
	// 当前页码
	private Integer PIndex = 0;
	// 总记录数
	private Long PCount = 0L;
	// 数据集合
	private List<T> data;
	
	private IRetCode retCode;
	

	
	public CommonResp(CommonReq<?> reqParam, IRetCode retCode, String string) {
		this.reqParam = reqParam;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.message =  string;
		parseCallLog(reqParam);
	}
	/**
	 * 业务执行异常时返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 */
	public CommonResp(CommonReq<?> reqParam,String transactionCode, IRetCode retCode) {
		this.reqParam = reqParam;
		this.transactionCode = transactionCode;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.message = retCode.getMessage();
		parseCallLog(reqParam);
	}
	
	/**
	 * 判断是否是系统调用的方法如果是系统主动调用的方法，前端入口不会记录日志。只能后端单独来记录调用日志。比如作业系统调用的方法不会在入口调用日志中体现。
	 * @param reqParam
	 */
	private void parseCallLog(CommonReq<?> reqParam) {
		String s = reqParam.getMsg().getVersion();
		if(StringUtil.isNotBlank(s) && !"".equals(s.trim())) {
			this.message = s;
		}
		if(null != reqParam) {
			AuthInfoVo authInfo = reqParam.getParam().getAuthInfo();
			if(null == authInfo) {
				String uuid = UUID.randomUUID().toString();
				authInfo = KasiteConfig.createAuthInfoVo(uuid);
			}
			boolean isSystem = KasiteConfig.isSystemCall(authInfo);
			if(isSystem) {
				String req = "非xml入参";
				AbsReq param = reqParam.getParam();
				if(null != param) {
					Document doc = param.getDoc();
					if(null != doc) {
						req = doc.asXML();
					}
				}
				LogBody logbody = new LogBody(authInfo).set("ReqParam",req)
				.set("RespCode", this.code)
				.set("RespMessage",this.message);
				if(!KstHosConstant.SUCCESSCODE.equals(this.code) && null != data && data.size() > 0) {
					try {
						logbody.set("Result", JSON.toJSON(data));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				LogUtil.info(log, logbody);
			}
		}
	}
	/**
	 * 判断是否是系统调用的方法如果是系统主动调用的方法，前端入口不会记录日志。只能后端单独来记录调用日志。比如作业系统调用的方法不会在入口调用日志中体现。
	 * @param reqParam
	 */
	private void parseCallLog(InterfaceMessage msg) {
		if(null != msg) {
			AuthInfoVo authInfo = KasiteConfig.getAuthInfo(msg);
			if(null == authInfo) {
				String uuid = UUID.randomUUID().toString();
				authInfo = KasiteConfig.createAuthInfoVo(uuid);
			}
			boolean isSystem = KasiteConfig.isSystemCall(authInfo);
			if(isSystem) {
				String req = msg.getParam();
				LogBody logbody = new LogBody(authInfo).set("ReqParam",req)
				.set("RespCode", this.code)
				.set("RespMessage",this.message);
				if(!KstHosConstant.SUCCESSCODE.equals(this.code) && null != data && data.size() > 0) {
					try {
						logbody.set("Result", JSON.toJSON(data));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				LogUtil.info(log, logbody);
			}
		}
	}
	/**
	 * 
	 * 业务执行异常时返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(CommonReq<?> reqParam,String transactionCode, IRetCode retCode,String message) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		this.message = message;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		parseCallLog(reqParam);
	}
	
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(CommonReq<?> reqParam,String transactionCode, RetCode.Success retCode,String message) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.message = 	message;
		this.retCode = retCode;
		parseCallLog(reqParam);
	}
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(CommonReq<?> reqParam,RetCode.Success retCode,T resp) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.data = new ArrayList<>(1);
		this.data.add(resp);
		parseCallLog(reqParam);
	}
	/**
	 * 
	 * 业务失败返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(InterfaceMessage msg,Map<String, String> map,IRetCode retCode,String message) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.message = message;
		parseCallLog(msg);
	}
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(InterfaceMessage msg,Map<String, String> map,RetCode.Success retCode,T resp) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.data = new ArrayList<>(1);
		this.data.add(resp);
		parseCallLog(msg);
	}
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(InterfaceMessage msg,RetCode.Success retCode,List<T> resp) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.data = resp;
		parseCallLog(msg);
	}
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(InterfaceMessage msg,RetCode.Success retCode) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		parseCallLog(msg);
	}
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(InterfaceMessage msg,RetCode.Success retCode,T resp) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.data = new ArrayList<>(1);
		this.data.add(resp);
		parseCallLog(msg);
	}
	public CommonResp(CommonReq<?> req, Success retCode) {
		if(null != req && null != req.getParam()) {
			this.transactionCode = req.getParam().getTransactionCode();
		}
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.message = retCode.getMessage();
		this.data = new ArrayList<>(1);
		parseCallLog(req);
	}
	/**
	 * 
	 * 业务执行成功返回
	 * @param reqParam
	 * @param transactionCode
	 * @param retCode
	 * @param message 
	 */
	public CommonResp(InterfaceMessage msg,Map<String, String> map,RetCode.Success retCode) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.data = new ArrayList<>(1);
		parseCallLog(msg);
	}
	/**
	 * 成功时返回的内容
	 * @param transactionCode
	 * @param retCode
	 * @param reqParam
	 * @param resp
	 */
	public CommonResp(InterfaceMessage msg,Map<String, String> map,RetCode.Success retCode,List<T> respList) {
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.message = retCode.getMessage();
		this.data = respList;
		parseCallLog(msg);
	}
	/**
	 * 成功时返回的内容
	 * @param transactionCode
	 * @param retCode
	 * @param reqParam
	 * @param resp
	 */
	public CommonResp(CommonReq<?> reqParam,String transactionCode, RetCode.Success retCode,  T resp) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
		}
		this.retCode = retCode;
		this.message = retCode.getMessage();
		this.data = new ArrayList<>(1);
		data.add(resp);
		parseCallLog(reqParam);
	}
	public CommonResp(CommonReq<?> reqParam,String transactionCode, IRetCode retCode,  T resp) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
			this.message = retCode.getMessage();
		}
		this.retCode = retCode;
		this.data = new ArrayList<>(1);
		data.add(resp);
		parseCallLog(reqParam);
	}
	public CommonResp(CommonReq<?> reqParam,IRetCode retCode,  T resp) {
		this.transactionCode = KstHosConstant.DEFAULTTRAN;
		if(null != retCode && null != retCode.getCode()) {
			this.code = retCode.getCode().toString();
			this.message = retCode.getMessage();
		}
		this.retCode = retCode;
		this.data = new ArrayList<>(1);
		data.add(resp);
		parseCallLog(reqParam);
	}
	public CommonResp(CommonReq<?> reqParam,String transactionCode, IRetCode code,String message,  T resp) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		this.code = code.getCode().toString();;
		this.message = message;
		this.data = new ArrayList<>(1);
		data.add(resp);
		parseCallLog(reqParam);
	}
	
	/**
	 * 成功时返回的内容
	 * @param transactionCode
	 * @param retCode
	 * @param reqParam
	 * @param resp
	 */
	public CommonResp(CommonReq<?> reqParam,
			String transactionCode, 
			RetCode.Success retCode,  
			List<T> respList) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		this.code = retCode.getCode().toString();
		this.retCode = retCode;
		this.message = retCode.getMessage();
		this.data = respList;
		parseCallLog(reqParam);
	}
	/**
	 * 成功时返回的内容
	 * @param transactionCode
	 * @param retCode
	 * @param reqParam
	 * @param resp
	 */
	public CommonResp(CommonReq<?> reqParam,
			String transactionCode, 
			RetCode.Success retCode,  
			List<T> respList,
			PageVo pageVo) {
		if(null != reqParam && null != reqParam.getParam()) {
			this.transactionCode = reqParam.getParam().getTransactionCode();
		}
		this.code = retCode.getCode().toString();
		this.retCode = retCode;
		this.message = retCode.getMessage();
		this.data = respList;
		if(null != pageVo) {
			this.PIndex = pageVo.getPIndex();
			this.PCount = pageVo.getPCount().longValue();
			this.PSize = pageVo.getPSize();
		}
		parseCallLog(reqParam);
	}


	public List<T> getList() {
		return data;
	}
	/**
	 * 用于获取结果集并判断返回值是否为 10000 如果不是到话就直接抛出异常，请外部做好捕获异常后的判断
	 * @return
	 * @throws RRException
	 */
	public List<T> getListCaseRetCode() throws RRException{
		check();
		return data;
	}
	/**
	 * 用于获取结果集并判断返回值是否为 10000 如果不是到话就直接抛出异常，请外部做好捕获异常后的判断
	 * @return
	 * @throws RRException
	 */
	public T getDataCaseRetCode() throws RRException{
		check();
		if(data != null && data.size() >0) {
			return data.get(0);
		}
		return null;
	}
	public void setList(List<T> list) {
		this.data = list;
	}

	public Long getPCount() {
		return PCount;
	}

	public void setPCount(Long count) {
		PCount = count;
	}

	public Integer getPSize() {
		return PSize;
	}

	public Integer getPIndex() {
		return PIndex;
	}

	public void setPSize(Integer size) {
		PSize = size;
	}

	public void setPIndex(Integer index) {
		PIndex = index;
	}
	
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 当明确返回的结果集只有一条的时候，直接取返回结果集 方便前端操作
	 * @return
	 */
	public T getResultData() {
		if(null != data && data.size() == 1) {
			return data.get(0);
		}
		return null;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
//	public Element getRoot() {
//		return root;
//	}
//	public void setRoot(Element root) {
//		this.root = root;
//	}
//	public Document getDoc() {
//		return doc;
//	}
//	public void setDoc(Document doc) {
//		this.doc = doc;
//	}
	public CommonResp<T> PIndex(int pIndex) {
		this.PIndex = pIndex;
		return this;
	}
	public CommonResp<T> PSize(int pSize) {
		this.PSize = pSize;
		return this;
	}
	public CommonResp<T> PCount(long pCount) {
		this.PCount = pCount;
		return this;
	}
	public CommonResp<T> transactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
		return this;
	}
	public CommonResp<T> code(String code) {
		this.code = code;
		return this;
	}
	public CommonResp<T> Message(String message) {
		this.message = message;
		return this;
	}

	public CommonResp<T> list(List<T> list) {
		this.data = list;
		if (list instanceof Page) {
			Page<T> page = (Page<T>) list;
			this.PIndex = page.getPageNum();
			this.PSize = page.getPageSize();
			this.PCount = page.getTotal();
		}
		return this;
	}
	
	/**
	 * 校验返回值， 返回异常则抛出异常
	 * @throws Exception 
	 */
	public void check() throws RRException {
		/*
		 * 判断返回的内容是否是10000
		 */
		if(!this.code.equals(RetCode.Success.RET_10000.getCode().toString())) {
			if(StringUtil.isNotBlank(message)) {
				throw new RRException(message,retCode.getCode());
			}
			throw new RRException(retCode);
		}
	}
	
	public String toResult() throws RRException { 
		return Resp.create(this).parseString();
	}
	public String toNotDataResult() throws RRException { 
		return Resp.create(RespType.XML_NODATA,this).parseString();
	}
	public JSONObject toJSONResult() throws RRException { 
		return Resp.create(RespType.JSON,this).parseJSON();
	}
	public IRetCode getRetCode() {
		return retCode;
	}

}
