<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
    String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
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
        <title>EDLI</title>
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
            var month;
            var year;
            $(function() {


                $('#monthyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        month = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(year,month, 1));
                    }
                });

                $('#empdetailsform').dialog({
                    autoOpen: false,
                    width: 950,
                    height:450,
                    modal: true,
                    buttons: {
                        "Close": function() {
                            $(this).dialog("close");
                        }
                    }
                });



            });

        </script>


        <script type="text/javascript">
            function showdetails(){
                getBlanket('continueDIV');
                EDLIAction.getEDLIDetails(month+1, year,  showEDLIDetails);
            }
            function showdesignationwisedetails(employeetype){
                getBlanket('continueDIV');
                EDLIAction.getDesignationWiseEmployeesDetails(month+1, year,employeetype,  showEDLIDesignationWiseDetails);
            }
            function showdesignationEmployees(designationcode,employeetype){
                getBlanket('continueDIV');
                EDLIAction.getDesignationEmployees(month+1, year,designationcode,employeetype,  fillFormDetails);
            }
            function showEDLIDetails(map){
                getBlanket('continueDIV');
                document.getElementById('edlidetails').style.display="block";
                document.getElementById("edlidetails").innerHTML=map.edlidetails;

                oTable = $('#edlidetailstable').dataTable({
                    "bJQueryUI": true,
                    "sScrollY":"180px",
                    "bSort": true,
                    "bFilter": true,
                    "bPaginate": false
//                    "sPaginationType": "full_numbers",
//                    'iDisplayLength': 20,
//                    "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
                });

            }
            function showEDLIDesignationWiseDetails(map){
                getBlanket('continueDIV');
                document.getElementById('edlidetailssal').style.display="block";
                document.getElementById("edlidetailssal").innerHTML=map.edlidetailssal;

                oTable = $('#edlipaidtable').dataTable({
                    "bJQueryUI": true,
                    "sScrollY":"180px",
                    "bSort": true,
                    "bFilter": true,
                    "bPaginate": false
//                    "sPaginationType": "full_numbers",
//                    'iDisplayLength': 20,
//                    "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
                });
            }

            function fillFormDetails(map){
            getBlanket('continueDIV');
            document.getElementById('formtable').style.display="block";
            document.getElementById("formtable").innerHTML=map.designationwiseemployees;

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
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
            #scrolling {
                /**allow ample width for table, padding and borders**/
                width: auto;
                /**adjust as needed. If no height is declared, scrollbar won't appear**/
                height: 200px;
                /**vertical scrollbar**/
                overflow-y:scroll;
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
                                    <td width="100%" align="center" class="headerdata">EDLI</td>
                                </tr>
                            </table>
                            <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" valign="top">
                                        <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td colspan="3" class="mainheader">EDLI Report</td>
                                            </tr>
                                            <tr class="lightrow">
                                                <td width="45%" align="right" class="textalign">Month & Year</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="50%" align="left" class="textfieldalign" >
                                                    <input type="text" class="textbox" name="monthyear" id="monthyear" readonly>
                                                </td>
                                            </tr>
                                            <tr class="darkrow">
                                                <td width="100%"colspan="3" align="center">
                                                    <input type="button" class="submitbu" id="printbut" value="Show" onclick="showdetails();">
                                                </td>
                                            </tr>
                                            <tr class="lightrow">
                                                <td width="100%"colspan="3" align="center">
                                                    <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                                        <tr>
                                                            <td width="50%">

                                                                <div id="edlidetails" style="display:none;height:190px;"></div>

                                                            </td>
                                                            <td width="50%">
                                                                <div id="edlidetailssal" style="display:none;height:190px;"></div>

                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="empdetailsform" title="Employee Details" >
                <div id="formtable" style="display:none;height:300px;overflow:auto;"></div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="budgetdetailsid" id="budgetdetailsid"  value="0" >
            <input type="hidden" name="ledgerid" id="ledgerid"  value="0" >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>
    </body>
    <script type="text/javascript">

    </script>
</html>

