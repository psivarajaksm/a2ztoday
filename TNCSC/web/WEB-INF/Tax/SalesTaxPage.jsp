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
        <title>Sales Tax</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
<!--        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
-->        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" /><!--
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />-->
<!--        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>       -->
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
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

                $('#selectmonth').dialog({
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
                                TaxAction.getSalesTaxTable(amonth+1,ayear,fillAccountingYearDetails);
                                $(this).dialog("close");
                            }

                        },

                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });

                $('#salestaxdetailsform').dialog({
                    autoOpen: false,
                    width: 550,
                    height: 300,
                    modal: true,
                    buttons: {
                        "save": function() {
                            var commdityid = document.getElementById("commdityid").value;
                            var salesid=document.getElementById("salestaxid").value;
                            var quantity = document.getElementById("quantity").value;
                            var rate = document.getElementById("rate").value;
                            var ratevalue = document.getElementById("ratevalue").value;
                            var taxpercentage = document.getElementById("taxpercentage").value;
                            var taxamount = document.getElementById("taxamount").value;
                            var totalamount = document.getElementById("totalamount").value;
                            if(quantity==""){
                                alert("Please enter the quantity");
                                document.getElementById('quantity').focus();
                                return false;
                            }else if(rate==""){
                                alert("Please enter the Rate");
                                document.getElementById('rate').focus();
                                return false;
                            }else if(ratevalue==""){
                                alert("Please enter the Rate Value");
                                document.getElementById('ratevalue').focus();
                                return false;
                            }else{
//                                caluclateSalesTax(ratevalue);
                                getBlanket('continueDIV');
                                TaxAction.saveVatOnSales(salesid,amonth+1,ayear,commdityid,quantity,rate,ratevalue,taxpercentage,taxamount,totalamount, savedSalesTaxStatus);

                            }
                            $(this).dialog("close");




                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });

            });
            function savedSalesTaxStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById('monthdisp').value=map.month;
                    document.getElementById('yeardisp').value=map.year;
                    TaxAction.getSalesTaxTable(map.month,map.year,fillAccountingYearDetails);
                }
            }

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
                        <td colspan="13" class="mainheader">Sales Tax Entry</td>
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
                <div id="salestaxdetailsform" title="Sales Details" >
                    <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Commodity Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox"  id="commodity" name="commodity" size="20" readonly /></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Quantity</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox"  id="quantity" name="quantity" onchange="caluclateVatSalesTax();" onkeypress="isNumeric(this);" size="20" /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Rate</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox"  id="rate" name="rate"  onchange="caluclateVatSalesTax();" onkeypress="isNumeric(this);"  size="20" /></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Value</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox" id="ratevalue" name="ratevalue"   onchange="caluclateSalesTax(this.value);" onkeypress="isNumeric(this);"   size="20" /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Tax percentage</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox"  id="taxpercentage" name="taxpercentage" readonly onkeypress="isNumeric(this);" size="20" /></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Tax Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox" id="taxamount" name="taxamount" onkeypress="isNumeric(this);"  readonly size="20" /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Total Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox" id="totalamount" name="totalamount"  onkeypress="isNumeric(this);" readonly size="20" /></td>
                        </tr>
                    </table>
                </div>
                <div id="selectmonth" title="Select Month and Year" >
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
            <input type="hidden" name="commdityid" id="commdityid">
            <input type="hidden" name="salestaxid" id="salestaxid">


        </form>
    </body>
    <script type="text/javascript">
        function fillAccountingYearDetails(map){
            document.getElementById('perioddisp').value=map.period;
            document.getElementById('taxdetails').style.display="block";
            document.getElementById("taxdetails").innerHTML=map.salesdetails;
            oTable = $('#taxtable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers",
                'iDisplayLength': 200,
//                "aLengthMenu": [[-1], ["All"]]
                "aLengthMenu": [[ 200, -1], [ 200, "All"]]
            });
        }

        function setSalesDetails(commdityid,commdityname,salesid,quanity,rate,ratevalue,taxpercentage,taxamount,totalamount,ismodify){
            document.getElementById("commdityid").value=commdityid;
            document.getElementById("commodity").value=commdityname;
            document.getElementById("salestaxid").value=salesid;
            document.getElementById("quantity").value=quanity;
            document.getElementById("rate").value=rate;
            document.getElementById("ratevalue").value=ratevalue;
            document.getElementById("taxpercentage").value=taxpercentage;
            document.getElementById("taxamount").value=taxamount;
            document.getElementById("totalamount").value=totalamount;
            if(ismodify=="No"){
                document.getElementById('rate').readOnly=true;
                document.getElementById('ratevalue').readOnly=true;
            }else{
                document.getElementById('rate').readOnly=false;
                document.getElementById('ratevalue').readOnly=false;
            }
            $('#salestaxdetailsform').dialog('open');
            return false;

        }
        function caluclateVatSalesTax(){
            var taxamount;
            var totalamount;
            var quantity = parseFloat(document.getElementById("quantity").value);
            var rate = parseFloat(document.getElementById("rate").value);
            var ratevalue = parseFloat(document.getElementById("ratevalue").value);
            var taxpercentage = document.getElementById("taxpercentage").value;
            if(quantity==""){
                quantity="0";
            }
            if(rate==""){
                rate="0";
            }
            if(ratevalue==""){
                ratevalue="0";
            }
            ratevalue= parseFloat(quantity) * parseFloat(rate);
            document.getElementById("ratevalue").value=ratevalue;

            taxamount=parseFloat(ratevalue) * (parseFloat(taxpercentage) / parseFloat("100"));
            var ratevalue11 = document.getElementById("ratevalue").value;
            totalamount= parseFloat(ratevalue11) + parseFloat(taxamount);


            document.getElementById("taxamount").value=taxamount;
            document.getElementById("totalamount").value=totalamount;

        }
        function caluclateSalesTax(ratevalue){
            var taxamount;
            var totalamount;
            var quantity = parseFloat(document.getElementById("quantity").value);
            var rate = parseFloat(document.getElementById("rate").value);
//            var ratevalue = document.getElementById("ratevalue").value;
            var taxpercentage = document.getElementById("taxpercentage").value;
            if(quantity==""){
                quantity="0";
            }
            if(rate==""){
                rate="0";
            }
            if(ratevalue==""){
                ratevalue="0";
            }
//            ratevalue= parseFloat(quantity) * parseFloat(rate);


            taxamount=parseFloat(ratevalue) * (parseFloat(taxpercentage) / parseFloat("100"));

            totalamount= parseFloat(ratevalue) + parseFloat(taxamount);

//            document.getElementById("ratevalue").value=ratevalue;
            document.getElementById("taxamount").value=taxamount;
            document.getElementById("totalamount").value=totalamount;

        }
    </script>
</html>

