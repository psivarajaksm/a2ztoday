<%-- 
    Document   : ITReport
    Created on : 16 Apr, 2015, 12:33:13 PM
    Author     : Onward
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
        <title>I.T. Report Details</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/IncomeTaxAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <style type="text/css">
            /*demo page css*/
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
            .ui-datepicker-calendar {display: none;}
            .ui-datepicker-month{display: none;}
            #progressbar{
                width: 128px;
                height: 15px;
                text-align: center;
            }
            #printresult{
                color: #E31212;
                font-weight: bold;
                font-size: 12px;
                text-align: center;
            }
        </style>
        <script type="text/javascript">
            var ayear;
            var syear;
            var mmonth;
            var myear;
            $(function() {
                $('#financialyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',
                    onClose: function(dateText, inst) {
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear, 1));
                    }
                });                
            });
            
            function onprintout(){
                var epfno = document.forms[0].epfno.value;
                var reporttype = document.forms[0].reporttype.value;
                var section = document.forms[0].section.value;
                if(syear==null || syear.length==0){
                    alert("Please Select the Start Year");                
                }else if(section=='' ||section=='0'){
                    alert("Please Select the Section Name");
                }else{
                    document.getElementById("printresult").innerHTML = "";
                    document.getElementById("progressbar").style.display='';
                    document.getElementById("printbut").disabled = true;
                    IncomeTaxAction.IncomeTaxParticularsPrintOut(syear, epfno, reporttype, section, CallBackStatus);
                }
            }
            function CallBackStatus(map){
                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.getElementById("progressbar").style.display='none';
                    var reporttype = document.forms[0].reporttype.value;
                    if(reporttype=="1"){
                        document.forms[0].action = "EmployeePayBillAction.do?method=PopupReport";  
                    }else{
                        document.forms[0].action = "AccountsReportsAction.do?method=PopupXMLReport";
                    }
                    document.forms[0].submit();                
                    document.getElementById("printbut").disabled = false;
                }else{
                    document.getElementById("printresult").innerHTML = map.ERROR;
                    document.getElementById("progressbar").style.display='none';
                    document.getElementById("printbut").disabled = false;
                }
            }
        </script>
    </head>
    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Income Tax</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Income Tax Report</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Starting Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="financialyear" id="financialyear" readonly>
                        </td>
                        <td width="20%" class="textalign">Section Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <select class="combobox" name="section" id="section"></select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno">
                        </td>
                        <td width="20%" class="textalign">Report Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="reporttype" id="reporttype">
                                <option value="1">First Quarter</option>
                                <option value="2">Second Quarter</option>
                                <option value="3">Third Quarter</option>
                                <option value="4">Four Quarter</option>                                
                            </select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td colspan="6" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Print" onclick="onprintout();">
                            <input type="reset" class="submitbu" value="Reset">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td colspan="6" align="center">
                            <div id="progressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                            <div id="printresult"></div>
                        </td>
                    </tr>
                </table>
                <br>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            IncomeTaxAction.loadSectionDetails(fillSectionCombo);
        }
        function fillSectionCombo(map){
            dwr.util.removeAllOptions("section");
            dwr.util.addOptions("section",map.sectionlist);
            
        }
    </script>
</html>
