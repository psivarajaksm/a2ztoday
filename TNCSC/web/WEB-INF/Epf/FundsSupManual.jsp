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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Employee Provident Fund Loan Application Transaction</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/EPFLoanAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var amonth=0;
            var ayear=0;
            var mmonth=0;
            var myear=0;
            $(function() {

                $('#applicationdate').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    yearRange: '1950:2050'
                })

                $('#monthyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'MM yy',
                    yearRange: '1950:2050',
                    maxDate: "+0m" ,
                    //                    minDate: "+0m" ,
                    onClose: function(dateText, inst) {
                        amonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                        ayear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(ayear,amonth, 1));
                    }
                }).val();

                $("#showbutton").click(function(){
                    var monthyear = document.getElementById('monthyear').value;
                    if(monthyear==""){
                        alert("Please Select the Salary Month and Year");
                        document.getElementById('monthyear').focus();
                        return false;
                    }else{
                        //                        document.getElementById('monthdisp').value=amonth+1;
                        //                        document.getElementById('yeardisp').value=ayear;
                        getBlanket('continueDIV');
                        EPFLoanAction.getEpfLoanTransaction(amonth+1,ayear,fillLoanReceiptDetails);
                        //                        $(this).dialog("close");
                    }

                });

                $("#loantrasaction").click(function(){
                    var chkarray="";
                    //                            var chkboxes=document.forms[0].receipt;
                    var chkboxes=document.getElementsByName("loanappid");
                    for (i=0;i<chkboxes.length;i++){
                        if (chkboxes[i].checked==true){
                            chkarray=chkarray+chkboxes[i].value+"-";
                        }

                    }
                    if(chkarray==""){
                        alert("Please Select the Atleast One Loan Application");
                        //                        document.getElementById('monthyear').focus();
                        return false;
                    }else{

                        getBlanket('continueDIV');
                        //                        EPFLoanAction.getEpfLoanApplicationsForWorkingSheet(amonth+1,ayear,fillLoanReceiptDetails);

                    }

                });



            });

        </script>
        <style type="text/css">
            /*            .ui-datepicker-calendar {
                            display: none;
                        }*/

            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
            /**
             * It may be useful to copy the below styles and use on your page
            */
        </style>
    </head>
    <body>
        <form method="post" name="myform">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Loan Transaction</td>
                    </tr>
                </table>
                <table width="35%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                    <tr class="darkrow">
                        <td width="48%" class="textalign">Loan Month and Year</td>
                        <td width="2%" class="mandatory"></td>
                        <td width="44%"class="textfieldalign"><input type="text" id="monthyear" name="monthyear"  size="20" /></td>
                        <td width="6%" class="textalign"><input type="button" CLASS="submitbu" name="showbutton" id="showbutton" value="Show"></td>
                    </tr>
                </table>
                <div id="loanreceiptdetails" style="display:none;overflow:auto;"></div>

            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="epfloanappreceiptid" id="epfloanappreceiptid">

        </form>
    </body>
    <script type="text/javascript">       
        function fillLoanReceiptDetails(map){
            getBlanket('continueDIV');
            document.getElementById('loanreceiptdetails').style.display="block";
            document.getElementById("loanreceiptdetails").innerHTML=map.loanreceiptdetails;
            oTable = $('#taxtable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
            });
        }

        function showApplicationReceiptEntryForm(){
            document.getElementById('epfloanappreceiptid').value="";
            document.getElementById('epfnumber').value="";
            document.getElementById('applicationdate').value="";
            document.getElementById('loantype').value="";
            document.getElementById('loanamount').value="";
            document.getElementById('tapalno').value="";
            document.getElementById('empdesignation').value="";
            document.getElementById('empregion').value="";
            document.getElementById('empname').value="";

        }

        function checkAll(a)
        {
            var loanall=document.getElementsByName("loanall");
            var recLen=document.getElementsByName("loanappid");
            //            alert("recLen.length==="+recLen.length);
            if(loanall[0].checked==true)
            {
                for(var i=0; i<recLen.length;i++)
                {
                    recLen[i].checked=true;
                }
            }
            else
            {
                for (var i=0; i<recLen.length;i++)
                {
                    recLen[i].checked=false;
                }
            }
        }
    </script>
</html>

