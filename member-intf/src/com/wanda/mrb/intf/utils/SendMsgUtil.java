/**
 * 
 */
package com.wanda.mrb.intf.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.smsplatform.server.ServiceClient;
import com.hp.smsplatform.server.ServiceUrl;
import com.hp.smsplatform.server.rabbitmq.WriteToQueue;
import com.hp.smsplatform.server.webservice.bean.Settle;
import com.hp.smsplatform.server.webservice.bean.SettleResult;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.bean.MsgIpConfigBean;

/**
 * @author xuesi
 *
 */
public class SendMsgUtil {
	/**
	 * @param msgSvcIp	短信代理地址
	 * @param msgChannelId	通道号
	 * @param mobileNo	手机号
	 * @param systemId	系统编号
	 * @param msgContent	短信内容
	 * @throws Exception 
	 */
	public static void sendMsgCheckCode(Connection conn, String msgSvcIp,String msgChannelId,String mobileNo,String systemId,String msgContent) throws Exception{
		try {
			PreparedStatement ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
			ps.setString(1, "MSG_OPEN");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String isMsgOpen = rs.getString("parameter_value");
				if ("0".equals(isMsgOpen)) {
					systemId = "001";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = sendSMSNew(msgSvcIp, mobileNo, msgContent,"MW2");
		System.out.println("send sms resutl " + result + " send mobile is " + mobileNo);
	}
	
	public static MsgIpConfigBean getMsgIpConfig(Connection conn){
		MsgIpConfigBean msgIpMap = new MsgIpConfigBean();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
			ps.setString(1, "MSG_MQ_IP");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				msgIpMap.setMsgMqIp(rs.getString("parameter_value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msgIpMap;
	}
	
	/**
	 * 
	 * @param url 短信服务url
	 * @param mobile 接收短信手机号
	 * @param sendcontent 短信内容
	 * @return 发送结果 
	 * "success","S001"," the mobile param is null ","S002",
    " the send data param is null ","S003"," send faild ","S004"," no param ","S005";
	 */
    public static String sendSMSNew(String url, String mobile,String sendContent,String smstype) {
        PrintWriter out = null;
        String result = "";
        try {
        	StringBuffer jsonstr = new StringBuffer();
    		jsonstr.append("{").append("\"mobile\"").append(":\"").append(mobile).append("\",").append("\"smsdata\"").append(":\"").append(sendContent).append("\",").append("\"smstype\"").append(":\"").append(smstype).append("\"}");
    		System.out.println(jsonstr.toString());
    		url += "/"+jsonstr.toString();
            URL urlconn = new URL(url);
			//使用代理打开网页
			HttpURLConnection action = (HttpURLConnection) urlconn.openConnection(); 
			BufferedReader br = new BufferedReader(new InputStreamReader(action.getInputStream(),"UTF-8"));
			String line = "";
			 while ((line = br.readLine()) != null) {
	                result += line;
	            }
			br.close();
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("发送 POST 请求出现异常！"+e);
            result = e.getLocalizedMessage(); 
        }
        //使用finally块来关闭输出流、输入流
        finally{
            if(out!=null){
			    out.close();
			}
        }
        System.out.println("sms send resut is " + result + "send mobile is " + mobile);
        return result;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			sendMsgCheckCode(null, null, null, null, "234242", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
