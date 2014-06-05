package com.talkweb.wanda.det.dao.bean;

/*
 * Copyright (c) 2007-2009 talkWeb All rights reserved.
 * History: 
 * Date          Modified_By    Reason    Description 
 * 2010-12-28    xiaowenbin       ����      ����
 */


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * getPK,�����ݿ�ʹ�õ�һ��String��Ψһ���� 20λ��ͬһ������99999�������ظ�
 * ���Ƕ������ظ�
 * @author xiaowenbin
 * 
 */
public class BasePK {
/*	private static String[] ls = new String[3000];

	private static int li = 0;

	public synchronized static String getPK() {
		String lo = getpk();
		for (int i = 0; i < 3000; i++) {
			String lt = ls[i];
			if (lt == lo) {
				lo = getPK();
				break;
			}
		}
		ls[li] = lo;
		li++;
		if (li == 3000) {
			li = 0;
		}
		return lo;
	}

	private static String getpk() {
		String a = (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
				.trim();
		String d = (String.valueOf(Math.random())).substring(2, 8).trim();
		return new StringBuffer(a + d).toString();
	}*/
	
	private static String localTime = (new SimpleDateFormat("yyyyMMddHHmm")
			.format(new Date())).trim();

	private static int i = 10000;

	private static String ip = "001";
	static {
		try {
			String ip = OSUtil.getLocalIP();
			String[] ipArray = ip.split("\\.");
			String lastIp = ipArray[3];
			while (lastIp.length() < 3) {
				lastIp += "0";
			}
			BasePK.ip = lastIp;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static String getPK() {
		if (i == 99999) {// һ����������99999��
			try {// ��1����
				Thread.sleep(60*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String a = (new SimpleDateFormat("yyyyMMddHHmm")
				.format(new Date())).trim();
		if (a.equals(localTime)) {
			String d = String.valueOf(++i);
			return new StringBuffer(a + d + ip).toString();
		} else {
			i = 10000;
			localTime = new String(a);
			String d = String.valueOf(i);
			return new StringBuffer(a + d + ip).toString();
		}
	}
}
