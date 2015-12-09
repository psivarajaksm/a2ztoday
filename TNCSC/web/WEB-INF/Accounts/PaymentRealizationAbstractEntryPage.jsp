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
        <script type="text/javascript" src="<%=staticPath%>scripts/dateValidations.js"></script>
        <script src="<%=staticPath%>scripts/jquery.handsontable.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
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
                
                $('#realizationdate').datepicker({ 
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();
                
                $('#realizationfromdate').datepicker({ 
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();
                
                $('#realizationtodate').datepicker({ 
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
                            var book = document.getElementById('book').value;  
                            CashierEntryAction.getPaymentAbstractDetails( periodsele, book, fillVoucherDetailsForGivenDate);                            
                            $(this).dialog("close");                            
                        },
                        
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#voucherentryform').dialog({  
                    autoOpen: false,
                    width: 300,
                    height: 100,
                    modal: true,                   
                    buttons: {
                        "Ok": function() {  
                            var recpayid= document.getElementById('receiptpaymentid').value; 
                            var realizationdate= document.getElementById('realizationdate').value; 
                            var periodsele = document.getElementById('periodsele').value;  
                            var book = document.getElementById('book').value;
                            var dbtype = document.getElementById('dbtype').value;
                            //                            alert("recpayid = "+recpayid+"\nrealizationdate = "+realizationdate+"\nperiodsele = "+periodsele+"\nbook = "+book);                            
                            CashierEntryAction.savePaymentRealizationAbstractDate(recpayid, realizationdate, periodsele, book, dbtype, fillVoucherDetailsForGivenDate);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#printentryform').dialog({  
                    autoOpen: false,
                    width: 350,
                    height: 170,
                    modal: true,                   
                    buttons: {
                        "Ok": function() {                            
                            document.forms[0].periodsele.value = document.getElementById('periodsele').value;  
                            document.forms[0].acbook.value = document.getElementById('book').value;
                            
                            var fdate = document.getElementById('realizationfromdate').value;
                            var tdate = document.getElementById('realizationtodate').value;
                            
                            if(fdate==null || fdate.length==0){
                                alert("Please Select the From Date!");
                                return false;
                            }else if(tdate==null || tdate.length==0){
                                alert("Please Select the To Date!");
                                return false;
                            }else{
                                if(pastDataCheckingwithslace(fdate, tdate)){
                                    document.getElementById('realfromdate').value = fdate;
                                    document.getElementById('realtodate').value = tdate;
                            
                                    if(document.getElementById("isrealized").checked){
                                        document.getElementById('realized').value = "true";
                                        //                                document.getElementById('realfromdate').value = document.getElementById('realizationfromdate').value;
                                        //                                document.getElementById('realtodate').value = document.getElementById('realizationtodate').value;
                                    }else{
                                        document.getElementById('realized').value = "false";
                                        //                                document.getElementById('realfromdate').value = "";
                                        //                                document.getElementById('realtodate').value = "";
                                    }                            
                                    document.forms[0].action = "AccountsReportsAction.do?method=PaymentRealizationAbstractPrintOut";
                                    document.forms[0].submit();
                                    document.getElementById('realizationfromdate').value = "";
                                    document.getElementById('realizationtodate').value = "";
                                    $(this).dialog("close"); 
                                }else{
                                    alert("To Date is Equal or Greater than the From Date");
                                    return false;
                                }
                                
                            }                           
                            
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });            
                
            });
           
        </script>
        <script type="text/javascript">
            function onprintout(){
                $('#printentryform').dialog("open");
            }

            function EmployeePayBillPrintStatus(map){
                if(map.ERROR!=null){
                    alert(map.ERROR);
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
                    </table>
                </div>
                <div id="voucherentryform" title="Realization Date" >  
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">                        
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Date</td>                            
                            <td width="2%" class="textalign"><input type="text" id="realizationdate" name="realizationdate"  size="20" /></td>
                        </tr>    
                    </table>
                </div>
                <div id="printentryform" title="Print Details" >  
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">                     
                        <tr class="darkrow">
                            <td width="50%" class="textalign">Is Realized</td>                            
                            <td width="50%" align="left">
                                <input type="checkbox" name="isrealized" id="isrealized">
                            </td>
                        </tr>    
                        <tr class="lightrow">
                            <td width="50%" class="textalign">Realized From Date</td>                            
                            <td width="50%" class="textalign"><input type="text" id="realizationfromdate" name="realizationfromdate"  size="20" /></td>
                        </tr>    
                        <tr class="darkrow">
                            <td width="50%" class="textalign">Realized To Date</td>                            
                            <td width="50%" class="textalign"><input type="text" id="realizationtodate" name="realizationtodate"  size="20" /></td>
                        </tr>    
                    </table>
                </div>
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">   
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
            <input type="hidden" name="receiptpaymentid" id="receiptpaymentid"  value="0" >
            <input type="hidden" name="acbook" id="acbook">
            <input type="hidden" name="realfromdate" id="realfromdate">
            <input type="hidden" name="realtodate" id="realtodate">
            <input type="hidden" name="realized" id="realized">
            <input type="hidden" name="dbtype" id="dbtype">
        </form>
    </body>
    <script type="text/javascript">              
        bodyonload();
        function bodyonload(){          
            AccountsVoucherAction.getTrailBalanceEmptyTable(fillLedgerDetails);               
        }
        
        function trim(str) {
            return str.replace(/^\s+|\s+$/g,"");
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
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
                //                "sPaginationType": "full_numbers",
                //                'iDisplayLength': 20,
                //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            }); 
            

        }     
        function showRealiseForm(recpayid){
            var arr = trim(recpayid).split('/');
            //            alert("arr[0] = "+arr[0]+"\narr[1]="+arr[1]);
            document.getElementById('receiptpaymentid').value=arr[0]; 
            document.getElementById('dbtype').value=arr[1]; 
            $('#voucherentryform').dialog("open");           
        }
        
        function fillVoucherDetailsForGivenDate(map){
            
            document.getElementById('voucherdetails').style.display="block";
            document.getElementById("voucherdetails").innerHTML=map.voucherdetails;               
            //            document.getElementById('bookdisp').value=map.bookdisp;
            //            document.getElementById('voucherdatefromdisp').value=document.getElementById('voucherdatefrom').value;            
            //            document.getElementById('voucherdatetodisp').value=document.getElementById('voucherdateto').value;            
            
            
            oTable = $('#vouchertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"275px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
                //                "sPaginationType": "full_numbers",
                //                'iDisplayLength': 20,
                //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            }); 
        }       
       

    </script>
</html>

