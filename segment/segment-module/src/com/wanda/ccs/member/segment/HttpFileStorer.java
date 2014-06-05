package com.wanda.ccs.member.segment;

import java.io.InputStream;

import com.wanda.ccs.member.segment.vo.FileAttachVo;

public interface HttpFileStorer {
	
	void storeFile(FileAttachVo fileInfo, InputStream uploadedStream);

}
