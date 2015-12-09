<%-- 
    Document   : ChangePassword
    Created on : May 19, 2011, 10:31:45 AM
    Author     : Jagan Mohan. B
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
    String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>

<%
    String path = request.getContextPath();
    String message = "";
    if((request.getAttribute("message")!=null))
        message = (String) request.getAttribute("message");
    request.removeAttribute("message");
    String NORMAL_FORCE = "";
    if((request.getAttribute("NORMAL_FORCE")!=null))
        NORMAL_FORCE = (String) request.getAttribute("NORMAL_FORCE");
    request.removeAttribute("NORMAL_FORCE");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script src="dwr/interface/UserAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <style type="text/css">
            body {
                font: 11px Verdana, Arial, Helvetica, sans-serif;
                margin:0px;
                background:#FFFFFF;
                padding:0px;
            }
            #CHANGE_PASSWORD_DIV {
                position:absolute;
                width:800px;
                height:380px;
                margin-top:-125px;
                margin-left:-290px;
                z-index:9002;
                padding:10px;
                overflow:auto;
                border: 8px outset #1a64ee;
                padding:0px 0px 0px 0px;
            }
        </style>

        <script>
            function confirmPasswordCheck(obj){
                if(obj.value!=document.forms[0].password.value){
                    alert("<bean:message key="al_confpasssame" />");
                    obj.value="";
                    return false;
                }
                return true;
            }

            function changeYourPassword(){
                //var emailid = document.forms[0].emailid.value;
                var currentpassword = document.forms[0].currentpassword.value;
                var password = document.forms[0].password.value;
                var confirmpassword = document.forms[0].confirmpassword.value;

                /*if(emailid==null || emailid==""){
                    alert("\"Email id\" field is mandatory");  return false;
                } else */
                if(currentpassword==null || currentpassword==""){
                    alert("<bean:message key="al_currpass" />");  return false;
                } else if(password==null || password==""){
                    alert("<bean:message key="al_19" />");  return false;
                } else if(confirmpassword==null || confirmpassword==""){
                    alert("<bean:message key="al_20" />");  return false;
                } else if(currentpassword==password){
                    alert("<bean:message key="al_newpassmach" />");   return false;
                } else if(confirmpassword!=password){
                    alert("<bean:message key="al_confpasssame" />");   return false;
                }
                var answer = confirm("Do You Want to Change Your Password?");
                if (answer){
                    getBlanket('continueDIV');
                    document.forms[0].action="UserAction.htm";
                    document.forms[0].method.value="changeYourPassword";
                    document.forms[0].submit();
                }
            }
            function existChangePassword(flag){
                if(flag=='F')
                    logOut();
                else if(flag=='N')
                    homePage();
                else
                    logOut();
            }
        </script>
    </head>

    <body>
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="<%=staticPath %>images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="left-sidebar">
                <%@ include file="/common/leftpage.jsp" %>
            </div>
            <div id="content">
                <div id="CHANGE_PASSWORD_DIV" style="display:none; background-color:#FFFFFF;" align="center">
                    <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                        <tr>
                            <td>                                
                                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="95%"  align="center" class="headerdata"><bean:message key="changepass" /></td>
                                        <td WIDTH="5%" ALIGN="RIGHT"><img src="<%=staticPath %>images/close.png" name="close" id="close" onclick="existChangePassword('<%=NORMAL_FORCE%>');" title="Close" ></td>
                                    </tr>
                                </table>
                                <table width="85%" align="center" class="tableBorder1" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="100%" colspan="3" align="left" class="headerdata1"><bean:message key="changepass" /></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" align="center" class="lightrow"><bean:message key="head1" /></td>
                                    </tr>
                                    <tr class="darkrow">
                                        <td width="35%" class="textalign"><bean:message key="currpassword" /></td>
                                        <td width="5%" class="mandatory">*</td>
                                        <td width="60%" class="textfieldalign"><input type="password" class="textbox" name="currentpassword" id="currentpassword" maxlength="515" onblur="passwordCheck(this);"></td>
                                    </tr>
                                    <tr class="lightrow">
                                        <td width="35%" class="textalign"><bean:message key="password" /></td>
                                        <td width="5%" class="mandatory">*</td>
                                        <td width="60%" class="textfieldalign" >
                                            <input type="password" class="textbox" name="password" id="password" onkeyup="updatestrength(this.value);" maxlength="15" onblur="passwordCheck(this);">&nbsp;&nbsp;&nbsp;
                                            <img src="<%=staticPath %>images/tooshort.png" name="strength" id="strength" style="vertical-align:middle;" border="0" width="190" height="25" />
                                        </td>
                                    </tr>
                                    <tr class="darkrow">
                                        <td width="35%" class="textalign"><bean:message key="confpass" /></td>
                                        <td width="5%" class="mandatory">*</td>
                                        <td width="60%" class="textfieldalign"><input type="password" CLASS="textbox" maxlength="15" name="confirmpassword" id="confirmpassword" maxlength="15" onblur="confirmPasswordCheck(this);"></td>
                                    </tr>
                                    <tr>
                                        <td class="lightrow" colspan="3" align="center">
                                            <input type="button" CLASS="submitbutton" name="save" id="save" value="Submit" onclick="changeYourPassword()">
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script>
        bodyOnload();
        function bodyOnload(){
            var message = '<%=message%>';
            if(message!=null && message!=""){
                alert(message);
            }
            getBlanket('CHANGE_PASSWORD_DIV');
        }
    </script>
</html>
