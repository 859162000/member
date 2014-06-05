
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
'复合客群子客群';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.COMBINE_SEGMENT_SUB_ID is
'复合客群子客群ID';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SEGMENT_ID is
'客群ID';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SUB_SEGMENT_ID is
'子客群ID';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SORT_INDEX is
'排序索引(0为主客群)';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SET_RELATION is
'复合集合关系-DIM1015';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.COUNT_ALTER is
'复合后数据变化';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.CAL_COUNT is
'实际数量（计算本复合客群时的子客群数量）';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.CAL_COUNT_TIME is
'实际数量计算时间';

comment on column ccs_mbr_prod.T_COMBINE_SEGMENT_SUB.SEGMENT_VERSION is
'客群版本(计算本复合客群时的子客的版本）';

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