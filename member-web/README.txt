1. 加入4个工程的引用，分别考入了jar到WEB-INF/lib目录下。
   jobhub-client-1.3.2.jar 代替了原来的 ccs-job-schedule.jar 
   		用于远程调用jobhub中的job。
   ccsmbrpoint.jar 目前保留的是1.3.1版，需要更换。 
   		算积分的模块
   member-data.jar 
 		数据模块,目前采用eclipse的Web Deployment Assembly作为开发时的关联。
   segment-module-1.3.2.jar
   		客群计算模块
   		
2. 删除CXF支持的包，因为之前调用job-scheduler工程是使用，现在改jobhub-client-1.3.2.jar为http直接调用。
	CXF-library.txt
	ccs-job-schedule.jar
	cxf-api-2.6.0.jar
	cxf-rt-bindings-xml-2.6.0.jar
	cxf-rt-core-2.6.0.jar
	cxf-rt-frontend-jaxrs-2.6.0.jar
	cxf-rt-rs-extension-providers-2.6.0.jar
	cxf-rt-rs-extension-search-2.6.0.jar
	cxf-rt-transports-http-2.6.0.jar
	geronimo-javamail_1.4_spec-1.7.1.jar
	jaxb-impl-2.1.13.jar
	jsr311-api-1.1.1.jar
	stax2-api-3.1.1.jar
	woodstox-core-asl-4.1.2.jar
	wsdl4j-1.6.2.jar
	xmlschema-core-2.0.2.jar
  