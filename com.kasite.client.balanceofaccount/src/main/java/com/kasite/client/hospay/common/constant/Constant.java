package com.kasite.client.hospay.common.constant;

import com.alipay.api.AlipayConstants;

/**
 * 常量
 *
 * @author cc
 */
public class Constant {
    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;

    /**
     * 菜单类型
     *
     * @author chenshun
     * @email 343675979@qq.com
     * @date 2016年11月15日 下午1:24:29
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author chenshun
     * @email 343675979@qq.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        private CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**健康之路constant*/
    public static final String ERRCODE = "errcode";
    public static final String ERRMESSAGE = "errmsg";
    public static final String CODE = "Code";
    public static final String MESSAGE = "Message";
    public static final String RESULT = "Result";
    public static final String TOTAL = "Total";
    public static final String OUTPUT = "OutPut";
    public static final String LIST = "List";
    public static final String SIZE = "size";
    public static final String ROWNUM = "rowNum";
    public static final String ENTITY = "Entity";
    public static final String DATA = "Data";
    public static final String RESPCODE = "RespCode";
    public static final String RESPMESSAGE = "RespMessage";
    public static final String TRANSACTIONCODE = "TransactionCode";
    public static final String RESP = "Resp";
    public static final String PAGE = "Page";
    public static final String PINDEX = "PIndex";
    public static final String PSIZE = "PSize";
    public static final String PCOUNT = "PCount";
    public static final String REQ = "Req";

    public static final String ClientId = "ClientId";
    public static final String ClientVersion = "ClientVersion";
    public static final String Sign = "Sign";
    public static final String SessionKey = "SessionKey";
    public static final String ROWS = "Rows";
    public static final String PayTypeName[] ={"线上支付","线下支付"};

    /**
     * 小薇问药公众号渠道ID9000058
     */
    public static final String WY_ChannelId = "9000058";
    /**
     * 药店后台9000059
     */
    public static final String WY_Store_ChannelId = "9000059";

    /**
     * 日期类型yyyy-MM-dd
     */
    public static final String DATEFORMATTYPE = "yyyy-MM-dd";
    /**
     * 日期类型yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIMEFORMATTYPE = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期类型yyyyMMddHHmmssSSS
     */
    public static final String SEQFORMATTYPE = "yyyyMMddHHmmssSSS";
    /**
     * ConnectionTimeOut 20秒
     */
    public static final Integer CONNECTIONTIMEOUT = 20000;
    /**
     * SoTimeOut 20秒
     */
    public static final Integer SOTIMEOUT = 20000;
    /**
     * 1800 秒
     */
    public static final Integer I1800 = 1800;

    /**
     * 900 秒
     */
    public static final Integer I900 = 900;

    /**
     * 128
     */
    public static final Integer I128 = 128;
    /**
     * def_Encoding UTF-8
     */
    public static final String DEF_ENCODING = "UTF-8";
    /**
     * MD5签名
     */
    public static final String SIGNTYPE_MD5 = "MD5";
    /**
     * 验证纯数字
     */
    public static final String REGULAR_NUMBER = "[0-9]+";
    /**
     * 验证手机号码
     */
    public static final String REGULAR_MOBILE = "^[1][0-9]{10}$";

    public static final String REGULAR_MOBILE_V2 = "^1[3|4|5|7|8]\\d{9}$";
    /**
     * 健康之路员工列表
     */
    public static final String JK_CORP_ORGID = "68EF488D78764D8CA95F2810FB985D43";
    /**
     * 健康之路组织架构
     */
    public static final String JK_OUT_ORGID = "437E43840ECE4C748B10253B323C5DDB";



    /*******************************************短信标签*************************/
    /**
     * 发送手机验证码短信标签
     */
    public final static String SENDVERCODE_HANDLERID = "10103511";
    /**
     * 移出黑名单提醒
     */
    public final static String DELBLACK_HANDLERID = "30000001";
    /**
     * 加入黑名单问题回复
     */
    public final static String ADDBLACK_HANDLERID = "30000002";


    /**
     * 扫码事件1-8状态
     * 1自主关注、2取消关注、3员工扫码新用户、4员工扫码非新用户、5药店扫码新用户、6药店扫码非新用户、7当天取关、8、深夜扫码
     *
     * 现作为返回数据类型定义
     */
    public final static int I0 = 0;
    public final static int I1 = 1;
    public final static int I2 = 2;
    public final static int I3 = 3;
    public final static int I4 = 4;
    public final static int I5 = 5;
    public final static int I6 = 6;
    public final static int I8 = 8;


