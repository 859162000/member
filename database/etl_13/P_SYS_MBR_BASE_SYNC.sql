CREATE OR REPLACE PROCEDURE P_SYS_MBR_BASE_SYNC
(
in_sync_date in varchar2 --ͬ�������ַ���YYYYMMDD
)
--ͬ�����ε�
AS
on_flag NUMBER;
os_prompt varchar2(3000);
v_sync_date  varchar2(6); --ͬ�������ַ���YYYYMMDD
BEGIN
/******************************����ͬ��******************************/

--------------��Ա������Ϣ-------------
P_SYS_MBR_SYNC(in_sync_date);
--------------��ԱKPI��Ϣ-------------
P_SYS_MBR_SYNC(in_sync_date);
--------------Ӫ����Ϣ-------------
P_SYS_CAMPAIGN_SYNC(in_sync_date);
--------------����ͬ��״̬-------------
update ccs_report.t_sys_data_job@CCSFLAG set flag_mbr_base=1,time_mbr_base=sysdate where ymd= in_sync_date;

/******************************ͬ�����******************************/
on_flag:=0;
os_prompt:='ͬ���ɹ�';
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_BASE','P_SYS_MBR_BASE',NULL,SYSDATE,on_flag,os_prompt);
COMMIT;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
     P_SYS_SP_FN_ERR_LOG('P_SYS_CAMPAIGN_SYNC',SYSDATE,'ERROR','���ݿ���NO_DATA_FOUND');
      WHEN TOO_MANY_ROWS THEN
      P_SYS_SP_FN_ERR_LOG('P_SYS_CAMPAIGN_SYNC',SYSDATE,'ERROR','TOO_MANY_ROWS');
      WHEN OTHERS THEN
      on_flag := -1;
rollback;
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_BASE','P_SYS_MBR_BASE',NULL,SYSDATE,on_flag,SUBSTRB(SQLERRM,1,3000));
end;