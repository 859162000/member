-- Add/modify columns 
alter table ccs_mbr_prod.T_EXT_POINT_CRITERIA add ISDELETE char(1) default '0';
-- Add comments to the columns 
comment on column ccs_mbr_prod.T_EXT_POINT_CRITERIA.ISDELETE
  is '�߼�ɾ����ʶ,Ĭ��:0 δɾ��;1ɾ��;����:�Ƿ�';






