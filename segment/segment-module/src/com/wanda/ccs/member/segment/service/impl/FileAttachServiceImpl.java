package com.wanda.ccs.member.segment.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.member.segment.service.FileAttachService;
import com.wanda.ccs.member.segment.vo.FileAttachVo;

import com.google.code.pathlet.config.anno.InstanceIn;

public class FileAttachServiceImpl implements FileAttachService {
	
	private static Log log = LogFactory.getLog(FileAttachServiceImpl.class);
	
	private ExtJdbcTemplate jdbcTemplate;
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;

	private ExtJdbcTemplate getJdbcTemplate() {
		if (this.jdbcTemplate == null) {
			this.jdbcTemplate = new ExtJdbcTemplate(dataSource);
		}

		return this.jdbcTemplate;
	}

	public FileAttachVo getFile(Long fileAttachId) {
		FileAttachVo vo = (FileAttachVo) getJdbcTemplate().queryForObject(
				"select FILE_ATTACH_ID, REF_OBJECT_ID, REF_OBJECT_TYPE, FILE_NAME, FILE_SIZE, FILE_DESC, CONTENT_TYPE" +
				" from T_FILE_ATTACH where FILE_ATTACH_ID=?",
				new Object[] { fileAttachId },
				new EntityRowMapper<FileAttachVo>(FileAttachVo.class));
		return vo;
	}

	public List<FileAttachVo> getFiles(Long refObjectId, String refObjectType) {
		List<FileAttachVo> files = getJdbcTemplate().query(
				"select FILE_ATTACH_ID, REF_OBJECT_ID, REF_OBJECT_TYPE, FILE_NAME, FILE_SIZE, FILE_DESC, CONTENT_TYPE" +
				" from T_FILE_ATTACH where REF_OBJECT_ID=? and REF_OBJECT_TYPE=?",
				new Object[] { refObjectId, refObjectType },
				new EntityRowMapper<FileAttachVo>(FileAttachVo.class));
		return files;
	}
	
	public void downloadFile(final HttpServletRequest request, final HttpServletResponse response, final Long fileAttachId, final String charset)
		throws IOException {

		final FileAttachVo fileInfo = getFile(fileAttachId); 
		
		String sql = " SELECT FILE_DATA FROM T_FILE_ATTACH WHERE FILE_ATTACH_ID =" + fileAttachId;
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			
			public void processRow(ResultSet rs) throws SQLException {
				
				response.setContentType(fileInfo.getContentType());
				response.setCharacterEncoding(charset);
				
				
				OutputStream out = null;
				InputStream in = null;
				try {
					setDownloadFileName(request, response, fileInfo.getFileName(), charset); //设置下载时提示保存的文件名
					
					Blob blob = rs.getBlob("FILE_DATA");
					in = blob.getBinaryStream();
					out = response.getOutputStream();

					byte[] buffer = new byte[4096];
					int length;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
				} 
				catch (IOException e) {
					throw new SQLException(e);
				}
				finally {
					if(in != null) { try { in.close(); } catch(IOException ioe){ log.error(ioe.getMessage(), ioe); }}
					if(out != null) { try {out.flush(); } catch(IOException ioe){log.error(ioe.getMessage(), ioe); }}
				}

			}
			
		});

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
}
