package com.kasite.core.serviceinterface.module.his;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.common.cache.dao.ISysCacheMapper;
import com.kasite.core.serviceinterface.common.cache.dbo.SysCache;
import com.kasite.core.serviceinterface.module.his.resp.HisLockOrder;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryExamItemList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryNumbers;

import tk.mybatis.mapper.entity.Example;
/**
 * 调用HIS相关接口缓存，如果后续系统要做集群部署这个缓存部分要改成redis 或者数据库的方式共享
 * @author daiyanshui
 *
 */
public class CallHisCacheMap {
	private final static Object object = new Object();

	public final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_HIS);
	private static CallHisCacheMap install;
	/**
	 * default expiry time 30m
	 * 号源缓存 20分钟，10分钟内没有挂号缓存都作废  TODO 这个要看医院的锁号是否有缓存时间。如果没有则认为是支付时间，指定时间内完成支付不然就作废。
	 */
	private long EXPIRY = 1000 * 60 * 20;
	private static long EXTIME_ARRAY = 0;
	static {
		EXTIME_ARRAY = System.currentTimeMillis();
	}
	
	//排版缓存 
	private ExpiryMap<String, HisQueryClinicSchedule> arrayMap = new ExpiryMap<>();
	//号源缓存
	private ExpiryMap<String, HisQueryNumbers> numberMap = new ExpiryMap<>();
	//锁号缓存
	private ExpiryMap<String, HisLockOrder> lockMap = new ExpiryMap<>();
	//医技预约缓存
	private ExpiryMap<String, HisQueryExamItemList> examItemMap = new ExpiryMap<>();
		
	//后续有医院是直接查询his接口的时候扩展
//	private Map<String, HisQueryClinicDept> deptMap = new HashMap<>();
//	
//	private Map<String, HisQueryClinicDoctor> doctorMap = new HashMap<>();
//	
	private CallHisCacheMap() {
		
	}
	
	public static CallHisCacheMap get() {
		if(null == install) {
			synchronized (object) {
				install = new CallHisCacheMap();
			}
		}
		return install;
	}
	
	public void setLock(String orderId,HisLockOrder order) {
		lockMap.put(orderId, order,EXPIRY);
	}
	
	public HisLockOrder getLock(String orderId) {
		return lockMap.get(orderId);
	}

	
	public ISysCacheMapper getCacheMapper() {
		return SpringContextUtil.getBean(ISysCacheMapper.class);
	}

	/**
	 * 查询HIS接口的时候保存本地缓存对象
	 * @param arrayKey 本地每个排班每次查询唯一
	 * @param array 排班结果集对象
	 */
	public void setArray(String arrayKey,HisQueryClinicSchedule array) {
		//先判断缓存里有没有。如果没有 则从数据库里查询 如果还是没有则 新增／如果有则保存
		if(null == array) {
			return ;
		}
		HisQueryClinicSchedule obj = arrayMap.get(arrayKey);
		boolean isCache = false;
		if(null != obj) {
			//判断是否需要更新缓存
			String sid = obj.getScheduleId();
			Integer isHald = obj.getIsHalt();
			Integer regFee = obj.getRegFee();
			Integer othreFee = obj.getOtherFee();
			Integer treatFee = obj.getTreatFee();
			String key1 = obj.getDeptCode() +","+ obj.getDoctorCode() +"," +obj.getRegDate() +","+sid+","+isHald+","+regFee+","+othreFee+","+treatFee;
			String sid2 = array.getScheduleId();
			Integer isHald2 = array.getIsHalt();
			Integer regFee2 = array.getRegFee();
			Integer othreFee2 = array.getOtherFee();
			Integer treatFee2 = array.getTreatFee();
			String key2 = array.getDeptCode() +","+ array.getDoctorCode() +"," +array.getRegDate() +","+sid2+","+isHald2+","+regFee2+","+othreFee2+","+treatFee2;
			if( key1.equals(key2) ) {
				isCache = true;
			}
		}
//		排班缓存30分钟
		long EXPIRY = 1000 * 60 * 30;
		if(!isCache) {
			try {
				//判断是否有超过6个小时 如果超过6小时清理一次数据库缓存的数据
				long t = System.currentTimeMillis();
				if(t - EXTIME_ARRAY > 1000 * 60 * 60 * 6) {
					//系统启动 6小时执行一次缓存清理
					try {
						Example example = new Example(SysCache.class);
						example.createCriteria().andCondition("createtime < date_add(now(), interval -1 day)");
						getCacheMapper().deleteByExample(example);
					}catch (Exception e) {
						e.printStackTrace();
					}
					EXTIME_ARRAY = t;
				}
				SysCache record = new SysCache();
				record.setSid(arrayKey);
				String value = JSONObject.toJSON(array).toString();
				record.setValue(value);
				record.setInvalidTime(new Timestamp(DateOper.getNowDateTime().getTime()+EXPIRY));
				Example example = new Example(SysCache.class);
				example.createCriteria().andEqualTo("sid", record.getSid());
				int size = getCacheMapper().updateByExampleSelective(record, example);
				if(size <= 0) {
					getCacheMapper().insert(record);
				}
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}
		}
		arrayMap.put(arrayKey, array,EXPIRY);
	}
	
	/**
	 * 查询HIS接口的时候保存本地缓存对象
	 * @param arrayKey 本地保证唯一
	 * @param array 排班结果集对象
	 */
	public HisQueryClinicSchedule getArray(String arrayKey) {
		HisQueryClinicSchedule hcs = arrayMap.get(arrayKey);
		if(null == hcs) {
			try {
				SysCache record = new SysCache();
				record.setSid(arrayKey);
				Example example = new Example(SysCache.class);
				example.createCriteria().andEqualTo("sid", record.getSid());
				SysCache cache = getCacheMapper().selectOneByExample(example);
				if(null != cache) {
					String v = cache.getValue();
					hcs = JSONObject.toJavaObject(JSONObject.parseObject(v), HisQueryClinicSchedule.class);
					arrayMap.put(arrayKey, hcs);
				}
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}
		}
		return arrayMap.get(arrayKey);
	}
	/**
	 * 查询HIS接口的时候保存本地缓存对象
	 * @param numberSn 本地每个排班每次查询唯一
	 * @param array 号源结果集对象
	 */
	public void setNumber(String numberSn,HisQueryNumbers number) {
		numberMap.put(numberSn, number,EXPIRY);
	}
	
	/**
	 * 查询HIS接口的时候保存本地缓存对象
	 * @param numberSn 本地保证唯一
	 * @param array 号源结果集对象
	 */
	public HisQueryNumbers getNumber(String numberSn) {
		return numberMap.get(numberSn);
	}
	/**
	 * 查询医技预约项目时候保存本地缓存对象
	 * @param numberSn 本地每个排班每次查询唯一
	 * @param array 号源结果集对象
	 */
	public void setExamItem(String key,HisQueryExamItemList obj) {
		examItemMap.put(key, obj,EXPIRY);
	}
	
	/**
	 * 查询HIS接口的时候保存本地缓存对象
	 * @param numberSn 本地保证唯一
	 * @param array 号源结果集对象
	 */
	public HisQueryExamItemList getExamItem(String key) {
		return examItemMap.get(key);
	}
}
