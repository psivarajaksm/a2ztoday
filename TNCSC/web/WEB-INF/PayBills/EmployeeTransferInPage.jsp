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
    String filename = "";
    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");

    }
    filename = (String) request.getAttribute("filepath");

    request.removeAttribute("message");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>File Upload</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/highcharts.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/exporting.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/EmployeeTransferOutAction.js"></script>                    
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>        
        <script type="text/javascript">    
                
            $(function() {
                $("#savebutton").click(function(){                    
                    document.forms[0].action="EmployeeTransferOutAction.htm";
                    document.forms[0].method.value="uploadFile";
                    document.forms[0].submit();  
                });  
                
                $('#confirmform').dialog({
                    autoOpen: false,
                    width: 800,
                    modal: true,
                    buttons: {
                        "Confirm": function() {      
                            var filename='<%=filename%>';
                            EmployeeTransferOutAction.saveUploadedDatas(filename,saveUploadedDatasStatus);                             
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });

            });
            function saveUploadedDatasStatus(map){
                alert(map.details);       
                $('#confirmform').dialog('close');   
            }
        </script>
    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center" >
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post" enctype="multipart/form-data">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" class="headerdata">File Upload</td>
                                </tr>
                            </table>                                
                            <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">

                                <tr class="lightrow">
                                    <td width="30%" class="textalign">File</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="65%" colspan="2" align="left">                                                    
                                        <input type="file" name="fileuploadname" id="fileuploadname">&nbsp;&nbsp;&nbsp;                                                    
                                    </td>
                                </tr>    
                                <tr class="darkrow">
                                    <td width="100%" colspan="4" align="center">
                                        <input id="savebutton" type="button" class="submitbu" value="Upload"  >                                       
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <%if (message.equalsIgnoreCase("success")) {%>
                            <script type="text/javascript">
                                bodyonload();
                                function bodyonload(){                                    
                                    getBlanket('continueDIV');
                                    var filename='<%=filename%>';                                    
                                    EmployeeTransferOutAction.displayFileDatas(filename,fillDisplayTextFileDatas);
                                }
                                function fillDisplayTextFileDatas(map){                                                                                                       
                                    document.getElementById('textFileDetails').style.display="block";
                                    document.getElementById("textFileDetails").innerHTML=map.details;
                                    getBlanket('continueDIV');     
                                    $('#confirmform').dialog('open');                                    
                                }
                            </script>
                            <% } else if (message.equalsIgnoreCase("Failed")) {%>
                            <script type="text/javascript">
                                alert("Given Text File is Failed to upload");
                            </script>
                            <% }%>
                        </td>
                    </tr>
                </table>
                <div id="confirmform" title="Content of the uploaded file" >
                    <div id="textFileDetails" style="display:none;height:400px;overflow:auto;"></div>
                </div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="filename" id="filename"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
        </form>
    </body>    
</html>

