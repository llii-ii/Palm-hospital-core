package com.kasite.core.common.resp;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;

/**
 * 
 * @className: RespUtil
 * @author: lcz
 * @date: 2018年7月19日 下午6:27:31
 */
public class Resp {
	
	/**
	 * 返回值处理，默认返回XML成功信息，transaction为空串
	 * commResp 对象中的  data属性会转成XML中的Data节点，PIndex、PSize、PCount不为空时，会添加Page XML节点
	 * 
	 * @Description: 
	 * @param commResp	数据结果集
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(CommonResp<?> commResp) throws RRException {
		return create(RespType.XML, KstHosConstant.DEFAULTTRAN, commResp.getCode(), commResp.getMessage(), commResp, null);
	}
	
	/**
	 * 返回值处理，transaction为空串
	 * @Description: 
	 * @param type	返回值类型
	 * @param ret
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(RespType type,CommonResp<?> commResp) throws RRException {
		return create(type, KstHosConstant.DEFAULTTRAN, commResp.getCode().toString(), commResp.getMessage(), commResp, null);
	}
	/**
	 * 返回值处理，默认返回XML成功信息，transaction为空串
	 * commResp 对象中的  data属性会转成XML中的Data节点，PIndex、PSize、PCount不为空时，会添加Page XML节点
	 * @Description: 
	 * @param commResp
	 * @param columns			需要的节点名称
	 * @param excludeColumns	排除的节点
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(CommonResp<?> commResp,String columns,String excludeColumns) throws RRException {
		ParseConfig cfg = new ParseConfig();
		cfg.setColumns(columns);
		cfg.setExcludeColumns(excludeColumns);
		return create(RespType.XML, KstHosConstant.DEFAULTTRAN, commResp.getCode().toString(), commResp.getMessage(), commResp, cfg);
	}
	
	/**
	 * 返回值处理，默认返回XML格式，transaction为空串
	 * @Description: 
	 * @param ret
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(RetCode ret) throws RRException {
		return create(RespType.XML, KstHosConstant.DEFAULTTRAN, ret.getCode().toString(), ret.getMessage(), null, null);
	}
	/**
	 * 返回值处理	默认返回成功信息
	 * @Description: 
	 * @param type	返回值类型
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(RespType type) throws RRException {
		return create(type, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000.getCode().toString(), RetCode.Success.RET_10000.getMessage(), null, null);
	}
	
	
	/**
	 * 返回值处理，默认返回XML格式
	 * @Description: 
	 * @param transactionCode
	 * @param ret
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(String transactionCode,RetCode ret) throws RRException {
		return create(RespType.XML, KstHosConstant.DEFAULTTRAN, ret.getCode().toString(), ret.getMessage(), null, null);
	}
	/**
	 * 返回值处理，默认返回XML格式
	 * @Description: 
	 * @param transactionCode
	 * @param ret
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(String transactionCode,RetCode ret,String message) throws RRException {
		return create(RespType.XML, KstHosConstant.DEFAULTTRAN, ret.getCode().toString(), ret.getMessage(), null, null);
	}
	/**
	 * 返回值处理，transaction为空串
	 * @Description: 
	 * @param type	返回值类型
	 * @param ret
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(RespType type,RetCode ret) throws RRException {
		return create(type, KstHosConstant.DEFAULTTRAN, ret.getCode().toString(), ret.getMessage(), null, null);
	}
	/**
	 * 返回值处理
	 * @Description: 
	 * @param type
	 * @param transactionCode
	 * @param ret
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(RespType type,String transactionCode,RetCode ret) throws RRException {
		return create(type, transactionCode, ret.getCode().toString(), ret.getMessage(), null, null);
	}
	
	/**
	 * 返回值处理
	 * @Description: 
	 * @param type					返回值类型
	 * @param transactionCode		
	 * @param code
	 * @param message
	 * @param data					返回的数据结果集
	 * @param cfg					返回值转换相关配置,为空时，默认返回对象所有属性，且属性首字符大写
	 * @return
	 * @throws RRException 
	 */
	public static IRespHandler create(RespType type,String transactionCode,String code,String message,CommonResp<?> data,ParseConfig cfg) throws RRException {
		if(data==null) {
			throw new RRException(RetCode.Common.ERROR_NOTRESPONSE);
		}else {
			data.transactionCode(transactionCode).code(code).Message(message);
		}
		if(cfg==null) {
			cfg = new ParseConfig();
		}
		BaseRespHandler handler = null;
		switch (type) {
		case XML:
			handler = new XmlRespHandler();
			break;
		case JSON:
			handler = new JsonRespHandler();
			break;
		case XML_NODATA:
			handler = new XmlRespHandlerNotData();
			break;
		default:
			handler = new BaseRespHandler();
			break;
		}
		handler.data(data).config(cfg);
		return handler;
	}
	
}
