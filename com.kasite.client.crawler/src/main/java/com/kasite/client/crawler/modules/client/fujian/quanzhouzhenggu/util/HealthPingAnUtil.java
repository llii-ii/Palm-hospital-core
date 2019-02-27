package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.util;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.coreframework.util.ArithUtil;
import com.coreframework.util.DateOper;
import com.kasite.core.common.util.StringUtil;

/**
 * 
 * @className: PingAnUtil
 * @author: lcz
 * @date: 2018年6月7日 下午3:53:55
 */
@Component
public class HealthPingAnUtil {
	
	/**
	 * 民族Map
	 */
	private static Map<String, String> raceMap = new ConcurrentHashMap<String, String>();
	public HealthPingAnUtil() {
		/* 医院：汉族  蒙古族  回族  藏族  维吾尔族  苗族  彝族  壮族  布依族  朝鲜族  满族  侗族  瑶族  白族  土家族  哈尼族  哈萨克族  
		 * 傣族  黎族  傈僳族  佤族  畲族  高山族  拉祜族  水族  东乡族  纳西族  景颇族  柯尔克孜族  土族  达翰尔族  仫佬族  羌族  布朗族  
		 * 撒拉族  毛南族  仡佬族  锡伯族  阿昌族  普米族  塔吉克族  怒族  乌孜别克族  俄罗斯族  鄂温克族  德昂族  保安族  裕固族  京族  
		 * 塔塔尔族  独龙族  鄂伦春族  赫哲族  门巴族  珞巴族  基诺族  外国血统  其它 
		 * 
		 * 平安：1 汉族 2 蒙古族 3 回族 4 藏族  5 维吾尔族  6 苗族 7 彝族 8 壮族 9 布依族 10 朝鲜族 11 满族 12 侗族 13 瑶族 14 白族 
		 * 15 土家族  16 哈尼族 17 哈萨克族 18 傣族  19 黎族20 傈傈族  21 佤族 22 畲族 23 高山族 24 拉祜族 25 水族  26 东乡族
		 * 27 纳西族 28 景颇族 29 柯尔克孜族 30 土族 31 达翰尔族 32 仫佬族  33 羌族 34 布朗族 35 撒拉族 36 毛南族  37 仡佬族 
		 *  38 锡伯族 39 阿昌族 40 普米族  41 塔吉克族 42 怒族  43 乌孜别克族 44 俄罗斯族 45 鄂温克族 46 德昂族 47 保安族  
		 *  48 裕固族  49 京族 50 塔塔尔族 51 独龙族  52 鄂伦春族  53 赫哲族  54 门巴族
		 *  55 珞巴族 56 基诺族  90 外籍人士 99 其他*/
		raceMap.put("汉族","1");
		raceMap.put("蒙古族", "2");
		raceMap.put("回族", "3");
		raceMap.put("藏族", "4");
		raceMap.put("维吾尔族", "5");
		raceMap.put("苗族", "6");
		raceMap.put("彝族", "7");
		raceMap.put("壮族", "8");
		raceMap.put("布依族", "9");
		raceMap.put("朝鲜族", "10");
		raceMap.put("满族", "11");
		raceMap.put("侗族", "12");
		raceMap.put("瑶族", "13");
		raceMap.put("白族", "14");
		raceMap.put("土家族", "15");
		raceMap.put("哈尼族", "16");
		raceMap.put("哈萨克族", "17");
		raceMap.put("傣族", "18");
		raceMap.put("黎族", "19");
		raceMap.put("傈僳族", "20");
		raceMap.put("佤族", "21");
		raceMap.put("畲族", "22");
		raceMap.put("高山族", "23");
		raceMap.put("拉祜族", "24");
		raceMap.put("水族", "25");
		raceMap.put("东乡族", "26");
		raceMap.put("纳西族", "27");
		raceMap.put("景颇族", "28");
		raceMap.put("柯尔克孜族", "29");
		raceMap.put("土族", "30");
		raceMap.put("达翰尔族", "31");
		raceMap.put("仫佬族", "32");
		raceMap.put("羌族", "33");
		raceMap.put("布朗族", "34");
		raceMap.put("撒拉族", "35");
		raceMap.put("毛南族", "36");
		raceMap.put("仡佬族", "37");
		raceMap.put("锡伯族", "38");
		raceMap.put("阿昌族", "39");
		raceMap.put("普米族", "40");
		raceMap.put("塔吉克族", "41");
		raceMap.put("怒族", "42");
		raceMap.put("乌孜别克族", "43");
		raceMap.put("俄罗斯族", "44");
		raceMap.put("鄂温克族", "45");
		raceMap.put("德昂族", "46");
		raceMap.put("保安族", "47");
		raceMap.put("裕固族", "48");
		raceMap.put("京族", "49");
		raceMap.put("塔塔尔族", "50");
		raceMap.put("独龙族", "51");
		raceMap.put("鄂伦春族", "52");
		raceMap.put("赫哲族", "53");
		raceMap.put("门巴族", "54");
		raceMap.put("珞巴族", "55");
		raceMap.put("基诺族", "56");
		raceMap.put("外国血统", "90");
		raceMap.put("外籍人士", "90");
		raceMap.put("其它", "99");
		raceMap.put("其他", "99");
	}
	/***
	 * 获取平安对应的民族ID
	 * @Description: 
	 * @return
	 */
	public String getPaRaceValue(String key) {
		return raceMap.get(key);
	}
	
	
	/**
	 * 证件类型转换,将平安的证件类型字典转换为医院对应的证件类型字典
	 * 平安 证件类型  01	居民身份证   02	中国人民解放军军官证   03 中国人民武装警察警官证   04	香港特区护照/港澳居民来往内地通行证   05	澳门特区护照/港澳居民来往内地通行证   06	台湾居民来往大陆通行证   07	外国人永久居留证   08	外国人护照   09	残疾人证   10	军烈属证明   11	外国人就业证   12	外国专家证   13	外国人常驻记者证   14	台港澳人员就业证   15	回大陆（内地）定居专家证   20	户口簿   21	出生证   90	社会保障卡   99	其他身份证件
	 * 医院接口证件类型   01	居民身份证   02	居民户口簿  03	护照   04	军官证  05	驾驶证  06	港澳居民来往内地通行证 07	台湾居民来往内地通行证 08	士兵证 09	返乡证 10	组织机构代码 11	港澳通行证  12	台湾通行证  13	户口簿  14	学生证  15	国际海员证  16	外国人永久居留证    17	旅行证  18	警官证 19	微信号   21	台胞证 22	电子就医码  23	社会保障号码  99	其他法定有效证件
	 * @Description: 
	 * @return
	 */
	public String parsePingAnCertTypeToHos(String certType) {
		String hosCertType = null;
		if("02".equals(certType)) {
			//中国人民解放军军官证
			hosCertType = "04";
		}else if("03".equals(certType)) {
			//警官证
			hosCertType = "18";
		}else if("04".equals(certType) || "05".equals(certType)) {
			//香港特区护照、港澳居民来往内地通行证
			hosCertType = "06";
		}else if("06".equals(certType)) {
			//台湾居民来往大陆通行证
			hosCertType = "07";
		}else if("07".equals(certType)) {
			//台湾居民来往大陆通行证
			hosCertType = "16";
		}else if("08".equals(certType)) {
			//外国人护照
			hosCertType = "03";
		}else if("09".equals(certType)) {
			//残疾人证
			hosCertType = "";
		}else if("10".equals(certType)) {
			//军烈属证明
			hosCertType = "";
		}else if("11".equals(certType)) {
			//外国人就业证
			hosCertType = "";
		}else if("12".equals(certType)) {
			//外国专家证
			hosCertType = "";
		}else if("13".equals(certType)) {
			//外国人常驻记者证
			hosCertType = "";
		}else if("14".equals(certType)) {
			//台港澳人员就业证
			hosCertType = "";
		}else if("15".equals(certType)) {
			//回大陆（内地）定居专家证
			hosCertType = "";
		}else if("20".equals(certType)) {
			//户口簿
			hosCertType = "02";
		}else if("21".equals(certType)) {
			//出生证
			hosCertType = "";
		}else if("90".equals(certType)) {
			//社会保障卡
			hosCertType = "23";
		}
		return hosCertType;
	} 
	/**
	 * 证件类型转换,将医院对应的证件类型字典转换为平安的证件类型字典
	 * 平安 证件类型  01	居民身份证   02	中国人民解放军军官证   03 中国人民武装警察警官证   04	香港特区护照/港澳居民来往内地通行证   05	澳门特区护照/港澳居民来往内地通行证   06	台湾居民来往大陆通行证   07	外国人永久居留证   08	外国人护照   09	残疾人证   10	军烈属证明   11	外国人就业证   12	外国专家证   13	外国人常驻记者证   14	台港澳人员就业证   15	回大陆（内地）定居专家证   20	户口簿   21	出生证   90	社会保障卡   99	其他身份证件
	 * 医院接口证件类型   01	居民身份证   02	居民户口簿  03	护照   04	军官证  05	驾驶证  06	港澳居民来往内地通行证 07	台湾居民来往内地通行证 08	士兵证 09	返乡证 10	组织机构代码 11	港澳通行证  12	台湾通行证  13	户口簿  14	学生证  15	国际海员证  16	外国人永久居留证    17	旅行证  18	警官证 19	微信号   21	台胞证 22	电子就医码  23	社会保障号码  99	其他法定有效证件
	 * @Description: 
	 * @return
	 */
	public String parseHosCertTypeToPa(String certType) {
		if(StringUtil.isBlank(certType)) {
			return "";
		}
		String paCertType = null;
		if("04".equals(certType)) {
			//中国人民解放军军官证
			paCertType = "02";
		}else if("18".equals(certType)) {
			//警官证
			paCertType = "03";
		}else if("06".equals(certType)) {
			//香港特区护照、港澳居民来往内地通行证
			paCertType = "04";//或者05
		}else if("07".equals(certType)) {
			//台湾居民来往大陆通行证
			paCertType = "06";
		}else if("16".equals(certType)) {
			//台湾居民来往大陆通行证
			paCertType = "07";
		}else if("03".equals(certType)) {
			//外国人护照
			paCertType = "08";
		}
//		else if("--".equals(certType)) {
//			//残疾人证
//			paCertType = "09";
//		}else if("".equals(certType)) {
//			//军烈属证明
//			paCertType = "10";
//		}else if("".equals(certType)) {
//			//外国人就业证
//			paCertType = "11";
//		}else if("".equals(certType)) {
//			//外国专家证
//			paCertType = "12";
//		}else if("".equals(certType)) {
//			//外国人常驻记者证
//			paCertType = "13";
//		}else if("".equals(certType)) {
//			//台港澳人员就业证
//			paCertType = "14";
//		}else if("".equals(certType)) {
//			//回大陆（内地）定居专家证
//			paCertType = "15";
//		}else if("".equals(certType)) {
//			//出生证
//			paCertType = "21";
//		}
		
		else if("02".equals(certType)) {
			//户口簿
			paCertType = "20";
		}else if("23".equals(certType)) {
			//社会保障卡
			paCertType = "90";
		}
		return paCertType;
	} 
	/**
	 * 就诊方式字典转换，将医院返回的就诊方式转换为平安的就诊方式
	 * 医院：0 普通门诊 1 生育门诊 2 住院 3 生育住院 4 急诊 5 特病门诊 6 特需门诊 7 专家门诊 8 体检 9 预防接种 10 其他
	 * 平安：11 普通门诊 12 急诊 13 门诊特病 14 门诊慢病 15 特需门诊 21 普通住院 22 特殊病种住院 23 转外诊治住院 24 特需住院 31 家庭病床 41 药店购药
	 * @Description: 
	 * @return
	 */
	public String parseHosMedicalTypeToPa(String hosMedicalType) {
		if(StringUtil.isBlank(hosMedicalType)) {
			return "";
		}
		String paMedicalType = null;
		if("0".equals(hosMedicalType)) {
			//普通门诊
			paMedicalType = "11";
		}else if("1".equals(hosMedicalType)) {
			//生育门诊
			paMedicalType = "";
		}else if("2".equals(hosMedicalType)) {
			//住院
			paMedicalType = "21";
		}else if("3".equals(hosMedicalType)) {
			//生育住院
			paMedicalType = "";
		}else if("4".equals(hosMedicalType)) {
			//急诊
			paMedicalType = "12";
		}else if("5".equals(hosMedicalType)) {
			//特病门诊
			paMedicalType = "13";
		}else if("6".equals(hosMedicalType)) {
			//特需门诊
			paMedicalType = "15";
		}else if("7".equals(hosMedicalType)) {
			//专家门诊
			paMedicalType = "";
		}else if("8".equals(hosMedicalType)) {
			//体检
			paMedicalType = "";
		}else if("9".equals(hosMedicalType)) {
			//预防接种
			paMedicalType = "";
		}else if("10".equals(hosMedicalType)) {
			// 其他
			paMedicalType = "";
		}
		return paMedicalType;
	}
	/**
	 * 将平安就诊方式转换为医院的就诊方式
	 * 医院：0 普通门诊 1 生育门诊 2 住院 3 生育住院 4 急诊 5 特病门诊 6 特需门诊 7 专家门诊 8 体检 9 预防接种 10 其他
	 * 平安：11 普通门诊 12 急诊 13 门诊特病 14 门诊慢病 15 特需门诊 21 普通住院 22 特殊病种住院 23 转外诊治住院 24 特需住院 31 家庭病床 41 药店购药
	 * @Description: 
	 * @param medicalType
	 * @return
	 */
	public String parsePingAnMedicalTypeToHos(String medicalType) {
		if(StringUtil.isBlank(medicalType)) {
			return "";
		}
		String HosMedicalType = null;
		if("11".equals(medicalType)) {
			//普通门诊
			HosMedicalType = "0";
		}else if("12".equals(medicalType)) {
			//急诊
			HosMedicalType = "4";
		}else if("13".equals(medicalType)) {
			//门诊特病
			HosMedicalType = "5";
		}else if("14".equals(medicalType)) {
			//门诊慢病
			HosMedicalType = "10";
		}else if("15".equals(medicalType)) {
			//特需门诊
			HosMedicalType = "6";
		}else if("21".equals(medicalType)) {
			//普通住院
			HosMedicalType = "2";
		}else if("22".equals(medicalType)) {
			//特殊病种住院 
			HosMedicalType = "10";
		}else if("23".equals(medicalType)) {
			//转外诊治住院
			HosMedicalType = "10";
		}else if("24".equals(medicalType)) {
			// 特需住院
			HosMedicalType = "10";
		}else if("31".equals(medicalType)) {
			//  家庭病床
			HosMedicalType = "10";
		}else if("41".equals(medicalType)) {
			//   药店购药
			HosMedicalType = "10";
		}
		return HosMedicalType;
	}
	/**
	 * 医院：1 出院诊断    2 门诊诊断  3 入院初步诊断   4 术前诊断  5 术后诊断  6 尸检诊断  7	放射诊断  8	超声诊断  9	病理诊断  10 并发症诊断  11 院内感染诊断  12 主要诊断  13 次要诊断 99 其他
	 * 平安：0、主要诊断，1、次要诊断 ,2、次要诊断 
	 * @Description: 
	 * @param diagnosisType
	 * @return
	 */
	public String parseHosDiagnosisTypeToPa(String diagnosisType) {
		String paDiagType = null;
		if("12".equals(diagnosisType)) {
			paDiagType = "0";
		}else if("13".equals(diagnosisType)) {
			paDiagType = "1";
		}
		return paDiagType;
	}
	
