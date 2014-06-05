CREATE OR REPLACE PROCEDURE P_SYS_CAMPAIGN_SYNC
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
--------------T_ACT_RESULT-------------
DELETE FROM T_ACT_RESULT where CMN_ACTIVITY_ID in (
select CMN_ACTIVITY_ID from ccs_mbr_prod.T_ACT_RESULT@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_ACT_RESULT
(CMN_ACTIVITY_ID,VOUCHER_POOL_ID,EXT_POINT_RULE_ID,CONTACT_COUNT,RES_CONFIG_TYPE,RES_SEGMENT_ID,
ALTER_SEGMENT_ID,CONTROL_RES_COUNT,RES_COUNT,ALTER_RES_COUNT,CREATE_BY,CREATE_DATE,UPDATE_BY,
UPDATE_DATE,VERSION)
select CMN_ACTIVITY_ID,VOUCHER_POOL_ID,EXT_POINT_RULE_ID,CONTACT_COUNT,RES_CONFIG_TYPE,RES_SEGMENT_ID,
ALTER_SEGMENT_ID,CONTROL_RES_COUNT,RES_COUNT,ALTER_RES_COUNT,CREATE_BY,CREATE_DATE,UPDATE_BY,
UPDATE_DATE,VERSION
from ccs_mbr_prod.T_ACT_RESULT@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24;
COMMIT;


----------T_ACT_TARGET--------------
DELETE FROM T_ACT_TARGET where ACT_TARGET_ID in (
select ACT_TARGET_ID from ccs_mbr_prod.T_ACT_TARGET@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_ACT_TARGET
(ACT_TARGET_ID,TARGET_TYPE,SEGMENT_ID,TOTAL_COUNT,MAX_COUNT,CONTROL_COUNT,CREATE_BY,CREATE_DATE,
UPDATE_BY,UPDATE_DATE,VERSION)
select ACT_TARGET_ID,TARGET_TYPE,SEGMENT_ID,TOTAL_COUNT,MAX_COUNT,CONTROL_COUNT,CREATE_BY,CREATE_DATE,
UPDATE_BY,UPDATE_DATE,VERSION
from ccs_mbr_prod.T_ACT_TARGET@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24;
COMMIT;

---------T_CAMPAIGN---------------
DELETE FROM T_CAMPAIGN where CAMPAIGN_ID in (
select CAMPAIGN_ID from ccs_mbr_prod.T_CAMPAIGN@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_CAMPAIGN
(CAMPAIGN_ID,CODE,NAME,START_DATE,END_DATE,STATUS,TYPE,CHANNEL,DESCRIPTION,CREATION_CINEMA_ID,CREATION_AREA_ID,
ALL_CINEMA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,CREATION_LEVEL,ALLOW_MODIFIER)
select CAMPAIGN_ID,CODE,NAME,START_DATE,END_DATE,STATUS,TYPE,CHANNEL,DESCRIPTION,CREATION_CINEMA_ID,CREATION_AREA_ID,
ALL_CINEMA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,CREATION_LEVEL,ALLOW_MODIFIER
from ccs_mbr_prod.T_CAMPAIGN@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24;
COMMIT;

------------T_CMN_ACTIVITY------------
DELETE FROM T_CMN_ACTIVITY where CMN_ACTIVITY_ID in (
select CMN_ACTIVITY_ID from ccs_mbr_prod.T_CMN_ACTIVITY@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_CMN_ACTIVITY
(CMN_ACTIVITY_ID,NAME,CODE,START_DTIME,END_DTIME,STATUS,PROMOTION_TYPE,DESCRIPTION,
PRIORITY,CAMPAIGN_ID,CMN_PHAISE_ID,OFFER_ID,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,
VERSION,ACT_TARGET_ID)
select CMN_ACTIVITY_ID,NAME,CODE,START_DTIME,END_DTIME,STATUS,PROMOTION_TYPE,DESCRIPTION,
PRIORITY,CAMPAIGN_ID,CMN_PHAISE_ID,OFFER_ID,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,
VERSION,ACT_TARGET_ID
from ccs_mbr_prod.T_CMN_ACTIVITY@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24;
COMMIT;

------------T_CMN_PHASE------------
DELETE FROM T_CMN_PHASE where CMN_PHAISE_ID in (
select CMN_PHAISE_ID from ccs_mbr_prod.T_CMN_PHASE@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_CMN_PHASE
(CMN_PHAISE_ID,NAME,CODE,START_DATE,END_DATE,STATUS,DESCRIPTION,CAMPAIGN_ID,CREATE_BY,
CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION)
select CMN_PHAISE_ID,NAME,CODE,START_DATE,END_DATE,STATUS,DESCRIPTION,CAMPAIGN_ID,CREATE_BY,
CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION
from ccs_mbr_prod.T_CMN_PHASE@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24;
COMMIT;


------------T_CONTACT_HISTORY------------
DELETE FROM T_CONTACT_HISTORY where CONTACT_HISTORY_ID in (
select CONTACT_HISTORY_ID from ccs_mbr_prod.T_CONTACT_HISTORY@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24
);
insert into T_CONTACT_HISTORY
(CONTACT_HISTORY_ID,HAS_RESPONSE,MEMBER_ID,ACT_TARGET_ID,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,
HAS_RESPONSE2,HAS_SEND,IS_CONTROLSET)
select CONTACT_HISTORY_ID,HAS_RESPONSE,MEMBER_ID,ACT_TARGET_ID,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,
HAS_RESPONSE2,HAS_SEND,IS_CONTROLSET
from ccs_mbr_prod.T_CONTACT_HISTORY@MBR_BASE
where update_date>=to_date(in_sync_date,'yyyymmdd') + 6/24 and update_date <to_date(in_sync_date,'yyyymmdd') + 30/24;
COMMIT;
/******************************同步完成******************************/
on_flag:=0;
os_prompt:='同步成功';
P_SYS_TASK_SCHEDULING_LOG('P_SYS_CAMPAIGN_SYNC','P_SYS_CAMPAIGN_SYNC',NULL,SYSDATE,on_flag,os_prompt);
COMMIT;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
     P_SYS_SP_FN_ERR_LOG('P_SYS_CAMPAIGN_SYNC',SYSDATE,'ERROR','数据库中NO_DATA_FOUND');
      WHEN TOO_MANY_ROWS THEN
      P_SYS_SP_FN_ERR_LOG('P_SYS_CAMPAIGN_SYNC',SYSDATE,'ERROR','TOO_MANY_ROWS');
      WHEN OTHERS THEN
      on_flag := -1;
rollback;
P_SYS_TASK_SCHEDULING_LOG('P_SYS_CAMPAIGN_SYNC','P_SYS_CAMPAIGN_SYNC',NULL,SYSDATE,on_flag,SUBSTRB(SQLERRM,1,3000));
end;