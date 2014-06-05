/**
 * 
 */
package org.tempuri;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

/**
 * @author Administrator
 * 
 */
public class WebServiceTest {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		WmgwLocator wmgwLocator = new WmgwLocator();
		
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", "proxy1.wanda.cn");
		System.getProperties().put("proxyPort", "8080");


		String strArgs[] = new String[10];
		strArgs[0] = "JC2059";		//用户名
		strArgs[1] = "060033";		//密码
		strArgs[2] = "13428989579";//手机号
		strArgs[3] = "A+B C";	//短信内容
		strArgs[4] = "1";			//类型 
		strArgs[5] = "*";			//
		String strMsg = new String(strArgs[3].getBytes("UTF-8"));//web发送格式转换
		//mongateCsSendSmsEx
		try {
			System.out.println("Test mongateCsSendSmsEx ...");
			System.out.println("back value is :"
					+ wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(strArgs[0],
							strArgs[1], strArgs[2], strMsg, Integer.valueOf(strArgs[4]).intValue()));
			System.out.println("send mongateCsSendSmsEx end !");
			System.out.println();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mongateCsSendSmsExNew
		//MongateCsSpSendSmsNew
		try {
			System.out.println("Test mongateCsSendSmsExNew ...");
			System.out.println("back value is :"
					+ wmgwLocator.getwmgwSoap().mongateCsSpSendSmsNew(strArgs[0],
							strArgs[1], strArgs[2], strMsg, Integer.valueOf(strArgs[4]).intValue(),strArgs[5]));
			System.out.println("send mongateCsSendSmsExNew end !");
			System.out.println();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mongateCsGetStatusReportExEx
		try {
			System.out.println("Test mongateCsGetStatusReportExEx ...");
			String[] strRet = wmgwLocator.getwmgwSoap().mongateCsGetStatusReportExEx(strArgs[0], strArgs[1]);
			System.out.println("back value is :");
			if (strRet != null)
			{
				for(int i = 0; i < strRet.length; ++i)
				{
					System.out.println(strRet[i]);
				}
			}
			else
			{
				System.out.println("null");
			}
			System.out.println("send mongateCsGetStatusReportExEx end !");
			System.out.println();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//mongateQueryBalance
		try {
			System.out.println("Test mongateQueryBalance ...");
			System.out.println("back value is :"
					+ wmgwLocator.getwmgwSoap().mongateQueryBalance(strArgs[0], strArgs[1]));
			System.out.println("send mongateQueryBalance end !");
			System.out.println();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mongateCsGetSmsExEx
		try {
			System.out.println("Test mongateCsGetSmsExEx ...");
			String[] strRet = wmgwLocator.getwmgwSoap().mongateCsGetSmsExEx(strArgs[0], strArgs[1]);
			System.out.println("back value is :");
			if (strRet != null)
			{
				for(int i = 0; i < strRet.length; ++i)
				{
					System.out.println(strRet[i]);
				}
			}
			else
			{
				System.out.println("null");
			}
			System.out.println("send mongateCsGetSmsExEx end !");
			System.out.println();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
