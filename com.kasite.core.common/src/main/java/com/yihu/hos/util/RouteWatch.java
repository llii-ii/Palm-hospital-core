package com.yihu.hos.util;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.hos.exception.CommonServiceException;
import com.yihu.hos.service.CommonServiceRetCode;

/**
 * @author Administrator
 * 
 */
public class RouteWatch implements Runnable {
	
	/**
	 * 检索时间
	 */
	private long delay = 60000 * 3;
	/**
	 * 
	 */
	private RouteChange change;

	public RouteWatch(RouteChange change) throws AbsHosException{
		try {
			this.change = change;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonServiceException(CommonServiceRetCode.Common.ERROR_ROUTE_CHANGE,e.getMessage());
		}
	}
	@Override
	public void run() {
		while (true) {
			try{
				this.change.onChange();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
