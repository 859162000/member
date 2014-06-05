CREATE OR REPLACE PROCEDURE P_SYS_MBR_TRANS_SYNC
(
in_sync_date in varchar2 --同步日期字符串YYYYMMDD
)
--同步交易数据
AS
on_flag NUMBER;
os_prompt varchar2(3000);
v_sync_date  varchar2(6); --同步日期字符串YYYYMMDD

BEGIN
/******************************增量同步T_TICKET_TRANS_ORDER , T_GOODS_TRANS_ORDER******************************/

DELETE FROM T_TICKET_TRANS_ORDER where TRANS_ID in (
select TRANS_ID from ccs_mbr_prod.T_TICKET_TRANS_ORDER@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
)
;
insert into T_TICKET_TRANS_ORDER
(TRANS_ID,ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,MEMBER_NUM,IS_POINT,CINEMA_INNER_CODE,MEMBER_ID,FILM_CODE,FILM_NAME,
               HALL_NUM,SHOW_TIME,BIZ_DATE,TRANS_TIME,POINT,POINT_AMOUNT,TRANS_ORDER_ID,HIS_MEMBER_NO,update_by,update_date)
select TRANS_ID,ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,MEMBER_NUM,IS_POINT,CINEMA_INNER_CODE,MEMBER_ID,FILM_CODE,FILM_NAME,
                HALL_NUM,SHOW_TIME,BIZ_DATE,TRANS_TIME,POINT,POINT_AMOUNT,TRANS_ORDER_ID,HIS_MEMBER_NO,update_by,update_date
from ccs_mbr_prod.T_TICKET_TRANS_ORDER@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
;
COMMIT;


------------------------
DELETE FROM T_GOODS_TRANS_ORDER where GOODS_TRANS_ID in (
select GOODS_TRANS_ID from ccs_mbr_prod.T_GOODS_TRANS_ORDER@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
)
;
insert into T_GOODS_TRANS_ORDER
(GOODS_TRANS_ID,ORDER_ID,TOTAL_AMOUNT,MEMBER_NUM,IS_POINT,TRANS_TYPE,CINEMA_INNER_CODE,MEMBER_ID,GOOD_NAME,
             BIZ_DATE,TRANS_TIME,POINT,TRANS_ORDER_ID,update_by,update_date)
select GOODS_TRANS_ID,ORDER_ID,TOTAL_AMOUNT,MEMBER_NUM,IS_POINT,TRANS_TYPE,CINEMA_INNER_CODE,MEMBER_ID,GOOD_NAME,
             BIZ_DATE,TRANS_TIME,POINT,TRANS_ORDER_ID,update_by,update_date
from ccs_mbr_prod.T_GOODS_TRANS_ORDER@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
;
COMMIT;
/******************************同步完成******************************/
on_flag:=0;
os_prompt:='同步成功';
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_TRANS_SYNC','P_SYS_MBR_TRANS_SYNC',NULL,SYSDATE,on_flag,os_prompt);
COMMIT;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
     P_SYS_SP_FN_ERR_LOG('P_SYS_MBR_TRANS_SYNC',SYSDATE,'ERROR','数据库中NO_DATA_FOUND');
      WHEN TOO_MANY_ROWS THEN
      P_SYS_SP_FN_ERR_LOG('P_SYS_MBR_TRANS_SYNC',SYSDATE,'ERROR','TOO_MANY_ROWS');
      WHEN OTHERS THEN
      on_flag := -1;
rollback;
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_TRANS_SYNC','P_SYS_MBR_TRANS_SYNC',NULL,SYSDATE,on_flag,SUBSTRB(SQLERRM,1,3000));
end;
