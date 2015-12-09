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
    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");
    }
    request.removeAttribute("message");
%>
<%
            String ledgerList = "";
            if (request.getSession().getAttribute("ledgerList") != null) {
                ledgerList = (String) request.getSession().getAttribute("ledgerList");
            }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>File Upload</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/BudgetAction.js"></script>
        <script src="dwr/interface/FileUploadAction.js"></script>        
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <script type="text/javascript">
           function prepareControlOfExpenditure(){   
                var periodsele = document.getElementById('accountingperiod').value;  
                var voucherdatefrom = document.getElementById('accountingperiod').options[accountingperiod.selectedIndex].innerHTML;;  
//                var voucherdateto = document.getElementById('voucherdateto').value;  
                BudgetAction.prepareControlOfExpenditure(periodsele, voucherdatefrom, '', controlOfExpenditureBoardDownload);
            }
            
            function controlOfExpenditureBoardDownload(map){
                var filePath = map.filePath;
                var fileName = map.fileName;
                document.getElementById("filePath").value = filePath;
                document.getElementById("fileName").value = fileName;
                //                    document.getElementById("paybillprogressbar").style.display='none';
                document.forms[0].action = "EmployeePayBillAction.do?method=PopupReport";
                document.forms[0].submit();

            }
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>
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
                <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" class="headerdata"><bean:message key="ControlofExpenditure" /></td>
                                </tr>
                            </table>
                            <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="49%" align="center" valign="top">
                                        <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">
                                            <tr class="lightrow">
                                                <td width="30%" class="textalign">Accounting Period</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="65%" colspan="2" class="textfieldalign" >
                                                    <select class="combobox" name="accountingperiod" id="accountingperiod" class="textfieldalign" ></select>
                                                </td>
                                            </tr> 
                                            <tr class="darkrow">
                                                <td width="30%" class="textalign"></td>
                                                <td width="5%" class="mandatory"></td>
                                                <td width="65%" colspan="2" class="textfieldalign" >
                                                    <input type="button" class="submitbu" id="printbut" value="Print" onclick="prepareControlOfExpenditure();">
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
            <div id="budgetdetails" style="display:none;height:300px;overflow:auto;"></div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="budgetdetailsid" id="budgetdetailsid"  value="0" >
            <input type="hidden" name="ledgerid" id="ledgerid"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">
        onloadData();
        function onloadData(){            
            BudgetAction.loadBudgetYearDetails(fillAccountingPeriod);
        }       
        function fillAccountingPeriod(map){
            dwr.util.removeAllOptions("accountingperiod");
            dwr.util.addOptions("accountingperiod",map.budgetlist);
        }
    </script>
</html>

