package com.kasite.core.common.log;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.kasite.core.common.config.KasiteConfig;

/**
 * @author Administrator
 *
 */
public class NetworkUtil {
	public static void getLocalIP2() {
		Enumeration<NetworkInterface> allNetInterfaces; // 定义网络接口枚举类
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces(); // 获得网络接口

			InetAddress ip = null; // 声明一个InetAddress类型ip地址
			while (allNetInterfaces.hasMoreElements()) // 遍历所有的网络接口
			{
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				KasiteConfig.print(netInterface.getName()); // 打印网端名字
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses(); // 同样再定义网络地址枚举类
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (ip != null && (ip instanceof Inet4Address)) // InetAddress类包括Inet4Address和Inet6Address
					{
						KasiteConfig.print("IP = " + ip.getHostAddress());
					}
				}
				KasiteConfig.print("end----------:" + netInterface.getName()); // 打印网端名字
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String localIp = null;

	public static void setLocalIp(String ip) {
		if (ip == null || (ip = ip.trim()).isEmpty()) {
			return;
		}
		try {
			InetAddress ia = InetAddress.getByName(ip);
			if (Inet4Address.class.isInstance(ia)) {
				localIp = ip;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isValid(InetAddress ia) {
		if (ia instanceof Inet6Address) {
			return false;
		}
		// 10、172.16、192.168开头的，才被认为是本机的ip
		if (ia.isSiteLocalAddress()) {
			return true;
		}
		String ip = ia.getHostAddress();
		return ip.startsWith("172") || ip.startsWith("192");
	}

	public static String getLocalIP() {
		if (localIp != null) {
			return localIp;
		}
		String ip = KasiteConfig.getIp();
		if (ip != null && ip.length() > 0) {
			localIp = ip;
			return localIp;
		}
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (!isUp(ni)) {
					continue;
				}
				Enumeration<?> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = (InetAddress) e2.nextElement();
					if (isValid(ia)) {
						localIp = ia.getHostAddress();
						return localIp;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			localIp = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "0.0.0.0";
		}
		return localIp;
	}

	public static List<String> getLocalIPList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (!isUp(ni)) {
					continue;
				}
				Enumeration<?> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = (InetAddress) e2.nextElement();
					if (isValid(ia)) {
						ipList.add(ia.getHostAddress());
					}

				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipList;
	}

	/**
	 * 网卡是否有效
	 * 
	 * @param ni
	 * @return
	 * @throws SocketException
	 */
	private static boolean isUp(NetworkInterface ni) throws SocketException {
		return ni.getName().startsWith("eth") && (!ni.isVirtual()) && ni.isUp();
	}
}
