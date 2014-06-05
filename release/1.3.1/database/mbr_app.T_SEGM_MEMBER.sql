

/*==============================================================*/
/* Table: ccs_mbr_prod.T_SEGM_MEMBER                                         */
/*==============================================================*/
create table ccs_mbr_prod.T_SEGM_MEMBER 
(
   SEGM_MEMBER_ID       INTEGER              not null,
   SEGMENT_ID           INTEGER              not null,
   MEMBER_ID            INTEGER              not null,
   constraint PK_T_SEGM_MEMBER primary key (SEGM_MEMBER_ID)
);

comment on table ccs_mbr_prod.T_SEGM_MEMBER is
'��Ⱥ��Ӧ��Ա';

comment on column ccs_mbr_prod.T_SEGM_MEMBER.SEGM_MEMBER_ID is
'��Ⱥ��Ӧ��ԱID';

comment on column ccs_mbr_prod.T_SEGM_MEMBER.SEGMENT_ID is
'��ȺID';

comment on column ccs_mbr_prod.T_SEGM_MEMBER.MEMBER_ID is
'��ԱID';

/*==============================================================*/
/* Index: IDX_SEGM_MEMBER                                       */
/*==============================================================*/
create index IDX_SEGM_MEMBER on ccs_mbr_prod.T_SEGM_MEMBER (
   SEGMENT_ID ASC
);
