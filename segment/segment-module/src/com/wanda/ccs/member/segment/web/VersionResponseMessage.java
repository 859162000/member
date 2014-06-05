package com.wanda.ccs.member.segment.web;

import com.google.code.pathlet.web.widget.ResponseLevel;
import com.google.code.pathlet.web.widget.ResponseMessage;

public class VersionResponseMessage extends ResponseMessage {
	
	private Long version;
	
	public VersionResponseMessage(ResponseLevel level, String message, Long version) {
		super(level, message);
		this.version = version;
	}

	public Long getVersion() {
		return version;
	}
	
}
