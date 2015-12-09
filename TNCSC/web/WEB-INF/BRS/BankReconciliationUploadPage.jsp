<%-- 
    Document   : BankReconciliationUploadPage
    Created on : 20 Mar, 2014, 5:11:23 PM
    Author     : Karthikeyan S & Prince vijayakumar M
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

    String booknames = "";

    if (request.getSession().getAttribute("bookname") != null) {
        booknames = (String) request.getSession().getAttribute("bookname");
    }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>BRS File Upload</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>       
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/dateValidations.js"></script>
        <script src="<%=staticPath%>scripts/jquery.handsontable.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <script src="dwr/interface/BRSAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var smonth;
            var syear;
            $(function() {
                $('#recondate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear,smonth, 1));
                    }
                });
            });
            function onUploadfile(){
                var fileName = document.getElementById("fileuploadname").value;
                if(fileName.length==0){
                    alert("Please Select the File");
                    return false;
                }else if(smonth==null || syear==null){
                    alert("Please Select Month & Year");
                    return false;
                }else{
                    document.getElementById("paybillprintresult").innerHTML = "";
                    document.getElementById("paybillprogressbar").style.display='';
                    document.getElementById("printbut").disabled = true;

                    document.getElementById("month").value = smonth;
                    document.getElementById("year").value = syear;
                    document.forms[0].action = "BRSAction.do?method=uploadReconciliationFile";
                    document.forms[0].submit();
                }
            }
            function readUpLoadFile(obj){
                var fileName = obj.value;
                var ind=fileName.indexOf(".");

                if(document.forms[0].fileuploadname.value == ""){
                    alert("Please Select File Name");
                    fileName="";
                    return false;
                }
                var fileType=fileName.substr(ind+1);
                if(fileType.toUpperCase() =="XLS" || fileType.toUpperCase() =="XLSX"){                    
                }else{
                    alert("Please choose File type as XLS or XLSX. Other file types are not permitted ");
                    obj.value="";
                    return false;  
                }
            }
        </script> 
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>

        <style type="text/css">
            /*demo page css*/
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
        </style>
        <style type="text/css">
            #paybillprogressbar{
                width: 128px;
                height: 15px;
                text-align: center;
            }
            #paybillprintresult{
                color: #E31212;
                font-weight: bold;
                font-size: 12px;
                text-align: center;
            }
        </style>
    </head>

    <body>		<!-- Tabs -->
        <form method="post" enctype="multipart/form-data">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">BRS File Upload</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="3" class="mainheader">Upload BRS  File</td>
                    </tr>   
                    <tr class="lightrow">
                        <td width="45%" class="textalign">Account Book</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="50%" class="textfieldalign" >
                            <select class="combobox" name="accountbook" id="accountbook"></select>                            
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="45%" class="textalign">Month & Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="50%" class="textfieldalign" >
                            <input type="text"  class="textbox"  id="recondate" name="recondate"  size="20" />
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="45%" class="textalign">Accounting Period</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="50%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="accountingperiod" id="accountingperiod"></select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="45%" class="textalign">Upload File</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="50%" class="textfieldalign" >
                            <input type="file" name="fileuploadname" id="fileuploadname" onchange="readUpLoadFile(this)">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td colspan="3" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Upload" onclick="onUploadfile();">
                            <input type="reset" class="submitbu" value="Reset">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td colspan="3" align="center">
                            <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                            <div id="paybillprintresult"></div>
                        </td>
                    </tr>
                    <!--                    <tr class="lightrow">
                                            <td colspan="3" align="center">
                                                <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                                                <div id="paybillprintresult"></div>
                                            </td>
                                        </tr>-->
                </table>
                <br>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
            <input type="hidden" name="month" id="month">
            <input type="hidden" name="year" id="year">
        </form>
    </body>    

    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            document.getElementById("paybillprogressbar").style.display='none';
            document.getElementById("printbut").disabled = false;

            var message = '<%=message%>';
            if(message!=null && message!=""){
                alert(message);
            }
            BRSAction.getAccountBook(loadaccountbookdialogbox); 
        }
        function loadaccountbookdialogbox(map){
            dwr.util.removeAllOptions("accountbook");
            dwr.util.addOptions("accountbook", map.accountbookList);
            dwr.util.removeAllOptions("accountingperiod");
            dwr.util.addOptions("accountingperiod", map.accountYearList);
        }

    </script>
</html>
