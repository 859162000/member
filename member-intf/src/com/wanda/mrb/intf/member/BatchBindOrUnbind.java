package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;
import com.wanda.mrb.intf.utils.VoucherCodeUtil;
import com.wanda.mrb.intf.utils.VoucherNumberEncoder;
/**
 * 批量绑定解绑券
 * @author Sam Chen
 * @date 2015-07-31
 */
public class BatchBindOrUnbind extends ServiceBase {

	private int memberId;
	private String voucherNumbers;
	private String bindFlag;// 绑定类型
	private String bindMemberId;// 用来判断是否被绑定
	private boolean inPoolFlag = false;// 是否在库中
	private boolean bindMemberFlag = false;// 用来判断是否被绑定
	private boolean bindThisMemberFlag = false;// 用来判断是否被绑定到该会员
	
	public BatchBindOrUnbind() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_BATCHBIND;
		this.timeOutFlag = true;
	}

	@Override
	protected void bizPerform() throws Exception {
		List<String> voucherNumList = new ArrayList<String>();
		Connection conn = getDBConnection();
		memberId = this.checkMember(conn,memberNo);

		String[] vStrs = voucherNumbers.trim().split("\\,");
		voucherNumList = Arrays.asList(vStrs);
		if(voucherNumList.size()>6){
			throwsBizException("M090011", "被操作的券的个数不能超出6个"+":"+voucherNumbers);
		}
		for(String voucherNumber : voucherNumList){
			// 判断券是否存在
			ResultQuery rsq = SqlHelp.query(conn,
					SQLConstDef.QUERY_VOUCHER_BY_BARCODE,
					VoucherNumberEncoder.md5Encrypt(voucherNumber.trim()));
			ResultSet rs = rsq.getResultSet();
			if (rs == null || !rs.next()) {
				throwsBizException("M090001", voucherNumber.trim()+":券不存在或券不可用！");
			}
			rsq.free();
			// 判断券是否在券库中，是否已绑定
			rsq = SqlHelp.query(conn, SQLConstDef.QUERY_VOUCHER_POOL_BY_BARCODE,VoucherNumberEncoder.md5Encrypt(voucherNumber.trim()));
			rs = rsq.getResultSet();
			if (rs != null && rs.next()) {
				bindMemberId = rs.getString("MEMBER_ID");
				inPoolFlag = true;// 在券库中
				// 已绑定
				if (bindMemberId != null && !"".equals(bindMemberId)) {
					bindMemberFlag = true;// 已绑定
					// 判断该券是否绑定到了该会员
					PreparedStatement psBindThisMember = conn.prepareStatement(SQLConstDef.CHECK_VOUCHER_REL);
					psBindThisMember.setString(1, this.memberNo);
					psBindThisMember.setString(2,VoucherNumberEncoder.md5Encrypt(voucherNumber.trim()));
					ResultSet rsBindThisMember = psBindThisMember.executeQuery();
					if (rsBindThisMember != null && rsBindThisMember.next()) {
						bindThisMemberFlag = true;// 券绑定到了该会员
					} else {
						bindThisMemberFlag = false;// 券没有绑定到该会员
					}
				} else {
					bindMemberFlag = false;// 未绑定
				}
			} else {
				inPoolFlag = false;// 不在券库中
			}
			rsq.free();
			// 开始进行绑定
			if ("1".equals(bindFlag)) {// 1.绑定
				if (inPoolFlag) {// 在券库中
					if (bindMemberFlag && !bindThisMemberFlag) {// 该券如果绑定了会员但没有绑定该会员
						throwsBizException("M090002", "该券"+voucherNumber+"已经绑定到其他会员！");
					} else if(bindThisMemberFlag){
						throwsBizException("M090007", "该券"+voucherNumber+"已经绑定到该会员！");
					} else {// 只要该券没有绑定到其他会员，就可以进行绑定
						SqlHelp.operate(conn,SQLConstDef.UPDATE_MEMBER_VOUCHER_REL,
								String.valueOf(memberId),
								"2", //补充缺少的参数，Fixed by Zhang Chen Long(2014-04-29)
								VoucherNumberEncoder.md5Encrypt(voucherNumber));
					}
				} else {// 不在券库中，绑定
					SqlHelp.operate(conn, SQLConstDef.INSERT_MEMBER_VOUCHER_REL,
							VoucherCodeUtil.desEncrypt(voucherNumber),
							VoucherNumberEncoder.md5Encrypt(voucherNumber),
							String.valueOf(memberId),
							"2");
				}
			} else if ("2".equals(bindFlag)) {// 2.解绑
				if (!bindMemberFlag) {
					throwsBizException("M090003", "该券"+voucherNumber+"未绑定会员,不能解绑！");
				} else if (!bindThisMemberFlag) {// 该券和该会员没有绑定关系
					throwsBizException("M090004", ":该券"+voucherNumber+"和该会员没有绑定关系,不能解绑！");
				} else {// 解绑
					SqlHelp.operate(conn, SQLConstDef.DELETE_MEMBER_VOUCHER_REL,
							VoucherNumberEncoder.md5Encrypt(voucherNumber));
				}
			} else if ("3".equals(bindFlag)) {// 3.赠送
				if (!bindThisMemberFlag) {
					throwsBizException("M090005", "该会员没有绑定该券"+voucherNumber+",该会员不能赠送该券！");
				} else {
					//解绑
					SqlHelp.operate(conn, SQLConstDef.DELETE_MEMBER_VOUCHER_REL,
							VoucherNumberEncoder.md5Encrypt(voucherNumber));
					//送人
					SqlHelp.operate(conn,
							SQLConstDef.UPDATE_MEMBER_VOUCHER_REL,
							String.valueOf(memberId),
							"3",
							VoucherNumberEncoder.md5Encrypt(voucherNumber));
				}
			}
			rsq.free();
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root, "MEMBER_NO", 64);
		voucherNumbers = getChildValueByName(root, "VOUCHER_NUMBERS", 1000);
		if("".equals(voucherNumbers)||null == voucherNumbers||voucherNumbers.trim().length()<=0){
			throwsBizException("M090006", "VOUCHER_NUMBERS券码为空！");
		}
		bindFlag = getChildValueByName(root, "BIND_TYPE", 2);
		if("".equals(bindFlag)){
			throwsBizException("M090006", "绑定类型不能为空！");
		}
	}

	@Override
	protected String composeXMLBody() {
		return null;
	}

}
