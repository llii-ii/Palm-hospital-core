package com.kasite.core.serviceinterface.module.basic.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 更新文章信息的请求入参
 * @author zhaoy
 *
 */
public class ReqUpdateArticle extends AbsReq {

	public ReqUpdateArticle(InterfaceMessage msg, String oprType) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.hosId = super.getHosId();
			this.id = getDataJs().getString("ArticleId");
			this.title = getDataJs().getString("Title");
			this.status = getDataJs().getInteger("Status");
			this.type = getDataJs().getInteger("Type");
			this.bigImgUrl = getDataJs().getString("BigImgUrl");
			this.imgUrl = getDataJs().getString("ImgUrl");
			this.typeClass = getDataJs().getInteger("TypeClass");
			this.linkUrl = getDataJs().getString("LinkUrl");
			this.isHead = getDataJs().getInteger("IsHead");
			this.finalDeal = getDataJs().getInteger("FinalDeal");
			this.contents = getDataJs().getString("Contents");
			this.isSueDate = getDataJs().getString("IsSueDate");
			this.operatorId = getDataJs().getString("OperatorId");
			this.operatorName = getDataJs().getString("OperatorName");
			
			if("update".equals(oprType)) {
				if(StringUtil.isBlank(this.id)) {
					throw new RRException(RetCode.Common.ERROR_PARAM,"文章ID参数不能为空!");
				}
			}
		}
	}
	
	/**文章主键*/
	private String id;    
	
	/**标题*/
	private String title; 
	
	/**状态（是否发布  0未发1已发）*/
	private Integer status;  
	
	/**文章类型0 新闻动态；1 通知公告；2 健康宣教；3 就诊指南；4 住院需知*/
	private Integer type; 
	
	/**大图片路径*/
	private String  bigImgUrl;  
	
	/**小图路径*/
	private String imgUrl; 
	
	/**文章栏目*/
	private Integer typeClass; 
	
	/**链接路径*/
	private String linkUrl; 
	
	/**是否置顶*/
	private Integer isHead;        

	private Integer finalDeal;
	
	/**文章内容*/
	private String contents;  
	
	/**发布时间*/
	private String isSueDate;

	private String hosId;
	
	private String operatorId;
	
	private String operatorName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBigImgUrl() {
		return bigImgUrl;
	}

	public void setBigImgUrl(String bigImgUrl) {
		this.bigImgUrl = bigImgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Integer typeClass) {
		this.typeClass = typeClass;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Integer getIsHead() {
		return isHead;
	}

	public void setIsHead(Integer isHead) {
		this.isHead = isHead;
	}

	public Integer getFinalDeal() {
		return finalDeal;
	}

	public void setFinalDeal(Integer finalDeal) {
		this.finalDeal = finalDeal;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getIsSueDate() {
		return isSueDate;
	}

	public void setIsSueDate(String isSueDate) {
		this.isSueDate = isSueDate;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
