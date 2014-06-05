-- Add/modify columns 
alter table ccs_mbr_prod.T_EXT_POINT_CRITERIA add ISDELETE char(1) default '0';
-- Add comments to the columns 
comment on column ccs_mbr_prod.T_EXT_POINT_CRITERIA.ISDELETE
  is '逻辑删除标识,默认:0 未删除;1删除;其他:非法';






