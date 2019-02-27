package com.kasite.client.crawler.modules.upload.datacloud;

/**
 * 上传至厦门区域数据平台
 * @author daiyanshui
 *
 */
public interface IXMDataCloudService {
//	/**
//	 * 生成需要上传的文件
//	 * 将居民健康档案信息通过标准转换成对方需要的数据格式文件保存到指定目录
//	 * @param fileDir 指定文件目录
//	 * @throws Exception 
//	 */
//	public void mkfile(String fileDir) throws Exception;
////	
//	/**
//	 * 将文件上传到居民健康档案云平台
//	 * @param fileDir
//	 */
//	public void upload(String fileDir) ;
//	
	/**
	 * 组装要上传的数据
	 * @param dateTime
	 */

	void assemblyData(String startDate, String endDate, String evenNo);
	
	void DealQualityControlPackage(String startDate, String endDate) throws Exception;
	
	void assemblyBasicData();
	
}
