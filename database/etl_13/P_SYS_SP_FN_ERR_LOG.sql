CREATE OR REPLACE PROCEDURE P_SYS_SP_FN_ERR_LOG
/*
��ȡ����ϵͳ��SP��FUNCTION��ORACLE�׳����쳣��ҵ���߼����Զ���Ĵ�����ʾ��Ϣ����ά���Ŵ�ʹ�á�
*/
(
is_Object_Name          VARCHAR2,
id_Log_Date             DATE,
is_Err_Level            VARCHAR2,
is_Message              VARCHAR2
)
is
BEGIN

INSERT INTO t_sys_sp_fn_err_log(object_name, log_date, err_level, message)
values(is_Object_Name,id_Log_Date,is_Err_Level,is_Message);

COMMIT;

EXCEPTION
WHEN OTHERS THEN
INSERT INTO T_SYS_SP_FN_ERR_LOG(object_name, log_date, err_level, message, update_date)
VALUES('P_SYS_SP_FN_ERR_LOG',SYSDATE,'FATAL','P_SYS_SP_FN_ERR_LOG���������־��ʱ����',SYSDATE);
COMMIT;
--DBMS_OUTPUT.PUT_LINE(SQLERRM);

END ;