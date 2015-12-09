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
        <title>Region Master Creation</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>     
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/PayBillMasterAction.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>       
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>

        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {     
                $("#savebutton").click(function(){

                    var regionname=document.getElementById('regionname').value;
                    if(regionname==""){
                        alert("Please Enter the Region or Section Name")
                        document.getElementById('regionname').focus();
                        return false;
                    }else{ 
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            var regioncode=document.getElementById('regioncode').value;
                            getBlanket('continueDIV');
                            PayBillMasterAction.saveRegionMaster(regioncode,regionname,masterSaveDetails);
                            
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
                    document.getElementById('regioncode').value=map.regioncode;
                    document.getElementById('regionname').value="";
                    document.getElementById("regionDetails").innerHTML=map.regionDetails;
                    oTable = $('#regiontable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"215px",
                        "bSort": true,
                        "bFilter": true,
                        'iDisplayLength': 100,
                        "sPaginationType": "full_numbers"
                    }); 
                        
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
                        <td colspan="4" class="mainheader">Region Master Creation</td>
                    </tr>                    
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Region Code</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="regioncode" id="regioncode" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Region Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="regionname" id="regionname">
                        </td>
                    </tr> 
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="regionDetails" style="widht:90%;height:310px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            PayBillMasterAction.getRegionDetails(displayRegionDetails)
        }
        function displayRegionDetails(map){
            document.getElementById("regionDetails").innerHTML=map.regionDetails;
            document.getElementById('regioncode').value=map.regioncode;
            oTable = $('#regiontable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"215px",
                "bSort": true,
                "bFilter": true,
                'iDisplayLength': 100,
                "sPaginationType": "full_numbers"
            }); 
        }
        
        function getRegionMasterDetailsForModify(regionCode,regionName){            
            document.getElementById('regioncode').value=regionCode;
            document.getElementById('regionname').value=regionName;
        }
    </script>
</html>




