CREATE OR REPLACE PROCEDURE P_SYS_SP_FN_ERR_LOG
/*
获取报表系统中SP及FUNCTION中ORACLE抛出的异常及业务逻辑中自定义的错误提示信息，供维护排错使用。
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
VALUES('P_SYS_SP_FN_ERR_LOG',SYSDATE,'FATAL','P_SYS_SP_FN_ERR_LOG插入错误日志表时出错',SYSDATE);
COMMIT;
--DBMS_OUTPUT.PUT_LINE(SQLERRM);

END ;