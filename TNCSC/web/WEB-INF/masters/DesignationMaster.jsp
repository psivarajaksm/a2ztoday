<%-- 
    Document   : DesignationMaster
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
        <title>Designation Master Creation</title>
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

                    var cadretype=document.getElementById('cadretype').value;
                    var wingtype=document.getElementById('wingtype').value;
                    var designationcode=document.getElementById('designationcode').value;
                    var designationname=document.getElementById('designationname').value;
                    var payscale=document.getElementById('payscale').value;
                    var orderno=document.getElementById('orderno').value;

                    if(cadretype=="0"){
                        alert("Please Select the Cadre Type")
                        document.getElementById('cadretype').focus();
                        return false;
                    }else if(wingtype=="0"){
                        alert("Please Enter the Wing Name")
                        document.getElementById('wingtype').focus();
                        return false;
                    }else if(designationname==""){
                        alert("Please Enter the Designation Name")
                        document.getElementById('designationname').focus();
                        return false;                    
                    }else if(payscale==""){
                        alert("Please Enter the Pay Scale")
                        document.getElementById('payscale').focus();
                        return false;                    
                    }else if(orderno==""){
                        alert("Please Enter the Order No")
                        document.getElementById('orderno').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            PayBillMasterAction.saveDesignationMaster(designationcode,designationname,payscale,orderno,
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
                    document.getElementById('cadretype').value="0";
                    document.getElementById('wingtype').value="0";
                    document.getElementById('designationcode').value="";
                    document.getElementById('designationname').value="";
                    document.getElementById('payscale').value="";
                    document.getElementById('orderno').value="";
                    document.getElementById("designationDetails").innerHTML=map.designationDetails;
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
                        <td colspan="4" class="mainheader">Designation Master Creation</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Cadre</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >

                            <select class="combobox" name="cadretype" id="cadretype">
                                <option value="0">--Select--</option>
                                <option value="M">Managerial</option>
                                <option value="S">Supervisor</option>
                                <option value="C">Clerical</option>
                                <option value="L">Lower</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Wing</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >

                            <select class="combobox" name="wingtype" id="wingtype" onblur="getDesignationCode(this);">
                                <option value="0">--Select--</option>
                                <option value="E">Establishment & Accounts</option>
                                <option value="C">Construction</option>
                                <option value="Q">Quality Control</option>
                                <option value="M">Mechanical</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Designation Code</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="designationcode" id="designationcode" readonly>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Designation Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="designationname" id="designationname">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Pay Scale</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="payscale" id="payscale">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Order No</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="orderno" id="orderno">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="designationDetails" style="height:300px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            PayBillMasterAction.getDesignationDetails(displayDesignationDetails)
        }
        function displayDesignationDetails(map){
            document.getElementById("designationDetails").innerHTML=map.designationDetails;
        }
        function getDesignationCode(wingtypeObj){
            var cadretype=document.getElementById('cadretype').value
            var wingtypeObj = wingtypeObj.value;
            if(cadretype=="0"){
                alert("Please Select the Cadre Type")
                document.getElementById('cadretype').focus();
                return false;
            }else if(wingtypeObj=="0"){
                alert("Please Select the Wing Type")
                document.getElementById('wingtype').focus();
                return false;
            }else{
                PayBillMasterAction.getDesignationCodeSerial(cadretype,wingtypeObj,loaddesignationcode);
            }
        }
        function loaddesignationcode(map){
            document.getElementById('designationcode').value=map.designationcode;
        }
        function getDesignationMasterDetailsForModify(designationCode,designationName,payscale,orderno){
            document.getElementById('cadretype').value=designationCode.charAt(0);
            document.getElementById('wingtype').value=designationCode.charAt(1);
            document.getElementById('designationcode').value=designationCode;
            document.getElementById('designationname').value=designationName;
            document.getElementById('payscale').value=payscale;
            document.getElementById('orderno').value=orderno;

        }
    </script>
</html>




