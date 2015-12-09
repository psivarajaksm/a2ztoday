<%--
    Document   : transactionDeductionPage
    Created on : Jul 31, 2012, 2:43:09 PM
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
        <title>Employee Salary Deduction Others</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
         <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script src="dwr/interface/SalaryDeductionOthersAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $('#deductionmonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });



                $("#processRecord").click(function(){
                    var deductiontypes=document.getElementById('deductiontypes').value;
                    var deductionmode=document.getElementById('deductionmode').value;
                    var deductionmonth=document.getElementById('deductionmonth').value;
                    var deductionamt=document.getElementById('deductionamt').value;
                    var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                    var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                    month=eval(month)+1;
                    if(deductiontypes=="0"){
                        alert("Please Enter the Salay Deduction Type");
                        document.getElementById('deductiontypes').focus();
                        return false;
                    }else if(deductionmode=="0"){
                        alert("Please Select the Salay Deduction Mode");
                        document.getElementById('deductionmode').focus();
                        return false;
                    }else if(deductionmonth==""){
                        alert("Please Select the Deduction Month & Year");
                        document.getElementById('deductionmonth').focus();
                        return false;
                    }else if(deductionamt==""){
                        if(deductionmode=="1"){
                            alert("Please Enter the Deduction Amount");
                            document.getElementById('deductionamt').focus();
                            return false;
                        }else{
                            alert("Please Enter the Deduction Days");
                            document.getElementById('deductionamt').focus();
                            return false;
                        }

                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            SalaryDeductionOthersAction.checkDeductionProcess(deductionmode,checkDeductionProcess);
                            //                            SalaryDeductionOthersAction.salayDeductionOthersProcess(deductiontypes,deductionmode,month,year,deductionamt,displayDeductionDetails);
                        }
                    }
                });
                $("#savebutton").click(function(){
                    var installmentmode="1";
                    var delimiter="$#";
                    var chkarray="";
                    var hdnemployees=document.getElementById('hdnemployees').value;
                    var hdnprocess=document.getElementById('hdnprocess').value;
                    var isnew=document.getElementById('isnew').value;
                    var answer = confirm("Do You Want to Continue?");
                    if(hdnprocess=="yes"){
                        if (answer){
                            getBlanket('continueDIV');
                            //                            alert("hdnemployees=="+hdnemployees);
                            var chkboxes=document.forms[0].noofepno;
                            for (i=0;i<chkboxes.length;i++){
                                if (chkboxes[i].checked==true){
                                    chkarray=chkarray+chkboxes[i].value+"$#";
                                }

                            }
                            SalaryDeductionOthersAction.saveDeductionOthers(chkarray,installmentmode,delimiter,isnew,saveDeductionDetails);
                        }
                    }else{
                        alert("Please before save process the deductions");
                        return false;
                    }
                });
            });
            function checkDeductionProcess(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    return false;
                }else if(map.reason!=null && map.reason!=""){
                    getBlanket('continueDIV');
                    var answer = confirm(map.reason);
                    if (answer){
                        document.getElementById('isnew').value="2";
                        var deductiontypes=document.getElementById('deductiontypes').value;
                        var deductionmode=document.getElementById('deductionmode').value;
                        var deductionmonth=document.getElementById('deductionmonth').value;
                        var deductionamt=document.getElementById('deductionamt').value;
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        month=eval(month)+1;
                        var isnew=document.getElementById('isnew').value;
                        getBlanket('continueDIV');
                        SalaryDeductionOthersAction.salayDeductionOthersProcess(deductiontypes,deductionmode,month,year,deductionamt,isnew,displayDeductionDetails);

                        //                        var deductionmode=document.getElementById('deductionmode').value;
                        //                        var deductiontypes=document.getElementById('deductiontypes').value;
                        //                        var deductionamt=document.getElementById('deductionamt').value;
                        //                        SalaryDeductionOthersAction.getDeductionProcessedDetails(deductionmode,deductiontypes,deductionamt,displayDeductionDetails);
                    }

                } else {
                    var deductiontypes=document.getElementById('deductiontypes').value;
                    var deductionmode=document.getElementById('deductionmode').value;
                    var deductionmonth=document.getElementById('deductionmonth').value;
                    var deductionamt=document.getElementById('deductionamt').value;
                    var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                    var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                    month=eval(month)+1;
                    var isnew=document.getElementById('isnew').value;
                    getBlanket('continueDIV');
                    getBlanket('continueDIV');
                    SalaryDeductionOthersAction.salayDeductionOthersProcess(deductiontypes,deductionmode,month,year,deductionamt,isnew,displayDeductionDetails);
                }
            }
            function displayDeductionDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById('deductiondisplayHTML').style.display="block";
                    document.getElementById('deductiondisplayHTML').innerHTML=map.displayHTML;
                    document.getElementById('hdnprocess').value="yes";

                    oTable = $('#ptaxtable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"190px",
                        "bSort": true,
                        "bFilter": true,
                        "bPaginate": false
                    });
                }
            }

            function saveDeductionDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    return false;
                } else {
                    alert(map.reason);
                    getBlanket('continueDIV');
                    document.getElementById('isnew').value="1";
                    document.getElementById('deductiontypes').value="0";
                    document.getElementById('deductionmode').value="0";
                    document.getElementById('deductionmonth').value="";
                    document.getElementById('deductionamt').value="";
                    document.getElementById('deductiondisplayHTML').innerHTML="";
                    document.getElementById('deductiondisplayHTML').style.display="none";
                    document.getElementById('hdnprocess').value="";
                    document.getElementById('hdnemployees').value="";
                }
            }
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>

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

                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Employee One Day Salary Deduction</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Deduction Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="deductiontypes" id="deductiontypes" class="textfieldalign" ></select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Deduction  Mode</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="deductionmode" id="deductionmode" onchange="changeDiv(this.value)" >
                                <option value="0">--Select--</option>
                                <option value="1">Fixed Amount</option>
                                <option value="2">Salary Proportionate </option>
                            </select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Current Month And year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="deductionmonth" id="deductionmonth" readonly>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign"><div id="amtordays"></div></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="deductionamt" id="deductionamt" onkeypress="isNumeric(this);">

                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="processRecord" id="processRecord" value="Process">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
                <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td><div id="deductiondisplayHTML" align="center" style="overflow:auto;"> </div></td>
                    </tr>
                </table>
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="hdnemployees" id="hdnemployees"  value="">
            <input type="hidden" name="hdnprocess" id="hdnprocess"  value="">
            <input type="hidden" name="isnew" id="isnew"  value="1">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            SalaryDeductionOthersAction.getLoadDeductionTypes(fillDeductionsCombo)
            changeDiv("1");
        }
        function fillDeductionsCombo(map){
            dwr.util.removeAllOptions("deductiontypes");
            dwr.util.addOptions("deductiontypes",map.deductionList);
        }
        function changeDiv(idValue){
            if(idValue=="1"){
                document.getElementById('amtordays').innerHTML="<div>Amount</div>";
            }else{
                document.getElementById('amtordays').innerHTML="<div>No of Days to Recovery</div>";
            }

        }

        //        function checkEmployees(epfno){
        //           var epfnos=document.getElementById('hdnemployees').value;
        //           epfnos=epfnos+epfno+"$#";
        //           document.getElementById('hdnemployees').value=epfnos;
        //        }
    </script>
</html>

