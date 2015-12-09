<%-- 
    Document   : employeedeductionAccountcode
    Created on : Nov 21, 2012, 10:29:34 AM
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
        <title>Employee Account Code Assign</title>
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
                $("#savebutton").click(function(){
                    var deductioncode=document.getElementById('deductioncode').value;
                    var epfno=document.getElementById('epfno').value;
                    var empaccountcode=document.getElementById('empaccountcode').value;
                    var serialno=document.getElementById('serialno').value;
                    

                    if(deductioncode=="0"){
                        alert("Please Select the Account Type")
                        document.getElementById('deductioncode').focus();
                        return false;
                    }else if(epfno==""){
                        alert("Please Enter the Employee EPF Number")
                        document.getElementById('epfno').focus();
                        return false;
                    }if(empaccountcode==""){
                        alert("Please Enter the Account Code For Employee")
                        document.getElementById('empaccountcode').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            PayBillMasterAction.saveEmployeeAccountCode(deductioncode,epfno,empaccountcode,serialno,accountcodeSaveDetails);
                        }
                    }
                });
            });


            function accountcodeSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.getElementById('deductioncode').value="0";
                    document.getElementById('epfno').value="";                    
                    document.getElementById('empaccountcode').value="";
                    document.getElementById('employeename').value="";
                    document.getElementById('serialno').value="";


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
                        <td colspan="4" class="mainheader">Employee Account Code Assign</td>
                    </tr>

                    <tr class="lightrow">
                        <td width="30%" class="textalign">Account Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <select class="combobox" name="deductioncode" id="deductioncode"></select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();" >
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Employee Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <input type="text" class="textbox" name="employeename" id="employeename" readonly >
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Account Code</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="empaccountcode" id="empaccountcode" >
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
            <input type="hidden" name="serialno" id="serialno">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            PayBillMasterAction.loadDeductionDetails(fillDeductionsCombo);
        }
        function fillDeductionsCombo(map){
            dwr.util.removeAllOptions("deductioncode");
            dwr.util.addOptions("deductioncode",map.deductionlist);
        }

        function loadEmployeeDetails(){
            var epfno=document.getElementById('epfno').value;
            var deductioncode=document.getElementById('deductioncode').value;
            if(deductioncode=="0"){
                alert("Please Select the Account Type")
                document.getElementById('deductioncode').focus();
                return false;
            }else{
                if(epfno.length>0){
                    PayBillMasterAction.loadEmployeeAccountDetails(epfno,deductioncode,fillEmployeeDetails);
                }else{
                    document.getElementById('epfno').value="";
                    document.getElementById("epfno").focus();
                    document.getElementById("employeename").value="";
                }
            }
        }
        function fillEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert("Please Enter the Valid Employee EPF No");
                document.forms[0].epfno.value = "";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
                document.getElementById("empaccountcode").value="";

                // return false;
            } else {
                document.getElementById("employeename").value=map.employeename;
                document.getElementById("empaccountcode").value=map.accountcode;
                document.getElementById("serialno").value=map.serialno;
            }
        }
    </script>
</html>

