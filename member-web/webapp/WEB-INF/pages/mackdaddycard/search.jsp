<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.aggrepoint.com/adk" prefix="adk"%>
<adk:form name="frmSearch" action="search">
	<table width="100%" height="18" border="0">
         <tr>
             <td align="right" valign="top" nowrap="nowrap" width="100"> 申请人:</td>
             <td align="left" valign="top" nowrap="nowrap" width="100">
             	<input type="text" name="submitBy" value="${query.submitBy}" class="txtinput_wid90" />
           	 </td>
           	  <td align="left">
           	 <button type="submit" class="btn search" >查询</button>
           	 </td>
         </tr>
    </table>
    
</adk:form>