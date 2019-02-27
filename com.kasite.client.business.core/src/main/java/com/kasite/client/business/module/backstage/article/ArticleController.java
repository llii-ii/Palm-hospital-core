package com.kasite.client.business.module.backstage.article;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.R;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryArticleList;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateArticle;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryArticleList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 文章管理
 * 
 * @author zhaoy
 *
 */
@RequestMapping("/article")
@RestController
public class ArticleController extends AbstractController {

	@Autowired
	IBasicService basicService;
	
	@PostMapping("/queryArticle.do")
	@RequiresPermissions("article:article:query")
	@SysLog(value="查询文章列表", isSaveResult=false)
	R queryArticle(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("queryArticle", reqParam, request);
		CommonResp<RespQueryArticleList> resp = basicService.queryArticleList(new CommonReq<ReqQueryArticleList>(new ReqQueryArticleList(msg,"query")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/addArticle.do")
	@RequiresPermissions("article:article:add")
	@SysLog(value="新增文章列表")
	R addArticle(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("addArticle", reqParam, request);
		CommonResp<RespMap> resp = basicService.addArticle(new CommonReq<ReqUpdateArticle>(new ReqUpdateArticle(msg, "add")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/updateArticle.do")
	@RequiresPermissions("article:article:update")
	@SysLog(value="编辑文章列表")
	R updateArticle(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("updateArticle", reqParam, request);
		CommonResp<RespMap> resp = basicService.updateArticle(new CommonReq<ReqUpdateArticle>(new ReqUpdateArticle(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/delArticle.do")
	@RequiresPermissions("article:article:delete")
	@SysLog(value="删除文章列表")
	R delArticle(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("delArticle", reqParam, request);
		CommonResp<RespMap> resp = basicService.delArticle(new CommonReq<ReqUpdateArticle>(new ReqUpdateArticle(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/toIsSue.do")
	@RequiresPermissions("article:article:update")
	@SysLog(value="发布")
	R toIsSue(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("toIsSue", reqParam, request);
		CommonResp<RespMap> resp = basicService.toIsSue(new CommonReq<ReqUpdateArticle>(new ReqUpdateArticle(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
	
	@PostMapping("/toHead.do")
	@RequiresPermissions("article:article:update")
	@SysLog(value="置顶")
	R toHead(String reqParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterfaceMessage msg = createInterfaceMsg("toHead", reqParam, request);
		CommonResp<RespMap> resp = basicService.toHead(new CommonReq<ReqUpdateArticle>(new ReqUpdateArticle(msg, "update")));
		return R.ok(resp.toJSONResult());
	}
}
