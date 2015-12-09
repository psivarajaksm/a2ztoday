<%--
    Document   : LoginPage
    Created on : Jul 6, 2012, 11:12:45 AM
    Author     : root
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
    String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    String loginStaticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
    System.out.println("loginStaticPath====" + loginStaticPath);
%>

<!--<link rel="shortcut icon" href="<%=loginStaticPath%>images/onwardlogo.gif" type="image/x-icon">-->
<%
    String path = request.getContextPath();
    String message = "";
    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");
    }
    request.removeAttribute("message");
%>
<html>
    <head>
        <title><bean:message key="projecttitle" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link href="<%=loginStaticPath%>css/onward.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>

        <script src="<%=loginStaticPath%>scripts/common.js"></script>
        <script src="<%=loginStaticPath%>scripts/blanket.js"></script>
        <script src="<%=loginStaticPath%>scripts/dateValidations.js"></script>
        <script src="dwr/interface/PayBillProcessingAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>       
        <script>
            var noofemployees;
            var empmaster=new Array;            
            var progressstatus=0;
            var serialno="0";
            $(function() {
                $('#processmonth').datepicker({ dateFormat: "mm/yy" }).val();                
                $("#processbutton").click(function(){
                    getBlanket('continueDIV');
                    var processmonth="15/"+document.getElementById('processmonth').value;                    
                    PayBillProcessingAction.preparePayRoll(processmonth,serialno,empmaster[serialno], fillPayBillProcessDetails);                    
                });
                $("#progressbar").progressbar({
                    value: progressstatus
                });
              
            });
            function fillPayBillProcessDetails(map){               
                progressstatus=progressstatus+1;
                $("#progressbar").progressbar({
                    value: progressstatus
                });
                if(progressstatus>=100){
                    progressstatus=0;
                }
                serialno=map.serialno;
                if( noofemployees > serialno){                    
                    var processmonth="15/"+document.getElementById('processmonth').value;
                    PayBillProcessingAction.preparePayRoll(processmonth,serialno,empmaster[serialno], fillPayBillProcessDetails);
                }
                else
                {
                    $("#progressbar").progressbar({
                        value: 100
                    });
                    getBlanket('continueDIV');
                    alert(map.reason);
                }
            }
        </script>
    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center">
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
             <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">               
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">EPF Preparation</td>
                    </tr>
                </table>
                <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">EPF Preparation</td>
                    </tr>
                    <tr class="darkrow">
                         <td width="50%" class="textalign"></td>
                        <td width="50%">
                            <input type="text" id="processmonth"  size="20"/>
                            <input id="processbutton" type="button" class="submitbu" value="Submit" tabindex="26" >&nbsp;&nbsp;&nbsp;
                        </td>
                    </tr>
                </table>
                <br>
                <table width="80%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td>
                            <div id="progressbar"></div>
                        </td>
                    </tr>
                </table>
                <br>
                <div id="processedepfnos"></div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script>
        bodyOnLoad();
        function bodyOnLoad(){
            PayBillProcessingAction.getEmployeeList(fillEmployeemap);
        }
        function fillEmployeemap(map)
        {
            noofemployees = map.length;
            for (var i = 0; i < map.length; i++) {
                empmaster.push( map[i]);
            }            
        }      
    </script>
</html>
