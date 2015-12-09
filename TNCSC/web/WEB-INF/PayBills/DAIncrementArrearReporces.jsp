<%-- 
    Document   : DAIncrementArrearReporces
    Created on : May 3, 2013, 5:00:57 PM
    Author     : root
--%>
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
        <title>DA Increment Arrear Reprocess</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
        <script src="<%=staticPath%>scripts/jquery.handsontable.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <script src="dwr/interface/DAIncrementArrearAction.js"></script>
        <script src="dwr/interface/SupplementaryBillAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var dmonthid;
            var dyearid;
            $(function() {
                $('#daasondate').datepicker({
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
                
                $('#daincrementform').dialog({
                    autoOpen: false,
                    width: 850,
                    hight: 300,
                    modal: true,
                    buttons: {                        
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });

                $('#manualform').dialog({
                    autoOpen: false,
                    width: 550,
                    height: 450,
                    modal: true,
                    buttons: {
                        "save": function() {

                            var daasondate= document.getElementById('daasondate').value;
                            var basicamount=document.getElementById('basicamount').value;
                            var perpayamt=document.getElementById('perpayamt').value;
                            var gradepayamt=document.getElementById('gradepayamt').value;
                            var dueamt=document.getElementById('dueamt').value;
                            var drawnamt=document.getElementById('drawnamt').value;
                            var arrearamt=document.getElementById('arrearamt').value;
                            var epfamt = document.getElementById('epfamt').value;
                            var billtype= document.getElementById('billtype').value;                           

                            var subpaybillid= document.getElementById('dasubpaybillid').value;



                            if(daasondate==""){
                                alert("Please Select a Month and year");
                                document.getElementById('daasondate').focus();
                                return false;
                            } else if (basicamount==""){
                                alert("Please Enter the Basic")
                                document.getElementById('basicamount').focus();
                                return false;                            
                            } else if (billtype=="0"){
                                alert("Please Select the Bill Type")
                                return false;                            
                            }else {
                                getBlanket('continueDIV');
                                DAIncrementArrearAction.saveDaManual(subpaybillid,dmonthid,dyearid,basicamount,perpayamt,gradepayamt,dueamt,drawnamt,arrearamt,epfamt,billtype, saveDAManualStatus);
                                //                                $(this).dialog("close");
                            }

                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                
                $("#savebutton").click(function(){
                    
                });

                $('#dadrawnform').dialog({
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
                            //                            SupplementaryBillAction.updateincrementarrearmanual(supsalstrucidid,earningCode,earningAmount,fillIncrementModiPage);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });
            
            function setEpf(){  
                getcalc();
            
                //            document.getElementById('basicamount').value;
                //            document.getElementById('perpayamt').value;
                //            document.getElementById('gradepayamt').value;
                //            document.getElementById('oldda').value;
                //            document.getElementById('newda').value;
                //            var billtype = document.getElementById('billtype').value;
                //                        document.getElementById('dueamt').value=parseFloat(Math.round(due.toFixed(2)));
                //            document.getElementById('drawnamt').value=parseFloat(Math.round(drawn.toFixed(2)));
                //            document.getElementById('arrearamt').value=parseFloat(Math.round(arrearamt.toFixed(2)));

                            
            }


            function accountcodeSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    


                }
            }

            function saveDAManualStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');                    
                    alert(map.success);
                    $("#manualform").dialog("close");
                    $("#daincrementform").dialog("close");
                    document.getElementById('daasondate').value="";
                    document.getElementById('basicamount').value="";
                    document.getElementById('perpayamt').value="";
                    document.getElementById('gradepayamt').value="";
                    document.getElementById('dueamt').value="";
                    document.getElementById('drawnamt').value="";
                    document.getElementById('arrearamt').value="";
                    document.getElementById('epfamt').value="";
                    document.getElementById('billtype').value="0";

                }
            }
        </script>
        <script type="text/javascript">
            function onprintout(){
                var dabatch = document.forms[0].dabatch.value;
                document.getElementById("paybillprintresult").innerHTML = "";
                document.getElementById("paybillprogressbar").style.display='';
                document.getElementById("printbut").disabled = true;
                SupplementaryBillAction.EmployeeSupplementaryDABillPrintOut(dabatch, EmployeePayBillPrintStatus);
            }
            function EmployeePayBillPrintStatus(map){

                if(map.ERROR==null){
                    var filePath = map.filePath;
                    var fileName = map.fileName;
                    document.getElementById("filePath").value = filePath;
                    document.getElementById("fileName").value = fileName;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.forms[0].action = "EmployeePayBillAction.do?method=PopupReport";
                    document.forms[0].submit();
                    document.getElementById("printbut").disabled = false;
                }else{
                    document.getElementById("paybillprintresult").innerHTML = map.ERROR;
                    document.getElementById("paybillprogressbar").style.display='none';
                    document.getElementById("printbut").disabled = false;
                }
            }
        </script>
    </head>
    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>

            <div id="content">

                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">DA Increment Arrear Re process</td>
                    </tr>

                    <tr class="lightrow">
                        <td width="30%" class="textalign">DA Arrear</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <select class="combobox" name="daarrear" id="daarrear" onchange="loadBatches(this.value)" ></select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Batch Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <select class="combobox" name="dabatch" id="dabatch" onchange="loadBatchwiseEmployees(this.value)"></select>
                        </td>
                    </tr>                                     
                </table>
                <div id="employeedetails" style="display:none;height:330px;overflow:auto;"></div>
                <div id="dabatchincrementdiv" style="display: none;">
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr>
                            <td align="center"><input type="button" CLASS="submitbu" name="dabatchdeletebutton" id="dabatchdeletebutton" value="DA Deletion" onclick="removeFromDaIncrement();" ></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="daincrementform" title="DA Increment Arrear Form" >
                <div  id="dadrawnUpdationGridtwo" style="height:250px; overflow:auto;display: none;" ></div>
                <!--                <div id="daincmodidatediv">
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
                                </div>-->
                <div id="daincrementdiv" style="height:150px; overflow:auto;display: none;"></div>


            </div>

            <div id="manualform" title="DA Increment Arrear Manual Form" >
                <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Epfno / Batch no</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign">
                            <div id="epfnobatchno"></div>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Month & Year </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="daasondate"  name="daasondate"  size="20" readonly/></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Type </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign">
                            <select class="combobox" name="billtype" id="billtype" onchange="setEpf();">
                                <option value="0">--Select--</option>
                                <option value="1">Supplementary Bill</option>
                                <option value="2">Surrender Leave </option>
                                <option value="3">Increment Arrear </option>
                            </select>                            
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Old DA Percentage</td>
                        <td width="5%" class="mandatory">*</td>
                        <!--                        <td width="25%"class="textfieldalign"><input type="text" id="oldda"  name="oldda" onblur="isPercentage(this.id,'basicamount');" readonly value="72.00" size="20"/></td>-->
<!--                        <td width="25%"class="textfieldalign"><input type="text" id="oldda"  name="oldda" onblur="isPercentage(this.id,'basicamount');" size="20"/></td>-->
                        <td width="25%"class="textfieldalign"><input type="text" id="oldda"  name="oldda" size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">New DA Percentage</td>
                        <td width="5%" class="mandatory">*</td>
                        <!--                        <td width="25%"class="textfieldalign"><input type="text" id="newda"  name="newda" onblur="//isPercentage(this.id,'perpayamt');" readonly value="80.00" size="20"/></td>-->
<!--                        <td width="25%"class="textfieldalign"><input type="text" id="newda"  name="newda" onblur="isPercentage(this.id,'perpayamt');" size="20"/></td>-->
                        <td width="25%"class="textfieldalign"><input type="text" id="newda"  name="newda" size="20"/></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Basic </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="basicamount"  name="basicamount" onblur="checkFloat(this.id,'basicamount');" size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Per Pay </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="perpayamt"  name="perpayamt" onblur="checkFloat(this.id,'perpayamt');" size="20"/></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Grade Pay </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="gradepayamt"  name="gradepayamt" onblur="checkFloat(this.id,'gradepayamt');" size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Due </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="dueamt"  name="dueamt" onblur="checkFloat(this.id,'dueamt');" onfocus="getcalc();" size="20"/></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Drawn </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="drawnamt"  name="drawnamt" onblur="checkFloat(this.id,'drawnamt');" size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Arrear</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="arrearamt"  name="arrearamt" onblur="checkFloat(this.id,'arrearamt');" size="20"/></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">EPF </td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="epfamt"  name="epfamt" onblur="checkFloat(this.id,'epfamt');" size="20"/></td>
                    </tr>                    
                </table>
            </div>
            <div id="dadrawnform" title="DA Drawn Details Form" >
                <div  id="dadrawnUpdationGrid" class="dataTable" class="dataTable" style="width: 275px;height:150px; overflow: auto;" ></div>                
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="serialno" id="serialno">
            <!--            <input type="hidden" name="filePath" id="filePath">
                        <input type="hidden" name="fileName" id="fileName">-->
            <input type="hidden" name="incmansupsalstrucid" id="incmansupsalstrucid">
            <input type="hidden" name="dasubpaybillid" id="dasubpaybillid" value="">

        </form>
    </body>
    <script type="text/javascript">
        var earningsContainer = $("#earningsGrid");
        var earningsUpdationContainer = $("#earningsUpdationGrid");
        var drawnUpdationContainer = $("#dadrawnUpdationGrid");
        bodyOnload();
        function bodyOnload(){
            DAIncrementArrearAction.loadDADetails(fillDeductionsCombo);
        }
        function removeFromDaIncrement(){
            var batchid=document.getElementById('dabatch').value;
            var chkarray=""; 
            var chkboxes=document.getElementsByName('daepfnos');                    
            for (i=0;i<chkboxes.length;i++){
                if (chkboxes[i].checked==true){
                    chkarray=chkarray+chkboxes[i].value+",";
                }

            }
                    
            if(batchid=="0"){                        
                alert("Please Select the Batch Type");
                document.getElementById('dabatch').focus();
                return false;
            }else if(chkarray==""){
                alert("Please Select the atleast one Check box to Delete Employees");
                return false;
            }else{
                getBlanket('continueDIV');
                DAIncrementArrearAction.removeFromDa(batchid,chkarray,fillEmployeemap);

            }
            //        function removeFromDaIncrement(batchid, epfno){
            //            alert(batchid+"  "+epfno);       
            //            DAIncrementArrearAction.removeFromDa(batchid,chkarray,fillEmployeemap);
        }
        function fillDeductionsCombo(map){
            dwr.util.removeAllOptions("daarrear");
            dwr.util.addOptions("daarrear",map.daarrearlist);
        }
        function loadBatches(daarrearid){
            if(daarrearid=="0"){
                alert("Please Select the Da Arrear")
                document.getElementById('daarrear').focus();
                return false;
            }else{                
                DAIncrementArrearAction.loadbatchDetails(daarrearid,fillBatchesCombo);
            }            
        }
        function fillBatchesCombo(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById('daarrear').value="0";
                document.getElementById("daarrear").focus();
                // return false;
            }else{
                dwr.util.removeAllOptions("dabatch");
                dwr.util.addOptions("dabatch",map.dabatchlist);
            }            
        }

        function loadBatchwiseEmployees(batchid){
            getBlanket('continueDIV');
            DAIncrementArrearAction.getBatchEmployeeListHTML(batchid,fillEmployeemap);
        }
        function fillEmployeemap(map){
            getBlanket('continueDIV');
            document.getElementById("dabatchincrementdiv").style.display = "block";
            document.getElementById('employeedetails').style.display="block";
            document.getElementById("employeedetails").innerHTML=map.employeelist;
            //            document.getElementById('daname').value=map.daname;
            //            document.getElementById('batchno').value="";

            oTable = $('#employeetable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
                //                "sPaginationType": "full_numbers"
                //                'iDisplayLength': 20,
                //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });
        }

        function modifyDADetails(epfno,batchid){
            DAIncrementArrearAction.modifyDADetailsinHTML(batchid,epfno,showDAIncrementArrearDetails);
        }

        function showDAIncrementArrearDetails(map){
            //            getBlanket('continueDIV');            
            document.getElementById("daincrementdiv").style.display = '';            
            document.getElementById("daincrementdiv").innerHTML = map.daincrementhtml;
            $('#daincrementform').dialog('open');
        }

        //        function loadEmployeeDetails(){
        //            var epfno=document.getElementById('epfno').value;
        //            var deductioncode=document.getElementById('deductioncode').value;
        //            if(deductioncode=="0"){
        //                alert("Please Select the Account Type")
        //                document.getElementById('deductioncode').focus();
        //                return false;
        //            }else{
        //                if(epfno.length>0){
        //                    PayBillMasterAction.loadEmployeeAccountDetails(epfno,deductioncode,fillEmployeeDetails);
        //                }else{
        //                    document.getElementById('epfno').value="";
        //                    document.getElementById("epfno").focus();
        //                    document.getElementById("employeename").value="";
        //                }
        //            }
        //        }
        //        function fillEmployeeDetails(map){
        //            if(map.ERROR!=null && map.ERROR!=""){
        //                alert("Please Enter the Valid Employee EPF No");
        //                document.forms[0].epfno.value = "";
        //                document.getElementById("epfno").focus();
        //                document.getElementById("employeename").value="";
        //                document.getElementById("empaccountcode").value="";
        //
        //                // return false;
        //            } else {
        //                document.getElementById("employeename").value=map.employeename;
        //                document.getElementById("empaccountcode").value=map.accountcode;
        //                document.getElementById("serialno").value=map.serialno;
        //            }
        //        }

        function shownewdadetailsforupdation(epfno,month,year,batchid){
            DAIncrementArrearAction.getDAIncrementEarningsUpdation(epfno,month,year,batchid, fillDrawnUpdationExcellSheet)
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
                            width:100
                            //color: 'green', //make the text green and bold
                            //fontWeight: 'bold'
                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },{
                        match: function (row, col, data) {
                            return (col === 1  );
                        },
                        style: {
                            width:100
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

            $('#dadrawnform').dialog('open');
        }
        
        function checkAll(a)
        {
            var receiptAll=document.getElementsByName("dabatchselectall");
            var epfnocheckbox=document.getElementsByName("daepfnos");
            //            alert("recLen.length==="+recLen.length);
            if(receiptAll[0].checked==true)
            {
                for(var i=0; i<epfnocheckbox.length;i++)
                {
                    epfnocheckbox[i].checked=true;
                }
            }
            else
            {
                for (var i=0; i<epfnocheckbox.length;i++)
                {
                    epfnocheckbox[i].checked=false;
                }
            }
        }
        
        function addDADetails(subpaybillid,processingdetailsid, month, year, epfno, dabatchno){ 
            //alert(subpaybillid);
            DAIncrementArrearAction.getDADetailsCompManual(subpaybillid, processingdetailsid, month, year,epfno, dabatchno , fillDADetailsCompManual);
        }
        function fillDADetailsCompManual(map){            
            document.getElementById("dadrawnUpdationGridtwo").style.display = '';            
            document.getElementById("dadrawnUpdationGridtwo").innerHTML = map.dabreakups;           
        }

        function showDaManualInput(dasubpaybillid, epfno,dabatchno){
            //            alert("dasubpaybillid=="+dasubpaybillid);
            $('#manualform').dialog('open');
            document.getElementById("epfnobatchno").innerHTML = epfno+" / "+dabatchno;
            document.getElementById("dasubpaybillid").value = dasubpaybillid;
        }

        function getcalc(){
            var basicamt=document.getElementById('basicamount').value;
            var perpayamt=document.getElementById('perpayamt').value;
            var gradepayamt=document.getElementById('gradepayamt').value;
            var oldda=document.getElementById('oldda').value;
            var newda=document.getElementById('newda').value;
            var billtype = document.getElementById('billtype').value;
            if(billtype=="0"){
                alert("Please Select the Bill Type!");
                return false;
            }else{
                calculation(basicamt,perpayamt,gradepayamt,oldda,newda,billtype);
            }           
            
        }

        function calculation(basic,perpay,gradepay,oldda,newda,billtype){
            var basicamt=parseFloat(basic);
            var perpayamt=parseFloat(perpay);
            var gradepayamt=parseFloat(gradepay);
            var total="";
            var due="";
            var drawn="";
            var dueround="";
            var drawnround="";
            var arrearamt="";
            var arrearamtround="";
            var epfamt="";
            total=parseFloat(basicamt) + parseFloat(perpayamt)+parseFloat(gradepayamt);
            due=total*(parseFloat(newda)/100);
            drawn=total*(parseFloat(oldda)/100);
            dueround=parseFloat(Math.round(due.toFixed(2)));
            drawnround=parseFloat(Math.round(drawn.toFixed(2)));
            arrearamt=dueround-drawnround;
            arrearamtround=parseFloat(Math.round(arrearamt.toFixed(2)));
            
            epfamt=arrearamtround*(parseFloat(12)/100);
            document.getElementById('dueamt').value=dueround;
            document.getElementById('drawnamt').value=drawnround;
            document.getElementById('arrearamt').value=arrearamtround;        
            if(billtype=="2"){
                document.getElementById('epfamt').value="0.00";
            }else{
                document.getElementById('epfamt').value=parseFloat(Math.round(epfamt.toFixed(2)));
            }
        }
    </script>
</html>

