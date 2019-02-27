package com.kasite.client.basic.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.kasite.core.serviceinterface.module.basic.dbo.Patient;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IPatientMapper
 * @author: lcz
 * @date: 2018年7月18日 下午6:39:43
 */
public interface IPatientMapper extends Mapper<Patient>{
	
//	@Update({"<script>",
//		"UPDATE B_PATIENT PATIENT,B_MEMBER MEMBER  SET PATIENT.HOSPITALNO = '' WHERE PATIENT.MEMBERID=MEMBER.MEMBERID AND PATIENT.STATE=1 ",
//			"<if test=\"memberId!=null and memberId!=''\">",
//				" AND PATIENT.MEMBERID=#{memberId}",
//			"</if>",
//			"<if test=\"memberName!=null and memberName!=''\">",
//				" AND MEMBER.MEMBERNAME=#{memberName}",
//			"</if>",
//			"<if test=\"mobile!=null and mobile!=''\">",
//				" AND MEMBER.MOBILE=#{mobile}",
//			"</if>",
//			"<if test=\"idCardNo!=null and idCardNo!=''\">",
//				" AND MEMBER.IDCARDNO=#{idCardNo}",
//			"</if>",
//		"</script>"})
//	int delHospitalNo(@Param("memberId")String memberId,@Param("memberName")String memberName,@Param("idCardNo")String idCardNo,@Param("mobile")String mobile);
//	
//	
//	@Update("INSERT INTO B_PATIENT_OLD(ID, MEMBERID, CARDNO, CARDTYPE, CARDTYPENAME, MCARDNO, BIRTHNUMBER, HOSID, STATE, ISDEFAULT, CREATETIME, UPDATETIME, HISMEMBERID, ACCOUNTID, HOSPITALNO, OPERATORID, OPERATORNAME)SELECT ID, MEMBERID, CARDNO, CARDTYPE, CARDTYPENAME, MCARDNO, BIRTHNUMBER, HOSID, STATE, ISDEFAULT, CREATETIME, UPDATETIME, HISMEMBERID, ACCOUNTID, HOSPITALNO, OPERATORID, OPERATORNAME FROM B_PATIENT WHERE MEMBERID=#{memberId} AND STATE=#{state}")
//	int removePatientToOld(@Param("memberId")String memberId,@Param("state")Integer state);
//	@Update({"<script>",
//		"INSERT INTO B_PATIENT_OLD(ID, MEMBERID, CARDNO, CARDTYPE, CARDTYPENAME, MCARDNO, BIRTHNUMBER, HOSID, STATE, ISDEFAULT, CREATETIME, UPDATETIME, HISMEMBERID, ACCOUNTID, HOSPITALNO, OPERATORID, OPERATORNAME)SELECT ID, MEMBERID, CARDNO, CARDTYPE, CARDTYPENAME, MCARDNO, BIRTHNUMBER, HOSID, STATE, ISDEFAULT, CREATETIME, UPDATETIME, HISMEMBERID, ACCOUNTID, HOSPITALNO, OPERATORID, OPERATORNAME FROM B_PATIENT WHERE MEMBERID=#{memberId} AND STATE=#{state}",
//		"<if test=\"cardType!=null and cardType!=''\">",
//			" AND CARDTYPE=#{cardType}",
//		"</if>",
//		"<if test=\"cardNo!=null and cardNo!=''\">",
//			" AND CARDNO=#{cardNo}",
//		"</if>",
//	"</script>"})
//	int removePatientToOldForMap(Map<String,String> map);
//	
//	@Update("DELETE FROM B_PATIENT WHERE MEMBERID=#{memberId} AND STATE=#{state}")
//	int removeInvalidPatient(@Param("memberId")String memberId,@Param("state")Integer state);
//	@Update({"<script>",
//		"DELETE FROM B_PATIENT WHERE MEMBERID=#{memberId} AND STATE=#{state}",
//		"<if test=\"cardType!=null and cardType!=''\">",
//			" AND CARDTYPE=#{cardType}",
//		"</if>",
//		"<if test=\"cardNo!=null and cardNo!=''\">",
//			" AND CARDNO=#{cardNo}",
//		"</if>",
//	"</script>"})
//	int removeInvalidPatientForMap(Map<String,String> map);
}
