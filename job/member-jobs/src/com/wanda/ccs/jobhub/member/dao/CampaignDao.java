package com.wanda.ccs.jobhub.member.dao;

import static com.wanda.ccs.member.segment.SegmentConstants.Schemas.MBRODS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.vo.CampaignDetailVo;
import com.wanda.ccs.jobhub.member.vo.CampaignVo;

/**
 * 
 * @author ZR
 *
 */
public class CampaignDao {
	
	public final static int MEMBER_NUMBER = 50000;
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	@InstanceIn(path = "/dataSourceOds")
	private DataSource dataSourceOds;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	private ExtJdbcTemplate extJdbcTemplateOds = null;
	
	private ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	private ExtJdbcTemplate getJdbcTemplateOds()  {
		if(this.extJdbcTemplateOds == null) {
			this.extJdbcTemplateOds = new ExtJdbcTemplate(dataSourceOds);
		}
		return this.extJdbcTemplateOds;
	}
	
	private String[] parseSql(int count, int threadNum) {
		List<String> sql = new ArrayList<String>();
		//int threadNum = 4;
		
		int per = count / threadNum;
		System.out.println("平均：" + per);
		
		String s = null;
		for(int i=0; i<threadNum; i++) {
			int start, end;
			if(i == threadNum-1) { //最后一条分页sql
				start = i * per;
				end = count+1;
			} else { //分页sql
				start = i * per;
				end = (i+1) * per;
			}
			s = "SELECT * FROM (SELECT T.MEMBER_ID,T.CAMPAIGN_ID,T.IS_CONTROL,ROWNUM RN FROM T_CAMPAIGN_SEGMENT T WHERE ROWNUM<" + end + " and T.CAMPAIGN_ID=?) WHERE RN>=" + start;
			sql.add(s);
			s = null;
		}
		
		return sql.toArray(new String[] {});
	}
	
	/*public void sycnMemberSegment(Long campaignId) throws Exception {
		final String insert = "insert into "+MBRODS+".T_CAMPAIGN_SEGMENT(MEMBER_ID,CAMPAIGN_ID,IS_CONTROL) values(?,?,?)";
		
		int count = getJdbcTemplate().queryForInt("select count(T.MEMBER_ID) from T_CAMPAIGN_SEGMENT T where T.CAMPAIGN_ID=?", new Object[] { campaignId });
		if(count > MEMBER_NUMBER) {
			System.out.println("分析sql...");
			String[] sql = parseSql(count, 4);
			List<TaskInfo> task = new ArrayList<TaskInfo>();
			// 读信息
			System.out.println("创建读线程...");
			for(int i=0,len=sql.length;i<len;i++) {
				System.out.println(sql[i]);
				task.add(new TaskInfo(i, SyncType.READ, getJdbcTemplate(), sql[i], new Object[] {campaignId}));
			}
			// 写信息
			System.out.println("创建写线程...");
			task.add(new TaskInfo(sql.length, SyncType.WRITE, getJdbcTemplateOds(), insert, null));
			SyncTaskPool syncTaskPool = new SyncTaskPool(task);
			
			System.out.println("开始执行...");
			ExecStatus status = syncTaskPool.start();
			System.out.println("同步客群执行结果 " + status);
			if(status == ExecStatus.FAIL) {
				throw new Exception("客群同步执行失败");
			}
		} else {
			getJdbcTemplate().query("select t.MEMBER_ID,t.CAMPAIGN_ID,t.IS_CONTROL from T_CAMPAIGN_SEGMENT t where t.CAMPAIGN_ID=?", new Object[] { campaignId }, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					List<Object[]> param = new ArrayList<Object[]>();
					int size = 0;
					
					do {
						param.add(new Object[] {rs.getInt("MEMBER_ID"), rs.getInt("CAMPAIGN_ID"), rs.getString("IS_CONTROL")});
						size++;
						if(size > 4000) {
							getJdbcTemplateOds().batchUpdate(insert, param);
							param.clear();
							size=0;
						}
					} while(rs.next());
					
					if(size > 0) {
						getJdbcTemplateOds().batchUpdate(insert, param);
						param.clear();
					}
				}
			});
		//}
	}*/
	
