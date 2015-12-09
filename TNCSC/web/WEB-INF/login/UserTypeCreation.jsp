<%-- 
    Document   : UserTypeCreation
    Created on : Oct 18, 2012, 3:11:42 PM
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
        <title>Employee Earnings Slap Details</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="dwr/interface/UserTypeAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {

                $("#savebutton").click(function(){                   
                    var funtype=document.getElementById('funtype').value;                    
                    var usertype=document.getElementById('usertype').value;                   
                   

                    if(funtype=="0"){
                        alert("Please Select the function Type");
                        document.getElementById('funtype').focus();
                        return false;
                    }else if(funtype=="1"){                        
                        if(usertype==""){
                            alert("Please Select the User Type");
                            document.getElementById('usertype').focus();
                            return false;
                        }else{
                            var answer = confirm("Do You Want to Continue?");
                            if (answer){
                                getBlanket('continueDIV');
                                var usertypes="0";
                                UserTypeAction.saveUserType(funtype,usertypes,usertype,saveUsertypeDetails);
                            }
                        }
                    }else if(funtype=="2"){
                        var usertypes=document.getElementById('usertypes').value;
                        if(usertypes=="0"){
                            alert("Please Select the User Type");
                            document.getElementById('usertypes').focus();
                            return false;
                        }else if(usertype==""){
                            alert("Please Select the User Type");
                            document.getElementById('usertype').focus();
                            return false;
                        }else{
                            var answer = confirm("Do You Want to Continue?");
                            if (answer){
                                getBlanket('continueDIV');
                                UserTypeAction.saveUserType(funtype,usertypes,usertype,saveUsertypeDetails);
                            }
                        }
                        
                    }
                });
            });
            function saveUsertypeDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    alert(map.success);
                    getBlanket('continueDIV');
                    document.getElementById('funtype').value="0";
                    document.getElementById('usertypes').value="0";
                    document.getElementById('usertype').value="";
                    clearDivs("0");
                    
                }
            }
        </script>
    </head>
    <body>
        <form method="post">
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">

                <table width="100%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">User Type Creation</td>
                    </tr>
                    <tr>
                        <td width="30%" class="textalign">Function Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="funtype" id="funtype" onchange="clearDivs(this.value)">
                                <option value="0">--Select--</option>
                                <option value="1">Addition</option>
                                <option value="2">Modification</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" colspan="4">
                            <div id="showold" align="center" style="display:none;">
                                <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                                    <tr class="lightrow">
                                        <td width="30%" class="textalign">User Types</td>
                                        <td width="5%" class="mandatory">*</td>
                                        <td width="65%" colspan="2" class="textfieldalign" >
                                            <select class="combobox" name="usertypes" id="usertypes" class="textfieldalign"  onchange="getDatasforModification()"></select>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id="shownew" align="center" style="display:none;">
                                <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                                    <tr class="darkrow">
                                        <td width="30%" class="textalign">User Type Name</td>
                                        <td width="5%" class="mandatory">*</td>
                                        <td width="65%"class="textfieldalign"><input type="text" class="textbox"  id="usertype"  size="25"/></td>
                                    </tr>
                                </table>
                            </div>
                            <div id="showbutton" align="center" style="display:none;">
                                <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                                    <tr>
                                        <td width="100%" colspan="4" align="center">
                                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                                        </td>
                                    </tr>
                                </table>
                            </div>

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
        function clearDivs(value){
            if(value=="0"){
                document.getElementById('usertype').value="";
                document.getElementById('showold').style.display="none";
                document.getElementById('shownew').style.display="none";
                document.getElementById('showbutton').style.display="none";                
            }else if(value=="1"){                
                document.getElementById('shownew').style.display="block";
                document.getElementById('showbutton').style.display="block";
                document.getElementById('showold').style.display="none";
                document.getElementById('usertype').readOnly = false;
                document.getElementById('usertype').value="";
            }else if(value=="2"){
                document.getElementById('usertype').value="";
                document.getElementById('shownew').style.display="block";
                document.getElementById('showbutton').style.display="block";
                document.getElementById('showold').style.display="block";
                document.getElementById('usertype').readOnly = true;
                UserTypeAction.getUsertypes(fillusertypes);
            }
        }
        function fillusertypes(map){
            dwr.util.removeAllOptions("usertypes");
            dwr.util.addOptions("usertypes",map.usertypes);
        }
        function getDatasforModification(){
            var funtype=document.getElementById('funtype').value
            var usertypes=document.getElementById('usertypes').value
            if(funtype=="0"){
                alert("Please Select the function Type");
                document.getElementById('funtype').focus();
                return false;
            }else if(usertypes=="0"){
                alert("Please Enter the User Type");
                document.getElementById('usertypes').focus();
                return false;
            }else if(funtype=="2"){                
                document.getElementById('usertype').readOnly = false;
                var item=document.getElementById("usertypes").options[document.getElementById("usertypes").selectedIndex].text;
                document.getElementById('usertype').value=item;
                
            }
        }
    </script>
</html>

