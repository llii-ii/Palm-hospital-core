/**
 * 
 */
package com.kasite.core.serviceinterface.module.survey.req;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-6 上午11:01:00
 */
public class ReqCommitAnswer extends AbsReq{
	
	private int subjectId;
	private List<ReqAnswers> answers;
	private String operatorId;
	private String ip;
	private String operatorName;
	private String phoneOrPc;
	private String userName;
	private int sex;
	private int age;
	private String mobile;
	
	
	public String getPhoneOrPc() {
		return phoneOrPc;
	}
	public void setPhoneOrPc(String phoneOrPc) {
		this.phoneOrPc = phoneOrPc;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getMoblie() {
		return mobile;
	}
	public void setMobile(String moblie) {
		this.mobile = moblie;
	}
	public ReqCommitAnswer(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element("Data");
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		List<?> answers = ser.element("Answers").elements("e");
		
		this.subjectId=XMLUtil.getInt(ser, "SubjectId", true);
		this.answers = getAnswersByElements(answers);
		this.operatorId = XMLUtil.getString(ser, "OperatorId", false,super.getOpenId());;
		this.operatorName = XMLUtil.getString(ser, "OperatorName", false,super.getOperatorName());;
		this.ip=XMLUtil.getString(ser, "IP", false);
		this.phoneOrPc=XMLUtil.getString(ser, "PhoneOrPc", false);
		this.userName=XMLUtil.getString(ser, "UserName", false);
		this.mobile=XMLUtil.getString(ser, "Mobile", false);
		this.sex=XMLUtil.getInt(ser, "Sex", false);
		this.age=XMLUtil.getInt(ser, "Age", false);
	}
	public List<ReqAnswers> getAnswers() {
		return answers;
	}
	public void setAnswers(List<ReqAnswers> answers) {
		this.answers = answers;
	}
	/**XML 节点list转List*/
	public List<ReqAnswers> getAnswersByElements(List<?> answers)
			throws AbsHosException {
		List<ReqAnswers> list = new ArrayList<ReqAnswers>();
		for (Iterator<?> it = answers.iterator(); it.hasNext();) {      
		    Element elm = (Element) it.next();   
			String answer = XMLUtil.getString(elm, "Answer", false);
			Integer questId = XMLUtil.getInt(elm, "QuestId", false);
			String blank = XMLUtil.getString(elm, "Blank", false);
			ReqAnswers answersBean = new ReqAnswers(getMsg(), answer, questId, blank);
			list.add(answersBean);      
		}      
		return list;
	}
	
}

