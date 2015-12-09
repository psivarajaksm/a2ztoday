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
        <script src="dwr/interface/EmployeePayBillAction.js"></script>
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
        <script type="text/javascript">
            function onprintout(){
                var printingbill = document.forms[0].printingbill.value;
                if(printingbill==1){
                    document.forms[0].action = "EmployeePayBillAction.do?method=EmployeePayBillPrintOut";
                    document.forms[0].submit();
                }else if(printingbill==2){
                    document.forms[0].action = "EmployeePayBillAction.do?method=EmployeeSupplementaryBillPrintOut";
                    document.forms[0].submit();
                }else if(printingbill==3){
                    document.forms[0].action = "EmployeePayBillAction.do?method=EmployeeAcquitanceSlipPrintOut";
                    document.forms[0].submit();
                }
            }
//            function EmployeePayBillPrintStatus(map){
//                document.getElementById("paybillprogressbar").style.display='none';
//                document.getElementById("paybillprintresult").style.display='';
//                if(map.ERROR!=null && map.ERROR!=""){
//                    alert("Pay Bill PrintOut Problem");
//                    return false;
//                } else {
//
//                }
//            }
        </script>
    </head>

    <body>		<!-- Tabs -->
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <!--            <div id="left-sidebar" >
            <%--@ include file="/common/leftpage.jsp" --%>
        </div>-->
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Employee Pay Bill Printout</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">

                    <tr>
                        <td colspan="6" class="mainheader">Employee Pay Bill PrintOut</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Bill Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                           <select class="combobox" name="printingbill" id="printingbill">
                                <option value="1">Pay Bill</option>
                                <option value="2">Supplementary Bill</option>
                                <option value="3">Pay Acquitance Slip</option>
                            </select>
                        </td>
                        <td width="45%" colspan="3"></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" >
                        </td>
                        <td width="20%" class="textalign">Section Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" ><input type="text" class="textbox" name="sectionname" id="sectionname" ></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="year" id="year">
                                <option value="1991">1991</option>
                                <option value="1992">1992</option>
                                <option value="1993">1993</option>
                                <option value="1994">1994</option>
                                <option value="1995">1995</option>
                                <option value="1996">1996</option>
                                <option value="1997">1997</option>
                                <option value="1998">1998</option>
                                <option value="1999">1999</option>
                                <option value="2001">2001</option>
                                <option value="2002">2002</option>
                                <option value="2003">2003</option>
                                <option value="2004">2004</option>
                                <option value="2005">2005</option>
                                <option value="2006">2006</option>
                                <option value="2007">2007</option>
                                <option value="2008">2008</option>
                                <option value="2009">2009</option>
                                <option value="2010">2010</option>
                                <option value="2011">2011</option>
                                <option value="2012">2012</option>
                                <option value="2013">2013</option>
                                <option value="2014">2014</option>
                                <option value="2015">2015</option>
                                <option value="2016">2016</option>
                            </select>
                        </td>
                        <td width="20%" class="textalign">Month</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" >
                            <select class="combobox" name="month" id="month">
                                <option value="1">January</option>
                                <option value="2">February</option>
                                <option value="3">March</option>
                                <option value="4">April</option>
                                <option value="5">May</option>
                                <option value="6">June</option>
                                <option value="7">July</option>
                                <option value="8">August</option>
                                <option value="9">September</option>
                                <option value="10">October</option>
                                <option value="11">November</option>
                                <option value="12">December</option>
                            </select>
                    </tr>
                    <tr class="darkrow">
                        <td colspan="6" align="center">
                            <input type="button" class="submitbu" value="Print" onclick="onprintout();">
                            <input type="reset" class="submitbu" value="Reset">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td colspan="6" align="center">
                            <!--                            <div id="paybillprogressbar" style="display: none"><img src="images/paybillprint.gif" width="128" height="15"/></div>
                                                        <div id="paybillprintresult" style="display: none">Pay Bill Sucessfully Generated</div>-->
                            <%
                                if (request.getSession(false).getAttribute("paybillprintresult") != null) {
                            %>
                            <div id="paybillprintresult"><%=(String)request.getSession(false).getAttribute("paybillprintresult")%></div>
                            <%
                                }
                            %>
                        </td>
                    </tr>
                </table>
                <br>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>

    </body>
</html>


