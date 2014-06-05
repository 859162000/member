-- Add/modify columns 
alter table ccs_mbr_prod.T_SEGMENT add COMBINE_SEGMENT char(1) default '0' not null;
alter table ccs_mbr_prod.T_SEGMENT add STATUS VARCHAR2(20) default '10' not null;
alter table ccs_mbr_prod.T_SEGMENT add OCCUPIED char(1) default '0' not null;
alter table ccs_mbr_prod.T_SEGMENT add ISDELETE char(1) default '0' not null;
-- Add comments to the columns 
comment on column ccs_mbr_prod.T_SEGMENT.COMBINE_SEGMENT
  is '是否复合客群';
comment on column ccs_mbr_prod.T_SEGMENT.STATUS
  is '客群状态（DIM-1030）';
comment on column ccs_mbr_prod.T_SEGMENT.OCCUPIED
  is '被占用标记 0未占用，1客群计算占用, 2客群响应计算占用，3波次冻结占用';
comment on column ccs_mbr_prod.T_SEGMENT.ISDELETE
  is '逻辑删除标识,默认:0 未删除;1删除;其他:非法';


-- 设置CAL_COUNT>=0的为“计算完成”状态，CAL_COUNT=-1的为“计算中”状态
update ccs_mbr_prod.T_SEGMENT set STATUS='30' where CAL_COUNT >= 0;
update ccs_mbr_prod.T_SEGMENT set STATUS='20' where CAL_COUNT = -1;
commit;