	/**
	 * 医院：汉族  蒙古族  回族  藏族  维吾尔族  苗族  彝族  壮族  布依族  朝鲜族  满族  侗族  瑶族  白族  土家族  哈尼族  哈萨克族  
	 * 傣族  黎族  傈僳族  佤族  畲族  高山族  拉祜族  水族  东乡族  纳西族  景颇族  柯尔克孜族  土族  达翰尔族  仫佬族  羌族  布朗族  
	 * 撒拉族  毛南族  仡佬族  锡伯族  阿昌族  普米族  塔吉克族  怒族  乌孜别克族  俄罗斯族  鄂温克族  德昂族  保安族  裕固族  京族  
	 * 塔塔尔族  独龙族  鄂伦春族  赫哲族  门巴族  珞巴族  基诺族  外国血统  其它 
	 * 
	 * 平安：1 汉族 2 蒙古族 3 回族 4 藏族  5 维吾尔族  6 苗族 7 彝族 8 壮族 9 布依族 10 朝鲜族 11 满族 12 侗族 13 瑶族 14 白族 
	 * 15 土家族  16 哈尼族 17 哈萨克族 18 傣族  19 黎族20 傈傈族  21 佤族 22 畲族 23 高山族 24 拉祜族 25 水族  26 东乡族
	 * 27 纳西族 28 景颇族 29 柯尔克孜族 30 土族 31 达翰尔族 32 仫佬族  33 羌族 34 布朗族 35 撒拉族 36 毛南族  37 仡佬族 
	 *  38 锡伯族 39 阿昌族 40 普米族  41 塔吉克族 42 怒族  43 乌孜别克族 44 俄罗斯族 45 鄂温克族 46 德昂族 47 保安族  
	 *  48 裕固族  49 京族 50 塔塔尔族 51 独龙族  52 鄂伦春族  53 赫哲族  54 门巴族
	 *  55 珞巴族 56 基诺族  90 外籍人士 99 其他
	 * @Description: 
	 * @return
	 */
	public String parseHosRaceToPa(String race) {
		if(StringUtil.isBlank(race)) {
			return "";
		}
		return raceMap.get(race);
	}
	
