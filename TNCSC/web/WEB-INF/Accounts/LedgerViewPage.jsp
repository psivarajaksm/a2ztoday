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
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="<%=staticPath%>scripts/jquery.handsontable.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script src="dwr/interface/AccountsReportsAction.js"></script>
        <script src="dwr/interface/AccountsVoucherAction.js"></script>
        <script type="text/javascript">
            $(function() {
                $('#voucherdatefrom').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();

                $('#voucherdateto').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();
               
               
                $('#accbookdateform').dialog({
                    autoOpen: true,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            //                            var booksele= document.getElementById('book').value;
                            var ledgersele = document.getElementById('ledger').value;
                            var periodsele = document.getElementById('periodsele').value;
                            var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                            var voucherdateto = document.getElementById('voucherdateto').value;
                            var reporttype = document.getElementById('reporttype').value;                            
                            AccountsVoucherAction.getLedgerDetailsMap( periodsele, ledgersele, voucherdatefrom, voucherdateto, reporttype, fillVoucherDetailsForGivenDate);
                            //                            AccountsVoucherAction.getLedgerDetails(booksele, periodsele, ledgersele, voucherdatefrom, voucherdateto, fillVoucherDetailsForGivenDate);
                            $(this).dialog("close");

                        },

                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                $("#showbutton").click(function(){

                    var ledgersele = document.getElementById('ledger').value;
                    var periodsele = document.getElementById('periodsele').value;
                    var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                    var voucherdateto = document.getElementById('voucherdateto').value;
                    var reporttype = document.getElementById('reporttype').value;
                    if(ledgersele=="0"){
                        alert("Please Select the Ledger Name")
                        document.getElementById('ledger').focus();
                        return false;
                    }else if(voucherdatefrom==""){
                        alert("Please Select the From Date")
                        document.getElementById('voucherdatefrom').focus();
                        return false;
                    }else if(voucherdateto==""){
                        alert("Please Select the To Date")
                        document.getElementById('voucherdateto').focus();
                        return false;
                    }else{
                        getBlanket('continueDIV');
                        AccountsVoucherAction.getLedgerDetailsMap( periodsele, ledgersele, voucherdatefrom, voucherdateto, reporttype, fillVoucherDetailsForGivenDate);                        
                    }

                });

                $('#voucherentryform').dialog({
                    autoOpen: false,
                    width: 1000,
                    height: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {

                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });


            });

        </script>
        <script type="text/javascript">
            function financialyearcheck(){
                var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                if(voucherdatefrom!=""){
                    var db=voucherdatefrom;
                    var strArray=db.split("/")
                    var arrDay=strArray[0]
                    var arrMonth=strArray[1]
                    if((arrDay!="01")||(arrMonth!="04")){
                        alert("Please Select the Correct Financial Year");
                        document.getElementById('voucherdatefrom').value="";
                    }
                }
            }
            function onprintout(){
                var ledgersele = document.getElementById('ledger').value;
                var periodsele = document.getElementById('periodsele').value;
                var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                var voucherdateto = document.getElementById('voucherdateto').value;
                var reporttype = document.getElementById('reporttype').value;
                //                alert("ledgersele = "+ledgersele+"\nperiodsele="+periodsele+"\nvoucherdatefrom="+voucherdatefrom+"\nvoucherdateto="+voucherdateto);
                document.getElementById("paybillprintresult").innerHTML = "";
                document.getElementById("paybillprogressbar").style.display='';
                document.getElementById("printbut").disabled = true;
                AccountsReportsAction.LedgerPrintOut(ledgersele, periodsele,voucherdatefrom,voucherdateto,reporttype, EmployeePayBillPrintStatus);
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
        <style type="text/css">

            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}

        </style>
    </head>
    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" colspan="13" align="center" class="headerdata">Ledger</td>
                    </tr>
<!--                    <tr>
                        <td align="center" colspan="15" class="mainheader">Ledger</td>
                    </tr>-->
                    <tr class="darkrow">
                        <!--                        <td width="5%" class="textalign">Cash Book</td>
                                                <td width="2%" class="mandatory">*</td>
                                                <td width="15%" class="textfieldalign" ><input type="text" id="bookdisp" name="bookdisp"  size="20" readonly /></td>-->

                        <td width="5%" class="textalign">Ledger</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="15%" class="textfieldalign" >
                            <select class="combobox" name="ledger" id="ledger"></select>
                            <!--                            <input type="text" id="ledgerdisp" name="ledgerdisp"  size="20" readonly />-->
                        </td>

                        <td width="4%" class="textalign">From</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15" class="textfieldalign"><input type="text" class="textbox" id="voucherdatefrom" name="voucherdatefrom"  size="20" readonly /></td> <!--remove onchange="financialyearcheck();" -->

                        <td width="4%" class="textalign">To</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15" class="textfieldalign"><input type="text" class="textbox" id="voucherdateto" name="voucherdateto"  size="20" readonly /></td>
                        
                        <td width="4%" class="textalign">Report Type</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15" class="textfieldalign"><select class="combobox"  id="reporttype" name="reporttype">
                                <option value="1" >Ledger wise</option>
                                <option value="2" >Ledger Group wise</option>
                            </select></td>

                        <td width="%" class="textalign"><input type="button" CLASS="submitbu" name="showbutton" id="showbutton" value="Show"></td>
                        <td width="%" class="textalign">Accounting Period</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15%" class="textfieldalign" ><input type="text" class="textbox" name="perioddisp" id="perioddisp" ></td>
                    </tr>
                </table>
                <div id="voucherdetails" style="display:none;overflow:auto;"></div>

<!--                <div id="accbookdateform" title="Voucher Date & Type Selection Form" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                                <tr class="darkrow">
                                                    <td width="48%" class="textalign">Cash Book</td>
                                                    <td width="2%" class="mandatory">*</td>
                                                    <td colspan="2" width="50%" class="textfieldalign" >
                                                        <select class="combobox" name="book" id="book"></select>
                                                    </td>
                                                </tr>
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Ledger</td>
                            <td width="2%" class="mandatory">*</td>
                            <td colspan="2" width="50%" class="textfieldalign" >
                                <select class="combobox" name="ledger" id="ledger"></select>
                            </td>
                        </tr>
                        <tr class="darkrow">
                            <td width="48%" class="textalign">Period From</td>
                            <td width="2%" class="textalign"><input type="text" id="voucherdatefrom" name="voucherdatefrom"  size="20" /></td>
                            <td width="2%" class="textalign">To</td>
                            <td width="50"class="textfieldalign"><input type="text" id="voucherdateto" name="voucherdateto"  size="20" /></td>
                        </tr>
                    </table>

                </div>               -->

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            AccountsVoucherAction.getLedgerEmptyTable(fillLedgerDetails);
        }

        function fillLedgerDetails(map){

            dwr.util.removeAllOptions("ledger");
            dwr.util.addOptions("ledger", map.ledgerlist);

            document.getElementById('periodsele').value=map.periodcode;
            document.getElementById('perioddisp').value=map.period;
        }

        function fillVoucherDetailsForGivenDate(map){
            getBlanket('continueDIV');
            document.getElementById('voucherdetails').style.display="block";
            document.getElementById("voucherdetails").innerHTML=map.voucherdetails;
            oTable = $('#vouchertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"145px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
            });
        }

    </script>
</html>

