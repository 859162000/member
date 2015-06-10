简介
=======================
	此项目为会员系统的项目组的总目录。


包含内容
=======================
	/database      做数据库程序，目前是会员相关的etl存储过程程序。
	/job           包还多个后台调度任务项目组
	/member-data   member-web项目依赖，所有member-web项目中的数据操作放在此工程。
	/member-intf   会员接口应用项目。
	/member-web   会员管理web应用项目。
	/member-etl    会员Kettle ETL工程。
	/release   发布部署的脚本，包含公共的打包部署脚本和每个发布版的部署资源和说明文档
	/segment   客群管理项目组


编译说明
=======================
	1.如何批量更改各module版本。
	mvn versions:set -DnewVersion=1.3.2.2

	2.提供版本的提交方式。
	mvn versions:commit

	3.部署发布到maven库服务器上
	mvn clean deploy -Dmaven.test.skip=true -Prelease
	其中 -Dmaven.test.skip=true 是为了跳过测试，因为有的项目需要特殊的测试环境。
	-Prelease 是为了指定一些项目为转为部署打包（配置切换为发布专用）

	4.或打包放在本地
	mvn clean package -Dmaven.test.skip=true -Prelease

发版说明
=======================
   1.第一步取得安装介质
       在UAT环境（10.199.201.104）
       进入目录 /home/appuser/deploy

       手工上传安装介质到
       
       从 member-intf\target\member-intf-1.6.0.war
          member-web\target\member-web-1.6.0.war
	  segment\segment-main\target\segment-main-1.6.0.war
	  segment\segment-module\target\segment-module-1.6.0.jar
	  job\member-jobhub\target\member-jobhub-1.6.0.war
	  job\member-jobs\target\member-jobs-1.6.0.jar
       
       到目录下：/home/appuser/deploy/archives/
       保持文件名不变。注意其中的版本号为变量。
    2. 第二步部署
    停止tomcat服务器
    /opt/tomcat8080/bin/shutdown.sh
    执行自动拷贝相应版本的安装介质
    ./deploy_all-test.sh 1.6.0



其他maven命令说明： 
=======================
	2.自动更改子模块的版本，此命令目前不需要使用。
	mvn -N versions:update-child-modules

	3.提供版本的提交后的回退。
	mvn versions:revert 

	5.如果想让maven强制下载最新的包，需要加入-U参数。
	如：mvn clean install -U



发布环境的目录结构（测试和生产环境）
=======================
tomcat/modules/
    member-jobs      会员批处理任务模块，客群计算，批量导出，会员积分调整
    segment-module   会员客户群管理和营销活动管理的界面部分后台处理

/home/appuser/deploy  发布脚本和部署介质
