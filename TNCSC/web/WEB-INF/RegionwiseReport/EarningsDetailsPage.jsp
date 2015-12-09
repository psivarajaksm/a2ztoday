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

            String earninglist = "";
            String regionlist = "";

            if (request.getSession().getAttribute("earninglist") != null) {
                earninglist = (String) request.getSession().getAttribute("earninglist");
            }
            if (request.getSession().getAttribute("regionlist") != null) {
                regionlist = (String) request.getSession().getAttribute("regionlist");
            }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Employee Schedule</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/RegionwiseReportAction.js"></script>
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

            function onShow(){
                document.getElementById("showgrid").innerHTML =  "";
                var epfno = document.forms[0].epfno.value;
                var earningid = document.forms[0].earningid.value;
                var region = document.forms[0].region.value;
                if(smonth==null ||smonth.length==0 || syear==null || syear.length==0){
                    alert("Please Select the Start Month and Year");
                }
                if(amonth==null ||amonth.length==0 || ayear==null || ayear.length==0){
                    alert("Please Select the End Month and Year");
                }
                getBlanket('continueDIV');
                RegionwiseReportAction.getEarningDetailsGrid(syear, smonth,ayear, amonth, epfno, earningid, region, ShowScheduleGrid);
            }

            function ShowScheduleGrid(map){
                getBlanket('continueDIV');
                if(map!=null){
                    document.getElementById("showgrid").innerHTML = map.schedulegrid;
                }else{
                    document.getElementById("showgrid").innerHTML = "There is No Record for the Given Inputs";
                }
            }
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
            #scrolling {
                /**allow ample width for table, padding and borders**/
                width: auto;
                /**adjust as needed. If no height is declared, scrollbar won't appear**/
                height: 200px;
                /**vertical scrollbar**/
                overflow-y:scroll;
            }
        </style>
    </head>

    <body>		<!-- Tabs -->
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
                        <td width="100%" align="center" class="headerdata">EARNINGS DETAILS</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Earnings Details</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">From Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="payslipstartingdate" id="payslipstartingdate" readonly>
                        </td>
                        <td width="20%" class="textalign">To Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" >
                            <input type="text" class="textbox" name="payslipenddate" id="payslipenddate" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">EPF Number</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno">
                        </td>
                        <td width="20%" class="textalign">Earnings Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <%=earninglist%>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Region</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <%=regionlist%>
                            <input type="button" class="submitbu" value="Show" onclick="onShow();">
                        </td>
                        <td width="50%" colspan="3"></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="80%" colspan="6">
                            <div id="showgrid" align="center"></div>
                        </td>
                    </tr>
                    <!--                    <tr class="darkrow">
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
                                        </tr>-->
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
</html>


