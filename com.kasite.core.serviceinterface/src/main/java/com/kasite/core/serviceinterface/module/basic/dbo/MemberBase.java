package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 成员基本信息-数据库实体类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年5月21日 下午4:58:56
 */
@Table(name="B_MEMBER")
public class MemberBase extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private String memberId;
	
	private String memberCode;
	/**
	 * 姓名
	 */
	private String memberName;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
	private String idCardNo;
	/**
	 * 性别
	 */
	private Integer sex;
	
	/**
	 * 出生日期
	 */
	private String birthDate;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 是否小孩
	 */
	private Integer isChildren;
	/**
	 * 名族
	 */
	private String nation;
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

	
	/**
	 * @return the memberCode
	 */
	public String getMemberCode() {
		return memberCode;
	}

	/**
	 * @param memberCode the memberCode to set
	 */
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	public Integer getIsChildren() {
		return isChildren;
	}

	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

}
