package com.kasite.client.business.module.backstage.survey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.FileUtils;
import com.kasite.core.common.util.MatrixToImageWriter;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.survey.SurveyService;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuerySubject;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestion;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestionFlow;
import com.kasite.core.serviceinterface.module.survey.req.ReqQuestionItem;
import com.kasite.core.serviceinterface.module.survey.req.ReqUpdateSubject;
import com.kasite.core.serviceinterface.module.survey.resp.RespQuerySubject;
import com.yihu.hos.util.JSONUtil;
import com.yihu.wsgw.api.InterfaceMessage;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@RestController
public class WtbjController extends AbstractController {

	@Autowired
	SurveyService surveyService;
	
	@PostMapping("/survey/querySubjectlistByOrgId.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询满意度调查问卷列表", isSaveResult=false)
	R querySubjectlistByOrgId(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySubjectlistByOrgId", reqParam, request);
		CommonResp<RespQuerySubject> respList = surveyService.querySubjectlistByOrgId(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg)));
		return R.ok(respList.toJSONResult());
	}
	
	@PostMapping("/survey/addDetailQuestion.do")
	@RequiresPermissions("survey:question:insert")
	@SysLog(value="新增问题信息")
	R addDetailQuestion(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addDetailQuestion", reqParam, request);
		CommonResp<RespMap> resp = surveyService.addDetailQuestion(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"add")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/upateItem.do")
	@RequiresPermissions("survey:questionItem:update")
	@SysLog(value="更新问题类型选项信息")
	R upateItem(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateQuestionItem", reqParam, request);
		CommonResp<RespMap> resp = surveyService.updateQuestionItem(new CommonReq<ReqQuestionItem>(new ReqQuestionItem(msg,"update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/deleteQuestionItems.do")
	@RequiresPermissions("survey:questionItem:delete")
	@SysLog(value="批量删除问题类型信息")
	R deleteQuestionItems(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("deleteQuestionItems", reqParam, request);
		CommonResp<RespMap> resp = surveyService.deleteQuestionItems(new CommonReq<ReqQuestionItem>(new ReqQuestionItem(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/updateSubjectbegin.do")
	@RequiresPermissions("survey:subject:update")
	@SysLog(value="更新满意度调查问卷信息")
	R updateSubjectbegin(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateSubjectbegin", reqParam, request);
		CommonResp<RespMap> resp = surveyService.updateSubjectBegin(new CommonReq<ReqUpdateSubject>(new ReqUpdateSubject(msg, "updateBegin")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/matdeleteQuestion.do")
	@RequiresPermissions("survey:question:delete")
	@SysLog(value="删除矩阵题目")
	R matdeleteQuestion(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("matdeleteQuestion", reqParam, request);
		CommonResp<RespMap> resp = surveyService.matdeleteQuestion(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/addQuestion.do")
	@RequiresPermissions("survey:question:insert")
	@SysLog(value="新增问卷问题")
	R addQuestion(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addQuestion", reqParam, request);
		CommonResp<RespMap> resp = surveyService.addQuestion(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"add")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/addItem.do")
	@RequiresPermissions("survey:questionItem:insert")
	@SysLog(value="新增问卷问题选项")
	R addItem(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addQuestionItem", reqParam, request);
		CommonResp<RespMap> resp = surveyService.addQuestionItem(new CommonReq<ReqQuestionItem>(new ReqQuestionItem(msg,"add")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/deleteQuestion.do")
	@RequiresPermissions("survey:question:delete")
	@SysLog(value="删除问卷问题")
	R deleteQuestion(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("deleteQuestion", reqParam, request);
		CommonResp<RespMap> resp = surveyService.deleteQuestion(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/updateQuest.do")
	@RequiresPermissions("survey:question:update")
	@SysLog(value="更新问卷问题")
	R updateQuest(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateQuest", reqParam, request);
		CommonResp<RespMap> resp = surveyService.updateQuestion(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/exchangeQuestionSort.do")
	@RequiresPermissions("survey:question:update")
	@SysLog(value="更新问题排序")
	R exchangeQuestionSort(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("exchangeQuestionSort", reqParam, request);
		CommonResp<RespMap> resp = surveyService.exchangeQuestionSort(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"exchange")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/exchangeQuestionItemSort.do")
	@RequiresPermissions("survey:questionItem:update")
	@SysLog(value="更新问题选项排序")
	R exchangeQuestionItemSort(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("exchangeQuestionItemSort", reqParam, request);
		CommonResp<RespMap> resp = surveyService.exchangeQuestionItemSort(new CommonReq<ReqQuestionItem>(new ReqQuestionItem(msg,"exchange")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/exchangeMatQuestionItemSorts.do")
	@RequiresPermissions("survey:questionItem:update")
	@SysLog(value="批量更新问题选项排序")
	R exchangeMatQuestionItemSorts(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("exchangeMatQuestionItemSorts", reqParam, request);
		CommonResp<RespMap> resp = surveyService.exchangeMatQuestionItemSorts(new CommonReq<ReqQuestionItem>(new ReqQuestionItem(msg,"exchange")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/addQuestionFlow.do")
	@RequiresPermissions("survey:questionFlow:insert")
	@SysLog(value="新增问题选项跳转")
	R addQuestionFlow(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addQuestionFlow", reqParam, request);
		CommonResp<RespMap> resp = surveyService.addQuestionFlow(new CommonReq<ReqQuestionFlow>(new ReqQuestionFlow(msg,"add")));
		return R.ok(resp.toJSONResult());
	}
	
	
	@PostMapping("/survey/delAllQuestionFlow.do")
	@RequiresPermissions("survey:questionFlow:delete")
	@SysLog(value="删除问题选项跳转")
	R delAllQuestionFlow(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addQuestionFlow", reqParam, request);
		CommonResp<RespMap> resp = surveyService.deleteQuestionFlow(new CommonReq<ReqQuestionFlow>(new ReqQuestionFlow(msg,"delete")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/addQuestionFlows.do")
	@RequiresPermissions("survey:questionFlow:insert")
	@SysLog(value="批量新增问题选项跳转")
	R addQuestionFlows(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addQuestionFlows", reqParam, request);
		CommonResp<RespMap> resp = surveyService.addQuestionFlows(new CommonReq<ReqQuestionFlow>(new ReqQuestionFlow(msg,"add")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/judgeQuestSumBySubjectid.do")
	@RequiresPermissions("survey:question:query")
	@SysLog(value="判断是否是判断题类型", isSaveResult=false)
	R judgeQuestSumBySubjectid(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("judgeQuestSumBySubjectid", reqParam, request);
		CommonResp<RespMap> resp = surveyService.judgeQuestSumBySubjectid(new CommonReq<ReqQuestion>(new ReqQuestion(msg,"query")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/deleteQuestionItem.do")
	@RequiresPermissions("survey:questionItem:delete")
	@SysLog(value="批量删除问题类型信息")
	R deleteQuestionItem(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("deleteQuestionItem", reqParam, request);
		CommonResp<RespMap> resp = surveyService.deleteQuestionItem(new CommonReq<ReqQuestionItem>(new ReqQuestionItem(msg,"update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/querySubjectById.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询满意度调查问卷详情", isSaveResult=false)
	R querySubjectById(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("querySubjectById", reqParam, request);
		CommonResp<RespQuerySubject> resp = surveyService.querySubjectById(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/updateSubjectForNet.do")
	@RequiresPermissions("survey:subject:update")
	@SysLog(value="问卷发布-保存")
	R updateSubjectForNet(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateSubjectForNet", reqParam, request);
		CommonResp<RespMap> resp = surveyService.updateSubjectForNet(new CommonReq<ReqUpdateSubject>(new ReqUpdateSubject(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	/**
	 * 查询问卷发布地址
	 * 
	 * @param key
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/survey/queryFbAddress.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询问卷发布地址", isSaveResult=false)
	R queryFbAddress(String key, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String address = KasiteConfig.getSurverUrl();
		return R.ok().put("Address", address);
	}
	
	/**
	 * 根据发布地址创建二维码
	 * 
	 * @param SubjectId
	 * @param URL
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/survey/createQRCode.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="查询问卷发布地址", isSaveResult=false)
	R createQRCode(String SubjectId, String URL, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(StringUtil.isBlank(SubjectId) || StringUtil.isBlank(URL)) {
			return R.error("参数为空!");
		}
		FileInputStream in = null;
		FileOutputStream out = null;
		
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		String content = URL;
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(content,
				BarcodeFormat.QR_CODE, 400, 400, hints);
		// 创建二维码本地存放根路径
		StringBuffer imagePath = KasiteConfig.getLocalConfigPathByName("uploadFile", true, true, true);
		String imageLocalPath = imagePath.append("/QRCode").toString();
		File file = new File(imageLocalPath);
		if(!file.exists()) {
			file.mkdirs();
		} 
		File file1 = new File(file.getAbsolutePath(), "subjectID"
				+ SubjectId + ".jpg");
		MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
		// 二维码临时路径
		File fileTmp = new File(imagePath.append("/images/tmpQRcode").toString());
		if(!fileTmp.exists()){
			fileTmp.mkdirs();
		}
		//拷贝文件到临时路径
		in = new FileInputStream(file1);
		out = new FileOutputStream(new File(fileTmp, "tmp" + "_subjectID"
				+ SubjectId + ".jpg"));
		  byte[] buffer=new byte[1024];
		int by = 0;
		while ((by = in.read(buffer)) != -1) {
			out.write(buffer, 0, by);
		}
		in.close();
		out.flush();
		out.close();
		
		String imagePathUrl = imagePath.substring(imagePath.indexOf("uploadFile")) + "/tmp_subjectID" + SubjectId + ".jpg";
		return R.ok().put("QRUrl", imagePathUrl);
	}
	
	@PostMapping("/survey/allStatisticaBySubjectId.do")
	@RequiresPermissions("survey:subject:query")
	@SysLog(value="问卷统计", isSaveResult=false)
	R allStatisticaBySubjectId(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("allStatisticaBySubjectId", reqParam, request);
		CommonResp<RespQuerySubject> resp = surveyService.allStatisticaBySubjectId(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg)));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/survey/downLoadFile.do")
	@RequiresPermissions("survey:survey:download")
	@SysLog(value="问题答案下载", isSaveResult=false)
	R downLoadFile(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WritableWorkbook wwb = null;
		WritableSheet wsheet = null;
		File tempFile = null;
		
		InterfaceMessage msg = createInterfaceMsg("allStatisticaBySubjectId", reqParam, request);
		CommonResp<RespQuerySubject> resp = surveyService.allStatisticaBySubjectId(new CommonReq<ReqQuerySubject>(new ReqQuerySubject(msg)));
		JSONArray respJsonArray = resp.toJSONResult().getJSONArray("Data");
		Integer questId = respJsonArray.getJSONObject(0).getInteger("QuestionId");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = "data_" + format.format(new Date()) + ".xls";
		String rootDir = request.getSession().getServletContext().getRealPath("/");
		String rootUrl = KasiteConfig.getTempfilePath();
		String savePath = rootUrl + "survey";
		File saveDir = new File(rootDir + File.separatorChar + savePath);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		savePath = savePath + File.separatorChar + filename;
		String tempPath = rootDir + File.separatorChar + savePath;
		tempFile = new File(tempPath);
		wwb = Workbook.createWorkbook(tempFile);
		if (null != questId) {
			initDayPrintContent(wwb, wsheet, respJsonArray.getJSONObject(0).getJSONArray("Result"), questId);
		} else{
			initAllDayPrintContent(wwb, wsheet, respJsonArray.getJSONObject(0).getJSONArray("Result"));
		}
		wwb.write();
		wwb.close();
		return R.ok().put("filePath", savePath);
	}
	
	/**
	 * 导出Excel
	 * 
	 * @param wsheet
	 * @param arry
	 */
	private void initDayPrintContent(WritableWorkbook wwb,
			WritableSheet wsheet, JSONArray arry, int id)
			throws Exception {
		try {//
			for (int i = 0; i < arry.size(); i++) {
				int type = arry.getJSONObject(i).getInteger("QuestType");
				if (id == arry.getJSONObject(i).getInteger("QuestId")) {
					String title = arry.getJSONObject(i).getString("Question");
					String sheep = "Q" + (i + 1);
					if (type == 1) {
						wsheet = wwb.createSheet(sheep, 0);
						initSC(title,
								arry.getJSONObject(i).getJSONArray(
										"SvQuestionItems"), wsheet);
					}
					if (type == 3) {
						wsheet = wwb.createSheet(sheep, 0);
						initTKT(title,
								arry.getJSONObject(i).getJSONArray(
										"SvQuestionItems"), wsheet);
					}
					if (type == 2) {
						wsheet = wwb.createSheet(sheep, 0);
						initMC(title,
								arry.getJSONObject(i).getJSONArray(
										"SvQuestionItems"), wsheet);
					}
					if (type == 4 || type == 5) {
						wsheet = wwb.createSheet(sheep, 0);
						initJZ(title, arry.getJSONObject(i), wsheet);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void initAllDayPrintContent(WritableWorkbook wwb,
			WritableSheet wsheet, JSONArray arry)
			throws Exception {
		try {//

			for (int i = 0; i < arry.size(); i++) {
				int type = arry.getJSONObject(i).getInteger("QuestType");
				String title = arry.getJSONObject(i).getString("Question");
				String sheep = "Q" + (i + 1);
				wsheet = wwb.createSheet(sheep, i);
				if (type == 1) {
					initSC(title,
							arry.getJSONObject(i).getJSONArray(
									"SvQuestionItems"), wsheet);
				}
				if (type == 3) {
					initTKT(title,
							arry.getJSONObject(i).getJSONArray(
									"SvQuestionItems"), wsheet);
				}
				if (type == 2) {
					initMC(title,
							arry.getJSONObject(i).getJSONArray(
									"SvQuestionItems"), wsheet);
				}
				if (type == 4 || type == 5) {
					initJZ(title, arry.getJSONObject(i), wsheet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 选择题
	 * 
	 * @param title
	 * @param arry
	 * @param wsheet
	 * @throws Exception
	 */
	private void initSC(String title, JSONArray arry,
			WritableSheet wsheet) throws Exception {
		Label label = null;
		label = new Label(0, 0, title);
		wsheet.addCell(label);
		label = new Label(0, 1, "选项");
		wsheet.addCell(label);
		// C,R
		label = new Label(1, 1, "回复数（占比）");
		wsheet.addCell(label);
		int pr = 2;
		for (int j = 0; j < arry.size(); j++) {
			String percent = arry.getJSONObject(j).getString("Percent");
			String sum = arry.getJSONObject(j).getString("Sum");
			String itemCont = arry.getJSONObject(j).getString("ItemCont");
			label = new Label(0, (pr), itemCont);
			wsheet.addCell(label);
			label = new Label(1, (pr), sum + "(" + percent + "%)");
			wsheet.addCell(label);
			JSONArray datas = arry.getJSONObject(j).getJSONArray(
					"OtherAnswer");
			if (null != datas && datas.size() > 0) {
				for (int i = 0; i < datas.size(); i++) {
					pr++;
					label = new Label(0, (pr), "其它答案");
					wsheet.addCell(label);
					label = new Label(1, (pr), JSONUtil.getJsonString(
							datas.getJSONObject(i), "OAnswer"));
					wsheet.addCell(label);
					if ((i + 1) == datas.size()) {
						pr++;
					}
				}
			} else {
				pr++;
			}
		}
	}

	/**
	 * 矩阵
	 * 
	 * @param title
	 * @param arry
	 * @param wsheet
	 * @throws Exception
	 */
	private void initJZ(String title, JSONObject json,
			WritableSheet wsheet) throws Exception {
		Label label = null;
		label = new Label(0, 0, title);
		wsheet.addCell(label);
		// C,R
		label = new Label(0, 1, "选项");
		wsheet.addCell(label);
		JSONArray arry3 = json.getJSONArray("categories");
		// 小标题
		for (int j = 0; j < arry3.size(); j++) {
			String question = arry3.getJSONObject(j).getString("ItemCont");
			label = new Label((j + 1), 1, question);
			wsheet.addCell(label);
		}
		int num = json.getInteger("TotalSamp");
		JSONArray arry1 = json.getJSONArray("series");
		int i = 0;
		for (int j = 0; j < arry1.size(); j++) {
			String question = arry1.getJSONObject(j).getString("Question");
			label = new Label(0, (j + 2), question);
			wsheet.addCell(label);
			for (int k = 0; k < arry1.size(); k++) {
				JSONArray arry2 = arry1.getJSONObject(i)
						.getJSONArray("data");
				i++;
				for (int n = 0; n < arry2.size(); n++) {
					int sum = arry2.getJSONObject(n).getInteger("Sum");
					BigDecimal c = new BigDecimal(sum * 100);
					BigDecimal c1 = new BigDecimal(num);
					if (num == 0) {
						label = new Label((n + 1), (j + 2), sum + "（" + 0
								+ "%）");
					} else{
						label = new Label((n + 1), (j + 2), sum + "（"
								+ c.divide(c1, 2, BigDecimal.ROUND_HALF_DOWN) + "%）");
					}
					wsheet.addCell(label);
				}
				k = arry1.size();
			}
		}
	}

	private void initTKT(String title, JSONArray arry,
			WritableSheet wsheet) throws Exception {// 填空题
		Label label = null;
		label = new Label(0, 0, title);
		wsheet.addCell(label);
		label = new Label(0, 1, "答案");
		wsheet.addCell(label);
		for (int j = 0; j < arry.size(); j++) {
			label = new Label(0, (j + 2), arry.getJSONObject(j).getString(
					"Answer"));
			wsheet.addCell(label);
		}
	}

	private void initMC(String title, JSONArray arry,
			WritableSheet wsheet) throws Exception {
		Label label = null;
		label = new Label(0, 0, title);
		wsheet.addCell(label);
		label = new Label(0, 1, "选项");
		wsheet.addCell(label);
		// C,R
		label = new Label(1, 1, "回复数（占比）");
		wsheet.addCell(label);
		for (int j = 0; j < arry.size(); j++) {
			String percent = arry.getJSONObject(j).getString("Percent");
			String sum = arry.getJSONObject(j).getString("Sum");
			String itemCont = arry.getJSONObject(j).getString("ItemCont");
			label = new Label(0, (j + 2), itemCont);
			wsheet.addCell(label);
			label = new Label(1, (j + 2), sum + "(" + percent + "%)");
			wsheet.addCell(label);
		}
	}
}
