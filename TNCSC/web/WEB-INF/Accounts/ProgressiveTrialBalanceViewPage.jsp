<%--
    Document   : ProgressiveTrialBalanceViewPage
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
        <title>Progressive Trail Balance</title>
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
        <script src="dwr/interface/AccountsVoucherAction.js"></script>       
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

                
                $('#accbookdateform').dialog({  
                    autoOpen: true,
                    width: 600,                    
                    modal: true,                    
                    buttons: {
                        "Ok": function() {                                   
                            //                            var booksele= document.getElementById('book').value;
                            var periodsele = document.getElementById('periodsele').value;  
                            //                            var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                            var voucherdateto = document.getElementById('voucherdateto').value;                              
                            AccountsVoucherAction.getProgressiveTrailBalanceDetails( periodsele, voucherdateto, fillVoucherDetailsForGivenDate);
                            //                            AccountsVoucherAction.getProgressiveTrailBalanceDetails( periodsele, voucherdatefrom, voucherdateto, fillVoucherDetailsForGivenDate);
                            
                            $(this).dialog("close");
                            
                        },
                        
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
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
                
                //                $('#tbprint').click(function(){
                //                    var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                //                    var voucherdateto = document.getElementById('voucherdateto').value;
                //                    window.open("AccountsReportsAction.htm?method=trialBalancePrint&fromDate="+voucherdatefrom+"&toDate="+voucherdateto,  "Trial Balance Report", "width=1000,height=800");
                //                    return false;
                //                });
             
                
            });
            function progressivetrialPrint(){
                var voucherdatefrom = document.getElementById('voucherdatefromdisp').value;
                var voucherdateto = document.getElementById('voucherdatetodisp').value;
                var url="AccountsReportsAction.htm?method=progressiveTrialBalancePrint&fromDate="+voucherdatefrom+"&toDate="+voucherdateto;
                window.open(url, "Trial Balance Report", "width=1000,height=800");
                return false;
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
                        <td align="center" colspan="15"  class="headerdata">Progressive Trail Balance</td>
                    </tr>
                    <tr class="darkrow">
                        <!--                        <td width="5%" class="textalign">Cash Book</td>
                                                <td width="2%" class="mandatory">*</td>
                                                <td width="15%" class="textfieldalign" ><input type="text" id="bookdisp" name="bookdisp"  size="20" readonly /></td>-->


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

                <div id="accbookdateform" title="Voucher Date & Type Selection Form" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <!--                        <tr class="darkrow">
                                                    <td width="48%" class="textalign">Cash Book</td>
                                                    <td width="2%" class="mandatory">*</td>
                                                    <td colspan="2" width="50%" class="textfieldalign" >
                                                        <select class="combobox" name="book" id="book"></select>
                                                    </td>
                                                </tr>                       -->
                        <tr class="darkrow">
                            <!--                            <td width="48%" class="textalign">Period From</td>
                                                        <td width="2%" class="textalign"><input type="text" id="voucherdatefrom" name="voucherdatefrom"  size="20" /></td>-->
                            <td width="48%" class="textalign">Period To</td>
                            <td width="2%" class="mandatory">*</td>
                            <td width="50"class="textfieldalign"><input type="text" id="voucherdateto" name="voucherdateto"  size="20" /></td>
                        </tr>    
                    </table>
                </div>                

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">   
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
        </form>
    </body>
    <script type="text/javascript">              
        bodyonload();
        function bodyonload(){          
            AccountsVoucherAction.getTrailBalanceEmptyTable(fillLedgerDetails);               
        }  
        
        function fillLedgerDetails(map){      
            
            //            dwr.util.removeAllOptions("book");
            //            dwr.util.addOptions("book", map.booklist);
           
            
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
        
        function fillVoucherDetailsForGivenDate(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                return false;
            }else{
                document.getElementById('voucherdetails').style.display="block";
                document.getElementById("voucherdetails").innerHTML=map.voucherdetails;
                //            document.getElementById('bookdisp').value=map.bookdisp;
                document.getElementById('voucherdatefromdisp').value=map.voucherdatefrom;
//                document.getElementById('voucherdatefromdisp').value=document.getElementById('voucherdatefrom').value;
                document.getElementById('voucherdatetodisp').value=document.getElementById('voucherdateto').value;


                oTable = $('#vouchertable').dataTable({
                    "bJQueryUI": true,
                    "sScrollY":"275px",
                    "bSort": true,
                    "bFilter": true,
                    "bPaginate": false                    
                });

            }
            
        }
        

    </script>
</html>

