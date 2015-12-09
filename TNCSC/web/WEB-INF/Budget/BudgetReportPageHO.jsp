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
            String regionlist = "";
            if (request.getSession().getAttribute("regionList") != null) {
                regionlist = (String) request.getSession().getAttribute("regionList");
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
            var startyear=0;
            var endyear=0;
            $(function() {
                $('#endyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: false,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',
                    onClose: function(dateText, inst) {
                        startyear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(startyear, 3, 1));
                    }
                });
            });

            $(function() {
                $('#startyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: false,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',
                    onClose: function(dateText, inst) {
                        endyear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(endyear, 4, 1));
                    }
                });
            });

        </script>


        <script type="text/javascript">

            function onprintout(){
                var region = document.forms[0].region.value;
                var startyear = document.forms[0].startyear.value;
                var endyear = document.forms[0].endyear.value;
                document.getElementById("paybillprintresult").innerHTML = "";
                document.getElementById("paybillprogressbar").style.display='';
                document.getElementById("printbut").disabled = true;
                BudgetAction.BudgetReportHOPrintOut(region, startyear, endyear, EmployeePayBillPrintStatus);
            }
            function EmployeePayBillPrintStatus(map){

                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.forms[0].action = "BudgetAction.htm?method=PopupReport";
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
            .ui-datepicker-month{
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
                                    <td width="100%" align="center" class="headerdata">Budget Report HO</td>
                                </tr>
                            </table>
                            <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="49%" align="center" valign="top">
                                        <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td colspan="4" class="mainheader">Budget Report HO</td>
                                            </tr>
                                            <tr class="lightrow">
                                                <td width="30%" class="textalign">Region</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="65%" colspan="2" class="textfieldalign" >
                                                    <%=regionlist%>
                                                </td>
                                            </tr>
                                            <tr class="darkrow">
                                                <td width="30%" class="textalign">A/c Start Month & Year</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="65%" colspan="2" class="textfieldalign" >
                                                    <input type="text" class="textbox" name="startyear" id="startyear" readonly>                                                    
                                                </td>
                                            </tr>
                                            <tr class="lightrow">
                                                <td width="30%" class="textalign">A/c End Month & Year</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="65%" colspan="2" class="textfieldalign" >
                                                    <input type="text" class="textbox" name="endyear" id="endyear" readonly>                                                    
                                                </td>
                                            </tr>                                           
                                            <tr class="darkrow">
                                                <td width="30%" class="textalign"></td>
                                                <td width="5%" class="mandatory"></td>
                                                <td width="65%" colspan="2" class="textfieldalign" >
                                                    <input type="button" class="submitbu" id="printbut" value="Print" onclick="onprintout();">
                                                    <input type="reset" class="submitbu" value="Reset">
                                                </td>
                                            </tr> 
                                            <tr class="lightrow">
                                                <td colspan="3" align="center">
                                                    <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                                                    <div id="paybillprintresult"></div>
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
<!--    <script type="text/javascript">
        onloadData();
        function onloadData(){
            FileUploadAction.loadRegionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
        }
    </script>-->
</html>

