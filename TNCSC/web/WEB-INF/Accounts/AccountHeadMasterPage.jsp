<%-- 
    Document   : AccountHeadMasterPage
    Created on : Dec 18, 2012, 2:23:40 PM
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
        <title>Account Head Master</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/AccountsMastersAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $("#savebutton").click(function(){
                    var accountcode=document.getElementById('accountcode').value;
                    var ledgercode=document.getElementById('ledgercode').value;
                    var accountheadname=document.getElementById('accountheadname').value;
                    var serialno=document.getElementById('serialno').value;


                    if(accountcode=="0"){
                        alert("Please Select the Account Group Head Type")
                        document.getElementById('accountcode').focus();
                        return false;
                    }else if(ledgercode=="0"){
                        alert("Please Select the Ledger Account Type")
                        document.getElementById('ledgercode').focus();
                        return false;
                    }if(accountheadname==""){
                        alert("Please Enter the Account Head Name")
                        document.getElementById('accountheadname').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            AccountsMastersAction.saveAccountHeadMaster(accountcode,ledgercode,accountheadname,serialno,accountHeadSaveDetails);
                        }
                    }
                });
            });


            function accountHeadSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.getElementById('accountcode').value="0";
                    document.getElementById('ledgercode').value="0";
                    document.getElementById('accountheadname').value="";
                    document.getElementById('serialno').value="0";
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
                        <td colspan="4" class="mainheader">Account Head Master</td>
                    </tr>

                    <tr class="lightrow">
                        <td width="30%" class="textalign">Account Group Head</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <select class="combobox" name="accountcode" id="accountcode"></select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Ledger Account</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign">
                            <select class="combobox" name="ledgercode" id="ledgercode"></select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Account Head Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="accountheadname" id="accountheadname" >
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
            <input type="hidden" name="serialno" id="serialno" value="0">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            AccountsMastersAction.loadAccountGroupHeadDetails(fillDeductionsCombo);
        }
        function fillDeductionsCombo(map){
            dwr.util.removeAllOptions("accountcode");
            dwr.util.addOptions("accountcode",map.accountgrouplist);
            dwr.util.removeAllOptions("ledgercode");
//            dwr.util.addOptions("ledgercode",map.accountgrouplist);
            dwr.util.addOptions("ledgercode",map.ledgerlist);
        }        
    </script>
</html>

