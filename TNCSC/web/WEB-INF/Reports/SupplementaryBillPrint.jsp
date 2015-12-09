<%--
Document   : jquerys
Created on : Jul 5, 2012, 10:26:37 AM
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
        <title>Employee Salary Details</title>        
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
<!--        <script src="dwr/interface/EmployeePayBillAction.js"></script>-->
        <script src="dwr/interface/SupplementaryBillAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>

        <style type="text/css">
            /*demo page css*/
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
        </style>
        <script type="text/javascript">
            $(function() {
                    $('#asondate').datepicker({ 
                        dateFormat: "dd/mm/yy",
                        maxDate: "+0m" ,
                        changeMonth: true,
                        changeYear: true
                    }).val();
                });
                </script>
        
        <script type="text/javascript">
            function onprintout(){
                document.forms[0].action = "SupplementaryBillAction.do?method=EmployeeSupplementaryBillPrintOut";
                document.forms[0].submit();
            }
            
            function changeDiv(idValue){

                var epfno=document.getElementById('epfno').value;           
                var asondate=document.getElementById("asondate").value;
                if(idValue=="1"){
                    var asondate=document.getElementById("asondate").value;
                    SupplementaryBillAction.modifySupplementaryBillDatas(epfno,asondate,"SUPLEMENTARYBILL",fillExistingSupplementaryBillDetails);
                }else if(idValue=="2"){
                    var asondate=document.getElementById("asondate").value;
                    SupplementaryBillAction.modifySurrenderLeaveDatas(epfno,asondate,"LEAVESURRENDER",fillExistingSupplementaryBillDetails);
                }
            }
             function fillExistingSupplementaryBillDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);
                document.getElementById("billtype").value="0";
                return false;
            } else {
                var billtype=document.getElementById('billtype').value;
                if(billtype=="1"){
                document.getElementById('billsdisplayDiv').style.display="block";
                dwr.util.removeAllOptions("billno");
                dwr.util.addOptions("billno",map);
                }

            }
        }

        </script>
    </head>

    <body>		<!-- Tabs -->
        <form method="post">
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <table width="100%" align="center" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Employee Supplementary Bill</td>
                    </tr>                    
                    <tr class="lightrow">
                        <td width="30%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno">
                        </td>
                    </tr> 
                    <tr class="darkrow">
                        <td width="30%" class="textalign">As on Date</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="asondate" id="asondate" readonly>
                        </td>
                    </tr>                  
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Type of Supplementary Bill Prepare</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="billtype" id="billtype" onchange="changeDiv(this.value)" >
                                <option value="0">--Select--</option>
                                <option value="1">Supplementary Bill</option>
                                <option value="2">Surrender Leave </option>                          
                                <option value="3">Increment Arrear </option>                          
                            </select>
                        </td>
                    </tr>                  
                </table>    

                <div id="billsdisplayDiv" style="display: none;">
                    <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                        <tr class="darkrow">
                            <td width="30%" class="textalign">Bills</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="65%" colspan="2" class="textfieldalign" >
                                <select class="combobox" name="billno" id="billno" class="textfieldalign" onchange="displayBillDetails(this.value)">
                                    
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="button" CLASS="submitbu" name="addbutton" id="addbutton" value="Add" onclick="onprintout()"></td>
                        </tr>
                    </table>
                </div>

                <div id="footer">
                    <%@ include file="/common/footer.jsp" %>
                </div>
                <input type="hidden" name="method">
                <input type="hidden" name="isnewmap" id="isnewmap" value="0">
                <input type="hidden" name="serialno" id="serialno" value="0">
                </form>

                </body>
                </html>


