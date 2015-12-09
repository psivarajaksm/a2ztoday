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
        <script src="dwr/interface/AccountsReportsAction.js"></script>
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
        <style type="text/css">
            #paybillprogressbar{
                width: 128px;
                height: 15px;
                text-align: center;
            }
            #paybillprintresult{
                color: #E31212;
                font-weight: bold;
                font-size: 12px;
                text-align: center;
            }
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

            function trim(text) {
                return text.replace(/^\s+|\s+$/g, "");
            }

            function onprintout(Obj){

                var chno,chtype;

                if(Obj.vno.length==undefined){
                    if (Obj.vno.checked){
                        chno = Obj.vno.value;
                        chtype = document.getElementById("chtype").value;
                        document.getElementById("paybillprintresult").innerHTML = "";
                        document.getElementById("paybillprogressbar").style.display='';
                        document.getElementById("printbut").disabled = true;
                        AccountsReportsAction.BankChallanPrintOut(chno, chtype, EmployeePayBillPrintStatus);
                    }
                }else{
                    if(Obj.vno.length == 0){
                        alert("Please Select the Challan No");
                        return false;
                    }else{
                        for (i = 0; i < Obj.vno.length; i++){
                            if (Obj.vno[i].checked){
                                chno = Obj.vno[i].value
                            }
                        }
                        chtype = document.getElementById("chtype").value;
                        document.getElementById("paybillprintresult").innerHTML = "";
                        document.getElementById("paybillprogressbar").style.display='';
                        document.getElementById("printbut").disabled = true;
                        AccountsReportsAction.BankChallanPrintOut(chno, chtype, EmployeePayBillPrintStatus);
                    }
                }
            }
            function EmployeePayBillPrintStatus(map){

                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                    var urladdress = map.urladdress;
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.forms[0].action = "AccountsReportsAction.do?method=PopupPDFReport";
                    document.forms[0].submit();
                    document.getElementById("printbut").disabled = false;
                }else{
                    document.getElementById("paybillprintresult").innerHTML = map.ERROR;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.getElementById("printbut").disabled = false;
                }

            }
            function onShow(){
                var challantype = document.forms[0].challantype.value;
                var startingdate = document.forms[0].startingdate.value;
                var enddate = document.forms[0].enddate.value;
                var challanno = document.forms[0].challanno.value;
                document.getElementById('receiptgrid').innerHTML="";
                AccountsReportsAction.getChallanDetails(startingdate, enddate,challantype,challanno, LoadChallanGrid);
            }
            function LoadChallanGrid(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    return false;
                } else {
                    document.getElementById('receiptgrid').innerHTML=map.challangrid;
                    document.getElementById("chtype").value = map.challantype;
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
                        <td width="100%" align="center" class="headerdata">Bank challan</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Bank challan Report</td>
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
                        <td width="20%" class="textalign">Challan Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="challantype" id="challantype">
                                <option value="cash">Cash Challan</option>
                                <option value="chdd">Cheque/DD Challan</option>
                            </select>
                        </td>
                        <td width="20%" class="textalign">Challan No</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%"class="textfieldalign" >
                            <input type="text" class="textbox" name="challanno" id="challanno">
                            <input type="button" class="submitbu" id="search" value="Search" onclick="onShow();">
                        </td>
                    </tr>

                    <tr class="darkrow">
                        <td width="100%" colspan="6" align="center">
                            <div id="receiptgrid"></div>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td colspan="6" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Print" onclick="onprintout(this.form);">
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
                <br>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
            <input type="hidden" name="urladdress" id="urladdress">
            <input type="hidden" name="chtype" id="chtype">
        </form>
    </body>
</html>


