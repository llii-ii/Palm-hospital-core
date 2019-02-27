package com.kasite.core.common.req;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.github.pagehelper.Page;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;

public class PageVo {

	private Integer PSize = 0;
	private Integer PIndex = 0;
	private Integer PCount = 0;
	
	public Integer getPCount() {
		return PCount;
	}
	public void setPCount(Integer count) {
		PCount = count;
	}
	public Integer getPSize() {
		return PSize;
	}
	public Integer getPIndex() {
		return PIndex;
	}
	public void setPSize(Integer size) {
		PSize = size;
	}
	public void setPIndex(Integer index) {
		PIndex = index;
	}
	public PageVo(){
		
	}
	public PageVo(int pIndex,int pSize){
		this.PIndex = pIndex;
		this.PSize = pSize;
	}
	public PageVo(Element page) throws AbsHosException{
		init(page);
	}
	private void init(Element page) throws AbsHosException{
		if(page!=null){
			this.PIndex = XMLUtil.getInt(page, "PIndex", true);
			this.PSize = XMLUtil.getInt(page, "PSize", true);
			this.PCount = XMLUtil.getInt(page, "PCount", false);
		}
	}
	
	public PageVo(String xml) throws AbsHosException{
		Document doc = null;
		try {
			doc = XMLUtil.parseXml(xml);
		} catch (ParamException e) {
			e.printStackTrace();
			throw e;
		}
		Element root = doc.getRootElement();
		Element ser = root.element("Data");
		if(ser == null){
			ser = root.element("Service");
		}
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		Element page = ser.element("Page");
		init(page);
	}
	
	//在处理下载的时候，不需要传入Page节点
	public PageVo(AbsReq req,boolean nullPage) throws AbsHosException{
		Element root = req.getRoot();
		Element ser = root.element("Data");
		if(ser == null){
			ser = root.element("Service");
		}
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		Element page = ser.element("Page");
		if(page==null){
			this.PIndex =0;
			this.PSize=0;
		}else{
			this.PIndex = XMLUtil.getInt(page, "PIndex", true);
			this.PSize = XMLUtil.getInt(page, "PSize", true);
		}
	}
	//在处理下载的时候，不需要传入Page节点
	public PageVo(String xml,boolean nullPage) throws AbsHosException{
		Document doc = null;
		try {
			doc = XMLUtil.parseXml(xml);
		} catch (ParamException e) {
			e.printStackTrace();
			throw e;
		}
		Element root = doc.getRootElement();
		Element ser = root.element("Data");
		if(ser == null){
			ser = root.element("Service");
		}
		if(ser==null){
			throw new ParamException("传入参数中[Data]节点不能为空。");
		}
		Element page = ser.element("Page");
		if(page==null){
			this.PIndex =0;
			this.PSize=0;
			//throw new ParamException("传入参数中[Page]节点不能为空。");
		}else{
			this.PIndex = XMLUtil.getInt(page, "PIndex", true);
			this.PSize = XMLUtil.getInt(page, "PSize", true);
		}
	}

	public void initPCount(List<?> list) {
		if (list instanceof Page) {
			Page<?> page = (Page<?>) list;
			this.PCount = Integer.parseInt(Long.toString(page.getTotal()));
		}
	}
}
