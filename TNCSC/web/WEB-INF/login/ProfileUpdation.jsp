<%-- 
    Document   : ProfileUpdation
    Created on : Oct 14, 2011, 1:59:26 PM
    Author     : Jagan Mohan. B
--%>

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
    if((request.getAttribute("message")!=null))
        message = (String) request.getAttribute("message");
    request.removeAttribute("message");
    UserViewModel sesessionviewmodel = new UserViewModel();
    if((session.getAttribute("userDetails")!=null))
        sesessionviewmodel = (UserViewModel) session.getAttribute("userDetails");
    String sessionusertype = sesessionviewmodel.getUsertype();
    String sessionUserId = sesessionviewmodel.getUserid();
    sesessionviewmodel = null;

    String adminUser = ApplicationConstants.ADMIN_USER;
    String supperUser = ApplicationConstants.SUPER_USER;
%>

<html>
    <head>
        <script src="dwr/interface/UserAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <script>
            function emptyFields(){
                var todaydate = document.forms[0].todaydate.value;
                var fields=document.getElementsByTagName("input");
                for(i=0;i<fields.length;i++) {
                    if(!(fields[i].type=="button")) {
                        fields[i].value = "";
                    }
                }
                var fields1=document.getElementsByTagName("select");
                for(i=0;i<fields1.length;i++) {
                    if(!(fields1[i].type=="button")) {
                        fields1[i].value = "";
                    }
                }

                document.forms[0].todaydate.value=todaydate;
            }
            
            function disabledfieldsFalse(){
                var fields=document.getElementsByTagName("input");
                for(var i=0;i<fields.length;i++){
                    if(fields[i].type!="button"){
                        fields[i].disabled=false;
                    }
                }
                var fields=document.getElementsByTagName("select");
                for(var i=0;i<fields.length;i++){
                    if(fields[i].type!="button"){
                        fields[i].disabled=false;
                    }
                }
            }

            function updateProfileDetails(){
                var userid = document.forms[0].userid.value;
                var username = document.forms[0].username.value;
                var dateofbirth = document.forms[0].dateofbirth.value;
                var emailid = document.forms[0].emailid.value;
                var mobilenumber = document.forms[0].mobilenumber.value;
                var address = document.forms[0].address.value;
                var secretquestionone = document.forms[0].secretquestionone.value;
                var youranswerone = document.forms[0].youranswerone.value;
                var secretquestiontwo = document.forms[0].secretquestiontwo.value;
                var youranswertwo = document.forms[0].youranswertwo.value;

                if(username==null || username==""){
                    alert("<bean:message key="al_8" />");  return false;
                } else if(dateofbirth==null || dateofbirth==""){
                    alert("<bean:message key="al_9" />");  return false;
                } else if(emailid==null || emailid==""){
                    alert("<bean:message key="al_10" />");  return false;
                } else if(mobilenumber==null || mobilenumber==""){
                    alert("<bean:message key="al_11" />");  return false;
                } else if(address==null || address==""){
                    alert("<bean:message key="al_12" />");  return false;
                } else if(userid==null || userid==""){
                    alert("<bean:message key="al_18" />");  return false;
                } else if(secretquestionone==null || secretquestionone==""){
                    alert("<bean:message key="al_1" />");  return false;
                } else if(youranswerone==null || youranswerone==""){
                    alert("<bean:message key="al_2" />");  return false;
                } else if(secretquestiontwo==null || secretquestiontwo==""){
                    alert("<bean:message key="al_3" />");  return false;
                } else if(youranswertwo==null || youranswertwo==""){
                    alert("<bean:message key="al_4" />");  return false;
                }
                var answer = confirm("Do You Want to Continue?");
                if (answer){
                    disabledfieldsFalse();
                    getBlanket('continueDIV');
                    document.forms[0].action="UserAction.htm";
                    document.forms[0].method.value="updateProfileDetails";
                    document.forms[0].submit();
                }
            }
        </script>
        <script>

            function getProfileUpdateUserData(obj){
                var userid = obj.value;
                if(userid.length < 4){
                    alert("Your user id must be atleast 4 Characters in length");
                    obj.value = "";
                    emptyFields();
                    return false;
                }
                
                UserAction.getProfileUpdateUserData(userid,function setAccountValues(map){                    
                    if(map==null || map=="" || map.length<=0){
                        alert("<bean:message key="al_6" />");
                        emptyFields();
                        document.forms[0].userid.value = "";
                        return false;
                    } else{
                        if(map.ERROR!=null && map.ERROR!=""){
                            alert(map.ERROR);
                            emptyFields();
                            document.forms[0].userid.value = "";
                            return false;
                        } else {
                            dwr.util.setValues(map);
                        }
                    }
                });
            }
        </script>
    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center">
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
                <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" class="headerdata">Update Profile</td>
                                </tr>
                            </table>
                            <table width="85%" class="tableBorder1" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" colspan="3" align="left" class="headerdata1"><bean:message key="personaldet" /></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="userid" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox" name="userid" id="userid" onblur="getProfileUpdateUserData(this)"></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="title" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><select class="combobox" name="salutationtype" id="salutationtype"></select></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="gender" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><select class="combobox" name="gender" id="gender"></select></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="firstname" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox3" name="username" id="username" maxlength="50"></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="dob" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign">
                                        <input type="text" class="datetextbox" name="dateofbirth" id="dateofbirth" maxlength="10" onkeyup="formatDate(this);" onblur="isValidDate(this); minorValidateDate(this);">
                                        <img src="<%=staticPath %>images/calpicker.png" name="dateofbirthimage" id="dateofbirthimage" border="0" width="20" height="19" align="top" onClick="scwShow(document.getElementById('dateofbirth'),event,'dateofbirth');return false;">&nbsp;[dd/mm/yyyy]
                                    </td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="email" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox3" name="emailid" id="emailid" maxlength="50" onblur="validateEmailid(this);"></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="phonenumber" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox" name="mobilenumber" id="mobilenumber" maxlength="15" onblur="isNumber(this);" ></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="address" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox3" name="address" id="address" maxlength="1000"></td>
                                </tr>
                            </table>
                            <table width="85%" id="SECURITY_DIV" class="tableBorder1" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr class="darkrow">
                                    <td width="100%" colspan="4" align="left" class="headerdata1"><bean:message key="secdet" /></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="secque1" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><select class="combobox1" name="secretquestionone" id="secretquestionone"></select></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="answer" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox3" name="youranswerone" id="youranswerone" maxlength="50"></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="secque2" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><select class="combobox1" name="secretquestiontwo" id="secretquestiontwo"></select></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="answer" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox3" name="youranswertwo" id="youranswertwo" maxlength="50"></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign">&nbsp;</td>
                                    <td width="5%" class="mandatory">&nbsp;</td>
                                    <td width="60%" class="textfieldalign">
                                        <input type="button" CLASS="submitbu" name="save" id="save" value="Submit" onclick="updateProfileDetails()">
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
    <script>
        bodyOnload();
        function bodyOnload(){
            var message = '<%=message%>';
            if(message!=null && message!=""){
                alert(message);
            }

            var displayid = "salutationtype,secretquestionone,secretquestiontwo,gender";
            var parentcodes = "1,7,8,2" ;
            UserAction.loadApplicationReferenceCodes(displayid,parentcodes,{callback:fillComboBoxValues,async:false});
        }
        function fillComboBoxValues(map){
            dwr.util.removeAllOptions("salutationtype");
            dwr.util.addOptions("salutationtype",map.salutationtype);

            dwr.util.removeAllOptions("secretquestionone");
            dwr.util.addOptions("secretquestionone",map.secretquestionone);

            dwr.util.removeAllOptions("secretquestiontwo");
            dwr.util.addOptions("secretquestiontwo",map.secretquestiontwo);

            dwr.util.removeAllOptions("gender");
            dwr.util.addOptions("gender",map.gender);

//            dwr.util.removeAllOptions("usertype");
//            dwr.util.addOptions("usertype",map.usertype);
            UserAction.getUsertypes(fillusertypes);
            setTimeout('sessionUserTypeChecking()', 500);
        }

        function fillusertypes(map){
            dwr.util.removeAllOptions("usertype");
            dwr.util.addOptions("usertype",map.usertypes);
        }
        function sessionUserTypeChecking(){
            var sessionusertype = '<%=sessionusertype%>';
            var adminUser = '<%=adminUser%>';
            var supperUser = '<%=supperUser%>';
            if(sessionusertype!=adminUser && sessionusertype!=supperUser){
                document.forms[0].userid.value = '<%=sessionUserId%>';
                document.forms[0].userid.disabled=true;

                getProfileUpdateUserData(document.forms[0].userid);
            }
        }

    </script>
</html>
