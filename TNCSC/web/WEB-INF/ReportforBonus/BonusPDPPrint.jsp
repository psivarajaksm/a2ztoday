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
        <script src="dwr/interface/BonusBillAction.js"></script>
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

            var amonth;
            var ayear;
            var smonth;
            var syear;
            var mmonth;
            var myear;

            $(function() {
                $('#payslipstartingdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear,smonth, 1));
                    }
                });
                $('#payslipenddate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        amonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        ayear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(ayear,amonth, 1));
                    }
                });
            });

            function onprintout(){
                //                var startmonth = document.forms[0].payslipstartingdate.value;
                //                var endmonth = document.forms[0].payslipenddate.value;
                //                var startyear = document.forms[0].payslipstartingdate.value;
                //                var endyear = document.forms[0].payslipenddate.value;
                var pfno = document.forms[0].pfno.value;
                var category = document.forms[0].category.value;
                if(smonth==null ||smonth.length==0 || syear==null || syear.length==0){
                    alert("Please Select the Start Month and Year");
                }else if(amonth==null ||amonth.length==0 || ayear==null ||ayear.length==0){
                    alert("Please Select the End Month and Year");
                }
                var section = document.forms[0].section.value;
                document.getElementById("paybillprintresult").innerHTML = "";
                document.getElementById("paybillprogressbar").style.display='';
                document.getElementById("printbut").disabled = true;
                BonusBillAction.EmployeeBonusPDPPrintOut(smonth, syear,amonth, ayear, pfno, category, section,  EmployeePayBillPrintStatus);
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
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>
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
                        <td width="100%" align="center" class="headerdata">Bonus</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Pay Drawn particulars</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Starting Month and Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="payslipstartingdate" id="payslipstartingdate" readonly>
                        </td>
                        <td width="20%" class="textalign">End Month and Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" >
                            <input type="text" class="textbox" name="payslipenddate" id="payslipenddate" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="pfno" id="pfno">
                        </td>
                        <td width="20%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="category" id="category">
                                <option value="R">Regular</option>
                                <option value="O">Others</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Section Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="section" id="section"></select>
                        </td>
                        <td width="20%" class="textalign"></td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign" ></td>
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


