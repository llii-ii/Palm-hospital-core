package com.kasite.core.common.validator.dict;

import java.util.function.Supplier;

public interface Race {
	static Race create( Supplier< Race > supplier ) {
		 return supplier.get();
	}
	
	default String getName(String value) {
		switch (value) {
		case "汉族":return "汉族";
		case "蒙古族":return "蒙古族";
		case "回族":return "回族";
		case "藏族":return "藏族";
		case "维吾尔族":return "维吾尔族";
		case "苗族":return "苗族";
		case "彝族":return "彝族";
		case "壮族":return "壮族";
		case "布依族":return "布依族";
		case "朝鲜族":return "朝鲜族";
		case "满族":return "满族";
		case "侗族":return "侗族";
		case "瑶族":return "瑶族";
		case "白族":return "白族";
		case "土家族":return "土家族";
		case "哈尼族":return "哈尼族";
		case "哈萨克族":return "哈萨克族";
		case "傣族":return "傣族";
		case "黎族":return "黎族";
		case "傈僳族":return "傈僳族";
		case "佤族":return "佤族";
		case "畲族":return "畲族";
		case "高山族":return "高山族";
		case "拉祜族":return "拉祜族";
		case "水族":return "水族";
		case "东乡族":return "东乡族";
		case "纳西族":return "纳西族";
		case "景颇族":return "景颇族";
		case "柯尔克孜族":return "柯尔克孜族";
		case "土族":return "土族";
		case "达翰尔族":return "达翰尔族";
		case "仫佬族":return "仫佬族";
		case "羌族":return "羌族";
		case "布朗族":return "布朗族";
		case "撒拉族":return "撒拉族";
		case "毛南族":return "毛南族";
		case "仡佬族":return "仡佬族";
		case "锡伯族":return "锡伯族";
		case "阿昌族":return "阿昌族";
		case "普米族":return "普米族";
		case "塔吉克族":return "塔吉克族";
		case "怒族":return "怒族";
		case "乌孜别克族":return "乌孜别克族";
		case "俄罗斯族":return "俄罗斯族";
		case "鄂温克族":return "鄂温克族";
		case "德昂族":return "德昂族";
		case "保安族":return "保安族";
		case "裕固族":return "裕固族";
		case "京族":return "京族";
		case "塔塔尔族":return "塔塔尔族";
		case "独龙族":return "独龙族";
		case "鄂伦春族":return "鄂伦春族";
		case "赫哲族":return "赫哲族";
		case "门巴族":return "门巴族";
		case "珞巴族":return "珞巴族";
		case "基诺族":return "基诺族";
		case "外国血统":return "外国血统";
		case "其它":return "其它";

			default:return null;
		}
	}
	/**对字典值进行校验*/
	default boolean validate(String value){
		if(null == getName(value)) {
			return false;
		}else {
			return true;
		}
	}
}
