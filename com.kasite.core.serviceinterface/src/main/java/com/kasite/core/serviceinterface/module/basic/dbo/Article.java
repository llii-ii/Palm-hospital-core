package com.kasite.core.serviceinterface.module.basic.dbo;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.kasite.core.common.bean.dbo.BaseDbo;
import com.kasite.core.common.exception.ParamException;
import com.yihu.hos.util.RelativeXmlNode;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 文章实体类
 * 
 * @author mhd
 * @version 1.0 2017-6-21 下午3:12:34
 */
@Table(name="B_ARTICLE")
public class Article extends BaseDbo{
	/**
	 * 文章主键
	 */
	@Id
	@KeySql(useGeneratedKeys = true)
	@RelativeXmlNode(BeanFiled = "id", XmlFiled = "ArticleId")
	private String id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 状态（是否发布 0未发1已发）
	 */
	private Integer status;
	/**
	 * 文章类型
	 */
	private Integer type;
	/**
	 * 大图片路径
	 */
	private String bigImgUrl;
	/**
	 * 小图路径
	 */
	private String imgUrl;
	/**
	 * 文章栏目
	 */
	private Integer typeClass;
	/**
	 * 操作时间
	 */
	private Timestamp isSueDate;
	/**
	 * 链接路径
	 */
	private String linkUrl;
	/**
	 * 是否置顶
	 */
	private Integer isHead;
	
	private Integer finalDeal;
	/**
	 * 文章内容
	 */
	@RelativeXmlNode(BeanFiled = "contents", XmlFiled = "Content")
	private String contents;
	/**
	 * 医院id
	 */
	private String hosId;

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

	public Timestamp getIsSueDate() {
		return isSueDate;
	}

