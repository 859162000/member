package com.wanda.ccs.jobhub.member.service.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.service.CreateCardService;
import com.wanda.ccs.jobhub.member.vo.CardBean;
import com.wanda.ccs.jobhub.member.vo.CardOrderBean;
public class CreateCardServiceImpl implements CreateCardService {

	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	@Transactional
	public void updateCreateCard()  {
		
		//获取所有的已经审批的并且等待生成卡号的万人迷卡申请单
		StringBuilder bsql=new StringBuilder();
		bsql.append("SELECT " );
		bsql.append("cardorder.MACK_DADDY_CARD_ORDER_ID, ");
		bsql.append("cardorder.REGION_CODE, ");
		bsql.append("cardorder.CINEMA_ID, ");
		bsql.append("cardorder.NUMBER_OF_CARDS, ");
		bsql.append("cardorder.START_NO, ");
		bsql.append("cardorder.END_NO ");
		bsql.append("FROM T_MACK_DADDY_CARD_ORDER cardorder ");
		bsql.append("WHERE cardorder.STATUS= 'P' and cardorder.DEAL_STATUS='W' ");
		bsql.append("ORDER BY cardorder.MACK_DADDY_CARD_ORDER_ID");

		List<CardOrderBean> rslist = getJdbcTemplate().query(
				bsql.toString(),
				new RowMapper<CardOrderBean>() {
					public CardOrderBean mapRow(ResultSet rs, int rowNum) throws SQLException {
						 CardOrderBean cardOrderBean = new CardOrderBean(); 
						 cardOrderBean.setOrderID(rs.getLong("MACK_DADDY_CARD_ORDER_ID"));
						 cardOrderBean.setRegionCode(rs.getString("REGION_CODE"));
						 cardOrderBean.setCinemaId(rs.getLong("CINEMA_ID"));
						 cardOrderBean.setNumberOfCards(rs.getLong("NUMBER_OF_CARDS"));
						 cardOrderBean.setStartNo(rs.getString("START_NO"));
						 cardOrderBean.setEndNo(rs.getString("END_NO"));
						return cardOrderBean;
					}
				});
		
		StringBuilder updateSql =new StringBuilder();
		updateSql.append("update T_MACK_DADDY_CARD_ORDER cardorder set cardorder.DEAL_STATUS='P' where cardorder.STATUS= 'P' and cardorder.DEAL_STATUS='W'");
		getJdbcTemplate().update(updateSql.toString());
		List<CardBean> cardList = new ArrayList<CardBean>();
		StringBuilder overUpdateSql =new StringBuilder();
		
//		overUpdateSql.append("update T_MACK_DADDY_CARD_ORDER cardorder set cardorder.DEAL_STATUS='S' where cardorder.MACK_DADDY_CARD_ORDER_ID in (");
		//根据获取到的制卡申请单生成需要插入的卡列表
		if(rslist != null && !rslist.isEmpty()){
			for(int i = 0 ; i < rslist.size() ; i++){
				CardOrderBean order = rslist.get(i);
				overUpdateSql.append(order.getCinemaId());
				if(i<rslist.size()-1)
					overUpdateSql.append(",");
				for(int j=0;j<order.getNumberOfCards();j++){
					CardBean card = new CardBean();
					card.setCardNumber(String.valueOf((Long.valueOf(order.getStartNo())+j)));
					card.setStatus("I");
					card.setCinemaId(order.getCinemaId());
					card.setOrderID(order.getOrderID());
					cardList.add(card);
				}
			}
		}
		try {
			//批量插入卡列表
			this.saveProperty(cardList);
			if(!overUpdateSql.toString().equals("")){
				overUpdateSql.insert(0, "update T_MACK_DADDY_CARD_ORDER cardorder set cardorder.DEAL_STATUS='S' where cardorder.MACK_DADDY_CARD_ORDER_ID in (");
				overUpdateSql.append(")");
				getJdbcTemplate().update(overUpdateSql.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			overUpdateSql.insert(0, "update T_MACK_DADDY_CARD_ORDER cardorder set cardorder.DEAL_STATUS='E' where cardorder.MACK_DADDY_CARD_ORDER_ID in (");
			overUpdateSql.append(")");
			getJdbcTemplate().update(overUpdateSql.toString());
			e.printStackTrace();
		}
	}
	
	public void saveProperty(final List<CardBean> list)throws Exception {
		String sql = " insert into t_mack_daddy_card(mack_daddy_card_id, mack_daddy_card_order_id, cinema_id, card_number,create_date,update_date,version,status) values (S_T_MACK_DADDY_CARD.NEXTVAL, ?, ?, ?,?,?, '0', 'I') ";
		  try{  
			  getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter(){  
					public int getBatchSize() {  
					    return list.size();  
					}  
					public void setValues(PreparedStatement pst, int i) throws SQLException {  
						CardBean card = list.get(i);
						pst.setLong(1, card.getOrderID());
						pst.setLong(2, card.getCinemaId());
						pst.setString(3, card.getCardNumber());
						pst.setDate(4,new Date(System.currentTimeMillis()));
						pst.setDate(5,new Date(System.currentTimeMillis()));
					}	  
		        });  
		    }catch(Exception e){  
		        e.printStackTrace();  
		        throw new Exception(e.getMessage());  
		    }  
	}
	
}
