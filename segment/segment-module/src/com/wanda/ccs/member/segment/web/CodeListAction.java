package com.wanda.ccs.member.segment.web;

import java.util.List;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.member.segment.service.CodeList;
import com.wanda.ccs.member.segment.service.CodeListService;
import com.wanda.ccs.member.segment.vo.CodeEntryVo;

public class CodeListAction {

	@InstanceIn(path="CodeListService")
	private CodeListService service;
	
	private String typeId;
	
	private String sourceId;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public List<CodeEntryVo> getCodeList() throws Exception {
		CodeList cList = service.getCodeList(sourceId, typeId);
		if(cList != null) {
			return cList.getEntrys();
		}
		else {
			return null;
		}
	}
	
	public void flush() throws Exception {
		service.flush(sourceId);
	}
	

}