package com.kasite.core.httpclient.http;



public class IParseParamEncodeImpl{
	static String URL_FLAG_START = "<soapenvencode>";
	static String URL_FLAG_END = "</soapenvencode>";
	private static IParseParamEncodeImpl instance;
	public static synchronized IParseParamEncodeImpl getInstance(){
		if(null == instance){
			instance = new IParseParamEncodeImpl();
		}
		return instance;
	}
	/**
	 * 解析
	 * @param param 参数报文 非空
	 * @param map 请求对象入参  可以为空
	 * @return 返回url和请求参数
	 */
	public EncodeVo parse(String soapXml){
		String retVal = soapXml;
		String args = null;
		EncodeVo vo = new EncodeVo();
		String param = soapXml;
		if (StringUtils.isNotBlank(param) && param.indexOf(URL_FLAG_START) >= 0) {
			int index1 = param.indexOf(URL_FLAG_START);
			int index2 = param.indexOf(URL_FLAG_END);
			args = param.substring(index1+URL_FLAG_START.length(),
					index2);
			String repUrl = param.substring(index1,
					index2+URL_FLAG_END.length());
			retVal = soapXml.replace(repUrl, "");
		}
		vo.setEncode(args);
		vo.setRetVal(retVal);
		return vo;
	}
	
	
}
