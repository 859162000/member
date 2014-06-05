package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberLevelDao;
import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistoryArchiveDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.MemberLevelDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MemberPointDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistoryArchiveDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistoryArchive;
import com.wanda.mms.control.stream.vo.PointHistroy;

/**
 * 积分清零
 * @author wangshuai
 * @date 2013-05-23	
 */
public class IntegralReset {
	static Logger logger = Logger.getLogger(IntegralReset.class.getName());

	Dimdef dimdef = new Dimdef();
	public static void main(String[] args) {
		SENDDBConnection db=SENDDBConnection.getInstance();	
	    Connection conn = null;
		conn=db.getConnection();
		IntegralReset ir = new IntegralReset();
		Date da = new Date();
		String date = DateUtil.getDateStr(da);//时间可能是 时分秒
		String yy = date.substring(0,4);
		String mm = date.substring(4,6);
		int aa = Integer.valueOf(yy);
		aa=aa-1;
		String yyyy = String.valueOf(aa);
		String eqyy = yyyy+"1231";
		String ymd = yy+"1231"; 
		//System.out.println(ymd);
		
		if(mm.equals("01")){
			//System.out.println("月份错误执行积分清零调度会有风险");
			//可能以错过积分清零功能的正常调用时间每年的12月31日 
			ir.IntegralResetDispose(conn, eqyy);//调用计分清零的计算功能，计算明年每个会员的积分值，进行清洗并替换。
			ir.archivePointHistory(conn, eqyy);//把积分有效期是今年的会员积分数据清理并归档。
			ir.pointTransType(conn);//最后把所有积分清零数据归档。
		}else{
			ir.IntegralResetDispose(conn, ymd);
			ir.archivePointHistory(conn, ymd);
			ir.pointTransType(conn);
		}
		 
	}

