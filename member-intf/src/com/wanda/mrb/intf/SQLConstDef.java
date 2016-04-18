package com.wanda.mrb.intf;

public class SQLConstDef {
	/*--------------注册会员-------------*/
	/*读取S_T_MEMBER.nextval生成memberID*/
	public static final String MEMBER_REGISTER_MEMBERID = "select S_T_MEMBER.nextval from dual";
	/* 会员注册 */
	public static final String MEMBER_REGISTER = "INSERT INTO T_MEMBER (MEMBER_ID,MEMBER_NO,MOBILE,NAME,GENDER,BIRTHDAY,REGIST_TYPE,REGIST_CHNID,REGIST_OP_NO,REGIST_OP_NAME,REGIST_CINEMA_ID,STATUS,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,EMAIL,PHONE,FAV_CINEMA_ID,REGIST_DATE,SOURCE_TYPE,REGIST_OP_SYS,ARRIVAL_TYPE,OFTEN_CHANNEL,REGIST_CHN_EXT_ID) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?,?,to_date(?,'yyyy-mm-dd HH24:MI:SS'),?,?,?,?,?)";
	/* 会员注册会员信息 */
	public static final String MEMBER_REGISTER_INFO = "INSERT INTO T_MEMBER_INFO (MANAGE_CINEMA_ID,EDUCATION,OCCUPATION,INCOME,MARRY_STATUS,CHILD_NUMBER,FQ_CINEMA_DIST,FQ_CINEMA_TIME,MOBILE_OPTIN,IDCARD_TYPE,IDCARD_NO,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,MEMBER_INFO_ID,MEMBER_ID,QQ) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,S_T_MEMBER_INFO.nextval,?,?)";
	/* 会员注册会员地址信息 */
	public static final String MEMBER_REGISTER_ADDR = "INSERT INTO T_MEMBER_ADDR (MEMBER_ADDR_ID,ZIPCODE,ADDRESS1,PROVINCE_ID,CITY_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,MEMBER_ID) " +
			"VALUES (S_T_MEMBER_ADDR.nextval,?,?,?,?,?,?,sysdate,?,sysdate,?)";
	public static final String MEMBER_REGISTER_PROVINCE_CITY = "select p.seqid as PROVINCE,ci.seqid as CITY from t_cinema c, T_PROVINCE p, T_CITY ci where p.seqid = c.province and ci.seqid = c.city and  c.seqid = ?";
	/* 会员喜欢的影片信息 */
	public static final String MEMBER_REGISTER_FAV_FILMTYPE = "INSERT INTO T_MEM_FAV_FILMTYPE (FILM_TYPES,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,FAV_FILMTYPE_ID,MEMBER_ID) " +
			"VALUES (?,?,?,sysdate,?,sysdate,S_T_MEM_FAV_FILMTYPE.nextval,?)";
	/* 会员喜欢的联络方式 */
	public static final String MEMBER_REGISTER_FAV_CONTACT = "INSERT INTO T_MEM_FAV_CONTACT (CONTACT_MEANS,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,FAV_CONTACT_ID,MEMBER_ID) " +
			"VALUES (?,?,?,sysdate,?,sysdate,S_T_MEM_FAV_CONTACT.nextval,?)";
	
