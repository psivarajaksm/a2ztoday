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
        <title>Stop Process</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="dwr/interface/EarningSlapDetailsAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var smonth;
            var syear;

            $(function() {
                $('#processmonth').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear,smonth, 1));
                    }
                });
                $("#stopbutton").click(function(){
                    var processtype=document.getElementById('processtype').value;
                    var processmonth=document.getElementById('processmonth').value;
                    var answer = confirm("Do You Want to Continue?");
                    if(processtype=="0"){
                        alert("Please Select the Process Type");
                        document.getElementById('processtype').focus();
                        return false;
                    }else if(processmonth==""){
                        alert("Please Select the Month and Year");
                        document.getElementById('processmonth').focus();
                        return false;
                    }else{
                        if (answer){
                            getBlanket('continueDIV');
                            EarningSlapDetailsAction.saveStopProcess(processtype,smonth+1,syear,saveStopProcess);
                        }
                    }
                });
            });
            function saveStopProcess(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.getElementById('processtype').value="0";
                    document.getElementById('processmonth').value="";                    
                }
            }
        </script>
<!--        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>-->
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

                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Stop Process</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Process Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="processtype" id="processtype">
                                <option value="0">--Select--</option>
                                <option value="1">Pay Roll Process</option>
<!--                                <option value="2">Supplementary Process</option>-->
                            </select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Month And year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="processmonth" id="processmonth" readonly>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
<!--                            <input type="button" CLASS="submitbu" name="startbutton" id="startbutton" value="Start Process">-->
                            <input type="button" CLASS="submitbu" name="stopbutton" id="stopbutton" value="Stop Process">
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
</html>

