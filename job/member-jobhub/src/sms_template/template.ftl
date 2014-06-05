<#if type == 'line'>
各位领导，以下为万达会员系统${yyyyMMdd[4..5]}月${yyyyMMdd[6..7]}日院线数据统计，请查收。
<#else>
各位领导，以下为万达会员系统${yyyyMMdd[4..5]}月${yyyyMMdd[6..7]}日${innerName}数据统计，请查收。
</#if>
新增会员数：${NewMember}(${NewMemberSum})
线上入会数：${NetMember}(${NetMemberSum})
线下入会数：${NotNetMember}(${NotNetMemberSum})
线上票房消费金额：${NetTicketSum}元(本月累计${NetTicketMonthSum}元，本年累计${NetTicketYearSum}元)
线下票房消费金额：${NotNetTicketSum}元(本月累计${NotNetTicketMonthSum}元，本年累计${NotNetTicketYearSum}元)
卖品消费金额：${NotNetGoodsSum}元
可兑换积分增加：${ExchangePointnew}
可兑换积分兑换：${ExchangePoint}
可兑换积分余额：${ExchangePointBalance}