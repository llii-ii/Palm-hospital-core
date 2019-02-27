package com.yihu.hos.util;

public class ConfigBean {

	public ConfigBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * <Response>
	 * 	<Count>10</Count>
	 *  <Size>11</Size>
	 * 	<Data>
	 *    <Name>A</Name>
	 * 	</Data>	 
	 * 	<Data>
	 *    <Name>B</Name>
	 * 	</Data>
	 * </Response>
	 * 
	 * String exData = // Response
	 * String parseExData {"Count<-->count;Size<-->size"}
 		String ex = cfg.getEx();// "Response-->ResultCode";
		String codeValue = cfg.getCodeValue();
		String exList = cfg.getExList();// "Response-->DeptList-->Dept";
		String parseDept = cfg.getParseData();// "Dept[YYYY-MM-DD]-->Ksdm<-->deptCode;Ksmc<-->deptName;Dept->addr<-->deptAddr;[1001]<-->deptBrief";
		int isDataList = cfg.getIsDataList(); // 0是数据对象 1 是列表。
 */
	private String ex = "";//"Response-->ResultCode";
	private String codeValue = "";//"111";
	private String respMessage = "";//Response-->ResultMessage<-->RespMessage;
	private String exList = "";//"Response-->DeptList-->Dept";//数据节点
	private String parseData = "";//"Ksdm-->deptCode;Ksmc-->deptName;Dept->addr-->deptAddr;";//数据节点映射
	
	private String exData;
	private String parseExData;
	private Integer isDataList;
	
	
	public String getRespMessage() {
		return respMessage;
	}

	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}

	public Integer getIsDataList() {
		return isDataList;
	}

	public void setIsDataList(Integer isDataList) {
		this.isDataList = isDataList;
	}

	
	public ConfigBean(String ex, String codeValue, String exList,
			String parseData,Integer isDataList) {
		this.ex = ex;
		this.codeValue = codeValue;
		this.exList = exList;
		this.isDataList = isDataList;
		this.parseData = parseData;
	}
	
	
	public String getEx() {
		return ex;
	}
	public void setEx(String ex) {
		this.ex = ex;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getExList() {
		return exList;
	}
	public void setExList(String exList) {
		this.exList = exList;
	}
	public String getParseData() {
		return parseData;
	}
	public void setParseData(String parseData) {
		this.parseData = parseData;
	}

	public String getExData() {
		return exData;
	}

	public void setExData(String exData) {
		this.exData = exData;
	}

	public String getParseExData() {
		return parseExData;
	}

	public void setParseExData(String parseExData) {
		this.parseExData = parseExData;
	}
	
}
