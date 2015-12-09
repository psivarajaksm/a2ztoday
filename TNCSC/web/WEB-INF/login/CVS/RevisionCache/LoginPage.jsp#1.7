<%-- 
    Document   : LoginPage
    Created on : Jul 6, 2012, 11:12:45 AM
    Author     : root
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    String loginStaticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
    System.out.println("loginStaticPath====" + loginStaticPath);
%>

<!--<link rel="shortcut icon" href="<%=loginStaticPath%>images/onwardlogo.gif" type="image/x-icon">-->
<%
    String path = request.getContextPath();
    String message = "";
    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");
    }
    request.removeAttribute("message");
%>
<html>
    <head>
        <title><bean:message key="projecttitle" /></title>
        <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <META name="GENERATOR" content="IBM Software Development Platform">
        <META http-equiv="Content-Type" content="text/html; charset=iso-8859-6">
        <META http-equiv="Content-Style-Type" content="text/css">        
        <link href="<%=loginStaticPath%>css/onward.css" rel="stylesheet" type="text/css" />
        <link href="<%=loginStaticPath%>css/search.css" rel="stylesheet" type="text/css" />
        <link href="<%=loginStaticPath%>css/global.css" rel="stylesheet" type="text/css" />

        <script src="<%=loginStaticPath%>scripts/blanket.js"></script>
        <script src="<%=loginStaticPath%>scripts/common.js"></script>
        <script src="<%=loginStaticPath%>scripts/global.js"></script>
        <script src="<%=loginStaticPath%>scripts/main.js"></script>

        <script src="<%=loginStaticPath%>scripts/tooltip.js"></script>
        <script src="<%=loginStaticPath%>scripts/cal.js"></script>
        <script src="<%=loginStaticPath%>scripts/dateValidations.js"></script>
        <script src="dwr/interface/LoginAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <style type="text/css">
            body {
                font: 11px Verdana, Arial, Helvetica, sans-serif;
                margin:0px;
                background:#DEEFFD;
                padding:0px;
            }
            #FORGOT_PASSWORD_DIV {
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
            function goToLogin(actionName){
                if (actionName == "ValidateUser") {
                    var userid      = document.forms[0].userid.value;
                    var password    = document.forms[0].password.value;
                    if ( (userid==null || userid=="") && (password==null || password=="") ){
                        alert("<bean:message key="al_useridpassword" />");
                    } else if(userid==null || userid=="") {
                        alert("<bean:message key="al_userid" />");
                    } else if(password==null || password=="") {
                        alert("<bean:message key="al_password" />");
                    } else if(password.length < 8) {
                        alert("Error: Password must contain at least Eight characters!");
                    } else{
                        getBlanket('continueDIV');
                        document.forms[0].action="LoginAction.htm";
                        document.forms[0].method.value="goToLogin";
                        document.forms[0].submit();
                    }
                }
            }
            function forgotPasswordPage(){
                getBlanket('FORGOT_PASSWORD_DIV');
                document.forms[0].forgotuserid.value = "";
                document.forms[0].dateofbirth.value = "";
                document.forms[0].emailid.value = "";
                document.forms[0].secretquestionone.value = "";
                document.forms[0].youranswerone.value = "";
                document.forms[0].secretquestiontwo.value = "";
                document.forms[0].youranswertwo.value = "";
                document.forms[0].newpassword.value = "";
                document.forms[0].confirmpassword.value = "";
                document.forms[0].forgotuserid.focus();
            }

            function confirmPasswordCheck(obj){
                if(obj.value!=document.forms[0].newpassword.value){
                    alert("<bean:message key="al_5" />");
                    obj.value="";
                    return false;
                }
                return true;
            }
            function submitForgotPassword(actionName){
                if (actionName == "ValidateForgotPassword") {
                    var forgotuserid        = document.forms[0].forgotuserid.value;
                    var dateofbirth         = document.forms[0].dateofbirth.value;
                    var emailid             = document.forms[0].emailid.value;

                    var secretquestionone   = document.forms[0].secretquestionone.value;
                    var youranswerone       = document.forms[0].youranswerone.value;
                    var secretquestiontwo   = document.forms[0].secretquestiontwo.value;
                    var youranswertwo       = document.forms[0].youranswertwo.value;

                    var newpassword         = document.forms[0].newpassword.value;
                    var confirmpassword     = document.forms[0].confirmpassword.value;

                    if(forgotuserid==null || forgotuserid=="") {
                        alert("<bean:message key="al_userid" />");
                    } else if(dateofbirth==null || dateofbirth==""){
                        alert("<bean:message key="al_9" />");  return false;
                    } else if(emailid==null || emailid==""){
                        alert("<bean:message key="al_10" />");  return false;
                    } else if(secretquestionone==null || secretquestionone==""){
                        alert("<bean:message key="al_1" />");  return false;
                    } else if(youranswerone==null || youranswerone==""){
                        alert("<bean:message key="al_2" />");  return false;
                    } else if(secretquestiontwo==null || secretquestiontwo==""){
                        alert("<bean:message key="al_3" />");  return false;
                    } else if(youranswertwo==null || youranswertwo==""){
                        alert("<bean:message key="al_4" />");  return false;
                    } else if(newpassword==null || newpassword=="") {
                        alert("<bean:message key="al_password" />");
                    } else if(newpassword.length < 8) {
                        alert("Error: Password must contain at least Eight characters!");
                    } else if(confirmpassword!=newpassword){
                        alert("<bean:message key="al_5" />");
                    } else{
                        getBlanket('FORGOT_PASSWORD_DIV');
                        getBlanket('continueDIV');
                        document.forms[0].action="LoginAction.htm";
                        document.forms[0].method.value="submitForgotPassword";
                        document.forms[0].submit();
                    }
                }
            }
        </script>

    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center">
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post">
            <div class="mainDiv">
                <div class="headerDiv">
                    <!--                    <div class="headerDivleft"></div>
                                        <div class="headerDivright"></div>-->
                </div>
                <div class="marque">
                    <marquee behavior="scroll" align="middle" dir="ltr" style="padding-top:3px">
                        <font style="color:#CC0000; font-weight:bold; font-family:Calibri; font-size:14px">Welcome to 
                            <span style="color:#49384d">Tamilnadu Civil Supplies Corporation</span>
                        </font>
                    </marquee>
                </div>
                <div class="bodyDiv">
                    <div class="loginDiv">
                        <div class="loginDivtop"></div>
                        <div class="loginDivbody" align="center">
                            <table width="88%" class="loginTable" border="0" cellpadding="2" cellspacing="2">
                                <tr>
                                    <td width="50%"><bean:message key="username" /></td>
                                    <td width="50%">
                                        <input type="text" class="loginTxtBox" name="userid" id="userid" onkeyup="userIdClearText(this);" maxlength="16" />
                                        <!--                                        <input type="text" class="text_box" name="userid" id="userid" onkeyup="userIdClearText(this);" maxlength="16" />-->
                                    </td>
                                </tr>
                                <tr>
                                    <td><bean:message key="password" /></td>
                                    <td>
                                        <input type="password" class="loginTxtBox" name="password" id="password" maxlength="15" onblur="passwordCheck(this)" />
                                        <!--                                        <input type="password" class="text_box" name="password" id="password" maxlength="15" onblur="//passwordCheck(this)" />-->
                                    </td>
                                </tr>
                                <tr>
                                    <td><div align="right">
                                            <input type="button" class="loginButtonBox" name="loginbutton" id="loginbutton" value="Login" onclick="goToLogin('ValidateUser')">
                                            <!--                                            <input type="button" class="submitbutton" name="loginbutton" id="loginbutton" value="Login" onclick="//goToLogin('ValidateUser')">-->                                            
                                        </div>
                                    </td>
                                    <td>
                                        <input type="button" class="loginButtonBox" name="forgotbutton" id="forgotbutton" value="Forgot Password" onclick="forgotPasswordPage()">
                                        <!--                                        <input type="button" class="submitbutton" name="forgotbutton" id="forgotbutton" value="Forgot Password" onclick="forgotPasswordPage()">-->
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="center">
                                        <font class="errorLogin">
                                            <%=message%>
                                        </font>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="loginDivbottom"></div>				
                    </div>		
                </div>
                <div class="footerDiv">
                    <div class="footerDivreserved">© Copyright 2013-14 Tamil Nadu Civil Supplies Corporation.&nbsp;&nbsp; <font style="color:#FFFFFF">All rights Reserved.</font></div>
                    <div class="footerDivPoweredby">Powered by</div>
                    <div class="footerDivImg"><img src="<%=loginStaticPath%>images/onward_logo.png" width="86" height="34" /></div>	
                </div>
            </div>

            <!--            <table align="center" width="100%" border="0"  cellpadding="1" cellspacing="0">
                            <tr>
                                <td>
                                    <table align="center" width="100%" border="0" cellpadding="1" cellspacing="0">
                                        <tr>
                                            <td  align="left">                                    
                                                <img src="images/isoquant.png"  class="hederReqBlanket" border="0">                                   
                                            </td>
                                            <td align="center" width="35%" valign="middle">
                                                <table width="90%" border="0" class="loginTableBorder" cellpadding="6" cellspacing="1">
                                                    <tbody>
                                                        <tr>
                                                            <td align="center"><b><font face="arial">Login to the System</font></b><br>
                                                                <font face="arial" size="-1">
                                                                    <nobr>Enter your User ID and password to login&nbsp;</nobr>
                                                                </font>
                                                                <table border="0" cellpadding="25" cellspacing="0">
                                                                    <tbody>
                                                                        <tr>
                                                                            <td align="right">
                                                                                <table border="0" cellpadding="2" cellspacing="0">
                                                                                    <tbody>
                                                                                        <tr>
                                                                                            <td width="50%" ><span class="Fieldset_title"><%--<bean:message key="username" />--%></span></td>
                                                                                            <td width="50%" align="left"><label><input type="text" class="text_box" name="userid" id="userid" onkeyup="userIdClearText(this);" maxlength="16" /></label></td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td width="50%" class="Fieldset_title"><%--<bean:message key="password" />--%></td>
                                                                                            <td width="50%" align="left"><input type="password" class="text_box" name="password" id="password" maxlength="15" onblur="passwordCheck(this)" /></td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td colspan="2" align="center">
                                                                                                <input type="button" class="submitbutton" name="loginbutton" id="loginbutton" value="Login" onclick="goToLogin('ValidateUser')">&nbsp;
                                                                                                <input type="button" class="submitbutton" name="forgotbutton" id="forgotbutton" value="Forgot Password" onclick="forgotPasswordPage()">
                                                                                            </td>
                                                                                        </tr>
                                                                                    </tbody>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </tbody>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="center" valign="top"><font face="arial" size="-1">
                                                                    <a href="javascript:alert('Type%20your%20ID%20and%20Password%20in%20the%20provided%20input%20box%20and%20click%20the%20\'Log%20In\'%20button!')"><font color="%000000;">How to Log In?</font></a></font>
                                                            </td>
                                                        </tr>
                                                        <tr align="center">
                                                            <td class="errormsg"><%--=message--%></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>-->
            <div id="FORGOT_PASSWORD_DIV" style="display:none; background-color:#FFFFFF;" align="center">
                <table width="98%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td WIDTH="95%" ALIGN="CENTER" colspan="3">Forgot Password</td>
                                    <td WIDTH="5%" ALIGN="RIGHT"><img src="<%=loginStaticPath%>images/close.png" name="close" id="close" onclick="getBlanket('FORGOT_PASSWORD_DIV');" title="Close" ></td>
                                </tr>
                            </table>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr><td height="10" style="background:#1a64ee"></td></tr>
                                <tr><td height="10"></td></tr>
                            </table>
                            <table width="95%" align="center" class="tableBorder2" border="0" cellpadding="3" cellspacing="0">
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="userid" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="text" class="textbox" name="forgotuserid" id="forgotuserid" onkeyup="userIdClearText(this);"></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="dob" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="text" class="datetextbox" name="dateofbirth" id="dateofbirth" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this); minorValidateDate(this);"></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="email" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="text" class="textbox3" name="emailid" id="emailid" maxlength="50" onblur="validateEmailid(this);"></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="secque1" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><select class="combobox1" name="secretquestionone" id="secretquestionone"></select></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="answer" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="text" class="textbox3" name="youranswerone" id="youranswerone" maxlength="50"></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="secque2" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><select class="combobox1" name="secretquestiontwo" id="secretquestiontwo"></select></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="answer" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="text" class="textbox3" name="youranswertwo" id="youranswertwo" maxlength="50"></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="password" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="password" class="textbox" name="newpassword" id="newpassword" maxlength="15" onblur="passwordCheck(this);"></td>
                                </tr>
                                <tr>
                                    <td width="40%" class="darkrow"><bean:message key="confirmpassword" /></td>
                                    <td width="5%" class="lightrow">&nbsp;</td>
                                    <td width="55%" class="lightrow"><input type="password" CLASS="textbox" maxlength="15" name="confirmpassword" id="confirmpassword" maxlength="15" onblur="confirmPasswordCheck(this);"></td>
                                </tr>
                                <tr class="lightrow"><td colspan="3">&nbsp;</td></tr>
                                <tr>
                                    <td WIDTH="100%" COLSPAN="3" ALIGN="CENTER">
                                        <input type="button" class="submitbu" value="Submit" tabindex="26" onclick="submitForgotPassword('ValidateForgotPassword');">&nbsp;&nbsp;&nbsp;
                                        <input type="button" class="submitbu" value="Exit" tabindex="27" onclick="getBlanket('FORGOT_PASSWORD_DIV');">
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <%
        //System.out.println("  sessionid IN LOGIN PAGE AFTER : " + session.getId());
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,no-store");
        response.addHeader("Cache-Control", "pre-check=0,post-check=0");
        response.setDateHeader("Expires", -1);
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
%>
    <script>
        bodyOnLoad();
        function bodyOnLoad(){
            var displayid = "secretquestionone,secretquestiontwo";
            var parentcodes = "7,8" ;
            LoginAction.loadApplicationReferenceCodes(displayid,parentcodes,fillComboBoxValues);

            document.forms[0].userid.focus();
            pauseClearAllCookies();
        }
        function fillComboBoxValues(map){
            dwr.util.removeAllOptions("secretquestionone");
            dwr.util.addOptions("secretquestionone",map.secretquestionone);

            dwr.util.removeAllOptions("secretquestiontwo");
            dwr.util.addOptions("secretquestiontwo",map.secretquestiontwo);
        }


        function pauseClearAllCookies(){
            setTimeout("checkCookies();",1000);
        }

        var cookie_counter = 0;
        var cookieList;

        function checkCookies(){
            if(document.cookie.indexOf(";") != -1){
                cookieList = document.cookie.split(";");
            }else{
                cookieList = [document.cookie];
            }
            setTimeout("clearAllCookies();",1000);
        }

        function clearAllCookies(){
            if(cookie_counter < cookieList.length ){

                var cookieName = "";

                if(cookieList[cookie_counter].indexOf("=") != -1){
                    cookieName = cookieList[cookie_counter].split("=")[0];
                }else{
                    cookieName = cookieList[cookie_counter];
                }

                // clear js cookies
                Delete_Cookie(cookieName, '/', document.domain);

                // clear server cookies
                Delete_Cookie(cookieName, '/', '');

                // increment counter
                cookie_counter++;

                //recall the function
                setTimeout("clearAllCookies();",800);
            }
        }

        function Delete_Cookie(name,path,domain) {
            var cookie_date = new Date ();
            cookie_date.setTime (cookie_date.getTime()-1);
            if (Get_Cookie(name)) document.cookie = name + "=" +
                ( (path) ? ";path=" + path : "") +
                ( (domain) ? ";domain=" + domain : "") +
                ";expires="+cookie_date.toGMTString();
            //document.cookie = cookie_name += "=; expires=" + cookie_date.toGMTString();
        }

        function Get_Cookie(check_name) {
            // first we'll split this cookie up into name/value pairs
            // note: document.cookie only returns name=value, not the other components
            var a_all_cookies = document.cookie.split( ';' );
            var a_temp_cookie = '';
            var cookie_name = '';
            var cookie_value = '';

            for (i = 0; i < a_all_cookies.length; i++){
                // now we'll split apart each name=value pair
                a_temp_cookie = a_all_cookies[i].split( '=' );

                // and trim left/right whitespace while we're at it
                cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');

                // if the extracted name matches passed check_name
                if (cookie_name == check_name) {
                    // we need to handle case where cookie has no value but exists (no = sign, that is):
                    if (a_temp_cookie.length > 1){
                        cookie_value = unescape(a_temp_cookie[1].replace(/^\s+|\s+$/g, ''));
                    }
                    // note that in cases where cookie is initialized but no value, null is returned
                    return cookie_value;
                    break;
                }
                a_temp_cookie = null;
                cookie_name = '';
            }
        }
    </script>
</html>
