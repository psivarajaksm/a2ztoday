<%--
    Document   : ChangeEpfno
    Created on : May 30, 2013, 12:22:07 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>Change Employee EPF Number</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/EmployeeMasterAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $('#reasondate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();


                $("#savebutton").click(function(){
                    var epfno=document.getElementById('epfno').value;
                    var newepfno=document.getElementById('newepfno').value;
                    var empcategory=document.getElementById('empcategory').value;

                    if(empcategory=="0"){
                        alert("Please Select the Employee Category")
                        document.getElementById('empcategory').focus();
                        return false;
                    }else if(epfno==""){
                        alert("Please Enter the Employee EPF Number")
                        document.getElementById('epfno').focus();
                        return false;
                    }else if(newepfno==""){
                        alert("Please Enter the Changing Employee EPF Number")
                        document.getElementById('newfpfno').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue the Change Given New EPF Number?");
                        if (answer){
                            getBlanket('continueDIV');
                            EmployeeMasterAction.saveChangeEpfno(empcategory,epfno,newepfno,masterSaveDetails);
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
                    document.getElementById('epfno').value="";
                    document.getElementById('newepfno').value="";
                    document.getElementById('newemployeename').value="";
                    document.getElementById('newfpfno').value="";
                    document.getElementById('employeename').value="";
                    document.getElementById('newfathername').value="";


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

                <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Change Employee EPF Number</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Emp. Category</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="empcategory" id="empcategory">
                                <option value="0">--Select--</option>
                                <option value="G">Government</option>
                                <option value="R">Regular</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();" >
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
                        <td width="30%" class="textalign">New EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="newepfno" id="newepfno" onkeypress="specialCharNotAllowed(this)" onblur="loadNewEmployeeDetails();" >
                        </td>
                    </tr>
                </table>
                <div  id="empdetailsid" style="display:none;">
                    <table width="100%" align="center" border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Fpf No</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="newfpfno" id="newfpfno" readonly >
                            </td>
                        </tr>
                        <tr class="lightrow">
                            <td width="30%" class="textalign">Employee Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="newemployeename" id="newemployeename" readonly >
                            </td>
                        </tr>
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Father Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <input type="text" class="textbox" name="newfathername" id="newfathername" readonly >
                            </td>
                        </tr>
                    </table>
                </div>
                <table width="100%" align="center" border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script type="text/javascript">
        function loadEmployeeDetails(){
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                EmployeeMasterAction.loadEmployeeDetails(epfno,fillEmployeeDetails);
            }else{
                document.getElementById('epfno').value="";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
            }
        }
        function fillEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                //                alert("Please Enter the Valid Employee EPF No");
                document.getElementById("epfno").value = "";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";

                // return false;
            } else {
                document.getElementById("employeename").value=map.employeename;
            }
        }

        function loadNewEmployeeDetails(){
            var newepfno=document.getElementById('newepfno').value;
            var empcategory=document.getElementById('empcategory').value;
            if(newepfno.length>0){
//                if(empcategory=="R"){
//                    EmployeeMasterAction.getEmployeeDetailsFromEPFMaster(newepfno,setEmployeeDetailsFROMEPFMaster);
//                }else{
                    EmployeeMasterAction.loadEmployeeDetails(newepfno,showEmployeeDetails);
//                }
            }else{
                document.getElementById('empdetailsid').style.display="none";
                document.getElementById('newepfno').value="";
                document.getElementById("newepfno").focus();
                document.getElementById("newemployeename").value="";
            }
        }
        function showEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                //                alert(map.ERROR);
                document.getElementById('empdetailsid').style.display="none";
                //                document.getElementById('newepfno').value="";
                //                document.getElementById('newfpfno').value="";
                //                document.getElementById('newemployeename').value="";
                //                document.getElementById('newfathername').value="";
            }else{
                alert("Given EPF Number is Already in Employee Master.Please Contact Administrator");
                document.getElementById('newepfno').value="";
                document.getElementById('newfpfno').value="";
                document.getElementById('newemployeename').value="";
                document.getElementById('newfathername').value="";
                document.getElementById('empdetailsid').style.display="none";
                //                document.getElementById('newfpfno').value=map.fpfno;
                //                document.getElementById('newemployeename').value=map.employeename;
                //                document.getElementById('newfathername').value=map.fathername;

            }
        }
        function setEmployeeDetailsFROMEPFMaster(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById('empdetailsid').style.display="none";
                document.getElementById('newepfno').value="";
                document.getElementById('newfpfno').value="";
                document.getElementById('newemployeename').value="";
                document.getElementById('newfathername').value="";
            }else{
                document.getElementById('empdetailsid').style.display="block";
                document.getElementById('newfpfno').value=map.fpfno;
                document.getElementById('newemployeename').value=map.employeename;
                document.getElementById('newfathername').value=map.fathername;

            }
        }
    </script>
</html>





