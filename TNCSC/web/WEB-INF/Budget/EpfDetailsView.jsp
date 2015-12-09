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
        <title>File Upload</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/EpfAction.js"></script>
        <script src="dwr/interface/FileUploadAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var startyear=0;
            var endyear=0;
            var smonth;
            var syear;
            $(function() {
                $('#startyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    onClose: function(dateText, inst) {
                        smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(syear,smonth, 1));
                    }
                });
            });
            function onloadepfdetails(){
                var region = document.forms[0].region.value;
                var empcategory = document.forms[0].empcategory.value;
                EpfAction.getEpfDetails(smonth, syear, region, empcategory, loadepfdetails)
            }
            function loadepfdetails(map){

                if(map!=null){
                    document.getElementById("epfgrid").innerHTML = map.epfgrid;
                }else{
                    alert("There is No data for the Given Inputs");
                }
                
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
                                    <td width="100%" align="center" class="headerdata">Epf Details</td>
                                </tr>
                            </table>
                            <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" valign="top">
                                        <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td colspan="6" class="mainheader">Epf Details</td>
                                            </tr>
                                            <tr class="lightrow">
                                                <td width="20%" class="textalign">Month & Year</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="25%"class="textfieldalign" >
                                                    <input type="text" class="textbox" name="startyear" id="startyear" readonly>
                                                </td>
                                                <td width="20%" class="textalign">Payroll Category</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="25%" class="textfieldalign" >
                                                    <select class="combobox" name="empcategory" id="empcategory" class="textfieldalign">
                                                        <option value="R">Regular</option>
                                                        <option value="S">Seasonal</option>
                                                        <option value="L">Load Man</option>
                                                    </select>

                                                </td>
                                            </tr>
                                            <tr class="darkrow">
                                                <td width="20%" class="textalign">Region</td>
                                                <td width="5%" class="mandatory">*</td>
                                                <td width="25%"class="textfieldalign" >
                                                    <select class="combobox" name="region" id="region" class="textfieldalign" onchange="onloadepfdetails()" ></select>
                                                </td>
                                                <td width="50%" class="textalign" colspan="3"></td>
                                            </tr>
                                            <tr class="lightrow">
                                                <td width="100%"colspan="6" align="center">
                                                    <div id="epfgrid"></div>
                                                </td>
                                            </tr> 
                                            <!--                                            <tr class="lightrow">
                                                                                            <td width="100%"colspan="6" align="center">
                                                                                                <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                                                                                                <div id="paybillprintresult"></div>
                                                                                            </td>
                                                                                        </tr> -->
                                        </table>
                                    </td>
                                </tr>
                            </table>                            
                        </td>
                    </tr>
                </table>
            </div>
            <div id="budgetdetails" style="display:none;height:300px;overflow:auto;"></div>
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
        onloadData();
        function onloadData(){
            FileUploadAction.loadRegionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
        }
    </script>
</html>