	/*----------- 修改会员 ------------*/
	public static final String MEMBER_REGISTER_UPDATE = "UPDATE T_MEMBER " +
			"SET NAME=?, GENDER=?, BIRTHDAY=?, STATUS=?, UPDATE_BY=?, " +
			"UPDATE_DATE=sysdate, EMAIL=?, PHONE=?, FAV_CINEMA_ID=?, SOURCE_TYPE=?, MOBILE=?,ARRIVAL_TYPE=?,OFTEN_CHANNEL=? WHERE MEMBER_NO = ?";
	public static final String MEMBER_REGISTER_UPDATE_NOFAVCINEMA = "UPDATE T_MEMBER " +
			"SET NAME=?, GENDER=?, BIRTHDAY=?, STATUS=?, UPDATE_BY=?, " +
			"UPDATE_DATE=sysdate, EMAIL=?, PHONE=?, SOURCE_TYPE=?, MOBILE=?,ARRIVAL_TYPE=?,OFTEN_CHANNEL=?  WHERE MEMBER_NO = ?";
	public static final String MEMBER_INFO_UPDATE = "UPDATE T_MEMBER_INFO " +
			"SET EDUCATION=?, OCCUPATION=?, INCOME=?, MARRY_STATUS=?, " +
			"CHILD_NUMBER=?, FQ_CINEMA_DIST=?, FQ_CINEMA_TIME=?, MOBILE_OPTIN=?, IDCARD_TYPE=?, " +
			"IDCARD_NO=?, IDCARD_HASHNO=?, UPDATE_BY=?, " +
			"UPDATE_DATE=sysdate, QQ=? " +
			"WHERE MEMBER_ID = (SELECT MEMBER_ID FROM T_MEMBER WHERE MEMBER_NO = ?)";
	public static final String MEMBER_ADDR_UPDATE = "UPDATE T_MEMBER_ADDR SET ZIPCODE=?, " +
			"ADDRESS1=?, COUNTRY_ID=?, PROVINCE_ID=?, CITY_ID=?, " +
			"UPDATE_BY=?, UPDATE_DATE=sysdate " +
			"WHERE MEMBER_ID = (SELECT MEMBER_ID FROM T_MEMBER WHERE MEMBER_NO = ?)";
	public static final String MEMBER_FAV_FILMTYPE_UPDATE = "UPDATE T_MEM_FAV_FILMTYPE " +
			"SET FILM_TYPES=?, UPDATE_BY=?, " +
			"UPDATE_DATE=sysdate " +
			"WHERE MEMBER_ID = (SELECT MEMBER_ID FROM T_MEMBER WHERE MEMBER_NO = ?)";
	public static final String MEMBER_FAV_CONTACT_UPDATE = "UPDATE T_MEM_FAV_CONTACT SET CONTACT_MEANS=?, " +
			"UPDATE_BY=?, UPDATE_DATE=sysdate " +
			"WHERE MEMBER_ID = (SELECT MEMBER_ID FROM T_MEMBER WHERE MEMBER_NO = ?)";
	/*------------查询会员------------*/
	/*根据会员编号查询会员编码*/
	public static final String MEMBER_CHECK_BY_MEMBERNO = "SELECT MEMBER_ID,STATUS FROM T_MEMBER WHERE MEMBER_NO=? and ISDELETE = '0'";
	/*根据会员手机号查询会员ID*/
	public static final String MEMBER_CHECK_BY_MOBILE = "SELECT MEMBER_ID,STATUS FROM T_MEMBER WHERE MOBILE=? and ISDELETE = '0'";
	/*根据会员手机号查询会员编码*/
	public static final String MEMBER_CHECK_MEMBER_NO_BY_MOBILE = "SELECT MEMBER_NO FROM T_MEMBER WHERE MOBILE=? and ISDELETE = '0'";
	/*根据会员卡号查询会员编码*/
	public static final String MEMBER_CHECK_BY_CARDNUMBER = "SELECT mdc.MEMBER_ID,m.STATUS " +
			"FROM T_MACK_DADDY_CARD mdc,T_MEMBER m " +
			"WHERE m.MEMBER_ID = mdc.MEMBER_ID AND mdc.CARD_NUMBER=? AND m.ISDELETE = '0'";
	/*根据关联会员卡号查询会员编码*/
	public static final String MEMBER_CHECK_BY_REL_CARDNUMBER = "SELECT mcr.MEMBER_ID,m.STATUS " +
			"FROM T_MEM_CARD_REL mcr,T_MEM_CARD_INFO c,T_MEMBER m WHERE mcr.CARD_ID = c.SEQ_ID AND m.MEMBER_ID = mcr.MEMBER_ID AND c.CARD_NUMBER=? AND m.ISDELETE = '0'";
	/*根据会员id查询会员编码*/
	public static final String MEMBER_CHECK_BY_MEMBERID = "SELECT m.MEMBER_NO,m.NAME,m.MOBILE,m.EMAIL,c.SHORT_NAME,c.INNER_CODE, " +
			"to_char(m.BIRTHDAY,'yyyy-mm-dd') AS BIRTHDAY,m.GENDER,c.CODE,m.REGIST_OP_NO,m.REGIST_OP_NAME,m.SOURCE_TYPE,m.REGIST_CHNID,m.PHONE," +
			"to_char(m.REGIST_DATE,'yyyy-mm-dd hh24:mi:ss') AS REGIST_DATE,m.FAV_CINEMA_ID,m.ARRIVAL_TYPE,m.OFTEN_CHANNEL,m.REGIST_CHN_EXT_ID " +
			"FROM T_MEMBER m,T_CINEMA c " +
			"WHERE m.REGIST_CINEMA_ID = c.seqid and m.MEMBER_ID = ?";
	public static final String MEMBER_INFO_BY_MEMBERID = "SELECT mi.IDCARD_NO,mi.IDCARD_HASHNO," +
			"mi.IDCARD_TYPE,mi.MARRY_STATUS,mi.CHILD_NUMBER,mi.EDUCATION," +
			"mi.OCCUPATION,mi.INCOME,mi.FQ_CINEMA_DIST,mi.FQ_CINEMA_TIME,mi.MOBILE_OPTIN,mi.QQ,mi.MANAGE_CINEMA_ID " +
			"FROM T_MEMBER_INFO mi WHERE mi.MEMBER_ID = ?";
	public static final String MEMBER_ADDR_BY_MEMBERID = "SELECT ma.ADDRESS1,ma.PROVINCE_ID,ma.CITY_ID," +
			"ma.ZIPCODE " +
			"FROM T_MEMBER_ADDR ma WHERE ma.MEMBER_ID = ?";
	public static final String MEMBER_CONTACT_BY_MEMBERID = "SELECT mfc.CONTACT_MEANS " +
			"FROM T_MEM_FAV_CONTACT mfc WHERE mfc.MEMBER_ID = ?";
	public static final String MEMBER_LEVEL_BY_MEMBERID = "SELECT ml.MEM_LEVEL,to_char(ml.EXPIRE_DATE,'yyyy-mm-dd') AS EXPIRE_DATE,ml.ORG_LEVEL," +
			"to_char(ml.SET_TIME,'yyyy-mm-dd hh24:mi:ss') AS SET_TIME,ml.TARGET_LEVEL,ml.LEVEL_POINT_OFFSET,ml.TICKET_OFFSET,ml.TICKET_OFFSET_UP " +
			"FROM T_MEMBER_LEVEL ml WHERE ml.MEMBER_ID = ?";
	public static final String MEMBER_CARD_BY_MEMBERID = "SELECT mc.CARD_NUMBER " +
			"FROM T_MACK_DADDY_CARD mc WHERE mc.MEMBER_ID = ?";
	/*根据手机号查询该手机号是否已经注册*/
	public static final String MEMBER_CHECK_MOBILE_NO = "SELECT * FROM T_MEMBER where MOBILE = ? and ISDELETE = '0'";
	public static final String MEMBER_SELECT_INFO = "SELECT m.MEMBER_NO,tmp.LEVEL_POINT_TOTAL," +
			"tmp.ACTIVITY_POINT,tmp.EXG_POINT_BALANCE,tml.MEM_LEVEL,to_char(tml.EXPIRE_DATE,'yyyy-mm-dd') AS EXPIRE_DATE,tml.ORG_LEVEL," +
			"to_char(tml.SET_TIME,'yyyy-mm-dd hh24:mi:ss') AS SET_TIME,tml.TARGET_LEVEL,tml.LEVEL_POINT_OFFSET,tml.TICKET_OFFSET,tml.TICKET_OFFSET_UP " +
			"FROM T_MEMBER m, T_MEMBER_LEVEL tml, T_MEMBER_POINT tmp " +
			"WHERE m.MEMBER_ID = tml.MEMBER_ID and m.MEMBER_ID = tmp.MEMBER_ID and m.MEMBER_ID = ?";
	/*根据影城编码获得影城id*/
	public static final String MEMBER_SELECT_CINEMAID_BY_CODE = "select seqid from t_cinema where code = ?";
	public static final String MEMBER_SELECT_CINEMACODE_BY_ID = "select code,short_name from t_cinema where seqid = ?";
	public static final String MEMBER_SELECT_INNERCODE_BY_CODE = "select inner_code from t_cinema where code = ?";
	
