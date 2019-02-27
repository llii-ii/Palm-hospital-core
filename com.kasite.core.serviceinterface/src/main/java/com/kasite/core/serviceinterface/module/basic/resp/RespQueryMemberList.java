package com.kasite.core.serviceinterface.module.basic.resp;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.util.StringUtil;

/**
 * 
 * @className: RespQueryMemberList
 * @author: lcz
 * @date: 2018年7月23日 下午3:11:26
 */
public class RespQueryMemberList extends AbsResp{
	
	private String memberName;
	private String cardNo;
	private String cardType;
	private String cardTypeName;
	private String memberId;
	private String mobile;
	private String idCardNo;
	private Integer sex;
	private String birthDate;
	private String address;
//	private String mcardNo;
//	private String birthNumber;
	private Integer isChildren;
//	private String blance;
	private String opId;
	private String channelId;
	private Integer isDefault;
	private Integer isDefaultMember;
	private String hospitalNo;
	/**
	 *  证件类型  01、居民身份证，02、居民户口簿，03、护照，04、军官证，05、驾驶证，06、港澳居民来往内地通行证，07、台湾居民来往内地通行证，08、士兵证，09、返乡证，10、组织机构代码，11、港澳通行证，12、台湾通行证，13、户口簿，14、学生证，15、国际海员证，16、外国人永久居留证，17、旅行证，18、警官证，19、微信号，20、港澳居民来往内地通行证，21、台胞证，22、电子就医码 23、社会保障号码  99、其他法定有效证件
	 */
	private String certType;
	/**
	 * 证件号码
	 */
	private String certNum;
	/**
	 * 监护人姓名
	 */
	private String guardianName;
	/**
	 * 监护人性别
	 */
	private Integer guardianSex;
	/**
	 * 监护人证件类型
	 */
	private String guardianCertType;
	/**
	 * 监护人证件号码
	 */
	private String guardianCertNum;
	/**His端成员ID**/
	private String hisMemberId;
	
	/**
	 * 卡包
	 */
	private List<RespCardPackage> data_1;
	
	
	/**
	 * @return the isDefaultMember
	 */
	public Integer getIsDefaultMember() {
		return isDefaultMember;
	}
	/**
	 * @param isDefaultMember the isDefaultMember to set
	 */
	public void setIsDefaultMember(Integer isDefaultMember) {
		this.isDefaultMember = isDefaultMember;
	}
	/**
	 * @return the data_1
	 */
	public List<RespCardPackage> getData_1() {
		return data_1;
	}
	/**
	 * @param data_1 the data_1 to set
	 */
	public void setData_1(List<RespCardPackage> data_1) {
		this.data_1 = data_1;
	}
	/**
	 * @return the hisMemberId
	 */
	public String getHisMemberId() {
		return hisMemberId;
	}
	/**
	 * @param hisMemberId the hisMemberId to set
	 */
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	/**
	 * @return the certType
	 */
	public String getCertType() {
		return certType;
	}
	/**
	 * @param certType the certType to set
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}
	/**
	 * @return the certNum
	 */
	public String getCertNum() {
		return certNum;
	}
	/**
	 * @param certNum the certNum to set
	 */
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	/**
	 * @return the guardianName
	 */
	public String getGuardianName() {
		return guardianName;
	}
	/**
	 * @param guardianName the guardianName to set
	 */
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	/**
	 * @return the guardianSex
	 */
	public Integer getGuardianSex() {
		return guardianSex;
	}
	/**
	 * @param guardianSex the guardianSex to set
	 */
	public void setGuardianSex(Integer guardianSex) {
		this.guardianSex = guardianSex;
	}
	/**
	 * @return the guardianCertType
	 */
	public String getGuardianCertType() {
		return guardianCertType;
	}
	/**
	 * @param guardianCertType the guardianCertType to set
	 */
	public void setGuardianCertType(String guardianCertType) {
		this.guardianCertType = guardianCertType;
	}
	/**
	 * @return the guardianCertNum
	 */
	public String getGuardianCertNum() {
		return guardianCertNum;
	}
	/**
	 * @param guardianCertNum the guardianCertNum to set
	 */
	public void setGuardianCertNum(String guardianCertNum) {
		this.guardianCertNum = guardianCertNum;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
//	public String getMcardNo() {
//		return mcardNo;
//	}
//	public void setMcardNo(String mcardNo) {
//		this.mcardNo = mcardNo;
//	}
//	public String getBirthNumber() {
//		return birthNumber;
//	}
//	public void setBirthNumber(String birthNumber) {
//		this.birthNumber = birthNumber;
//	}
	public Integer getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}
//	public String getBlance() {
//		return blance;
//	}
//	public void setBlance(String blance) {
//		this.blance = blance;
//	}
	public String getOpId() {
		return opId;
	}
	public void setOpId(String opId) {
		this.opId = opId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
	 
	/**
	 * 将数据转到Map中
	 * @param map
	 */
	public void toMap(Map<String, String> map) {
		JSONObject json = (JSONObject) JSON.toJSON(this);
		for (Map.Entry<String, Object> entity : json.entrySet()) {
			String key = entity.getKey();
			Object value = entity.getValue();
			if(StringUtil.isNotBlank(value)) {
				map.put(key, value.toString());
			}
		}
//		map.put("hisMemberId", hisMemberId);
	}
	
}
