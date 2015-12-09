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
        <title>Purchase Tax Details Entry Form</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/dateValidations.js"></script>
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <script src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/TaxAction.js"></script>       
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var amonth=0;
            var ayear=0;
            $(function() {              
                
                $('#monthyear').datepicker({ 
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    yearRange: '1950:2050',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        amonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        ayear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(ayear,amonth, 1));
                    }
                }).val();                                    
                
                $('#dateform').dialog({  
                    autoOpen: true,
                    width: 600,                    
                    modal: true,                    
                    buttons: {
                        "Ok": function() {                                                               
                            var monthyear = document.getElementById('monthyear').value;  
                            if(monthyear==""){
                                alert("Please Select the Voucher Date");   
                                document.getElementById('monthyear').focus();
                                return false;
                            }else{
                                document.getElementById('monthdisp').value=amonth+1;
                                document.getElementById('yeardisp').value=ayear;
                                TaxAction.getPurchaseTaxTableDetails(amonth+1,ayear, fillAccountingYearDetails);  
                                $(this).dialog("close");
                            }
                            
                        },
                        
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#entryform').dialog({  
                    autoOpen: false,
                    width: 550,
                    height: 370,
                    modal: true,                   
                    buttons: {
                        "save": function() {  
                            
                            var vatonpurchaseid= document.getElementById('vatonpurchaseid').value;
                            var month=document.getElementById('monthdisp').value;
                            var year=document.getElementById('yeardisp').value;
                            var companyname=document.getElementById('companyname').value;
                            var billdate=document.getElementById('billdate').value;
                            var billno=document.getElementById('billno').value;
                            var commodityname=document.getElementById('commodityname').value;
                            var quantity = document.getElementById('quantity').value;
                            var rate= document.getElementById('rate').value;
                            var amount= document.getElementById('amount').value;
                            var vat= document.getElementById('vat').value;
                            var vatamt= document.getElementById('vatamt').value;
                            var total = document.getElementById('total').value;
                            var todaydate=document.getElementById('todaydate').value;
                            
                            
                            
                            if(companyname=="0"){
                                alert("Please Select Company Name");
                                document.getElementById('companyname').focus();
                                return false;
                            } else if (billdate==""){
                                alert("Please Enter Bill Date")
                                document.getElementById('billdate').focus();
                                return false;
                            }else if(isFutureDateinGrid(billdate,todaydate)){
                                alert("Entered Bill date can not be greater than the working date");
                                continuesavepart=false;
                            }else if(days_between(todaydate,billdate)>90){
                                alert("Entered Bill date can not be less than the Past 90 Days from the working date");
//                                alert("Entered Bill date should be less than the 90 Days from the working date");
                                continuesavepart=false;
                            } else if(billno==""){
                                alert("Please Enter Bill No");
                                document.getElementById('billno').focus();
                                return false;
                            } else if(commodityname=="0"){
                                alert("Please Select commodity")
                                document.getElementById('commodityname').focus();
                                return false;
                            } else if(amount==""){                                
                                alert("Please enter amount");
                                document.getElementById('amount').focus();
                                return false;
                            } else if(vat==""){
                                alert("Please Enter Vat");
                                document.getElementById('vat').focus();
                                return false;
                            } else if(vatamt==""){
                                alert("Please Enter Tax Amount");
                                document.getElementById('vatamt').focus();
                                return false;
                            } else if(total==""){
                                alert("Please enter toal amount");
                                document.getElementById('total').focus();
                                return false;
                            } else {
                                TaxAction.saveVatOnPurchase(vatonpurchaseid,month,year,companyname,billdate,billno,commodityname,quantity,rate,amount,vat,vatamt,total, savePurchaseTaxStatus);                                                         
//                                $(this).dialog("close");   
                            }    
                            
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });                
                
                $('#savebutton').click(function(){                     
                    $('#accbookdateform').dialog('open');
                    return false;
                });     
                
            });            
           
        </script>
        <style type="text/css">

            .ui-datepicker-calendar {
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
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="13" class="mainheader">Purchase Tax Entry</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="5%" class="textalign">Month</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="15%" class="textfieldalign" ><input type="text" id="monthdisp" name="monthdisp"  size="20" readonly /></td>
                        <td width="4%" class="textalign"> Year </td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15"class="textfieldalign"><input type="text" id="yeardisp" name="yeardisp"  size="20" readonly /></td>
                        <td width="%" class="textalign">Accounting Period</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15%"class="textfieldalign" ><input type="text" class="textbox" name="perioddisp" id="perioddisp" readonly ></td>
                    </tr>
                </table>
                <div id="taxdetails" style="display:none;height:370px;overflow:auto;"></div>
                <div id="entryform" title="Purchase Tax Entry Form" >                                 

                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Company Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="companyname" id="companyname" onchange="getTinNumber(this.value);"></select></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">TIN</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="tinnumber"  name="tinnumber" readonly size="20"/></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Bill No</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="billno"  name="billno" size="20"/></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Bill Date</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="datetextbox" name="billdate" id="billdate" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this);">&nbsp;[dd/mm/yyyy]</td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Commodity Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="commodityname" id="commodityname" onchange="commodityChange();"  ></select></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Quantity</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="quantity" onkeypress="isNumeric(this);" onblur="caluclateAmount();" size="20"/></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Rate</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="rate"  name="rate" onkeypress="isNumeric(this);"  onblur="caluclateAmount();" size="20"/></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Amount (Exclusive of VAT)</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="amount"  name="amount" onblur="checkFloat(this.id,'Amount');" onkeyup="caluclateVatAmount();" size="20"/></td>
                        </tr>

                        <tr class="darkrow">
                            <td width="20%" class="textalign">VAT %</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="vat"  name="vat"  onblur="checkFloat(this.id,'Vat %');" onkeyup="caluclateVatAmount();" readonly="true" size="20"/></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Vat Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="vatamt"  name="vatamt"  onblur="checkFloat(this.id,'Vat Amount');" onkeyup="caluclateVatTotAmount();" size="20"/></td>
                        </tr>


                        <tr class="darkrow">
                            <td width="20%" class="textalign">Total</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="total"  name="total" onblur="checkFloat(this.id,'Total');" size="20"/></td>
                        </tr>
                    </table>

                </div>
                <div id="dateform" title="Select Month and Year" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">                        
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Date</td>
                            <td width="2%" class="mandatory"></td>
                            <td width="50"class="textfieldalign"><input type="text" id="monthyear" name="monthyear"  size="20" /></td>
                        </tr>    
                    </table>
                </div>                

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">           
            <input type="hidden" name="vatonpurchaseid" id="vatonpurchaseid">
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">


        </form>
    </body>
    <script type="text/javascript">       
        bodyonload();
        function bodyonload(){         
            TaxAction.getPurchaseTaxTable(fillAccountingYearDetails);   
            TaxAction.loadCompanyAndCommodityNames(fillComboBoxesDetails);  
        }    
        function fillComboBoxesDetails(map){
            dwr.util.removeAllOptions("companyname");
            dwr.util.addOptions("companyname",map.companyname);
            
            dwr.util.removeAllOptions("commodityname");
            dwr.util.addOptions("commodityname",map.commodityname);
            
        }
        function getTinNumber(partyid){
            TaxAction.getTINnumber(partyid,fillComboTinDetails);
        }
        function fillComboTinDetails(map){
            document.getElementById('tinnumber').value=map.tinnumber;
        }
        function fillAccountingYearDetails(map){         
            
            document.getElementById('perioddisp').value=map.period;  
            document.getElementById('taxdetails').style.display="block";
            document.getElementById("taxdetails").innerHTML=map.voucherdetails;            
            oTable = $('#taxtable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers",
                'iDisplayLength': 20,
                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            }); 
        }
        function showTaxEntryForm(){
            document.getElementById('vatonpurchaseid').value="";
            document.getElementById('companyname').value="0";
            document.getElementById('billdate').value="";
            document.getElementById('billno').value="";
            commodityname=document.getElementById('commodityname').value="0";
            document.getElementById('quantity').value="";
            document.getElementById('rate').value="";
            document.getElementById('amount').value="";
            document.getElementById('vat').value="";
            document.getElementById('vatamt').value="";
            document.getElementById('total').value="";
            document.getElementById('tinnumber').value="";
            
            $('#entryform').dialog('open');
        }
        function setSetPurchaseTaxId(id){
            document.getElementById('vatonpurchaseid').value=id;
            TaxAction.getPurchaseonTaxDetailsForModi(id, fillPurchaseDetialForModi );           
        }
        
        function fillPurchaseDetialForModi(map){
            
            document.getElementById('vatonpurchaseid').value=map.vatonpurchaseid;
            document.getElementById('tinnumber').value=map.tinnumber;
            document.getElementById('companyname').value=map.companyname;
            document.getElementById('billdate').value=map.billdate;
            document.getElementById('billno').value=map.billno;
            commodityname=document.getElementById('commodityname').value=map.commodityname;
            document.getElementById('quantity').value=map.quantity;
            document.getElementById('rate').value=map.rate;
            document.getElementById('amount').value=map.amount;
            document.getElementById('vat').value=map.vat;
            document.getElementById('vatamt').value=map.vatamt;
            document.getElementById('total').value=map.total;
            document.getElementById('rate').readOnly=true;
            document.getElementById('amount').readOnly=true;
            document.getElementById('vat').readOnly=true;
            document.getElementById('vatamt').readOnly=true;
            document.getElementById('total').readOnly=true;
            
            $('#entryform').dialog('open'); 
            
        }
        
        function caluclateAmount(){           
            var amount;            
            var vatam;
            var quantity = document.getElementById("quantity").value;
            var rate = document.getElementById("rate").value;          
            var vat = document.getElementById("vat").value;     
            if(vat==""){
                vat=0;
            }
            if(quantity==""){
                quantity="0";
            }
            if(rate==""){
                rate="0";
            }          
            amount= parseFloat(quantity) * parseFloat(rate);           
            vatam=vat*amount/100;
            
            document.getElementById("amount").value=amount.toFixed(2);
            document.getElementById("vatamt").value=vatam.toFixed(2);
            var tot =amount+vatam;
            document.getElementById("total").value=tot.toFixed(2);
            
        }
        
        function caluclateVatAmount(){                  
            var vatam;
            var vat = document.getElementById("vat").value;
            var amount = document.getElementById("amount").value;          
            
            if(vat==""){
                vat="0";
            }
            if(amount==""){
                amount="0";
            }      
            vatam=parseFloat(vat)*parseFloat(amount)/100;
            document.getElementById("vatamt").value=vatam.toFixed(2);
            var amt=parseFloat(amount);
            var tot =amt+vatam;
            document.getElementById("total").value=tot.toFixed(2);
        }
        
        function caluclateVatTotAmount(){
            var amount = document.getElementById("amount").value;  
            var vatam = document.getElementById("vatamt").value;
            if(amount==""){
                amount="0";
            }
            if(vatam==""){
                vatam="0";
            }               
            var tot=parseFloat(amount)+parseFloat(vatam);
            document.getElementById("total").value=tot.toFixed(2);
        }
        
        function savePurchaseTaxStatus(){
            document.getElementById('vatonpurchaseid').value="";
            document.getElementById('companyname').value="0";
            document.getElementById('billdate').value="";
            document.getElementById('billno').value="";
            commodityname=document.getElementById('commodityname').value="0";
            document.getElementById('quantity').value="";
            document.getElementById('rate').value="";
            document.getElementById('amount').value="";
            document.getElementById('vat').value="";
            document.getElementById('vatamt').value="";
            document.getElementById('total').value="";
            document.getElementById('tinnumber').value="";
            
            TaxAction.getPurchaseTaxTableDetails(amonth+1,ayear, fillAccountingYearDetails);
        }       
        function commodityChange(){
            var commodityname=document.getElementById('commodityname').value;
            TaxAction.getDetailsForPurahseVat(commodityname, applyPurchaseVatDetails);  
        }        
        function applyPurchaseVatDetails(map){           
            document.getElementById('rate').value=map.rate;            
            document.getElementById('vat').value=map.vatper;
            //            alert("=="+map.ismodify);
            
            if(map.ismodify=="no"){
                document.getElementById('rate').readOnly=true;
                document.getElementById('amount').readOnly=true;
                document.getElementById('vat').readOnly=true;
                document.getElementById('vatamt').readOnly=true;
                document.getElementById('total').readOnly=true;
            }else{
                document.getElementById('rate').readOnly=false;
                document.getElementById('amount').readOnly=false;
                document.getElementById('vat').readOnly=true;
                document.getElementById('vatamt').readOnly=false;
                document.getElementById('total').readOnly=false;
            }
        }
        
    </script>
</html>