 /**
  * 调用计分清零的计算功能，计算明年每个会员的积分值，进行清洗并替换。
  * @param conn
  * @param date
  * @return
  */
	public String IntegralResetDispose(Connection conn,String date){
	String ss ="";
	String yy = (String) date.subSequence(0, 4);//得到时间参数中的年份
	int intyy = Integer.valueOf(yy);
	intyy = intyy +1;//年份加1
	String yyyy = String.valueOf(intyy);
	String te = "0101";
	String time =yyyy+te; 
	String mmdd = "1231";
	String ymd = yyyy+mmdd;
	
	PointHistroyDao pointDao = new PointHistroyDaoImpl();;
	MemberPointDao memdao = new MemberPointDaoImpl();
	//得到可兑换积分有效期是明年会员的总条数
	Long totalNum = pointDao.fandPointHistroyResetTotalNum(conn, ymd);//
	Page page = new Page();
    StringBuffer sb = new StringBuffer();
	long pageSize =40;
	long pageNo = 0;
	while (totalNum>0) {
		if(pageNo==0){
			page.setPage(pageNo*pageSize);
		}else{
			page.setPage((pageNo*pageSize)+1);
		}
		page.setPageSize(pageSize +page.getPageSize());
		List<MemberPoint> mplist = pointDao.fandPointHistroyResetPage(conn, date, page);
		for (int i = 0; i < mplist.size(); i++) {
			MemberPoint mp = mplist.get(i);//得到当前会员明年累计的总积分
			MemberPoint memp =	memdao.fandMemberPointByID(conn, mp.getMemberId());//得到当前会员可用积分
			long balance = mp.getActivity_Point()+mp.getLevel_Point_Total();
			//如果会员明年的定级积分加非定级积分大于等于0并且大于等于
			if(balance>=0&&balance>=memp.getExg_Point_Balance()&&memp.getExg_Point_Balance()>=0){
				long a = memp.getExg_Point_Balance()-memp.getExg_Expire_Point_Balance();
				PointHistroy ph = new PointHistroy();//积分历史
				ph.setMemberid(mp.getMemberId());
				Date dateTime = new Date();
				String dd = DateUtil.getDateStrss(dateTime);
				ph.setSetTime(dd);//积分操作时间#SET_TIME
				ph.setLevel_Point(0);//定级积分
				ph.setTicket_Count(0);//升降级判定票数
				ph.setActivity_Point(0);//非定级积分
				ph.setExchange_Point(0);//把得到的当年的剩余积分减掉，
				//-mp.getExg_Expire_Point_Balance() 
				ph.setOrg_Point_Balance(memp.getExg_Point_Balance());//变化前可兑换积分余额#ORG_POINT_BALANCE
				ph.setExg_expire_point_balance(a);//当年即将过期可兑换积分
				ph.setExchange_Point_Expire_Time(ymd);//可兑换积分有效期#EXCHANGE_POINT_EXPIRE_TIME
				ph.setPoint_Balance(memp.getExg_Point_Balance());//变化后可兑换积分余额#POINT_BALANCE
				ph.setPoint_Type(dimdef.POINT_TYPE_TZ);//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
				ph.setPoint_Sys(dimdef.POINT_SYS_HYXT);//维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
				ph.setPoint_Trans_Type(dimdef.POINT_TRANS_TYPE_JFQL);//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
				ph.setPoint_Trans_Code("0");//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
				ph.setPoint_Trans_Code_Web("0");//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
				ph.setAdj_Resion("积分清零");
				ph.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ph.setCreate_By("member_sys");
				ph.setUpdate_By("member_sys");
				ph.setVersion(1);
				
				String vv = pointDao.addPointHistroy(conn, ph);
				sb.append(vv);
				
			
			
			}else{
				
				if(mp.getExg_Expire_Point_Balance()<0){
					long syjf = 	memp.getExg_Point_Balance();
					long eepb = (memp.getExg_Point_Balance()-syjf)-memp.getExg_Expire_Point_Balance();
					PointHistroy ph = new PointHistroy();
					//ph.setPointHistoryid(pointHistoryid)
					ph.setMemberid(mp.getMemberId());
					Date dateTime = new Date();
					String dd = DateUtil.getDateStrss(dateTime);
					ph.setSetTime(dd);
					ph.setLevel_Point(0);
					ph.setTicket_Count(0);
					ph.setActivity_Point(0);
					ph.setExchange_Point(-syjf);//把得到的当年的剩余积分减掉，
					//-mp.getExg_Expire_Point_Balance()
					
					ph.setOrg_Point_Balance(memp.getExg_Point_Balance());//变化前可兑换积分余额#ORG_POINT_BALANCE
					ph.setExg_expire_point_balance(-eepb);//当年即将过期可兑换积分
					ph.setExchange_Point_Expire_Time(ymd);
					ph.setPoint_Balance(memp.getExg_Point_Balance()-syjf);
			
					ph.setPoint_Type(dimdef.POINT_TYPE_TZ);//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
					ph.setPoint_Sys(dimdef.POINT_SYS_HYXT);//维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
					ph.setPoint_Trans_Type(dimdef.POINT_TRANS_TYPE_JFQL);//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
						ph.setPoint_Trans_Code("");//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
					ph.setPoint_Trans_Code_Web("");//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
					ph.setAdj_Resion("积分清零问题数据");
					ph.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法
					ph.setCreate_By("member_sys");
					ph.setUpdate_By("member_sys");
					ph.setVersion(1);
					String vv = pointDao.addPointHistroy(conn, ph);
					sb.append(vv);
				}else{
				//插入一条数据 把积分清一下 &mp.getExg_Expire_Point_Balance()>memp.getExg_Expire_Point_Balance()
					long syjf = 	memp.getExg_Point_Balance()-balance;
					
				//	System.out.println(memp.getExg_Point_Balance()+"-"+balance+"="+syjf);
					long eepb = (memp.getExg_Point_Balance()-syjf)-memp.getExg_Expire_Point_Balance();
				PointHistroy ph = new PointHistroy();
				//ph.setPointHistoryid(pointHistoryid)
				ph.setMemberid(mp.getMemberId());
				Date dateTime = new Date();
				String dd = DateUtil.getDateStrss(dateTime);
				ph.setSetTime(dd);//积分操作时间#SET_TIME
				ph.setLevel_Point(0);//定级积分#LEVEL_POINT
				ph.setTicket_Count(0);//升降级判定票数#TICKET_COUNT
				ph.setActivity_Point(0);//非定级积分#ACTIVITY_POINT
				ph.setExchange_Point(-syjf);//把得到的当年的剩余积分减掉，可兑换积分#EXCHANGE_POINT
	
				ph.setOrg_Point_Balance(memp.getExg_Point_Balance());//变化前可兑换积分余额#ORG_POINT_BALANCE
			
				ph.setExchange_Point_Expire_Time(ymd);//可兑换积分有效期#EXCHANGE_POINT_EXPIRE_TIME
				ph.setPoint_Balance(memp.getExg_Point_Balance()-syjf);//变化后可兑换积分余额#POINT_BALANCE
				ph.setExg_expire_point_balance(eepb);//过期积分
			
				ph.setPoint_Type(dimdef.POINT_TYPE_TZ);//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
				ph.setPoint_Sys(dimdef.POINT_SYS_HYXT);//维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
				ph.setPoint_Trans_Type(dimdef.POINT_TRANS_TYPE_JFQL);//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
					ph.setPoint_Trans_Code("");//单号(交易单号(POS/网站)/积分兑换交易单号/特殊积分规则ID)
				ph.setPoint_Trans_Code_Web("");//如果point_trans_type是交易,point_sys是网站，则在此记录网站订单号关联的POS订单
				ph.setAdj_Resion("积分清零");
				ph.setIsdelete(0);//逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ph.setCreate_By("member_sys");
				ph.setUpdate_By("member_sys");
				ph.setVersion(1);
				String vv = pointDao.addPointHistroy(conn, ph);
				sb.append(vv);
				}
			}
			
		}
		
		pageNo = pageNo+1;
		totalNum = totalNum - pageSize; 
		
	}
	ss = sb.toString();
	return ss;
	}
	
