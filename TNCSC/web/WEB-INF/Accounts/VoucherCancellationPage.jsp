<%-- 
    Document   : VoucherCancellationPage
    Created on : Apr 26, 2013, 3:27:55 PM
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
        <title>Voucher Cancellation</title>
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
        <script src="dwr/interface/CashierEntryAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $('#accountdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();

            });
            function saveAccountBook(){
                var acctype=document.getElementById('acctype').value;
                var accountdate=document.getElementById('accountdate').value;
                var currentbook=document.getElementById('currentbook').value;

                var chkarray="";
//                var chkboxes=document.forms[0].vouchercancelName;
                var chkboxes=document.getElementsByName('vouchercancelName');
                for (i=0;i<chkboxes.length;i++){
                    if (chkboxes[i].checked==true){
                        chkarray=chkarray+chkboxes[i].value+",";
                    }

                }

                if(acctype=="0"){
                    alert("Please Select the Type of Account Entries");
                    document.getElementById('acctype').focus();
                    return false;
                }else if(accountdate==""){
                    alert("Please Select the Account Date");
                    document.getElementById('accountdate').focus();
                    return false;
                }else if(currentbook=="0"){
                    alert("Please Select the Current Account Book");
                    document.getElementById('currentbook').focus();
                    return false;
                }
                else if(chkarray==""){
                    alert("Please Select the alteast one Voucher?");
                    return false;
                }
                else{
                    getBlanket('continueDIV');
                    CashierEntryAction.saveVoucherCancellation(currentbook,chkarray,accountdate,acctype,clearVoucherCancelPage);

                }

            }

            function clearVoucherCancelPage(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.getElementById('acctype').value="0";
                    document.getElementById('accountdate').value="";
                    document.getElementById('currentbook').value="0";
//                    document.getElementById('accbook').value="0";
                    document.getElementById('voucherids').value="";
                    document.getElementById("voucherDetails").style.display="none";
                }
            }
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

                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td width="100%" colspan="7" align="center" class="headerdata">Voucher Cancellation</td>
                    </tr>
                    <tr>
                        <td colspan="7" class="mainheader">Voucher Cancellation</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="29%" class="textalign">Type of Account Entries</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" class="textfieldalign" >
                            <select class="combobox" name="acctype" id="acctype">
                                <option value="0">Select</option>
                                <option value="P">Payment</option>
                                <option value="R">Receipt</option>
                                <option value="J">Journal</option>
                                <option value="P">Bank</option>
                            </select>
                        </td>
                        <td width="14%" class="textalign">A/C Entry Date</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="10%" class="textfieldalign" >
                            <input type="text" class="textbox" name="accountdate" id="accountdate" readonly>
                        </td>
                        <td width="25%" class="mandatory">&nbsp;&nbsp;</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="29%" class="textalign">Current A/C Book</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="10%" class="textfieldalign" >
                            <select class="combobox" name="currentbook" id="currentbook" class="textfieldalign" onchange="loadEntries(this);" ></select>

                        </td>
                        <!--                        <td width="29%" class="textalign">Change to A/C Book</td>
                                                <td width="1%" class="mandatory">*</td>
                                                <td width="10%" class="textfieldalign" >
                                                    <select class="combobox" name="accbook" id="accbook" class="textfieldalign"></select>
                                                </td>-->
                        <td width="25%" colspan="4" class="mandatory">&nbsp;&nbsp;</td>
                    </tr>
                </table>
            </div>
            <div id="voucherDetails" style="height:300px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="voucherids" id="voucherids" value="">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            CashierEntryAction.getAccountBook(fillCashBookCombo)
        }
        function fillCashBookCombo(map){
            dwr.util.removeAllOptions("currentbook");
            dwr.util.addOptions("currentbook",map.accountbooklist);
            //            dwr.util.removeAllOptions("accbook");
            //            dwr.util.addOptions("accbook",map.accountbooklist);
        }
        function loadEntries(obj){
            var acctype=document.getElementById('acctype').value;
            var accountdate=document.getElementById('accountdate').value;
            var accbook=obj.value;
            if(acctype=="0"){
                alert("Please Select the Type of Account Entries");
                document.getElementById("voucherDetails").style.display="none";
                document.getElementById('acctype').foucs();
                return false;
            }else if(accountdate==""){
                alert("Please Select the Account Date");
                document.getElementById("voucherDetails").style.display="none";
                document.getElementById('accountdate').foucs();
                return false;
            }else if(accbook=="0"){
                alert("Please Select the Current A/C Book");
                document.getElementById("voucherDetails").style.display="none";
                document.getElementById('currentbook').value="0";
                document.getElementById('currentbook').foucs();
                return false;
            }else{
                CashierEntryAction.getVoucherDetailsForCancel(acctype,accountdate,accbook,fillVoucherAccountsDetails)
            }

        }

        function fillVoucherAccountsDetails(map){
            document.getElementById("voucherDetails").style.display="block";
            document.getElementById("voucherDetails").innerHTML=map.voucheraccountdetails;
            oTable = $('#voucheraccounttable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"160px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers"
            });
        }
        


    </script>
</html>
