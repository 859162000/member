CREATE OR REPLACE PROCEDURE P_SYS_MBR_KPI_SYNC
(
in_sync_date in varchar2 --同步日期字符串YYYYMMDD
)
AS
on_flag NUMBER;
os_prompt varchar2(3000);
v_sync_date  varchar2(6); --同步日期字符串YYYYMMDD
BEGIN
/******************************增量同步T_MEMBER_KPI******************************/

DELETE FROM T_MEMBER_KPI where MEMBER_KPI_ID in (
select MEMBER_KPI_ID from ccs_mbr_prod.T_MEMBER_KPI@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
)
;
insert into T_MEMBER_KPI
(MEMBER_KPI_ID,KPI_YEAR,CINEMA_ID,KPI_VALUE,CREATE_BY,CREATE_DATE,
                     UPDATE_BY,UPDATE_DATE,VERSION,KPI_NAME,KPI_TYPE)
select MEMBER_KPI_ID,KPI_YEAR,CINEMA_ID,KPI_VALUE,CREATE_BY,CREATE_DATE,
                     UPDATE_BY,UPDATE_DATE,VERSION,KPI_NAME,KPI_TYPE
from ccs_mbr_prod.T_MEMBER_KPI@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
;
COMMIT;
/******************************同步完成******************************/
on_flag:=0;
os_prompt:='同步成功';
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_KPI_SYNC','P_SYS_MBR_TRANS_SYNC',NULL,SYSDATE,on_flag,os_prompt);
COMMIT;
EXCEPTION
   WHEN NO_DATA_FOUND THEN
     P_SYS_SP_FN_ERR_LOG('P_SYS_MBR_KPI_SYNC',SYSDATE,'ERROR','数据库中NO_DATA_FOUND');
      WHEN TOO_MANY_ROWS THEN
      P_SYS_SP_FN_ERR_LOG('P_SYS_MBR_KPI_SYNC',SYSDATE,'ERROR','TOO_MANY_ROWS');
      WHEN OTHERS THEN
      on_flag := -1;
rollback;
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_KPI_SYNC','P_SYS_MBR_KPI_SYNC',NULL,SYSDATE,on_flag,SUBSTRB(SQLERRM,1,3000));
end;
