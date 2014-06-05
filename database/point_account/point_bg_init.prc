create or replace procedure point_bg_init is

  V_point_his_id_start number(30);
  V_point_his_id_end   number(30);
  v_cur_year_num       number(4) := to_number(to_char(sysdate, 'yyyy'));
  v_cur_year_char      varchar2(4) := to_char(sysdate, 'yyyy');
  v_cur_last_second    date := trunc(add_months(sysdate, 12), 'yyyy') - 1 / 86400;
  v_last_year_char     varchar2(4):= to_char(add_months(sysdate,-12),'yyyy');
begin

  -----禁用钱包计算触发器
  execute immediate 'alter trigger TI_POINT_BAG disable';
  -----取出批量计算起始时间的会员积分历史id，用于评定需要修正积分的会员id
  execute immediate 'truncate table t_member_exg_point';
  ----------------首先将当年新增积分写入钱包表，作为当年钱包--
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
       and a.adj_resion not like '%冲正%'
     group by to_number(to_char(a.exchange_point_expire_time, 'yyyy')) *
              100000000000 + a.member_id,
              a.member_id,
              to_char(a.set_time, 'yyyy');
  commit;

  ------更新钱包表统计信息避免oracle在后续操作中选择错误的执行计划
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

  -----------------批量计算完成后启动触发器---------------------
  execute immediate 'alter trigger TI_POINT_BAG enable';

  ----------------取得操作结束后的会员积分历史id，用来判定批量计算期间受影响会员---
  select max(t.point_history_id)
    into V_point_his_id_end
    from t_point_history t;

  ----------------第二部分-修补批量计算期间产生变动的会员积分--方式为将受影响的会员积分钱包删掉按原算法重新计算--
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
       and a.adj_resion not like '%冲正%'
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
