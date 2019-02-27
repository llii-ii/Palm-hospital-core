package test.com.kasite.client.crawler;
import java.net.InetAddress;

import com.coreframework.remoting.Server;
import com.coreframework.util.AppConfig;
import com.coreframework.util.PropUtils;

public class TestSysXml {

	public static void main(String[] args) throws Exception {
		// 读取配置
		PropUtils.loadFromSysXml();
		int port = AppConfig.getInteger("Port", 12345);
		String ip = AppConfig.getValue("Ip");
		System.out.println(ip);

		InetAddress address = InetAddress.getLocalHost();
		String localIp = (address.getHostAddress());// 获取IP地址
		if (!ip.equals(localIp)) {
			System.out.println("----------- error -------------");
			System.out.println("--- LocalIp:" + localIp + " ---");
			System.out.println("--- sys.xml-> Ip = " + ip + " ---");
			System.out.println("----------- end -------------");
		}
		System.out.println("发布服务 端口: " + port);
		if (port != 0) {
			Server server = Server.getInstance(port);
			server.start();
		}

	}

}
