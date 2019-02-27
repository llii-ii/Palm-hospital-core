package com.kasite.client.core.msg.bean.bo;
 
public class Sms {
 
    private String phone;   //电话号码
    private String code;    //短信验证码
    private Long time;      //短信验证码生成时间
 
    public Sms() {
    }
 
    public Sms(String phone, String code, Long time) {
        this.phone = phone;
        this.code = code;
        this.time = time;
    }
 
    public String getPhone() {
        return phone;
    }
 
    public void setPhone(String phone) {
        this.phone = phone;
    }
 
    public String getCode() {
        return code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public Long getTime() {
        return time;
    }
 
    public void setTime(Long time) {
        this.time = time;
    }
}
