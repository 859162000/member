/**  
 * @Title: MessageSendJob.java
 * @Package com.wanda.ccs.member.segment
 * @Description: 短信定时发送的任务
 * @author 许雷
 * @date 2015年6月1日 上午10:16:25
 * @version V1.0  
 */
package com.wanda.ccs.member.segment;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.member.segment.vo.SegmentMessageVo;

/**
 * @ClassName: MessageSendJob
 * @Description: 短信定时发送的任务
 * @author 许雷
 * @date 2015年6月1日 上午10:16:25
 *
 */
public class SendJobThread extends Thread {

	private static Log log = LogFactory.getLog(SendJobThread.class);

	private SegmentMessageVo messageSendVo;

	private BlockingQueue<String> moibleQue;

	private String content = "";

	public SendJobThread(SegmentMessageVo messageSendVo, String content,
			BlockingQueue<String> moibleQue) {
		this.messageSendVo = messageSendVo;
		this.content = content;
		this.moibleQue = moibleQue;
	}

	/*
	 * (非 Javadoc) <p>Title: run</p> <p>Description: </p>-
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// 获取短信平台代理地址和通道号
		try {
			if (messageSendVo == null || moibleQue == null
					|| moibleQue.size() == 0) {
				log.warn("An Exception here is messageSend or moibleList object is null, so can't send!");
			} else {
				String moible = moibleQue.poll();
				while (moible != null) {
					for (int i = 0; i < 3; i++) {// 失败尝试三次
						String j = MSGSendUtil.sendSMSBySysC(moible,content);
						System.out.println();
						if ("OK".equals(j)) {
							int count = MessageSendJob.count.getAndIncrement();
							log.info("Send Message : 第" + count + "条， 已发送！" );
							break;
						}
					}
					moible = moibleQue.poll();
				}
			}
		} catch (Exception e) {
			log.warn("An Exception here is " + e.getMessage(), e);
			e.printStackTrace();
		}
	}
}
