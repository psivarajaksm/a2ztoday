<%--
    Document   : professionaltaxDeductionPage
    Created on : Aug 14, 2012, 2:43:09 PM
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
        <title>Professional Tax Deduction</title>
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
            function processRecord(){
                var finanicalyear=document.getElementById('finanicalyears').value;
                var deductionmode=document.getElementById('deductionmode').value;
                var installmentmode=document.getElementById('installmentmode').value;
                if(finanicalyear=="0"){
                    alert("Please Select the Finanical Years ");
                    document.getElementById('finanicalyears').focus();
                    return false;
                }else if(deductionmode=="0"){
                    alert("Please Select the Deduction Mode");
                    document.getElementById('deductionmode').focus();
                    return false;
                }else if(installmentmode=="0"){
                    alert("Please Select the Installment Mode");
                    document.getElementById('installmentmode').focus();
                    return false;
                }else{
                    var answer = confirm("Do You Want to Continue?");
                    if (answer){
                        getBlanket('continueDIV');
                        SalaryDeductionOthersAction.checkDeductionProcess(deductionmode,checkDeductionProcess);
                        //                            SalaryDeductionOthersAction.proffesionalTaxDeductionProcess(finanicalyear,deductionmode,installmentmode,displayPTDetails);
                    }
                }
            }
            function checkDeductionProcess(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                }else if(map.reason!=null && map.reason!=""){
                    getBlanket('continueDIV');
                    var answer = confirm(map.reason);
                    if (answer){
                        document.getElementById('isnew').value="2";
                        var finanicalyear=document.getElementById('finanicalyears').value;
                        var deductionmode=document.getElementById('deductionmode').value;
                        var installmentmode=document.getElementById('installmentmode').value;
                        var isnew=document.getElementById('isnew').value;
                        getBlanket('continueDIV');
                        SalaryDeductionOthersAction.proffesionalTaxDeductionProcess(finanicalyear,deductionmode,installmentmode,isnew,displayPTDetails);
                    }

                } else {
                    var finanicalyear=document.getElementById('finanicalyears').value;
                    var deductionmode=document.getElementById('deductionmode').value;
                    var installmentmode=document.getElementById('installmentmode').value;
                    var isnew=document.getElementById('isnew').value;
//                    getBlanket('continueDIV');
                    SalaryDeductionOthersAction.proffesionalTaxDeductionProcess(finanicalyear,deductionmode,installmentmode,isnew,displayPTDetails);
                    //                        SalaryDeductionOthersAction.salayDeductionOthersProcess(deductiontypes,deductionmode,month,year,deductionamt,displayDeductionDetails);
                }
            }
            function displayPTDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById('pfdisplayHTML').style.display="block";
                    document.getElementById('pfdisplayHTML').innerHTML=map.displayHTML;
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

            function saveRecord(){
                var hdnemployees=document.getElementById('hdnemployees').value;
                var installmentmode=document.getElementById('installmentmode').value;
                var delimiter="-";
                var chkarray="";
                var hdnprocess=document.getElementById('hdnprocess').value;
                var isnew=document.getElementById('isnew').value;
                var answer = confirm("Do You Want to Continue?");
                if(hdnprocess=="yes"){
                    if (answer){
                        getBlanket('continueDIV');
//                        var chkboxes=document.forms[0].noofepno;
                        var chkboxes=document.getElementsByName("noofepno");
                        for (i=0;i<chkboxes.length;i++){
                            if (chkboxes[i].checked==true){
                                chkarray=chkarray+chkboxes[i].value+"-";
                            }

                        }
//                        alert("chkarray=="+chkarray);
                        SalaryDeductionOthersAction.saveDeductionOthers(chkarray,installmentmode,delimiter,isnew,savePTDetails);
                    }
                }else{
                    alert("Please before save process the deductions");
                    return false;
                }
            }
            function savePTDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.reason);
                    document.getElementById('pfdisplayHTML').innerHTML="";
                    document.getElementById('pfdisplayHTML').style.display="none";
                    document.getElementById('finanicalyears').value="0";
                    document.getElementById('paidmonthtr').style.display="none";
                    document.getElementById('deductionmode').value="0";
                    document.getElementById('installmentmode').value="0";
                    document.getElementById('hdnprocess').value="";
                    document.getElementById('isnew').value="1";
                    document.getElementById('hdnemployees').value="";
                }
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

                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Professional Tax Deduction</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Financial Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="finanicalyears" id="finanicalyears" class="textfieldalign" onchange="displayMonths(this.value)" ></select>
                        </td>
                    </tr>
                </table>

                <div id="paidmonthtr" style="display:none;">
                    <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Deduction  Mode</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <select class="combobox" name="deductionmode" id="deductionmode">
                                    <option value="0">--Select--</option>
                                    <option value="3">First Half Yearly</option>
                                    <option value="4">Second Half Yearly </option>
                                </select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="30%" class="textalign">Installment  Mode</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <select class="combobox" name="installmentmode" id="installmentmode">
                                    <option value="0">--Select--</option>
                                    <option value="1">Single Payment</option>
                                    <option value="2">Double Payment </option>
                                </select>
                            </td>
                        </tr>
                        <tr class="darkrow">
                            <td width="100%" colspan="4" align="center">
                                <input type="button" CLASS="submitbu" name="processbutton" id="processbutton" value="Process" onclick="processRecord();">
                                <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save" onclick="saveRecord();">
                            </td>
                        </tr>
                    </table>
                </div>
                <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td><div id="pfdisplayHTML" align="center" style="overflow:auto;"> </div></td>
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
            SalaryDeductionOthersAction.loadFinanicalYears(fillFinanicalYears)

        }
        function fillFinanicalYears(map){
            dwr.util.removeAllOptions("finanicalyears");
            dwr.util.addOptions("finanicalyears",map.finanicalyearslist);
        }
        function displayMonths(year){
            if(year!=0){
                document.getElementById('paidmonthtr').style.display="block";
            }else{
                document.getElementById('paidmonthtr').style.display="none";
                document.getElementById('deductionmode').value="0";
                document.getElementById('installmentmode').value="0";
            }
        }
        function checkEmployees(epfno){
            var epfnos=document.getElementById('hdnemployees').value;
            epfnos=epfnos+epfno+"-";
            document.getElementById('hdnemployees').value=epfnos;
        }
    </script>
</html>

