-- Alter table 
alter table T_POS_GOODS_REFUND_ORDER
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table T_POS_GOODS_REFUND_ORDER add channel varchar2(10);
-- Add comments to the columns 
comment on column T_POS_GOODS_REFUND_ORDER.channel
  is '销售渠道';
  

-- Alter table 
alter table T_RT_POS_GOODS_REFUND_ORDER
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table T_RT_POS_GOODS_REFUND_ORDER add channel varchar2(10);
-- Add comments to the columns 
comment on column T_RT_POS_GOODS_REFUND_ORDER.channel
  is '销售渠道';
  
  

-- Alter table 
alter table T_POS_DETAIL_GOODS_ORDER
  storage
  (
    next 8
  )
;
-- Add/modify columns 
alter table T_POS_DETAIL_GOODS_ORDER add channel varchar2(10);
-- Add comments to the columns 
comment on column T_POS_DETAIL_GOODS_ORDER.channel
  is '销售渠道';


-- Alter table 
alter table T_RT_POS_DETAIL_GOODS_ORDER
  storage
  (
    next 1
  )
;
-- Add/modify columns 
alter table T_RT_POS_DETAIL_GOODS_ORDER add channel varchar2(10);
-- Add comments to the columns 
comment on column T_RT_POS_DETAIL_GOODS_ORDER.channel
  is '销售渠道';
