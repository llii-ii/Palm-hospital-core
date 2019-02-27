package com.kasite.core.serviceinterface.module.basic;


import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryArticle;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryArticleList;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDeptLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalListLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryArticle;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryArticleList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDeptLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryHospitalLocal;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 基础信息接口
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:10:13
 */
public interface BasicCommonService{
	/**
	 * 查询本地医院信息
	 * 
	 * @param msg
	 * @return
	 */
	public String QueryHospitalLocal(InterfaceMessage msg) throws Exception;
	/**
	 * 查询本地医院信息
	 * @Description: 
	 * @param req
	 * @return
	 */
	CommonResp<RespQueryHospitalLocal> queryHospitalLocal(CommonReq<ReqQueryHospitalLocal> req)  throws Exception;

	/**
	 * 查询本地医院列表
	 * 
	 * @param msg
	 * @return
	 */
	public String QueryHospitalListLocal(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询本地库医院列表
	 * @Description: 
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQueryHospitalLocal> queryHospitalListLocal(CommonReq<ReqQueryHospitalListLocal> reqComm) throws Exception;
	
	
	/**
	 * 查询本地基础科室
	 * 
	 * @param msg
	 * @return
	 */
	public String QueryBaseDeptLocal(InterfaceMessage msg) throws Exception;
	/**
	 *  查询本地基础科室
	 * @Description: 
	 * @param reqParam
	 * @return
	 */
	public CommonResp<RespQueryBaseDeptLocal> queryBaseDeptLocal(CommonReq<ReqQueryBaseDeptLocal> reqParam) throws Exception;
	
	/**
	 * 查询本地基础医生
	 * 
	 * @param msg
	 * @return
	 */
	public String QueryBaseDoctorLocal(InterfaceMessage msg) throws Exception;
	/**
	 * 查询本地基础医生
	 * @Description: 
	 * @param reqParam
	 * @return
	 */
	public CommonResp<RespQueryBaseDoctorLocal> queryBaseDoctorLocal(CommonReq<ReqQueryBaseDoctorLocal> reqParam) throws Exception;

	/**
	 * 文章列表
	 * 
	 * @param msg
	 * @return
	 */
	public String QueryArticleList(InterfaceMessage msg) throws Exception;
	/**
	 * 查询文章列表
	 * @Description: 
	 * @param reqParam
	 * @return
	 */
	public CommonResp<RespQueryArticleList> queryArticleList(CommonReq<ReqQueryArticleList> reqParam) throws Exception;

	/**
	 * 文章详情
	 * 
	 * @param msg
	 * @return
	 */
	public String QueryArticle(InterfaceMessage msg) throws Exception;
	/**
	 * 查询文章详情
	 * @Description: 
	 * @param reqParam
	 * @return
	 */
	public CommonResp<RespQueryArticle> queryArticle(CommonReq<ReqQueryArticle> reqParam) throws Exception;
}
