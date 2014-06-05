/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wanda.ccs.member.ap2in;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
				

public class TestFilter implements Filter {
	

    
    public void init(FilterConfig filterConfig) throws ServletException {
    	
    }


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest httpReq = (HttpServletRequest)req;
    	
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + httpReq.getMethod());
    	System.out.println("~~~URL:" + httpReq.getRequestURL().toString());
    	System.out.println("~~~URI:" + httpReq.getRequestURI());
    	System.out.println("~~~QueryString:" + httpReq.getQueryString());
    	if(httpReq.getQueryString() != null) {
    		System.out.println("~~~QueryString:" + httpReq.getQueryString());
    	}
    	if(httpReq.getContentType() != null) {
    		System.out.println("~~~ContentType:" + httpReq.getContentType());
    	}
    	System.out.println("~~~CharacterEncoding:" + httpReq.getCharacterEncoding());
    	Enumeration<String> headerNames = httpReq.getHeaderNames();
    	while(headerNames.hasMoreElements()) {
    		String headerName = (String)headerNames.nextElement();
    		if(headerName != null && headerName.length() > 0) {
    			Enumeration<String> headers = httpReq.getHeaders(headerName);
    			while(headers.hasMoreElements()) {
    				String headerValue = headers.nextElement();
    				System.out.println("~~~Header[" + headerName + ":" + headerValue + "]");
    			}
    		}
    	}

    	System.out.println("~~~~~SessionID[" + httpReq.getSession().getId() + "]");
//    	ServletInputStream is = httpReq.getInputStream();
//    	if(is != null) {
//	    	String body = IOUtils.toString(is, httpReq.getCharacterEncoding());
//	    	if(body != null && body.length() > 0) {
//	    		System.out.println("~~~Body:" + body);
//	    	}
//    	}
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~END");
    	
    	chain.doFilter(req, res);
    }


	public void destroy() {
		
	}

 
}
