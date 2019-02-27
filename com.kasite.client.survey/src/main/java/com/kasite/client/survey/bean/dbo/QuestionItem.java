/**
 * 
 */
package com.kasite.client.survey.bean.dbo;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-5 下午4:59:10
 */
@Table(name="SV_QUESTIONITEM")
public class QuestionItem extends BaseDbo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@KeySql(useGeneratedKeys=true)
	private Integer itemId;
	private Integer questId;
	private String itemCont;
	private Integer sortNum;
	private Integer status;
	private Integer originalId;
	private String itemOption;
	private String ifAddBlank;
	private String ifAllowNull;
//	private Integer nextQuestId;
	
//	public Integer getNextQuestId() {
//		return nextQuestId;
//	}
//
//	public void setNextQuestId(Integer nextQuestId) {
//		this.nextQuestId = nextQuestId;
//	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getQuestId() {
		return questId;
	}

	public void setQuestId(Integer questId) {
		this.questId = questId;
	}

	public String getItemCont() {
		return itemCont;
	}

	public void setItemCont(String itemCont) {
		this.itemCont = itemCont;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}

	public String getItemOption() {
		return itemOption;
	}

	public void setItemOption(String itemOption) {
		this.itemOption = itemOption;
	}

	public String getIfAddBlank() {
		return ifAddBlank;
	}

	public void setIfAddBlank(String ifAddBlank) {
		this.ifAddBlank = ifAddBlank;
	}

	public String getIfAllowNull() {
		return ifAllowNull;
	}

	public void setIfAllowNull(String ifAllowNull) {
		this.ifAllowNull = ifAllowNull;
	}

	
	
//	public String toString() {
//		return new ReflectionToStringBuilder(this).toString();
//	}
}
