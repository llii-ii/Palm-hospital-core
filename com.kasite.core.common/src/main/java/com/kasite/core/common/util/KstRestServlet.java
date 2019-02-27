//package com.kasite.core.common.util;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.common.json.JSONException;
//import com.common.json.JSONObject;
//import com.coreframework.util.ReflectionUtils;
//import com.coreframework.util.StringUtil;
//import com.kasite.core.common.constant.KstHosConstant;
//import com.kasite.core.common.log.LogBody;
//import com.yihu.wsgw.api.InterfaceMessage;
//
///**
// * HTTP方式调用接口-在子工程的web.xml中配置KstRestServlet开启http调用
// * 
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午3:31:17
// */
//public class KstRestServlet extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_DEFAULT);
//
//	public KstRestServlet() {
//		super();
//	}
//
//	@Override
//	public void init() throws ServletException {
//
//	}
//
//	@Override
//	public void destroy() {
//		super.destroy();
//
//	}
//
//	/**
//	 * 返回结果
//	 *
//	 * @param response
//	 * @param paramType
//	 * @param code
//	 * @param message
//	 * @author 無
//	 * @date 2018年4月24日 下午3:32:34
//	 */
//	public void respWrite(HttpServletResponse response, int paramType, int code, String message) {
//		if (paramType == 0) {
//			JSONObject json = new JSONObject();
//			try {
//				json.put("RespCode", code);
//				json.put("RespMessage", message);
//			} catch (JSONException e1) {
//				e1.printStackTrace();
//			}
//			try {
//				response.getWriter().write(json.toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				response.getWriter().write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Resp><RespCode>" + code
//						+ "</RespCode><RespMessage>" + message + "</RespMessage></Resp>");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * get调用
//	 *
//	 * @param request
//	 * @param response
//	 * @throws ServletException
//	 * @throws IOException
//	 * @author 無
//	 * @date 2018年4月24日 下午3:32:19
//	 */
//	@Override
//	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		this.doPost(request, response);
//
//	}
//
////	/**
////	 * post 调用
////	 *
////	 * @param request
////	 * @param response
////	 * @throws ServletException
////	 * @throws IOException
////	 * @author 無
////	 * @date 2018年4月24日 下午3:32:10
////	 */
////	@Override
////	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////
////		int paramType = 0;
////		int outType = 0;
////
////		String authInfo = null;
////		String api = null;
////		String param = null;
////		String v = null;
////		String sequenceNo = null;
////		String className = null;
////		String methodName = null;
////		try {
////			request.setCharacterEncoding("utf-8");
////			response.setContentType("text/html;charset=UTF-8");
////			String paramTypeStr = request.getParameter("ParamType");
////			if (!StringUtil.isBlank(paramTypeStr)) {
////				paramType = Integer.parseInt(paramTypeStr);
////			}
////			String outTypeStr = request.getParameter("OutType");
////			if (!StringUtil.isBlank(outTypeStr)) {
////				outType = Integer.parseInt(outTypeStr);
////			}
////			authInfo = request.getParameter("AuthInfo");
////			api = request.getParameter("Api");
////			param = request.getParameter("Param");
////			v = request.getParameter("V");
////			sequenceNo = request.getParameter("SequenceNo");
////			className = request.getParameter("ClassName");
////			methodName = request.getParameter("MethodName");
////			if (StringUtil.isBlank(className) || StringUtil.isBlank(methodName)) {
////				respWrite(response, paramType, 2000, "远程实现类或方法不能为空");
////			}
////		} catch (Exception e) {
////			this.respWrite(response, paramType, 20000, e.getMessage());
////			LogUtil.error(log, new LogBody(authInfo).set("api", api).set("authInfo", authInfo).set("param", param)
////					.set("Exception", e.getMessage()));
////			return;
////		}
////		String clientip = null;
////		// 获取ng转义前的ip
////		clientip = request.getHeader("X-Real_IP");
////		if (StringUtil.isBlank(clientip)) {
////			clientip = request.getRemoteAddr();
////		}
////		String ret = invoke(className, methodName, authInfo, sequenceNo, api, param, paramType, outType, v, clientip);
////		response.getWriter().write(ret);
////	}
//
//	/**
//	 * 类反射调用方法
//	 * 
//	 * @param clazzName
//	 * @param methodName
//	 * @param authInfo
//	 * @param sequenceNo
//	 * @param api
//	 * @param param
//	 * @param paramType
//	 * @param outType
//	 * @param v
//	 * @param clientip
//	 * @return
//	 */
//	public String invoke(String className, String methodName, String authInfo, String sequenceNo, String api,
//			String param, int paramType, int outType, String v, String clientip) {
//		InterfaceMessage msg = new InterfaceMessage();
//		msg.setApiName(api);
//		msg.setAuthInfo(authInfo);
//		msg.setClientIp(clientip);
//		msg.setOutType(outType);
//		msg.setParam(param);
//		msg.setParamType(paramType);
//		msg.setVersion(v);
//		msg.setSeq(sequenceNo);
//		Object obj = ReflectionUtils.newInstance(className);
//		Object retObj = ReflectionUtils.invokeMethod(obj, methodName, new Class[] { InterfaceMessage.class },
//				new Object[] { msg });
//		if (null != retObj) {
//			return retObj.toString();
//		}
//		return "";
//	}
//}
