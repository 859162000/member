这个是在 10.199.90.13/pldw/CCS_MBR_DW上的存储过程。
主要用于从MBR_APP导数据到CCS_RAW


执行逻辑
================
1.通过JOB.sql中的job在每天6:05分跑，同步上一交易日的数据。
2.JOB会调用P_SYS_MBR_BASE_SYNC 存储过程。
3.P_SYS_MBR_BASE_SYNC会调用
   P_SYS_CAMPAIGN_SYNC
   P_SYS_MBR_SYNC
   进行实际同步。
4.下面两个存储过程分别是用来记录错误日志，任务执行计划的。
  P_SYS_SP_FN_ERR_LOG
  P_SYS_TASK_SCHEDULING_LOG

注：
除上面提到的存储过程外，其他的存储过程为废弃的。