package com.wanda.ccs.member.segment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import com.google.code.pathlet.util.FileUtils;
import com.wanda.ccs.member.segment.vo.FileAttachVo;

import javax.mail.internet.MimeUtility;

public class HttpFileServiceUtils {

	
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, FileAttachVo fileInfo, String charset, InputStream fileDataIn) throws IOException {

		response.setContentType(fileInfo.getContentType());
		response.setCharacterEncoding(charset);
		setDownloadFileName(request, response, fileInfo.getFileName(), charset); //设置下载时提示保存的文件名
		
		OutputStream out = null;
		try {
			out = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int length;
			while ((length = fileDataIn.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		} catch (IOException e) {
			throw e;
		}
		finally {
			if(fileDataIn != null) { fileDataIn.close(); }
			if(out != null) { out.flush(); }
		}
	}
	
	private void setDownloadFileName(HttpServletRequest request, HttpServletResponse response, String fileName,
			String charset) throws UnsupportedEncodingException {
		String agent = request.getHeader("USER-AGENT");
		String codedfilename = null;
		if(agent != null) {
			if (agent.indexOf("MSIE") != -1) {
				codedfilename = "filename=\"" + URLEncoder.encode(fileName, charset).replaceAll("\\+", "%20") + "\"";
			} 
			else if (agent != null && agent.indexOf("Mozilla") != -1) {
				codedfilename = "filename=\"" + MimeUtility.encodeText(fileName, charset, "B") + "\"";
			} 
			else if (agent != null && agent.indexOf("opera") != -1)  {
				codedfilename = "filename*=" + charset + "''" + fileName;
			}
			else if (agent != null && agent.indexOf("safari") != -1) {
				codedfilename = "filename=\"" + new String(fileName.getBytes(charset), "ISO8859-1") + "\"";
			}
			else if (agent != null && agent.indexOf("applewebkit") != -1) {
				codedfilename = "filename=\"" + MimeUtility.encodeText(fileName, charset, "B") + "\"";
			} 
		}
		
		if(codedfilename == null) {
			codedfilename = "filename=\"" + fileName + "\"";
		}
		
		response.setHeader("Content-Disposition", "attachment;" + codedfilename);
	}
	
	
	public List<FileAttachVo> uploadFile(HttpServletRequest request, long fileMaxSize, HttpFileStorer storer) throws IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart == false) {
			throw new IOException("HTTP request does not contains multipart content!");
		}
		
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set factory constraints
		factory.setSizeThreshold(10240);
		
		//If ignore setRepository, the the system temporary directory will be set as default.
		//factory.setRepository(yourTempDirectory); 

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(fileMaxSize);

		List<FileAttachVo> uploadedFiles = new ArrayList<FileAttachVo>();
		
		// Parse the request
		try {
			List<FileItem> items = upload.parseRequest(request);
			
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = iter.next();

			    if (item.isFormField() == false) {
			    	//String fieldName = item.getFieldName();
			    	//boolean isInMemory = item.isInMemory();
			    	
			    	//item may return the absolute full path name. we only need the last name
			        String fileName = FileUtils.parseFileName(item.getName());
			        String contentType = item.getContentType();
			        long sizeInBytes = item.getSize();
			        
			        InputStream uploadedStream = item.getInputStream();
			        try {
			        	FileAttachVo fileInfo = new FileAttachVo();
			        	fileInfo.setFileName(fileName);
			        	fileInfo.setContentType(contentType);
			        	fileInfo.setFileSize(sizeInBytes);
			        	
			        	storer.storeFile(fileInfo, uploadedStream);
			        	uploadedFiles.add(fileInfo);
			        } 
			        finally {
			        	uploadedStream.close();
			        }
			    }
			}
		} catch (FileUploadException e) {
			throw new IOException(e);
		}
		
		return uploadedFiles;
	}
}
