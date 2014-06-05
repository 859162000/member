create or replace trigger TI_point_bag
  before insert on t_point_history
  for each row
declare
  -- local variables here
begin

  if :new.point_type != '5'
   then
    if :new.exchange_point < 0 then
      ----如果是扣减积分，需判断钱包余额
      merge into T_MEMBER_EXG_POINT d
      using
      
       (select member_exg_point_id,
               point_balance - (case
                 when :new.exchange_point * -1 - sum(lst) over(order by expire_id asc) < 0 then
                  0
                 else
                  least(:new.exchange_point  * -1 - sum(lst) over(order by expire_id asc),point_balance)
               end) des_pb,
               MEMBER_ID,
               expire_id
          from (select mep_dest.member_exg_point_id,
                       mep_dest.point_balance,
                       mep_dest.MEMBER_ID,
                       to_char(mep_dest.EXPIRE_TIME,'YYYY') expire_id,
                       nvl(lag(mep_dest.point_balance)
                           over(order by to_char(mep_dest.EXPIRE_TIME,'YYYY') asc),
                           0) lst
                  from T_MEMBER_EXG_POINT mep_dest
                 where mep_dest.member_id = :new.member_id
                   and mep_dest.point_balance > 0
                   and mep_dest.expired = 0)) s
      
      on (s.MEMBER_ID = d.MEMBER_ID  and  s.expire_id = to_char(d.EXPIRE_TIME,'YYYY') )
      when matched then
        update
           set d.point_balance = s.des_pb,
               d.update_date   = case
                                   when d.point_balance = s.des_pb then
                                    d.update_date
                                   else
                                    sysdate
                                 end;
      ---------------------------------------扣减积分操作结束
    elsif (:new.exchange_point > 0  and   :new.exchange_point_expire_time > sysdate) ----如果是失效就不操作直接放行,如果增加的积分有效期已经在失效范围,则放弃操作  then
      -----如果是增加积分则需要判断钱包账户是否存在，这里用merge直接处理完毕
    then
      merge into T_MEMBER_EXG_POINT MEP
      using (select :new.MEMBER_ID MEMBER_ID,
                    :NEW.EXCHANGE_POINT_EXPIRE_TIME EXCHANGE_POINT_EXPIRE_TIME
               from dual) TTB
      on (MEP.MEMBER_ID = TTB.MEMBER_ID and MEP.EXPIRE_TIME = TTB.EXCHANGE_POINT_EXPIRE_TIME)
      when matched then
        update
           set POINT_BALANCE = POINT_BALANCE + :new.EXCHANGE_POINT,
               update_date   = sysdate
      when not matched then
        insert
          (MEMBER_EXG_POINT_ID,
           MEMBER_ID,
           POINT_BALANCE,
           GAIN_YEAR,
           EXPIRE_TIME,
           EXPIREd,
           UPDATE_DATE,
           UPDATE_BY,
           CREATE_DATE,
           CREATE_BY,
           VERSION)
        values
          (to_number(to_char(:NEW.EXCHANGE_POINT_EXPIRE_TIME, 'YYYY')) *
           100000000000 + :new.MEMBER_ID,
           :new.MEMBER_ID,
           :new.EXCHANGE_POINT,
           to_char(:new.set_time,'yyyy'),
           :new.EXCHANGE_POINT_EXPIRE_TIME,
           '0',
           systimestamp,
           'member_system',
           systimestamp,
           'member_system',
           1);
    
      -----------------增加积分操作结束
    
    end if; ----积分加减判断结束
  
  end if; ------积分类型判断结束

end TI_point_bag;
/
