//package com.yihu.hos.web;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.kasite.core.common.annotation.AuthUser;
//import com.kasite.core.common.bean.bo.AuthInfoVo;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.constant.ApiModule;
//import com.kasite.core.common.constant.KstHosConstant;
//import com.kasite.core.common.exception.ParamException;
//import com.kasite.core.common.util.DataUtils;
//import com.kasite.core.common.util.StringUtil;
//import com.kasite.core.common.util.XMLUtil;
//import com.yihu.hos.ApiCustomizeModule;
//import com.yihu.hos.IRetCode;
//import com.yihu.hos.exception.AbsHosException;
//import com.yihu.hos.util.CallApiUtils;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//public abstract  class BaseController {
//	/**
//	 * Code节点名称
//	 */
//	private String RESPCODE = "RespCode";
//	
//	/**
//	 * Message节点名称
//	 */
//	private String MESSAGE = "RespMessage";
//	
//	/**
//	 * Data节点名称
//	 */
//	private String DATA = "Data";
//	
//	public void initParamName(String code,String message,String data){
//		this.RESPCODE = code;
//		this.MESSAGE = message;
//		this.DATA = data;
//	}
//	
//	public JSONObject jsonResult(int code, String message){
//		JSONObject  j=new JSONObject();
//		j.put(RESPCODE, code);
//		j.put(MESSAGE, message);
//		return j;
//	}
//	
//	public JSONObject jsonData(int code, String message, Object result){
//		JSONObject  j=new JSONObject();
//		j.put(RESPCODE, code);
//		j.put(MESSAGE, message);
//		Object rs = result;
//		if(result instanceof com.common.json.JSONObject 
//				|| result instanceof com.common.json.JSONArray){
//			rs = result.toString();
//		}
//		j.put(DATA, rs);
//		return j;
//	}
//	
//	private String page_iTotalDisplayRecords = "iTotalDisplayRecords";
//	private String page_iTotalRecords = "iTotalRecords";
//	private String page_sEcho = "sEcho";
//	public JSONObject pageData(IRetCode retCode,Page page){
//		JSONObject result = new JSONObject();
//		if(null != page){
//			result.put(RESPCODE, retCode.getCode());
//			result.put(MESSAGE, retCode.getMessage());
//			if(null != page.getResult()){
//				result.put(DATA, JSONArray.fromObject(page.getResult()));
//			}else{
//				result.put(DATA, "[]");
//			}
//			result.put(page_iTotalDisplayRecords, page.getTotalItems());
//			result.put(page_iTotalRecords, page.getTotalItems());
//			result.put(page_sEcho,page.getPageNo());
//		}
//		return result;
//	}
//	
//	protected Page builderPagebyRequest(HttpServletRequest request) throws AbsHosException{
//		Page page = new Page();
//		String iDisplayStart = request.getParameter("iDisplayStart");
//		String iDisplayLength = request.getParameter("iDisplayLength");
//		String page_sEcho = request.getParameter("sEcho");
//		try{
//			if(null != iDisplayLength){
//				page.setPageSize(Integer.parseInt(iDisplayLength));
//			}
//			if(null != iDisplayStart){
//				page.setStartRows(Integer.parseInt(iDisplayStart));
//			}
//			if(StringUtil.isNotBlank(page_sEcho)){
//				page.setPageNo(Integer.parseInt(page_sEcho));
//			}
//		}catch(Exception e){
//			throw new ParamException("请求初始化 Page 对象异常");
//		}
//		return page;
//	}
//	
//	private static Pattern pattern = Pattern.compile("/+([A-Za-z]*)/+call([\\s\\S]*)Api.do");
//	
//	/**
//	 * doPost
//	 *
//	 * @param api
//	 * @param params
//	 * @param request
//	 * @return
//	 * @author 無
//	 * @date 2018年4月25日 下午6:00:27
//	 */
//	public ResponseMessage doPost(AuthInfoVo authInfo,String api, String params, HttpServletRequest request) {
//		try {
//			String userAget = request.getHeader("User-Agent").toLowerCase();
//			String clientId = KstHosConstant.WX_CHANNEL_ID;
//			if (userAget.contains(KstHosConstant.USERAGET_ALIPAY)) {
//				clientId = KstHosConstant.ZFB_CHANNEL_ID;
//			} else if (userAget.contains(KstHosConstant.USERAGET_WX)) {
//				clientId = KstHosConstant.WX_CHANNEL_ID;
//			}
//			String resp = CallApiUtils.callApi(authInfo, api, params);
//			Document doc = DocumentHelper.parseText(resp);
//			Element root = doc.getRootElement();
//			String respCode = XMLUtil.getString(root, KstHosConstant.RESPCODE, false);
//			String respJs = DataUtils.xml2JSON(resp);
//			if(KstHosConstant.SUCCESSCODE.equals(respCode)) {
//				return new ResponseMessage(true, respJs, null);
//			}else {
//				return new ResponseMessage(false, respJs, null);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseMessage(false, null, e.getLocalizedMessage());
//		}
//		
//	}
//
//	/**
//	 * callApi
//	 *
//	 * @param request
//	 * @param response
//	 * @param customize
//	 *            该值不为空则调用自定义API
//	 * @return
//	 * @author 無
//	 * @date 2018年4月25日 下午6:00:39
//	 */
//	@RequestMapping(value = "/call{customize}Api.do")
//	@ResponseBody
//	public String callApi(@AuthUser AuthInfoVo authInfo,HttpServletRequest request, HttpServletResponse response,
//			@PathVariable("customize") String customize) {
//		String apiFullName = "";
//		String apiParam = "";
//		String result = "";
//		String controller = "";
//		String api = "";
//		try {
//			controller = getControllerName(request.getServletPath());
//			api = request.getParameter("api");
//			if (StringUtil.isNotEmpty(api)) {
//				apiFullName = getApi(api, controller, customize);
//			} else {
//				KasiteConfig.print("RequestMapping:" + request.getServletPath() + "\r\nAPI:空");
//				return null;
//			}
//			apiParam = DataUtils.jsonToXml(request.getParameter("apiParam"));
//			ResponseMessage resp = doPost(authInfo,apiFullName, apiParam, request);
//			result = resp.getResult().toString();
//			KasiteConfig.print("\r\n控制:" + controller + "\r\n请求:" + request.getServletPath() + "\r\n" + "参数:"
//					+ apiParam + "\r\n" + "API:" + apiFullName + "\r\n" + "返回:" + result + "\r\n");
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			KasiteConfig.print("\r\n控制:" + controller + "\r\n请求:" + request.getServletPath() + "\r\n" + "参数:"
//					+ apiParam + "\r\n" + "API:" + apiFullName + "\r\n" + "返回:" + e.getLocalizedMessage() + "\r\n");
//		}
//		return null;
//	}
//
//	/**
//	 * 获取API全名称
//	 *
//	 * @param api
//	 * @param controller
//	 * @param customize
//	 * @return
//	 * @author 無
//	 * @date 2018年4月26日 上午11:03:57
//	 */
//	public String getApi(String api, String controller, String customize) {
//		String apiFullName = "";
//		if (StringUtil.isNotEmpty(api)) {
//			if (StringUtil.isEmpty(customize)) {
//				/** Controller和ApiModule对应关系 */
//				switch (controller) {
//				case "basic":
//					apiFullName = ApiModule.Basic.valueOf(api).getName();
//					break;
//				case "hospital":
//					apiFullName = ApiModule.Basic.valueOf(api).getName();
//					break;
//				case "bat":
//					apiFullName = ApiModule.Bat.valueOf(api).getName();
//					break;
//				case "order":
//					apiFullName = ApiModule.Order.valueOf(api).getName();
//					break;
//				case "pay":
//					apiFullName = ApiModule.Pay.valueOf(api).getName();
//					break;
//				case "queue":
//					apiFullName = ApiModule.Queue.valueOf(api).getName();
//					break;
//				case "report":
//					apiFullName = ApiModule.Report.valueOf(api).getName();
//					break;
//				case "survery":
//					apiFullName = ApiModule.Survey.valueOf(api).getName();
//					break;
//				case "webapp":
//					apiFullName = ApiModule.Bat.valueOf(api).getName();
//					break;
//				case "yygh":
//					apiFullName = ApiModule.YY.valueOf(api).getName();
//					break;
//				default:
//					apiFullName = "未知的Controller";
//					break;
//				}
//			} else {
//				/** 自定义的都走ApiCustomizeModule */
//				apiFullName = ApiCustomizeModule.CustomizeApiModule.valueOf(api).getName();
//			}
//		} else {
//			apiFullName = "";
//		}
//		return apiFullName;
//	}
//
//	/**
//	 * 获取请求的Controller名
//	 *
//	 * @param path
//	 * @return
//	 * @author 無
//	 * @date 2018年4月26日 上午11:03:11
//	 */
//	public String getControllerName(String path) {
//		String name = null;
////		Pattern pattern = Pattern.compile("/+([A-Za-z]*)/+call([\\s\\S]*)Api.do");
//		Matcher matcher = pattern.matcher(path);
//		if (matcher.find()) {
//			matcher.reset();
//			while (matcher.find()) {
//				/** 取第一个正则表达式匹配的数据 */
//				name = matcher.group(1);
//			}
//		}
//		return name;
//	}
//}
