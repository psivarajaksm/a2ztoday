<%-- 
    Document   : EmployeeTransferPage
    Created on : Aug 6, 2012, 12:20:02 PM
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
        <title>Employee Transfers and Postings</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/EmployeeTransferOutAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            

            $(function() {                  
                $('#transferdate').datepicker({ 
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: "dd/mm/yy" }).val();

                $("#savebutton").click(function(){
                    var epfno=document.getElementById('epfno').value;   
                    var currentregion=document.getElementById('currentregion').value;
                    var region=document.getElementById('region').value;
                    var section=document.getElementById('section').value;
                    if(epfno==""){
                        alert("Please Enter the EPF Number");
                        document.getElementById('epfno').focus();
                        return false;
                    }else if((region=="0")&&(section!="DEPUTATION")){
                        alert("Please Select the Transfer Region");
                        document.getElementById('region').focus();
                        return false;
                    }else if((region==currentregion)&&(section!="DEPUTATION")){
                        alert("Please Select the  Different Transfer Region .Because Both Regions are Same");
                        document.getElementById('region').focus();
                        return false;                        
                    }else{
                        getBlanket('continueDIV');
                        EmployeeTransferOutAction.saveEmployeeTransfer(epfno,region,saveEmployeeTransferDetails);
                    }
                    
                });
              
            });

            function saveEmployeeTransferDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                }else{
                    getBlanket('continueDIV');
                    alert(map.success);
                }

//                var filePath = map.filePath;
//                var fileName = map.fileName;
//                document.getElementById("filePath").value = filePath;
//                document.getElementById("fileName").value = fileName;
//                document.forms[0].action = "EmployeeTransferOutAction.do?method=PopupReport";
//                document.forms[0].submit();
                
                
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
                        <td colspan="4" class="mainheader">Employee Transfer Out</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Employee Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="employeename" id="employeename" readonly >
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Current Region</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="currentregion" id="currentregion" disabled class="textfieldalign" ></select>
                            <!--                            <input type="text" class="textbox" name="currentregion" id="currentregion" readonly >
                                                        <input type="hidden" name="currentregioncode"  id="currentregioncode">-->
                        </td>
                    </tr>                        
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Section Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="section" id="section" readonly >
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Transfer Region</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="region" id="region" class="textfieldalign" ></select>
                            <!--                            <input type="text" class="textbox" name="currentregion" id="currentregion" readonly >
                                                        <input type="hidden" name="currentregioncode"  id="currentregioncode">-->
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
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            EmployeeTransferOutAction.loadRegionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);

            dwr.util.removeAllOptions("currentregion");
            dwr.util.addOptions("currentregion",map.regionlist);
        }
        function loadEmployeeDetails(){
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                EmployeeTransferOutAction.loadEmployeeDetails(epfno,fillEmployeeDetails);
            }else{
                document.getElementById('epfno').value="";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
                document.getElementById("currentregion").value="0";
                document.getElementById("section").value="";
                document.getElementById("region").value="0";
            }
        }
        function fillEmployeeDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById("epfno").value="";
                document.getElementById("epfno").focus();
                document.getElementById("employeename").value="";
                document.getElementById("currentregion").value="0";
                document.getElementById("section").value="";
                document.getElementById("region").value="0";
                //return false;
            } else {
                document.getElementById("employeename").value=map.employeename;
                document.getElementById("currentregion").value=map.currentregion;
                document.getElementById("section").value=map.section;
            }
        }
        
    </script>
</html>




