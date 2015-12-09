<%-- 
    Document   : EmployeeFundSubPage
    Created on : 24 Sep, 2012, 3:16:49 PM
    Author     : user
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
        <title>Employee Fund Sub Details</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/EmployeeFundSubAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">     
              
            var amonth;
            var ayear;
            var smonth;
            var syear;
            var mmonth;
            var myear;
                
            $(function() {
              
                
                $('#fundsubmonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        amonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        ayear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(ayear,amonth, 1));
                    }
                });
                
                $('#salarymonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear, smonth, 1));
                    }
                });
                
                $('#salarymonthm').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        mmonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        myear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(myear, mmonth, 1));
                    }
                });

                $('#modifyepfform').dialog({
                    autoOpen: false,
                    width: 400,
                    height: 400,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            var epfnumber=document.getElementById('epfnumber').value;
                            var salary=document.getElementById('salary').value;
                            var epfwhole=document.getElementById('epfwhole').value;
                            var fbf=document.getElementById('fbf').value;
                            var rl=document.getElementById('rl').value;
                            var vpf=document.getElementById('vpf').value;
                            var ecpf=document.getElementById('ecpf').value;
                            var ecfb=document.getElementById('ecfb').value;
                            var nrl=document.getElementById('nrl').value;
                            var epfid=document.getElementById('epfid').value;                               
                            if(epfid==""){
                                alert("Please Click the Radio Button")                                
                                return false;
                            }else {
                                getBlanket('continueDIV');
                                EmployeeFundSubAction.modifyEmployeeEPF(epfid,epfnumber,salary,epfwhole,fbf,rl,vpf,ecpf,ecfb,nrl,mmonth+1,myear,modifyEmployeeEPFStatus);
                                $(this).dialog("close");
                            }
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                
                $("#savebutton").click(function(){
                    //                    var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                    //                    var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                    //                    month=eval(month)+1;
                    
                    var fundsubmonth=document.getElementById('fundsubmonth').value;
                    var salarymonth=document.getElementById('salarymonth').value;                    
                    var employeetype=document.getElementById('employeetype').value;
                    var epfno=document.getElementById('epfno').value;
                    var employeename=document.getElementById('employeename').value;
                    var deductionamt=document.getElementById('deductionamt').value;
                    var answer = confirm("Do You Want to Continue?");
                    if(fundsubmonth==""){
                        alert("Please Select the Month and Year");
                        document.getElementById('fundsubmonth').focus();
                        return false;
                    }if(salarymonth==""){
                        alert("Please Select the Month and Year");
                        document.getElementById('salarymonth').focus();
                        return false;
                    }else if(employeetype=="0"){
                        alert("Please Select the Employee Type");
                        document.getElementById('employeetype').focus();
                        return false;
                    }else if(epfno==""){
                        alert("Please enter the EPF NO");
                        document.getElementById('epfno').focus();
                        return false;
                    }else if(deductionamt==""){
                        alert("Please enter the Salary Amount");
                        document.getElementById('deductionamt').focus();
                        return false;
                    }else{
                        if (answer){
                            getBlanket('continueDIV');

                            EmployeeFundSubAction.saveEmployeeEPF(amonth+1,ayear,employeetype,epfno,employeename,deductionamt,smonth+1,syear,saveEmployeeEPFStatus);
                        }
                    }
                });

            });

            function modifyEmployeeEPFStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    document.getElementById('epfnumber').value="";
                    document.getElementById('salary').value="";
                    document.getElementById('epfwhole').value="";
                    document.getElementById('fbf').value="";
                    document.getElementById('rl').value="";
                    document.getElementById('vpf').value="";
                    document.getElementById('ecpf').value="";
                    document.getElementById('ecfb').value="";
                    document.getElementById('nrl').value="";
                    document.getElementById('epfid').value="";
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.message);
                    document.getElementById('epfnumber').value="";
                    document.getElementById('salary').value="";
                    document.getElementById('epfwhole').value="";
                    document.getElementById('fbf').value="";
                    document.getElementById('rl').value="";
                    document.getElementById('vpf').value="";
                    document.getElementById('ecpf').value="";
                    document.getElementById('ecfb').value="";
                    document.getElementById('nrl').value="";
                    document.getElementById('epfid').value="";
                    document.getElementById("recordedDetails").innerHTML=map.recordedDetails;                  

                }

            }
            function saveEmployeeEPFStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');    
                    document.getElementById('epfno').value="";
                    document.getElementById('employeename').value="";
                    document.getElementById('deductionamt').value="";
                    document.getElementById('epfid').value="";
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.message);
                    document.getElementById('epfno').value="";
                    document.getElementById('employeename').value="";
                    document.getElementById('deductionamt').value="";
                    document.getElementById('epfid').value="";                    
                    document.getElementById("recordedDetails").innerHTML=map.recordedDetails;
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
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Employee Fund Sub</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Employee Details</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Current Month And year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="fundsubmonth" id="fundsubmonth" readonly>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Employee Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="employeetype" id="employeetype" onchange="loadDatasOnEmployeeType(this.value)" >
                                <option value="0">--Select--</option>
                                <option value="R">Regular</option>
                                <option value="S">Seasonal</option>
                                <option value="L">Load Man</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Employee Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="employeename" id="employeename" readonly >
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Salary Amount</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="deductionamt" id="deductionamt" onblur="checkFloat(this.id,'Salary Amount');" onkeypress="isNumeric(this);">

                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Salary Month And year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="salarymonth" id="salarymonth" readonly>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
                            <!--                            <input type="button" CLASS="submitbu" name="processRecord" id="processRecord" value="Process">-->
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="recordedDetails" style="height:200px;overflow:auto;"> </div>
            <div id="modifyepfform" title="EPF Form" >
                <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr class="lightrow">
                        <td width="20%" class="textalign">epfno</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="epfnumber" name="epfnumber" readonly/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Salary</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="salary" name="salary"  onblur="checkFloat(this.id,'EPF Whole');" size="20" /></td>
                    </tr>

                    <tr class="lightrow">
                        <td width="20%" class="textalign">EPF Whole</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="epfwhole" name="epfwhole" onblur="checkFloat(this.id,'EPF Whole');" size="20"/></td>
                    </tr>

                    <tr class="darkrow">
                        <td width="20%" class="textalign">FBF</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="fbf" name="fbf"  onblur="checkFloat(this.id,'FBF');" size="20" /></td>
                    </tr>

                    <tr class="lightrow">
                        <td width="20%" class="textalign">RL</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="rl" name="rl" onblur="checkFloat(this.id,'RL');" size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">VPF</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="vpf" name="vpf"  onblur="checkFloat(this.id,'VPF');" size="20" /></td>
                    </tr>

                    <tr class="lightrow">
                        <td width="20%" class="textalign">ECPF</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="ecpf" name="ecpf" onblur="checkFloat(this.id,'ECPF');" size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">ECFB</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="ecfb" name="ecfb"  onblur="checkFloat(this.id,'ECFB');" size="20" /></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">NRL</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign"><input type="text" id="nrl" name="nrl" onblur="checkFloat(this.id,'NRL');" size="20"/></td>                        
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Salary Month And year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="salarymonthm" id="salarymonthm" readonly>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="epfid" id="epfid">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
          
        }
        function loadDatasOnEmployeeType(employeetype){                      
            if(employeetype=="0"){
                employeetype="ALL";
            }            
            getBlanket('continueDIV');
            EmployeeFundSubAction.getRecordedDetails(employeetype,amonth+1,ayear,displayRecordDetails);
        }
        function displayRecordDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                getBlanket('continueDIV');
                document.getElementById("recordedDetails").innerHTML="";
                return false;
            } else {
                getBlanket('continueDIV');
                document.getElementById("recordedDetails").innerHTML=map.recordedDetails;
            }
            
        }
        function loadEmployeeDetails(){
            var employeetype=document.getElementById('employeetype').value;
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                if(employeetype=="0"){
                    alert("Please Select Employee Type?");
                    document.getElementById('epfno').value="";
                    document.getElementById("employeename").value="";
                    document.getElementById('employeetype').focus();
                    return false;
                }else{
                    EmployeeFundSubAction.getEmployeeDetails(epfno,employeetype,fillEmployeeDetails);
                }
            }else{
                document.getElementById('epfno').value="";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
            }

        }
        function fillEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert("Please Enter the Valid Employee EPF No");
                document.getElementById("epfno").value="";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
                // return false;
            } else {
                document.getElementById("employeename").value=map.employeename;
            }
        }

        function setEPFId(epfid){
            //            var epfno=document.forms[0].epfno.value;
            EmployeeFundSubAction.getDetailsForModification(epfid,fillDetailsForModification);
        }
        function fillDetailsForModification(map){
            document.getElementById('epfnumber').value=map.epfno;
            document.getElementById('salary').value=map.salary;
            document.getElementById('epfwhole').value=map.epfwhole;
            document.getElementById('fbf').value=map.fbf;
            document.getElementById('rl').value=map.rl;
            document.getElementById('vpf').value=map.vpf;
            document.getElementById('ecpf').value=map.ecpf;
            document.getElementById('ecfb').value=map.ecfb;
            document.getElementById('nrl').value=map.nrl;
            document.getElementById('epfid').value=map.epfid;          
            myear=map.myear;
            mmonth=map.mmonth;
            $('#salarymonthm').datepicker('setDate', new Date(map.myear, map.mmonth, 1));
            $('#modifyepfform').dialog('open');
            return false;
        }
    </script>
</html>


