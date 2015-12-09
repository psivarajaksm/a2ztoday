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
        <title>File Upload</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/BudgetAction.js"></script>
        <script src="dwr/interface/FileUploadAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">            
            $(function() {
                $('#uploadmonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        //dmonthid=eval(month)+1;
                        //dyearid=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });
            });
        </script>
        <script type="text/javascript">           
            function readUpLoadFile(obj){
                var fileName = obj.value;
                var ind=fileName.indexOf(".");

                if(document.forms[0].fileuploadname.value == ""){
                    alert("Please Select File Name");
                    fileName="";
                    document.getElementById('textFileDetails').style.display="none";
                    document.getElementById('displaydatas').style.display="none";
                    return false;
                }
                var fileType=fileName.substr(ind+1);
                if(fileType.toUpperCase() !="TXT"){
                    alert("Please choose File type as TXT. Other file types are not permitted ");
                    document.getElementById('textFileDetails').style.display="none";
                    document.getElementById('displaydatas').style.display="none";
                    
                    obj.value="";
                    return false;
                }
            }
            function displayFile(){
                if(document.forms[0].fileuploadname.value == ""){
                    alert("Please Select File Name");
                    fileName="";
                    return false;
                }else{
                    getBlanket('continueDIV');
                    document.forms[0].action="BudgetAction.htm";
                    document.forms[0].method.value="uploadFile";
                    document.forms[0].submit();
                }                
            }

            function getOldRecords() {
                var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                month=eval(month)+1;
                
                var region=document.getElementById('region').value;
                var uploadmonth=document.getElementById('uploadmonth').value;
                if(region=="0"){
                    alert("Please Select the Region");
                    document.getElementById('region').focus();
                    return false;
                }else if(uploadmonth==""){
                    alert("Please Select the Upload File For the Month Year");
                    document.getElementById('uploadmonth').focus();
                    return false;
                }else{
                    getBlanket('continueDIV');
                    FileUploadAction.getExistingDatas(region,month,year,existingDatas);
                
                }

            }
            
            function existingDatas(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    //                    document.getElementById('nrl').value="";
                    //                    document.getElementById('epfid').value="";
                    return false;
                } else {
                    getBlanket('continueDIV');
                    if(map.isRecord=="Yes"){
                        document.getElementById('isolddata').value="1";
                        //                        document.getElementById('displaydatas').style.display="block";
                        document.getElementById('textFileDetails').style.display="block";
                        document.getElementById("textFileDetails").innerHTML=map.display;
                    }
                }

            }


            function saveUpLoadExcelFileData(){
                var answer = confirm("Do You Want Upload Excel File ?");
                if (answer){
                    getBlanket('continueDIV');
                    document.forms[0].action="FileUploadAction.htm";
                    document.forms[0].method.value="saveUpLoadExcelFileData";
                    document.forms[0].submit();
                } else {
                    return false;
                }
            }
            function uploadTXTFile(){
                var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                month=eval(month)+1;

                var region=document.getElementById('region').value;
                var isolddata=document.getElementById('isolddata').value;
                var answer;

                var uploadmonth=document.getElementById('uploadmonth').value;
                if(region=="0"){
                    alert("Please Select the Region");
                    document.getElementById('region').focus();
                    return false;
                }else if(uploadmonth==""){
                    alert("Please Select the Upload File For the Month Year");
                    document.getElementById('uploadmonth').focus();
                    return false;
                }else{
                    if(isolddata=="1"){
                        answer = confirm("Do You Want Replace the existing Data ?");
                    }
                    else{
                        answer = confirm("Do You Want to Continue the Upload Data ?");
                    }
                    if (answer){
                        getBlanket('continueDIV');
                        FileUploadAction.uploadTxtFileDatatoDB(isolddata,region,month,year,uploadStatus);
                    }
                }
                
            }
            function SaveFileToDB(){
                BudgetAction.saveTextFiletoDB(saveTextFiletoDBStatus);
            }
            function saveTextFiletoDBStatus(map){
                alert(map.status);
            }

            function uploadStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.SUCCESS);                    
                    document.getElementById('isolddata').value="0";
                    document.getElementById('region').value="0";
                    document.getElementById('uploadmonth').value="";
                    document.getElementById('textFileDetails').style.display="none";
                    document.getElementById('displaydatas').style.display="none";


                }

            }
        </script>
        <script>
            function downLoadBeneficiaryExcelFile() {
                var iframe = document.createElement("iframe");
                var method='downLoadBeneficiaryExcelFile';
                iframe.src = 'FileUploadAction.htm?method='+method;
                iframe.style.display = "none";
                document.body.appendChild(iframe);
            }
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>
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
                            <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="49%" align="center" valign="top">
                                        <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td colspan="4" class="mainheader">File Upload</td>
                                            </tr>
                                            <!--                                            <tr class="lightrow">
                                                                                            <td width="30%" class="textalign">Region</td>
                                                                                            <td width="5%" class="mandatory">*</td>
                                                                                            <td width="65%" colspan="2" class="textfieldalign" >
                                                                                                <select class="combobox" name="region" id="region" class="textfieldalign" ></select>
                                                                                            </td>
                                                                                        </tr>                                           -->

                                            <tr class="darkrow">
                                                <td width="30%" class="textalign">Please select the file</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="65%" colspan="2" align="left">
                                                    <!--                                                Please select the file&nbsp;&nbsp;&nbsp;-->
                                                    <input type="file" name="fileuploadname" id="fileuploadname" onchange="readUpLoadFile(this)">&nbsp;&nbsp;&nbsp;
                                                    <input type="button" class="submitbu" name="save" id="save" value="Display Data" onclick="displayFile()">
                                                </td>
                                            </tr>
                                            <tr class="darkrow">
                                                <td width="100%" colspan="4" align="center">
                                                    <div id="displaydatas" align="center" style="display:none;">
                                                        <input type="button" class="submitbu" name="upload" id="upload" value="Upload Data" onclick="uploadTXTFile()">
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <%if (message.equalsIgnoreCase("success")) {%>
                            <script type="text/javascript">
                                bodyonload();
                                function bodyonload(){
                                    getBlanket('continueDIV');
                                    BudgetAction.displayTextFileDatas(fillDisplayTextFileDatas);
                                }
                                function fillDisplayTextFileDatas(map){
                                    if(map.ERROR!=null && map.ERROR!=""){
                                        alert(map.ERROR);
                                        getBlanket('continueDIV');
                                        return false;
                                    }else {
                                        getBlanket('continueDIV');
                                        document.getElementById('textFileDetails').style.display="block";
                                        document.getElementById("textFileDetails").innerHTML=map.display;
                                    }
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
            </div>
            <div id="textFileDetails" style="display:none;height:300px;overflow:auto;"></div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">

        </form>
    </body>
    <script type="text/javascript"> 
        //        onloadData();
        //        function onloadData(){
        //            FileUploadAction.loadRegionDetails(fillRegionCombo);
        //        }
        //        function fillRegionCombo(map){
        //            dwr.util.removeAllOptions("region");
        //            dwr.util.addOptions("region",map.regionlist);
        //        }
    </script>
</html>

