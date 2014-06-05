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
	/member-job-service  会员积分运算的公共服务。
	/release   发布部署的脚本，包含公共的打包部署脚本和每个发布版的部署资源和说明文档
	/segment   客群管理项目组


编译说明
=======================
	1.如何批量更改各module版本。
	mvn versions:set -DnewVersion=1.3.2.2

	2.自动更改子模块的版本，此命令目前不需要使用。
	mvn -N versions:update-child-modules

	3.提供版本的回退和提交方式。
	mvn versions:revert 
	mvn versions:commit

	4.部署发布到服务器上
	mvn clean deploy -Dmaven.test.skip=true -Prelease
	其中 -Dmaven.test.skip=true 是为了跳过测试，因为有的项目需要特殊的测试环境。
	-Prelease 是为了指定一些项目为转为部署打包（配置切换为发布专用）

	5.如果想让maven强制下载最新的包，需要加入-U参数。
	如：mvn clean install -U