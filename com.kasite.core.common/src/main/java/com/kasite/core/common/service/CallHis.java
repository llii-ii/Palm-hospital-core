package com.kasite.core.common.service;

/**
 * 定义机构在接口实现类上返回表示这家医院的接口实现类
 * 后续通过 authInfo 的参数中获取到机构id进行匹配，最终判断是需要调用哪家医院的接口实现
 * @author daiyanshui
 */
public enum CallHis {
	/** 演示医院 */
	yanshiyiyuan("1024727"),
	/** 福建省 莆田市 仙游县妇幼保健院 */
	fjptxyxfybjy("1023571"),
	/** 福建省 泉州市 泉州儿童医院 */
	fjqzetyy("514"),
	/** 福建省 泉州市 福建医科大学附属第二医院（泉州二附院）*/
	fjykdxfsdeyy("59"),
	/** 湖北省 武汉市 湖北武汉亚洲心脏病院（武汉亚心医院） */
	hbwhyxyy("734"),
	/** 四川省 泸州市 纳溪区人民医院 */
	sclzsnxyy("1026977"),
	/** 四川省 绵阳市 人民医院 */
	scmysrmyy("22407"),
	/** 云南省 文山市 人民医院 */
	ynwssrmyy("1042149"),
	/** 四川省 泸州市龙马谭区人民医院 */
	sclzslmtyy("1031509"),
	/** 辽宁省 鞍山市 鞍山市中心医院 (汤岗子新城医院)*/
	astgzxcyy("21803"),
	/** 陕西省 安康市 安康市中医医院 */
	SX_AK_AKSZYYY("22623"),

	/**  福建省 泉州市 石狮（泉州）华侨医院*/
	FJ_QZ_HQYY("1029594"),
	/**  福建省 泉州市 关前医院*/
	FJ_QZ_GQYY("1024011"),
	
	/**  江苏省张家港第三人民医院*/
	JS_ZJG_ZJGDSRMYY("1014008"),
	/**  海南省 海口市 解放军第187中心医院*/
	HN_HK_JFJD187ZXYY("8000928"),
	
	/** 湖北省，十堰市，郧西县人民医院*/
	HB_SY_YXXRMYY("22108"),
	
	
	/**福建省，福州市，福建省人民医院*/
	FJ_FZ_FJSRMYY("36"),
	/*
	 * 后续医院名称别名明名规则：  省份_城市_医院 拼音首字母 全大写
	 * SC 四川
	 * HB 湖北
	 * LN 辽宁
	 * FJ 福建
	 * HN 海南
	 * 。。。
	 */
	
	
	
	
	; 
	public String orgCode;
	CallHis(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
}
