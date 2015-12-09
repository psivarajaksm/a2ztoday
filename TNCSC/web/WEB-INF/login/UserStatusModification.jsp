<%-- 
    Document   : UserStatusModification
    Created on : Jul 8, 2011, 10:12:22 AM
    Author     : root
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.onward.valueobjects.UserViewModel"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
            String imagePath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>
<%
            String path = request.getContextPath();
            String message = "";
            if ((request.getAttribute("message") != null)) {
                message = (String) request.getAttribute("message");
            }
            UserViewModel sesessionviewmodel = new UserViewModel();
            if ((session.getAttribute("userDetails") != null)) {
                sesessionviewmodel = (UserViewModel) session.getAttribute("userDetails");
            }
            String sessionusertype = sesessionviewmodel.getUsertype();
            String sessionUserId = sesessionviewmodel.getUserid();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script src="dwr/interface/UserAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <link type="text/css" href="<%=imagePath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=imagePath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=imagePath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>        

        <!-- Below imports for tooltip purpose-->
        <link rel="stylesheet" type="text/css" href="<%=imagePath%>script/build/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="<%=imagePath%>script/build/container/assets/skins/sam/container.css" />
        <script type="text/javascript" src="<%=imagePath%>script/build/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="<%=imagePath%>script/build/container/container-min.js"></script>


        <SCRIPT LANGUAGE="JAVASCRIPT">
            function getUserIdDetails(obj) {
                document.getElementById("message").style.display = "none";
                var name =obj.name;
                var userid=obj.value;
                var statustype        = document.forms[0].statustype.value;
                var regionid=document.getElementById('regionid').value;

                if(statustype == ""){
                    alert("<bean:message key="al_statustype" />");
                    document.forms[0].userid.value = "";
                    return false;
                }
                if(userid!=null && userid!=""){
                    if(specialCharNotAllowed(obj)){
                        UserAction.getUserIdDetails(userid,'<%=sessionusertype%>',name,regionid,setUserDetails);
                    }
                }
            }
            function setUserDetails(map) {
                emptyFields();
                emptyComboBodFields();
                emptyCheckBoxFields();
                if(map==null || map=="" || map.length<=0) {
                    alert("<bean:message key="al_useridnotvalid" />");
                    document.forms[0].userid.value = "";
                    return false;
                } else{
                    if(map.ERROR!=null && map.ERROR!=""){
                        alert(map.ERROR);
                        document.forms[0].userid.value = "";
                    } else{
                        dwr.util.setValues(map);
                        var usertypess=map.usertyprids;
                        var usertypridsize=map.usertypridsize;
                        
                        var selectobject = document.getElementById("usertype");
                        var usertypesArray=usertypess.split(",");
                    

                        for (var j = 0; j < usertypridsize; j++) {
                            for (var i = 0; i < selectobject.length; i++) {
                                if (selectobject.options[i].value == usertypesArray[j]) {
                                    selectobject.options[i].selected = true;                                    
                                }
                            }
                        }

                        /*if(map.createprofile=='Y')  document.forms[0].createprofile.checked = true;*/
                    }
                }
            }            

            function userStusUpdationDetails() {
                var userid            = document.forms[0].userid.value;
                var userstatus        = document.forms[0].userstatus.value;
                var statustype        = document.forms[0].statustype.value;
                var usertype          = document.forms[0].usertype.value;
                if(statustype == ""){
                    alert("<bean:message key="al_funtype" />");
                    return false;
                }
                if(userid == ""){
                    alert("<bean:message key="al_userid" />");
                    return false;
                }
                if(statustype=="1"){
                    if(userstatus == ""){
                        alert("<bean:message key="al_userstatus" />");
                        return false;
                    }
                } else if(statustype=="2"){                    
                    if(usertype == "0"){
                        alert("<bean:message key="al_usertype" />");
                        return false;
                    }
                }

                var answer = confirm("Do You Want to Continue?");
                if (answer){
                    disabledfieldsFalse();
                    var results="";
                    var selectobject = document.getElementById("usertype");

                    for(i=0;i<selectobject.length;i++){
                        if(selectobject.options[i].selected == true){
                            var value = selectobject.options[i].value;
                            results= results+value+",";
                        }
                    }

                    
                    //document.forms[0].usertype.value=results;
                    document.getElementById("usertypeids").value=results;
                    /*if(document.forms[0].createprofile.checked==true)   document.forms[0].createprofile.value='Y';*/                    
                    getBlanket('continueDIV');
                    document.forms[0].action="UserAction.htm";
                    document.forms[0].method.value="userStusUpdationDetails";
                    document.forms[0].submit();
                }
            }
        </SCRIPT>

        <script>
            function emptyFields(){
                document.getElementById("message").style.display = "none";
                var fields=document.getElementsByTagName("input");
                for(i=0;i<fields.length;i++) {
                    if(!(fields[i].type=="button")) {
                        fields[i].value='';
                    }
                }
            }
            function emptyComboBodFields(){
                document.getElementById("message").style.display = "none";
                var statustype = document.forms[0].statustype.value;
                var fields=document.getElementsByTagName("select");
                for(i=0;i<fields.length;i++) {
                    if(!(fields[i].type=="button")) {
                        fields[i].value='';
                        document.forms[0].statustype.value=statustype;
                    }
                }
            }
            function emptyCheckBoxFields(){
                //document.getElementById("createprofile").checked=false;
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

            function resetForm(){
                document.getElementById("message").style.display = "none";
                emptyFields();
                emptyComboBodFields();
                emptyCheckBoxFields();
            }

            function statusTypeCheck(obj){
                emptyFields();
                emptyComboBodFields();
                emptyCheckBoxFields();
                var fieldValue = obj.value;
                document.getElementById("message").style.display = 'none';
                var sessionusertype = '<%=sessionusertype%>';
                if(fieldValue=="1"){
                    document.forms[0].userid.readOnly=false;
                    document.getElementById("STATUS").style.display = '';
                    document.getElementById("RIGHTS_DIV").style.display = 'none';
                    document.getElementById("save").style.display = '';
                    document.getElementById("reset").style.display = '';
                    document.forms[0].userstatus.disabled=false;
                    document.forms[0].usertype.disabled=true;
                    document.forms[0].emailid.disabled=true;
                    document.forms[0].mobilenumber.disabled=true;

                    document.forms[0].save.value='User Status Change';
                } else if(fieldValue=="2"){
                    document.forms[0].userid.readOnly=false;
                    document.getElementById("STATUS").style.display = 'none';
                    document.getElementById("RIGHTS_DIV").style.display = 'none';
                    document.getElementById("save").style.display = '';
                    document.getElementById("reset").style.display = '';
                    document.forms[0].userstatus.disabled=true;
                    document.forms[0].usertype.disabled=false;
                    document.forms[0].emailid.disabled=false;
                    document.forms[0].mobilenumber.disabled=false;

                    document.forms[0].save.value='User Data Modification';
                } else if(fieldValue=="3"){
                    document.forms[0].userid.readOnly=false;
                    document.getElementById("STATUS").style.display = 'none';
                    document.getElementById("RIGHTS_DIV").style.display = 'none';
                    document.getElementById("save").style.display = '';
                    document.getElementById("reset").style.display = '';
                    document.forms[0].userstatus.disabled=false;
                    document.forms[0].usertype.disabled=true;
                    document.forms[0].emailid.disabled=true;
                    document.forms[0].mobilenumber.disabled=true;

                    document.forms[0].save.value='Reset Password';
                } else if(fieldValue=="4"){
                    document.forms[0].userid.readOnly=false;
                    document.getElementById("STATUS").style.display = 'none';
                    document.getElementById("RIGHTS_DIV").style.display = '';
                    document.getElementById("save").style.display = '';
                    document.getElementById("reset").style.display = '';
                    document.forms[0].userstatus.disabled=false;
                    document.forms[0].usertype.disabled=true;
                    document.forms[0].emailid.disabled=true;
                    document.forms[0].mobilenumber.disabled=true;

                    document.forms[0].save.value='User Rights Change';
                } else {
                    document.forms[0].userid.readOnly=true;
                    document.getElementById("STATUS").style.display = 'none';
                    document.getElementById("RIGHTS_DIV").style.display = 'none';
                    document.getElementById("save").style.display = 'none';
                    document.getElementById("reset").style.display = '';
                    document.forms[0].userstatus.disabled=true;
                    document.forms[0].usertype.disabled=true;
                    document.forms[0].emailid.disabled=true;
                    document.forms[0].mobilenumber.disabled=true;
                }
            }
            function CheckUserStatus(obj){
                var userstatus = obj.value;
                var presentstatus      = document.forms[0].presentstatus.value;
                var userid             = document.forms[0].userid.value;
                if(userid == ""){
                    alert("<bean:message key="al_userid" />");
                    document.forms[0].userstatus.value='';
                    return false;
                }
                if(presentstatus=="1"){
                    if(userstatus=="1"){
                        alert("<bean:message key="al_usalreadynew" />");
                        document.forms[0].userstatus.value='';
                        return false;
                    } if(userstatus=="2"){
                        alert("<bean:message key="al_usactive" />");
                        document.forms[0].userstatus.value='';
                        return false;
                    }
                } else if(presentstatus=="2"){
                    if(userstatus=="1"){
                        alert("<bean:message key="al_usnew" />");
                        document.forms[0].userstatus.value='';
                        return false;
                    } if(userstatus=="2"){
                        alert("<bean:message key="al_usalactive" />");
                        document.forms[0].userstatus.value='';
                        return false;
                    }
                } else if(presentstatus=="4"){
                    if(userstatus=="2"){
                        alert("<bean:message key="al_usdelete" />");
                        document.forms[0].userstatus.value='';
                        return false;
                    } if(userstatus=="4"){
                        alert("<bean:message key="al_usaldelete" />");
                        document.forms[0].userstatus.value='';
                        return false;
                    }
                }
            }
            function openWindow(url,width,height,glflag) {
                var statustype      = document.forms[0].statustype.value;

                if(statustype == ""){
                    alert("<bean:message key="al_statustype" />");
                    document.forms[0].userid.value = "";
                    return false;
                }
                document.forms[0].glFlag.value = glflag;
                loadwindow(url,width,height);
            }

            function getUserDetails(){
                getUserIdDetails(document.forms[0].userid);
            }

        </script>
    </head>

    <body class="yui-skin-sam" topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center">
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="<%=imagePath%>images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <div id="dwindow" style="position:absolute; left:100px; top:100px; display:none; z-index:1000;">
            <table WIDTH="100%" class="iframeheader" BORDER="0" CELLPADDING="0" CELLSPACING="0">
                <tr>
                    <td WIDTH="94%" ALIGN="CENTER"><bean:message key="searchcriter" /></td>
                    <td WIDTH="6%" ALIGN="RIGHT">
                        <img src="<%=imagePath%>images/i_maximize.png" title="Maximize" border="0" align="top" width="20" height="20" id="maxname" onClick="maximize('<%=path%>',850,550)">
                        <img src="<%=imagePath%>images/i_close.png" title="Close" border="0" align="top" width="20" height="20" onClick="closeit()">
                    </td>
                </tr>
            </table>
            <div id="dwindowcontent" class="iframeBorder">
                <iframe id="cframe" style="border:0px" src="" width="100%" height="100%"></iframe>
            </div>
        </div>
        <form METHOD="post">
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
                                    <td width="100%" colspan="4" align="center" class="headerdata"><bean:message key="userstatuschange" /></td>
                                </tr>
                            </table>
                            <table width="85%" align="center" class="tableBorder1" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" colspan="4" align="left" class="headerdata1"><bean:message key="userstatuschange" /></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="statustype" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign">
                                        <select class="combobox" name="statustype" id="statustype" onchange="statusTypeCheck(this);">
                                            <option value="">- - - - Select-One - - - -</option>
                                            <option value="1">Status Modification</option>
                                            <option value="2">Data Modification</option>
                                            <option value="3">Reset Password</option>
                                            <!--option value="4">User Rights</option-->
                                        </select>
                                    </td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign">Region</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" colspan="4" class="textfieldalign" >
                                        <select class="combobox" name="regionid" id="regionid" class="textfieldalign" ></select>
                                    </td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="userid" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign">
                                        <input type="text" CLASS="textbox" maxlength="16" name="userid" id="userid" onblur="getUserIdDetails(this);">
                                        <!--img src="<%=imagePath%>images/search.png" id="useridbutton" name="useridbutton" border="0" width="20" height="20" align="top" onclick="openWindow('UserCreationAction.htm?method=searchPage',850,550,'SMuserid');"-->
                                    </td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="username" /></td>
                                    <td width="5%" class="mandatory">&nbsp;</td>
                                    <td width="60%" class="textfieldalign"><input type="text" CLASS="textbox" name="username" id="username"  maxlength="30" disabled></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="gender" /></td>
                                    <td width="5%" class="mandatory">&nbsp;</td>
                                    <td width="60%" class="textfieldalign"><select name="gender" id="gender" CLASS="combobox" disabled></select></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="dob" /></td>
                                    <td width="5%" class="mandatory">&nbsp;</td>
                                    <td width="60%" class="textfieldalign"><input type="text" class="textbox1" style="width:35mm;" name="dateofbirth" id="dateofbirth" disabled/></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="email" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" CLASS="textbox" name="emailid" id="emailid"  maxlength="50" onblur="validateEmailid(this);" disabled></td>
                                </tr>
                                <tr class="lightrow">
                                    <td width="35%" class="textalign"><bean:message key="mobility" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><input type="text" CLASS="textbox" name="mobilenumber" id="mobilenumber"  maxlength="15" disabled></td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="35%" class="textalign"><bean:message key="usertype" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign">                                        
                                        <select name="usertype" id="usertype" multiple="multiple" CLASS="multiplecombobox" ></select></td>
<!--                                    <td width="60%" class="textfieldalign"><select name="usertype" id="usertype" multiple="multiple" CLASS="multiplecombobox" disabled></select></td>-->
                                </tr>
                                <tr class="lightrow" ID="STATUS" style="display:none">
                                    <td width="35%" class="textalign"><bean:message key="modifiedstatus" /></td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="60%" class="textfieldalign"><select name="userstatus" id="userstatus" class="combobox" disabled onchange="CheckUserStatus(this);"></select></td>
                                </tr>
                            </table>
                            <table width="95%" ID="RIGHTS_DIV" STYLE="display:none;" align="center" class="tableBorder1" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="headerdata" width="60%" class="textfieldalign" COLSPAN="2"><bean:message key="userrights" />&nbsp;&nbsp;&nbsp;</td>
                                    <td width="60%" class="textfieldalign" COLSPAN="3">&nbsp;</td>
                                </tr>
                                <tr CLASS="lightrow">
                                    <td WIDTH="20%"><input type="checkbox" name="createprofile" id="createprofile">Registration</td>
                                    <td WIDTH="20%"><input type="checkbox" name="renewal" id="renewal">Renewal</td>
                                    <td WIDTH="20%"><input type="checkbox" name="qualification" id="qualification">Additional Qualification</td>
                                    <td WIDTH="20%"><input type="checkbox" name="updateprofile" id="updateprofile">Change of Address</td>
                                    <td WIDTH="20%"><input type="checkbox" name="experience" id="experience">Experience</td>
                                </tr>
                            </table><br>
                            <table WIDTH="80%" ALIGN="CENTER" BORDER="0" CELLSPACING="0" CELLPADDING="0">
                                <tr>
                                    <td ALIGN="CENTER">
                                        <input type="button" CLASS="submitbutton" name="save" id="save" value="Save" style="display:none;" onclick="userStusUpdationDetails()">
                                        <input type="button" CLASS="submitbutton" name="reset" id="reset" value="Reset" style="display:none;" onclick="resetForm()">
                                    </td>
                                </tr>
                            </table><br>
                            <table WIDTH="80%" ALIGN="CENTER" BORDER="0" CELLSPACING="0" CELLPADDING="0">
                                <tr ID="message">
                                    <td ALIGN="CENTER" COLSPAN="4"><font color="red" size="4px" face="Arial, Helvetica, sans-serif"><%=message%></font></td>
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
            <input type="hidden" name="glFlag">
            <input type="hidden" name="usertypeids" id="usertypeids" value="">
            <input type="hidden" name="presentstatus" id="presentstatus">
        </form>
    </body>
    <script>
        bodyOnload();
        function bodyOnload(){
            displayid = "gender,userstatus";
            parentcodes = "2,10" ;
            UserAction.loadRegionDetails(fillRegionValues);
            UserAction.loadApplicationReferenceCodes(displayid,parentcodes,fillComboBoxValues);
        }
        function fillRegionValues(map){
            dwr.util.removeAllOptions("regionid");
            dwr.util.addOptions("regionid",map.regionlist);
        }
        function fillComboBoxValues(map){
            dwr.util.removeAllOptions("gender");
            dwr.util.addOptions("gender",map.gender);

            dwr.util.removeAllOptions("userstatus");
            dwr.util.addOptions("userstatus",map.userstatus);
            UserAction.getUsertypes(fillusertypes);
            //            setTimeout('sessionUserTypeChecking()', 1000);
        }
        function fillusertypes(map){
            dwr.util.removeAllOptions("usertype");
            dwr.util.addOptions("usertype",map.usertypes);
        }
        function sessionUserTypeChecking(){
            /*var message = '<%=message%>';
            if(message!=null && message!=""){
                alert(message);
                document.getElementById("message").style.display = "none";
            }*/
            var sessionusertype = '<%=sessionusertype%>';
            if(sessionusertype=="C"){
                document.getElementById("useridbutton").style.display = 'none';

                document.forms[0].statustype.value = "2";
                statusTypeCheck(document.forms[0].statustype);
                document.forms[0].userid.value = '<%=sessionUserId%>';

                document.forms[0].userid.disabled=true;
                document.forms[0].statustype.disabled=true;
                document.forms[0].usertype.disabled=true;

                getUserIdDetails(document.forms[0].userid);
            }
        }
    </script>
</html>