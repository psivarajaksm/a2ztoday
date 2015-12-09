<%-- 
    Document   : RemittanceUpdateJsp
    Created on : Aug 6, 2013, 3:24:17 PM
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
        <title>Remittance Entry</title>
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
        <script src="dwr/interface/CashierEntryAction.js"></script>        
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
                $('#remittancedate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();
               
                $("#showbutton").click(function(){
                    var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                    var voucherdateto = document.getElementById('voucherdateto').value;
                    if(voucherdatefrom==""){
                        alert("Please Select the From Date")
                        document.getElementById('voucherdatefrom').focus();
                        return false;
                    }else if(voucherdateto==""){
                        alert("Please Select the To Date")
                        document.getElementById('voucherdateto').focus();
                        return false;
                    }else{
                        getBlanket('continueDIV');
                        CashierEntryAction.getRemittanceDetails(voucherdatefrom, voucherdateto, fillVoucherDetailsForGivenDate);
                    }

                });

                $('#voucherentryform').dialog({
                    autoOpen: false,
                    width: 300,
                    height: 150,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var challanid= document.getElementById('receiptchallanid').value;
                            var remittancedate= document.getElementById('remittancedate').value;
                            var voucherdatefrom = document.getElementById('voucherdatefrom').value;
                            var voucherdateto = document.getElementById('voucherdateto').value;

                            if(challanid=="" || challanid=="0"){
                                alert("Please Create a Bank Challan First for this Receipt");
                                $(this).dialog("close");
                                return false;
                            }else if(remittancedate==""){
                                alert("Please Select the Remittance Date");
                                $(this).dialog("close");
                                return false;
                            }else  if(voucherdatefrom==""){
                                alert("Please Select the From Date")                                
                                $(this).dialog("close");
                                return false;
                            }else if(voucherdateto==""){
                                alert("Please Select the To Date")                                
                                $(this).dialog("close");
                                return false;
                            }else {
                                getBlanket('continueDIV');
                                CashierEntryAction.saveRemittanceDate(challanid, remittancedate,voucherdatefrom,voucherdateto, fillVoucherDetailsForGivenDate);                                
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
                    <tr>
                        <td width="100%" align="center" class="headerdata">Remittance Entry</td>
                    </tr>
                </table>
                <table width="60%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                    <tr class="darkrow">
                        <td width="15%" class="textalign">Voucher From Date </td>
                        <td width="2%" class="mandatory"></td>
                        <td width="10%"class="textfieldalign"><input type="text" class="textbox" id="voucherdatefrom" name="voucherdatefrom"  size="20" readonly /></td>

                        <td width="15%" class="textalign">Voucher To Date</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="10%"class="textfieldalign"><input type="text" class="textbox" id="voucherdateto" name="voucherdateto"  size="20" readonly /></td>

                        <td width="6%" class="textalign"><input type="button" CLASS="submitbu" name="showbutton" id="showbutton" value="Show"></td>
                    </tr>
                </table>
                <div id="voucherdetails" style="display:none;overflow:auto;"></div>

                <div id="voucherentryform" title="Remittance Date" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Date</td>
                            <td width="2%" class="textalign"><input type="text" class="textbox" id="remittancedate" name="remittancedate"  size="20" /></td>
                        </tr>
                    </table>

                </div>
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="receiptchallanid" id="receiptchallanid"  value="0" >
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">
        function fillVoucherDetailsForGivenDate(map){

            if(map.ERROR!=null && map.ERROR!=""){
                getBlanket('continueDIV');
                alert(map.ERROR);
            }else{
                getBlanket('continueDIV');
                document.getElementById('voucherdetails').style.display="block";
                document.getElementById("voucherdetails").innerHTML=map.voucherdetails;

                oTable = $('#vouchertable').dataTable({
                    "bJQueryUI": true,
                    "sScrollY":"195px",
                    "bSort": true,
                    "bFilter": true,
                    "bPaginate": false
                    //                "sPaginationType": "full_numbers",
                    //                'iDisplayLength': 20,
                    //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
                });

            }

            
        }

        function showRemittanceForm(challaid,remittancedate){

            document.getElementById('receiptchallanid').value=challaid;
            document.getElementById('remittancedate').value=remittancedate;
            $('#voucherentryform').dialog("open");
        }


    </script>
</html>

