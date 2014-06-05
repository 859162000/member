create table ccs_mbr_prod.T_CONTACT_HISTORY_TEMP 
(
   CONTACT_HISTORY_TEMP_ID INTEGER              not null,
   CREATE_BY            VARCHAR2(40),
   CREATE_DATE          TIMESTAMP(6),
   UPDATE_BY            VARCHAR2(40),
   UPDATE_DATE          TIMESTAMP(6)         default SYSDATE,
   VERSION              INTEGER,
   FILE_ATTACH_ID       INTEGER              not null,
   MEMBER_ID            INTEGER,
   MOBILE               VARCHAR2(20),
   constraint PK_T_CONTACT_HISTORY_TEMP primary key (CONTACT_HISTORY_TEMP_ID)
);

comment on column ccs_mbr_prod.T_CONTACT_HISTORY_TEMP.CONTACT_HISTORY_TEMP_ID is
'导入目标id';

comment on column ccs_mbr_prod.T_CONTACT_HISTORY_TEMP.FILE_ATTACH_ID is
'文件ID';

comment on column ccs_mbr_prod.T_CONTACT_HISTORY_TEMP.MEMBER_ID is
'会员id';

comment on column ccs_mbr_prod.T_CONTACT_HISTORY_TEMP.MOBILE is
'会员手机号';

alter table ccs_mbr_prod.T_CONTACT_HISTORY_TEMP
   add constraint FK_T_CONTAC_REFERENCE_T_FILE_A foreign key (FILE_ATTACH_ID)
      references ccs_mbr_prod.T_FILE_ATTACH (FILE_ATTACH_ID)
      on delete cascade;

      
alter table ccs_mbr_prod.T_ABATCH_ERRE_LOG add ERRE_LEVEL VARCHAR2(10);
comment on column ccs_mbr_prod.T_ABATCH_ERRE_LOG.ERRE_LEVEL is
'错误级别 10错误 20警告';