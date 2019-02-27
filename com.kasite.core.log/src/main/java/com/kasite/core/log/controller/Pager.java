package com.kasite.core.log.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页参数
 * @author chenzhibin <br> 2017-5-9 下午18:12:30
 */
public class Pager {
	public HttpServletRequest request;
	public int page = 1;//当前页
	public int start = 0;//从第一条记录开始，也就是0
	public int pageSize = 10;//每页10条记录
	public static int exportpageSize=1000;


	public Pager(int page,int pageSize) {
		super();
		Object oRows = pageSize;
		if (oRows == null || "".equals(oRows.toString())) {
			this.pageSize = 10;
		} else {
			this.pageSize = Integer.valueOf(oRows.toString());
		}
		Object oPage = page;
		if (oPage == null || "".equals(oPage)) {
			this.start = 0;
		} else {
			if (Integer.valueOf(oPage.toString()) <= 1) {
				this.start = 0;
			} else {
				this.start = (Integer.valueOf(oPage.toString()) - 1) * pageSize;
			}
		}
	}

	/**
	 * @param request
	 */
	public Pager(HttpServletRequest request) {
		super();
		this.request = request;
		Object oRows = request.getParameter("rows");
		if (oRows == null || "".equals(oRows.toString())) {
			this.pageSize = 10;
		} else {
			this.pageSize = Integer.valueOf(oRows.toString());
		}
		Object oPage = request.getParameter("page");
		if (oPage == null || "".equals(oPage)) {
			this.start = 0;
		} else {
			if (Integer.valueOf(oPage.toString()) <= 1) {
				this.start = 0;
			} else {
				this.start = (Integer.valueOf(oPage.toString()) - 1) * pageSize;
			}
		}
	}

	public Pager(HttpServletRequest request,boolean isExportPage) {
		super();
		this.request = request;
		Object oRows = request.getParameter("rows");
		if (isExportPage) {
			this.pageSize = 1000;
		} else {
			if (oRows == null || "".equals(oRows.toString())) {
				this.pageSize = 10;
			} else {
				this.pageSize = Integer.valueOf(oRows.toString());
			}
		}
		Object oPage = request.getParameter("page");
		if (oPage == null || "".equals(oPage)) {
			this.start = 0;
		} else {
			if (Integer.valueOf(oPage.toString()) <= 1) {
				this.start = 0;
			} else {
				this.start = (Integer.valueOf(oPage.toString()) - 1) * pageSize;
			}
		}
	}

}
