package com.kasite.client.crawler.config;

/**
 * 
 * @author daiyanshui
 *
 */
public class ElasticsearchClientConfig {

	public static final String ESURL = "es.url";
	
	
	/**
	 * ES 对应定义的表： 所有表的名称定义统一为小写
	 * @author daiyanshui
	 *
	 */
	public enum ElasticIndex{
		/**人口信息学*/
		persion,
		/** 门诊-挂号*/
		clinicregisetrinfo,
		/** 门诊-处方明细*/
		clinicprescriptiondetail,
		/** 门诊-诊断记录*/
		clinicdiagnosis,
		/** 门诊-费用汇总*/
		clinicfreesummary,
		/** 门诊-票据信息*/
		clinicbill,
		/** 门诊-门/急诊病历*/
		cliniccase,
		
		/** 住院-入院记录*/
		hospitalization,
		/** 住院-诊断记录*/
		hospitalizationdiagnosis,
		/** 住院-费用汇总*/
		hospitalizationfreesummary,
		/** 住院-费用清单*/
		hospitalizationperscriptiondetail,
		/** 住院-票据信息*/
		hospitalizationbill,
		/** 住院-出院小结*/
		hospitalizationsummary,
		
		/** 医院疾病字典目录*/
		diseasedict,
		/** 医疗目录字典*/
		medicalcaredict,
		/** 药品目录字典*/
		listcatdict,
		/** 手术目录字典*/
		operationcodedict,
		
		
		/** 手术记录*/
		operationlist,
		/** 检查报告单*/
		reportlist,
		/** 检验报告单*/
		checklist,
		/** 检验项目结果*/
		checkitemlist,
		
		
		pingan,
		
	}
	
}
