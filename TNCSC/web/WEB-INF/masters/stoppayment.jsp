<%-- 
    Document   : stoppayment
    Created on : Jul 30, 2012, 2:17:31 PM
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
        <title>Employee Stop Payment</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/PayBillMasterAction.js"></script>
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
                    var reason=document.getElementById('reasontype').value;
                    var reasondate=document.getElementById('reasondate').value;
                    var remarks=document.getElementById('remarks').value;
                    var reasontype=document.getElementById("reasontype").options[document.getElementById("reasontype").selectedIndex].text;
                    
                    if(epfno==""){
                        alert("Please Enter the Employee EPF Number")
                        document.getElementById('epfno').focus();
                        return false;
                    }else if(reason=="0"){
                        alert("Please Select the Reason Type")
                        document.getElementById('reasontype').focus();
                        return false;
                    }else if(reasondate==""){
                        alert("Please Select the Reason Date")
                        document.getElementById('reasondate').focus();
                        return false;
                    }else if(remarks==""){
                        alert("Please Enter the Remarks")
                        document.getElementById('remarks').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){      
                            getBlanket('continueDIV');
                            PayBillMasterAction.saveStopPayment(epfno,reasontype,reasondate,remarks,masterSaveDetails);
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
                    document.getElementById('reasontype').value="0";
                    document.getElementById('epfno').value="";
                    document.getElementById('reasondate').value="";
                    document.getElementById('remarks').value="";
                    document.getElementById('employeename').value="";
                    

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
                        <td colspan="4" class="mainheader">Employee Stop Payment</td>
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
                        <td width="30%" class="textalign">Reason Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="reasontype" id="reasontype" >
                                <option value="0">--Select--</option>
                                <option value="7">Seasonal</option>
                                <option value="8">Load Man</option>
<!--                                <option value="9">Transferred</option>-->
                                <option value="10">No Report</option>
                                <option value="1">Resigned</option>
                                <option value="2">Retired</option>
                                <option value="3">Death</option>
                                <option value="4">Under Suspension</option>
                                <option value="5">On Leave</option>
                                <option value="6">Revoke</option>
                                <option value="11">Removed from Service</option>

                            </select>
                        </td>
                    </tr>                    
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="reasondate" id="reasondate" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Remarks</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <textarea class="textareabox" name="remarks" id="remarks"></textarea>
                        </td>
                    </tr>                    
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
                PayBillMasterAction.loadEmployeeDetails(epfno,fillEmployeeDetails);                
            }else{
                document.getElementById('epfno').value="";                
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
            }
        }
        function fillEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.forms[0].epfno.value = "";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";

                // return false;
            } else {                                
                document.getElementById("employeename").value=map.employeename;
            }
        }
    </script>
</html>




