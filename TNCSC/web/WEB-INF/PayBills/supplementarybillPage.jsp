<%--
    Document   : supplementarybillPage
    Created on : Aug 2, 2012, 11:02:32 AM
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
        <title>Employee Supplementary Bill</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
        <script src="<%=staticPath%>scripts/jquery.handsontable.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <script src="dwr/interface/SupplementaryBillAction.js"></script>
        <script src="dwr/interface/EmployeePayBillAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var frommonthid;
            var fromyearid;
            var tomonthid;
            var toyearid;
            var frommonthidform;
            var fromyearidform;
            var tomonthidform;
            var toyearidform;
            var dmonthid;
            var dyearid;
            var earningslist;
            $(function() {
                $('#deductionmonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        dmonthid=eval(month)+1;
                        dyearid=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });

                $('#frommonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        frommonthid=eval(month)+1;
                        fromyearid=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });
                $('#tomonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        tomonthid=eval(month)+1;
                        toyearid=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });
                
                $('#frommonthform').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        frommonthidform=eval(month)+1;
                        fromyearidform=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });
                $('#tomonthform').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        tomonthidform=eval(month)+1;
                        toyearidform=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });
                
                $('#asondate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();
                $('#surrerderleavedate').datepicker({
                    dateFormat: "dd/mm/yy",
                    maxDate: "+0m" ,
                    changeMonth: true,
                    changeYear: true
                }).val();
                $("#addbutton").click(function(){
                    var epfno=document.getElementById('epfno').value;
                    var noofdaysid=document.getElementById('noofdaysid').value;
                    var isnewmap=document.getElementById("isnewmap").value;
                    var deductionmonth=document.getElementById("deductionmonth").value;


                    if(epfno==""){
                        alert("Please Enter the Employee EPF Number");
                        document.getElementById('epfno').focus();
                        return false;
                    }else if(deductionmonth==""){
                        alert("Please select the Supplementary Bill  Month and Year");
                        document.getElementById('deductionmonth').focus();
                        return false;
                    }else if(noofdaysid==""){
                        alert("Please enter the Number of the Days for Supplementary Bill");
                        document.getElementById('noofdaysid').focus();
                        return false;
                    }else{
                        var chkbtn=document.getElementById("addbutton").value;
                        var serialno=document.getElementById("serialno").value;
                        if(chkbtn=="Add"){
                            var answer = confirm("Do You Want to Add?");
                            if (answer){
                                getBlanket('continueDIV');
                                SupplementaryBillAction.displayAddedSupplementaryDetails(epfno,dmonthid,dyearid,noofdaysid,isnewmap,displayAddedDetails);
                            }
                        }
                        if(chkbtn=="Modify"){
                            var answer = confirm("Do You Want to Modify?");
                            if (answer){
                                getBlanket('continueDIV');
                                SupplementaryBillAction.displayAddModifyDataDetails(serialno,dmonthid,dyearid,noofdaysid,addModifyData);
                            }
                        }

                    }

                });

                $("#savebutton").click(function(){
                    var epfno=document.getElementById('epfno').value;
                    var asondate=document.getElementById('asondate').value;
                    var orderno=document.getElementById('orderno').value;
                    var mapsize=document.getElementById("isnewmap").value;
                    var funtype=document.getElementById('funtype').value;
                    if(funtype=="modify"){
                        var billno = document.getElementById('billno').value;
                    }
                    else{
                        var billno="";
                    }

                    if(epfno==""){
                        alert("Please Enter the Employee EPF Number");
                        document.getElementById('epfno').focus();
                        return false;
                    }else if(asondate==""){
                        alert("Please select the Supplementary Bill  As on Date");
                        document.getElementById('asondate').focus();
                        return false;
                    }else if(orderno==""){
                        alert("Please enter the Order Number For Supplementary Bill");
                        document.getElementById('orderno').focus();
                        return false;
                    }else if(mapsize=="0"){
                        alert("Please Enter the Supplementary Bill");                        
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            SupplementaryBillAction.saveSupplementaryBill(epfno,asondate,orderno,billno,saveSupplementaryBillDetails);
                        }
                    }

                });
                $("#saveincrementbillbutton").click(function(){                   
                    var earningCode="";
                    var earningAmount="";
                    earningsData = earningsContainer.handsontable("getData");           
                    for (var i = 0; i < earningsData.length; i++){
                        var earningCode=earningCode+earningslist[earningsData[i][0]]+"TNCSCSEPATOR";                               
                        var earningAmount =earningAmount+earningsData[i][1]+"TNCSCSEPATOR";
                    }
                    
                    var epfno=document.getElementById('epfno').value;
                    var arreardate=document.getElementById('asondate').value;
                    var frommonth=document.getElementById('frommonth').value;
                    var tomonth=document.getElementById('tomonth').value;
                    var orderno=document.getElementById('orderno').value;
                    var asondate=document.getElementById('asondate').value;
                    var designationname=document.getElementById('designationname').value;
                    if(frommonth==""){
                        alert("Please Select the From Month");
                        document.getElementById('frommonth').focus();
                        return false;
                    }else if(tomonth==""){
                        alert("Please Select the To Month");
                        document.getElementById('tomonth').focus();
                        return false;
                    }else if(arreardate==""){
                        alert("Please Select the Arrear Date");
                        document.getElementById('arreardate').focus();
                        return false;
                    }else if(orderno==""){
                        alert("Please Enter the Order Number")
                        document.getElementById('orderno').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');                          
                            SupplementaryBillAction.saveIncrementArrearBill(epfno, asondate, orderno, frommonthid, fromyearid, tomonthid, toyearid, earningCode, earningAmount, designationname, incrementArrearSaveDetails);
                        }
                    }


                });
                $("#savesurrenderbillbutton").click(function(){
                    var epfno=document.getElementById('epfno').value;
                    var noofsurrenderdays=document.getElementById('noofsurrenderdays').value;
                    var orderno=document.getElementById('orderno').value;
                    var asondate=document.getElementById('asondate').value;
                    var surrerderleavedate=document.getElementById('surrerderleavedate').value;
                    var funtype=document.getElementById('funtype').value;
                    var designationname=document.getElementById('designationname').value;
                    if(epfno==""){
                        alert("Please Enter the Employee EPF Number");
                        document.getElementById('epfno').focus();
                        return false;
                    }else if(asondate==""){
                        alert("Please select the Supplementary Bill  As on Date");
                        document.getElementById('asondate').focus();
                        return false;
                    }else if(orderno==""){
                        alert("Please enter the Order Number For Supplementary Bill");
                        document.getElementById('orderno').focus();
                        return false;
                    }else if(noofsurrenderdays==""){
                        alert("Please Enter the How Many Days to Surrender the Leave?");
                        document.getElementById('noofsurrenderdays').focus();
                        return false;
                    }                   
                    else if(surrerderleavedate=="" ){
                        alert("Please Select the Leave Surrender Date");
                        document.getElementById('surrerderleavedate').focus();
                        return false;
                    }
                    
                    else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            document.getElementById('displayDivHTML').innerHTML="";
                            SupplementaryBillAction.saveLeaveSurrenderBill(epfno,asondate,noofsurrenderdays,orderno,funtype,surrerderleavedate,designationname,masterSaveDetails);
                        }
                    }
                });
                $("#deletesurrenderbillbutton").click(function(){
                    var epfno=document.getElementById('epfno').value;
                    var asondate=document.getElementById('asondate').value;
                    var answer = confirm("Do You Want to Delete Leave Surrender?");
                    if (answer){
                        getBlanket('continueDIV');
                        SupplementaryBillAction.deleteSurrenderLeaveDatas(epfno,asondate,"LEAVESURRENDER",clearSLDetails);
                    }
                });         
                
                $("#incperiodchange").click(function(){                      
                    getBlanket('continueDIV');
                    var billid=document.getElementById('billno').value;
                    var epfno=document.getElementById('epfno').value;                    
                    var orderno=document.getElementById('orderno').value;
                    var asondate=document.getElementById('asondate').value;
                    //alert(billid);
                    //                    SupplementaryBillAction.saveIncrementArrearBill(epfno, asondate, orderno, frommonthid, fromyearid, tomonthid, toyearid, earningCode, earningAmount,incrementArrearSaveDetails);
                    SupplementaryBillAction.ModifyIncrementArrearBill(epfno, asondate, orderno,frommonthidform, fromyearidform, tomonthidform, toyearidform,billid,incrementArrearModifyDetails);
                }); 
                
                $('#drawnform').dialog({
                    autoOpen: false,
                    width: 300,
                    hight: 450,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            
                            var supsalstrucidid=document.getElementById("incmansupsalstrucid").value; 
                            var earningCode="";
                            var earningAmount="";
                            earningsData = drawnUpdationContainer.handsontable("getData");           
                            for (var i = 0; i < earningsData.length; i++){
                                var earningCode=earningCode+earningsData[i][0]+"TNCSCSEPATOR";                               
                                var earningAmount =earningAmount+earningsData[i][1]+"TNCSCSEPATOR";
                            }
                            getBlanket('continueDIV');
                            SupplementaryBillAction.updateincrementarrearmanual(supsalstrucidid,earningCode,earningAmount,fillIncrementModiPage);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#earningsform').dialog({
                    autoOpen: false,
                    width: 300,
                    hight: 450,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var supsalstrucidid=document.getElementById("incduesupsalstrucid").value;   
                            var earningCode="";
                            var earningAmount="";
                            earningsData = earningsUpdationContainer.handsontable("getData");           
                            for (var i = 0; i < earningsData.length; i++){
                                var earningCode=earningCode+earningsData[i][0]+"TNCSCSEPATOR";                               
                                var earningAmount =earningAmount+earningsData[i][1]+"TNCSCSEPATOR";
                            }
                            getBlanket('continueDIV');                            
                            SupplementaryBillAction.updateincrementarreardue(supsalstrucidid,earningCode,earningAmount,fillIncrementModiPage);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
                $('#incrementform').dialog({
                    autoOpen: false,
                    width: 1000,
                    hight: 450,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var epfno=document.getElementById('epfno').value;           
                            var asondate=document.getElementById("asondate").value;
                            getBlanket('continueDIV');
                            SupplementaryBillAction.saveIncrementArrearDetails(epfno,asondate,saveIncrementArrearStatus);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                
            });
            function fillIncrementModiPage(map)
            {                
                var epfno=document.getElementById('epfno').value;           
                var asondate=document.getElementById("asondate").value;
                var billnoform=document.getElementById("billnoform").value;
                
                SupplementaryBillAction.getIncrementArrearDetails(epfno,asondate,billnoform,showIncrementArrearDet);
                
            }
            function saveIncrementArrearStatus(map){
                getBlanket('continueDIV');
                alert(map.message);                
            }            
            function showIncrementArrearDet(map){                       
                document.getElementById("incrementdiv").style.display = '';
                document.getElementById("incrementdiv").innerHTML = map.incrementhtml;
                getBlanket('continueDIV');            
            }
            
            function clearSLDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.Success);
                    document.getElementById('funtype').value="0";
                    clearDivs();
                }
            }
            function addModifyData(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById("deductionmonth").value="";
                    document.getElementById("noofdaysid").value="";
                    document.getElementById('displayDivHTML').style.display="block";
                    document.getElementById('displayDivHTML').innerHTML=map.displayHTML;
                }
            }
            function displayAddedDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById("isnewmap").value="1";
                    document.getElementById('displayDivHTML').style.display="block";
                    document.getElementById('displayDivHTML').innerHTML=map.displayHTML;
                    document.getElementById("deductionmonth").value="";
                    document.getElementById("noofdaysid").value="";                    
                    document.getElementById("noofdaysid").focus();                    
                }
            }

            function saveSupplementaryBillDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    alert(map.success);
                    getBlanket('continueDIV');
                    clearDivs();
                    document.getElementById('funtype').value="0";                    
                }
            }
            function masterSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    alert(map.success);
                    getBlanket('continueDIV');
                    clearDivs();
                    document.getElementById('funtype').value="0";                   
                }
            }
            function incrementArrearSaveDetails(map){
                getBlanket('continueDIV');
                document.getElementById("billnoform").value=map.billno;
                document.getElementById("incrementdiv").style.display = '';
                document.getElementById("incrementdiv").innerHTML = map.incrementhtml;
                $('#incrementform').dialog('open');
                
            }

            function incrementArrearModifyDetails(map){
                getBlanket('continueDIV');
                document.getElementById("incrementdiv").style.display = '';
                document.getElementById("incrementdiv").innerHTML = map.incrementhtml;
                //                $('#incrementform').dialog('open');

            }
        </script>      
    </head>
    <body>
        <form method="post">
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">

                <table width="100%" align="center" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Employee Supplementary Bill</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="29%" class="textalign">Function Type</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="funtype" id="funtype" onchange="clearDivs()">
                                <option value="0">--Select--</option>
                                <option value="add">Addition</option>
                                <option value="modify">Modification</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Employee Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="employeename" id="employeename" readonly >
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Designation</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="designationname" id="designationname" readonly >
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">As on Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="asondate" id="asondate" readonly>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Order Number</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="orderno" id="orderno">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Type of Supplementary Bill Prepare</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="billtype" id="billtype" onchange="changeDiv(this.value)" >
                                <option value="0">--Select--</option>
                                <option value="1">Supplementary Bill</option>
                                <option value="2">Surrender Leave </option>
                                <option value="3">Increment Arrear </option>
                            </select>
                        </td>
                    </tr>
                </table>
                <div id="billsdisplayDiv" style="display: none;">
                    <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Bills</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <select class="combobox" name="billno" id="billno" class="textfieldalign" onchange="displayBillDetails(this.value)">
                                    <option value="0">--Select--</option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="supplementarybillDiv" style="display: none;">
                    <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                        <tr>
                            <td colspan="4" class="mainheader">Supplementary Bill</td>
                        </tr>
                    </table>
                    <table width="100%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Month & Year</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="deductionmonth" id="deductionmonth" readonly>

                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="30%" class="textalign">No of Days</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="noofdaysid" id="noofdaysid" >
                            </td>
                        </tr>
                    </table>

                    <table width="100%" class="tableBorder2" align="center"  border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="100%" colspan="4" align="center">
                                <input type="button" CLASS="submitbu" name="addbutton" id="addbutton" value="Add">
                                <!--                                <input type="button" CLASS="submitbu" name="modifybutton" style="display:none;" id="modifybutton" value="Modify">-->
                                <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                            </td>
                        </tr>
                    </table>                   
                </div>
                <div id="displayDivHTML" style="padding-left:350px;width:600px;height:150px;overflow:auto;display: none;">
                </div>
                <div id="surrenderbillDiv" style="display: none;">
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr>
                            <td colspan="4" class="mainheader">Surrender Leave</td>
                        </tr>
                        <tr class="lightrow">
                            <td width="30%" class="textalign">Surrender Date</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="surrerderleavedate" id="surrerderleavedate" readonly>
                            </td>
                        </tr>
                        <tr class="darkrow">
                            <td width="30%" class="textalign">No of Days leave Surrender</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="noofsurrenderdays" id="noofsurrenderdays">
                            </td>
                        </tr>
                    </table>
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr class="lightrow">
                            <td width="30%"></td>
                            <td width="5%"></td>
                            <td width="10%" align="center">
                                <input type="button" CLASS="submitbu" name="savesurrenderbillbutton" id="savesurrenderbillbutton" value="Save">
                            </td>
                            <td width="10%" align="left">
                                <div id="displaydelete" align="center" style="display:none;">
                                    <input type="button" class="submitbu" name="deletesurrenderbillbutton" id="deletesurrenderbillbutton" value="Delete">
                                </div>
                            </td>
                            <td width="45%">&nbsp;&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div id="incrementbillDiv" style="display: none;">
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr>
                            <td colspan="4" class="mainheader">Increment Arrear</td>
                        </tr>
                        <tr class="darkrow">
                            <td width="30%" class="textalign">From Month</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <!--<input type="text" class="textbox" name="frommonth" id="frommonth">-->
                                <input type="text" class="textbox" name="frommonth" id="frommonth" readonly>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="30%" class="textalign">To Month</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <!--<input type="text" class="textbox" name="tomonth" id="tomonth">-->
                                <input type="text" class="textbox" name="tomonth" id="tomonth" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td width="100%" colspan="4" class="textfieldalign" >
                                <div  id="earningsGrid" class="dataTable" class="dataTable" style="width: 275px;height:150px; overflow: auto;padding-left:525px; " ></div>
                            </td>
                        </tr>
                    </table>
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="100%" colspan="4" align="center">
                                <input type="button" CLASS="submitbu" name="saveincrementbillbutton" id="saveincrementbillbutton" value="Save">
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div id="incrementform" title="Increment Arrear Form" >     
                <div id="incmodidatediv">
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">                   
                        <tr class="darkrow">
                            <td width="14%" class="textalign">From Month</td>
                            <td width="14%" class="mandatory">*</td>
                            <td width="14%" class="textfieldalign" ><input type="text" class="textbox" name="frommonthform" id="frommonthform" readonly></td>                   
                            <td width="14%" class="textalign">To Month</td>
                            <td width="14%" class="mandatory">*</td>
                            <td width="14%" class="textfieldalign" ><input type="text" class="textbox" name="tomonthform" id="tomonthform" readonly></td>
                            <td width="14%" class="textfieldalign"><input type="button" CLASS="submitbu" name="incperiodchange" id="incperiodchange" value="Change"></td>
                        </tr>
                    </table>
                </div>
                <div id="incrementdiv" style="height:400px; overflow:auto;"></div>
            </div>

            <div id="earningsform" title="Due Details Form" >                
                <div  id="earningsUpdationGrid" class="dataTable" class="dataTable" style="width: 275px;height:150px; overflow: auto;" ></div>
            </div>

            <div id="drawnform" title="Drawn Details Form" >                
                <div  id="drawnUpdationGrid" class="dataTable" class="dataTable" style="width: 275px;height:150px; overflow: auto;" ></div>
            </div>           

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isnewmap" id="isnewmap" value="0">
            <input type="hidden" name="incduesupsalstrucid" id="incduesupsalstrucid">
            <input type="hidden" name="incmansupsalstrucid" id="incmansupsalstrucid">
            <input type="hidden" name="serialno" id="serialno" value="0">
            <input type="hidden" name="billnoform" id="billnoform" >
        </form>
    </body>
    <script type="text/javascript">
        var earningsContainer = $("#earningsGrid");
        var earningsUpdationContainer = $("#earningsUpdationGrid");
        var drawnUpdationContainer = $("#drawnUpdationGrid");
        
        function clearDivs(){
            document.getElementById("addbutton").value="Add";
            document.getElementById('epfno').value="";
            document.getElementById("employeename").value="";
            document.getElementById("designationname").value="";
            document.getElementById("asondate").value="";
            document.getElementById("orderno").value="";
            document.getElementById("billtype").value="0";
            document.getElementById("noofsurrenderdays").value="";
            document.getElementById("surrerderleavedate").value="";
            document.getElementById('displayDivHTML').innerHTML="";
            document.getElementById('supplementarybillDiv').style.display="none";
            document.getElementById('surrenderbillDiv').style.display="none";
            document.getElementById('incrementbillDiv').style.display="none";
            document.getElementById('billsdisplayDiv').style.display="none";
            document.getElementById('displayDivHTML').style.display="none";
            document.getElementById('displaydelete').style.display="none";

        }
        function loadEmployeeDetails(){
            var funtype=document.getElementById('funtype').value;
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                if(funtype=="0"){
                    alert("Please Select Funtion Type?");
                    document.getElementById('epfno').value="";
                    document.getElementById("employeename").value="";
                    document.getElementById("designationname").value="";
                    document.getElementById('funtype').focus();
                    return false;
                }else if(funtype=="add"){
                    SupplementaryBillAction.loadEmployeeDetails(epfno,fillEmployeeDetails);
                }else if(funtype=="modify"){
                    SupplementaryBillAction.loadEmployeeDetails(epfno,fillEmployeeDetails);
                }
            }else{
                document.getElementById('epfno').value="";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
                document.getElementById("designationname").value="";
            }

        }
        function fillEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert("Please Enter the Valid Employee EPF No");
                document.forms[0].epfno.value = "";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
                document.getElementById("designationname").value="";
                // return false;
            } else {
                document.getElementById("employeename").value=map.employeename;
                document.getElementById("designationname").value=map.designationname;
            }
        }
        function changeDiv(idValue){
            var epfno=document.getElementById('epfno').value;
            var funtype=document.getElementById('funtype').value;
            var asondate=document.getElementById("asondate").value;
            var orderno=document.getElementById("orderno").value;
            var designationname=document.getElementById("designationname").value;
            if(funtype=="0"){
                alert("Please Select Funtion Type?");
                document.getElementById('funtype').focus();
                document.getElementById("billtype").value="0";
            }else if(epfno==""){
                alert("Please Enter the Employee EPF Number");
                document.getElementById("billtype").value="0";
                document.getElementById('epfno').focus();
                return false;
            } else if(asondate==""){
                alert("Please select the As on date for Supplementary Bill");
                document.getElementById("billtype").value="0";
                document.getElementById('asondate').focus();
                return false;
            }else if(orderno==""){
                alert("Please enter the order no");
                document.getElementById("billtype").value="0";
                document.getElementById('orderno').focus();
                return false;
            }
            else {
                if(funtype=="add"){
                    if(idValue=="1"){
                        if("Retired Employee"==designationname){
                            alert("Supplementary Bill not allowed for Retired Employee");
                            document.getElementById("billtype").value="0";
                            document.getElementById("noofsurrenderdays").value="";
                            document.getElementById("surrerderleavedate").value="";
                            document.getElementById("frommonth").value="";
                            document.getElementById("tomonth").value="";
                            document.getElementById('supplementarybillDiv').style.display="none";
                            document.getElementById('surrenderbillDiv').style.display="none";
                            document.getElementById('incrementbillDiv').style.display="none";
                            document.getElementById('displaydelete').style.display="none";
                            document.getElementById("deductionmonth").value="";
                            document.getElementById("noofdaysid").value="";
                            document.getElementById('displayDivHTML').innerHTML="";
                            document.getElementById('displayDivHTML').style.display="none";
                            document.getElementById("isnewmap").value="0";
                        }else{
                            document.getElementById("noofsurrenderdays").value="";
                            document.getElementById("surrerderleavedate").value="";
                            document.getElementById("frommonth").value="";
                            document.getElementById("tomonth").value="";
                            document.getElementById("tomonth").value="";
                            document.getElementById('supplementarybillDiv').style.display="block";
                            document.getElementById('surrenderbillDiv').style.display="none";
                            document.getElementById('incrementbillDiv').style.display="none";
                            document.getElementById('displaydelete').style.display="none";
                            document.getElementById("isnewmap").value="0";
                        }
                    }else if(idValue=="2"){
                        document.getElementById("frommonth").value="";
                        document.getElementById("tomonth").value="";
                        document.getElementById('supplementarybillDiv').style.display="none";
                        document.getElementById('surrenderbillDiv').style.display="block";
                        document.getElementById('displaydelete').style.display="none";
                        document.getElementById('incrementbillDiv').style.display="none";
                        document.getElementById("deductionmonth").value="";
                        document.getElementById("noofdaysid").value="";
                        document.getElementById('displayDivHTML').innerHTML="";
                        document.getElementById('displayDivHTML').style.display="none";
                        document.getElementById("isnewmap").value="0";
                    }else if(idValue=="3"){
                        document.getElementById("noofsurrenderdays").value="";
                        document.getElementById('supplementarybillDiv').style.display="none";
                        document.getElementById('surrenderbillDiv').style.display="none";
                        
                        document.getElementById('displaydelete').style.display="none";
                        document.getElementById("deductionmonth").value="";
                        document.getElementById("noofdaysid").value="";
                        document.getElementById('incmodidatediv').style.visibility = "hidden";
                        // document.getElementById('displayDivHTML').innerHTML="";
                        // document.getElementById('displayDivHTML').style.display="none";
                        document.getElementById("isnewmap").value="0";
                        EmployeePayBillAction.getEmployeeEarningsAndDeductionsInc(epfno,asondate,fillEarningsDeductionsExcellSheet);
                    }else{
                        document.getElementById("noofsurrenderdays").value="";
                        document.getElementById("surrerderleavedate").value="";
                        document.getElementById("frommonth").value="";
                        document.getElementById("tomonth").value="";
                        document.getElementById('supplementarybillDiv').style.display="none";
                        document.getElementById('surrenderbillDiv').style.display="none";
                        document.getElementById('incrementbillDiv').style.display="none";
                        document.getElementById('displaydelete').style.display="none";
                        document.getElementById("deductionmonth").value="";
                        document.getElementById("noofdaysid").value="";
                        document.getElementById('displayDivHTML').innerHTML="";
                        document.getElementById('displayDivHTML').style.display="none";
                        document.getElementById("isnewmap").value="0";
                    }
                }else{
                    if(idValue=="1"){
                        if("Retired Employee"==designationname){
                            alert("Supplementary Bill not allowed for Retired Employee");
                            document.getElementById("billtype").value="0";
                            document.getElementById('supplementarybillDiv').style.display="none";
                            document.getElementById('surrenderbillDiv').style.display="none";
                            document.getElementById('incrementbillDiv').style.display="none";
                        }else{
                            var asondate=document.getElementById("asondate").value;
                            document.getElementById('supplementarybillDiv').style.display="none";
                            document.getElementById('surrenderbillDiv').style.display="none";
                            document.getElementById('incrementbillDiv').style.display="none";
                            SupplementaryBillAction.modifySupplementaryBillDatas(epfno,asondate,"SUPLEMENTARYBILL",fillExistingSupplementaryBillDetails);
                        }
                    }else if(idValue=="2"){
                        var asondate=document.getElementById("asondate").value;
                        document.getElementById('supplementarybillDiv').style.display="none";
                        document.getElementById('displayDivHTML').style.display="none";
                        document.getElementById('surrenderbillDiv').style.display="none";
                        document.getElementById('incrementbillDiv').style.display="none";
                        SupplementaryBillAction.modifySurrenderLeaveDatas(epfno,asondate,"LEAVESURRENDER",fillExistingSupplementaryBillDetails);
                    }else if(idValue=="3"){                        
                        var asondate=document.getElementById("asondate").value;
                        document.getElementById('supplementarybillDiv').style.display="none";
                        document.getElementById('surrenderbillDiv').style.display="none";
                        document.getElementById('incrementbillDiv').style.display="none";                        
                        document.getElementById('incmodidatediv').style.visibility = "visible";
                        //getBlanket('continueDIV');
                        SupplementaryBillAction.modifyIncrementArrears(epfno,asondate,"INCREMENTARREAR",fillExistingSupplementaryBillDetails);
                        //SupplementaryBillAction.getIncrementArrearDetails(epfno,asondate,showIncrementArrearDetails);
                    }
                }
            }

        }
        function deleteEmployees(mapId){
            var answer = confirm("Do You Want to Delete this Row?");
            if (answer){
                getBlanket('continueDIV');
                SupplementaryBillAction.deleteSupplementaryBillData(mapId,fillSupplementaryBillDetails);
            }

        }
        function fillSupplementaryBillDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                return false;
            } else {
                getBlanket('continueDIV');
                document.getElementById("isnewmap").value=map.mapsize;
                document.getElementById('displayDivHTML').style.display="block";
                document.getElementById('displayDivHTML').innerHTML=map.displayHTML;
            }

        }
        function fillExistingSupplementaryBillDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById("billtype").value="0";
                return false;
            } else {
                var billtype=document.getElementById('billtype').value;
                if(billtype=="1"){
                    document.getElementById('billsdisplayDiv').style.display="block";
                    dwr.util.removeAllOptions("billno");
                    dwr.util.addOptions("billno",map);
                }else if(billtype=="2"){
                    document.getElementById('billsdisplayDiv').style.display="none";
                    document.getElementById("noofsurrenderdays").value=map.noofdays;
                    document.getElementById("surrerderleavedate").value=map.sldate;
                    document.getElementById('supplementarybillDiv').style.display="none";
                    document.getElementById('surrenderbillDiv').style.display="block";
                    document.getElementById('incrementbillDiv').style.display="none";
                    document.getElementById('displaydelete').style.display="block";
                }else if(billtype=="3"){                  
                    document.getElementById('displayDivHTML').style.display="none";
                    document.getElementById('billsdisplayDiv').style.display="block";
                    dwr.util.removeAllOptions("billno");
                    dwr.util.addOptions("billno",map);
                }

            }
        }
        function displayBillDetails(billid){
            if(billid!=0){         
                var billtype=document.getElementById('billtype').value;
                document.getElementById("billnoform").value=billid;
                if(billtype==1){
                    SupplementaryBillAction.displaySupplementaryBillsData(billid,displaySupplementaryBillDetails);                
                }
                if(billtype==3){
                    document.getElementById('displayDivHTML').style.display="none";
                    var asondate=document.getElementById("asondate").value;
                    var epfno=document.getElementById('epfno').value;
                    document.getElementById('supplementarybillDiv').style.display="none";
                    document.getElementById('surrenderbillDiv').style.display="none";
                    document.getElementById('incrementbillDiv').style.display="none";
                    getBlanket('continueDIV');                        
                    SupplementaryBillAction.getIncrementArrearDetails(epfno,asondate,billid,showIncrementArrearDetails);
                }
            }else{
                document.getElementById('displayDivHTML').style.display="none";
            }
        }
        function displaySupplementaryBillDetails(map){
            document.getElementById("isnewmap").value=map.mapsize;
            document.getElementById('displayDivHTML').style.display="block";
            document.getElementById('displayDivHTML').innerHTML=map.displayHTML;
        }
        function getModifyData(sno,date,month,year,noofdays){
            document.getElementById('supplementarybillDiv').style.display="block";
            document.getElementById("addbutton").value="Modify";
            //            document.getElementById("deductionmonth").value=date;
            document.getElementById("noofdaysid").value=noofdays;
            document.getElementById("serialno").value=sno;
            $('#deductionmonth').datepicker("setDate", new Date(year,eval(month)-1,01) );
            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            dmonthid=eval(month)+1;
            dyearid=eval(year);
        }


        function fillEarningsDeductionsExcellSheet(map){
            if(map.status=="NEW"){
                document.getElementById('incrementbillDiv').style.display="block";
                document.getElementById('displayDivHTML').innerHTML="";
                document.getElementById('displayDivHTML').style.display="none";
                earningslist=map.earningslist;
                var autoComEarningsArray = new Array(earningslist.earningslistlength);
                for (var i = 0; i < earningslist.earningslistlength; i++) {
                    autoComEarningsArray[i]=earningslist[i];
                }

                employeeearningslist=map.employeeearningslist;
                row=employeeearningslist.employeeearningslength;



                earningsContainer.handsontable({
                    rows: row,
                    cols: 2,
                    rowHeaders: false, //turn off 1, 2, 3, ..
                    // minSpareRows: 1,

                    rowHeaders: true,
                    colHeaders: true,
                    colHeaders: ["EARNINGS", "AMOUNT"],
                    fillHandle: false, //fillHandle can be turned off

                    contextMenu: ["row_above", "row_below", "remove_row"],
                    //contextMenu will only allow inserting and removing rows
                    legend: [

                        {
                            match: function (row, col, data) {
                                return (col === 0); //if it is first row
                            },
                            style: {
                                //color: 'green', //make the text green and bold
                                //fontWeight: 'bold'
                            },
                            title: 'Heading', //make some tooltip
                            readOnly: false //make it read-only
                        },
                        {
                            match: function (row, col, data) {
                                var xx=data()[row][col];
                                if (parseInt(data()[row][col], 10) < 0) { //if row contains negative number
                                    earningsContainer.handsontable('getCell', row, col).className = 'negative'; //add class "negative"
                                }
                                else {
                                    earningsContainer.handsontable('getCell', row, col).className = '';
                                    if (col==1){
                                        if(xx.length)
                                        {
                                            earningsContainer.handsontable('getCell', row, col).className = '';
                                            var strValidChars = "0123456789,.-";
                                            var strChar;
                                            for (var i = 0; i < xx.length; i++){
                                                strChar = xx.charAt(i);
                                                if (strValidChars.indexOf(strChar) == -1){
                                                    earningsContainer.handsontable('getCell', row, col).className = 'negative';
                                                }
                                            }
                                        }
                                        earningsData = earningsContainer.handsontable("getData");
                                        if(earningsData.length>row){
                                            setTimeout(function () {
                                                earningsContainer.handsontable("selectCell", row+1, 0);
                                            }, 10);
                                        }
                                    }
                                }
                                if (col==0){
                                    var x=(data()[row][col]);
                                    if (autoComEarningsArray.indexOf(x) == -1){

                                        earningsContainer.handsontable('getCell', row, col).className = 'negative'; //add class "negative"
                                    }
                                    else
                                    {
                                        earningsContainer.handsontable('getCell', row, col).className = '';

                                    }

                                    setTimeout(function () {
                                        earningsContainer.handsontable("selectCell", row, 1);
                                    }, 10);

                                }

                            }
                        }
                    ],
                    autoComplete: [
                        {
                            match: function (row, col, data) {
                                if (col == 2 || col == 3) {
                                    return true;
                                }
                                return false;
                            },
                            highlighter: function (item) {
                                var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
                                var label = item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
                                    return '<strong>' + match + '</strong>';
                                });
                                return '<span style="margin-right: 10px; background-color: ' + item + '">&nbsp;&nbsp;&nbsp;</span>' + label;
                            },
                            source: function () {
                                return ["yellow", "red", "orange", "green", "blue", "gray", "black", "white"]
                            }
                        },
                        {
                            match: function (row, col, data) {
                                return (col === 0); //if it is first column
                            },
                            source: function () {
                                //return ["BMW", "Chrysler", "Nissan", "Suzuki", "Toyota", "Volvo"]
                                return autoComEarningsArray
                            }
                        }

                    ]

                });



                var x = new Array(employeeearningslist.employeeearningslength);
                for (var i = 0; i < employeeearningslist.employeeearningslength; i++) {
                    x[i] = new Array(1);
                }

                for (var j = 0; j < employeeearningslist.employeeearningslength; j++)
                {
                    x[j][0]=employeeearningslist[j];
                    var e=parseInt(employeeearningslist.employeeearningslength)+j;
                    x[j][1]=employeeearningslist[e];
                }

                earningsContainer.handsontable("loadData", x);
            }else{
                alert("Increment Arrear Already Made for this Employee Provident Fund Number for the entered date ");
            }
        }

        function fillEarningsUpdationExcellSheet(map){
            earningslist=map.earningslist;
            var autoComEarningsArray = new Array(earningslist.earningslistlength);
            for (var i = 0; i < earningslist.earningslistlength; i++) {
                autoComEarningsArray[i]=earningslist[i];
            }
            employeeearningslist=map.employeeearningslist;
            row=employeeearningslist.employeeearningslength;
            earningsUpdationContainer.handsontable({
                rows: row,
                cols: 2,
                rowHeaders: false, //turn off 1, 2, 3, ..
                // minSpareRows: 1,

                rowHeaders: true,
                colHeaders: true,
                colHeaders: ["EARNINGS", "AMOUNT"],
                fillHandle: false, //fillHandle can be turned off

                contextMenu: ["row_above", "row_below", "remove_row"],
                //contextMenu will only allow inserting and removing rows
                legend: [

                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first row
                        },
                        style: {
                            //color: 'green', //make the text green and bold
                            //fontWeight: 'bold'
                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {   
                            var xx=data()[row][col];
                           
                           

                        }
                    }
                ],
                autoComplete: [
                    {
                        match: function (row, col, data) {
                            if (col == 2 || col == 3) {
                                return true;
                            }
                            return false;
                        },
                        highlighter: function (item) {
                            var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
                            var label = item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
                                return '<strong>' + match + '</strong>';
                            });
                            return '<span style="margin-right: 10px; background-color: ' + item + '">&nbsp;&nbsp;&nbsp;</span>' + label;
                        },
                        source: function () {
                            return ["yellow", "red", "orange", "green", "blue", "gray", "black", "white"]
                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first column
                        },
                        source: function () {
                            //return ["BMW", "Chrysler", "Nissan", "Suzuki", "Toyota", "Volvo"]
                            return autoComEarningsArray
                        }
                    }

                ]

            });



            var x = new Array(employeeearningslist.employeeearningslength);
            for (var i = 0; i < employeeearningslist.employeeearningslength; i++) {
                x[i] = new Array(1);
            }

            for (var j = 0; j < employeeearningslist.employeeearningslength; j++)
            {
                x[j][0]=employeeearningslist[j];
                var e=parseInt(employeeearningslist.employeeearningslength)+j;
                x[j][1]=employeeearningslist[e];
            }

            earningsUpdationContainer.handsontable("loadData", x);            
          
        }
        
        function showDueDetails(month, year, epfno,supsalstrucidid){
            document.getElementById("incduesupsalstrucid").value=supsalstrucidid;
            SupplementaryBillAction.getDueDetails(epfno,month,year,supsalstrucidid,fillEarningsUpdationExcellSheet);
            $('#earningsform').dialog('open');             
        }
        function showManIncDetails(month, year, epfno,payprocesid){            
            SupplementaryBillAction.getManIncDetails(epfno,month,year,payprocesid,fillDrawnUpdationExcellSheet);           
        }       
        
        function showIncrementArrearDetails(map){
            getBlanket('continueDIV');
            document.getElementById("incrementdiv").style.display = '';
            document.getElementById("incrementdiv").innerHTML = map.incrementhtml;
            $('#incrementform').dialog('open');
        }
        
        function fillDrawnUpdationExcellSheet(map){
            
            document.getElementById("incmansupsalstrucid").value=map.subpayproid;
            
            earningslist=map.earningslist;
            var autoComEarningsArray = new Array(earningslist.earningslistlength);
            for (var i = 0; i < earningslist.earningslistlength; i++) {
                autoComEarningsArray[i]=earningslist[i];
            }
            employeeearningslist=map.employeeearningslist;
            row=employeeearningslist.employeeearningslength;
            drawnUpdationContainer.handsontable({
                rows: row,
                cols: 2,
                rowHeaders: false, //turn off 1, 2, 3, ..
                // minSpareRows: 1,

                rowHeaders: true,
                colHeaders: true,
                colHeaders: ["EARNINGS", "AMOUNT"],
                fillHandle: false, //fillHandle can be turned off

                contextMenu: ["row_above", "row_below", "remove_row"],
                //contextMenu will only allow inserting and removing rows
                legend: [

                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first row
                        },
                        style: {
                            //color: 'green', //make the text green and bold
                            //fontWeight: 'bold'
                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {   
                            var xx=data()[row][col];
                           
                           

                        }
                    }
                ],
                autoComplete: [
                    {
                        match: function (row, col, data) {
                            if (col == 2 || col == 3) {
                                return true;
                            }
                            return false;
                        },
                        highlighter: function (item) {
                            var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
                            var label = item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
                                return '<strong>' + match + '</strong>';
                            });
                            return '<span style="margin-right: 10px; background-color: ' + item + '">&nbsp;&nbsp;&nbsp;</span>' + label;
                        },
                        source: function () {
                            return ["yellow", "red", "orange", "green", "blue", "gray", "black", "white"]
                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first column
                        },
                        source: function () {
                            //return ["BMW", "Chrysler", "Nissan", "Suzuki", "Toyota", "Volvo"]
                            return autoComEarningsArray
                        }
                    }

                ]

            });


            var x = new Array(employeeearningslist.employeeearningslength);
            for (var i = 0; i < employeeearningslist.employeeearningslength; i++) {
                x[i] = new Array(1);
            }

            for (var j = 0; j < employeeearningslist.employeeearningslength; j++)
            {
                x[j][0]=employeeearningslist[j];  
                var e=parseInt(employeeearningslist.employeeearningslength)+j;
                x[j][1]=employeeearningslist[e];
            }

            drawnUpdationContainer.handsontable("loadData", x);
            
            $('#drawnform').dialog('open');
        }
        
        function showdrwandetailsforupdation(epfno,month,year){
            var asondate=document.getElementById('asondate').value;
            var designationname=document.getElementById('designationname').value;
            SupplementaryBillAction.getIncrementEarningsUpdation(epfno,month,year, asondate,designationname, fillDrawnUpdationExcellSheet);            
        }
        
    </script>
</html>