	public void setIsSueDate(Timestamp isSueDate) {
		this.isSueDate = isSueDate;
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

	public void setFinalDeal(Integer finaldeal) {
		this.finalDeal = finaldeal;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosid) {
		this.hosId = hosid;
	}

	public String toXml() throws ParamException {
		StringBuffer sb = new StringBuffer();
		String temp = "";
		if (StringUtils.isNotBlank(this.contents)) {
			// temp = EscapeCodeUtil.escape(this.contents==null ? "" :
			// this.contents);
			temp = this.contents == null ? "" : this.contents;
		}

		sb.append("<Data>");
		sb.append("<ArticleId>").append(StringUtils.isBlank(this.id) ? "" : this.id).append("</ArticleId>");
		sb.append("<Title>").append(StringUtils.isBlank(this.title) ? "" : this.title).append("</Title>");
		sb.append("<Type>").append(this.type == null ? 0 : this.type).append("</Type>");
		sb.append("<ImgUrl>").append(StringUtils.isBlank(this.imgUrl) ? "" : this.imgUrl).append("</ImgUrl>");
		sb.append("<LinkUrl>").append(StringUtils.isBlank(this.linkUrl) ? "" : this.linkUrl).append("</LinkUrl>");
		sb.append("<Contents>").append(temp).append("</Contents>");
		sb.append("<Status>").append(this.status == null ? 0 : this.status).append("</Status>");
		sb.append("<CreateDate>").append(this.getCreateTime()).append("</CreateDate>");
		sb.append("<BigImgUrl>").append(StringUtils.isBlank(this.bigImgUrl) ? "" : this.bigImgUrl)
				.append("</BigImgUrl>");
		sb.append("<Lastmodify>").append(this.getUpdateTime()).append("</Lastmodify>");
		sb.append("<IsHead>").append(this.isHead == null ? 0 : this.isHead).append("</IsHead>");
		sb.append("<TypeClass>").append(this.typeClass == null ? 0 : this.typeClass).append("</TypeClass>");
		sb.append("<IssueDate>").append(this.isSueDate == null ? "" : this.isSueDate).append("</IssueDate>");
		sb.append("<FinalDeal>").append(this.finalDeal == null ? 0 : this.finalDeal).append("</FinalDeal>");
		sb.append("<HosId>").append(this.hosId == null ? "" : this.hosId).append("</HosId>");
		sb.append("</Data>");
		/*
		 * XMLUtil xu= new XMLUtil(); Document doc =
		 * xu.parseXml("<Data></Data>"); Element root = doc.getRootElement();
		 * xu.addElement(root, "ArticleId", StringUtils.isBlank(this.id)? "" :
		 * this.id ); xu.addElement(root, "Title",
		 * StringUtils.isBlank(this.title)? "" :this.title );
		 * xu.addElement(root, "Type", this.type == null? 0 : this.type );
		 * xu.addElement(root, "ImgUrl", StringUtils.isBlank(this.imgUrl)? "" :
		 * this.imgUrl ); xu.addElement(root, "LinkUrl",
		 * StringUtils.isBlank(this.linkUrl)? "" : this.linkUrl );
		 * xu.addElement(root, "Contents", StringUtils.isBlank(this.contents)?
		 * "" : this.contents ); xu.addElement(root, "Status", this.Status ==
		 * null? 0 : this.Status ); xu.addElement(root, "CreateDate",
		 * this.createDate ); xu.addElement(root, "BigImgUrl",
		 * StringUtils.isBlank(this.bigImgUrl)? "" : this.bigImgUrl );
		 * xu.addElement(root, "Lastmodify",this.lastmodify );
		 * xu.addElement(root, "TypeClass", typeClass == null ? 0 :
		 * this.typeClass ); xu.addElement(root, "IssueDate", this.issueDate ==
		 * null ? "" : this.issueDate );
		 */

		return sb.toString();
	}

	public String toListXml() {
		StringBuffer sb = new StringBuffer();

		sb.append("<Data>");
		sb.append("<ArticleId>").append(StringUtils.isBlank(this.id) ? "" : this.id).append("</ArticleId>");
		sb.append("<Title>").append(StringUtils.isBlank(this.title) ? "" : this.title).append("</Title>");
		sb.append("<Type>").append(this.type == null ? 0 : this.type).append("</Type>");
		sb.append("<ImgUrl>").append(StringUtils.isBlank(this.imgUrl) ? "" : this.imgUrl).append("</ImgUrl>");
		sb.append("<LinkUrl>").append(StringUtils.isBlank(this.linkUrl) ? "" : this.linkUrl).append("</LinkUrl>");
		sb.append("<Status>").append(this.status == null ? 0 : this.status).append("</Status>");
		sb.append("<CreateDate>").append(this.getCreateTime()).append("</CreateDate>");
		sb.append("<BigImgUrl>").append(StringUtils.isBlank(this.bigImgUrl) ? "" : this.bigImgUrl)
				.append("</BigImgUrl>");
		sb.append("<Lastmodify>").append(this.getUpdateTime()).append("</Lastmodify>");
		sb.append("<IsHead>").append(this.isHead == null ? 0 : this.isHead).append("</IsHead>");
		sb.append("<TypeClass>").append(this.typeClass == null ? 0 : this.typeClass).append("</TypeClass>");
		sb.append("<IssueDate>").append(this.isSueDate == null ? "" : this.isSueDate).append("</IssueDate>");
		sb.append("<FinalDeal>").append(this.finalDeal == null ? 0 : this.finalDeal).append("</FinalDeal>");
		sb.append("<HosId>").append(this.hosId == null ? "" : this.hosId).append("</HosId>");
		sb.append("</Data>");
		return sb.toString();
	}

	public String updateXml() {
		// 等价于
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(this.getTitle())) {
			sb.append("a.TITLE=").append("'").append(this.getTitle()).append("'").append(",");
		}
		sb.append("a.TYPE=").append(this.getType() == null ? 0 : this.getType()).append(",");
		sb.append("a.TYPECLASS=").append(this.getTypeClass()).append(",");
		sb.append("a.status=").append(this.getStatus()).append(",");
		if (StringUtils.isNotBlank(this.getImgUrl())) {
			String tem = this.getImgUrl();
			sb.append("a.IMGURL=").append("'").append(tem).append("'").append(",");
		}
		if (StringUtils.isNotBlank(this.getBigImgUrl())) {
			String tem = this.getBigImgUrl();
			sb.append("a.BIGIMGURL=").append("'").append(tem).append("'").append(",");
		}
		if (StringUtils.isNotBlank(this.getLinkUrl())) {
			String tem = this.getLinkUrl();
			sb.append("a.LINKURL=").append("'").append(tem).append("'").append(",");
		} else {
			sb.append("a.LINKURL=").append("''").append(",");
		}

		if (this.getUpdateTime() != null) {
			String date = formate.format(this.getUpdateTime());
			sb.append("a.LASTMODIFY=").append("date_format('" + date + "','%Y-%m-%d %H:%i:%s')").append(",");
		}
		if (this.getIsSueDate() != null) {
			String date = formate.format(this.getIsSueDate());
			sb.append("a.issuedate=").append("date_format('" + date + "','%Y-%m-%d %H:%i:%s')").append(",");
		} else if (this.getIsSueDate() == null) {
			sb.append("a.issuedate=").append("sysdate()").append(",");
		} else {
			sb.append("a.issuedate=").append("sysdate()").append(",");
		}
		if (StringUtils.isNotBlank(this.getContents())) {
			String tem = this.getContents();
			sb.append("a.CONTENTS=").append("'").append(tem).append("'");
		} else {
			sb.append("a.CONTENTS=").append("''");
		}
		sb.append(" where a.ID=").append("'").append(this.getId()).append("'");
		return sb.toString();
	}

	public String updateXmlForIssue() {
		// 等价于
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		if (this.getUpdateTime() != null) {
			String date = formate.format(this.getUpdateTime());
			sb.append("a.LASTMODIFY=").append("date_format('" + date + "','%Y-%m-%d %H:%i:%s')").append(",");
		}
		if (this.getIsSueDate() != null) {
			String date = formate.format(this.getIsSueDate());
			sb.append("a.issuedate=").append("date_format('" + date + "','%Y-%m-%d %H:%i:%s')").append(",");
		} else if (this.getIsSueDate() == null) {
			sb.append("a.issuedate=").append("sysdate()").append(",");
		} else {
			sb.append("a.issuedate=").append("sysdate()").append(",");
		}
		sb.append("a.TYPE=").append(this.getType() == null ? 0 : this.getType()).append(",");
		sb.append("a.TYPECLASS=").append(this.getTypeClass()).append(",");
		sb.append("a.status=").append(this.getStatus());
		sb.append(" where a.ID=").append("'").append(this.getId()).append("'");
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Article)) {
			return false;
		}
		Article oth = (Article) obj;

		return (oth.getCreateTime() == this.getCreateTime() && oth.getUpdateTime() == this.getUpdateTime()
				&& oth.getIsHead() == this.getIsHead());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * this.getIsHead();
		return result;
	}
}
