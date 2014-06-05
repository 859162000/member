package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.MemberPointDao;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.MemberPointDaoImpl;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;

/**
 *	积分调整
 * @author wangshuai
 * @date 2013-05-29	
 */
public class PointAdjust {
	static Logger logger = Logger.getLogger(PointAdjust.class.getName());

	Dimdef dimdef = new Dimdef();
	
	public String adjust(Connection conn,PointHistroy ph) {
		return doAdjust( conn, ph, true);
	}
	
	public String adjustNoCommit(Connection conn,PointHistroy ph) {
		return doAdjust( conn, ph, false);
	}
	
	//积分调整方法  
	public String doAdjust(Connection conn,PointHistroy ph, boolean commit){
		//传参 会员ID，积分数，调整日期，调整原因，调整类型
		String ss = "";
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		String dt = yy+te;
		PointHistroyDao phdao = new PointHistroyDaoImpl();//得到会员积分历史DAO
		MemberPointDao mpdao = new MemberPointDaoImpl();//会员积分DAO
		PointHistroyDao dao = new PointHistroyDaoImpl();
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//	    Connection conn = null;
//		conn=db.getConnection();
		
		MemberPoint mp = mpdao.fandMemberPointByID(conn, ph.getMemberid());//查看会员积分
		
		//ph.setSetTime(date);
		//ph.setTicket_Count(0);
		//ph.setActivity_Point(0);
		ph.setOrg_Point_Balance(mp.getExg_Point_Balance());//得到可兑换积分余额#EXG_POINT_BALANCE 放入  变化前可兑换积分余额#ORG_POINT_BALANCE
		
		ph.setPoint_Balance(mp.getExg_Point_Balance()+ph.getLevel_Point()+ph.getActivity_Point());
		//ph.setPoint_Type("4");//维数据
		ph.setPoint_Sys(dimdef.POINT_SYS_HYXT);
		
		ph.setIsdelete(0);//逻辑删除标识#ISDELETE
		ph.setVersion(1);//版本号#VERSION
		
		if(commit) {
			phdao.addPointHistroy(conn, ph);
		} else {
			phdao.addPointHistroyNotAutoCommit(conn, ph);
		}
		
		
		//调整积分
		return ss; 
	}
	//批量积分调整方法
	public String batchAdjust(List<PointHistroy> phlist){
		String ss = "";
		
		
		
		//调整积分
		return ss;
		
	}
	
}
