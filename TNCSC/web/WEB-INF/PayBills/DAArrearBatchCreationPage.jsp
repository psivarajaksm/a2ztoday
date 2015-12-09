<%-- 
    Document   : DAArrearBatchCreationPage
    Created on : 5 Mar, 2015, 4:23:36 PM
    Author     : Onward
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
        <title>DA Arrear Batch Creation Page</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="dwr/interface/DAIncrementArrearAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            var amonth;
            var ayear;
            var smonth;
            var syear;
            var mmonth;
            var myear;

            $(function() {
                $('#batchdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    onClose: function(dateText, inst) {
                        amonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        ayear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(ayear,amonth, 1));
                    }
                });
            });
            function saveDAArrear(){
                var fileno = document.forms[0].fileno.value;
                var batchperiod = document.forms[0].batchperiod.value;
                if(fileno==""){                    
                    alert("Please enter the File No");
                    return false;
                }else{
                    var answer = confirm("Do You Want to Continue?");
                    if (answer){
                        getBlanket('continueDIV');
                        DAIncrementArrearAction.saveDAArrearBatchCreation(fileno,batchperiod, DAArrearBatchCreationStatus);
                    }
                }
            }
            function DAArrearBatchCreationStatus(map){
                getBlanket('continueDIV');
                document.forms[0].fileno.value="";
                document.forms[0].batchperiod.value="1";
                if(map.ERROR==null){
                    alert(map.success);
                }else{
                    alert(map.ERROR);
                }
            }
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>
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
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <td width="100%" class="mainheader">DA Arrear Batch Creation Page</td>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">                    
                    <tr class="darkrow">
                        <td width="8%" class="textalign">File No</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%"  >
                            <input type="text" class="textbox" name="fileno" id="fileno" >
                        </td>
                        <td width="8%" class="textalign">Period</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%"  >
<!--                            <input type="text" class="textbox" name="batchdate" id="batchdate" readonly>-->
                            <select class="combobox" name="batchperiod" id="batchperiod" >
                                <option value="1">April to June</option>
                                <option value="2">July to March</option>
                            </select>
                        </td>
                        <td width="20%" >
                            <input type="button" CLASS="submitbu" name="showdet" id="showdet" value="Save" onclick="saveDAArrear();" >
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>