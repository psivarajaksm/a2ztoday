<%-- 
    Document   : PayCodeMaster
    Created on : Jul 20, 2012, 11:12:05 AM
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
        <title>Employee PayCode Master Creation</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>   
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/PayBillMasterAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <script type="text/javascript">

            $(function() {
                $("#savebutton").click(function(){

                    var paytype=document.getElementById('paytype').value;
                    var paycodename=document.getElementById('paycodename').value;
                    if(paytype=="0"){
                        alert("Please Select the Pay Type")
                        document.getElementById('paytype').focus();
                        return false;
                    }else if(paycodename==""){
                        alert("Please Enter the Pay Name")
                        document.getElementById('paycodename').focus();
                        return false;
                    }else{ 
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){

                            var paypercentage=eval(document.getElementById('paypercentage').value);
                            var paycode=document.getElementById('paycode').value;
                            getBlanket('continueDIV');
                            PayBillMasterAction.savePayCodeMaster(paytype,paycode,paycodename,paypercentage,
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
                    document.getElementById('paytype').value="0";
                    document.getElementById('paycode').value="";
                    document.getElementById('paycodename').value="";
                    document.getElementById('paypercentage').value="";
                    document.getElementById("paycodeDetails").innerHTML=map.paycodeDetails;
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
                        <td colspan="4" class="mainheader">PayCode Master Creation</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Pay Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >

                            <select class="combobox" name="paytype" id="paytype" onchange="getPayCode(this.value,'paycode');">
                                <option value="0">--Select--</option>
                                <option value="E">Earnings</option>
                                <option value="D">Deductions</option>
                                <option value="L">Loans</option>
                                <option value="R">Recovery</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">PayCode</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="paycode" id="paycode" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">PayCode Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="paycodename" id="paycodename">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Percentage</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="amounttextbox" name="paypercentage" id="paypercentage" maxlength="5" value="0" onblur="checkPercentage(this.id,'Paycode Percentage');">&nbsp;&nbsp;%
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="paycodeDetails" style="height:300px;overflow:auto;"> </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script type="text/javascript">
//        bodyOnload();
//        function bodyOnload(){
//            PayBillMasterAction.getPayCodeDetails(displayPayCodeDetails)
//            
//        }
//        function displayPayCodeDetails(map){
//            document.getElementById("paycodeDetails").innerHTML=map.paycodeDetails;
//        }

        function getPayCode(paytype,orderbyString){
//            var paytype = paytypeObj.value;
            PayBillMasterAction.getPayCodeSerial(paytype,orderbyString,loadpaycode);
        }
        function loadpaycode(map){
            document.getElementById('paycode').value=map.paycode;
            document.getElementById('paycodename').value="";
            document.getElementById('paypercentage').value="0";
            document.getElementById("paycodeDetails").innerHTML=map.paycodeDetails;
        }

        function getPaycodeMasterDetailsForModify(payCode,payCodeName,percentage){
            document.getElementById('paytype').value=payCode.charAt(0);
            document.getElementById('paycode').value=payCode;
            document.getElementById('paycodename').value=payCodeName;
            document.getElementById('paypercentage').value=percentage;
        }
    </script>
</html>




