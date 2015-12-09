<%-- 
    Document   : CashBookAbstract
    Created on : May 17, 2013, 10:08:25 AM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>Cash Book Abstract Report</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/AccountsReportsAction.js"></script>
        <script src="dwr/interface/AccountsVoucherAction.js"></script>
        <script src="dwr/interface/CashierEntryAction.js"></script>
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
                $(function() {
                    $('#startingdate').datepicker({
                        dateFormat: "dd/mm/yy",
                        maxDate: "+0m" ,
                        changeMonth: true,
                        changeYear: true
                    }).val();
                });
                $(function() {
                    $('#enddate').datepicker({
                        dateFormat: "dd/mm/yy",
                        maxDate: "+0m" ,
                        changeMonth: true,
                        changeYear: true
                    }).val();
                });
            });
        </script>
    </head>

    <body>		<!-- Tabs -->
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Cash Book Abstract Report</td>
                    </tr>
                </table>
                <table width="70%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Cash Book Abstract Report</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">From Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="startingdate" id="startingdate" readonly>
                        </td>
                        <td width="20%" class="textalign">To Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" >
                            <input type="text" class="textbox" name="enddate" id="enddate" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Cash Book Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="ctype" id="ctype">
                                <option value="0">Select</option>
                                <option value="P">Payment</option>
                                <option value="R">Receipt</option>
                                <option value="B">Bank</option>
                                <option value="J">Journal</option>
                            </select>
                        </td>
                        <td width="20%" class="textalign">A/C Book Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="accbook" id="accbook" class="textfieldalign"></select>
                            <!--                            <select class="combobox" name="absttype" id="absttype">
                                                            <option value="0">Select</option>
                                                            <option value="D">Day Wise Abstract</option>
                                                            <option value="M">Month Wise Abstract</option>
                                                        </select>-->
                        </td>
                    </tr>
                    <!--                    <tr class="lightrow">
                                            <td width="20%" class="textalign">Account Head</td>
                                            <td width="5%" class="mandatory">*</td>
                                            <td width="25%" class="textfieldalign" >
                                                <select class="combobox" name="ledger" id="ledger"></select>
                                            </td>
                                            <td width="5%" colspan="3" class="mandatory"></td>

                                        </tr>-->
                    <tr class="darkrow">
                        <td colspan="6" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Print" onclick="onprintout();">
                            <input type="reset" class="submitbu" value="Reset">
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
        bodyOnload();
        function bodyOnload(){
            CashierEntryAction.getAccountBook(fillCashBookCombo)
        }
        function fillCashBookCombo(map){
            dwr.util.removeAllOptions("accbook");
            dwr.util.addOptions("accbook",map.accountbooklist);
        }

        function onprintout(){
            var startingdate = document.getElementById('startingdate').value;
            var enddate = document.getElementById('enddate').value;
            var ctype = document.getElementById('ctype').value;
            var accbook = document.getElementById('accbook').value;
            if(startingdate==""){
                alert("Please select the From date");
                document.getElementById('startingdate').focus();
                return false;
            }else if(enddate==""){
                alert("Please Select the end date");
                document.getElementById('enddate').focus();
                return false;
            }else if(ctype==""){
                alert("Please Select the cash book type");
                document.getElementById('ctype').focus();
                return false;
            }else if(accbook==""){
                alert("Please Select the Account book type");
                document.getElementById('accbook').focus();
                return false;
            }else{
                //                var acccode=document.getElementById("ledger").options[document.getElementById("ledger").selectedIndex].text;
                //                getBlanket('continueDIV');
                var url="AccountsReportsAction.htm?method=cashBookAbstractPrint&fromDate="+startingdate+"&toDate="+enddate+"&ctype="+ctype+"&accbook="+accbook;
                window.open(url, "Cash Book Abstract Report", "width=1000,height=800");
                //                getBlanket('continueDIV');
                return false;
            }
        }
    </script>
</html>
