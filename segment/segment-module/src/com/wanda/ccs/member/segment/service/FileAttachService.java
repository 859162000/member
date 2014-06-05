package com.wanda.ccs.member.segment.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanda.ccs.member.segment.vo.FileAttachVo;

public interface FileAttachService {

	FileAttachVo getFile(Long fileAttachId);

	List<FileAttachVo> getFiles(Long refObjectId, String refObjectType);

	void downloadFile(final HttpServletRequest request,
			final HttpServletResponse response, final Long fileAttachId,
			final String charset) throws IOException;

}