	/*查询会员可用积分*/
	public static final String QUERY_POINT_BALANCE = "select p.EXG_POINT_BALANCE,m.STATUS,m.MOBILE from T_MEMBER m, T_MEMBER_POINT p where m.MEMBER_ID=p.MEMBER_ID and m.MEMBER_NO=?";
	/*验证会员是否存在*/
	public static final String CHECK_MEMBER = "select MEMBER_ID from T_MEMBER where MEMBER_NO=? and ISDELETE = '0'";
	
	/*验证会员是否存在*/
	public static final String CHECK_MEMBER_BY_MOBILE = "select MEMBER_ID,STATUS from T_MEMBER where MOBILE=? and ISDELETE = '0' ";
	
	public static final String MEMBER_CARD_REL="select c.CARD_NUMBER, y.TYPE_NAME ,y.TYPE_CODE " +
			"from T_CARD c ,T_MEM_CARD_REL  r ,T_MEMBER m, T_CARD_TYPE y " +
			"where c.CARD_TYPE_ID=y.CARD_TYPE_ID and c.card_id=r.card_id " +
			"and m.member_id=r.member_id and m.member_no=?";

/*	public static final String MEMBER_VOUCHER_REL="select p.PRINT_CODE," +
			"to_char(o.SALE_DATE,'yyyy-mm-dd hh24:mi:ss') AS SALE_DATE," +
			"to_char(o.EXPIRY_DATE,'yyyy-mm-dd hh24:mi:ss') AS EXPIRY_DATE," +
			"t.TYPE_CODE,t.TYPE_NAME,t.USE_TYPE, m.member_no, p.OPERRATE_TYPE, o.UNIT_VALUE " +
			"from T_MEMBER m, T_VOUCHER_POOL_DETAIL p ,T_VOUCHER v ,T_VOUCHER_ORDER o,T_VOUCHER_TYPE t " +
			"where v.VOUCHER_ORDER_ID=o.VOUCHER_ORDER_ID and v.VOUCHER_TYPE_ID=t.VOUCHER_TYPE_ID " +
			"and m.member_id=p.member_id and p.BAR_CODE=v.BAR_CODE " +
			"and m.member_no=? and v.VOUCHER_STATUS=? ORDER BY o.EXPIRY_DATE";*/
	
	
	
	public static final String MEMBER_VOUCHER_REL="select A.* from (select distinct p.PRINT_CODE,"+
	"to_char(o.SALE_DATE,'yyyy-mm-dd hh24:mi:ss') AS SALE_DATE,"+ 
	"to_char(o.EXPIRY_DATE,'yyyy-mm-dd hh24:mi:ss') AS EXPIRY_DATE,"+ 
	"t.TYPE_CODE,t.TYPE_NAME,t.USE_TYPE, m.member_no, p.OPERRATE_TYPE, a.SHOW_VALUE as UNIT_VALUE ,a.MIN_PRICE "+ 
	"from T_MEMBER m, T_VOUCHER_POOL_DETAIL p ,T_VOUCHER v,T_VOUCHER_ORDER o,T_VOUCHER_TYPE t,T_VOUCHER_TYPE_DEF a "+ 
	"where v.VOUCHER_ORDER_ID=o.VOUCHER_ORDER_ID and v.VOUCHER_TYPE_ID=t.VOUCHER_TYPE_ID  and a.voucher_type_id = v.voucher_type_id "+ 
	"and m.member_id=p.member_id and p.BAR_CODE=v.BAR_CODE "+ 
	"and m.member_no=? "+ 
	"and v.VOUCHER_STATUS=?) " + 
	"A order by A.EXPIRY_DATE";
	
	
	public static final String CHECK_VOUCHER_REL="select * from T_MEMBER m,T_VOUCHER_POOL_DETAIL p where m.member_id=p.member_id and m.member_no=? and p.BAR_CODE=?";

//	public static final String CHECK_VOUCHER_="";
	
