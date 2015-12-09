<%-- 
    Document   : BRSReportPage
    Created on : 25 Mar, 2014, 1:31:47 PM
    Author     : Karthikeyan S
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

    String booknames = "";

    if (request.getSession().getAttribute("bookname") != null) {
        booknames = (String) request.getSession().getAttribute("bookname");
    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Region Wise BRS Report</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>        
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/BRSAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var smonth;
            var syear;
            $(function() {
                $('#recondate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    onClose: function(dateText, inst) {
                        smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear,smonth, 1));
                    }
                });
            });
            function ShowBRSDetails(){
                var accountbook = document.getElementById("accountbook").value;
                var accountingperiod = document.getElementById("accountingperiod").value;
                var region = document.getElementById("region").value;
                var statustype = document.getElementById("statustype").value;
                if(accountbook==0){
                    alert("Please Select the Account Book");
                    return false;
                }else if(smonth==null || syear==null){
                    alert("Please Select Month & Year");
                    return false;
                }else if(accountingperiod==0){
                    alert("Please Select Accounting Period");
                    return false;
                }else if(region==0){
                    alert("Please Select Region Name");
                    return false;
                }else if(statustype==0){
                    alert("Please Select Status Type");
                    return false;
                }else{
                    getBlanket('continueDIV');
                    BRSAction.getRegionWiseBrsReportDetails(smonth+1,syear,accountbook,accountingperiod,region,statustype, displayBrsDetails );
                }
            }
            
            function displayBrsDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById('brsdetails').style.display="block";
                    document.getElementById("brsdetails").innerHTML=map.brsdetails;
                    
                    document.getElementById("gmonth").value = map.month;
                    document.getElementById("gyear").value = map.year;
                    document.getElementById("gaccountbook").value = map.accountbook;
                    document.getElementById("gaccountingperiod").value = map.accountingperiod;
                    document.getElementById("gregion").value = map.region;
                    document.getElementById("gstatus").value = map.status;
                    
                    oTable = $('#brstable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"200px",
                        "bSort": true,
                        "bFilter": true,
                        "bPaginate": false
                    });
                }
            }           
        
        
            function exportBRSdetailsRegionWise(){
                var gmonth = document.getElementById("gmonth").value;
                var gyear = document.getElementById("gyear").value;
                var accountbook = document.getElementById("gaccountbook").value;
                var accountingperiod = document.getElementById("gaccountingperiod").value;
                var region = document.getElementById("gregion").value;
                var statustype = document.getElementById("gstatus").value;
                BRSAction.BRSRegionWiseReport(gmonth, gyear, accountbook,accountingperiod, region,statustype, sam);
            }
            
            function sam(map){
                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.forms[0].action = "AccountsReportsAction.do?method=PopupXMLReport";
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
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Region Wise BRS Report</td>
                    </tr>
                </table>
                <table width="70%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Region Wise BRS Report</td>
                    </tr>   
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Account Book</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="accountbook" id="accountbook"></select>                            
                        </td>
                        <td width="20%" class="textalign">Month & Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text"  class="textbox"  id="recondate" name="recondate"  size="20" />
                        </td>
                    </tr>                    
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Accounting Period</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="accountingperiod" id="accountingperiod"></select>
                        </td>
                        <td width="20%" class="textalign">Region Name </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="region" id="region"></select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Status</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="statustype" id="statustype">
                                <option value="0">--Select--</option>
                                <option value="1">Approval</option>
                                <option value="2">Pending</option>
                            </select>                            
                        </td>
                        <td colspan="3" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Show" onclick="ShowBRSDetails();">
                            <input type="reset" class="submitbu" value="Reset">
                        </td>
                    </tr>                  
                </table>
                <br>
            </div>
            <div id="brsdetails" style="display:none;overflow:auto;"></div>
            <div>
                <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                <div id="paybillprintresult"></div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
            <input type="hidden" name="gmonth" id="gmonth">
            <input type="hidden" name="gyear" id="gyear">
            <input type="hidden" name="gaccountbook" id="gaccountbook">
            <input type="hidden" name="gaccountingperiod" id="gaccountingperiod">
            <input type="hidden" name="gregion" id="gregion">
            <input type="hidden" name="gstatus" id="gstatus">
        </form>
    </body>    

    <script type="text/javascript">
        bodyonload();
        function bodyonload(){   
            var message = '<%=message%>';
            if(message!=null && message!=""){

                alert(message);

            }
            BRSAction.getAccountBook(loadaccountbookdialogbox); 
        }
        function loadaccountbookdialogbox(map){
            dwr.util.removeAllOptions("accountbook");
            dwr.util.addOptions("accountbook", map.accountbookList);
            dwr.util.removeAllOptions("accountingperiod");
            dwr.util.addOptions("accountingperiod", map.accountYearList);
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region", map.regionList);
        }

    </script>
</html>