	/***
	 * 把积分有效期是今年的会员积分数据清理并归档。
	 * @param conn
	 * @param date
	 * @return
	 */
	public String archivePointHistory(Connection conn,String date){
		String ss="" ;
		 
		//String date = DateUtil.getDateStr(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy -1;
		String yyyy = String.valueOf(intyy);
		String te = "0101";
		String time =yyyy+te; 
		String mmdd = "1231";
		String ymd = yyyy+mmdd;

		SENDDBConnection db=SENDDBConnection.getInstance();	
	  //  Connection conn = null;
		conn=db.getConnection();
		 
		PointHistroyDao pointDao = new PointHistroyDaoImpl();;
		
		MemberPointDao memdao = new MemberPointDaoImpl();
		String sup ="";
		StringBuffer sbup = new StringBuffer();
		//得到总条数
		Long totalNum = pointDao.fandPointHistroyResetTotal(conn, date);
		Page page = new Page();  
		long pageSize =40;
		long pageNo = 0;
		while (totalNum>0) {
			page.setPage(pageNo);
			page.setPageSize(pageSize);

			List<MemberPoint> mplist = pointDao.fandPointHistroyResetPage(conn, ymd, page);
			for (int i = 0; i < mplist.size(); i++) {
				MemberPoint mp = mplist.get(i);
				MemberPoint memp =	memdao.fandMemberPointByID(conn, mp.getMemberId());
 				List<PointHistroy> phlist = pointDao.fandPointHistroyByExchange_point_expire_time(conn, date, mp.getMemberId());
 				String strup =archive(conn,phlist);// upMemberLevel(conn,mp,phlist);
 				sbup.append(strup).append("\n");	 
				}
			
		//	pageNo = pageNo+1;
			totalNum = totalNum - pageSize; 
			
		}
		ss = sbup.toString();
		 
		return ss;
		
	}
	
