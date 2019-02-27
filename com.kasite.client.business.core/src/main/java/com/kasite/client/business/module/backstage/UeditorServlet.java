package com.kasite.client.business.module.backstage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.client.business.config.StorageManager;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.R;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@WebServlet(urlPatterns = "/ueditor/init.do")
public class UeditorServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String htmlPath = KasiteConfig.localConfigPath() + "/controller.html";
        File file = new File(htmlPath);
        if(!file.exists()) {
        	file.createNewFile();
        }else {
        	file.delete();
        	file.createNewFile();
        }
        //用于存储html字符串
        StringBuilder stringHtml = new StringBuilder();
        stringHtml.append("<!DOCTYPE html><html lang=\"en\"></html>");
        PrintStream printStream = new PrintStream(new FileOutputStream(htmlPath));
        printStream.println(stringHtml.toString());
        printStream.flush();
        printStream.close();
        req.setCharacterEncoding("utf-8");
        String retJson = new ActionEnter(new StorageManager(), req, htmlPath).exec();
        R r = R.ok().put("responseText", retJson).put("responseTextJson", JSONObject.parse(retJson));
        resp.getWriter().write(JSON.toJSONString(r));
    }
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req, resp);
    }
}
