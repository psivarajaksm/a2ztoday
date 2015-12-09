<%-- 
    Document   : empMaster
    Created on : Jul 17, 2012, 2:34:59 PM
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
        <title>Government Employee Master Creation</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="<%=staticPath%>scripts/dateValidations.js"></script>
        <script src="dwr/interface/EmployeeMasterAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <style type="text/css">
            .ui-datepicker
            {
                width: 12em;
                height: 12em;
            }

        </style>
        <script type="text/javascript">

            $(function() {
//                $('#dateofbirth').datepicker({
//                    changeMonth: true,
//                    changeYear: true,
//                    yearRange: '1900:2050',
//                    dateFormat: "dd/mm/yy" }).val();
//                $('#dateofappoinment').datepicker({
//                    changeMonth: true,
//                    changeYear: true,
//                    yearRange: '1900:2050',
//                    dateFormat: "dd/mm/yy" }).val();
//                $('#dateofprobation').datepicker({
//                    changeMonth: true,
//                    changeYear: true,
//                    yearRange: '1900:2050',
//                    dateFormat: "dd/mm/yy" }).val();
//                $('#dateofconfirmation').datepicker({ dateFormat: "dd/mm/yy" }).val();
           
                $("#savebutton").click(function(){                 
                    //                        getBlanket('continueDIV');
                    var funtype=document.getElementById('funtype').value;
                    var epfno=document.getElementById('epfno').value;
                    var fpfno=document.getElementById('fpfno').value;
                    var employeename=document.getElementById('employeename').value;
                    var fathername=document.getElementById('fathername').value;
                    var gender=document.getElementById('gender').value;
                    var dateofbirth=document.getElementById('dateofbirth').value;
                    var region=document.getElementById('region').value;
                    var section=document.getElementById('section').value;
                    var dateofappoinment=document.getElementById('dateofappoinment').value;
                    var dateofprobation=document.getElementById('dateofprobation').value;
                    var empcategory=document.getElementById('empcategory').value;
                    var designation=document.getElementById('designation').value;
                    var community=document.getElementById('community').value;
                    var pancardno=document.getElementById('pancardno').value;
                    var paymentmode=document.getElementById('paymentmode').value;
                    var bankcode=document.getElementById('bankcode').value;
                    var banksbaccount=document.getElementById('banksbaccount').value;
                    var dateofconfirmation="";
                    if(funtype=="0"){
                        alert("Please Select the Function Type");
                        document.getElementById('funtype').focus();
                        return false;
                    }else  if(epfno==""){
                        alert("Please enter the GPF Number");
                        document.getElementById('epfno').focus();
                        return false;
                    }else  if(epfno.length<=5){
                        alert("Please entered GPF Number length is greater the 5");
                        document.getElementById('epfno').focus();
                        return false;
                    }
                    //                    else if(fpfno==""){
                    //                        alert("Please enter the FBF Number");
                    //                        document.getElementById('fpfno').focus();
                    //                        return false;
                    //                    }
                    else if(employeename==""){
                        alert("Please enter the Employee Name");
                        document.getElementById('employeename').focus();
                        return false;
                    }else if(fathername==""){
                        alert("Please enter the Father Name");
                        document.getElementById('fathername').focus();
                        return false;
                    }else if(gender=="0"){
                        alert("Please select the Gender of the Employee");
                        document.getElementById('gender').focus();
                        return false;
                    }else if(dateofbirth==""){
                        alert("Please select the Date of Birth");
                        document.getElementById('dateofbirth').focus();
                        return false;
                    }else if(region=="0"){
                        alert("Please Select the Employee Region Name");
                        document.getElementById('region').focus();
                        return false;
                    }else if(section=="0"){
                        alert("Please Select the Employee Section Name");
                        document.getElementById('section').focus();
                        return false;
                    }else if(designation=="0"){
                        alert("Please Select the Employee Designation Name");
                        document.getElementById('designation').focus();
                        return false;
                    }
                    else if(empcategory=="0"){
                        alert("Please select the Employee Category");
                        document.getElementById('empcategory').focus();
                        return false;
                    }
                    //                    else if(pancardno==""){
                    //                        alert("Please enter the PanCard Number");
                    //                        document.getElementById('pancardno').focus();
                    //                        return false;
                    //                    }
                    else if(paymentmode=="0"){
                        alert("Please enter the Payment Mode");
                        document.getElementById('paymentmode').focus();
                        return false;
                    }
                    //                    else if(bankcode=="0"){
                    //                        alert("Please Select the Bank");
                    //                        document.getElementById('bankcode').focus();
                    //                        return false;
                    //                    }
                    else if(banksbaccount==""){
                        alert("Please enter the Bank Account Number");
                        document.getElementById('banksbaccount').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            EmployeeMasterAction.saveEmployeeMaster(epfno,fpfno,employeename,fathername,
                            gender,dateofbirth,region,section,dateofappoinment,dateofprobation,dateofconfirmation,designation,
                            community,pancardno,paymentmode,bankcode,banksbaccount,empcategory,"","","",
                            masterSaveDetails);
                        }
                    
                    }
                });
            });   
            
            
            function masterSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.forms[0].action="EmployeeMasterAction.htm";                
                    document.forms[0].method.value="employeeMasterPage";
                    document.forms[0].submit();
                    //                        document.getElementById('epfno').value="";
                    //                        document.getElementById('fpfno').value="";
                    //                        document.getElementById('employeename').value="";
                    //                        document.getElementById('fathername').value="";
                    //                        document.getElementById('gender').value="";
                    //                        document.getElementById('dateofbirth').value="";
                    //                        document.getElementById('region').value="";
                    //                        document.getElementById('section').value="";
                    //                        document.getElementById('dateofappoinment').value="";
                    //                        document.getElementById('dateofprobation').value="";
                    ////                        document.getElementById('dateofconfirmation').value="";
                    //                        document.getElementById('designation').value="";
                    //                        document.getElementById('community').value="";
                    //                        document.getElementById('pancardno').value="";
                    //                        document.getElementById('paymentmode').value="";
                    //                        document.getElementById('bankcode').value="";
                    //                        document.getElementById('banksbaccount').value="";
                }

            }
            function clearall(){
                
                document.getElementById('epfno').value="";
                document.getElementById('banksbaccount').value="";
                document.getElementById('fpfno').value="";
                document.getElementById('employeename').value="";
                document.getElementById('fathername').value="";
                document.getElementById('gender').value="0";
                document.getElementById('dateofbirth').value="";
//                document.getElementById('region').value="0";
                document.getElementById('section').value="0";
                document.getElementById('dateofappoinment').value="";
                document.getElementById('dateofprobation').value="";
//                document.getElementById('dateofconfirmation').value="";
                document.getElementById('designation').value="0";
                document.getElementById('community').value="";
                document.getElementById('pancardno').value="";
                document.getElementById('paymentmode').value="0";
                document.getElementById('bankcode').value="";
                document.getElementById('empcategory').value="0";
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
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Government Employee Master</td>
                    </tr>
                    <br>
                    <td width="100%" class="mainheader">Government Employee Master Creation</td>
                </table>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="29%" class="textalign">Function Type</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="funtype" id="funtype" onchange="clearall()">
                                <option value="0">--Select--</option>
                                <option value="add">Addtion</option>
                                <option value="modify">Modification</option>                                
                            </select>
                        </td> 
                        <td width="29%" class="textalign">Emp. Category</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="empcategory" id="empcategory">
                                <option value="0">--Select--</option>
                                <option value="G">Government</option>
                            </select>
                            <!--<input type="text" class="textbox" name="gender" id="gender">-->
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="29%" class="textalign">GPF Number</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowedFOREPFNO(this)" onblur="checkEPFno();">
                            <!--                            <div id="addid"><input type="text" class="textbox" name="epfno" id="epfno" onblur="checkEPFno();"></div>-->
                            <!--<div id="modifyid" style="display: none"><input type="text" class="textbox" name="epfno" id="epfno" onblur="getEmployeeDetails(this.value);"></div>-->
                        </td>
                        <td width="29%" class="textalign">FPF Number</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="fpfno" id="fpfno">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="29%" class="textalign">Employee Name</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="employeename" id="employeename">
                        </td>
                        <td width="29%" class="textalign">Father Name</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="fathername" id="fathername">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="29%" class="textalign">Gender</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="gender" id="gender">
                                <option value="0">--Select--</option>
                                <option value="M">Male</option>
                                <option value="F">Female</option>                                
                            </select>
                            <!--<input type="text" class="textbox" name="gender" id="gender">-->
                        </td>
                        <td width="29%" class="textalign">Date of Birth</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" nowrap>
                            <input type="text" class="datetextbox" name="dateofbirth" id="dateofbirth" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this); minorValidateDate(this);">&nbsp;[dd/mm/yyyy]
