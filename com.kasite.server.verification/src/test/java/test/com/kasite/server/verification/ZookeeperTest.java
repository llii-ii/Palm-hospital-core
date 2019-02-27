package test.com.kasite.server.verification;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.kasite.server.verification.module.zk.ZkClientWrapper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
public class ZookeeperTest extends TestCase {
	String zkService = "39.108.219.202:2181,39.108.233.43:2181";
	public ZookeeperTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ZookeeperTest.class);
	}

	public void testReadZookeeper() throws InterruptedException {
		String zkUrl = zkService;
		int connectionTimeout = 20000;
		String rootPath = "/ApiRouteRoot";
		String routeName = "KASITE-CLIENT-BUSINESS.1024727";
		String charsetName = "GBK";
		try {
			ZkClientWrapper wrapper = new ZkClientWrapper(zkUrl,connectionTimeout);
			boolean isExistRoot = wrapper.exists(rootPath);
			if(!isExistRoot) {
				wrapper.createPersistent(rootPath);
				System.out.println("创建根节点");
			}
		//proxyUrl="http://127.0.0.1:8670"
			String path = rootPath + "/" + routeName.trim();
			boolean bool = wrapper.exists(path);
			if (!bool) {
//				// 不存在进行创建
				String xmlContent = "";
				String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
						"<Config>" + 
						"	<Mapping>" + 
						"		<module name=\"api.verificat\" remoteClass=\"api/verificat\"/> " + 
						"		<module name=\"basic.BasicApi\" remoteClass=\"com.kasite.client.business.module.basic.ws.BasicWs\"/> " + 
						"		<module name=\"bat.BatApi\" remoteClass=\"com.kasite.client.business.module.basic.ws.BatWs\"/> " + 
						"		<module name=\"msg.msgApi\" remoteClass=\"com.kasite.client.business.module.core.msg.ws.MsgWs\"/>" + 
						"		<module name=\"order.orderApi\" remoteClass=\"com.kasite.client.business.module.order.ws.OrderWs\"/> " + 
						"		<module name=\"pay.PayWs\" remoteClass=\"com.kasite.client.business.module.pay.ws.PayWs\"/> " + 
						"		<module name=\"queue.QueueWs\" remoteClass=\"com.kasite.client.business.module.queue.ws.QueueWs\"/> " + 
						"		<module name=\"report.ReportWs\" remoteClass=\"com.kasite.client.business.module.report.ws.ReportWs\"/> " + 
						"		<module name=\"rf.rfApi\" remoteClass=\"com.kasite.client.business.module.basic.ws.ReportFormsWs\"/> " + 
						"		<module name=\"smartPay.smartPayWs\" remoteClass=\"com.kasite.client.business.module.pay.ws.SmartPayWs\"/> " + 
						"		<module name=\"survey.SurveyWs\" remoteClass=\"com.kasite.client.business.module.survey.ws.SurveyWs\"/> " + 
						"		<module name=\"yy.yygh\" remoteClass=\"com.kasite.client.business.module.yy.ws.YYWs\"/> " + 
						"	</Mapping>" + 
						"	<Route url=\"127.0.0.1:8994\" proxyUrl=\"http://127.0.0.1:8670\"></Route>" + 
						"</Config>" + 
						"";
				String regex = "\\{0\\}";// {0} 应用名称
				xmlContent = template.replaceAll(regex, routeName);
				String nodeValue = new String(xmlContent.getBytes(), charsetName);
//				wrapper.createPersistent(path, nodeValue);
				System.out.println("创建路由节点。");
			}
			
			
		
			List<String> appRouteList = wrapper.getChildren(rootPath);
			for (String appRouteName : appRouteList) {
				System.out.println(appRouteName);
				String nodePath = rootPath+"/"+appRouteName;
				wrapper.delete(nodePath);
//				System.out.println(nodePath);
//				Object obj = wrapper.readData(nodePath);
//				if (obj != null) {
//					String nodeValue = (String) obj;
//					System.out.println(nodeValue);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 测试获取zookeeper
	 * @throws InterruptedException 
	 */
	public void testZookeeper() throws InterruptedException {
//		String zkUrl = zkService;
//		int connectionTimeout = 20000;
//		String rootPath = "/ApiRouteRoot_ORGA";
//		String routeName = "basic";
//		String charsetName = "GBK";
//		try {
//			ZkClientWrapper wrapper = new ZkClientWrapper(zkUrl,connectionTimeout);
//			boolean isExistRoot = wrapper.exists(rootPath);
//			if(!isExistRoot) {
//				wrapper.createPersistent(rootPath);
//				System.out.println("创建根节点");
//			}
//		
//			String path = rootPath + "/" + routeName.trim();
//			boolean bool = wrapper.exists(path);
//			if (!bool) {
//				// 不存在进行创建
//				String xmlContent = "";
//				String template = "<Config> \n" + 
//						"  <Mapping> \n" + 
//						"    <module name=\"basic.verificat\" remoteClass=\"api/verificat\"></module>  \n" + 
//						"    <module name=\"basic.init\" remoteClass=\"api/init\"></module>  \n" + 
//						"  </Mapping>  \n" + 
//						"  <Route url=\"127.0.0.1:8994\" proxyUrl=\"http://127.0.0.1:8183\"></Route> \n" + 
//						"</Config>";
//				String regex = "\\{0\\}";// {0} 应用名称
//				xmlContent = template.replaceAll(regex, routeName);
//				String nodeValue = new String(xmlContent.getBytes(), charsetName);
//				wrapper.createPersistent(path, nodeValue);
//				System.out.println("创建路由节点。");
//			}
//			
//			
//		
//			List<String> appRouteList = wrapper.getChildren(rootPath);
//			for (String appRouteName : appRouteList) {
//				System.out.println(appRouteName);
//				String nodePath = rootPath+"/"+appRouteName;
////				wrapper.delete(nodePath);
//				System.out.println(nodePath);
//				Object obj = wrapper.readData(nodePath);
//				if (obj != null) {
//					String nodeValue = (String) obj;
//					System.out.println(nodeValue);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	
	
	
	
	
	private static final AtomicInteger URL_SEED = new AtomicInteger(0);
	public void testGetIndex() {
//		for (int i = 0; i < 10000; i++) {
//			int seed = URL_SEED.incrementAndGet();
//			if (seed < 0) {
//				URL_SEED.set(0);
//				seed = 2;
//			}
//			int len = 8;
//			for (int j = 0; j < len; j++) {
//				System.out.println((seed+j)%len);
//			}
//			
//			
//		}
		
	}
	
}