	/**
	 * 将落地客群从52同步到53
	 * 
	 * @param campaignId
	 */
	public void sycnMemberSegment(Long campaignId) {
		final String sql = "insert into "+MBRODS+".T_CAMPAIGN_SEGMENT(MEMBER_ID,CAMPAIGN_ID,IS_CONTROL) values(?,?,?)";
		
		getJdbcTemplate().query("select distinct t.MEMBER_ID,t.CAMPAIGN_ID,t.IS_CONTROL from T_CAMPAIGN_SEGMENT t where t.CAMPAIGN_ID=?", new Object[] {campaignId}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				List<Object[]> args = new ArrayList<Object[]>();
				int size = 0;
				do {
					args.add(new Object[] {rs.getInt("MEMBER_ID"), rs.getInt("CAMPAIGN_ID"), rs.getString("IS_CONTROL")});
					size++;
					if(size > 4000) {
						getJdbcTemplateOds().batchUpdate(sql, args);
						args.clear();
						size=0;
					}
				} while(rs.next());
				
				if(size > 0) {
					getJdbcTemplateOds().batchUpdate(sql, args);
					args.clear();
				}
			}
		});
	}
	
	public int memberSegmentCount(Long campaignId) {
		return getJdbcTemplateOds().queryForInt("select count(t.member_id) from "+MBRODS+".T_CAMPAIGN_SEGMENT t where t.campaign_id=?", new Object[] { campaignId });
	}
	
	public void deleteMemberSegment(Long campaignId) {
		getJdbcTemplateOds().update("delete from "+MBRODS+".T_CAMPAIGN_SEGMENT t where t.campaign_id=?", new Object[] { campaignId });
	}
	
	/**
	 * 保存计算结果
	 * 
	 * @param vo
	 */
	public void updateCalResult(CampaignDetailVo vo) {
		StringBuilder sql = new StringBuilder();
		sql.append("update T_CAMPAIGN_DETAIL t set ")
		.append("t.recommend_num=?,t.control_num=?,t.sum_num=?,")//人数
		.append("t.recommend_rate=?,t.control_rate=?,t.sum_rate=?,")//比率
		.append("t.recommend_income=?,t.control_income=?,t.sum_income=?,")//收入
		//.append("t.point_exchange_recommend=?,t.point_exchange_control=?,t.point_exchange_sum=?,")//兑换积分
		.append("t.update_date=sysdate,")
		.append("t.status='20' ")
		.append("where t.campaign_id=? ")
		.append("and t.ymd=?")
		.append(" and t.type=?");
		
		getJdbcTemplate().update(sql.toString(), 
				new Object[] {vo.getRecommendNum(), vo.getControlNum(),vo.getSumNum(),
							vo.getRecommendRate(), vo.getControlRate(), vo.getSumRate(),
							vo.getRecommendIncome(), vo.getControlIncome(), vo.getSumIncome(),
							vo.getCampaignId(), vo.getYmd(), vo.getType()});
	}
	
	/**
	 * 更新计算状态
	 * 
	 * @param campaignId
	 * @param ymd
	 * @param status
	 */
	public void updateCalStatus(Long campaignId, int ymd, String status) {
		getJdbcTemplate().update("update T_CAMPAIGN_DETAIL t set t.status=? where t.campaign_id=? and t.ymd=?", new Object[] {status, campaignId, ymd});
	}
	
	/**
	 * 生成统计数据
	 * 
	 * @param list
	 */
	public int batchCreateCalStatus(List<CampaignDetailVo> list) {
		List<Object[]> args = new ArrayList<Object[]>();
		CampaignDetailVo vo = null;
		for(int i=0,len=list.size();i<len;i++) {
			vo = list.get(i);
			args.add(new Object[] {vo.getYmd(), vo.getType(), vo.getCampaignId()});
		}
		
		int[] index = getJdbcTemplate().batchUpdate("insert into T_CAMPAIGN_DETAIL(ID,YMD,TYPE,CAMPAIGN_ID) values(S_T_CAMPAIGN_DETAIL.Nextval,?,?,?)", args);
		
		return index.length;
	}
	
	/**
	 * 查询活动统计
	 * 
	 * @param ymd
	 * @param campaignId
	 * @param type
	 * @param status
	 * @return
	 */
	public List<CampaignDetailVo> queryCampaignDetailCal(Integer ymd, Long campaignId, String type, String[] status) {
		StringBuilder sql = new StringBuilder("select t.campaign_id,t.start_date,t.end_date,t.status campaign_status,t.cal_count,t.control_count,t.cal_count-t.control_count recommend_count,d.ymd,d.type,t.criteria_scheme from T_CAMPAIGN_BASE t,T_CAMPAIGN_DETAIL d where t.campaign_id=d.campaign_id ");
		List<Object> args = new ArrayList<Object>();
		
		if(ymd != null) {
			args.add(ymd);
			sql.append(" and d.ymd=? ");
		}
		
		if(campaignId != null) {
			args.add(campaignId);
			sql.append(" and d.campaign_id=? ");
		}
		
		if(type != null) {
			args.add(type);
			sql.append(" and d.type=? ");
		}
		
		if(status != null && status.length > 0) {
			sql.append(" and d.status in (");
			for(int i=0,len=status.length;i<len;i++) {
				sql.append("?,");
				args.add(status[i]);
			}
			sql = sql.deleteCharAt(sql.length() - 1).append(")");
		}
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper<CampaignDetailVo>() {
			@Override
			public CampaignDetailVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CampaignDetailVo vo = new CampaignDetailVo();
				vo.setCampaignId(rs.getLong("CAMPAIGN_ID"));
				vo.setStartDate(rs.getDate("START_DATE"));
				vo.setEndDate(rs.getDate("END_DATE"));
				vo.setCampaignStatus(rs.getString("CAMPAIGN_STATUS"));
				vo.setCalCount(rs.getInt("CAL_COUNT"));
				vo.setControlCount(rs.getInt("CONTROL_COUNT"));
				vo.setRecommendCount(rs.getInt("RECOMMEND_COUNT"));
				vo.setYmd(rs.getInt("YMD"));
				vo.setType(rs.getString("TYPE"));
				
				String[] temp = rs.getString("CRITERIA_SCHEME").split("%\\$%");
				vo.setCriteriaScheme(temp[0]);
				vo.setCinemaScheme(temp[1]);
				
				return vo;
			}
		});
	}
	
	public List<CampaignVo> queryCampaign(String date, String[] status) {
		StringBuilder sql = new StringBuilder("select t.campaign_id,t.status campaign_status,t.start_date,t.end_date,t.cal_count,t.control_count,t.cal_count-t.control_count recommend_count,t.criteria_scheme from T_CAMPAIGN_BASE t where t.ISDELETE=0 and t.START_DATE<=to_date(?,'yyyy-mm-dd') and t.END_DATE>=to_date(?,'yyyy-mm-dd') and t.status in ");
		List<Object> args = new ArrayList<Object>();
		args.add(date);
		args.add(date);
		if(status != null && status.length > 0) {
			sql.append("(");
			for(int i=0,len=status.length;i<len;i++) {
				sql.append("?,");
				args.add(status[i]);
			}
			sql = sql.deleteCharAt(sql.length() - 1).append(")");
		}
		
		//getJdbcTemplate().queryForList(sql.toString(), args.toArray(), CampaignVo.class);
		
		List<CampaignVo> list = getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper<CampaignVo>() {
			@Override
			public CampaignVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CampaignVo vo = new CampaignVo();
				vo.setCampaignId(rs.getLong("CAMPAIGN_ID"));
				vo.setCampaignStatus(rs.getString("CAMPAIGN_STATUS"));
				vo.setStartDate(rs.getDate("START_DATE"));
				vo.setEndDate(rs.getDate("END_DATE"));
				vo.setCalCount(rs.getInt("CAL_COUNT"));
				vo.setControlCount(rs.getInt("CONTROL_COUNT"));
				vo.setRecommendCount(rs.getInt("RECOMMEND_COUNT"));
				String[] temp = rs.getString("CRITERIA_SCHEME").split("%\\$%");
				vo.setCriteriaScheme(temp[0]);
				vo.setCinemaScheme(temp[1]);
				return vo;
			}
		});
		
		return list;
	}
	
	public void updateCampaignStatus(Long[] campaignId, String status) {
		StringBuilder sql = new StringBuilder("update T_CAMPAIGN_BASE t set t.status=? where t.campaign_id in ");
		List<Object> args = new ArrayList<Object>();
		args.add(status);
		if(campaignId != null && campaignId.length > 0) {
			sql.append("(");
			for(int i=0,len=campaignId.length;i<len;i++) {
				sql.append("?,");
				args.add(campaignId[i]);
			}
			sql = sql.deleteCharAt(sql.length() - 1).append(")");
		}
		
		getJdbcTemplate().update(sql.toString(), args.toArray());
	}
	
	/**
	 * 统计活动明细汇总数据
	 * 
	 * @param vo
	 */
	public CampaignDetailVo queryCalTotal(Long campaignId) {
		String sql = "select t.campaign_id,"+
		    "sum(t.recommend_num) as recommend_num,"+
		    "sum(t.control_num) as control_num,"+
		    "sum(t.sum_num) as sum_num,"+
		    "sum(t.recommend_income) as recommend_income,"+
		    "sum(t.control_income) as control_income,"+
		    "sum(t.sum_income) as sum_income "+
		    "from T_CAMPAIGN_DETAIL t where t.campaign_id=? and t.type=0 "+
		    "group by t.campaign_id";
		
		return getJdbcTemplate().queryForObject(sql, new Object[] {campaignId}, new EntityRowMapper<CampaignDetailVo>(CampaignDetailVo.class));
	}
	
	public int existCalTotal(Long campaignId) {
		String sql = "select count(1) from T_CAMPAIGN_DETAIL t where t.campaign_id=? and t.type=1";
		
		return getJdbcTemplate().queryForInt(sql, new Object[] { campaignId });
	}
	
	/**
	 * 统计推荐响应收入
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public Map<String, Double> calIncome(String sql, Object[] args) {
		final Map<String, Double> map = new HashMap<String, Double>();
		map.put("RECOMMEND_NUM", 0D);
		map.put("CONTROL_NUM", 0D);
		map.put("RECOMMEND_INCOME", 0D);
		map.put("CONTROL_INCOME", 0D);
		
		getJdbcTemplateOds().query(sql, args, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				do {
					if(rs.getInt("IS_CONTROL") == 0) { //非对比组标志
						map.put("RECOMMEND_NUM", rs.getDouble("NUM"));
						map.put("RECOMMEND_INCOME", rs.getDouble("INCOME"));
					} else {
						map.put("CONTROL_NUM", rs.getDouble("NUM"));
						map.put("CONTROL_INCOME", rs.getDouble("INCOME"));
					}
				} while(rs.next());
			}
		});
		
		return map;
	}
	
	public List<CampaignVo> queryIncompleteCampaignByBefore(String date) {
		String sql = "select t.campaign_id, t.start_date, t.end_date, t.cal_count, t.control_count, t.cal_count-t.control_count recommend_count, t.status, t.CRITERIA_SCHEME from T_CAMPAIGN_BASE t where t.ISDELETE=0 and t.END_DATE<to_date(?,'yyyy-mm-dd') and t.status in (20, 30) ";
		
		List<CampaignVo> list = getJdbcTemplate().query(sql, new Object[] { date }, new RowMapper<CampaignVo>() {
			@Override
			public CampaignVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CampaignVo vo = new CampaignVo();
				
				vo.setCampaignId(rs.getLong("CAMPAIGN_ID"));
				vo.setStartDate(rs.getDate("START_DATE"));
				vo.setEndDate(rs.getDate("END_DATE"));
				vo.setCalCount(rs.getInt("CAL_COUNT"));
				vo.setControlCount(rs.getInt("CONTROL_COUNT"));
				vo.setRecommendCount(rs.getInt("RECOMMEND_COUNT"));
				vo.setCampaignStatus(rs.getString("STATUS"));
				
				String[] temp = rs.getString("CRITERIA_SCHEME").split("%\\$%");
				vo.setCriteriaScheme(temp[0]);
				vo.setCinemaScheme(temp[1]);
				
				return vo;
			}
		});
		
		return list;
	}
	
	public Map<Integer,CampaignDetailVo> queryCampaignDetailByTime(Long campaignId) {
		String sql = "select t.campaign_id, t.start_date, t.end_date, t.cal_count, t.control_count, t.cal_count-t.control_count recommend_count, t.status, d.id, d.ymd, d.type, t.criteria_scheme  from T_CAMPAIGN_BASE t, T_CAMPAIGN_DETAIL d where t.campaign_id=d.campaign_id and t.isdelete=0 and d.type=0 and t.campaign_id=? order by d.ymd";
		
		final Map<Integer, CampaignDetailVo> map = new HashMap<Integer, CampaignDetailVo>();
		
		getJdbcTemplate().query(sql, new Object[] { campaignId }, new RowMapper<CampaignDetailVo>() {
			@Override
			public CampaignDetailVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CampaignDetailVo vo = new CampaignDetailVo();
				vo.setId(rs.getLong("ID"));
				vo.setCampaignId(rs.getLong("CAMPAIGN_ID"));
				vo.setStartDate(rs.getDate("START_DATE"));
				vo.setEndDate(rs.getDate("END_DATE"));
				vo.setCalCount(rs.getInt("CAL_COUNT"));
				vo.setControlCount(rs.getInt("CONTROL_COUNT"));
				vo.setRecommendCount(rs.getInt("RECOMMEND_COUNT"));
				vo.setCampaignStatus(rs.getString("STATUS"));
				vo.setYmd(rs.getInt("YMD"));
				vo.setType(rs.getString("TYPE"));
				
				String[] temp = rs.getString("CRITERIA_SCHEME").split("%\\$%");
				vo.setCriteriaScheme(temp[0]);
				vo.setCinemaScheme(temp[1]);
				
				map.put(vo.getYmd(), vo);
				
				return null;
			}
		});
		
		return map;
	}
	
	public CampaignVo queryCampaignById(Long campaignId) {
		String sql = "select t.campaign_id,t.status,t.start_date,t.end_date,t.cal_count,t.control_count,t.cal_count-t.control_count recommend_count,t.criteria_scheme from T_CAMPAIGN_BASE t where t.ISDELETE=0 and t.CAMPAIGN_ID=?";
		
		return getJdbcTemplate().queryForObject(sql, new Object[] { campaignId }, new RowMapper<CampaignVo>() {
			@Override
			public CampaignVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CampaignVo vo = new CampaignVo();
				vo.setCampaignId(rs.getLong("CAMPAIGN_ID"));
				vo.setStartDate(rs.getDate("START_DATE"));
				vo.setEndDate(rs.getDate("END_DATE"));
				vo.setCalCount(rs.getInt("CAL_COUNT"));
				vo.setControlCount(rs.getInt("CONTROL_COUNT"));
				vo.setRecommendCount(rs.getInt("RECOMMEND_COUNT"));
				vo.setCampaignStatus(rs.getString("STATUS"));
				
				String[] temp = rs.getString("CRITERIA_SCHEME").split("%\\$%");
				vo.setCriteriaScheme(temp[0]);
				vo.setCinemaScheme(temp[1]);
				
				return vo;
			}
		});
	}
	
	public void cleanSyncCalMember(Long campaignId) {
		getJdbcTemplate().update("delete from T_CAMPAIGN_MEMBER t where t.campaign_id=?", new Object[] { campaignId });
	}
	
	public void delSyncCalMember(Long campaignId, int ymd, String calType) {
		getJdbcTemplate().update("delete from T_CAMPAIGN_MEMBER t where t.campaign_id=? and t.ymd=? and t.cal_type=?", new Object[] { campaignId, ymd, calType });
	}
	
	public Map<String, Integer> querySyncCalMemberCount(Long campaignId, String calType) {
		final Map<String, Integer> count = new HashMap<String, Integer>();
		count.put("RECOMMEND_NUM", 0);
		count.put("CONTROL_NUM", 0);
		
		getJdbcTemplate().query("select a.IS_CONTROL, count(a.MEMBER_KEY) NUM from (select distinct t.member_key, t.is_control from T_CAMPAIGN_MEMBER t where t.campaign_id=? and t.cal_type=?) a group by a.IS_CONTROL", new Object[] { campaignId, calType }, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				do {
					if(rs.getInt("IS_CONTROL") == 0) {
						count.put("RECOMMEND_NUM", rs.getInt("NUM"));
					} else {
						count.put("CONTROL_NUM", rs.getInt("NUM"));
					}
				} while(rs.next());
			}
		});
		
		return count;
	}
	
	public void syncCalMember(String sql, Object[] args, final Long campaignId, final int ymd, final String calType) {
		final String insertSql = "insert into T_CAMPAIGN_MEMBER(CAMPAIGN_ID,YMD,MEMBER_KEY,IS_CONTROL,CAL_TYPE) values(?,?,?,?,?)";
		
		getJdbcTemplateOds().query(sql, args, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				List<Object[]> args = new ArrayList<Object[]>();
				int size = 0;
				do {
					args.add(new Object[] { campaignId, ymd, rs.getInt("MEMBER_KEY"), rs.getString("IS_CONTROL"), calType });
					size++;
					if(size > 8000) {
						getJdbcTemplate().batchUpdate(insertSql, args);
						args.clear();
						size = 0;
					}
				} while(rs.next());
				
				if(size > 0) {
					getJdbcTemplate().batchUpdate(insertSql, args);
					args.clear();
				}
			}
		});
		
	}
	
	public void deleteCampaignDetail(Long campaignId) {
		getJdbcTemplate().update("delete from T_CAMPAIGN_DETAIL t where t.campaign_id=?", new Object[] { campaignId });
	}
	
}
