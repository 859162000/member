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
	/member-job-service  ��Ա��������Ĺ�������
	/release   ��������Ľű������������Ĵ������ű���ÿ��������Ĳ�����Դ��˵���ĵ�
	/segment   ��Ⱥ������Ŀ��


����˵��
=======================
	1.����������ĸ�module�汾��
	mvn versions:set -DnewVersion=1.3.2.2

	2.�Զ�������ģ��İ汾��������Ŀǰ����Ҫʹ�á�
	mvn -N versions:update-child-modules

	3.�ṩ�汾�Ļ��˺��ύ��ʽ��
	mvn versions:revert 
	mvn versions:commit

	4.���𷢲�����������
	mvn clean deploy -Dmaven.test.skip=true -Prelease
	���� -Dmaven.test.skip=true ��Ϊ���������ԣ���Ϊ�е���Ŀ��Ҫ����Ĳ��Ի�����
	-Prelease ��Ϊ��ָ��һЩ��ĿΪתΪ�������������л�Ϊ����ר�ã�

	5.�������mavenǿ���������µİ�����Ҫ����-U������
	�磺mvn clean install -U