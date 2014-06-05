CREATE OR REPLACE PROCEDURE P_SYS_TASK_SCHEDULING_LOG
/*
��¼����ϵͳ��Ҫ���ȵ����ݻ���SP������ˢ��SP�ĵ���ִ���������ά���Ŵ�ʹ�á�
*/
(
is_task_type          VARCHAR2,
is_task_name             VARCHAR2,
is_Para_Date             VARCHAR2,
id_execute_end_date              DATE,
is_successful                    VARCHAR2,
is_message                       VARCHAR2
)
is
BEGIN

INSERT INTO T_SYS_TASK_SCHEDULING_LOG(task_type, task_name,Para_Date, execute_end_date, is_successful, message)
values(is_task_type,is_task_name,is_Para_Date,id_execute_end_date,is_successful,is_message);

COMMIT;

EXCEPTION
WHEN OTHERS THEN
P_SYS_SP_FN_ERR_LOG('P_SYS_TASK_SCHEDULING_LOG',SYSDATE,'ERROR',SUBSTRB(SQLERRM,1,3000));
END ;