/**
 * 
 */
package com.kasite.client.survey.bean.dbo;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

/**
 * @author mhd
 * @version 1.0
 * 2017-7-7 下午3:51:55
 */
@Table(name="SV_SAMPLETRACK")
public class SampleTrack extends BaseDbo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Integer trackId;
	private Integer sampleId;
	private String mobile;
	private String remark;
	private Integer visited;
	private Integer callres;
	private Integer trackFlag;
	private Integer careUser;
	private String recordFile;
	private Integer recordInterval;
	public Integer getTrackId() {
		return trackId;
	}
	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}
	public Integer getSampleId() {
		return sampleId;
	}
	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getVisited() {
		return visited;
	}
	public void setVisited(Integer visited) {
		this.visited = visited;
	}
	public Integer getCallres() {
		return callres;
	}
	public void setCallres(Integer callres) {
		this.callres = callres;
	}
	public Integer getTrackFlag() {
		return trackFlag;
	}
	public void setTrackFlag(Integer trackFlag) {
		this.trackFlag = trackFlag;
	}
	public Integer getCareUser() {
		return careUser;
	}
	public void setCareUser(Integer careUser) {
		this.careUser = careUser;
	}
	public String getRecordFile() {
		return recordFile;
	}
	public void setRecordFile(String recordFile) {
		this.recordFile = recordFile;
	}
	public Integer getRecordInterval() {
		return recordInterval;
	}
	public void setRecordInterval(Integer recordInterval) {
		this.recordInterval = recordInterval;
	}

}
