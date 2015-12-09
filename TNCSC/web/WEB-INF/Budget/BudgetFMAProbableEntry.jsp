<%-- 
    Document   : BudgetFMAProbableEntry
    Created on : 18 Mar, 2014, 12:59:45 PM
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
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Budget FMA Probable Entry</title>
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
        <script src="dwr/interface/FileUploadAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var startyear=0;
            var endyear=0;
            $(function() {
                $('#endyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: false,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',
                    onClose: function(dateText, inst) {
                        startyear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(startyear, 3, 1));
                    }
                });
            });

            $(function() {
                $('#startyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: false,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',
                    onClose: function(dateText, inst) {
                        endyear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(endyear, 4, 1));
                    }
                });

                $('#budgetdetailsform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var budgetdetailsid = document.getElementById("budgetdetailsid").value;
                            var ledgerid = document.getElementById("ledgerid").value;
                            var ledgername=document.getElementById("ledgername").value;
                            var fmaamt = document.getElementById("fmaamt").value;
                            var hofmaamt=document.getElementById("hofmaamt").value;
                            
                            getBlanket('continueDIV');
                            BudgetAction.saveProbableFMADetails(budgetdetailsid,ledgerid,ledgername,fmaamt,hofmaamt,saveBudgetDetailsStatus);
                            $(this).dialog("close");


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
                    var region= 0;
                    var startyear=document.getElementById("startyear").value;
                    var endyear=document.getElementById("endyear").value;
                    BudgetAction.getBudgetForFMAProbable(region,startyear,endyear, displayBudgetDetailsAfterSave );
                }
            }
            function displayBudgetDetailsAfterSave(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert("Successfully Saved!");
                    document.getElementById('budgetdetails').style.display="block";
                    document.getElementById("budgetdetails").innerHTML=map.budgetdetails;
                    
                    oTable = $('#budgettable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"210px",
                        "bSort": true,
                        "bFilter": true,
                        "bPaginate": false
                        //                "sPaginationType": "full_numbers",
                        //                'iDisplayLength': 20,
                        //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
                    });
                
                }
            }

        </script>
        <script type="text/javascript">

        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
            .ui-datepicker-month{
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
                                    <td width="100%" align="center" class="headerdata">FMA Probable Entry</td>
                                </tr>
                            </table>
                            <table width="80%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">                                
                                <tr class="darkrow">
                                    <td width="15%" class="textalign">A/c Start Month & Year</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="15%" colspan="2" class="textfieldalign" >
                                        <input type="text" class="textbox" name="startyear" id="startyear" readonly>
                                    </td>
                                    <td width="15%" class="textalign">A/c End Month & Year</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="15%" colspan="2" class="textfieldalign" >
                                        <input type="text" class="textbox" name="endyear" id="endyear" readonly>
                                    </td>
                                    <td width="30%" align="center">
                                        <input type="button" class="submitbu" name="show" id="show" value="Show" onclick="displayBudgetForRevision()">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="budgetdetailsform" title="Budget Details" >
                <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Ledger Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign"><label id='ledgername'>dude</label></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Probable FMA <label id='current'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="fmaamt" name="fmaamt" onkeypress="isNumeric(this);"  size="20" />
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Approved FMA <label id='current1'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="hofmaamt" name="hofmaamt" onkeypress="isNumeric(this);"  readonly size="20" />
                        </td>
                    </tr>
                </table>
            </div>
<!--            <div id="budgetdetailsform" title="Budget Details" >
                <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Ledger Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign"><label id='ledgername'>dude</label></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign"><label id='first'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox"  id="firstactual" name="firstactual"  onblur="calculateAverage();"  onkeypress="isNumeric(this);"  readonly size="20" />
                        </td>
                    </tr>

                    <tr class="lightrow">
                        <td width="20%" class="textalign"><label id='second'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox"  id="secondactual" name="secondactual" onblur="calculateAverage();"  onkeypress="isNumeric(this);" readonly size="20" />
                        </td>
                    </tr>

                    <tr class="darkrow">
                        <td width="20%" class="textalign"><label id='third'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox"  id="thirdactual" name="thirdactual" onblur="calculateAverage();"  onkeypress="isNumeric(this);" readonly size="20" />
                        </td>
                    </tr>

                    <tr class="lightrow">
                        <td width="20%" class="textalign">Average</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="average" name="average"  size="20" readonly />
                        </td>
                    </tr>

                    <tr class="darkrow">
                        <td width="20%" class="textalign">Budget Estimate <label id='current'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="currentyearbudgetestimate" name="currentyearbudgetestimate" onkeypress="isNumeric(this);"  readonly size="20" />
                        </td>
                    </tr>

                    <tr class="lightrow">
                        <td width="20%" class="textalign">Actuals Upto September</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="actualfirsthalf" name="actualfirsthalf" onblur="calculateRevisedEstimate();" onkeypress="isNumeric(this);"   readonly size="20" />
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Probable October to March</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="probablesecondhalf" name="probablesecondhalf" onblur="calculateRevisedEstimate();" onkeypress="isNumeric(this);"  readonly size="20" />
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Revised Estimate <label id='current1'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="currentrevisedestimate" name="currentrevisedestimate"  size="20"  readonly onkeypress="isNumeric(this);"  />
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Budget Estimate <label id='next'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="nextyearbudgetestimate" name="nextyearbudgetestimate"  size="20"  readonly onkeypress="isNumeric(this);"  />
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Revised Estimate <label id='current2'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="currentrevisedestimateho" name="currentrevisedestimateho"  size="20" readonly />
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Budget Estimate<label id='next1'>dude</label></td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign">
                            <input type="text" class="amounttextbox" id="nextyearbudgetestimateho" name="nextyearbudgetestimateho"  size="20" readonly />
                        </td>
                    </tr>
                </table>
            </div>-->
            <div id="budgetdetails" style="display:none;overflow:auto;"></div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="budgetdetailsid" id="budgetdetailsid"  value="0" >
            <input type="hidden" name="ledgerid" id="ledgerid"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">

        </form>
    </body>
    <script type="text/javascript">
        onloadData();
        function onloadData(){
            FileUploadAction.loadRegionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
        }
        function displayBudgetForRevision(){
            var region= 0;
            var startyear=document.getElementById("startyear").value;
            var endyear=document.getElementById("endyear").value;
            var diffyear;
            diffyear=eval(endyear)-eval(startyear);

            if(startyear==""){
                alert("Please Select the Start Year");
                document.getElementById('startyear').focus();
                return false;
            } else if(endyear==""){
                alert("Please Select the End Year");
                document.getElementById('endyear').focus();
                return false;
            }else if(diffyear!=1){
                alert("Please Select the Correct Financial Year");
                return false;
            }else{
                getBlanket('continueDIV');
                BudgetAction.getBudgetForFMAProbable(region,startyear,endyear, displayBudgetDetails );
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
                    "sScrollY":"210px",
                    "bSort": true,
                    "bFilter": true,
                    "bPaginate": false
                    //                "sPaginationType": "full_numbers",
                    //                'iDisplayLength': 20,
                    //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
                });
            }
        }
        function setBudgetDetails(budgetdetailsid,ledgerid,ledgername,fmaamt,hofmaamt){
            document.getElementById("budgetdetailsid").value=budgetdetailsid;
            document.getElementById("ledgerid").value=ledgerid;
            document.getElementById("ledgername").innerHTML=ledgername;
            document.getElementById("fmaamt").value=fmaamt;
            document.getElementById("hofmaamt").value=hofmaamt;
            var c=eval(document.getElementById("startyear").value);

            document.getElementById("current").innerHTML=c+"-"+(c+1);
            document.getElementById("current1").innerHTML=c+"-"+(c+1);

            $('#budgetdetailsform').dialog('open');
            return false;

        }
