package com.wanda.ccs.member.segment.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanda.ccs.member.segment.service.CodeList;
import com.wanda.ccs.member.segment.vo.CodeEntryVo;

public class DefaultCodeList implements CodeList {
	
	private String typeId;
	
	private List<CodeEntryVo> entrys;
	
	private Map<String, CodeEntryVo> nameKeyEntrys;
	
	private Map<String, CodeEntryVo> valueKeyEntrys;
	
	public DefaultCodeList(String typeId, List<CodeEntryVo> entrys) {
		this.typeId = typeId;
		this.entrys = entrys;
		this.nameKeyEntrys = new HashMap<String, CodeEntryVo>();
		this.valueKeyEntrys = new HashMap<String, CodeEntryVo>();
		

		for(CodeEntryVo entry : entrys) {
			this.nameKeyEntrys.put(entry.getName(), entry);
			this.valueKeyEntrys.put(entry.getCode(), entry);
		}
	}

	public CodeEntryVo getEntryByName(String name) {
		return this.nameKeyEntrys.get(name);
	}

	public CodeEntryVo getEntryByValue(String code) {
		return this.valueKeyEntrys.get(code);
	}

	public String getEntryNameByValue(String value) {
		CodeEntryVo entry = this.valueKeyEntrys.get(value);
		if(entry != null) {
			return entry.getName();
		}
		else {
			return null;
		}
	}

	public List<CodeEntryVo> getEntrys() {
		return entrys;
	}

	public String getTypeId() {
		return typeId;
	}

}
