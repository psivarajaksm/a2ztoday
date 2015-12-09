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
        <title>Cheque Register</title>
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
        <script src="<%=staticPath%>scripts/dateValidations.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <script src="dwr/interface/CashierEntryAction.js"></script>     
        <script src="dwr/interface/AccountsVoucherAction.js"></script>     
        <script src="dwr/interface/AccountsReportsAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
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
                
                $('#chequedate').datepicker({
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
                            var periodsele = document.getElementById('periodsele').value;  
                            var voucherdatefrom = document.getElementById('voucherdatefrom').value;  
                            var voucherdateto = document.getElementById('voucherdateto').value;  
                            var book = document.getElementById('book').value;  
                            CashierEntryAction.getChequeRegister( periodsele, voucherdatefrom, voucherdateto,book, fillVoucherDetailsForGivenDate);
                            $(this).dialog("close");                            
                        },
                        
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#changechequedetails').dialog({
                    autoOpen: false,
                    width: 350,
                    height: 170,
                    modal: true,                   
                    buttons: {
                        "Ok": function() {  
                            var recpayid= document.getElementById('receiptpaymentid').value; 
                            var chequedate= document.getElementById('chequedate').value;
                            var voucherdate= document.getElementById('voucherdate').value;
                            var chequeno= document.getElementById('chequeno').value;
                            var periodsele = document.getElementById('periodsele').value;  
                            var voucherdatefrom = document.getElementById('voucherdatefrom').value;  
                            var voucherdateto = document.getElementById('voucherdateto').value;  
                            var book = document.getElementById('book').value;
                            var todaydate=document.getElementById('todaydate').value;
                            var accbank=document.getElementById('accbank').value;

                            if(chequeno==""){
                                alert("Please Enter ther Cheque Number");
                                document.getElementById('chequeno').focus();
                                return false;
                            }else if(chequedate==""){
                                alert("Please Enter ther Cheque Date");
                                document.getElementById('chequedate').focus();
                                return false;
                            }else if(isFutureDateinGrid(voucherdate,chequedate)){
                                alert("Cheque date should be greater than or equals voucher date");
                                return false;
                                //                            }else if(days_between(todaydate,chequedate)>85){
                                //                                alert("Entered Cheque date should be less than the 85 Days from the working date");
                                //                                return false;
                            }else if(accbank=="0"){
                                alert("Please Select ther Bank");
                                document.getElementById('accbank').focus();
                                return false;
                            }else{
                                if(days_between(todaydate,chequedate)>85){
                                    alert("Entered Cheque date should be less than the 85 Days from the working date");
                                }
                                CashierEntryAction.changeChequeDetails(recpayid, chequedate, periodsele, voucherdatefrom, voucherdateto,book,chequeno,accbank, fillVoucherDetailsForGivenDate);
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
                    <tr class="darkrow">
                        <td width="4%" class="textalign">From</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15"class="textfieldalign"><input type="text" id="voucherdatefromdisp" name="voucherdatefromdisp"  size="20" readonly /></td>

                        <td width="4%" class="textalign">To</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15"class="textfieldalign"><input type="text" id="voucherdatetodisp" name="voucherdatetodisp"  size="20" readonly /></td>

                        <td width="%" class="textalign">Accounting Period</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15%"class="textfieldalign" ><input type="text" class="textbox" name="perioddisp" id="perioddisp" ></td>                        
                    </tr>
                </table>
                <div id="voucherdetails" style="display:none;height:410px;overflow:auto;"></div>                   

                <div id="accbookdateform" title="Select the period" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="darkrow">
                            <td colspan="3" width="48%" class="textalign">Cash Book</td>                            
                            <td width="50%" class="textfieldalign" >
                                <select class="combobox" name="book" id="book"></select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Period From</td>                            
                            <td width="2%" class="textalign"><input type="text" class="textbox"  id="voucherdatefrom" name="voucherdatefrom"  size="20" /></td>
                            <td width="2%" class="textalign">To</td>
                            <td width="50"class="textfieldalign"><input type="text" class="textbox"  id="voucherdateto" name="voucherdateto"  size="20" /></td>
                        </tr>    
                    </table>
                </div>
                <div id="changechequedetails" title="Change Cheque Details" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">                        
                        <tr class="darkrow">
                            <td width="48%" class="textalign">Cheque No</td>
                            <td width="2%" class="textalign"><input type="text" class="amounttextbox" id="chequeno" name="chequeno" maxlength="6" onkeypress="isNumeric(this);"  size="20" /></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Cheque Date</td>
                            <td width="2%" class="textalign"><input type="text" class="textbox" id="chequedate" name="chequedate"  size="20" /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="48%" class="textalign">Bank</td>
                            <td width="2%" class="textalign"><select class="combobox" name="accbank" id="accbank" class="textfieldalign"></select></td>
                        </tr>

                    </table>

                </div>

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">   
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="voucherdate" id="voucherdate"  value="" >
            <input type="hidden" name="receiptpaymentid" id="receiptpaymentid"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">              
        bodyonload();
        function bodyonload(){
            CashierEntryAction.loadBanks(fillBanksCombo);
        }  
        function fillBanksCombo(map){
            dwr.util.removeAllOptions("accbank");
            dwr.util.addOptions("accbank",map.banklist);
            AccountsVoucherAction.getTrailBalanceEmptyTable(fillLedgerDetails);
        }
        function fillLedgerDetails(map){      
                        
            dwr.util.removeAllOptions("book");
            dwr.util.addOptions("book", map.booklist);
            
            document.getElementById('periodsele').value=map.periodcode;  
            document.getElementById('perioddisp').value=map.period;   
           
            document.getElementById('voucherdetails').style.display="block";
            document.getElementById("voucherdetails").innerHTML=map.voucherdetails;             
            oTable = $('#vouchertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"275px",
                "sScrollX" : "100%",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
            });             

        }     
        function showRealiseForm(recpayid,chequedate,chequeno,voucherdate,bankcode){
            document.getElementById('receiptpaymentid').value=recpayid; 
            document.getElementById('chequedate').value=chequedate;
            document.getElementById('chequeno').value=chequeno;
            document.getElementById('voucherdate').value=voucherdate;            
            document.getElementById('accbank').value=bankcode;
            
            $('#changechequedetails').dialog("open");
        }
        
        function fillVoucherDetailsForGivenDate(map){

            document.getElementById('voucherdetails').style.display="block";
            document.getElementById("voucherdetails").innerHTML=map.voucherdetails;               
            //            document.getElementById('bookdisp').value=map.bookdisp;
            document.getElementById('voucherdatefromdisp').value=document.getElementById('voucherdatefrom').value;            
            document.getElementById('voucherdatetodisp').value=document.getElementById('voucherdateto').value;            
            
            
            oTable = $('#vouchertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"275px",
                "sScrollX" : "100%",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
            }); 
        }   
    </script>
    <script type="text/javascript">
        function onprintout(){
            var periodsele = document.getElementById('periodsele').value;
            var voucherdatefrom = document.getElementById('voucherdatefrom').value;
            var voucherdateto = document.getElementById('voucherdateto').value;
            var book = document.getElementById('book').value;
            //            alert("perid = "+periodsele+"\nfrom date = "+voucherdatefrom+"\nto date = "+voucherdateto+"\nbook = "+book);
            document.getElementById("paybillprintresult").innerHTML = "";
            document.getElementById("paybillprogressbar").style.display='';
            document.getElementById("printbut").disabled = true;
            AccountsReportsAction.ChequeRegisterPrintOut(periodsele, voucherdatefrom, voucherdateto, book, EmployeePayBillPrintStatus);
        }

        function EmployeePayBillPrintStatus(map){

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
</html>

