package com.kasite.core.common.dao.vo;

/**
 * 
 * @author daiys
 * @date 2015-1-4
 */
public interface RetCode {

    String getMessage();

    Integer getCode();

    /**
     * 调用接口返回代码定义 定义范围：
     * 
     * @author daiys
     * @date 2015-1-4
     */
    enum Common implements RetCode {

        /**
         * 业务逻辑公共返回 Code 所有定义为 10000
         */
        Success(10000, "成功。"),
        /**
         * 通用失败操作返回
         */
        Fail(-10000, "失败。"),
        /**
         * -11000 参数解析异常。
         */
        Param_Error(-11000, "参数解析异常。"),

        /**
         * -12000类型转换异常
         */
        Parse_Error(-12000, "类型转换异常。"),

        /**
         * -13000初始化异常
         */
        Init_Error(-13000, "初始化异常。"),

        /**
         * -13001鉴权失败。
         */
        Auth_Error(-13001, "鉴权失败。"),

        /**
         * -14000接口调用异常
         */
        Call_Error(-14000, "接口调用异常。"),

        /**
         * -14001 没有查询到药店信息
         */
        Store_Null_Error(-14001, "没有查询到药店信息。"),
        /**
         * -14002 没有找到用户信息。
         */
        User_Null_Error(-14002, "没有找到用户信息。"),

        /**
         * -14003 重复操作异常
         */
        Repeat_Error(-14003, "重复操作异常。"),

        /**
         * -14004 没有找到对应信息
         */
        Null_Error(-14004, "没有找到对应信息。"),
        /**
         * -14005 日期异常
         */
        Date_Error(-14005, "日期异常。"),
        /**
         * -14006 卡券信息异常
         */
        Coupon_Error(-14006, "卡券信息异常。"),

        /**
         * -15000Sql执行异常
         */
        ExecSql_Error(-15000, "Sql执行异常。"),

        /**
         * -16000 Htpp请求异常
         */
        Http_Error(-16000, "Htpp请求异常"),

        /**
         * -19000未知异常
         */
        UnKnown_Error(-19000, "未知异常。"), ;
        /** 代码 */
        private Integer code;
        /** 消息 */
        private String message;

        public String getMessage() {
            return message;
        }

        Common(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }
    }

    /**
     * 
     * @author daiys
     * @date 2015-8-3
     */
    enum MYD implements RetCode {
        ERROR_CALLDB(-10001, "调用异常");
        /** 代码 */
        private Integer code;
        /** 消息 */
        private String message;

        public String getMessage() {
            return message;
        }

        MYD(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }
    }

}
