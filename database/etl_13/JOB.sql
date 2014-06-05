begin
  sys.dbms_job.submit(job => :job,
                      what => 'P_SYS_MBR_BASE_SYNC(TO_CHAR(SYSDATE-1,''YYYYMMDD''));',
                      next_date => to_date('23-11-2013 06:05:00', 'dd-mm-yyyy hh24:mi:ss'),
                      interval => 'TRUNC(SYSDATE+1)+6/24+05/1440');
  commit;
end;
/