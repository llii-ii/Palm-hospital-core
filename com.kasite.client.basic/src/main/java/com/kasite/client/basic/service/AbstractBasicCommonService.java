package com.kasite.client.basic.service;


import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.coreframework.util.DateOper;
import com.coreframework.util.StringUtil;
import com.kasite.client.basic.dao.IArticleMapper;
import com.kasite.client.basic.dao.IDeptMapper;
import com.kasite.client.basic.dao.IDoctorMapper;
import com.kasite.client.basic.dao.IHospitalMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.basic.BasicCommonService;
import com.kasite.core.serviceinterface.module.basic.dbo.Article;
import com.kasite.core.serviceinterface.module.basic.dbo.Hospital;
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

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
/**
 * 基础信息服务抽象类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:07:05
 */
public abstract class AbstractBasicCommonService implements BasicCommonService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_BASIC);

	@Autowired 
	IHospitalMapper hospitalMapper;
	
	@Autowired 
	IDeptMapper deptMapper;
	
	@Autowired 
	IDoctorMapper doctorMapper;
	
	@Autowired 
	IArticleMapper articleMapper;

	/**
	 * 查询本地医院信息
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryHospitalLocal(InterfaceMessage msg) throws Exception{
		ReqQueryHospitalLocal reqVo = new ReqQueryHospitalLocal(msg);
		CommonReq<ReqQueryHospitalLocal> reqParam = new CommonReq<ReqQueryHospitalLocal>(reqVo);
		CommonResp<RespQueryHospitalLocal> commResp = this.queryHospitalLocal(reqParam);
		return commResp.toResult();
	}
	@Override
	public CommonResp<RespQueryHospitalLocal> queryHospitalLocal(CommonReq<ReqQueryHospitalLocal> reqComm){
		ReqQueryHospitalLocal req = reqComm.getParam();
		Example example = new Example(Hospital.class);
		example.createCriteria().andEqualTo("hosId", req.getHosId())
		.andEqualTo("isShow", 1);
		Hospital hospital = hospitalMapper.selectOneByExample(example);
		if (hospital == null) {
			return new CommonResp<RespQueryHospitalLocal>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SQLEXECERROR,"查询医院信息异常，没有找到对应的医院信息");
		}
		RespQueryHospitalLocal resp = new RespQueryHospitalLocal();
		//将Hospital对象属性值复制到RespQueryHospitalLocal对象中的相同名称类型的属性中
		BeanCopyUtils.copyProperties(hospital, resp, null);
		List<RespQueryHospitalLocal> respList = new Vector<RespQueryHospitalLocal>();
		respList.add(resp);
		return new CommonResp<RespQueryHospitalLocal>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	@Override
	public String QueryHospitalListLocal(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryHospitalListLocal> reqParam = new CommonReq<ReqQueryHospitalListLocal>(new ReqQueryHospitalListLocal(msg));
		CommonResp<RespQueryHospitalLocal> commResp = this.queryHospitalListLocal(reqParam);
		return commResp.toResult();
	}

	@Override
	public CommonResp<RespQueryHospitalLocal> queryHospitalListLocal(CommonReq<ReqQueryHospitalListLocal> reqComm) throws Exception {
		ReqQueryHospitalListLocal req = reqComm.getParam();
		Example example = new Example(Hospital.class);
		example.setOrderByClause(" CREATETIME ASC ");
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isShow", 1);
		if(StringUtil.isNotBlank(req.getHosId())) {
			criteria.andEqualTo("hosId", req.getHosId());
		}
		List<Hospital> hosList = hospitalMapper.selectByExample(example);
		List<RespQueryHospitalLocal> respList = new Vector<RespQueryHospitalLocal>();
		for (Hospital hos : hosList) {
			RespQueryHospitalLocal resp = new RespQueryHospitalLocal();
			//将Hospital对象属性值复制到RespQueryHospitalLocal对象中的相同名称类型的属性中
			BeanCopyUtils.copyProperties(hos, resp, null);
			respList.add(resp);
		}
		return new CommonResp<RespQueryHospitalLocal>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	/**
	 * 查询本地基础科室接口 (非 Javadoc)
	 * <p>
	 * Title: QueryBaseDeptLocal
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param msg
	 * @return
	 * @see com.kst.hos.service.basic.BasicCommonService#QueryBaseDeptLocal(com.yihu.wsgw.api.InterfaceMessage)
	 */
	@Override
	public String QueryBaseDeptLocal(InterfaceMessage msg) throws Exception{
			ReqQueryBaseDeptLocal reqQueryBaseDept = new ReqQueryBaseDeptLocal(msg);
			CommonReq<ReqQueryBaseDeptLocal> reqParam = new CommonReq<ReqQueryBaseDeptLocal>(reqQueryBaseDept);
			CommonResp<RespQueryBaseDeptLocal>  respComm = this.queryBaseDeptLocal(reqParam);
			return respComm.toResult();
	}

	/**
	 * 查询本地基础医生
	 */
	@Override
	public String QueryBaseDoctorLocal(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryBaseDoctorLocal> reqParam = new CommonReq<ReqQueryBaseDoctorLocal>(new ReqQueryBaseDoctorLocal(msg));
			CommonResp<RespQueryBaseDoctorLocal>  commResp = this.queryBaseDoctorLocal(reqParam);
			return commResp.toResult();
	}


	/**
	 * 文章列表
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryArticleList(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryArticleList> reqParam = new CommonReq<ReqQueryArticleList>(new ReqQueryArticleList(msg));
			CommonResp<RespQueryArticleList> commResp = this.queryArticleList(reqParam);
			return commResp.toResult();
	}


	/**
	 * 文章详情
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryArticle(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryArticle> reqParam = new CommonReq<ReqQueryArticle>(new ReqQueryArticle(msg));
			CommonResp<RespQueryArticle> commResp = this.queryArticle(reqParam);
			return commResp.toResult();
	}

	@Override
	public CommonResp<RespQueryArticle> queryArticle(CommonReq<ReqQueryArticle> reqParam) throws Exception{
		ReqQueryArticle req = reqParam.getParam();
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		if (StringUtil.isNotBlank(req.getId())) {
			criteria.andEqualTo("id", req.getId());
		}
		if (StringUtil.isNotBlank(req.getHosID())) {
			criteria.andEqualTo("hosId", req.getHosID());
		}
		if (StringUtil.isNotBlank(req.getStatus())) {
			criteria.andEqualTo("status", req.getStatus());
		}
		if (StringUtil.isNotBlank(req.getTypeClass())) {
			criteria.andIn("typeClass",Arrays.asList(req.getTypeClass().split(",")));
		}
		Article article = articleMapper.selectOneByExample(example);
		RespQueryArticle resp = new RespQueryArticle();
		resp.setArticleId(article.getId().toString());
		resp.setBigImgUrl(article.getBigImgUrl());
		resp.setContent(article.getContents());
		if(article.getIsSueDate()!=null) {
			try {
				resp.setIsSueDate(DateOper.formatDate(article.getIsSueDate(), "yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				e.printStackTrace();
				LogUtil.error(log, e,req.getAuthInfo());
			}
		}
		resp.setTitle(article.getTitle());
		return new CommonResp<>(reqParam,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	
}
