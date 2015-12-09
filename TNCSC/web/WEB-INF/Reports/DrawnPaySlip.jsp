<%-- 
    Document   : DrawnPaySlip
    Created on : May 13, 2013, 5:00:31 PM
    Author     : root
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>Duplicate Pay Slip</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/EmployeePayBillAction.js"></script>
        <script src="dwr/engine.js"></script>
<!--        <script src="dwr/util.js"></script>-->-->

        <style type="text/css">
            /*demo page css*/
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
        </style>
        <script type="text/javascript">
            var dmonthid;
            var dyearid;

            
                $(function() {
                    $('#monthandyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    maxDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        dmonthid=eval(month)+1;
                        dyearid=eval(year);
                        $(this).datepicker('setDate', new Date(year, month, 1));
                    }
                });
//                    $('#startingdate').datepicker({
//                        dateFormat: "dd/mm/yy",
//                        maxDate: "+0m" ,
//                        changeMonth: true,
//                        changeYear: true
//                    }).val();
                });                
            
        </script>
    </head>

    <body>		<!-- Tabs -->
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Duplicate Pay Slip</td>
                    </tr>
                </table>
                <table width="70%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="6" class="mainheader">Duplicate Pay Slip</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();" >
                        </td>
                        <td width="20%" class="textalign">Month & Year</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%"class="textfieldalign" >
                            <input type="text" class="textbox" name="monthandyear" id="monthandyear" readonly>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <select class="combobox" name="billtype" id="billtype" >
                                <option value="0">--Select--</option>
                                <option value="1">Regular Bill</option>
                                <option value="2">Supplementary Bill</option>
                                <option value="3">Surrender Leave Bill</option>
                            </select>
<!--                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails();" >-->
                        </td>
                        <td width="50%" colspan="3" class="textalign"></td>

                    </tr>
                    <tr class="darkrow">
                        <td colspan="6" align="center">
                            <input type="button" class="submitbu" id="printbut" value="Print" onclick="onprintout();">
                            <input type="reset" class="submitbu" value="Reset">
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
    <script type="text/javascript">
        function loadEmployeeDetails(){
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                EmployeePayBillAction.getEmployeeDeductionDetails(epfno,fillEmployeeDeductionDetails);
            }else{
                document.getElementById('epfno').value="";
            }
        }


        function fillEmployeeDeductionDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert("Please Enter the Valid Employee EPF No");
                document.forms[0].epfno.value = "";
                
                document.getElementById('epfno').focus();
                return false;
            } 
        }

        function onprintout(){
               var epfno = document.getElementById('epfno').value;
                var monthandyear = document.getElementById('monthandyear').value;
                var billtype = document.getElementById('billtype').value;

                if(epfno==""){
                    alert("Please Enter the Epf No");
                    document.getElementById('epfno').focus();
                    return false;
                }else if(monthandyear==""){
                    alert("Please Select the Month & Year");
                    document.getElementById('monthandyear').focus();
                    return false;
                }else if(billtype=="0"){
                    alert("Please Select the Bill Type");
                    document.getElementById('billtype').focus();
                    return false;
                }else{
                    var features = 'height=1000,width=800,top=10,left=25,status=no,toolbar=0,location=0,menubar=0,titlebar=0,scrollbars=yes,modal=yes';
                    var url="UserTypeAction.htm?method=duplicatePaySlipPrint&epfno="+epfno+"&month="+dmonthid+"&year="+dyearid+"&monthandyear="+monthandyear+"&billtype="+billtype;
                    window.open(url, "Duplicate PaySlip Report", features);
//                getBlanket('continueDIV');
                return false;
                }
//                var acccode = document.getElementById('ledger').value;                
//                getBlanket('continueDIV');
                
           }
    </script>
</html>



