package com.kasite.core.common.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.kasite.core.common.config.KasiteConfig;

/**
 * 获取IP地址
 * getLocalIP: Windows 和 Linux 操作系统下的ip地址
 * @author Administrator
 * @date 2012-9-5 下午08:01:06
 */
public class GetIP {
	public static void main(String[] args) throws Exception {
		KasiteConfig.print(getLocalIP());
		KasiteConfig.print(getMacAddr());
	}
	public static String getMacAddr() {
		String MacAddr = "";
		String str = "";
		try {
			NetworkInterface NIC = NetworkInterface.getByName("en0");
			byte[] buf = NIC.getHardwareAddress();
			for (int i = 0; i < buf.length; i++) {
				str = str + byteHEX(buf[i]);
			}
			MacAddr = str.toUpperCase();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return MacAddr;
	}
	public static boolean isWindowsOS()
	{
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1)
		{
			isWindowsOS = true;
		}
		return isWindowsOS;
	}
	public static String getLocalIP() throws Exception {
		String ip = "";
		StringBuffer ips = new StringBuffer();
		if(isWindowsOS()){
			InetAddress addr = InetAddress.getLocalHost();
			if(null != addr) {
				ip = addr.getHostAddress().toString();
			}else {
				ip = "该 Windows 未找到IP地址。";
			}
			ips.append(ip);
		}else{
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			if(null != e1) {
				while (e1.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) e1.nextElement();
					if(null != ni) {
						Enumeration<?> e2 = ni.getInetAddresses();
						if(null != e2) {
							while (e2.hasMoreElements()) {
								InetAddress ia = (InetAddress) e2.nextElement();
								if (ia instanceof Inet6Address)
									continue;
								ip = ia.getHostAddress();
								ips.append(ip).append("@");
							}
						}
					}
//					if (!ni.getName().equals("en0")) {
//						continue;
//					} else {
//						Enumeration<?> e2 = ni.getInetAddresses();
//						while (e2.hasMoreElements()) {
//							InetAddress ia = (InetAddress) e2.nextElement();
//							if (ia instanceof Inet6Address)
//								continue;
//							ip = ia.getHostAddress();
//						}
//						break;
//					}
				}
			}
		}
		return ips.toString();
	}

	/* 一个将字节转化为十六进制ASSIC码的函数 */
	public static String byteHEX(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

}
