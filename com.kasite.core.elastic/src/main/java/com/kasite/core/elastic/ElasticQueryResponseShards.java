package com.kasite.core.elastic;

public class ElasticQueryResponseShards extends AbsElasticResponse{
	private Integer total;
	private Integer successful;	
	private Integer failed;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getSuccessful() {
		return successful;
	}
	public void setSuccessful(Integer successful) {
		this.successful = successful;
	}
	public Integer getFailed() {
		return failed;
	}
	public void setFailed(Integer failed) {
		this.failed = failed;
	}
	
}
