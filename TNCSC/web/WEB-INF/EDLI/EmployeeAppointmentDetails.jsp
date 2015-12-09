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
        <title>Appointment Details</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/EDLIAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            $(function() {
                $("#showbutton").click(function(){
                    var fromyear=document.getElementById('fromdate').value;
                    var toyear=document.getElementById('todate').value;
                    var fyear=eval(fromyear);
                    var tyear=eval(toyear)-1;
                     if(fromyear==""){
                        alert("Please select the Financial Start Year");
                        document.getElementById('fromdate').focus();
                        return false;
                    }else if(toyear==""){
                        alert("Please select the Financial End Year");
                        document.getElementById('todate').focus();
                        return false;
                    }else if(fyear!=tyear){
                        alert("Please select the Correct Financial End Year");
                        return false;
                    }else{
                        getBlanket('continueDIV');
                        EDLIAction.getAppointmentDetails(fromyear,toyear,fillUploadedDetails);
                    }
                });
                $('#empdetailsform').dialog({
                    autoOpen: false,
                    width: 950,
                    modal: true,
                    buttons: {
                        "Close": function() {
                            $(this).dialog("close");
                        }
                    }
                });

            });
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

                <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" colspan="7" align="center" class="headerdata">Employee Appointment Details</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="29%" class="textalign">Financial Start Year</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="15%" class="textfieldalign" >
                            <input type="text" class="textbox" name="fromdate" id="fromdate">
                        </td>

                        <td width="9%" class="textalign">Financial End Year</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="15%" class="textfieldalign" >
                            <input type="text" class="textbox" name="todate" id="todate">
                        </td>
                        <td width="30%" class="textfieldalign" >
                            <input type="button" CLASS="submitbu" name="showbutton" id="showbutton" value="Show">
                        </td>
                    </tr>
                    <tr>
                        <td  width="100%" align="center" colspan="7" valign="top" style="background:transparent">
                            <div id="empdetails" style="display:none;height:300px;"></div>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="empdetailsform" title="Appointment details" >
                <div id="formtable" style="display:none;height:400px;overflow:auto;"></div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
        </form>
    </body>
    <script type="text/javascript">

        function fillUploadedDetails(map){
            getBlanket('continueDIV');
            document.getElementById('empdetails').style.display="block";
            document.getElementById("empdetails").innerHTML=map.regionwiseemployee;

            oTable = $('#empappointtable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"225px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
//                "sPaginationType": "full_numbers",
//                'iDisplayLength': 20,
//                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });

        }

        function showRegionMonthwiseAppointmentEmployees(month,regionid){
            var fromyear=document.getElementById('fromdate').value;
            if(fromyear==""){
                alert("Please select the Financial Start Year");
                document.getElementById('fromdate').focus();
                return false;
            }else{
                getBlanket('continueDIV');
                EDLIAction.getRegionMonthwiseAppointmentEmployees(month,regionid,fromyear,fillFormDetails);
            }

        }

         function fillFormDetails(map){
            getBlanket('continueDIV');
            document.getElementById('formtable').style.display="block";
            document.getElementById("formtable").innerHTML=map.regionmonthwiseemployee;

            oTable = $('#emptable').dataTable({
                "bJQueryUI": true,
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
//                "sPaginationType": "full_numbers",
//                'iDisplayLength': 20,
//                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });
            $('#empdetailsform').dialog("open");
        }

        function getALLRegionMonthwiseAppointmentEmployees(month){
            var fromyear=document.getElementById('fromdate').value;
            if(fromyear==""){
                alert("Please select the Financial Start Year");
                document.getElementById('fromdate').focus();
                return false;
            }else{
                getBlanket('continueDIV');
                EDLIAction.getALLRegionMonthwiseAppointmentEmployees(month,fromyear,fillFormDetails);
            }

        }
        function getRegionAllMonthsAppointmentEmployees(regionid){
            var fromyear=document.getElementById('fromdate').value;
            var toyear=document.getElementById('todate').value;
            if(fromyear==""){
                alert("Please select the Financial Start Year");
                document.getElementById('fromdate').focus();
                return false;
            }else if(toyear==""){
                alert("Please select the Financial End Year");
                document.getElementById('todate').focus();
                return false;
            }
            else{
                getBlanket('continueDIV');
                EDLIAction.getRegionAllMonthsAppointmentEmployees(fromyear,regionid,toyear,fillFormDetails);
            }

        }
        function getAllRegionAppointmentEmployees(){
            var fromyear=document.getElementById('fromdate').value;
            var toyear=document.getElementById('todate').value;
            if(fromyear==""){
                alert("Please select the Financial Start Year");
                document.getElementById('fromdate').focus();
                return false;
            }else if(toyear==""){
                alert("Please select the Financial End Year");
                document.getElementById('todate').focus();
                return false;
            }
            else{
                getBlanket('continueDIV');
                EDLIAction.getAllRegionAppointmentEmployees(fromyear,toyear,fillFormDetails);
            }

        }
    </script>
</html>

