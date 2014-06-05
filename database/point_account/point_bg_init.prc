create or replace procedure point_bg_init is

  V_point_his_id_start number(30);
  V_point_his_id_end   number(30);
  v_cur_year_num       number(4) := to_number(to_char(sysdate, 'yyyy'));
  v_cur_year_char      varchar2(4) := to_char(sysdate, 'yyyy');
  v_cur_last_second    date := trunc(add_months(sysdate, 12), 'yyyy') - 1 / 86400;
  v_last_year_char     varchar2(4):= to_char(add_months(sysdate,-12),'yyyy');
begin

  -----����Ǯ�����㴥����
  execute immediate 'alter trigger TI_POINT_BAG disable';
  -----ȡ������������ʼʱ��Ļ�Ա������ʷid������������Ҫ�������ֵĻ�Աid
  execute immediate 'truncate table t_member_exg_point';
  ----------------���Ƚ�������������д��Ǯ������Ϊ����Ǯ��--
  select max(t.point_history_id)
    into V_point_his_id_start
    from t_point_history t;

  insert into t_member_exg_point
    select to_number(to_char(a.exchange_point_expire_time, 'yyyy')) *
           100000000000 + a.member_id,
           a.member_id,
           sum(a.exchange_point),
           to_char(a.set_time, 'yyyy'),
           max(a.exchange_point_expire_time),
           '0',
           'init_system',
           sysdate,
           'init_system',
           sysdate,
           '1'
      from t_point_history a
     where a.set_time >= trunc(sysdate, 'yyyy')
       and a.exchange_point > 0
       and a.adj_resion not like '%����%'
     group by to_number(to_char(a.exchange_point_expire_time, 'yyyy')) *
              100000000000 + a.member_id,
              a.member_id,
              to_char(a.set_time, 'yyyy');
  commit;

  ------����Ǯ����ͳ����Ϣ����oracle�ں���������ѡ������ִ�мƻ�
  execute immediate 'analyze table t_member_exg_point compute statistics';

  commit;

  ---------

  insert into t_member_exg_point
    select v_cur_year_num * 100000000000 + a.member_id,
           a.member_id,
           a.exg_point_balance - nvl(b.point_balance, 0),
           v_last_year_char,
           v_cur_last_second,
           '0',
           'init_system',
           sysdate,
           'init_system',
           sysdate,
           1
      from t_member_point a
      left outer join
    
     (select * from t_member_exg_point where gain_year = v_cur_year_char)
    
    b
        on a.member_id = b.member_id;

  commit;

  merge into t_member_exg_point aa
  using (select t.member_id, t.point_balance
           from t_member_exg_point t
          where t.gain_year = v_last_year_char
            and t.point_balance < 0) bb
  
  on (aa.member_id = bb.member_id and aa.gain_year = v_cur_year_char)
  when matched then
    update set aa.point_balance = aa.point_balance + bb.point_balance;

  commit;

  update t_member_exg_point t
     set t.point_balance = 0
   where t.gain_year = v_last_year_char
     and t.point_balance < 0;

  commit;

  -----------------����������ɺ�����������---------------------
  execute immediate 'alter trigger TI_POINT_BAG enable';

  ----------------ȡ�ò���������Ļ�Ա������ʷid�������ж����������ڼ���Ӱ���Ա---
  select max(t.point_history_id)
    into V_point_his_id_end
    from t_point_history t;

  ----------------�ڶ�����-�޲����������ڼ�����䶯�Ļ�Ա����--��ʽΪ����Ӱ��Ļ�Ա����Ǯ��ɾ����ԭ�㷨���¼���--
  delete t_member_exg_point t
   where t.member_id in
         (select distinct t.member_id
            from t_point_history t
           where t.point_history_id between V_point_his_id_end and
                 V_point_his_id_start);

  insert into t_member_exg_point
    select to_number(to_char(a.exchange_point_expire_time, 'yyyy')) *
           100000000000 + a.member_id,
           a.member_id,
           sum(a.exchange_point),
           to_char(a.set_time, 'yyyy'),
           max(a.exchange_point_expire_time),
           '0',
           'init_system',
           sysdate,
           'init_system',
           sysdate,
           '1'
      from t_point_history a
     where a.set_time >= trunc(sysdate, 'yyyy')
       and a.exchange_point > 0
       and a.adj_resion not like '%����%'
       and a.member_id in
           (select distinct t.member_id
              from t_point_history t
             where t.point_history_id between V_point_his_id_end and
                   V_point_his_id_start)
     group by to_number(to_char(a.exchange_point_expire_time, 'yyyy')) *
              100000000000 + a.member_id,
              a.member_id,
              to_char(a.set_time, 'yyyy');

  insert into t_member_exg_point
    select v_cur_year_num * 100000000000 + a.member_id,
           a.member_id,
           a.exg_point_balance - nvl(b.point_balance, 0),
           v_last_year_char,
           v_cur_last_second,
           '0',
           'init_system',
           sysdate,
           'init_system',
           sysdate,
           1
      from (select *
              from t_member_point
             where member_id in
                   (select distinct t.member_id
                      from t_point_history t
                     where t.point_history_id between V_point_his_id_end and
                           V_point_his_id_start)) a
      left outer join (select *
                         from t_member_exg_point
                        where gain_year = v_cur_year_char) b
        on a.member_id = b.member_id;

  merge into t_member_exg_point aa
  using (select t.member_id, t.point_balance
           from t_member_exg_point t
          where t.gain_year = v_last_year_char
            and t.point_balance < 0
            and t.member_id in
                (select distinct n.member_id
                   from t_point_history n
                  where n.point_history_id between V_point_his_id_end and
                        V_point_his_id_start)
         
         ) bb
  
  on (aa.member_id = bb.member_id and aa.gain_year = v_cur_year_num)
  when matched then
    update set aa.point_balance = aa.point_balance + bb.point_balance;

  update t_member_exg_point t
     set t.point_balance = 0
   where t.gain_year = v_last_year_char
     and t.point_balance < 0
     and t.member_id in
         (select distinct n.member_id
            from t_point_history n
           where n.point_history_id between V_point_his_id_end and
                 V_point_his_id_start);

end;
/