<!--                            <input type="text" class="textbox" name="dateofbirth" id="dateofbirth">-->
                        </td>
                    </tr>                   
                    <tr class="darkrow">
                        <td width="29%" class="textalign">Region</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="region" id="region" class="textfieldalign" ></select>
                        </td>
                        <td width="29%" class="textalign">Section</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >                            
                            <select class="combobox" name="section" id="section"></select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="29%" class="textalign">Date of Appoinment</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" nowrap>
                            <input type="text" class="datetextbox" name="dateofappoinment" id="dateofappoinment" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this);">&nbsp;[dd/mm/yyyy]
<!--                            <input type="text" class="textbox" name="dateofappoinment" id="dateofappoinment">-->
                        </td>
                        <td width="29%" class="textalign">Date of Probation</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" nowrap>
                            <input type="text" class="datetextbox" name="dateofprobation" id="dateofprobation" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this); ">&nbsp;[dd/mm/yyyy]
<!--                            <input type="text" class="textbox" name="dateofprobation" id="dateofprobation">-->
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <!--                        <td width="29%" class="textalign">Date of Confirmation</td>
                                                <td width="1%" class="mandatory">*</td>
                                                <td width="20%" colspan="4" class="textfieldalign" >
                                                    <input type="text" class="textbox" name="dateofconfirmation" id="dateofconfirmation">
                                                </td>-->
                        <td width="29%" class="textalign">Designation</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="designation" id="designation" class="textfieldalign" ></select>
                            <!--                            <input type="text" class="textbox" name="designation" id="designation">-->
                        </td>
                        <td width="29%" class="textalign">Community</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="community" id="community">
                        </td>
                    </tr>
                    <tr class="lightrow">                        
                        <td width="29%" class="textalign">Pan CardNumber</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="pancardno" id="pancardno">
                        </td>
                        <td width="29%" class="textalign">Bank Account Number</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="banksbaccount" id="banksbaccount">
                        </td>
                    </tr>

                    <tr class="darkrow">
                        <td width="29%" class="textalign">Payment Mode</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="paymentmode" id="paymentmode">
                                <option value="0">--Select--</option>
                                <option value="B">Bank</option>                                
                                <option value="C">Cheque</option>
                            </select>
                            <!--<input type="text" class="textbox" name="paymentmode" id="paymentmode">-->
                        </td>
                        <td width="29%" class="textalign">Bank Name</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="bankcode" id="bankcode">
                        </td>
                    </tr>
                    <!--                    <tr class="lightrow">
                                            <td width="29%" class="textalign">Bank Account Number</td>
                                            <td width="1%" class="mandatory">*</td>
                                            <td width="20%" colspan="4" class="textfieldalign" >
                                                <input type="text" class="textbox" name="banksbaccount" id="banksbaccount">
                                            </td>
                                            <td width="29%" class="textalign">Bank Account Number</td>
                                            <td width="1%" class="mandatory">*</td>
                                            <td width="20%" colspan="4" class="textfieldalign" >
                                                <input type="text" class="textbox" name="banksbaccount" id="banksbaccount">
                                            </td>
                                        </tr>-->
                    <tr class="lightrow">
                        <td width="100%" colspan="12" align="center">
                            <!--                            <button id="savebutton">Save</button>-->
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>


            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            EmployeeMasterAction.loadRegionDetailsforDeputation(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
            dwr.util.removeAllOptions("section");
            dwr.util.addOptions("section",map.sectionlist);
            dwr.util.removeAllOptions("designation");
            dwr.util.addOptions("designation",map.designationlist);
            document.getElementById('region').value=map.currentRegion;
            document.getElementById('region').disabled = true;

        }
        function checkEPFno(){
            var funtype=document.getElementById('funtype').value;
            var epfno=document.getElementById('epfno').value;
            if(funtype=="add"){
                EmployeeMasterAction.getEmployeeGPFNo(epfno,validateEPFNo);
            }else if(funtype=="modify"){
                EmployeeMasterAction.getGovernmentEmployeeDetails(epfno,setEmployeeDetails);
            }else{
                alert("Please Select Funtion Type?");   
                document.getElementById('funtype').focus();
                return false;
            }
                
                
        }
        function validateEPFNo(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById('epfno').value="";
                document.getElementById('funtype').focus();
                document.getElementById('fpfno').value="";
                document.getElementById('employeename').value="";
                document.getElementById('fathername').value="";
                document.getElementById('gender').value="0";
                document.getElementById('dateofbirth').value="";
//                document.getElementById('region').value="0";
                document.getElementById('section').value="0";
                document.getElementById('dateofappoinment').value="";
                document.getElementById('dateofprobation').value="";    
                document.getElementById('designation').value="0";
                document.getElementById('community').value="";
                document.getElementById('pancardno').value="";
                document.getElementById('paymentmode').value="0";
                document.getElementById('bankcode').value="";
                document.getElementById('banksbaccount').value="";
            }else{
//                var epfno=document.getElementById('epfno').value;
//                EmployeeMasterAction.getEmployeeDetailsFromEPFMaster(epfno,setEmployeeDetailsFROMEPFMaster);
                document.getElementById('fpfno').focus();
            }

        }
        function setEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById('epfno').value="";
                document.getElementById('funtype').focus();
                document.getElementById('fpfno').value="";
                document.getElementById('employeename').value="";
                document.getElementById('fathername').value="";
                document.getElementById('gender').value="0";
                document.getElementById('empcategory').value="0";
                document.getElementById('dateofbirth').value="";
