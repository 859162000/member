
/*==============================================================*/
/* Table: ccs_mbr_prod.T_COMBINE_SEGMENT_SUB                                 */
/*==============================================================*/
create table ccs_mbr_prod.T_COMBINE_SEGMENT_SUB 
(
   COMBINE_SEGMENT_SUB_ID INTEGER              not null,
   SEGMENT_ID           INTEGER              not null,
   SUB_SEGMENT_ID       INTEGER              not null,
   SORT_INDEX           NUMBER(8)            default 0 not null,
   SET_RELATION         VARCHAR2(20),
   COUNT_ALTER          INTEGER,
   CAL_COUNT            INTEGER              default 0,
   CAL_COUNT_TIME       TIMESTAMP,
   SEGMENT_VERSION      VARCHAR2(128),
   CREATE_BY            VARCHAR2(40),
   CREATE_DATE          TIMESTAMP(6),
   UPDATE_BY            VARCHAR2(40),
   UPDATE_DATE          TIMESTAMP(6)         default SYSDATE,
   VERSION              INTEGER              not null,
   constraint PK_T_COMBINE_SEGMENT_SUB primary key (COMBINE_SEGMENT_SUB_ID)
);

comment on table ccs_mbr_prod.T_COMBINE_SEGMENT_SUB is
'���Ͽ�Ⱥ�ӿ�Ⱥ';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.COMBINE_SEGMENT_SUB_ID is
'���Ͽ�Ⱥ�ӿ�ȺID';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SEGMENT_ID is
'��ȺID';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SUB_SEGMENT_ID is
'�ӿ�ȺID';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SORT_INDEX is
'��������(0Ϊ����Ⱥ)';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SET_RELATION is
'���ϼ��Ϲ�ϵ-DIM1015';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.COUNT_ALTER is
'���Ϻ����ݱ仯';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.CAL_COUNT is
'ʵ�����������㱾���Ͽ�Ⱥʱ���ӿ�Ⱥ������';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.CAL_COUNT_TIME is
'ʵ����������ʱ��';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SEGMENT_VERSION is
'��Ⱥ�汾(���㱾���Ͽ�Ⱥʱ���ӿ͵İ汾��';

/*==============================================================*/
/* Index: IDX_COMBINE_SEGMENT_SUB                               */
/*==============================================================*/
create unique index IDX_COMBINE_SEGMENT_SUB on ccs_mbr_prod.T_COMBINE_SEGMENT_SUB (
   SEGMENT_ID ASC,
   SORT_INDEX ASC
);

alter table ccs_mbr_prod.T_COMBINE_SEGMENT_SUB
   add constraint FK_FK_COMBINE_SEGMENT_SUB_1 foreign key (SEGMENT_ID)
      references ccs_mbr_prod.T_SEGMENT (SEGMENT_ID)
      on delete cascade;

alter table ccs_mbr_prod.T_COMBINE_SEGMENT_SUB
   add constraint FK_FK_COMBINE_SEGMENT_SUB_2 foreign key (SUB_SEGMENT_ID)
      references ccs_mbr_prod.T_SEGMENT (SEGMENT_ID)
      on delete cascade;