	public static final String INSERT_MEMBER_VOUCHER_REL="INSERT INTO T_VOUCHER_POOL_DETAIL(VOUCHER_POOL_DETAIL_ID, VOUCHER_POOL_ID, PRINT_CODE, BAR_CODE, MEMBER_ID, OPERRATE_TYPE) VALUES(S_T_VOUCHER_POOL_DETAIL.NEXTVAL, -1, ?, ?, ?, ?)";
	public static final String DELETE_MEMBER_VOUCHER_REL="UPDATE T_VOUCHER_POOL_DETAIL SET MEMBER_ID=null WHERE BAR_CODE =?";
	public static final String UPDATE_MEMBER_VOUCHER_REL="UPDATE T_VOUCHER_POOL_DETAIL SET MEMBER_ID=?, OPERRATE_TYPE=? WHERE BAR_CODE =?";

	public static final String CHECK_TRANS_ORDER="select POINT_HISTORY_ID from T_POINT_HISTORY where ORDER_ID=? and CINEMA_INNER_CODE=? and IS_SUCCEED=? and MEMBER_ID=?";
	
	public static final String UPDATE_POINT_BALANCE="update T_MEMBER_POINT set EXG_POINT_BALANCE=EXG_POINT_BALANCE+? ,EXG_EXPIRE_POINT_BALANCE = EXG_EXPIRE_POINT_BALANCE+? where MEMBER_ID=?";
	
