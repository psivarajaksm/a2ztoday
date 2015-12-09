<%-- 
    Document   : MastersUploadPage
    Created on : Nov 30, 2012, 10:34:06 AM
    Author     : root
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
    String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    String message = "";
    if ((request.getAttribute("status") != null)) {
        message = (String) request.getAttribute("status");        
    }
    request.removeAttribute("status");
    
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>File Upload</title>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script type="text/javascript">
            function onprintout(){                
                document.forms[0].action = "PayBillMasterAction.htm?method=mastersDatasUploadSave";
                document.forms[0].submit();
            }
        </script>
    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center" >
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post" enctype="multipart/form-data">

            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <div>
                    <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                        <tr>
                            <td>
                                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="100%" align="center" class="headerdata">Masters Data  Upload</td>
                                    </tr>
                                </table>

                                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="49%" align="center" valign="top">
                                            <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td colspan="4" class="mainheader">Masters Data Upload</td>
                                                </tr>
                                                <tr class="lightrow">
                                                    <td width="30%" class="textalign">Upload</td>
                                                    <td width="5%" class="mandatory">*</td>
                                                    <td width="65%" colspan="2" class="textfieldalign" >
                                                        <input type="file" id="uFile" name="uFile" />
                                                    </td>
                                                </tr>
                                                <tr class="darkrow">
                                                    <td width="30%" class="textalign"></td>
                                                    <td width="5%" class="mandatory"></td>
                                                    <td width="65%" colspan="2" class="textfieldalign" >
                                                        <input type="button" class="submitbu" id="printbut" value="Upload" onclick="onprintout();">
                                                        <input type="reset" class="submitbu" value="Reset">
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>            
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">            
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
            
        </form>
    </body>
    <script>
     bodyOnload();
    function bodyOnload(){
        var message = '<%=message%>';
        if(message!=null && message!=""){            
            if(message=="success"){
                alert("Given Master Files Successfully Uploaded");
            }else{
                alert("Given Text File is Failed to upload");
            }
        }

    }
    </script>
</html>


