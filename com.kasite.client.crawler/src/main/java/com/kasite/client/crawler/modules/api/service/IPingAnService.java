package com.kasite.client.crawler.modules.api.service;

import java.util.Date;

import com.kasite.client.crawler.modules.api.vo.PingAnReqAndRespVo;
import com.kasite.client.crawler.modules.api.vo.PingAnReqVo;
import com.kasite.client.crawler.modules.api.vo.PingAnRespVo;
import com.kasite.client.crawler.modules.api.vo.PingAnS270ReqVo;

public interface IPingAnService {

	/**
	 * 执行 doC100 的后续操作 封装调用 医院查询就人信息
	 * 然后查询相应患者的 门诊信息和住院信息保存到 本地的es数据库中
	 * 
	 * 新增发送 平安接口的方法
	 * 
	 * @param time
	 */
	public void doC100(Date time);
	
	/**
	 * 处理C210后续业务
	 * 
	 */
	public PingAnRespVo doC210(PingAnReqVo vo);
	
	/**
	 * C220后续业务处理
	 * 
	 */
	public void doC220(Date time);
	
	
	public void doS230(String medicalNum);
	
	public void doS250(String medicalNum);
	
	public void doS340(String medicalNum);
	
	public PingAnReqAndRespVo doS291(String inputXml);
	
	public PingAnReqAndRespVo doS299(String inputXml);
	
	public PingAnReqAndRespVo doS260(String inputXml);
	
	public PingAnReqAndRespVo doS270(String inputXml);
}
