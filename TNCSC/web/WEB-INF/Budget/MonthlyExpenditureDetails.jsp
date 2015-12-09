<%-- 
    Document   : MonthlyExpenditureDetails
    Created on : 14 Mar, 2014, 12:59:45 PM
    Author     : Karthikeyan S
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
    String isMonthlyEntryAdmin = "";
    if ((request.getSession().getAttribute("isMonthlyEntryAdmin") != null)) {
        isMonthlyEntryAdmin = (String) request.getSession().getAttribute("isMonthlyEntryAdmin");   
    }
    request.getSession().removeAttribute("isMonthlyEntryAdmin");
    String regionlist = "";
    if (request.getSession().getAttribute("regionlist") != null) {
        regionlist = (String) request.getSession().getAttribute("regionlist");
    }
    request.getSession().removeAttribute("regionlist");

    /*
     * String validButtonFlag = "";
     * if((request.getAttribute("validButtonFlag")!=null)) validButtonFlag =
     * (String) request.getAttribute("validButtonFlag");
     * request.removeAttribute("validButtonFlag");
     */


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Monthly Expenditure Entry</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>        
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/BudgetAction.js"></script>        
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var amonth;
            var ayear;
            var smonth;
            var syear;
            $(function() {                
               
                $('#monthandyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        amonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        ayear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(ayear,amonth, 1));
                    }
                });                
            });

            $(function() {
                $('#budgetdetailsform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var expendituredetailsid = document.getElementById("expendituredetailsid").value;
                            var ledgerid = document.getElementById("ledgerid").value;                            
                            var monthexpenditure = document.getElementById("monthexpenditure").value;
                            var region="0";
                            if("Yes"==document.getElementById("isMonthlyEntryAdmin").value){
                                region = document.getElementById('region').value;
                            }
                            var fmaamount=0;       
//                            var fmaamount=document.getElementById("fmaamount").value;       
                            if(monthexpenditure==""){
                                alert("Please Select the During the Month Expenditure");
                                document.getElementById('monthexpenditure').focus();
                                return false;
                            }else{
                                getBlanket('continueDIV');                                
                                BudgetAction.saveBudgetExpenditureDetails(expendituredetailsid,ledgerid,monthexpenditure,fmaamount,region, saveBudgetDetailsStatus);
                                $(this).dialog("close");
                            }
                                
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });
            function saveBudgetDetailsStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    var region="0";
                    if("Yes"==document.getElementById("isMonthlyEntryAdmin").value){
                        region = document.getElementById('region').value;
                    }
                    var budgetperiod = document.getElementById('budgetperiod').value;           
                    BudgetAction.getMonthExpenditure(amonth+1,ayear,budgetperiod,region, displayBudgetDetails );                    
                }
            }
        </script>
        <script type="text/javascript">

        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>
    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center" >
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post" enctype="multipart/form-data">

            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" class="headerdata">Control of Expenditure Entry</td>
                                </tr>
                            </table>
                            <table width="80%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr class="darkrow">
                                    <td width="10%" class="textalign">Expenditure Month & Year </td>
                                    <td width="3%" class="mandatory">*</td>
                                    <td width="15%" colspan="2" class="textfieldalign" >
                                        <input type="text" class="textbox" name="monthandyear" id="monthandyear" readonly>
                                    </td>
                                    <td width="10%" class="textalign">Accounting Period</td>
                                    <td width="3%" class="mandatory">*</td>
                                    <td width="15%" colspan="2" class="textfieldalign" >
                                        <select class="combobox" name="budgetperiod" id="budgetperiod" class="textfieldalign" ></select>
                                        <!--                                        <input type="text" class="textbox" name="perioddisp" id="perioddisp" >-->
                                    </td>                                    
                                    <td width="1%" class="textalign"></td>
                                    <td width="1%" class="mandatory"></td>
                                    <td id="isadminrole" style="display: none;" class="textfieldalign" width="20%" nowrap>
                                         &nbsp;&nbsp;&nbsp; Region &nbsp;&nbsp;&nbsp;
                                        <%=regionlist %>
                                    </td>
                                    <td width="15%" align="center">
                                        <input type="button" class="submitbu" name="show" id="show" value="Show" onclick="displayControlofExpenditure()">
                                    </td>
                                </tr>  
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="budgetdetailsform" title="Control of Expenditure" >
                <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Expenditure Month & Year</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign"><label id='expendituremonthandyear'>dude</label></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Accounting Period</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign"><label id='accperiod'>dude</label></td>
                    </tr>                    
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Ledger Name</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign"><label id='ledgername'>dude</label></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Budget Estimated <label id='accperiod1'>dude</label></td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign"><label id='budestimateamt'>dude</label></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">During the Month Expenditure</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign"><input type="text" class="amounttextbox"  id="monthexpenditure" name="monthexpenditure"  onblur="calculateBalanceAMT()"  onkeypress="isNumeric(this);" size="20" /></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Upto the Month Expenditure</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign"><input type="text" class="amounttextbox"  id="uptothemonthexpenditure" name="uptothemonthexpenditure" readonly size="20" /></td>
                    </tr>

                    <tr class="darkrow">
                        <td width="20%" class="textalign">Balance</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign"><input type="text" class="amounttextbox"  id="balanceamt" name="balanceamt" readonly size="20" /></td>
                    </tr>
