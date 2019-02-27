package com.kasite.client.hospay.module.bill.controller;

import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.client.hospay.module.bill.util.oauth.GoogleAuthenticatorSample;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * TODO 用于获取用户授权秘钥以及验证当前验证码的正确性
 */
@RequestMapping(value = "/billOauthController")
@RestController
public class BillOauthController {

     @Autowired
     RequestHandlerParam requestHandlerParam;

     @Autowired
     GoogleAuthenticatorSample googleAuthenticatorSample;

     /**
      * 生成用户秘钥,直接读取配置文件中的userName和hostName
      * @return String
      */
     @PostMapping(value = "/genSecret.do", produces = "text/html;charset=UTF-8")
     public String genSecret(){
          return googleAuthenticatorSample.genSecret(requestHandlerParam.userName, requestHandlerParam.hostName);
     }

     /**
      * 根据前端传入的验证码判断当前请求是否是正常
      * @param code
      * @return
      * TODO 秘钥由 genSecret()方法获取后，填在配置文件中
      */
     @PostMapping(value = "/oauthCode.do", produces = "application/json;charset=UTF-8")
     public R oauthCode (String code){
          Boolean authCode = false;
          if (StringUtil.isNotBlank(requestHandlerParam.passWord)){
               if (requestHandlerParam.passWord.equals(code)){
                    authCode = true;
               }
          }else {
               authCode = googleAuthenticatorSample.authcode(code, requestHandlerParam.secret);
          }
          if (!authCode){
               return R.error().put("RespCode","-10000").put("RespMessage","当前验证码有误,请重新输入!!!");
          }
          return R.error().put("RespCode","10000").put("RespMessage","验证成功,请继续操作!!!");
     }





}