	/*会员积分历史查询*/
	public static final String QUERY_POINT_HISTORY = "select to_char(ph.SET_TIME,'yyyy-mm-dd hh24:mi:ss') AS SET_TIME,ph.POINT_TYPE,ph.EXCHANGE_POINT," +
			"to_char(ph.EXCHANGE_POINT_EXPIRE_TIME,'yyyy-mm-dd') AS EXCHANGE_POINT_EXPIRE_TIME,ph.PRODUCT_NAME " +
			"from T_MEMBER m, T_POINT_HISTORY ph " +
			"where m.MEMBER_ID = ph.MEMBER_ID and ph.ISDELETE = '0' and ph.IS_HISTORY = '0' and ph.EXCHANGE_POINT <> 0 and m.MEMBER_NO=? and ph.CREATE_DATE > to_date(?,'yyyy-mm-dd') " +
			"and ph.CREATE_DATE < to_date(?,'yyyy-mm-dd')+1 order by SET_TIME DESC";
	/*会员消费历史查询--影片*/
	public static final String QUERY_TRANS_HISTORY_FILM = "SELECT ORDER_ID,TRANS_TIME,CODE,SHORT_NAME,PRDTYPE,FILM_NAME,TOTAL_AMOUNT,IS_POINT,SUM(POINT) AS POINT,HALL_NUM,SHOW_TIME,TICKET_NUM,SUM(EXCHANGE_POINT) AS EXCHANGE_POINT " +
			"FROM " +
			"(" +
			"SELECT tto.ORDER_ID,to_char(tto.TRANS_TIME,'yyyy-mm-dd hh24:mi:ss') AS TRANS_TIME,c.CODE,c.SHORT_NAME,'1' AS PRDTYPE,tto.FILM_NAME,tto.TOTAL_AMOUNT,tto.IS_POINT,0 AS POINT,tto.HALL_NUM,to_char(tto.SHOW_TIME,'yyyy-mm-dd hh24:mi:ss') AS SHOW_TIME,tto.TICKET_NUM,tto.point AS EXCHANGE_POINT " +
			"FROM T_TICKET_TRANS_ORDER tto,T_MEMBER m,T_CINEMA c,T_POINT_HISTORY ph " +
			"WHERE ph.ORDER_ID (+)= tto.ORDER_ID AND ph.CINEMA_INNER_CODE (+)= tto.CINEMA_INNER_CODE " +
			"AND m.MEMBER_ID = tto.MEMBER_ID AND c.INNER_CODE = tto.CINEMA_INNER_CODE and (ph.point_type = '6' or ph.point_type is null) " +
			"and ph.IS_SUCCEED = '1' AND m.MEMBER_NO = ? and tto.TRANS_TIME > to_date(?,'yyyy-mm-dd') " +
			"and tto.TRANS_TIME < to_date(?,'yyyy-mm-dd')+1 " +
			"UNION " +
			"SELECT tto.ORDER_ID,to_char(tto.TRANS_TIME,'yyyy-mm-dd hh24:mi:ss') AS TRANS_TIME,c.CODE,c.SHORT_NAME,'1' AS PRDTYPE,tto.FILM_NAME,tto.TOTAL_AMOUNT,tto.IS_POINT,tto.POINT,tto.HALL_NUM,to_char(tto.SHOW_TIME,'yyyy-mm-dd hh24:mi:ss') AS SHOW_TIME,tto.TICKET_NUM,0 AS EXCHANGE_POINT " +
			"FROM T_TICKET_TRANS_ORDER tto,T_MEMBER m,T_CINEMA c,T_POINT_HISTORY ph " +
			"WHERE ph.POINT_TRANS_CODE (+)= tto.ORDER_ID AND ph.CINEMA_INNER_CODE (+)= tto.CINEMA_INNER_CODE " +
			"AND m.MEMBER_ID = tto.MEMBER_ID AND c.INNER_CODE = tto.CINEMA_INNER_CODE and (ph.point_type = '1' or ph.point_type is null) " +
			"AND m.MEMBER_NO = ? and tto.TRANS_TIME > to_date(?,'yyyy-mm-dd') " +
			"and tto.TRANS_TIME < to_date(?,'yyyy-mm-dd')+1 " +
			") " +
			"GROUP BY ORDER_ID,TRANS_TIME,CODE,SHORT_NAME,PRDTYPE,FILM_NAME,TOTAL_AMOUNT,IS_POINT,HALL_NUM,SHOW_TIME,TICKET_NUM " +
			"order by TRANS_TIME DESC";
	/*会员消费历史查询--卖品*/
	public static final String QUERY_TRANS_HISTORY_GOOD = "SELECT gto.ORDER_ID," +
			"to_char(gto.TRANS_TIME,'yyyy-mm-dd hh24:mi:ss') AS TRANS_TIME,c.CODE,c.SHORT_NAME,'2' AS PRDTYPE,gto.GOOD_NAME,gto.TOTAL_AMOUNT,gto.IS_POINT,0 AS POINT,gto.point AS EXCHANGE_POINT " +
			"FROM T_GOODS_TRANS_ORDER gto,T_MEMBER m,T_CINEMA c,T_POINT_HISTORY ph " +
			"WHERE ph.ORDER_ID = gto.ORDER_ID AND ph.CINEMA_INNER_CODE = gto.CINEMA_INNER_CODE AND m.MEMBER_ID = gto.MEMBER_ID AND c.INNER_CODE = gto.CINEMA_INNER_CODE and ph.IS_SUCCEED = '1' and ph.point_type = '6' and m.MEMBER_NO = ? and gto.TRANS_TIME > to_date(?,'yyyy-mm-dd') and gto.TRANS_TIME < to_date(?,'yyyy-mm-dd')+1 " +
			"UNION " +
			"SELECT gto.ORDER_ID,to_char(gto.TRANS_TIME,'yyyy-mm-dd hh24:mi:ss') AS TRANS_TIME,c.CODE,c.SHORT_NAME,'2' AS PRDTYPE,gto.GOOD_NAME,gto.TOTAL_AMOUNT,gto.IS_POINT,gto.POINT,0 AS EXCHANGE_POINT " +
			"FROM T_GOODS_TRANS_ORDER gto,T_MEMBER m,T_CINEMA c,T_POINT_HISTORY ph " +
			"WHERE ph.point_trans_code = gto.ORDER_ID AND ph.CINEMA_INNER_CODE = gto.CINEMA_INNER_CODE " +
			"AND m.MEMBER_ID = gto.MEMBER_ID AND c.INNER_CODE = gto.CINEMA_INNER_CODE " +
			"and ph.point_type = '1' and m.MEMBER_NO = ? and gto.TRANS_TIME > to_date(?,'yyyy-mm-dd') " +
			"and gto.TRANS_TIME < to_date(?,'yyyy-mm-dd')+1 order by TRANS_TIME DESC";
	/*生成会员编码*/
	public static final String QUERY_MAX_MEMBER_NO = "select S_MEMBER_NUM.NEXTVAL AS MEMBERNUM FROM dual";
	public static final String QUERY_CINEMA_SEQID_INNER_CODE = "select seqid,inner_code from t_cinema where CODE = ?";
	
	
	/*生成注册影城*/
	public static final String QUERY_CINEMA_NUM_FROM_CITY = "select count(*) as cinemanum from t_cinema where city = (select city from t_cinema where isopen = 1 and code = ?)";
	public static final String QUERY_CINEMA_FROM_CITY = "select * from t_cinema where city = (select city from t_cinema where isopen = 1 and code = ?)";
	public static final String QUERY_VOUCHER_ORDER = "SELECT VOUCHER_ORDER_ID FROM T_VOUCHER_ORDER where APP_STATUS = 'P' and ORDER_STATUS = 'E' AND ORDER_NUM = ? ";
	/*验证券在总部系统中是否存在*/
	public static final String QUERY_VOUCHER_BY_BARCODE = "SELECT VOUCHER_NUMBER ,VOUCHER_ORDER_ID , EXPIRY_DATE,VOUCHER_STATUS FROM T_VOUCHER where bar_code = ? and voucher_status = 'A'";
	/*验证券是否在券库中*/
	public static final String QUERY_VOUCHER_POOL_BY_BARCODE = "SELECT VOUCHER_POOL_ID,MEMBER_ID FROM T_VOUCHER_POOL_DETAIL WHERE BAR_CODE = ?";
	/*查询卡的状态*/
	public static final String QUERY_CARD_STATUS_BY_CARD = "SELECT tdc.STATUS FROM T_MACK_DADDY_CARD tdc WHERE tdc.CARD_NUMBER = ?";
	/*查询该会员持有卡的数量*/
	public static final String QUERY_CARD_NUM_BY_MEMBERID = "SELECT COUNT(*) AS NUM FROM T_MACK_DADDY_CARD mdc WHERE mdc.MEMBER_ID =?";
	/*查询该会员卡是否已经发给其他会员*/
	public static final String QUERY_CARD_MEMBERID_BY_CARDNUMBER = "SELECT mdc.MEMBER_ID FROM T_MACK_DADDY_CARD mdc WHERE mdc.CARD_NUMBER =? and mdc.MEMBER_ID is not null";
	/*发卡*/
	public static final String ISSUE_CARD = "UPDATE T_MACK_DADDY_CARD SET MEMBER_ID=? WHERE CARD_NUMBER=?";
	/*清掉老卡*/
	public static final String ISSUE_CARD_DEL_OLD = "UPDATE T_MACK_DADDY_CARD SET MEMBER_ID=null,STATUS = 'R' WHERE MACK_DADDY_CARD_ID=?";
	/*发卡服务*/
	public static final String ISSUE_CARD_SVC = "INSERT INTO T_MACK_DADDY_CARD_SVC" +
			"(MACK_DADDY_CARD_SVC_ID, MEMBER_CARD_ID, CINEMA_ID, OPERATOR, OPERATOR_NAME, " +
			"SVC_TYPE, DESC_INFO, MEMBER_ID, SERVICED_TIME) " +
			"VALUES(S_T_MACK_DADDY_CARD_SVC.nextval, ?, ?, ?, ?, ?, ?, ?, sysdate)";
	/*查询会员卡的ID*/
	public static final String ISSUE_CARD_ID = "SELECT mdc.MACK_DADDY_CARD_ID FROM T_MACK_DADDY_CARD mdc WHERE mdc.CARD_NUMBER =?";
	/*查询会员卡的老卡ID*/
	public static final String ISSUE_OLD_CARD_ID = "SELECT mdc.MACK_DADDY_CARD_ID FROM T_MACK_DADDY_CARD mdc WHERE mdc.MEMBER_ID = ?";
	/*补卡卡服务*/
	public static final String ISSUE_CARD_MAKE_UP = "INSERT INTO T_MACK_DADDY_CARD_SVC" +
			"(MACK_DADDY_CARD_SVC_ID, MEMBER_CARD_ID, CINEMA_ID, OPERATOR, OPERATOR_NAME, " +
			"SVC_TYPE, DESC_INFO, NEW_MEMBER_CARD_ID, MEMBER_ID, SERVICED_TIME) " +
			"VALUES(S_T_MACK_DADDY_CARD_SVC.nextval, ?, ?, ?, ?, ?, ?, ?, ?, sysdate)";
	public static final String INSERT_CHECK_CODE="INSERT INTO T_TRANS_CHECK_CODE(TRANS_CHECK_CODE_ID,CHECK_CODE,GEN_TIME,MOBILE)values(S_T_TRANS_CHECK_CODE.NEXTVAL,?,SysDate,?)";
	public static final String SELECT_CHECK_CODE="SELECT * FROM T_TRANS_CHECK_CODE WHERE CHECK_CODE=? AND MOBILE=?";
	public static final String UPDATE_POINT_HISTORY="update T_POINT_HISTORY set IS_SUCCEED=? where POINT_HISTORY_ID=?";
	public static final String INSERT_POINT_HISTORY="insert into T_POINT_HISTORY(POINT_TYPE,EXCHANGE_POINT_EXPIRE_TIME,POINT_HISTORY_ID,VERSION,MEMBER_ID,SET_TIME,POINT_SYS," +
	"ORG_POINT_BALANCE,LEVEL_POINT,ACTIVITY_POINT,IS_SYNC_BALANCE,POINT_BALANCE,ISDELETE,EXCHANGE_POINT,PRODUCT_NAME,ORDER_ID,CREATE_DATE," +
	"POINT_TRANS_TYPE,IS_SUCCEED,CREATE_BY,CINEMA_INNER_CODE,POINT_TRANS_CODE)" +
	"values(?,SysDate,S_T_POINT_HISTORY.NEXTVAL,?,?,SysDate,?,?,?,?,?,?,?,?,?,?,SysDate,?,?,?,?,?)";
	public static final String INSERT_POINT_HISTORY_REWARD="insert into T_POINT_HISTORY(POINT_TYPE,EXCHANGE_POINT_EXPIRE_TIME,POINT_HISTORY_ID,VERSION,MEMBER_ID,SET_TIME,POINT_SYS," +
			"ORG_POINT_BALANCE,LEVEL_POINT,ACTIVITY_POINT,IS_SYNC_BALANCE,POINT_BALANCE,ISDELETE,EXCHANGE_POINT,PRODUCT_NAME,ORDER_ID,CREATE_DATE," +
			"POINT_TRANS_TYPE,IS_SUCCEED,CREATE_BY,CINEMA_INNER_CODE,POINT_TRANS_CODE,ADJ_REASON)" +
			"values(?,to_date(?,'yyyy-mm-dd HH24:MI:SS'),S_T_POINT_HISTORY.NEXTVAL,?,?,SysDate,?,?,?,?,?,?,?,?,?,?,SysDate,?,?,?,?,?,?)";
	/*通过维数据判断发送短信开关是否开放*/
	public static final String SELECT_MSG_OPEN_FROM_DIM = "select name from t_dimdef " +
			"where typeid = （select typeid from T_DIMTYPEDEF where typename = ?）";
	/*判断会员卡和手机号*/
	public static final String SELECT_MEMBER_CARD_BY_MOBILE = "SELECT mdc.CARD_NUMBER " +
			"FROM T_MACK_DADDY_CARD mdc,T_MEMBER m " +
			"WHERE mdc.MEMBER_ID = m.MEMBER_ID and m.MOBILE = ?";
	/*判断关联会员卡和手机号*/
	public static final String SELECT_REL_MEMBER_CARD_BY_MOBILE = "SELECT c.CARD_NUMBER " +
			"FROM T_MEM_CARD_INFO c,T_MEM_CARD_REL mcr,T_MEMBER m " +
			"WHERE m.MEMBER_ID = mcr.MEMBER_ID and mcr.CARD_ID = c.SEQ_ID and m.MOBILE = ?";
	public static final String SELECT_MEMBER_POINT = "SELECT LEVEL_POINT_TOTAL,ACTIVITY_POINT," +
			"EXG_POINT_BALANCE,EXG_EXPIRE_POINT_BALANCE FROM T_MEMBER_POINT WHERE MEMBER_ID = ?";
	public static final String SELECT_MEMBER_EXCHANGE_POINT = "select EXCHANGE_POINT,PRODUCT_NAME  from T_POINT_HISTORY where POINT_HISTORY_ID=?";
	/*读取发送短信代理*/
	public static final String SELECT_MEMBER_MSG_IP = "SELECT parameter_value FROM t_member_config WHERE parameter_name = 'MSG_PROXY_IP'";
	public static final String SELECT_MEMBER_MSG_PORT = "SELECT parameter_value FROM t_member_config WHERE parameter_name = 'MSG_PROXY_PORT'";
	public static final String SELECT_CLIENTINFO="select CONNPWD,CINEMACODE from T_INTF_CLIENTINFO where IPADDRESS =? and CONNUSER=?";
	
