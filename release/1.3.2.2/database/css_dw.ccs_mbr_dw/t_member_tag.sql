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
'��Աͳ������';

comment on column T_MEMBER_TAG.MEMBER_ID is
'��ԱID';

comment on column T_MEMBER_TAG.PRICE_CONSCIOUS is
'�۸����С�0�����С�1����';

comment on column T_MEMBER_TAG.FAMILY_NUMBER1 is
'��ͥ����1';

comment on column T_MEMBER_TAG.FAMILY_NUMBER2 is
'��ͥ����2';

comment on column T_MEMBER_TAG.TRADE_ABNORMAL is
'�쳣����';

comment on column T_MEMBER_TAG.ACTIVITY_LEVEL is
'��Ա��Ծ��';

comment on column T_MEMBER_TAG.ONLINE_TRADE_TYPE is
'��������ƫ��(���Ͻ��׷�ʽ)';

comment on column T_MEMBER_TAG.FAV_FILM_TYPE1 is
'ӰƬƫ��1(��û���ҵ�����Ϊ -999)';

comment on column T_MEMBER_TAG.FAV_FILM_TYPE2 is
'ӰƬƫ��2(��û���ҵ�����Ϊ -999)';

comment on column T_MEMBER_TAG.FAV_FILM_TYPE3 is
'ӰƬƫ��3(��û���ҵ�����Ϊ -999)';

comment on column T_MEMBER_TAG.CREATE_DATE is
'����ʱ��';

comment on column T_MEMBER_TAG.UPDATE_DATE is
'����ʱ��';

/*==============================================================*/
/* Index: IDX_T_MEMBER_TAG1                                     */
/*==============================================================*/
create index IDX_T_MEMBER_TAG1 on T_MEMBER_TAG (
   UPDATE_DATE DESC
);
