-- Add/modify columns 
alter table ccs_mbr_prod.T_SEGMENT add COMBINE_SEGMENT char(1) default '0' not null;
alter table ccs_mbr_prod.T_SEGMENT add STATUS VARCHAR2(20) default '10' not null;
alter table ccs_mbr_prod.T_SEGMENT add OCCUPIED char(1) default '0' not null;
alter table ccs_mbr_prod.T_SEGMENT add ISDELETE char(1) default '0' not null;
-- Add comments to the columns 
comment on column ccs_mbr_prod.T_SEGMENT.COMBINE_SEGMENT
  is '�Ƿ񸴺Ͽ�Ⱥ';
comment on column ccs_mbr_prod.T_SEGMENT.STATUS
  is '��Ⱥ״̬��DIM-1030��';
comment on column ccs_mbr_prod.T_SEGMENT.OCCUPIED
  is '��ռ�ñ�� 0δռ�ã�1��Ⱥ����ռ��, 2��Ⱥ��Ӧ����ռ�ã�3���ζ���ռ��';
comment on column ccs_mbr_prod.T_SEGMENT.ISDELETE
  is '�߼�ɾ����ʶ,Ĭ��:0 δɾ��;1ɾ��;����:�Ƿ�';


-- ����CAL_COUNT>=0��Ϊ��������ɡ�״̬��CAL_COUNT=-1��Ϊ�������С�״̬
update ccs_mbr_prod.T_SEGMENT set STATUS='30' where CAL_COUNT >= 0;
update ccs_mbr_prod.T_SEGMENT set STATUS='20' where CAL_COUNT = -1;
commit;