<!--                    <tr class="lightrow">
                        <td width="20%" class="textalign">FMA</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign"><input type="text" class="amounttextbox" id="fmaamount" name="fmaamount"  onkeypress="isNumeric(this);" size="20" /></td>
                    </tr> -->

                </table>
            </div>
            <div id="budgetdetails" style="display:none;overflow:auto;"></div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">                        
            <input type="hidden" name="expendituredetailsid" id="expendituredetailsid"  value="0" >
            <input type="hidden" name="estimatedamt" id="estimatedamt"  value="0" >
            <input type="hidden" name="expenditureamt" id="expenditureamt"  value="0" >
            <input type="hidden" name="uptoexpenditureamt" id="uptoexpenditureamt"  value="0" >
            <input type="hidden" name="ledgerid" id="ledgerid"  value="0" >            
            <input type="hidden" name="isMonthlyEntryAdmin" id="isMonthlyEntryAdmin"  value="<%=isMonthlyEntryAdmin %>" >            
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">

        </form>
    </body>   

    <script type="text/javascript">
        onloadData();
        function onloadData(){
            if("Yes"==document.getElementById("isMonthlyEntryAdmin").value){
                document.getElementById("isadminrole").style.display = "block";
            }else{                
                document.getElementById("isadminrole").style.display = "none";
            }
            BudgetAction.loadBudgetYearDetails(fillBudgetYearCombo);            
        }
        function fillBudgetYearCombo(map){
            dwr.util.removeAllOptions("budgetperiod");
            dwr.util.addOptions("budgetperiod",map.budgetlist);
        }
       
        function displayControlofExpenditure(){
            var region= 0;
            var monthandyear=document.getElementById("monthandyear").value;
            var budgetperiod = document.getElementById('budgetperiod').value;           

            if(monthandyear==""){
                alert("Please Select the Month & Year");
                document.getElementById('monthandyear').focus();
                return false;
            }else if(budgetperiod=="0"){
                alert("Please Select the Budget Period");
                document.getElementById('budgetperiod').focus();
                return false;
            }else{
                getBlanket('continueDIV');
                var region="0";
                if("Yes"==document.getElementById("isMonthlyEntryAdmin").value){
                    region = document.getElementById('region').value;
                }
                BudgetAction.getMonthExpenditure(amonth+1,ayear,budgetperiod,region, displayBudgetDetails );
            }
        }
        function displayBudgetDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                getBlanket('continueDIV');
                return false;
            } else {
                getBlanket('continueDIV');
                document.getElementById('budgetdetails').style.display="block";
                document.getElementById("budgetdetails").innerHTML=map.budgetdetails;

                oTable = $('#budgettable').dataTable({
                    "bJQueryUI": true,
                    "sScrollY":"330px",
                    "bSort": true,
                    "bFilter": true,
                    "bPaginate": false
                });
            }
        }
        function setExpenditureDetails(expendituredetailsid,ledgerid,ledgername,budestimateamt,accperiod,currentmntamt,fmaamt,uptothemonthexpenditure,balanceamt){
            document.getElementById("expendituredetailsid").value=expendituredetailsid;            
            document.getElementById("ledgerid").value=ledgerid;            
            document.getElementById("ledgername").innerHTML=ledgername;
            document.getElementById("budestimateamt").innerHTML=budestimateamt;
            document.getElementById("expendituremonthandyear").innerHTML=document.getElementById("monthandyear").value;
            document.getElementById("accperiod").innerHTML=accperiod;
            document.getElementById("accperiod1").innerHTML=accperiod;
            document.getElementById("monthexpenditure").value=currentmntamt;
//            document.getElementById("fmaamount").value=fmaamt;
            document.getElementById("uptothemonthexpenditure").value=uptothemonthexpenditure;
            document.getElementById("uptoexpenditureamt").value=uptothemonthexpenditure;
            document.getElementById("expenditureamt").value=currentmntamt;
            document.getElementById("balanceamt").value=balanceamt;
            document.getElementById("estimatedamt").value=budestimateamt;

            $('#budgetdetailsform').dialog('open');
            return false;

        }

        function calculateBalanceAMT(){
            var monthexpenditure=document.getElementById("monthexpenditure").value;
            var expenditureamt=document.getElementById("expenditureamt").value;
            var budestimateamt=document.getElementById("estimatedamt").value;
            var uptothemonthexpenditure=document.getElementById("uptoexpenditureamt").value;
            var balanceamt=0;

            uptothemonthexpenditure=  (parseFloat(uptothemonthexpenditure)-parseFloat(expenditureamt))+parseFloat(monthexpenditure);
            balanceamt=parseFloat(budestimateamt)-parseFloat(uptothemonthexpenditure);    
            document.getElementById("uptothemonthexpenditure").value=parseFloat(uptothemonthexpenditure);
            document.getElementById("balanceamt").value=parseFloat(balanceamt);
        }       
    </script>
</html>