//                document.getElementById('region').value="0";
                document.getElementById('section').value="0";
                document.getElementById('dateofappoinment').value="";
                document.getElementById('dateofprobation').value="";    
                document.getElementById('designation').value="0";
                document.getElementById('community').value="";
                document.getElementById('pancardno').value="";
                document.getElementById('paymentmode').value="0";
                document.getElementById('bankcode').value="";
                document.getElementById('banksbaccount').value="";
            }else{                
                document.getElementById('fpfno').value=map.fpfno;
                document.getElementById('employeename').value=map.employeename;
                if(map.empcategory=="R"){
                    document.getElementById('empcategory').value="G";
                }                
                document.getElementById('fathername').value=map.fathername;
                document.getElementById('gender').value=map.gender;
                document.getElementById('dateofbirth').value=map.dateofbirth;
//                document.getElementById('region').value=map.region;
                document.getElementById('section').value=map.section;
                document.getElementById('dateofappoinment').value=map.doa;
                document.getElementById('dateofprobation').value=map.dateofprobation;    
                document.getElementById('designation').value=map.designation;
                document.getElementById('community').value=map.community;
                document.getElementById('pancardno').value=map.pancardno;
                document.getElementById('paymentmode').value=map.paymentmode;
                document.getElementById('bankcode').value=map.bankcode;
                document.getElementById('banksbaccount').value=map.banksbaccount;
                document.getElementById('region').disabled = true;
                document.getElementById('fpfno').readOnly = true;
            }
            
        }            
        function setEmployeeDetailsFROMEPFMaster(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById('epfno').value="";
                document.getElementById('funtype').focus();
                document.getElementById('fpfno').value="";
                document.getElementById('employeename').value="";
                document.getElementById('fathername').value="";
                document.getElementById('gender').value="0";
                document.getElementById('empcategory').value="0";
                document.getElementById('dateofbirth').value="";
//                document.getElementById('region').value="0";
                document.getElementById('section').value="0";
                document.getElementById('dateofappoinment').value="";
                document.getElementById('dateofprobation').value="";
                document.getElementById('designation').value="0";
                document.getElementById('community').value="";
                document.getElementById('pancardno').value="";
                document.getElementById('paymentmode').value="0";
                document.getElementById('bankcode').value="";
                document.getElementById('banksbaccount').value="";
            }else{
                document.getElementById('fpfno').value=map.fpfno;
                document.getElementById('employeename').value=map.employeename;
//                document.getElementById('empcategory').value=map.empcategory;
                document.getElementById('fathername').value=map.fathername;
                document.getElementById('gender').value=map.gender;
                document.getElementById('dateofbirth').value=map.dateofbirth;
//                document.getElementById('region').value=map.region;
                document.getElementById('section').value=map.section;
                document.getElementById('dateofappoinment').value=map.doa;
                document.getElementById('dateofprobation').value=map.dateofprobation;
                document.getElementById('designation').value=map.designation;
                document.getElementById('community').value=map.community;
                document.getElementById('pancardno').value="";
                document.getElementById('paymentmode').value="0";
                document.getElementById('bankcode').value="";
                document.getElementById('banksbaccount').value="";
                document.getElementById('empcategory').value="0";
                document.getElementById('region').disabled = true;
                document.getElementById('fpfno').readOnly = true;
            }
        }
    </script>
</html>




