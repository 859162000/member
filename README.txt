���
=======================
	����ĿΪ��Աϵͳ����Ŀ�����Ŀ¼��


��������
=======================
	/database      �����ݿ����Ŀǰ�ǻ�Ա��ص�etl�洢���̳���
	/job           ���������̨����������Ŀ��
	/member-data   member-web��Ŀ����������member-web��Ŀ�е����ݲ������ڴ˹��̡�
	/member-intf   ��Ա�ӿ�Ӧ����Ŀ��
	/member-web   ��Ա����webӦ����Ŀ��
	/member-etl    ��ԱKettle ETL���̡�
	/release   ��������Ľű������������Ĵ������ű���ÿ��������Ĳ�����Դ��˵���ĵ�
	/segment   ��Ⱥ������Ŀ��


����˵��
=======================
	1.����������ĸ�module�汾��
	mvn versions:set -DnewVersion=1.3.2.2

	2.�ṩ�汾���ύ��ʽ��
	mvn versions:commit

	3.���𷢲���maven���������
	mvn clean deploy -Dmaven.test.skip=true -Prelease
	���� -Dmaven.test.skip=true ��Ϊ���������ԣ���Ϊ�е���Ŀ��Ҫ����Ĳ��Ի�����
	-Prelease ��Ϊ��ָ��һЩ��ĿΪתΪ�������������л�Ϊ����ר�ã�

	4.�������ڱ���
	mvn clean package -Dmaven.test.skip=true -Prelease

����˵��
=======================
   1.��һ��ȡ�ð�װ����
       ��UAT������10.199.201.104��
       ����Ŀ¼ /home/appuser/deploy

       �ֹ��ϴ���װ���ʵ�
       
       �� member-intf\target\member-intf-1.6.0.war
          member-web\target\member-web-1.6.0.war
	  segment\segment-main\target\segment-main-1.6.0.war
	  segment\segment-module\target\segment-module-1.6.0.jar
	  job\member-jobhub\target\member-jobhub-1.6.0.war
	  job\member-jobs\target\member-jobs-1.6.0.jar
       
       ��Ŀ¼�£�/home/appuser/deploy/archives/
       �����ļ������䡣ע�����еİ汾��Ϊ������
    2. �ڶ�������
    ֹͣtomcat������
    /opt/tomcat8080/bin/shutdown.sh
    ִ���Զ�������Ӧ�汾�İ�װ����
    ./deploy_all-test.sh 1.6.0



����maven����˵���� 
=======================
	2.�Զ�������ģ��İ汾��������Ŀǰ����Ҫʹ�á�
	mvn -N versions:update-child-modules

	3.�ṩ�汾���ύ��Ļ��ˡ�
	mvn versions:revert 

	5.�������mavenǿ���������µİ�����Ҫ����-U������
	�磺mvn clean install -U



����������Ŀ¼�ṹ�����Ժ�����������
=======================
tomcat/modules/
    member-jobs      ��Ա����������ģ�飬��Ⱥ���㣬������������Ա���ֵ���
    segment-module   ��Ա�ͻ�Ⱥ�����Ӫ�������Ľ��沿�ֺ�̨����

/home/appuser/deploy  �����ű��Ͳ������
