<%--
Document   : jquerys
Created on : Jul 5, 2012, 10:26:37 AM
Author     : root
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
            String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>

<%
            String message = "";
            if ((request.getAttribute("message") != null)) {
                message = (String) request.getAttribute("message");
            }
            request.removeAttribute("message");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Employee Salary Details</title>        
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <!--        <script src="dwr/interface/EmployeePayBillAction.js"></script>-->
        <script src="dwr/interface/SupplementaryBillAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <style type="text/css">
            /*demo page css*/
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
        </style>
        <script type="text/javascript">
            $(function() {
                $('#asondate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();
            });
        </script>

        <script type="text/javascript">
            function onprintout(){
                var asondate = document.forms[0].asondate.value;
                document.getElementById("paybillprintresult").innerHTML = "";
                document.getElementById("paybillprogressbar").style.display='';
                document.getElementById("printbut").disabled = true;
                SupplementaryBillAction.EmployeeSupplementaryIncrementArrearAcquitanceDetails(asondate, EmployeePayBillPrintStatus);
            }
            function EmployeePayBillPrintStatus(map){

                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.forms[0].action = "EmployeePayBillAction.do?method=PopupReport";
                    document.forms[0].submit();
                    document.getElementById("printbut").disabled = false;
                }else{
                    document.getElementById("paybillprintresult").innerHTML = map.ERROR;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.getElementById("printbut").disabled = false;
                }
            }
        </script>
    </head>

    <body>		<!-- Tabs -->
        <form method="post">
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <table width="100%" align="center" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Increment Arrear Acquitance</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">As on Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="asondate" id="asondate">
                        </td>
                        <td width="50%" colspan="3"></td>
<!--                        <td width="20%" class="textalign">PF Number</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="pfno" id="pfno" maxlength="15">
                        </td>-->
                    </tr>
                    <tr class="darkrow">
                        <td colspan="6" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Print" onclick="onprintout();">
                            <input type="reset" class="submitbu" value="Reset">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td colspan="6" align="center">
                            <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                            <div id="paybillprintresult"></div>
                        </td>
                    </tr>

                </table>    
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isnewmap" id="isnewmap" value="0">
            <input type="hidden" name="serialno" id="serialno" value="0">
            <input type="hidden" name="regionname" id="regionname" value="0">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">

        </form>

    </body>
</html>