package com.kasite.core.common.dao.vo;

import javax.servlet.http.HttpServletRequest;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.util.StringUtil;




/**
 * 分页参数对象，涉及到查询入参对象必须继承该对象
 * @created 2016-05-30
 * @author lcz
 *
 */
public class ReqPage extends AbsReqParam{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6006438210784111459L;
	/**
	 * 页码 从1开始
	 */
	private Integer PageIndex;
	/**
	 * 每页条数
	 */
	private Integer PageSize;
	/**
	 * 排序字段（多个逗号分隔，需和Orders一一对应）
	 */
	private String Sorts;
	/**
	 * 排序类型（1升序 -1降序，多个逗号分隔，需和Sorts一一对应）
	 */
	private String Orders;
	
	public String getSorts() {
		return Sorts;
	}
	public String getOrders() {
		return Orders;
	}
	public void setSorts(String sorts) {
		Sorts = sorts;
	}
	public void setOrders(String orders) {
		Orders = orders;
	}
	public Integer getPageIndex() {
		return PageIndex;
	}
	public Integer getPageSize() {
		return PageSize;
	}
	public void setPageIndex(Integer pageIndex) {
		this.PageIndex = pageIndex;
	}
	public void setPageSize(Integer pageSize) {
		this.PageSize = pageSize;
	}
	@Override
	public void paramValid(ReqAuthInfo auth) throws ParamException {
		
		super.paramValid(auth);
		
		if(StringUtil.isNotBlank(this.PageIndex) && this.PageIndex<0){
			throw new ParamException("参数错误，页码不能小于0.");
		}
		if(StringUtil.isNotBlank(this.PageSize) && this.PageSize<0){
			throw new ParamException("参数错误，每页条数不能小于0.");
		}
		if(StringUtil.isNotBlank(this.Sorts) || StringUtil.isNotBlank(this.Orders)){
			if(StringUtil.isBlank(this.Sorts) || StringUtil.isBlank(this.Orders) || this.Sorts.split(",").length!=this.Orders.split(",").length){
				throw new ParamException("参数错误，排序字段和排序类型必须一一对应.");
			}
		}
	}
    @Override
    public void paramInit(HttpServletRequest request) throws ParamException {
        
    }
	
	
	
}
