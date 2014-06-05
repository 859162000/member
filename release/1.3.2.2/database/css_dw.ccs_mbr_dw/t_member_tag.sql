/*==============================================================*/
/* Table: T_MEMBER_TAG                                          */
/*==============================================================*/
create table T_MEMBER_TAG 
(
   MEMBER_ID            INTEGER              not null,
   PRICE_CONSCIOUS      CHAR(1)              default '0' not null,
   FAMILY_NUMBER1       VARCHAR2(20)         default '0' not null,
   FAMILY_NUMBER2       VARCHAR2(20)         default '0' not null,
   TRADE_ABNORMAL       CHAR(1)              default '0' not null,
   ACTIVITY_LEVEL       VARCHAR2(20)         default '0' not null,
   ONLINE_TRADE_TYPE    VARCHAR2(20)         default '-999' not null,
   FAV_FILM_TYPE1       VARCHAR2(20)         default '-999' not null,
   FAV_FILM_TYPE2       VARCHAR2(20)         default '-999' not null,
   FAV_FILM_TYPE3       VARCHAR2(20)         default '-999' not null,
   CREATE_DATE          TIMESTAMP(6)         default SYSDATE not null,
   UPDATE_DATE          TIMESTAMP(6)         default SYSDATE not null,
   constraint PK_T_MEMBER_TAG primary key (MEMBER_ID)
);

comment on table T_MEMBER_TAG is
'会员统计属性';

comment on column T_MEMBER_TAG.MEMBER_ID is
'会员ID';

comment on column T_MEMBER_TAG.PRICE_CONSCIOUS is
'价格敏感。0不敏感、1敏感';

comment on column T_MEMBER_TAG.FAMILY_NUMBER1 is
'家庭构成1';

comment on column T_MEMBER_TAG.FAMILY_NUMBER2 is
'家庭构成2';

comment on column T_MEMBER_TAG.TRADE_ABNORMAL is
'异常消费';

comment on column T_MEMBER_TAG.ACTIVITY_LEVEL is
'会员活跃度';

comment on column T_MEMBER_TAG.ONLINE_TRADE_TYPE is
'电子渠道偏好(线上交易方式)';

comment on column T_MEMBER_TAG.FAV_FILM_TYPE1 is
'影片偏好1(如没有找到设置为 -999)';

comment on column T_MEMBER_TAG.FAV_FILM_TYPE2 is
'影片偏好2(如没有找到设置为 -999)';

comment on column T_MEMBER_TAG.FAV_FILM_TYPE3 is
'影片偏好3(如没有找到设置为 -999)';

comment on column T_MEMBER_TAG.CREATE_DATE is
'创建时间';

comment on column T_MEMBER_TAG.UPDATE_DATE is
'更新时间';

/*==============================================================*/
/* Index: IDX_T_MEMBER_TAG1                                     */
/*==============================================================*/
create index IDX_T_MEMBER_TAG1 on T_MEMBER_TAG (
   UPDATE_DATE DESC
);
