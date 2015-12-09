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
        <title>Employee Master Creation</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="<%=staticPath%>scripts/dateValidations.js"></script>
        <script src="<%=staticPath%>scripts/cal.js"></script>
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
                $('#dateofconfirmation').datepicker({ dateFormat: "dd/mm/yy" }).val();
           
                $('#modifyeducationform').dialog({
                    autoOpen: false,
                    width: 400,
                    height: 400,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            getBlanket('continueDIV');
                            var epfno = document.getElementById("epfno").value;
                            var educationtype = document.getElementById("educationtype").value;
                            var board = document.getElementById("board").value;
                            var serialno = document.getElementById("serialno").value;
                            var etype = "";    
                            if(educationtype=='Academic'){
                                etype =document.getElementById("basiceducation").value;
                            }else if(educationtype=='Technical'){
                                etype =document.getElementById("Profeducation").value;
                            }else if(educationtype=='Professional'){
                                etype =document.getElementById("techeducation").value;
                            }                            
                            EmployeeMasterAction.saveEmployeeDetail(epfno,educationtype,etype,board,"EDU",serialno,fillEmployeeDetail);
                            $(this).dialog("close");                            
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                $('#modifynomineeform').dialog({
                    autoOpen: false,
                    width: 400,
                    height: 400,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            getBlanket('continueDIV');
                            var epfno = document.getElementById("epfno").value;
                            var nomineename = document.getElementById("nomineename").value;
                            var nomineerelation = document.getElementById("nomineerelation").value;                            
                            var nomineepercentage = document.getElementById("nomineepercentage").value;      
                            var serialno = document.getElementById("serialno").value;
                            EmployeeMasterAction.saveEmployeeDetail(epfno,nomineename,nomineerelation,nomineepercentage,"NOM",serialno,fillEmployeeDetail);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                $('#modifyfamilyform').dialog({
                    autoOpen: false,
                    width: 400,
                    height: 400,
                    modal: true,
                    buttons: {
                        "Save": function() {
                            getBlanket('continueDIV');
                            var epfno = document.getElementById("epfno").value;
                            var membername = document.getElementById("membername").value;
                            var memberrelation = document.getElementById("memberrelation").value;
                            var memberage = document.getElementById("memberage").value;
                            var serialno = document.getElementById("serialno").value;
                            EmployeeMasterAction.saveEmployeeDetail(epfno,membername,memberrelation,memberage,"MEM",serialno,fillEmployeeDetail);
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });
                $("#addedubutton").click(function(){
                    $('#modifyeducationform').dialog('open');
                });               
                $("#addfamilybutton").click(function(){
                    $('#modifyfamilyform').dialog('open');
                });             
                $("#addnomineebutton").click(function(){
                    $('#modifynomineeform').dialog('open');
                }); 
                $('#Profeducation').hide();
                $('#techeducation').hide();
                $('#educationtype').change(function(){
                    $('#basiceducation').hide();
                    $('#Profeducation').hide();
                    $('#techeducation').hide();
                if($('#educationtype').val() == 'Academic') {
                    $('#basiceducation').show();                 
                }else if($('#educationtype').val() == 'Technical') {
                    $('#Profeducation').show(); 
                }else if($('#educationtype').val() == 'Professional') {
                    $('#techeducation').show();
                }
                });
                $('#permancheck').click(function() {
                    if($('#permancheck').prop('checked')) {
                        document.getElementById('permanentaddressline').value = document.getElementById('presentaddressline').value;
                        document.getElementById('permanentdistrict').value = document.getElementById('presentdistrict').value;
                        document.getElementById('permanentpincode').value= document.getElementById('presentpincode').value;
                    } else {
                        document.getElementById('permanentaddressline').value = "";
                        document.getElementById('permanentdistrict').value = "";
                        document.getElementById('permanentpincode').value= "";  
                    }
                });
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
                    var positiondate = document.getElementById('positiondate').value;
                    var positiontime= document.getElementById('positiontime').value;
                    var nativeregion = document.getElementById('nativeregion').value;
                    var dateofconfirmation="";
                    if(funtype=="0"){
                        alert("Please Select the Function Type");
                        document.getElementById('funtype').focus();
                        return false;
                    }else  if(epfno==""){
                        alert("Please enter the EPF Number");
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
                    }else if(dateofappoinment==""){
                        alert("Please Select the date of appoinment");
                        document.getElementById('dateofappoinment').focus();
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
                    else if(paymentmode=="0"){
                        alert("Please enter the Payment Mode");
                        document.getElementById('paymentmode').focus();
                        return false;
                    }
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
                            community,pancardno,paymentmode,bankcode,banksbaccount,empcategory,positiondate,positiontime,nativeregion,
                            masterSaveDetails);
                        }
                    
                    }
                });
            });   
            
            function fillEmployeeDetail(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    document.getElementById("serialno").value="mod";
                    var type = map.type;
                    if("NOM"==type){                        
                        document.getElementById("nomineeDetails").innerHTML=map.NOM;
                    }else if("MEM"==type){
                        document.getElementById("familyDetails").innerHTML=map.MEM;
                    }else if("EDU"==type){
                        document.getElementById("educationDetails").innerHTML=map.EDU;
                    }
                }
            }
            function getModifyEmployeeDetail(serialno,type,text1,text2,text3){
                document.getElementById('serialno').value=serialno;
                if("NOM"==type){
                    document.getElementById("nomineename").value=text1;
                    document.getElementById("nomineerelation").value=text2;
                    document.getElementById("nomineepercentage").value=text3;
                    $('#modifynomineeform').dialog('open');
                }else if("MEM"==type){
                    document.getElementById("membername").value=text1;
                    document.getElementById("memberrelation").value=text2;
                    document.getElementById("memberage").value=text3;
                    $('#modifyfamilyform').dialog('open');
                }else if("EDU"==type){
                    document.getElementById('educationtype').value=text1;                    
                    $('#basiceducation').hide();
                    $('#Profeducation').hide();
                    $('#techeducation').hide();
                    if(text1 == 'Academic') {
                        $('#basiceducation').show();
                        document.getElementById('basiceducation').value=text2;                                         
                    }else if(text1 == 'Technical') {                        
                        $('#Profeducation').show();
                        document.getElementById('Profeducation').value=text2;
                    }else if(text1 == 'Professional') {                        
                        $('#techeducation').show();
                        document.getElementById('techeducation').value=text2;
                    }
                    document.getElementById('board').value=text3;                      
                    $('#modifyeducationform').dialog('open');
                }
            }
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
                document.getElementById('positiondate').value="";
                document.getElementById('positiontime').value="0";
                document.getElementById('nativeregion').value="0";
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
                        <td width="100%" align="center" class="headerdata">Employee Master</td>
                    </tr>
                    <td width="100%" class="mainheader">Employee Master Creation</td>
                </table>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="lightrow">
                        <td width="29%" class="textalign">EPF Number</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onblur="checkEPFno();">
                        </td>
                        <td width="29%" class="textalign"></td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="29%" class="textalign">Employee Name</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="employeename" id="employeename" readonly="true">
                        </td>
                        <td width="29%" class="textalign">Date of Birth</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="datetextbox" name="dateofbirth" id="dateofbirth" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this); minorValidateDate(this);" readonly >&nbsp;[dd/mm/yyyy]
                        </td>
                    </tr>
                </table>
                <br>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="100%" colspan="12" align="left">
                            Present Address Detail
                        </td>
                    </tr>
                    <tr class="lightrow">                        
                        <td width="29%" class="textalign">Address Line</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" nowrap class="textfieldalign" >
                            <input type="text" class="textbox" name="presentaddressline" id="presentaddressline" maxlength="100" >                            
                        </td>
                        <td width="29%" class="textalign">District</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="presentdistrict" id="presentdistrict" class="textfieldalign" ></select>
                        </td>
                    </tr>
                    <tr class="darkrow">                        
                        <td width="29%" class="textalign">Pincode</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" nowrap class="textfieldalign" >
                            <input type="text" class="textbox" name="presentpincode" id="presentpincode" maxlength="6" onblur="">                            
                        </td>
                        <td width="29%" class="textalign">Blood Group</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >  
                            <input type="text" class="textbox" name="bloodgroup" id="bloodgroup" >
                        </td>
                    </tr>
                    <tr class="lightrow">                        
                        <td width="29%" class="textalign">Land Number</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" nowrap class="textfieldalign" >
                            <input type="text" class="textbox" name="landnumber" id="landnumber" maxlength="14" onblur="">
                        </td>
                        <td width="29%" class="textalign">Mobile Number</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="mobilenumber" id="mobilenumber" maxlength="14" onblur="">
                        </td>
                    </tr>
                </table>
                <br>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="100%" colspan="12" align="left">
                            Permanent Address Detail &nbsp;&nbsp;<input type="checkbox" name="permancheck" id="permancheck"> Is Permanent Address is same as Present Address?
                        </td>
                    </tr>
                    <tr class="lightrow">                        
                        <td width="29%" class="textalign">Address Line</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" nowrap class="textfieldalign" > 
                            <input type="text" class="textbox" name="permanentaddressline" id="permanentaddressline" maxlength="100" >                            
                        </td>
                        <td width="29%" class="textalign">District</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="permanentdistrict" id="permanentdistrict" class="textfieldalign" ></select>
                        </td>
                    </tr>
                    <tr class="darkrow">                        
                        <td width="29%" class="textalign">Pincode</td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" nowrap class="textfieldalign" >
                            <input type="text" class="textbox" name="permanentpincode" id="permanentpincode" maxlength="6" onblur="">                            
                        </td>
                        <td width="29%" class="textalign"></td>
                        <td width="1%" class="mandatory"></td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                        </td>
                    </tr>
                </table>
                <br>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="100%" colspan="12" align="left">
                            Education Detail <br>&nbsp;<input type="button" name="addedubutton" id="addedubutton" value="Add">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="educationDetails" style="overflow:auto;"></div>
                        </td>
                    </tr>
                </table>
                <div id="modifyeducationform" title="Education Detail Form">                        
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Type</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign">
                                <select id="educationtype" class="combobox" >
                                    <option value="Academic">Academic</option>
                                    <option value="Technical">Technical</option>
                                    <option value="Professional">Professional</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Education</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign">
                                <select id="basiceducation" class="combobox" >
                                    <option value="10th">10th</option>
                                    <option value="Diploma">Diploma</option>
                                    <option value="+2">+2</option>
                                    <option value="Other">Other</option> 
                                </select>
                                <select id="techeducation" class="combobox" >
                                    <option value="Typewriter">Typewriter</option>
                                    <option value="Shorthand">Shorthand</option>
                                    <option value="list">list</option> 
                                </select>
                                <select id="Profeducation" class="combobox" >
                                    <option value="MBA">MBA</option>
                                    <option value="MCA">MCA</option>
                                    <option value="2">2</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="lightrow"> 
                            <td width="20%" class="textalign">Name of Board/University</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="board" name="board" /></td>
                        </tr>
                    </table>                        
                </div>                    
                <br>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="100%" colspan="12" align="left">
                            Family Details <br>&nbsp;<input type="button" name="addfamilybutton" id="addfamilybutton" value="Add">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="familyDetails" style="overflow:auto;"></div> 
                        </td>
                    </tr>
                </table> 
                <div id="modifyfamilyform" title="Family Detail Form">                        
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Family Member Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="membername" name="membername" /></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Relation</td> 
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign">
                                <select id="memberrelation" class="combobox" >
                                    <option value="Father">Father</option>
                                    <option value="Mother">Mother</option>
                                    <option value="Son">Son</option>
                                    <option value="Daughter">Daughter</option>
                                    <option value="Brother">Brother</option>
                                    <option value="Sister">Sister</option>
                                    <option value="Grandfather">Grandfather</option>
                                    <option value="Grandmother">Grandmother</option>
                                    <option value="Cousins">Cousins</option>
                                    <option value="Uncle">Uncle</option>
                                    <option value="Aunt">Aunt</option>
                                    <option value="Nephew">Nephew</option>
                                    <option value="Niece">Niece</option>
                                    <option value="Cousin">Cousin</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Family Member Age</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="memberage" name="memberage" /></td>
                        </tr>
                    </table>                        
                </div>
                <br> 
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="100%" colspan="12" align="left">
                            Nominee Detail <br>&nbsp;<input type="button" name="addnomineebutton" id="addnomineebutton" value="Add">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="nomineeDetails" style="overflow:auto;"></div>
                        </td>
                    </tr>
                </table>
                <div id="modifynomineeform" title="Nominee Detail Form">                        
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Nominee Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="nomineename" name="nomineename" /></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Relation</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign">
                                <select id="nomineerelation" class="combobox" > 
                                    <option value="Father">Father</option>
                                    <option value="Mother">Mother</option>
                                    <option value="Son">Son</option>
                                    <option value="Daughter">Daughter</option>
                                    <option value="Brother">Brother</option>
                                    <option value="Sister">Sister</option>
                                    <option value="Grandfather">Grandfather</option>
                                    <option value="Grandmother">Grandmother</option>
                                    <option value="Cousins">Cousins</option>
                                    <option value="Uncle">Uncle</option>
                                    <option value="Aunt">Aunt</option>
                                    <option value="Nephew">Nephew</option>
                                    <option value="Niece">Niece</option>
                                    <option value="Cousin">Cousin</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Nominee Percentage</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="nomineepercentage" name="nomineepercentage" /></td>
                        </tr>
                        
                    </table>                       
                </div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="serialno" id="serialno">
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            EmployeeMasterAction.loadRegionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("presentdistrict");             
            dwr.util.addOptions("presentdistrict",map.regionlist);
            dwr.util.removeAllOptions("permanentdistrict");
            dwr.util.addOptions("permanentdistrict",map.regionlist);
        }
        function checkEPFno(){
            var epfno=document.getElementById('epfno').value;
            if(epfno!=null && epfno!=""){
                EmployeeMasterAction.getEmployeeDetails(epfno,setEmployeeDetails);
            }else{                
                return false;
            }
        }
        function setEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById('epfno').value="";
                document.getElementById('employeename').value="";
                document.getElementById('dateofbirth').value="";
            }else{
                document.getElementById('employeename').value=map.employeename;                
                document.getElementById('dateofbirth').value=map.dateofbirth;
                getEmployeeDetail();
            }
        }        
        function getEmployeeDetail(){
            getBlanket('continueDIV');
            var epfno = document.getElementById('epfno').value;
            EmployeeMasterAction.getEmployeeDetail(epfno,"MEM",fillEmployeeDetail);
            EmployeeMasterAction.getEmployeeDetail(epfno,"NOM",fillEmployeeDetail);
            EmployeeMasterAction.getEmployeeDetail(epfno,"EDU",fillEmployeeDetail);
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
                document.getElementById('positiondate').value="";
                document.getElementById('positiontime').value="0";
                document.getElementById('nativeregion').value="0";
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
                document.getElementById('positiondate').value="";
                document.getElementById('positiontime').value="0";
                document.getElementById('nativeregion').value="0";
            }
        }
    </script>
</html>




