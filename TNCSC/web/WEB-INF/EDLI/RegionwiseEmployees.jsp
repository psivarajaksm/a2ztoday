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
        <title>Employee List As Per Pay Bill</title>
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
                $('#selecteddate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: "dd/mm/yy" }).val();
//                EDLIAction.loadRegionDetails(fillRegionCombo);




                $("#show").click(function() {
                   var selecteddate =  document.getElementById('selecteddate').value;
                   if(selecteddate==""){
                        alert("Please select the Corret Date");
                        document.getElementById('selecteddate').focus();
                        return false;
                    }else{
                        getBlanket('continueDIV');
                        EDLIAction.getRegionwiseEmployeeDetails(selecteddate, fillUploadedDetails);
                    }

                });
            });


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

                <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="4" width="100%" align="center" class="headerdata">Employee List</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="50%" class="textalign">Retirement Date on or After</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="10%" class="textfieldalign" >
                            <input type="text" class="textbox" name="selecteddate" id="selecteddate">
<!--                            <select class="combobox" name="region" id="region" class="textfieldalign" ></select>-->
                        </td>
                        <td width="35%" class="textfieldalign"><input type="button" class="submitbu" name="show" id="show" value="show"></td>
                    </tr>
                    <tr>
                        <td colspan="4" width="100%" align="center" valign="top" style="background:transparent">
                            <div id="empdetails" style="display:none;height:350px;overflow:auto;"></div>
                        </td>
                    </tr>
                </table>
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

            oTable = $('#emptable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"300px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
//                "sPaginationType": "full_numbers",
//                'iDisplayLength': 20,
//                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
        }
    </script>
</html>

