<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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
        <title>Receipt Voucher Entry</title>
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
                $('#voucherdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    maxDate: '+0d'
                }).val();


                $('#accbookdateform').dialog({
                    autoOpen: true,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var booksele= document.getElementById('book').value;
                            var voucherdatesele = document.getElementById('voucherdate').value;
                            var periodsele = document.getElementById('periodsele').value;
                            var perioddisp = document.getElementById('perioddisp').value;
                            var finyear = perioddisp.split("-");
                            document.getElementById('openingdate').value="01/04/"+finyear[0];
                            document.getElementById('endingdate').value="31/03/"+finyear[1];
                            if(booksele=="0"){
                                alert("Please Select the Cash Book");
                                document.getElementById('book').focus();
                                return false;
                            }else if(voucherdatesele==""){
                                alert("Please Select the Receipt Date");
                                return false;
                            }else if(!compareDates("voucherdate","openingdate","greatereq")){
                                alert("Selected Voucher Date is not in selected accouting period");
                                return false;
                            }else if(!compareDates("voucherdate","endingdate","lesseq") ){
                                alert("Selected Voucher Date is not in selected accouting period");
                                return false;
                            }else{
                                AccountsVoucherAction.getVoucherDetails(booksele,periodsele,voucherdatesele,'R',fillVoucherDetailsForGivenDate);
                                $(this).dialog("close");
                            }

                        },

                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });

                $('#voucherentryform').dialog({
                    autoOpen: false,
                    width: 1020,
                    height: 500,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var continuesave = true;
                            var continuesavepart = true;
                            var accountscode='';
                            var accountsamount='';
                            var accountsoption='';
                            var partycode='';
                            var partyamount='';
                            var partypaymentmode='';
                            var refno="";
                            var bankcode="";
                            var partyalias='';
                            var chequedate="";

                            var period=document.getElementById('periodsele').value;
                            var booktype=document.getElementById('booksele').value;
                            var voucherdate=document.getElementById('voucherdatesele').value;
                            var updationtype=document.getElementById('updationtype').value;
                            var naaration=document.getElementById('narration').value;
                            var voucherid=document.getElementById('voucherid').value;
                            var sanctionedby=document.getElementById('sanctionedby').value;
                            var fileno=document.getElementById('fileno').value;
                            var todaydate=document.getElementById('todaydate').value;
                            var recamt=0;
                            accountsData = accountsContainer.handsontable("getData");
                            for (var i = 0; i < accountsData.length; i++){

                                if($.isNumeric(accountsData[i][1])){
                                    recamt=recamt+parseFloat(accountsData[i][1]);
                                }

                                if((accountsData[i][0]!='' && accountsData[i][1]!='') || (accountsData[i][0]=='' && accountsData[i][1]=='' )){
                                }else{
                                    continuesave=false;
                                }

                                if(accountsContainer.handsontable('getCell', i, 0).className=='negative'){
                                    continuesave=false;
                                }
                                if(accountsContainer.handsontable('getCell', i, 1).className=='negative'){
                                    continuesave=false;
                                }

                                accountscode=accountscode+accountcodelist[accountsData[i][0]]+"TNCSCSEPATOR";
                                accountsamount=accountsamount+accountsData[i][1]+" TNCSCSEPATOR";
                                accountsoption=accountsoption+"Receipt"+" TNCSCSEPATOR";
                            }
                            var netamt=0;
                            partyData = partycontainer.handsontable("getData");
                            for (var i = 0; i < partyData.length; i++){

                                if($.isNumeric(partyData[i][2])){
                                    netamt=netamt+parseFloat(partyData[i][2]);
                                }

                                if((partyData[i][0]!='' && partyData[i][1]!='' && partyData[i][2]!=''&& partyData[i][3]!='') || partyData[i][0]=='' && partyData[i][1]=='' && partyData[i][2]=='' && partyData[i][3]=='' ){
                                }else{
                                    continuesavepart=false;
                                }

                                if(partycontainer.handsontable('getCell', i, 0).className=='negative'){
                                    continuesavepart=false;
                                }
                                if(partycontainer.handsontable('getCell', i, 2).className=='negative'){
                                    continuesavepart=false;
                                }
                                if(partycontainer.handsontable('getCell', i, 3).className=='negative'){
                                    continuesavepart=false;
                                }

                                if(partycontainer.handsontable('getCell', i, 5).className=='negative'){
                                    continuesavepart=false;
                                }
                                if(partycontainer.handsontable('getCell', i, 6).className=='negative'){
                                    continuesavepart=false;
                                }

                                if(partylist[partyData[i][0]]!='undefined'){
                                    partycode=partycode+partylist[partyData[i][0]]+"TNCSCSEPATOR";
                                    partyalias=partyalias+partyData[i][1]+"TNCSCSEPATOR";
                                    partyamount=partyamount+partyData[i][2]+" TNCSCSEPATOR";
                                    partypaymentmode=partypaymentmode+paymentmodelist[partyData[i][3]]+" TNCSCSEPATOR";
                                    refno=refno+partyData[i][4]+" TNCSCSEPATOR";
                                    bankcode=bankcode+partyData[i][5]+" TNCSCSEPATOR";
//                                    bankcode=bankcode+banklist[partyData[i][5]]+" TNCSCSEPATOR";
                                    chequedate=chequedate+partyData[i][6]+" TNCSCSEPATOR";

                                    if(paymentmodelist[partyData[i][3]]==2 ||paymentmodelist[partyData[i][3]]==3){

                                        if(partyData[i][4]=='undefined' || partyData[i][4]==''){
                                            //                                            alert("==4="+partyData[i][4]);
                                            continuesavepart=false;
                                        }else if(partyData[i][5]=='undefined' || partyData[i][5]==''){
                                            //                                            alert("=5=="+banklist[partyData[i][5]]);
                                            continuesavepart=false;
                                        }else if(partyData[i][6]=='undefined' || partyData[i][6]==''){
                                            //                                            alert("==6="+partyData[i][6]);
                                            continuesavepart=false;
                                        }
                                        var chequeDates=partyData[i][6];
                                        if(isFutureDateinGrid(chequeDates,todaydate)){
                                            alert("Entered Cheque date can not be greater than the working date");
                                            continuesavepart=false;
                                        }
                                        if(days_between(todaydate,chequeDates)>85){
                                            alert("Entered Cheque date should be less than the 85 Days from the working date");
                                            continuesavepart=false;
                                        }

                                    }
                                }
                            }
                            if(continuesave){
                                if($.trim(naaration)!=''){
                                    if(continuesavepart){
                                        if(netamt==recamt)
                                        {
                                            //                                            if(sanctionedby==""){
                                            //                                                alert("Please Select the Sanctioned by");
                                            //                                                document.getElementById('sanctionedby').focus();
                                            //                                                return false;
                                            //                                            }else
                                            if(fileno==""){
                                                alert("Please enter the File no");
                                                document.getElementById('fileno').focus();
                                                return false;
                                            }else{
                                                getBlanket('continueDIV');
                                                AccountsVoucherAction.saveVoucherDetails(period,booktype,voucherdate,'R',updationtype,voucherid,naaration,accountscode,accountsamount,accountsoption,partycode,partyamount,partypaymentmode,refno,bankcode,sanctionedby,fileno,partyalias,chequedate, voucherSaveStatus);
                                            }
                                        }else{
                                            alert("Net Amount should be equal to party amount");
                                        }
                                    }else{
                                        alert("Data Enteted in the favour of grid is not valid!");
                                    }

                                }else{
                                    alert("Enter Narration!");
                                }

                            }else{
                                alert("Data Enteted in the payment grid is not valid!");
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
//            function voucherSaveStatus(map){
//                $('#voucherentryform').dialog('close');
//                var booksele= document.getElementById('book').value;
//                var voucherdatesele = document.getElementById('voucherdate').value;
//                var periodsele = document.getElementById('periodsele').value;
//                document.getElementById('sanctionedby').value="";
//                document.getElementById('fileno').value="";
//                getBlanket('continueDIV');
//                AccountsVoucherAction.getVoucherDetails(booksele,periodsele,voucherdatesele,'R',fillVoucherDetailsForGivenDate);
//            }

            function voucherSaveStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    $('#voucherentryform').dialog('close');
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    if(map!=null){
                        AccountsReportsAction.ReceiptPrintout(map.voucherno, map.voucherdate, map.booktype, EmployeePayBillPrintStatus);
                    }
                }
            }
            function EmployeePayBillPrintStatus(map){

                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.forms[0].action = "AccountsReportsAction.do?method=PopupPDFReport";
                    document.forms[0].submit();
                    $('#voucherentryform').dialog('close');
                    var booksele= document.getElementById('book').value;
                    var voucherdatesele = document.getElementById('voucherdate').value;
                    var periodsele = document.getElementById('periodsele').value;
                    document.getElementById('sanctionedby').value="";
                    document.getElementById('fileno').value="";
                    getBlanket('continueDIV');
                    AccountsVoucherAction.getVoucherDetails(booksele,periodsele,voucherdatesele,'R',fillVoucherDetailsForGivenDate);
                }else{
                    document.getElementById("paybillprintresult").innerHTML = map.ERROR;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.getElementById("printbut").disabled = false;
                }

            }

            function showVoucherEntryForm(){
                var booksele= document.getElementById('book').value;
                var voucherdatesele = document.getElementById('voucherdate').value;
                var periodsele = document.getElementById('periodsele').value;
                if(booksele=="0"){
                    alert("Please Select the Cash Book");
                    return false;
                }else if(voucherdatesele==""){
                    alert("Please Select the Receipt Date");
                    return false;
                }else if(periodsele==""){
                    alert("Please Select the Account Year");
                    return false;
                }else{
                    document.getElementById('updationtype').value="ADD";
                    document.getElementById('voucherid').value="";
                    document.getElementById('sanctionedby').value="";
                    document.getElementById('fileno').value="";
                    document.getElementById('narration').value="";
                    accountsData = accountsContainer.handsontable("getData");
                    partyData = partycontainer.handsontable("getData");

                    var y = new Array( accountsData.length);
                    for (var i = 0; i < accountsData.length; i++) {
                        y[i] = new Array(1);
                    }

                    for (var j = 0; j < accountsData.length; j++)
                    {
                        y[j][0]='';
                        y[j][1]='';

                    }


                    var x = new Array( partyData.length);
                    for (var i = 0; i < partyData.length; i++) {
                        x[i] = new Array(5);
                    }

                    for (var j = 0; j < partyData.length; j++)
                    {
                        x[j][0]='';
                        x[j][1]='';
                        x[j][2]='';
                        x[j][3]='';
                        x[j][4]='';
                    }

                    accountsContainer.handsontable("loadData", y);
                    partycontainer.handsontable("loadData", x);

                    //AccountsVoucherAction.getAccountsCodeListPartyDetails(fillEarningsDeductionsExcellSheet);
                    //                    $('#voucherentryform').dialog('open');
                    var cbook=document.getElementById('bookdisp').value;
                    var vdate=document.getElementById('voucherdatesele').value;
                    var titlem="Cash Book  : "+cbook+ "  Date : "+vdate;
                    $('#voucherentryform').dialog('option', 'title', titlem).dialog('open');
                    return false;
                }
            }
        </script>
        <style type="text/css">

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
                        <td colspan="13" class="mainheader">Receipt Entry</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="5%" class="textalign">Cash Book</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="15%" class="textfieldalign" ><input type="text" id="bookdisp" name="bookdisp"  size="20" readonly /></td>
                        <td width="4%" class="textalign">Date</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15"class="textfieldalign"><input type="text" id="voucherdatesele" name="voucherdatesele"  size="20" readonly /></td>
                        <td width="%" class="textalign">Accounting Period</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="15%"class="textfieldalign" ><input type="text" class="textbox" name="perioddisp" id="perioddisp" readonly ></td>
                    </tr>
                </table>
                <div id="voucherdetails" style="display:none;height:370px;overflow:auto;"></div>
                <div id="voucherentryform" title="Voucher Form" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="center" width="900">
                                <div  id="accountsGrid"  class="dataTable" style="width:980px;height:135px; overflow: auto;" ></div>
                            </td>
                        </tr>
                    </table>
                    <br>
                    <table>
                        <tr>
                            <td width="50%" valign="top" ><b>Narration:</b>
                                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td>
                                            <textarea rows="2" cols="140" id="narration" name="narration" >
                                            </textarea>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <!--                            <td align="left"><b> &nbsp; Sanctioned BY :  </b><select class="combobox" name="sanctionedby" id="sanctionedby" class="textfieldalign"></select></td>-->
                            <td align="left"><b> &nbsp; Sanctioned BY :  </b><input type="text" class="textfieldalign" id="sanctionedby" name="sanctionedby"  size="30" /></td>
                            <td colspan="2" align="left"><b>File Number :  </b><input type="text" class="textfieldalign" id="fileno" name="fileno"  size="30" /></td>
                        </tr>
                    </table>
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="center" width="900">
                                <div  id="partyGrid" class="dataTable" style="width:980px;height:135px; overflow: auto" ></div>
                            </td>
                        </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td><b>Net Amount</b></td>
                        </tr>
                        <tr>
                            <td><label id='netbalance'>0</label></td>
                        </tr>
                    </table>
                </div>

                <div id="accbookdateform" title="Voucher Date & Type Selection Form" >
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="darkrow">
                            <td width="48%" class="textalign">Cash Book</td>
                            <td width="2%" class="mandatory">*</td>
                            <td width="50%" class="textfieldalign" >
                                <select class="combobox" name="book" id="book"></select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="48%" class="textalign">Date</td>
                            <td width="2%" class="mandatory"></td>
                            <td width="50"class="textfieldalign"><input type="text" id="voucherdate" name="voucherdate"  size="20" /></td>
                        </tr>
                    </table>

                </div>
                <div id="accountingperiod" title="Voucher Date & Type Selection Form" >

                </div>

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="updationtype" id="updationtype"  value="0" >
            <input type="hidden" name="voucherid" id="voucherid"  value="0" >
            <input type="hidden" name="booksele" id="booksele"  value="0" >
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="xyz" id="xyz" readonly >
            <input type="hidden" name="rec" id="rec" readonly >
            <input type="hidden" name="pay" id="pay" readonly >
            <input type="hidden" name="ban" id="ban" readonly >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
            <input type="hidden" name="openingdate" id="openingdate"  value="0" >
            <input type="hidden" name="endingdate" id="endingdate"  value="0" >
            <input type="hidden" name="voucherno" id="voucherno">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">

        </form>
    </body>
    <script type="text/javascript">
        var newsalarystrutureid;
        var accountcodelist;
        var partylist;
        var banklist;
        var paymentmodelist;
        var newsuplesalarystructureid;
        var totalrecords=0;
        var progressstatus=0;
        var accountsContainer = $("#accountsGrid");
        var partycontainer = $("#partyGrid");
        bodyonload();
        function bodyonload(){
            AccountsVoucherAction.getVoucherEmptyTable('1',fillVoucherDetails);
            AccountsVoucherAction.getAccountsCodeListPartyDetails(fillEarningsDeductionsExcellSheet);
        }

        function fillEarningsDeductionsExcellSheet(map){

            accountcodelist=map.accountcodelist;
            partylist=map.partylist;
            banklist=map.banklist;
            paymentmodelist=map.paymentmodelist;
            //            dwr.util.removeAllOptions("sanctionedby");
            //            dwr.util.addOptions("sanctionedby",map.sactionedbylist);

            var autoComAccountsArray = new Array(accountcodelist.accountcodelistlength);
            for (var i = 0; i < accountcodelist.accountcodelistlength; i++) {
                autoComAccountsArray[i]=accountcodelist[i];
            }

            var autoComPartyArray = new Array(partylist.partylistlength);
            for (var i = 0; i < partylist.partylistlength; i++) {
                autoComPartyArray[i]=partylist[i];
            }

            var autoComBankArray = new Array(banklist.banklistlength);
            for (var i = 0; i < banklist.banklistlength; i++) {
                autoComBankArray[i]=banklist[i];
            }

            var autoComPaymodeModeArray = new Array(paymentmodelist.paymentmodelistlength);
            for (var i = 0; i < paymentmodelist.paymentmodelistlength; i++) {
                autoComPaymodeModeArray[i]=paymentmodelist[i];
            }

            accountsContainer.handsontable({
                rows: 40,
                cols: 2,
                //rowHeaders: false, //turn off 1, 2, 3, ..
                colHeaders: true,
                colHeaders: ["ACCOUNT NAME ","AMOUNT"],
                //                colHeaders: ["ACCOUNT NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","AMOUNT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"],
                fillHandle: false, //fillHandle can be turned off
                contextMenu: ["row_above", "row_below", "remove_row"],
                manualColumnResize: true,
                minSpareRows: 1,
                legend: [{
                        match: function (row, col, data) {
                            return (col === 0 );
                        },
                        style: {
                            width:780

                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 1 );
                        },
                        style: {
                            width:200

                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            var xx=data()[row][col];
                            var payamt=0;
                            accountsData = accountsContainer.handsontable("getData");
                            for (var i = 0; i < accountsData.length; i++){
                                    if($.isNumeric(accountsData[i][1])){
                                        payamt=payamt+parseFloat(accountsData[i][1])
                                    }
                            }
                            document.getElementById("netbalance").innerHTML =payamt;

                            if(col==1){
                                if(xx!=""){
                                    if(!$.isNumeric(xx)){
                                        accountsContainer.handsontable('getCell', row, col).className = 'negative';
                                    }
                                }

                            }
                            if(col==0){
                                if(xx!=""){
                                    document.getElementById('xyz').value=accountcodelist[xx];
                                    var xxx=document.getElementById('xyz').value;
                                    if(xxx=='undefined'){
                                        accountsContainer.handsontable('getCell', row, col).className = 'negative';
                                    }
                                }
                            }
                        }
                    }
                ],
                autoComplete: [
                    {
                        match: function (row, col, data) {
                            return (col === 0);
                        },
                        source: function () {
                            return autoComAccountsArray
                        }
                    }

                ]

            });

            row=partylist.partylistlength;

            partycontainer.handsontable({
                rows: 5,
                cols: 7,
                minSpareRows: 1,

                //                rowHeaders: false,
                colHeaders: true,
                colHeaders: ["RECEIVED FROM","ALIAS NAME", "AMOUNT","RECEIVE MODE","CHEQUE/DD NUMBER","BANK NAME","CHEQUE DATE(DD/MM/YYYY)"],
                //                colHeaders: ["RECEIVED FROM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","ALIAS NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", "AMOUNT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","RECEIVE MODE","NO&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","BANK NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","CHEQUE DATE(DD-MM-YYYY)"],
                //                colHeaders: ["RECEIVER NNAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", "AMOUNT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","PAYMENT MODE","NO&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","BANK NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"],
                fillHandle: false, //fillHandle can be turned off
                contextMenu: ["row_above", "row_below", "remove_row"],
                manualColumnResize: true,
                //contextMenu will only allow inserting and removing rows
                legend: [{
                        match: function (row, col, data) {
                            return (col === 0 || col === 1  || col === 5 );
                        },
                        style: {
                            width:180

                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 2 || col === 3  || col === 4 || col===6);
                        },
                        style: {
                            width:60

                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            if (col == 0 || col == 3 || col == 4) {
                                return true;
                            }
                            return false;
                        },
                        style: {
                            fontStyle: 'italic' //make the text italic
                        },
                        title: "Type to show the list of options"
                    },
                    {
                        match: function (row, col, data) {
                            var xx=data()[row][col];
                            if(col==0){
                                if(xx!=""){
                                    document.getElementById('rec').value=partylist[xx];
                                    var namestr=xx;
                                    var rec=document.getElementById('rec').value;
                                    if(rec=='undefined'){
                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
                                    }else{
                                        partycontainer.handsontable('setDataAtCell', row, col+1,namestr.replace(rec,""));
                                    }
                                }
                            }

                            if(col==2){
                                if(xx!=""){
                                    if(!$.isNumeric(xx)){
                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
                                    }
                                }

                            }


                            if(col==3)
                            {
                                if(xx!=""){
                                    document.getElementById('pay').value=paymentmodelist[xx];
                                    var pay=document.getElementById('pay').value;
                                    if(pay=='undefined'){
                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
                                    }
                                }

                            }

//                            if(col==5){
//                                if(xx!=""){
//                                    document.getElementById('ban').value=banklist[xx];
//                                    var ban=document.getElementById('ban').value;
//                                    if(ban=='undefined'){
//                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
//                                    }
//                                }
//                            }
                            if(col==6){
                                if(xx!=""){
                                    if(!isValidDateinGrid(xx)){
                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
                                    }
                                }

                            }

                        }
                    }
                ],
                autoComplete: [
                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first column
                        },
                        source: function () {
                            //return ["BMW", "Chrysler", "Nissan", "Suzuki", "Toyota", "Volvo"]
                            return autoComPartyArray
                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 3); //if it is first column
                        },
                        source: function () {
                            return autoComPaymodeModeArray

                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 5); //if it is first column
                        },
                        source: function () {
                            //return ["BMW", "Chrysler", "Nissan", "Suzuki", "Toyota", "Volvo"]
                            return autoComBankArray
                        }
                    }


                ]

            });


        }

        function showVoucherDetails(voucherId, accountingyear){
            document.getElementById('updationtype').value="MODIFY";
            document.getElementById('voucherid').value=voucherId;
            AccountsVoucherAction.getVoucherDetailsForModi(voucherId,fillVoucherDetailsForModi);
            var cbook=document.getElementById('bookdisp').value;
            var vdate=document.getElementById('voucherdatesele').value;
            var titlem="Cash Book  : "+cbook+ "  Date : "+vdate;
            $('#voucherentryform').dialog('option', 'title', titlem).dialog('open');
            //            $('#voucherentryform').dialog('open');
            return false;
        }

        function fillVoucherDetailsForModi(map){
            voucheraccountsdetails= map.voucheraccountsdetails;
            voucherpartydetails=map.voucherpartydetails;
            var voucherDet=map.voucherdetails;
            document.getElementById('narration').value=voucherDet.narration;
            document.getElementById('sanctionedby').value=voucherDet.sanctionedby;
            document.getElementById('fileno').value=voucherDet.fileno;
            var y = new Array(voucheraccountsdetails.voucherdetailslength);
            for (var i = 0; i < voucheraccountsdetails.voucherdetailslength; i++) {
                y[i] = new Array(1);
            }

            for (var j = 0; j < voucheraccountsdetails.voucherdetailslength; j++)
            {
                y[j][0]=voucheraccountsdetails[j];
                var e=parseInt(voucheraccountsdetails.voucherdetailslength)+j;
                y[j][1]=voucheraccountsdetails[e];
                //                var f=parseInt(voucheraccountsdetails.voucherdetailslength)+parseInt(voucheraccountsdetails.voucherdetailslength)+j;
                //                y[j][2]=voucheraccountsdetails[f];
            }

            var x = new Array(voucherpartydetails.voucherpartydetailslength);
            for (var i = 0; i < voucherpartydetails.voucherpartydetailslength; i++) {
                x[i] = new Array(5);
            }

            for (var j = 0; j < voucherpartydetails.voucherpartydetailslength; j++)
            {
                x[j][0]=voucherpartydetails[j];
                var i=parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+j;
                x[j][1]=voucherpartydetails[i];
                var e=parseInt(voucherpartydetails.voucherpartydetailslength)+j;
                x[j][2]=voucherpartydetails[e];
                var f=parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+j;
                x[j][3]=voucherpartydetails[f];
                var g=parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+j;
                x[j][4]=voucherpartydetails[g];
                var h=parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+j;
                x[j][5]=voucherpartydetails[h];
                var k=parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+parseInt(voucherpartydetails.voucherpartydetailslength)+j;
                x[j][6]=voucherpartydetails[k];
            }

            accountsContainer.handsontable({rows: voucheraccountsdetails.voucherdetailslength});
            partycontainer.handsontable({rows: voucherpartydetails.voucherpartydetailslength});
            accountsContainer.handsontable("loadData", y);
            partycontainer.handsontable("loadData", x);



        }
        function fillVoucherDetails(map){

            dwr.util.removeAllOptions("book");
            dwr.util.addOptions("book", map.booklist);
            document.getElementById('periodsele').value=map.periodcode;
            document.getElementById('perioddisp').value=map.period;
            document.getElementById('booksele').value=document.getElementById('book').value;
            document.getElementById('voucherdatesele').value=document.getElementById('voucherdate').value;
            document.getElementById('voucherdetails').style.display="block";
            document.getElementById("voucherdetails").innerHTML=map.voucherdetails;

            oTable = $('#vouchertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
//                "sPaginationType": "full_numbers",
//                'iDisplayLength': 20,
//                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });

        }

        function fillVoucherDetailsForGivenDate(map){
            document.getElementById('voucherdetails').style.display="block";
            document.getElementById("voucherdetails").innerHTML=map.voucherdetails;
            document.getElementById('bookdisp').value=map.bookdisp;
            document.getElementById('booksele').value=document.getElementById('book').value;
            document.getElementById('voucherdatesele').value=document.getElementById('voucherdate').value;
            oTable = $('#vouchertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
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

