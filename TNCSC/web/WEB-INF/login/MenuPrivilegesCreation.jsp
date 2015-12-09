<%-- 
    Document   : ProfileUpdation
    Created on : Oct 14, 2011, 1:59:26 PM
    Author     : Jagan Mohan. B
--%>

<%@page import="java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
            String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
            String path = request.getContextPath();
            String message = "";
            if ((request.getAttribute("message") != null)) {
                message = (String) request.getAttribute("message");
            }
            request.removeAttribute("message");
            UserViewModel sesessionviewmodel = new UserViewModel();
            if ((session.getAttribute("userDetails") != null)) {
                sesessionviewmodel = (UserViewModel) session.getAttribute("userDetails");
            }
            String sessionusertype = sesessionviewmodel.getUsertype();
            String sessionUserId = sesessionviewmodel.getUserid();
            sesessionviewmodel = null;

            String adminUser = ApplicationConstants.ADMIN_USER;
            String supperUser = ApplicationConstants.SUPER_USER;
%>
<%
            String usertype = "";
            String menulist = "";
            if (request.getSession(false).getAttribute("menumastermap") != null) {
                Map map = (Map) request.getSession(false).getAttribute("menumastermap");
                usertype = (String) map.get("usertypelist");
                menulist = (String) map.get("menulist");
            }
%>

<html>
    <head>
        <script src="dwr/interface/UserAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center">
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="<%=staticPath%>images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="left-sidebar">
                <%@ include file="/common/leftpage.jsp" %>
            </div>
            <div id="content">
                <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" class="headerdata">Menu Privileges</td>
                                </tr>
                            </table>                            
                            <table width="50%" id="SECURITY_DIV" class="tableBorder1" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr class="darkrow">
                                    <td width="100%" colspan="4" align="left" class="headerdata1">User Privileges</td>
                                </tr>
                                <!--                                <tr class="lightrow">
                                                                        <td width="30%" class="textalign">User Type</td>
                                                                        <td width="5%" class="mandatory">*</td>
                                                                        <td width="65%" colspan="2" class="textfieldalign" >
                                                                            <select class="combobox" name="usertypes" id="usertypes" class="textfieldalign"  onchange="getMenus(this.value)"></select>
                                                                        </td>
                                                                </tr>-->
                                <tr class="lightrow">
                                    <td width="35%" class="textalign">User Type</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><%=usertype%></td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        <div id="menudisplay" style="width:100%;height:290px;overflow:auto;"></div>
                                        <%--=menulist--%>
<!--                                        <table width="65%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="1">
                                            <tr>
                                                <td valign="top">
                                                    <table width="100%" align="center" border="0" cellpadding="0" cellspacing="1">
                                                        <tr class="gridmenu">
                                                            <td align="center">Sno</td>
                                                            <td align="center">Menu Label Name</td>
                                                            <td align="center">Accessible</td>
                                                        </tr>
                                                        <tr class="rowColor1">
                                                            <td align="center">1</td>
                                                            <td align="center">Administrator</td>
                                                            <td align="center"><input type="checkbox" name="accessright" value="ON" /></td>
                                                        </tr>
                                                        <tr class="rowColor2">
                                                            <td align="center">1</td>
                                                            <td align="center">Administrator</td>
                                                            <td align="center"><input type="checkbox" name="accessright" value="ON" /></td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>-->
                                    </td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign">&nbsp;</td>
                                    <td width="5%" class="mandatory">&nbsp;</td>
                                    <td width="60%" class="textfieldalign">
                                        <input type="button" CLASS="submitbu" name="save" id="save" value="Submit" onclick="saveMenuAssign()">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="currencytype" id="currencytype">
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
        </form>
    </body>
</html>

<script>
     bodyOnload();
    function bodyOnload(){
        var message = '<%=menulist%>';
        if(message!=null && message!=""){
            document.getElementById("menudisplay").innerHTML=message;
        }

    }

    function getMenus(usertypeID){
        UserAction.getAssignedMenus(usertypeID,displaymenus);
    }
    function displaymenus(map){
        var chkboxes=document.forms[0].accessright;
        if(chkboxes.length>1){
            for (i=0; i<chkboxes.length;i++)
            {
                chkboxes[i].checked=false;
            }
        }

        if(map.ERROR!=null && map.ERROR!=""){
            return false;
        } else {

            //            alert(map.success);
            for (var key in map.menuMap) {
                //                alert([key, map.menuMap[key]]);
                document.getElementById(key).checked = true;
            }
        }
    }

    function saveMenuAssign(){
        var checkVal=false;
        var chkarray="";
        var chkboxes=document.forms[0].accessright;
        var usertype=document.getElementById("usertype").value;

        var count=0;

        if(chkboxes.length>1){
            for (i=0; i<chkboxes.length;i++)
            {
                if(chkboxes[i].checked)
                {
                    count=count+1;
                }
            }
            if(count==0){
                alert("select Menu");
                return false;
            }
            else{
                for (i=0;i<chkboxes.length;i++){
                    if (chkboxes[i].checked==true){
                        chkarray=chkarray+chkboxes[i].value+"-";
                    }
                }
                checkVal=true;
            }
        }
        else{
            if(document.forms[0].accessright.checked){
                chkarray=document.forms[0].accessright.value;
                checkVal=true;
            }
            else{
                alert("Select Menu");
                return false;
            }
        }
        if(checkVal){
            getBlanket('continueDIV');
            UserAction.saveMenuAssign(usertype,chkarray, resultDetails);
        }
    }
    function resultDetails(map){
        if(map.ERROR!=null && map.ERROR!=""){
            getBlanket('continueDIV');
            alert(map.ERROR);
            return false;
        } else {
            var chkboxes=document.forms[0].accessright;
            if(chkboxes.length>1){
                for (i=0; i<chkboxes.length;i++)
                {
                    chkboxes[i].checked=false;
                }
            }
            document.getElementById("usertype").value="0";
            getBlanket('continueDIV');
            alert(map.success);
        }
    }
</script>
