CREATE OR REPLACE PROCEDURE P_SYS_MBR_SYNC
(
in_sync_date in varchar2 --ͬ�������ַ���YYYYMMDD
)
--ͬ����Ա�����Ϣ
AS
on_flag NUMBER;
os_prompt varchar2(3000);
v_sync_date  varchar2(6); --ͬ�������ַ���YYYYMMDD

BEGIN
/******************************����ͬ��******************************/
--------------T_MEMBER-------------
DELETE FROM T_MEMBER where MEMBER_ID in (
select MEMBER_ID from ccs_mbr_prod.T_MEMBER@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_MEMBER
(MEMBER_ID,MEMBER_NO,MOBILE,NAME,GENDER,BIRTHDAY,REGIST_TYPE,REGIST_CHNID,REGIST_OP_NO,REGIST_OP_NAME,
REGIST_CINEMA_ID,STATUS,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,EMAIL,ISCONTACTABLE,PHONE,
FAV_CINEMA_ID,SOURCE_TYPE,REGIST_DATE,CHANGE_STATUS_RESION,HIS_MEMBER_NO)
select MEMBER_ID,MEMBER_NO,MOBILE,NAME,GENDER,BIRTHDAY,REGIST_TYPE,REGIST_CHNID,REGIST_OP_NO,REGIST_OP_NAME,
REGIST_CINEMA_ID,STATUS,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,EMAIL,ISCONTACTABLE,PHONE,
FAV_CINEMA_ID,SOURCE_TYPE,REGIST_DATE,CHANGE_STATUS_RESION,HIS_MEMBER_NO
from ccs_mbr_prod.T_MEMBER@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24 ;
COMMIT;


----------T_MEMBER_ADDR--------------
DELETE FROM T_MEMBER_ADDR where MEMBER_ADDR_ID in (
select MEMBER_ADDR_ID from ccs_mbr_prod.T_MEMBER_ADDR@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_MEMBER_ADDR
(MEMBER_ADDR_ID,ZIPCODE,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,COUNTRY_ID,PROVINCE_ID,CITY_ID,ISDELETE,
CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_ID)
select MEMBER_ADDR_ID,ZIPCODE,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,COUNTRY_ID,PROVINCE_ID,CITY_ID,ISDELETE,
CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_ID
from ccs_mbr_prod.T_MEMBER_ADDR@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24 ;
COMMIT;

---------T_MEMBER_INFO---------------
DELETE FROM T_MEMBER_INFO where MEMBER_INFO_ID in (
select MEMBER_INFO_ID from ccs_mbr_prod.T_MEMBER_INFO@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_MEMBER_INFO
(MANAGE_CINEMA_ID,EDUCATION,OCCUPATION,INCOME,MARRY_STATUS,CHILD_NUMBER,FQ_CINEMA_DIST,FQ_CINEMA_TIME,
MOBILE_OPTIN,IDCARD_TYPE,IDCARD_NO,IDCARD_HASHNO,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,
VERSION,MEMBER_INFO_ID,MEMBER_ID,WEIBO,QQ,DOUBAN)
select MANAGE_CINEMA_ID,EDUCATION,OCCUPATION,INCOME,MARRY_STATUS,CHILD_NUMBER,FQ_CINEMA_DIST,FQ_CINEMA_TIME,
MOBILE_OPTIN,IDCARD_TYPE,IDCARD_NO,IDCARD_HASHNO,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,
VERSION,MEMBER_INFO_ID,MEMBER_ID,WEIBO,QQ,DOUBAN
from ccs_mbr_prod.T_MEMBER_INFO@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24 ;
COMMIT;

------------T_MEMBER_LEVEL------------
DELETE FROM T_MEMBER_LEVEL where LEVEL_ID in (
select LEVEL_ID from ccs_mbr_prod.T_MEMBER_LEVEL@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_MEMBER_LEVEL
(MEMBER_ID,MEM_LEVEL,EXPIRE_DATE,ORG_LEVEL,SET_TIME,TARGET_LEVEL,LEVEL_POINT_OFFSET,TICKET_OFFSET,CHANGE_LEVEL_NO,LEVEL_ID,ISDELETE,
CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_LEVEL_HISTORY_ID)
select MEMBER_ID,MEM_LEVEL,EXPIRE_DATE,ORG_LEVEL,SET_TIME,TARGET_LEVEL,LEVEL_POINT_OFFSET,TICKET_OFFSET,CHANGE_LEVEL_NO,LEVEL_ID,ISDELETE,
CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,MEMBER_LEVEL_HISTORY_ID
from ccs_mbr_prod.T_MEMBER_LEVEL@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24 ;
COMMIT;

------------T_MEMBER_POINT------------
DELETE FROM T_MEMBER_POINT where MEMBER_POINT_ID in (
select MEMBER_POINT_ID from ccs_mbr_prod.T_MEMBER_POINT@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_MEMBER_POINT
(MEMBER_POINT_ID,EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE,POINT_HISTORY_ID,LEVEL_POINT_TOTAL,ACTIVITY_POINT,ISDELETE,
CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,LAST_EXCHANGE_DATE,MEMBER_ID)
select MEMBER_POINT_ID,EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE,POINT_HISTORY_ID,LEVEL_POINT_TOTAL,ACTIVITY_POINT,ISDELETE,
CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,LAST_EXCHANGE_DATE,MEMBER_ID
from ccs_mbr_prod.T_MEMBER_POINT@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24 ;
COMMIT;
-------------T_FT_TYPE--------------------
DELETE FROM T_FT_TYPE ;
insert into T_FT_TYPE
(FILM_ID,TYPE,IDX)
select FILM_ID,TYPE,IDX
from ccs_mbr_prod.T_FT_TYPE@MBR_BASE
COMMIT;
/******************************ͬ�����******************************/
on_flag:=0;
os_prompt:='ͬ���ɹ�';
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_SYNC','P_SYS_MBR_SYNC',NULL,SYSDATE,on_flag,os_prompt);
COMMIT;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
     P_SYS_SP_FN_ERR_LOG('P_SYS_MBR_SYNC',SYSDATE,'ERROR','���ݿ���NO_DATA_FOUND');
      WHEN TOO_MANY_ROWS THEN
      P_SYS_SP_FN_ERR_LOG('P_SYS_MBR_SYNC',SYSDATE,'ERROR','TOO_MANY_ROWS');
      WHEN OTHERS THEN
      on_flag := -1;
rollback;
P_SYS_TASK_SCHEDULING_LOG('P_SYS_MBR_SYNC','P_SYS_MBR_SYNC',NULL,SYSDATE,on_flag,SUBSTRB(SQLERRM,1,3000));
end;