	public static final String INSERT_TICKET_TRANS_ORDER="INSERT INTO T_TICKET_TRANS_ORDER " +
			"(TRANS_ID,ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,IS_POINT,CINEMA_INNER_CODE," +
			"MEMBER_ID,FILM_CODE,FILM_NAME,HALL_NUM,SHOW_TIME,BIZ_DATE,TRANS_TIME,POINT) " +
			"VALUES (S_T_TICKET_TRANS_ORDER.NEXTVAL,?,?,?,'3',?,?,?,?,?,to_date(?,'YYYY-mm-dd HH24:MI:SS'),to_date(?,'yyyy-MM-dd'),sysdate,?)";
	public static final String INSERT_GOODS_TRANS_ORDER="INSERT INTO T_GOODS_TRANS_ORDER " +
			"(GOODS_TRANS_ID,ORDER_ID,TOTAL_AMOUNT,IS_POINT,TRANS_TYPE,CINEMA_INNER_CODE," +
			"MEMBER_ID,GOOD_NAME,BIZ_DATE,TRANS_TIME,POINT) " +
			"VALUES (S_T_GOODS_TRANS_ORDER.nextval,?,?,'3','Y',?,?,?,to_date(?,'yyyy-MM-dd'),sysdate,?)";
	public static final String QUERY_LAST_TICKET_SHOW_TIME="select to_char(LAST_TICKET_SHOW_TIME,'YYYY-mm-dd HH24:MI:SS') as LAST_TICKET_SHOW_TIME " +
			"from t_mbr_tag_result tr where tr.member_id = ?";
	public static final String QUERY_EVENT_OFFER_CONTENT = 
		"select d.*,ROWNUM from (" +
		"select * from (" +
		"select a.event_name, a.event_code, a.event_cond, b.*,rownum " +
		"from t_event a, t_event_cmn b " +
		"where a.event_id = b.event_id and event_cmn_type = 'intf' and event_source = 'MEMBER_QUERY' " +
		"and b.trigger_system = ? and b.chnl_code = ? and a.event_code in(?, ?) " +
		"union " +
		"select a.event_name, a.event_code, a.event_cond, b.*,rownum " +
		"from t_event a, t_event_cmn b " +
		"where a.event_id = b.event_id and event_cmn_type = 'intf' and event_source = 'MEMBER_QUERY' " +
		"and b.trigger_system = ? and b.chnl_code = ? and b.offer_cond = 'member_level' " +
		"and b.offer_cond_value = ? and a.event_code in(?,?) " +
		"union " + 
		"select a.event_name, a.event_code, a.event_cond, b.*,rownum " + 
		"from t_event a, t_event_cmn b " + 
		"where a.event_id = b.event_id and event_cmn_type = 'intf' and event_source = 'MEMBER_QUERY' " + 
		"and b.trigger_system = ? and b.chnl_code = ? and b.offer_cond = 'has_card' " +
		"and b.offer_cond_value = ? and a.event_code in(?) " +
		") c order by c.event_source, c.chnl_code, c.priority ) d where ROWNUM <= 1";
	public static final String QUERY_EVENT_REG_OFFER_CONTENT = "select a.event_name,a.event_code, a.event_cond, b.* " +
			"from t_event a, t_event_cmn b where a.event_id = b.event_id and a.event_id = 4 " +
			"and event_cmn_type = 'intf' and event_source = 'MEMBER_REGIST' " +
			"and b.trigger_system = ? and b.chnl_code = ? " +
			"order by b.event_source,b.chnl_code,b.priority";
	public static final String QUERY_EVENT_REDEEM_OFFER_CONTENT = "SELECT a.event_name,a.event_code," +
			"a.event_cond,b.* FROM T_EVENT a,T_EVENT_CMN b " +
			"WHERE a.event_id = b.event_id  and a.event_id = 9 and b.event_cmn_type = 'time' and b.event_source = 'REDEEM_ONLINE' " +
			"and b.chnl_code = 'SMS' and b.trigger_system = 'MEMBER'";
	public static final String INSERT_CCS_CARD = "INSERT INTO T_MEM_CARD_INFO (SEQ_ID,CARD_NUMBER," +
			"CARD_TYPE_CODE,CARD_TYPE_NAME,ISSUE_CINEMA,CARD_STATUS,EXPIRY_DATE,BALANCE,CARD_VALUE_TYPE) " +
			"VALUES (S_T_MEM_CARD_INFO.nextval,?,?,?,?,?,to_date(?,'yyyy-mm-dd HH24:MI:SS'),?,?)";
	public static final String INSERT_CCS_CARD_REL = "INSERT INTO T_MEM_CARD_REL (MEMBER_ID,BIND_TYPE," +
			"BIND_TIME,ISUNBIND,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,SEQID,CARD_ID) " +
			"VALUES (?,'0',sysdate,'1',?,sysdate,?,sysdate,0,S_T_MEM_CARD_REL.nextval,?)";
	public static final String DEL_CCS_CARD_OLD_REL = "update t_mem_card_rel set isunbind = 0 " +
			"where member_id = ? and card_id = (select seq_id from t_mem_card_info where card_number = ?)";
	public static final String SELECT_CCS_CARD_INFO = "select * from t_mem_card_info t where t.card_number = ?";
	
