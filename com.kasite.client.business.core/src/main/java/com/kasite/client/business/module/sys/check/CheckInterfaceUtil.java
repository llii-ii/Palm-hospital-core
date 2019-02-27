package com.kasite.client.business.module.sys.check;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.util.NetStateUtil;

/**
 * 接口状态校验 
 * 通过add 方法 传入 需要监控的接口
 * 
 * 通过CheckInterfaceHandler 实现告警通知 ，接口异常的时候会调用通知 CheckInterfaceHandler的接口
 * @author daiyanshui
 *
 */
public class CheckInterfaceUtil {
	enum CheckUrlType{
		http,
		pingIp
	}
	private static final Logger logger = LoggerFactory.getLogger(CheckInterfaceUtil.class);
	private final static Object obj = new Object();
	private Map<String,InterfaceStatusVo> map = new HashMap<>();
	private static CheckInterfaceUtil install;
	public static CheckInterfaceUtil get() {
		if(null == install) {
			synchronized(obj) {
				install = new CheckInterfaceUtil();
			}
		}
		return install;
	}
	/**
	 * 返回接口状态对象
	 * @param remoteInetAddr
	 * @return
	 */
	public InterfaceStatusVo get(String remoteInetAddr) {
		return map.get(remoteInetAddr);
	}
	
	/**
	 * 加入监听
	 * @param remoteInetAddr
	 */
	public void add(CheckUrlType type, String remoteInetAddr,CheckInterfaceHandler handler) {
		InterfaceStatusVo vo = new InterfaceStatusVo();
		vo.setHandler(handler);
		vo.setStatus(true);	
		vo.setUrlType(type.name());
		vo.setUpdatetime(System.currentTimeMillis());
		vo.setUrl(remoteInetAddr);
		check(vo);
		map.put(remoteInetAddr, vo);
	}
	private CheckInterfaceUtil() {
		init();
	}
	/**
	 * 判断接口状态
	 * @param vo
	 */
	private InterfaceStatusVo check(InterfaceStatusVo vo) {
//		NetStateUtil u = new NetStateUtil();
		vo.setUpdatetime(System.currentTimeMillis());
		String type = vo.getUrlType();
		boolean state = false;
		if(CheckUrlType.http.name().equals(type)) {
			String url = vo.getUrl();
			String tempUrl= url.substring(0, 7);//取出地址前5位
	        if(!tempUrl.contains("http")){//判断传过来的地址中是否有http
	        	url = "http://"+url;
	        }
			state = NetStateUtil.isConnServerByHttp(url);
		}else {
			state = NetStateUtil.isReachable(vo.getUrl());
		}
		vo.setStatus(state);
		vo.notifyHandler();
		return vo;
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private InterfaceStatusVo getRecommendCount(long limit_secounds,Callable<InterfaceStatusVo> task) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        Future<InterfaceStatusVo> future = exec.submit(task);
        InterfaceStatusVo taskResult = null;
        String failReason = null;
        try {
            // 等待计算结果，最长等待timeout秒，timeout秒后中止任务
            taskResult = future.get(limit_secounds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            failReason = "主线程在等待返回结果时被中断！";
        } catch (ExecutionException e) {
            failReason = "主线程等待返回结果，但任务本身抛出异常！";
        } catch (TimeoutException e) {
            failReason = "主线程等待计算结果超时，因此中断任务线程！";
            exec.shutdownNow();
        }
        if(null != failReason) {
        	logger.error(failReason);
        }
        exec.shutdown();
        return taskResult;
    }
	 private class TradeCountThread implements Callable<InterfaceStatusVo> {
		 InterfaceStatusVo vo;
		 public TradeCountThread(InterfaceStatusVo vo) {
			 this.vo = vo;
		 }
		 
		@Override
		public InterfaceStatusVo call() throws Exception {
			return check(vo);
		}
	 }
	private void init() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				long limit_secounds = 30;//超过30秒后关闭线程
				while(true) {
					try {
						for (Map.Entry<String, InterfaceStatusVo> entry : map.entrySet()) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									InterfaceStatusVo vo = entry.getValue();
									TradeCountThread task = new TradeCountThread(vo);
									try {
										getRecommendCount(limit_secounds, task);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						}
					}catch (Exception e) {
						logger.error("检查接口异常线程：",e);
					}
					try {
						//1分钟判断一次接口状态
						Thread.sleep(limit_secounds * 1000 * 2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.setName("CheckInterfaceThread_30_mis");
		t.start();
	}
}
