CREATE OR REPLACE PROCEDURE P_SYS_MBR_BASE_SYNC
(
in_sync_date in varchar2 --同步日期字符串YYYYMMDD
)
--同步波次等
AS
on_flag NUMBER;
os_prompt varchar2(3000);
v_sync_date  varchar2(6); --同步日期字符串YYYYMMDD
BEGIN
/******************************增量同步******************************/

--------------会员基本信息-------------
P_SYS_MBR_SYNC(in_sync_date);
--------------会员KPI信息-------------
P_SYS_MBR_SYNC(in_sync_date);
--------------营销信息-------------
P_SYS_CAMPAIGN_SYNC(in_sync_date);
--------------更新同步状态-------------
update ccs_report.t_sys_data_job@CCSFLAG set flag_mbr_base=1,time_mbr_base=sysdate where ymd= in_sync_date;

/******************************同步完成******************************/
on_flag:=0;
os_prompt:='同步成功';
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_BASE','P_SYS_MBR_BASE',NULL,SYSDATE,on_flag,os_prompt);
COMMIT;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
     P_SYS_SP_FN_ERR_LOG('P_SYS_CAMPAIGN_SYNC',SYSDATE,'ERROR','数据库中NO_DATA_FOUND');
      WHEN TOO_MANY_ROWS THEN
      P_SYS_SP_FN_ERR_LOG('P_SYS_CAMPAIGN_SYNC',SYSDATE,'ERROR','TOO_MANY_ROWS');
      WHEN OTHERS THEN
      on_flag := -1;
rollback;
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_BASE','P_SYS_MBR_BASE',NULL,SYSDATE,on_flag,SUBSTRB(SQLERRM,1,3000));
end;