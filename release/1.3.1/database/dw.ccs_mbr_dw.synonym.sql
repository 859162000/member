
--创建会员级别表t_member_level的同义词, 连接到DW_ODS数据库。
create or replace synonym CCS_MBR_DW.T_MEMBER_LEVEL
  for CCS_MBR_DW.T_MEMBER_LEVELT@OPLDW;
