package com.kasite.core.common.req;

import org.dom4j.Document;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public abstract class AbsReq {
	protected InterfaceMessage __INTERFACEMESSAGE__;
	protected PageVo __PAGE__;
	protected String __CONFIGKEY__;
	protected AuthInfoVo __AUTHINFO__;
	protected String getElementStringValue(Element element,String node,Boolean isMust) throws AbsHosException{
		String val = "";
		Element e = element.element(node);
		if(null == e && isMust == true){
			throw new ParamException(element.asXML() + " 节点中："+ node +"  为必传节点你传入的节点中没有找到该节点。");
		}
		if(null != e){
			return e.getTextTrim();
		}
		return val;
	}

	protected String getElementStringValue(Element element,String node,String defaultVal) throws AbsHosException{
		String val = defaultVal;
		Element e = element.element(node);
		if(null != e){
			return e.getTextTrim();
		}
		return val;
	}
	
	public String getConfigKey() {
		return __CONFIGKEY__;
	}

	protected Integer getElementIntegerValue(Element element,String node,Boolean isMust) throws AbsHosException{
		
		Integer val = 0;
		
		Element e = element.element(node);
		if(null == e && isMust == true){
			throw new ParamException(element.asXML() + " 节点中："+ node +"  为必传节点你传入的节点中没有找到该节点。");
		}
		if(null != e){
			String v = e.getTextTrim();
			if(StringUtil.isNotBlank(v)){
				try{
					val = Integer.parseInt(v);
				}catch (NumberFormatException ex) {
					throw new ParamException("节点中的数据转成整形出错。Node = "+ node +" Service = "+ element.asXML());
				}
			}
		}
		return val;
	}
	protected String CALLHISREQMODULEPARAM___;
	public String getHisReqXml(){
		return CALLHISREQMODULEPARAM___;
	}
	protected Document doc;
	
	protected Element root;
	
	protected Element __DATA__;
	
	protected JSONObject _DATAJS;
	
	public AuthInfoVo getAuthInfo() {
		return this.__AUTHINFO__;
	}
	
	public AbsReq(InterfaceMessage msg) throws AbsHosException{
		this.__INTERFACEMESSAGE__ = msg;
		if( null != msg){
			try {
				this.__AUTHINFO__ = KasiteConfig.parse(msg);
				this.__CONFIGKEY__ = __AUTHINFO__.getConfigKey();
				String param = msg.getParam();
				if(StringUtil.isNotBlank(param)){
					if(msg.getParamType() == 1){
						this.doc = XMLUtil.parseXml(param);
						if(null != doc) {
							this.root = doc.getRootElement();
							this.transactionCode = getElementStringValue(root, IConstant.TRANSACTIONCODE, false);
							this.columns = getElementStringValue(root, IConstant.COLUMNS, false);
							this.__DATA__ = root.element(IConstant.DATA);
							Element ser = __DATA__;
							if(ser == null){
								ser = root.element("Service");
							}
							if(ser==null){
								throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
							}
							Element page = ser.element(IConstant.PAGE);
							__PAGE__ = new PageVo();
							if(page==null){
								__PAGE__.setPIndex(0);
								__PAGE__.setPSize(0);
								__PAGE__.setPCount(0);
							}
							if(null != page){
								Integer PIndex = XMLUtil.getInt(page, IConstant.PINDEX, false);
								Integer PSize = XMLUtil.getInt(page, IConstant.PSIZE, false);
								__PAGE__.setPIndex(PIndex);
								__PAGE__.setPSize(PSize);
							}
						}
					}else if(msg.getParamType() == 0){
						JSONObject paramJs = JSONObject.parseObject(msg.getParam());
						JSONObject reqJs = paramJs.getJSONObject(IConstant.REQ);
						this.transactionCode = reqJs.getString(IConstant.TRANSACTIONCODE);
						this._DATAJS = reqJs.getJSONObject(IConstant.DATA);
						if(this._DATAJS.containsKey(IConstant.PAGE)) {
							JSONObject pageJs = this._DATAJS.getJSONObject(IConstant.PAGE);
							if(!pageJs.isEmpty()) {
								this.__PAGE__ = new PageVo(pageJs.getIntValue(IConstant.PINDEX),pageJs.getIntValue(IConstant.PSIZE));
							}
						}
					}
				}
			} catch (AbsHosException e) {
				e.printStackTrace();
				throw e;
			}
		}else{
			throw new ParamException("消息对象InterfaceMessage为空。");
		}
	}
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public Element getRoot() {
		return root;
	}
	public JSONObject getDataJs() {
		return _DATAJS;
	}
	public Element getData() {
		return __DATA__;
	}
	public void setRoot(Element root) {
		this.root = root;
	}
	
	protected String transactionCode;
	protected String columns;
	
	public InterfaceMessage getMsg() {
		return __INTERFACEMESSAGE__;
	}
	public PageVo getPage() {
		return __PAGE__;
	}

	public void setPage(PageVo page) {
		this.__PAGE__ = page;
	}
	
	public String getOpenId() {
		return this.__AUTHINFO__.getSign();
	}
	public String getOperatorName() {
		return this.__AUTHINFO__.getSign();
	}
	public String getClientId() {
		return this.__AUTHINFO__.getClientId();
	}
	
	public String getHosId() throws AbsHosException {
		if(getData()!=null) {
			return getElementStringValue(getData(), KstHosConstant.HOSID, __AUTHINFO__.getClientVersion());
		}else if(getDataJs()!=null && !getDataJs().isEmpty() && StringUtil.isNotBlank(getDataJs().getString(KstHosConstant.HOSID))) {
			return getDataJs().getString(KstHosConstant.HOSID);
		}else {
			KasiteConfig.print("传入参数医院ID为空！");
			return __AUTHINFO__.getClientVersion();
			//throw new ParamException("传入参数医院ID不能为空");
		}
		
	}

	public String checkOperatorName(String operatorName) {
		if(null == operatorName || "".equals(operatorName)) {
			operatorName = this.__AUTHINFO__.getSign();
		}
		return operatorName;
	}
	public String checkOperatorId(String operatorId) {
		if(null == operatorId || "".equals(operatorId)) {
			operatorId = this.__AUTHINFO__.getSign();
		}
		return operatorId;
	}

}
