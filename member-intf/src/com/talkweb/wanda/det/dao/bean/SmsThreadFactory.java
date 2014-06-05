package com.talkweb.wanda.det.dao.bean;

public class SmsThreadFactory {
	static AbstractSMSControl.SendMMSThread smm;

	private SmsThreadFactory() {
	}

	public static AbstractSMSControl.SendMMSThread getSmsThreadInstance(
			AbstractSMSControl asm) {

		if (smm == null) {
			System.out.println("creating new SendMMSThread()");
			smm = asm.new SendMMSThread();
		}
		System.out.println("geting  SendMMSThread() ");

		return smm;
	}
}
