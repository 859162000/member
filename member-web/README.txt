工程简介
==========================
会员webapp工程，提供了会员管理界面中的会员管理、万人迷卡和特殊积分规则功能。
部署在会员应用服务器上的 /member 网站路径下。


编译说明
==========================
工程编译采用了部分的maven编译模式，非标准maven编译打包模式！下面其中的改造点：
1. 其中的jar包大都是实现放置在webapp/WEB-INF/lib目录下的
没用通过maven的dependency功能自动获取，只有其中的member-data.jar是通过maven的dependency插件
的拷贝功能拷贝到webapp/WEB-INF/lib下（每次编译前自动替换webapp/WEB-INF/lib/member-data.jar）。

2.编译的方式是采用maven调用ant进行编译的，非标准的maven编译方式。而打包方式采用的maven标准的war
方式，但在打包前会先调用ant进行编译。

3.编译和打包过程中已配置跳过maven的测试环节。

编译常用命令
=================
自动打包上传到部署服务器 mvn clean deploy
只打包不上传到部署服务器 mvn clean package

开发调试说明
=================
1. 工程使用eclipse IDE，使用标准的dynamic web application工程。
2. 工程文件已建立在svn中，直接使用eclipse的import 》existing projects into workspace。
3. 开发调试时需要使用标准的eclipse server视图中建立tomcat 6.0应用服务器来调试。
   调试时需要配置 Server下的context.xml文件，工程根目录上的context.xml为该配置的参考示例。


进行maven改造的一些要点说明
=================================
此工程为部分的maven工程，其依赖的jar包均为拷贝到WEB/lib

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
  