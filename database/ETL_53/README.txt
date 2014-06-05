这个是在 10.199.90.53/pldw/CCS_MBR_DW上的存储过程。
主要用于从10.199.90.13/pldw 底表库导数据到CCS_ODS中。
导的表有 T_FILE, T_FT_TYPE 两个表。


执行逻辑
================
1.通过JOB.sql中的job在每天6:05分跑，同步上一交易日的数据。
2.下面两个存储过程分别是用来记录错误日志，任务执行历史。
  P_SYS_SP_FN_ERR_LOG
  P_SYS_TASK_SCHEDULING_LOG
  吧错误和任务执行历史记录到表：
  T_SYS_SP_FN_ERR_LOG
  T_SYS_TASK_SCHEDULING_LOG