	/***
	 * 把所有积分清零数据归档
	 * @param conn
	 * @return
	 */
	public String pointTransType(Connection conn){
		String ss="" ;
		 
		//String date = DateUtil.getDateStr(da);//时间可能是 时分秒
		 

		SENDDBConnection db=SENDDBConnection.getInstance();	
	  //  Connection conn = null;
		conn=db.getConnection();
		 
		PointHistroyDao pointDao = new PointHistroyDaoImpl();;
		
		MemberPointDao memdao = new MemberPointDaoImpl();
		String sup ="";
		StringBuffer sbup = new StringBuffer();
		//得到总条数
		String Point_Trans_Type  = dimdef.POINT_TRANS_TYPE_JFQL; //4积分清零;
	
		Long totalNum = pointDao.fandPointHistroyByPoint_Trans_Type(conn, Point_Trans_Type);
		Page page = new Page();  
		long pageSize =50;
		long pageNo = 0;
		while (totalNum>0) {
			page.setPage(pageNo);
			page.setPageSize(pageSize);
 
			List<PointHistroy> phlisttype = pointDao.fandPointHistroyByPage(conn, Point_Trans_Type, page);
 					String  trans_Type = archive(conn,phlisttype);
 					sbup.append(trans_Type).append("\n");
		//	pageNo = pageNo+1;
			totalNum = totalNum - pageSize; 
			
		}
		ss = sbup.toString();
		 
		return ss;
		
	}
	
	/***
	 * 数据归档
	 * @param conn
	 * @param phlist
	 * @return
	 */
	public String archive(Connection conn,List<PointHistroy> phlist ){
		PointHistoryArchiveDao phadao = new  PointHistoryArchiveDaoImpl();
		PointHistroyDao phdao = new	PointHistroyDaoImpl();
		String ss ="";
		StringBuffer phasb = new StringBuffer();
		for (int i = 0; i < phlist.size(); i++) {
			PointHistroy ph = phlist.get(i);
			PointHistoryArchive pa = new PointHistoryArchive();
			pa.setPointHistoryid(ph.getPointHistoryid());
			pa.setMemberid(ph.getMemberid());
			pa.setSetTime(ph.getSetTime());
			pa.setLevel_Point(ph.getLevel_Point());
			pa.setTicket_Count(ph.getTicket_Count());
			pa.setActivity_Point(ph.getActivity_Point());
			pa.setExchange_Point(ph.getExchange_Point());
			pa.setExchange_Point_Expire_Time(ph.getExchange_Point_Expire_Time());
			pa.setPoint_Type(ph.getPoint_Type());
			pa.setPoint_Sys(ph.getPoint_Sys());
			pa.setPoint_Trans_Type(ph.getPoint_Trans_Type());
			pa.setPoint_Trans_Code(ph.getPoint_Trans_Code());
			pa.setPoint_Trans_Code_Web(ph.getPoint_Trans_Code_Web());
			pa.setAdj_Resion(ph.getAdj_Resion());
			pa.setOrg_Point_Balance(ph.getOrg_Point_Balance());
			pa.setPoint_Balance(ph.getPoint_Balance());
			pa.setIs_Sync_Balance(ph.getIs_Sync_Balance());
			pa.setIsdelete(ph.getIsdelete());
			pa.setCreate_By(ph.getCreate_By());
			pa.setCreate_Date(ph.getCreate_Date());
			pa.setUpdate_By(ph.getUpdate_By());
			pa.setUpdate_Date(ph.getUpdate_Date());
			pa.setVersion(ph.getVersion());
			pa.setArchieve_By("member_sys");
			pa.setArchive_Time("");
			pa.setMember_point_id(ph.getMember_point_id());
			pa.setAdj_reason_type(ph.getAdj_reason_type());
			pa.setAdj_reason(ph.getAdj_reason());
			pa.setOrder_id(ph.getOrder_id());
			pa.setProduct_name(ph.getProduct_name());
			pa.setIs_succeed(ph.getIs_succeed());
			pa.setCinema_inner_code(ph.getCinema_inner_code());
			
			int flag = phadao.addPointHistoryArchive(conn, pa);
			//归档并删除历史表中的数据		
			if(flag==1){
			int cc = phdao.delPointHistroyReset(conn, ph.getPointHistoryid());
			phasb.append(cc);
			}
			phasb.append(flag);
		}
		ss = phasb.toString();
		return ss;
		
	}
	
	
	
}
