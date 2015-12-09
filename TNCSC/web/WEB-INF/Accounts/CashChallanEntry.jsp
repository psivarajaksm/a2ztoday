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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Bank Challan</title>
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
        <script src="dwr/interface/AccountsReportsAction.js"></script>        
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            $(function() {
                $('#challandate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    maxDate: '+0d'
                }).val();                
                
                $('#voucherentryform').dialog({
                    autoOpen: false,
                    width: 600,
                    height: 130,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var recId=document.getElementById('recid').value;
                            var challanno=document.getElementById('challanno').value;
                            var challandate = document.getElementById('challandate').value;  
                            AccountsVoucherAction.addToChallanDetails("cash",recId,challanno,challandate,fillChallanDetails);
                            $(this).dialog("close"); 
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#challanformdetails').dialog({
                    autoOpen: false,
                    width: 680,
                    height: 450,
                    modal: true,
                    buttons: {                        
                        "Cancel": function() {
                            var challandate = document.getElementById('challandate').value;  
                            AccountsVoucherAction.getChallanDetails("cash",challandate,fillChallanDetails);
                            $(this).dialog("close");
                        }
                    }
                });
                
                
                
                $('#challanform').dialog({
                    autoOpen: false,
                    width: 600,
                    height: 150,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var chkarray="";
                            var chkboxes=document.forms[0].receipt;         
                            for (i=0;i<chkboxes.length;i++){
                                if (chkboxes[i].checked==true){
                                    chkarray=chkarray+chkboxes[i].value+"~";
                                }

                            }
                            var challandate = document.getElementById('challandate').value;
                            var bank = document.getElementById('bank').value;
                            var remarks = document.getElementById('remarks').value;
                            var periodcode = document.getElementById("periodcode").value;
                            AccountsVoucherAction.createBankChallan("cash",chkarray,challandate,bank,remarks,periodcode,fillChallanDetails);
                            $(this).dialog("close"); 
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                

                $('#accbookdateform').dialog({
                    autoOpen: true,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {                            
                            var challandate = document.getElementById('challandate').value;                            
                            if(challandate==""){
                                alert("Please Select from Date");
                                return false;
                            }else{
                                AccountsVoucherAction.getChallanDetails("cash",challandate,fillChallanDetails);
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


            .ui-dialog-titlebar-close{
                display: none;
            }


            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
            /**
             * It may be useful to copy the below styles and use on your page
            */
            .dataTable {
                position: relative;
                font-family: Arial, Helvetica, sans-serif;
                line-height: 1.3em;
                font-size: 13px;
            }

            .dataTable table {
                border-collapse: separate;
                position: relative;
                -webkit-user-select: none;
                -khtml-user-select: none;
                -moz-user-select: none;
                -o-user-select: none;
                user-select: none;
                border-spacing: 0;
            }

            .dataTable th,
            .dataTable td {
                border-right: 1px solid #CCC;
                border-bottom: 1px solid #CCC;
                min-width: 50px;
                height: 22px;
                line-height: 16px;
                padding: 0 4px 0 4px; /* top, bottom padding different than 0 is handled poorly by FF with HTML5 doctype */
            }

            .dataTable div.minWidthFix {
                width: 50px;
            }

            .dataTable tr:first-child th.htNoFrame,
            .dataTable th:first-child.htNoFrame,
            .dataTable th.htNoFrame {
                border-left-width: 0;
                background-color: white;
                border-color: #FFF;
            }

            .dataTable th:first-child,
            .dataTable td:first-child,
            .dataTable .htNoFrame + th,
            .dataTable .htNoFrame + td {
                border-left: 1px solid #CCC;
            }

            .dataTable tr:first-child th,
            .dataTable tr:first-child td {
                border-top: 1px solid #CCC;
            }

            .dataTable thead tr:last-child th {
                border-bottom-width: 0;
            }

            .dataTable thead tr.lastChild th {
                border-bottom-width: 0;
            }

            .dataTable th {
                background-color: #EEE;
                color: #222;
                text-align: center;
                font-weight: normal;
                white-space: nowrap;
            }

            .dataTable th .small {
                font-size: 12px;
            }

            .dataTable thead th {
                padding: 2px 4px;
            }

            .dataTable th.active {
                background-color: #CCC;
            }

            /* border background */
            .dataTable .htBorderBg {
                position: absolute;
                font-size: 0;
            }

            .dataTable .htBorderBg.selection {
                background-color: #EEF4FF;
            }

            /* border line */
            .dataTable .htBorder {
                position: absolute;
                width: 2px;
                height: 2px;
                background: #000;
                font-size: 0;
            }

            .dataTable .htBorder.current {
                background: #5292F7;
                width: 2px;
                height: 2px;
            }

            .dataTable .htBorder.selection {
                background: #89AFF9;
                width: 1px;
                height: 1px;
            }

            /* fill handle */
            .dataTable .htFillHandle {
                position: absolute;
                width: 4px;
                height: 4px;
                background: #5292F7;
                border: 1px solid #fff;
                font-size: 0;
                cursor: crosshair;
            }

            .dataTable .htBorder.htFillBorder {
                background: red;
                width: 1px;
                height: 1px;
            }

            /* textarea border color */
            textarea.handsontableInput {
                border: 2px solid #5292F7;
                outline-width: 0;
                margin: 0;
                padding: 1px 4px 0 2px;
                font-family: Arial, Helvetica, sans-serif; /*repeat from .dataTable (inherit doesn't work with IE<8) */
                line-height: 1.3em;
                font-size: 13px;
                box-shadow: 1px 2px 5px rgba(0, 0, 0, 0.4);
                resize: none;
            }

            .handsontableInputHolder.htHidden textarea.handsontableInput {
                border-color: #5292F7;
                background: #5292F7;
                color: #5292F7;
            }

            .handsontableInputHolder {
                position: absolute;
                top: 0;
                left: 0;
                width: 1px;
                height: 1px;
                overflow: hidden;
            }

            /* typeahead rules. Needed only if you are using the autocomplete feature */
            .typeahead {
                position: absolute;
                z-index: 10000;
                top: 100%;
                left: 0;
                float: left;
                display: none;
                min-width: 160px;
                padding: 4px 0;
                margin: 0;
                list-style: none;
                background-color: white;
                border-color: #CCC;
                border-color: rgba(0, 0, 0, 0.2);
                border-style: solid;
                border-width: 1px;
                -webkit-box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
                -moz-box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
                box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
                -webkit-background-clip: padding-box;
                -moz-background-clip: padding;
                background-clip: padding-box;
                margin-top: 2px;
                -webkit-border-radius: 4px;
                -moz-border-radius: 4px;
                border-radius: 4px;
            }

            .typeahead li {
                line-height: 18px;
                display: list-item;
            }

            .typeahead a {
                display: block;
                padding: 3px 15px;
                clear: both;
                font-weight: normal;
                line-height: 18px;
                color: #333;
                white-space: nowrap;
            }

            .typeahead li > a:hover, .typeahead .active > a, .typeahead .active > a:hover {
                color: white;
                text-decoration: none;
                background-color: #08C;
            }

            .typeahead a {
                color: #08C;
                text-decoration: none;
            }

            .negative {
                color: red;
            }

        </style>
    </head>
    <body>
        <form method="post" name="myform">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">  
                <br/>
                <br/>                
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="50%">
                            <div id="challandetails" style="display:none;height:370px;overflow:auto;"></div>
                        </td>                    
                        <td  width="50%">
                            <div id="receiptdetails" style="display:none;height:370px;overflow:auto;"></div>
                        </td>
                    </tr>
                </table>

                <div id="voucherentryform" title="Challan" >  
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Challan Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <select class="combobox" name="challanno" id="challanno" class="textfieldalign"  ></select>
                            </td>
                        </tr>                        
                    </table>
                </div>

                <div id="accbookdateform" title="Voucher Date & Type Selection Form" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="darkrow">
                            <td width="48%" class="textalign">From Date</td>
                            <td width="2%" class="mandatory"></td>
                            <td width="50"class="textfieldalign"><input type="text" id="challandate" name="challandate"  size="20" /></td>
                        </tr>                       
                    </table>

                </div>
                <div id="challanform" title="Bank Details" >
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Bank Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <select class="combobox" name="bank" id="bank" class="textfieldalign"  ></select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="30%" class="textalign">Remarks</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="remarks" id="remarks">                                
                            </td>
                        </tr>
                    </table>                    
                </div>                  
                <div id="challanformdetails" title="Bank Details" >                    
                    <div id="challanreceiptdetails" style="display:none;height:370px;overflow:auto;"></div>
                </div>  

            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="recid" id="recid">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
            <input type="hidden" name="periodcode" id="periodcode">
        </form>
    </body>
    <script type="text/javascript">       
        bodyonload();
        function bodyonload(){
            AccountsVoucherAction.getBankChallanEmptyTable("cash",fillChallanDetails);           
        }        
       
        function fillChallanDetails(map){                    
            document.getElementById("periodcode").value = map.periodcode;
            dwr.util.removeAllOptions("bank");
            dwr.util.addOptions("bank",map.banklist);
            
            dwr.util.removeAllOptions("challanno");
            dwr.util.addOptions("challanno",map.challanlist);            
            
            document.getElementById('challandetails').style.display="block";
            document.getElementById("challandetails").innerHTML=map.challan;

            document.getElementById('receiptdetails').style.display="block";
            document.getElementById("receiptdetails").innerHTML=map.receiptdetails;
            
            oTable = $('#challantable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers",
                'iDisplayLength': 20,
                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });
            
            oTable = $('#receipttable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers",
                'iDisplayLength': 20,
                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });
                
        }        
        function createChallan(){     
            document.getElementById('challanreceiptdetails').style.display="none";
            $('#challanform').dialog('open');
        }
        
        function sendToChallan(recId){  
            document.getElementById('recid').value=recId;
            $('#voucherentryform').dialog('open');
        }
        
        function showChallanDetails(challanid){
            AccountsVoucherAction.loadChallanDetails("cash",challanid,showChallanDetailsForm); 
            
        }
        function showChallanDetailsForm(map){
            document.getElementById('challanreceiptdetails').style.display="block";
            document.getElementById("challanreceiptdetails").innerHTML=map.receiptdetails;
            oTable = $('#challanreceipttable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers",
                'iDisplayLength': 20,
                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });            
            $('#challanformdetails').dialog('open');
        }
        
        function removeFromChallan(recptid){
            AccountsVoucherAction.removeFromChallan("cash",recptid,showChallanDetailsForm);             
        }

        function printChallanDetails(challanid){
            AccountsReportsAction.BankChallanPrintOut(challanid, "cash", EmployeePayBillPrintStatus);
        }

        function EmployeePayBillPrintStatus(map){

            if(map.ERROR==null){
                var filePath = map.filePath;
                var fileName = map.fileName;
                document.getElementById("filePath").value = filePath;
                document.getElementById("fileName").value = fileName;
                document.forms[0].action = "EmployeePayBillAction.do?method=PopupReport";
                document.forms[0].submit();
            }else{
                alert(map.ERROR);
            }
        }
    </script>
</html>

