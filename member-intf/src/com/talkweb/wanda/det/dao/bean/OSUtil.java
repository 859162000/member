package com.talkweb.wanda.det.dao.bean;

/*
 * Copyright (c) 2007-2009 talkWeb All rights reserved.
 * History: 
 * Date          Modified_By    Reason    Description 
 * 2010-12-28    xiaowenbin       ����      ����
 */


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 
 * @author xiaowenbin
 * 
 */

public class OSUtil {

	/**
	 * �жϵ�ǰ�����Ƿ�Windows.
	 * 
	 * @return true---��Windows����ϵͳ
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * ��ȡ����IP��ַ�����Զ����Windows����Linux����ϵͳ
	 * 
	 * @return String
	 */
	public static String getLocalIP() {
		String sIP = "";
		InetAddress ip = null;
		try {
			// �����Windows����ϵͳ
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// �����Linux����ϵͳ
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces
							.nextElement();
					// ----------�ض���������Կ�����ni.getName�ж�
					// ��������ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.��ͷ�Ķ���lookback��ַ
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}

	// ���������Ҫ�������Բ�˵�����
	public static void main(String[] args) {
		String serverIP = OSUtil.getLocalIP();
		System.out.println("serverIP:::" + serverIP);
	}
}