	public static final String SELECT_CCS_CARD_INFO_ID = "SELECT t.seq_id AS CARD_ID FROM T_MEM_CARD_INFO t WHERE T.CARD_NUMBER = ?";
	public static final String SELECT_MEMBER_CARD_BY_MEMBERID = "select t.card_number from t_mack_daddy_card t where t.member_id = ?";
	public static final String QUERY_EVENT_MEMBER_FILM = "select a.event_name,a.event_code, a.event_cond, b.* " +
			"from t_event a, t_event_cmn b " +
			"where a.event_id = b.event_id and a.event_id = 10 and event_cmn_type = 'intf' " +
			"and event_source = 'MEMBER_QUERY' and b.trigger_system = ? " +
			"and b.chnl_code = ? and b.offer_cond_value = ? " +
			"order by b.event_source,b.chnl_code,b.priority";
	public static final String QUERY_NAME_GENDER_FROM_MEMBER = "select t.name,t.gender from t_member t where t.member_id = ?";
	
	public static final String SELECT_MSG_SVC_INFO = "select mc.parameter_value from t_member_config mc where mc.parameter_name = ?";
	public static final String SELECT_MSG_CINEMA_INNERCODE = "select c.inner_code from t_member_info mi,t_cinema c where mi.manage_cinema_id = c.seqid and mi.member_id = ?";
	public static final String SELECT_POINT_EXPIRE_DATE = "select to_number(to_char(sysdate,'yyyy'))+1||'-'||'12-31'||' 00:00:00' as expiretime from dual";
	public static final String SELECT_WEB_ORDER_NO = "select * from t_point_history t where t.order_id = ?";
	public static final String UPDATE_WEB_ORDER_NO = "update t_point_history ph set ph.order_id = ?,ph.point_trans_code = ? where ph.order_id = ?";
	public static final String UPDATE_WEB_TICKET_ORDER_NO = "update t_ticket_trans_order o set o.order_id = ? where o.order_id = ?";
	public static final String UPDATE_WEB_GOODS_ORDER_NO = "update t_goods_trans_order o set o.order_id = ? where o.order_id = ?";
	public static final String SELECT_MSG_CONFIG = "select * from t_member_config t where t.parameter_name like 'MSG_%'";
	public static final String INSERT_MEMBER_LOG = "INSERT INTO T_MEMBER_LOG (MEMBER_LOG_ID,MEMBER_ID,MEMBER_MOBILE,CHANGEDBY,CHANGEDDATE,CREATE_BY,CREATE_DATE,UPDATE_DATE,UPDATE_BY,BIRTHDAY) " +
			"VALUES (S_T_MEMBER_LOG.nextval,?,?,?,sysdate,?,sysdate,sysdate,?,to_date(?,'yyyy-mm-dd'))";
	public static final String SELECT_MOBILE_BIRTH_MEMBER = "select m.member_id,m.mobile,to_char(m.birthday,'yyyy-mm-dd') as birthday from t_member m where m.member_no = ?";
	public static final String SELECT_BIRTH_UPDATE_TIME = "select m.birthday_update_time from t_member m where m.member_no = ?";
	public static final String ADD_BIRTH_UPDATE_TIME = "update t_member set birthday_update_time = birthday_update_time + 1 where member_no = ?";
	/**
	 * 解绑券日志
	 */
	public static final String INSERT_MEMBER_VOUCHER_LOG="INSERT INTO T_VOUCHER_POOL_DETAIL_LOG( BAR_CODE, MEMBER_ID,TO_MEMBER_ID, OPERRATE_TYPE) VALUES( ?, ?, ?, ?)";
	
	public static final String CHECK_VOUCHER_REL_MOBILE="select * from T_MEMBER m,T_VOUCHER_POOL_DETAIL p where m.member_id=p.member_id and m.MOBILE=? and p.BAR_CODE=?";
}