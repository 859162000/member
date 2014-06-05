package com.wanda.ccs.smsgateway.engine;


public abstract class AbstractEngine implements Runnable, IEngine {
	protected boolean isStart = true;
	
	public boolean isStart() {
		return isStart;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
}
