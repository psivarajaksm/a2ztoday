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
        <title>Cashier Entry</title>
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
        <script src="<%=staticPath%>scripts/dateValidations.js"></script>
        <script src="dwr/interface/CashierEntryAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            function trim(str) {
                return str.replace(/^\s+|\s+$/g,"");
            }
            
            function checkNumeric(numb)
            {
                var numericExpression = /^[0-9]+$/;
                if(numb.match(numericExpression))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        </script>
        <script type="text/javascript">
            $(function() {
                $('#voucherdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    maxDate: '+0d'
                }).val();
                $("#showbutton").click(function(){
                    var voucherno=document.getElementById('voucherno').value;                    

                    if(voucherno==""){
                        alert("Please Enter the Voucher Number");
                        document.getElementById('voucherno').focus();
                    }else{
                        getBlanket('continueDIV');
                        CashierEntryAction.getVoucherDetailsForModi(voucherno,fillVoucherDetailsForModi);
                        //                        $('#voucherentryform').dialog('open');
                        //                        var answer = confirm("Do You Want to Continue?");
                        //                        if (answer){
                        //                            getBlanket('continueDIV');
                        //                            CashierEntryAction.getVoucherDetails(voucherno,fillVoucherDetailsForGivenVoucherNo);
                        //                        }
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
                            var partyalias='';
                            var partyamount='';
                            var partypaymentmode='';
                            var refno="";
                            var bankcode="";
                            var chequedate="";
                            var chequeDates="";
                            var xx="";

                            
                            //                            var updationtype=document.getElementById('updationtype').value;
                            var voucherid=document.getElementById('voucherno').value;
                            var todaydate=document.getElementById('todaydate').value;
                            var vouchernumber=trim(document.getElementById('vouchernumber').value);
                            var voucherdate=document.getElementById('voucherdate').value;
                            var voucherpreparedate=document.getElementById('voucherpreparedate').value;
                            var todaydate=document.getElementById('todaydate').value;

                            var payamt=0;
                            var adjamt=0;

                            accountsData = accountsContainer.handsontable("getData");
                            for (var i = 0; i < accountsData.length; i++){

                                if(accountsData[i][2]=='Payment'){
                                    if($.isNumeric(accountsData[i][1])){
                                        payamt=payamt+parseFloat(accountsData[i][1]);
                                    }
                                }
                                if(accountsData[i][2]=='Adjustment'){
                                    if($.isNumeric(accountsData[i][1])){
                                        adjamt=adjamt+parseFloat(accountsData[i][1]);
                                    }
                                }

                                if((accountsData[i][0]!='' && accountsData[i][1]!='' && accountsData[i][2]!='') || accountsData[i][0]=='' && accountsData[i][1]=='' && accountsData[i][2]=='' ){
                                }else{
                                    continuesave=false;
                                }

                                if(accountsContainer.handsontable('getCell', i, 0).className=='negative'){
                                    continuesave=false;
                                }
                                if(accountsContainer.handsontable('getCell', i, 1).className=='negative'){
                                    continuesave=false;
                                }
                                if(accountsContainer.handsontable('getCell', i, 2).className=='negative'){
                                    continuesave=false;
                                }

                                if(accountcodelist[accountsData[i][0]]!='undefined'){
                                    accountscode=accountscode+accountcodelist[accountsData[i][0]]+"TNCSCSEPATOR";
                                    accountsamount=accountsamount+accountsData[i][1]+" TNCSCSEPATOR";
                                    accountsoption=accountsoption+accountsData[i][2]+" TNCSCSEPATOR";
                                }
                            }
//                            document.getElementById("payment").innerHTML =payamt;
//                            document.getElementById("adjustment").innerHTML = adjamt;
//                            document.getElementById("netbalance").innerHTML = payamt-adjamt;
                            
                            document.getElementById("payment").innerHTML =payamt.toFixed(2);
                            document.getElementById("adjustment").innerHTML = adjamt.toFixed(2);
                            var netamount=parseFloat(payamt)-parseFloat(adjamt);
                            netamount=(Math.round(netamount*100)/100).toFixed(2);
                            document.getElementById("netbalance").innerHTML = netamount;
                            
                            
                            var netamt=0;
                            partyData = partycontainer.handsontable("getData");
                            for (var i = 0; i < partyData.length; i++){

                                if($.isNumeric(partyData[i][2])){
                                    netamt=netamt+parseFloat(partyData[i][2]);
                                }

                                if((partyData[i][0]!=''  && partyData[i][2]!=''  && partyData[i][3]!='') || partyData[i][0]=='' && partyData[i][1]=='' && partyData[i][2]=='' && partyData[i][3]==''){
                                    //                                if((partyData[i][0]!='' && partyData[i][1]!='' && partyData[i][2]!='' ) || partyData[i][0]=='' && partyData[i][1]=='' && partyData[i][2]=='' ){
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
                                    bankcode=bankcode+banklist[partyData[i][5]]+" TNCSCSEPATOR";
                                    chequedate=chequedate+partyData[i][6]+" TNCSCSEPATOR";
                                    
                                    var pcode = partylist[partyData[i][0]];
                                    if(pcode!="undefined"){
                                        var pmode = paymentmodelist[partyData[i][3]];
                                        //                                        if(pcode!="R013" && pmode=="2"){
                                        if(pmode=="2"){
                                            var prefno = partyData[i][4];
                                            if(checkNumeric(prefno)){
                                                if(prefno.length>6){
                                                    alert("Cheque No Not Greater than 6 Characters");
                                                    continuesavepart=false;
                                                } 
                                            }else{
                                                alert("Please Enter the Valid Cheque Number Only");
                                                continuesavepart=false;
                                            }
                                        }                                        
                                    }

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
                                        if(chequeDates!=""){
                                            if(isFutureDateinGrid(voucherdate,chequeDates)){
                                                alert("Cheque date should be greater than or equals voucher date");
                                                continuesavepart=false;
                                            }
                                            if(days_between(todaydate,chequeDates)>85){
                                                alert("Entered Cheque date should be less than the 85 Days from the working date");
                                                //                                                continuesavepart=false;
                                            }
                                        }
                                    }
                                }

                            }
                            if(continuesave){
                                //                                if($.trim(naaration)!=''){
                                if( (payamt-adjamt) < 0){
                                    alert("Payment Values Should be greater or equal zero");
                                }else{
                                    if(continuesavepart){
                                        if(netamt==netamount)
//                                        if(netamt==(payamt-adjamt))
                                        {
                                            if(vouchernumber.length==0){
                                                alert("Please Enter ther Voucher Number");
                                                document.getElementById('vouchernumber').focus();
                                                return false;
                                            }else{
                                                if(!checkNumeric(vouchernumber)){
                                                    var pattern = /^\d+(-\d+)*$/;
                                                    var pattern1 = /^\d+(\/\d+)*$/;
                                                    if(vouchernumber.match(pattern) || vouchernumber.match(pattern1)){
                                                    }else{
                                                        alert("Enter the Valid Voucher Number!");
                                                        document.getElementById('vouchernumber').focus();
                                                        return false;
                                                    }
                                                }
                                            }
                                            if(voucherdate==""){
                                                alert("Please Select the Voucher Date");
                                                document.getElementById('voucherdate').focus();
                                                return false;
                                            }else if(pastDataCheckingwithslace(voucherdate,voucherpreparedate)){
                                                alert("Entered voucher date can not be less than the voucher prepared date");
                                                continuesavepart=false;
                                            }else{
                                                getBlanket('continueDIV');
                                                CashierEntryAction.saveVoucherDetails('P',voucherid,partycode,partyamount,partypaymentmode,refno,bankcode,vouchernumber,voucherdate,chequedate,partyalias,voucherSaveStatus);
                                            }
                                            
                                            //                                                CashierEntryAction.saveVoucherDetails(period,booktype,voucherdate,'P',updationtype,voucherid,naaration,accountscode,accountsamount,accountsoption,partycode,partyamount,partypaymentmode,refno,bankcode, voucherSaveStatus);
                                        }else{
                                            alert("Net Amount should be equal to party amount");
                                        }
                                    }else{
                                        alert("Data Enteted in the favour of grid is not valid!");
                                    }
                                }
                                //                                }else{
                                //                                    alert("Enter Narration!");
                                //                                }

                            }else{
                                alert("Data Enteted in the payment grid is not valid!");
                            }
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });


            });
            function voucherSaveStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    //                    $('#voucherentryform').dialog('close');
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    $('#voucherentryform').dialog('close');
                    getBlanket('continueDIV');
                    alert(map.Success);
                    document.getElementById('voucherno').value="";
                    //                    var voucherno=document.getElementById('voucherno').value;
                    //                    CashierEntryAction.getVoucherDetails(map.voucherno,fillVoucherDetailsForGivenVoucherNo);
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
                        <td colspan="13" class="headerdata">Cashier Entry</td>
                    </tr>
                    <tr>
                        <td colspan="13" class="mainheader">Cashier Entry</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="35%" class="textalign">Voucher No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" ><input type="text" id="voucherno" class="textfieldalign" name="voucherno"  size="20" /></td>
                        <!--                        <td width="5%" class="mandatory"></td>-->
                        <td width="35%" class="mandatory"><input type="button" CLASS="submitbu" name="showbutton" id="showbutton" value="Show"></td>
                    </tr>

                </table>
                <div id="voucherdetails" style="display:none;height:370px;overflow:auto;"></div>
                <div id="voucherentryform" title="Cashier Entry" >
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
                        </tr></table>
                    <table  width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="left"><b> &nbsp; Sanctioned BY   &nbsp;&nbsp; :  </b><input type="text" class="textfieldalign" id="sanctionedby" name="sanctionedby"  size="30" /></td>
                            <!--                            <td align="left"><b> &nbsp; Sanctioned BY :  </b><select class="combobox" name="sanctionedby" id="sanctionedby" class="textfieldalign"></select></td>-->
                            <td colspan="2" align="left"><b>File Number &nbsp;&nbsp;:  </b><input type="text" class="textfieldalign" id="fileno" name="fileno"  size="30" /></td>
                        </tr>
                        <tr>
                            <td align="left"><b> &nbsp; Voucher number :  </b><input type="text" class="textfieldalign" id="vouchernumber" name="vouchernumber"  size="30" /></td>

                            <td colspan="2"  align="left"><b>Voucher Date &nbsp;:  </b><input type="text" class="textfieldalign" id="voucherdate" name="voucherdate"  onblur="isValidDate(this);" size="30" /></td>
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
                            <td>Payment</td>
                            <td>Adjustment</td>
                            <td>Net</td>
                        </tr>
                        <tr>
                            <td><label id='payment'>0</label></td>
                            <td><label id='adjustment'>0</label></td>
                            <td><label id='netbalance'>0</label></td>
                        </tr>
                        <!--                        <tr>
                                                    <td colspan="3" align="left"><b>Sanctioned BY :  </b><input type="text" class="textfieldalign" id="sanctionedby" name="sanctionedby"  size="30" /></td>

                                                </tr>-->

                    </table>
                </div>
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="updationtype" id="updationtype"  value="0" >
            <!--            <input type="hidden" name="voucherid" id="voucherid"  value="0" >-->
            <input type="hidden" name="booksele" id="booksele"  value="0" >
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="voucherpreparedate" id="voucherpreparedate"  value="0" >
            <input type="hidden" name="xyz" id="xyz" readonly >
            <input type="hidden" name="rec" id="rec" readonly >
            <input type="hidden" name="pay" id="pay" readonly >
            <input type="hidden" name="ban" id="ban" readonly >
            <input type="hidden" name="xyz" id="xyzx" readonly >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">


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
            //            CashierEntryAction.getVoucherEmptyTable('1',fillVoucherDetails);
            CashierEntryAction.getAccountsCodeListPartyDetails(fillEarningsDeductionsExcellSheet);
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
                rows: 5,
                cols: 3,
                //rowHeaders: false, //turn off 1, 2, 3, ..
                colHeaders: true,
                colHeaders: ["ACCOUNT NAME ","AMOUNT","PAYMENT/ADJUSTMENT"],
                //                colHeaders: ["ACCOUNT NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","AMOUNT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","PAYMENT/ADJUSTMENT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"],
                fillHandle: false, //fillHandle can be turned off
                //                contextMenu: ["row_above", "row_below", "remove_row"],
                manualColumnResize: true,
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
                            return (col === 1 ||col === 2 );
                        },
                        style: {
                            width:100

                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            if (col == 0 || col == 1 || col==2 ) {
                                return true;
                            }
                            //                            return (col === 0); //if it is first row},
                        },
                        readOnly: true //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            var xx=data()[row][col];
                            var payamt=0;
                            var adjamt=0;
                            accountsData = accountsContainer.handsontable("getData");
                            for (var i = 0; i < accountsData.length; i++){
                                if(accountsData[i][2]=='Payment'){
                                    if($.isNumeric(accountsData[i][1])){
                                        payamt=payamt+parseFloat(accountsData[i][1])
                                    }
                                }
                                if(accountsData[i][2]=='Adjustment'){
                                    if($.isNumeric(accountsData[i][1])){
                                        adjamt=adjamt+parseFloat(accountsData[i][1]);
                                    }
                                }
                            }