    public final static int I10000 = 10000;

    /************对账模块*************/
    public final static String SUMMARYQLCBILL = "9001";
    public final static String SUMMARYTHREEPARTYSINGLEBILL = "9001";
    public final static String SUMMARYTHREEPARTYBILL = "9002";
    public final static String QUERYEVERYDAYBILLS = "9003";
    public final static String DOWNLOADHISBILLS = "9004";
    public final static String ADDPENDINGORDER = "9005";
    public final static String BASEDBIZCHANGESEVERYDAYBALANCE = "9006";


    public final static String WXCHANNELID = "100123";
    public final static String ZFBCHANNELID = "100125";
    public final static String ERRORSTATE = "-1";
    public final static String RECEIVABLESTATE = "1";
    public final static String REFUNDSTATE = "2";
    public final static String CHECKSTATE = "0";
    public final static String PAYBIZTYPE = "1";
    public final static String REFUNDBIZTYPE = "2";
    public final static String WAITESTATE = "0";
    public final static String BEINGPAIDSTATE = "1";
    public final static String SUCCESSSTATE = "2";

    /** MYSQL数据库*/
    public final static String MYSQL = "MYSQL";
    /** ORACLE数据库*/
    public final static String ORACLE = "ORACLE";
    /** 支付订单状态*/
    public final static String CHARGEORDERSTATE = "1";
    /** 退款订单状态*/
    public final static String REFUNDORDERSTATE = "2";

    public final static Integer HTTPCODE = 200;
    /** 全流程交易订单类型*/
    public final static String QLCCHANNELORDERTYPE = "1";
    /** 渠道交易订单类型*/
    public final static String CHANNELORDERTYPE = "2";
    /** HIS交易订单类型*/
    public final static String HISCHANNELORDERTYPE = "3";
    /** 订单执行状态 0 1 2*/
    public final static String EXEORDERSTATE = "2";
    /** 订单检查状态 0 */
    public final static String CHECKZEROORDERSTATE = "0";
    /** 订单检查状态 1 */
    public final static String CHECKONEORDERSTATE = "1";
    /** 订单检查状态 2 */
    public final static String CHECKTWOORDERSTATE = "2";

    /** 业务类型1 冲正*/
    public final static String BIZTYPEONE = "1";
    /** 业务类型2 退费*/
    public final static String BIZTYPETWO = "2";
    /** 业务类型3 财务调账*/
    public final static String BIZTYPETHREE = "3";

    /** 执行状态 待执行*/
    public final static String EXESTATEWAIT = "0";
    /** 执行状态 正在执行*/
    public final static String EXESTATEBEGIN = "1";
    /** 执行状态 执行完成*/
    public final static String EXESTATEFINISH = "2";


    /********************* 微信接口返回RETURN_CODE ********************/
    public static final String RETURN_CODE_SUCCESS = "SUCCESS";
    public static final String RETURN_CODE_FAIL = "FAIL";

    /********************* 微信接口参数名 ********************/
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    public static final String RESULT_CODE = "result_code";
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String TOTAL_FEE = "total_fee";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String ERR_CODE = "err_code";
    public static final String ERR_CODE_DES = "err_code_des";

    /*************************全局变量使用的code***************************/
    public static final String RET_10000 = "10000";
    public static final String RET_10001 = "10001";
    public static final Integer RET_INT10000 = 10000;
    public static final Integer FAIL_INT10000 = -10000;
    public static final String FAIL_10000 = "-10000";
    public static final String RET_SUCCESSMSG = "成功";
    public static final String RER_FAILMSG = "失败";

    /***********************支付宝参数*******************************/
    /**签名类型-视支付宝服务窗要求*/
    public static String SIGN_TYPE = AlipayConstants.SIGN_TYPE_RSA2;

    /****************************几方汇总配置参数******************************/
    public static final String THREEPARTY = "3";
    public static final String TWOPARTY = "2";


    /****************************HIS配置参数******************************/
    public static final String HISCONFIGZERO = "0";
    public static final String HISCONFIGONE = "1";
    /****************************普通常量******************************/
    public final static String S0 = "0";
    public final static String S1 = "1";
    public final static String S2 = "2";
    public final static String S3 = "3";
    /****************************HIS接口调用类型**********************/
    public final static String SOAP = "SOAP";
    public final static String HL7 = "HL7";

}