//        function setBudgetDetails(budgetdetailsid,ledgerid,ledgername,fistactual,secondactual,thirdactual,average,currentbudgetestimate,actualfirsthalf,probablesecondhalf,currentrevisedestimate,nextyearbudgetestimate,currentrevisedestimateho,nextyearbudgetestimateho){
//            document.getElementById("budgetdetailsid").value=budgetdetailsid;
//            document.getElementById("ledgerid").value=ledgerid;
//            document.getElementById("ledgername").innerHTML=ledgername;
//            document.getElementById("firstactual").value=fistactual;
//            document.getElementById("secondactual").value=secondactual;
//            document.getElementById("thirdactual").value=thirdactual;
//            document.getElementById("average").value=average;
//            document.getElementById("currentyearbudgetestimate").value=currentbudgetestimate;
//            document.getElementById("actualfirsthalf").value=actualfirsthalf;
//            document.getElementById("probablesecondhalf").value=probablesecondhalf;
//            document.getElementById("currentrevisedestimate").value=currentrevisedestimate;
//            document.getElementById("nextyearbudgetestimate").value=nextyearbudgetestimate;
//            document.getElementById("currentrevisedestimateho").value=currentrevisedestimateho;
//            document.getElementById("nextyearbudgetestimateho").value=nextyearbudgetestimateho;
//            var x=document.getElementById("startyear").value-3;
//            var y=document.getElementById("startyear").value-2;
//            var z=document.getElementById("startyear").value-1;
//            var c=eval(document.getElementById("startyear").value);
//
//            document.getElementById("first").innerHTML = x+"-"+(x+1);
//            document.getElementById("second").innerHTML = y+"-"+(y+1);
//            document.getElementById("third").innerHTML=z+"-"+(z+1);
//
//            document.getElementById("current").innerHTML=c+"-"+(c+1);
//            document.getElementById("current1").innerHTML=c+"-"+(c+1);
//            document.getElementById("current2").innerHTML=c+"-"+(c+1);
//            document.getElementById("next").innerHTML=(c+1)+"-"+(c+2);
//            document.getElementById("next1").innerHTML=(c+1)+"-"+(c+2);
//
//
//            $('#budgetdetailsform').dialog('open');
//            return false;
//
//        }

        function calculateAverage(){
            var fistactual=document.getElementById("firstactual").value;
            var secondactual=document.getElementById("secondactual").value;
            var thirdactual=document.getElementById("thirdactual").value;
            var averageAmount=0;

            averageAmount=(eval(fistactual)+eval(secondactual)+eval(thirdactual))/3;
            var newnumber = new Number(averageAmount+'').toFixed(parseInt(2));


            document.getElementById("average").value=parseFloat(newnumber);
        }
        function calculateRevisedEstimate(){
            var actualfirsthalf=document.getElementById("actualfirsthalf").value;
            var probablesecondhalf=document.getElementById("probablesecondhalf").value;
            var averageEstimateAmount=0;

            averageEstimateAmount=(eval(actualfirsthalf)+eval(probablesecondhalf));

            var newnumber = new Number(averageEstimateAmount+'').toFixed(parseInt(2));
            document.getElementById("currentrevisedestimate").value=parseFloat(newnumber);
        }
    </script>
</html>


