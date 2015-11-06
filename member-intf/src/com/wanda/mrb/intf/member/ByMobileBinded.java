package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.AES;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;
import com.wanda.mrb.intf.utils.VoucherCodeUtil;
import com.wanda.mrb.intf.utils.VoucherNumberEncoder;
/**
 * 根据手机号绑定密文券号
 * @author Sam Chen
 *
 */
public class ByMobileBinded extends ServiceBase {
	private int memberId;
	private String voucherNumber;
	private String bindFlag;// 绑定类型
	private boolean inPoolFlag = false;// 是否在库中
	private String bindMemberId;// 用来判断是否被绑定
	private boolean bindMemberFlag = false;// 用来判断是否被绑定
	private boolean bindThisMemberFlag = false;// 用来判断是否被绑定到该会员
	private String orderNumber;
	private String voucherOrderId;

	public ByMobileBinded() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_BING_BYMOBILE;
		this.timeOutFlag = true;
	}

	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		memberId = this.checkMemberByMobile(conn, memberNo);
		// 判断 销售单是否存在 存在取出voucher_order_id
		ResultQuery rsq1 = SqlHelp.query(conn, SQLConstDef.QUERY_VOUCHER_ORDER,
				orderNumber);
		ResultSet rs = rsq1.getResultSet();
		if (rs == null || !rs.next()) {
			throwsBizException("M090021", "销售单不存在！");
		}
		voucherOrderId = rs.getString("VOUCHER_ORDER_ID");
		rsq1.free();
		// 判断券是否存在
		ResultQuery rsq = SqlHelp.query(conn,
				SQLConstDef.QUERY_VOUCHER_BY_BARCODE,
				VoucherNumberEncoder.md5Encrypt(AES.decrypt(
						voucherNumber, voucherOrderId)));
		rs = rsq.getResultSet();
		if (rs == null || !rs.next()) {
			throwsBizException("M090001", "券不存在或券不可用！");
		}
		rsq.free();

		// 判断券是否在券库中，是否已绑定
		rsq = SqlHelp.query(conn, SQLConstDef.QUERY_VOUCHER_POOL_BY_BARCODE,
				VoucherNumberEncoder.md5Encrypt(voucherNumber));
		rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			bindMemberId = rs.getString("MEMBER_ID");
			inPoolFlag = true;// 在券库中
			if (bindMemberId != null && !"".equals(bindMemberId)) {// 已绑定
				bindMemberFlag = true;// 已绑定
				// 判断该券是否绑定到了该会员
				PreparedStatement psBindThisMember = conn
						.prepareStatement(SQLConstDef.CHECK_VOUCHER_REL);
				psBindThisMember.setString(1, this.memberNo);
				psBindThisMember.setString(2,
						VoucherNumberEncoder.md5Encrypt(this.voucherNumber));
				ResultSet rsBindThisMember = psBindThisMember.executeQuery();
				if (rsBindThisMember != null && rsBindThisMember.next()) {
					bindThisMemberFlag = true;// 券绑定到了该会员
				} else {
					bindThisMemberFlag = false;// 券没有绑定到该会员
				}
			} else {// 未绑定
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
					throwsBizException("M090002", "该券已经绑定到其他会员！");
				} else if (bindThisMemberFlag) {
					throwsBizException("M090007", "该券已经绑定到该会员！");
				} else {// 只要该券没有绑定到其他会员，就可以进行绑定
					SqlHelp.operate(conn,
							SQLConstDef.UPDATE_MEMBER_VOUCHER_REL,
							String.valueOf(memberId), "2", // 补充缺少的参数，Fixed by
															// Zhang Chen
															// Long(2014-04-29)
							VoucherNumberEncoder.md5Encrypt(voucherNumber));
				}
			} else {// 不在券库中，绑定
				SqlHelp.operate(conn, SQLConstDef.INSERT_MEMBER_VOUCHER_REL,
						VoucherCodeUtil.desEncrypt(voucherNumber),
						VoucherNumberEncoder.md5Encrypt(voucherNumber),
						String.valueOf(memberId), "2");
			}
		} else if ("2".equals(bindFlag)) {// 2.解绑
			if (!bindMemberFlag) {
				throwsBizException("M090003", "该券未绑定会员，不能解绑！");
			} else if (!bindThisMemberFlag) {// 该券和该会员没有绑定关系
				throwsBizException("M090004", "该券和该会员没有绑定关系，不能解绑！");
			} else {// 解绑
				SqlHelp.operate(conn, SQLConstDef.DELETE_MEMBER_VOUCHER_REL,
						VoucherNumberEncoder.md5Encrypt(voucherNumber));
			}
		}
		rsq.free();
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root, "MEMBER_MOBILE", 64);
		voucherNumber = getChildValueByName(root, "VOUCHER_NUMBER", 64);
		bindFlag = getChildValueByName(root, "BIND_TYPE", 64);
		orderNumber = getChildValueByName(root, "ORDER_NUMBER", 100);
		if ("".equals(bindFlag)) {
			throwsBizException("M090006", "绑定类型不能为空！");
		}
		if ("".equals(memberNo)) {
			throwsBizException("M090007", "手机号不能为空！");
		}
		if ("".equals(voucherNumber)) {
			throwsBizException("M090008", "券码不能为空！");
		}
		if ("".equals(orderNumber)) {
			throwsBizException("M090009", "销售单号不能为空！");
		}
	}

	@Override
	protected String composeXMLBody() {
		return null;
	}
}