//                            document.getElementById("payment").innerHTML =payamt;
//                            document.getElementById("adjustment").innerHTML = adjamt;
//                            document.getElementById("netbalance").innerHTML = payamt-adjamt;
                            document.getElementById("payment").innerHTML =payamt.toFixed(2);
                            document.getElementById("adjustment").innerHTML = adjamt.toFixed(2);
                            var netamount=parseFloat(payamt)-parseFloat(adjamt);
                            netamount=(Math.round(netamount*100)/100).toFixed(2);
                            document.getElementById("netbalance").innerHTML = netamount;
                            if(col==2)
                            {
                                if(xx!=""){
                                    if(xx!='Payment' && xx!='Adjustment'){
                                        accountsContainer.handsontable('getCell', row, col).className = 'negative'; //add class "negative"
                                    }
                                }

                            }
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
                            if (col == 2) {
                                return true;
                            }
                            return false;
                        },
                        source: function () {
                            return [" Payment", " Adjustment" ]
                        }
                    },
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
                //minSpareRows: 1,

                //                rowHeaders: false,
                colHeaders: true,
                colHeaders: ["RECEIVER NAME","ALIAS NAME", "AMOUNT","PAYMENT MODE","NO","BANK NAME","CHEQUE DATE(DD/MM/YYYY)"],
                //                colHeaders: ["RECEIVER NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","ALIAS NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", "AMOUNT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","PAYMENT MODE","NO&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","BANK NAME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","CHEQUE DATE(DD-MM-YYYY)"],
                manualColumnResize: true,                
                fillHandle: false, //fillHandle can be turned off
                minSpareRows: 1,
                contextMenu: ["row_above", "row_below", "remove_row"],
                
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
                    //                    {
                    //                        match: function (row, col, data) {
                    //                            if (col == 0 || col == 1 || col==2 || col==3) {
                    //                                return true;
                    //                            }
                    //                            //                            return (col === 0); //if it is first row},
                    //                        },
                    //                        readOnly: true //make it read-only
                    //                    },
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
                            //                            if(col==0){
                            //                                if(xx!=""){
                            //                                    document.getElementById('rec').value=partylist[xx];
                            //                                    var rec=document.getElementById('rec').value;
                            //                                    if(rec=='undefined'){
                            //                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
                            //                                    }
                            //                                }
                            //                            }
                            if(col==1){
                                if(xx!=""){
                                    document.getElementById('rec').value=xx;
                                    var rec=document.getElementById('rec').value;
                                    if(rec=='undefined'){
                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
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

                            if(col==5){
                                if(xx!=""){
                                    document.getElementById('ban').value=banklist[xx];
                                    var ban=document.getElementById('ban').value;
                                    if(ban=='undefined'){
                                        partycontainer.handsontable('getCell', row, col).className = 'negative';
                                    }
                                }
                            }
                            if(col==6){
                                if(xx!=""){
                                    if(!isValidDateinGrid(xx)){
                                        //                                        futureDataChecking(xx)
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

        function showVoucherDetails(voucherId){
            //            document.getElementById('updationtype').value="MODIFY";
            document.getElementById('voucherid').value=voucherId;
            CashierEntryAction.getVoucherDetailsForModi(voucherId,fillVoucherDetailsForModi);
            //            $('#voucherentryform').dialog('open');
            return false;
        }

        function fillVoucherDetailsForModi(map){
            if(map.ERROR!=null && map.ERROR!=""){
                getBlanket('continueDIV');
                alert(map.ERROR);
                return false;
            } else {
                getBlanket('continueDIV');
                voucheraccountsdetails= map.voucheraccountsdetails;
                voucherpartydetails=map.voucherpartydetails;
                var voucherDet=map.voucherdetails;
                document.getElementById('narration').value=voucherDet.narration;
                document.getElementById('narration').readOnly=true;
                document.getElementById('sanctionedby').value=voucherDet.sanctionedby;
                document.getElementById('fileno').value=voucherDet.fileno;
                document.getElementById('sanctionedby').readOnly=true;
                document.getElementById('fileno').readOnly=true;
                document.getElementById('vouchernumber').value=voucherDet.voucherno;
                document.getElementById('voucherdate').value=voucherDet.voucherapproveddate;
                document.getElementById('voucherpreparedate').value=map.vochdate;
                var titlem="Cash Book  : "+map.cashbook+ "  Date : "+map.vochdate;
                $('#voucherentryform').dialog('option', 'title', titlem).dialog('open');
                var y = new Array(voucheraccountsdetails.voucherdetailslength);
                for (var i = 0; i < voucheraccountsdetails.voucherdetailslength; i++) {
                    y[i] = new Array(2);
                }

                for (var j = 0; j < voucheraccountsdetails.voucherdetailslength; j++)
                {
                    y[j][0]=voucheraccountsdetails[j];
                    var e=parseInt(voucheraccountsdetails.voucherdetailslength)+j;
                    y[j][1]=voucheraccountsdetails[e];
                    var f=parseInt(voucheraccountsdetails.voucherdetailslength)+parseInt(voucheraccountsdetails.voucherdetailslength)+j;
                    y[j][2]=voucheraccountsdetails[f];
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


        }
        function fillVoucherDetailsForGivenVoucherNo(map){
            if(map.ERROR!=null && map.ERROR!=""){
                getBlanket('continueDIV');
                alert(map.ERROR);
                return false;
            } else {
                getBlanket('continueDIV');
                document.getElementById('voucherdetails').style.display="block";
                document.getElementById("voucherdetails").innerHTML=map.voucherdetails;

                oTable = $('#vouchertable').dataTable({
                    "bJQueryUI": true,
                    "sScrollY":"255px",
                    "bSort": true,
                    "bFilter": true,
                    "sPaginationType": "full_numbers",
                    'iDisplayLength': 20,
                    "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
                });
            }
        }
    </script>
</html>

