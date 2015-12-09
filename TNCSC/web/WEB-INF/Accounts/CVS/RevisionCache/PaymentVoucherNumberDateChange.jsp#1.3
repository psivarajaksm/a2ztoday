<%-- 
    Document   : PaymentVoucherNumberDateChange
    Created on : May 24, 2013, 4:52:55 PM
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

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Payment Voucher Modification</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="<%=staticPath%>scripts/dateValidations.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/CashierEntryAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $('#fromdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();

                $('#todate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();
                
                $('#voucherapproveddate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();

                $('#changevoucherdetails').dialog({
                    autoOpen: false,
                    width: 350,
                    height: 200,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var voucherid= document.getElementById('voucherid').value;
                            var voucherno= document.getElementById('voucherno').value;
                            var voucherapproveddate= document.getElementById('voucherapproveddate').value;
                            var voucherdate= document.getElementById('voucherdate').value;
                            var fromdate = document.getElementById('fromdate').value;
                            var todate = document.getElementById('todate').value;
                            var accbook = document.getElementById('accbook').value;                            
                            var todaydate=document.getElementById('todaydate').value;

                            if(voucherno==""){
                                alert("Please Enter ther Voucher Number");
                                document.getElementById('voucherno').focus();
                                return false;
                            }else if(voucherapproveddate==""){
                                alert("Please Enter ther Voucher Approved Date");
                                document.getElementById('voucherapproveddate').focus();
                                return false;
                            }else if(pastDataCheckingwithslace(voucherapproveddate,voucherdate)){
                                alert("Entered voucher approved date can not be less than the voucher prepared date");
                                return false;
                            }else{
                                getBlanket('continueDIV');
                                CashierEntryAction.changeVoucherDateandnoDetails(voucherid, voucherno,voucherapproveddate,  fromdate, todate,accbook, fillVoucherDetailsForGivenDate);
                                $(this).dialog("close");
                            }

                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });
        </script>
    </head>
    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Payment Voucher Modification</td>
                    </tr>
                </table>
                <table width="70%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Payment Voucher Modification</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">From Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="fromdate" id="fromdate" readonly>
                        </td>
                        <td width="20%" class="textalign">To Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" >
                            <input type="text" class="textbox" name="todate" id="todate" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">A/C Book Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="accbook" id="accbook" class="textfieldalign"></select>

                        </td>
                        <td colspan="3" align="center">
                            <input type="button" class="submitbu" id="showbutton" name="showbutton" value="Show" onclick="showVouchers();" >
                            <!--                            <input type="reset" class="submitbu" value="Reset">-->
                        </td>
                    </tr>                    
                </table >

            </div>
            <div id="voucherDetails" style="height:330px;overflow:auto;"> </div>
            <div id="changevoucherdetails" title="Change Voucher Details" >
                <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr class="darkrow">
                        <td width="48%" class="textalign">Voucher No</td>
                        <td width="2%" class="textalign"><input type="text" class="amounttextbox" id="voucherno" name="voucherno" onkeypress="isNumeric(this);"  size="20" /></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="48%" class="textalign">Voucher Date</td>
                        <td width="2%" class="textalign"><input type="text" class="textbox" name="voucherapproveddate" id="voucherapproveddate" readonly></td>
                    </tr>

                </table>

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="voucherid" id="voucherid" value="">
            <input type="hidden" name="voucherdate" id="voucherdate" value="">
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
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
        function showVouchers(){
            var fromdate = document.getElementById('fromdate').value;
            var todate = document.getElementById('todate').value;            
            var accbook = document.getElementById('accbook').value;

            if(fromdate==""){
                alert("Please select the From date");
                document.getElementById('fromdate').focus();
                return false;
            }else if(todate==""){
                alert("Please Select the to date");
                document.getElementById('todate').focus();
                return false;
            }else if(accbook==""){
                alert("Please Select the Account book type");
                document.getElementById('accbook').focus();
                return false;
            }else{
                getBlanket('continueDIV');
                CashierEntryAction.getVoucherDetailsForModify(fromdate,todate,accbook,fillVoucherAccountsDetails)
            }          

        }

        function fillVoucherAccountsDetails(map){
            getBlanket('continueDIV');
            document.getElementById("voucherDetails").style.display="block";
            document.getElementById("voucherDetails").innerHTML=map.voucheraccountdetails;
            oTable = $('#voucheraccounttable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"225px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
                //                "sPaginationType": "full_numbers"
            });
        }

        function showUpdateVoucherDetails(voucherid,voucherno,voucherapproveddate,voucherdate){
            document.getElementById('voucherid').value=voucherid;
            document.getElementById('voucherno').value=voucherno;
            document.getElementById('voucherapproveddate').value=voucherapproveddate;
            document.getElementById('voucherdate').value=voucherdate;
            $('#changevoucherdetails').dialog("open");
        }

        function fillVoucherDetailsForGivenDate(map){
            getBlanket('continueDIV');
            document.getElementById('voucherDetails').style.display="block";
            document.getElementById("voucherDetails").innerHTML=map.voucheraccountdetails;


            oTable = $('#voucheraccounttable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"225px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
            });
        }


    </script>
</html>