	/**
	 * 元转分,传入金额为空时，返回空串
	 * @Description: 
	 * @param fee
	 * @return
	 */
	public String parseYuanToFen(String fee) {
		if(StringUtil.isBlank(fee)) {
			return "";
		}
		return ArithUtil.mul(fee, "100");
	}
	/**
	 * 分转元,传入金额为空时，返回空串
	 * @Description: 
	 * @param fee
	 * @return
	 */
	public String parseFenToYuan(String fee) {
		if(StringUtil.isBlank(fee)) {
			return "";
		}
		return ArithUtil.div(fee, "100",2);
	}
	
	/**
	 * 日期转换，传入日期为空时，返回空串
	 * @Description: 
	 * @param date
	 * @param sourceFormat
	 * @param targetFormat
	 * @return
	 * @throws ParseException
	 */
	public String formatDate(String date,String sourceFormat,String targetFormat) throws ParseException {
		if(StringUtil.isBlank(date)) {
			return "";
		}
		return DateOper.formatDate(date, sourceFormat, targetFormat);
	}
	
	
	/**
	 * 医院目录类别转换为平安目录类别
	 * Hos：01  药品  02  诊疗  03  医用耗材  04  其他 
	 * PA:1	药品  2 诊疗项目  3 服务设施  4 医用材料

	 * @Description: 
	 * @return
	 */
	public String parseHosCatToPa(String hosItemCat) {
		if(StringUtil.isBlank(hosItemCat)) {
			return "";
		}
		String paListCat = null;
		if("01".equals(hosItemCat)) {
			//药品
			paListCat = "1";
		}else if("02".equals(hosItemCat)) {
			//诊疗
			paListCat = "2";
		}else if("03".equals(hosItemCat)) {
			// 医用耗材
			paListCat = "4";
		}else if("04".equals(hosItemCat)) {
			//其他 
			paListCat = "";
		}else {
			paListCat = "";
		}
		return paListCat;
	}
	/**
	 * 医院性别转换为平安性别
	 * HOS：0 未知的性别    1 男性   2  女性   9 未说明的性别
	 * PA:1 男性   2  女性   9 未说明的性别
	 * @Description: 
	 * @param hosGender
	 * @return
	 */
	public String parseHosGenderToPa(String hosGender) {
		if(StringUtil.isBlank(hosGender)) {
			return "";
		}
		if("0".equals(hosGender)) {
			return "9";
		}else {
			return hosGender;
		}
	}
	/**
	 * 医院医疗项目类别转换为平安项目类别
	 * Hos:
	 * PA:01 西药       02	中成药       03 中草药       04	检查费       05	CT       06	核磁       07	B超       08	治疗费       09	化验费       10	手术费       11	输氧费       12	放射费       13	输血费       14	注射费       16	化疗费       17	床位费       18	材料费       19	护理费       20	诊疗费       23	医事服务费       24	分娩       25	人工器官       26	血透       27	挂号费       30	麻醉费       31	理疗费       32	监护费       33	观察费       40	体检费       60	服务费       61	伙食费       62	抢救费       99	其他费用
	 * @Description: 
	 * @param hosItemCode
	 * @return
	 */
	public String parseHosItemCodeToPa(String hosItemCode) {
		if(StringUtil.isBlank(hosItemCode)) {
			return "";
		}
		String itemCode = null;
		if("".equals(hosItemCode)) {
			//西药
			itemCode = "01";
		}else if("".equals(hosItemCode)) {
			// 02	中成药
			itemCode = "02";
		}else if("".equals(hosItemCode)) {
			//03 中草药
			itemCode = "03";
		}else if("".equals(hosItemCode)) {
			//04 检查费
			itemCode = "04";
		}else if("".equals(hosItemCode)) {
			//CT
			itemCode = "05";
		}else if("".equals(hosItemCode)) {
			//06 核磁
			itemCode = "06";
		}else if("".equals(hosItemCode)) {
			// 07 B超       
			itemCode = "07";
		}else if("".equals(hosItemCode)) {
			//08 治疗费       
			itemCode = "08";
		}else if("".equals(hosItemCode)) {
			//09 化验费       
			itemCode = "09";
		}else if("".equals(hosItemCode)) {
			//10 手术费       
			itemCode = "10";
		}else if("".equals(hosItemCode)) {
			//11 输氧费       
			itemCode = "11";
		}else if("".equals(hosItemCode)) {
			//12 放射费
			itemCode = "12";
		}else if("".equals(hosItemCode)) {
			//13 输血费       
			itemCode = "13";
		}else if("".equals(hosItemCode)) {
			//14 注射费       
			itemCode = "14";
		}else if("".equals(hosItemCode)) {
			//16 化疗费       
			itemCode = "16";
		}else if("".equals(hosItemCode)) {
			//17 床位费      
			itemCode = "17";
		}else if("".equals(hosItemCode)) {
			// 18 材料费      
			itemCode = "18";
		}else if("".equals(hosItemCode)) {
			// 19 护理费     
			itemCode = "19";
		}else if("".equals(hosItemCode)) {
			//20 诊疗费       
			itemCode = "20";
		}else if("".equals(hosItemCode)) {
			//23医事服务费 
			itemCode = "23";
		}else if("".equals(hosItemCode)) {
			//24分娩       
			itemCode = "24";
		}else if("".equals(hosItemCode)) {
			//25人工器官
			itemCode = "25";
		}else if("".equals(hosItemCode)) {
			//26	血透       
			itemCode = "26";
		}else if("".equals(hosItemCode)) {
			//27	挂号费     
			itemCode = "27";
		}else if("".equals(hosItemCode)) {
			//  30	麻醉费       
			itemCode = "30";
		}else if("".equals(hosItemCode)) {
			//31	理疗费      
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			// 32	监护费       
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			//33	观察费       
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			//40	体检费       
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			//60	服务费       
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			//61	伙食费       
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			//62	抢救费       
			itemCode = "";
		}else if("".equals(hosItemCode)) {
			//99	其他费用
			itemCode = "";
		}
		return itemCode;
	}
}
