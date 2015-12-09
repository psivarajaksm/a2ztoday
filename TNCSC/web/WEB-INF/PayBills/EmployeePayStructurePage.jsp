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
        <title>Employee Salary Structure</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
        <script src="dwr/interface/EmployeePayBillAction.js"></script>
        <script src="dwr/interface/EmployeeLoansandAdvancesAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            $(function() {
                $('#loandate').datepicker({ 
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true
                }).val();
                
                $('#newearningform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var epfno=document.getElementById('epfno').value;
                            var salarystructureid=document.getElementById('salarystructureid').value;                             
                            var earningnameadd=document.getElementById('earningnameadd').value;
                            var earningamountadd=document.getElementById('earningamountadd').value;
                            getBlanket('continueDIV');
                            EmployeePayBillAction.addEmployeeEarningDetailsBySalarystructureid(epfno,salarystructureid,earningnameadd,earningamountadd,fillEmployeeEarningDetailsAfterAddition); 
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                function fillEmployeeEarningDetailsAfterAddition(map){
                    $('#newearningform').dialog('close');
                    getBlanket('continueDIV');
                    document.getElementById("employeeearningdetails").innerHTML = map.earningdetails;
                }
                
                $('#newdeductionform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var epfno=document.getElementById('epfno').value;
                            var salarystructureid=document.getElementById('salarystructureid').value; 
                            var deductionnameadd=document.getElementById('deductionnameadd').value;
                            var deductionamountadd=document.getElementById('deductionamountadd').value;  
                            var deductionaccountadd=document.getElementById('deductionaccountadd').value;                              
                            getBlanket('continueDIV');
                            EmployeePayBillAction.addEmployeeDeductionDetailsBySalarystructureid(epfno,salarystructureid,deductionnameadd,deductionamountadd,deductionaccountadd,fillEmployeeDeductionDetailsAfterAddition); 
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });                
                function fillEmployeeDeductionDetailsAfterAddition(map){
                    $('#newdeductionform').dialog('close');
                    getBlanket('continueDIV');
                    document.getElementById("employeedeductiondetails").innerHTML = map.deductiondetails;                   
                }
                $('#modifyearningform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            getBlanket('continueDIV');
                            var epfno=document.getElementById('epfno').value;
                            var earningcodemodify=document.getElementById('earningcodemodify').value;
                            var earningamountmodify=document.getElementById('earningamountmodify').value;
                            var employeeearningsdetailsactualid=document.getElementById('employeeearningsdetailsactualid').value;                             
                            EmployeePayBillAction.modifyEmployeeEarningDetailsByEmployeeearningsdetailsactualid(epfno,employeeearningsdetailsactualid,earningcodemodify,earningamountmodify,fillEmployeeEarningDetailsAfterModification);
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                function fillEmployeeEarningDetailsAfterModification(map){
                    $('#modifyearningform').dialog('close');
                    getBlanket('continueDIV');
                    document.getElementById("employeeearningdetails").innerHTML = map.earningdetails;
                }
                $('#modifydeductionform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            getBlanket('continueDIV');
                            var epfno=document.getElementById('epfno').value;
                            var deductioncodemodify=document.getElementById('deductioncodemodify').value;
                            var deductionamountmodify=document.getElementById('deductionamountmodify').value;
                            var deductionaccountmodify=document.getElementById('deductionaccountmodify').value;
                            var employeedeductiondetailsactualid=document.getElementById('employeedeductiondetailsactualid').value;                             
                            EmployeePayBillAction.modifyEmployeeDeductionDetailsByEmployeedeductiondetailsactualid(epfno,employeedeductiondetailsactualid,deductioncodemodify,deductionamountmodify,deductionaccountmodify,fillEmployeeDeductionDetailsAfterModification);
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });                                
                function fillEmployeeDeductionDetailsAfterModification(map){
                    $('#modifydeductionform').dialog('close');
                    getBlanket('continueDIV');
                    document.getElementById("employeedeductiondetails").innerHTML = map.deductiondetails;                   
                }
                $('#newearningbutton').click(function(){
                    $('#newearningform').dialog('open');
                    return false;
                });
                $('#newdeductionbutton').click(function(){
                    $('#newdeductionform').dialog('open');
                    return false;
                });                
            });

            function employeeLoanSaveStatus(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    alert(map.ERROR);
                    getBlanket('continueDIV');
                    document.getElementById('loanname').value="";
                    document.getElementById('loandate').value="";
                    document.getElementById('loanamount').value="";
                    document.getElementById('noofinstallment').value="";
                    document.getElementById('firstinstallmentamt').value="";
                    //document.getElementById('successiveinstallmentamt').value="";
                    document.getElementById('fileno').value="";
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.message);
                    document.getElementById('loanname').value="";
                    document.getElementById('loandate').value="";
                    document.getElementById('loanamount').value="";
                    document.getElementById('noofinstallment').value="";
                    document.getElementById('firstinstallmentamt').value="";
                    //document.getElementById('successiveinstallmentamt').value="";
                    document.getElementById('fileno').value="";
                    document.getElementById("existingLoansdisplay").style.display = 'block';
                    document.getElementById("employeeearningdetails").innerHTML = map.loandetails;
                }
                
            }
            function modifyDeductionDetails(employeedeductiondetailsactualid){
                document.getElementById('employeedeductiondetailsactualid').value=employeedeductiondetailsactualid;
                $('#modifydeductionform').dialog('open');                
                EmployeePayBillAction.getEmployeeDeductionDetailsByEmployeedeductiondetailsactualid(employeedeductiondetailsactualid,fillEmployeeDeductionDetails);
            }
            function modifyEarningDetails(employeeearningsdetailsactualid){
                document.getElementById('employeeearningsdetailsactualid').value=employeeearningsdetailsactualid;
                $('#modifyearningform').dialog('open');
                EmployeePayBillAction.getEmployeeEarningDetailsByEmployeeearningsdetailsactualid(employeeearningsdetailsactualid,fillEmployeeEarningDetails);
            }
            function fillEmployeeDeductionDetails(map){
                document.getElementById("employeedeductiondetailsmodify").innerHTML = map.deductiondetails;
            }
            function fillEmployeeEarningDetails(map){
                document.getElementById("employeeearningdetailsmodify").innerHTML = map.earningdetails;
            }
        </script>
        <style type="text/css">

            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}


        </style>
    </head>


    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">
                <br>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Earning and Deduction Details</td>
                    </tr>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">

                    <tr>
                        <td colspan="6" class="mainheader">Employee Details</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">EPF No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="25%" colspan="4" class="textfieldalign" >
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetailsNLoanDetails();" >
                        </td>
                    </tr>

                    <tr class="darkrow">
                        <td width="20%" class="textalign">Region </td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%"class="textfieldalign" ><input type="text" class="textbox" name="branchname" id="branchname" readonly  ></td>


                        <td width="20%" class="textalign">Section</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%"class="textfieldalign" ><input type="text" class="textbox" name="section" id="section" readonly  ></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Employee Name</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="employeename" id="employeename" readonly >
                        </td>
                        <td width="20%" class="textalign">Designation</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%"class="textfieldalign" ><input type="text" class="textbox" name="designation" id="designation" readonly ></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="20%" class="textalign">Father Name</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="fathername" id="fathername"  readonly>
                        </td>
                        <td width="20%" class="textalign">DOB</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="dateofbirth" name="dateofbirth"readonly/></td>
                    </tr>
                    <tr class="lightrow">
                        <td width="20%" class="textalign">Date of Appointment</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="doa" id="doa"  readonly>
                        </td>
                        <td width="20%" class="textalign">Date of probation</td>
                        <td width="5%" class="mandatory"></td>
                        <td width="25%"class="textfieldalign" ><input type="text" class="textbox" id="dateofprobation" name="dateofprobation" readonly/></td>
                    </tr>
                </table>
                <div id="existingLoansdisplay" style="display:none;">
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td colspan="2" class="mainheader">Employee Earnings and Deductions</td>                           
                        </tr>
                        <tr>
                            <td>
                                <div id="employeeearningdetails" style="height:180px; overflow:auto;"></div>
                            </td>
                            <td>
                                <div id="employeedeductiondetails" style="height:180px; overflow:auto;"></div>
                            </td>                            
                        </tr>
                        <tr>
                            <td>
                                <input id="newearningbutton" type="button" class="submitbu" value="Add Earning"  >
                            </td>
                            <td>
                                <input id="newdeductionbutton" type="button" class="submitbu" value="Add Deduction"  >
                            </td>                            
                        </tr>
                    </table>

                </div>

                <div id="newearningform" title="Earning Entry Form" >
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Select Earning Detail</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="earningnameadd" id="earningnameadd"></select></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="earningamountadd" onblur="checkFloat(this.id,'Loan Amount');" size="20"/></td>
                        </tr>
                    </table>
                </div>

                <div id="newdeductionform" title="Deduction Entry Form" >
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Select Deduction Detail</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="deductionnameadd" id="deductionnameadd"></select></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="deductionamountadd" onblur="checkFloat(this.id,'Loan Amount');" size="20"/></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Deduction A/c No:</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="deductionaccountadd"  size="50"/></td>
                        </tr>                        
                    </table>
                </div>

                <div id="modifyearningform" title="Earning Entry Form" >
                    <div id="employeeearningdetailsmodify" ></div>
                </div>

                <div id="modifydeductionform" title="Deduction Entry Form" >
                    <div id="employeedeductiondetailsmodify" ></div>
                </div>                

            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="salarystructureid" id="salarystructureid">
            <input type="hidden" name="employeeearningsdetailsactualid" id="employeeearningsdetailsactualid">
            <input type="hidden" name="employeedeductiondetailsactualid" id="employeedeductiondetailsactualid">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            var paycodetype='E';
            EmployeeLoansandAdvancesAction.loadLoanNames(paycodetype,fillEarningCombo);
            var paycodetype='D';
            EmployeeLoansandAdvancesAction.loadLoanNames(paycodetype,fillDeductionCombo);            
        }
        function fillEarningCombo(map){
            dwr.util.removeAllOptions("earningnameadd");
            dwr.util.addOptions("earningnameadd",map.loanlist);
        }
        function fillDeductionCombo(map){
            dwr.util.removeAllOptions("deductionnameadd");
            dwr.util.addOptions("deductionnameadd",map.loanlist);            
        }
        function loadEmployeeDetailsNLoanDetails(){
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                EmployeePayBillAction.getEmployeePayStructureDetails(epfno,fillEmployeeDetailsNLoanDetails);
            }else{
                document.getElementById('epfno').value="";
                document.getElementById("employeeearningdetails").style.display = 'none';
                document.forms[0].epfno.value = "";
                document.getElementById("branchname").value="";
                document.getElementById("section").value="";
                document.getElementById("employeename").value="";
                document.getElementById("designation").value="";
                document.getElementById("fathername").value="";
                document.getElementById("dateofbirth").value="";
                document.getElementById("doa").value="";
                document.getElementById("dateofprobation").value="";
            }
        }
        function fillEmployeeDetailsNLoanDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                alert("Please Enter the Valid Employee EPF No");
                document.getElementById("employeeearningdetails").style.display = 'none';
                document.forms[0].epfno.value = "";
                document.getElementById("branchname").value="";
                document.getElementById("section").value="";
                document.getElementById("employeename").value="";
                document.getElementById("designation").value="";
                document.getElementById("fathername").value="";
                document.getElementById("dateofbirth").value="";
                document.getElementById("doa").value="";
                document.getElementById("dateofprobation").value="";
                document.getElementById('epfno').focus();
                //return false;
            } else {
                document.getElementById("existingLoansdisplay").style.display = 'block';
                document.getElementById("employeeearningdetails").innerHTML = map.earningdetails;
                document.getElementById("employeedeductiondetails").innerHTML = map.deductiondetails;   
                document.getElementById("salarystructureid").value = map.salarystructureid;                
                document.getElementById("branchname").value=map.branchname;
                document.getElementById("section").value=map.section;
                document.getElementById("employeename").value=map.employeename;
                document.getElementById("designation").value=map.designation;
                document.getElementById("fathername").value=map.fathername;
                document.getElementById("dateofbirth").value=map.dateofbirth;
                document.getElementById("doa").value=map.doa;
                document.getElementById("dateofprobation").value=map.dateofprobation;
            }
        }
        //        function setLoanId(loanId,process){            
        //            var epfno=document.forms[0].epfno.value;
        //            EmployeeLoansandAdvancesAction.getLoanDetailsForModification(loanId,epfno,fillLoadDetailsForModification)            
        //        }
        //        function fillLoadDetailsForModification(map){
        //            document.getElementById('loanname').value=map.loanname;
        //            document.getElementById('loandate').value=map.loandate;
        //            document.getElementById('loanamount').value=map.loanamount;
        //            document.getElementById('noofinstallment').value=map.noofinstallment;
        //            document.getElementById('firstinstallmentamt').value=map.firstinstallmentamt;
        //            //document.getElementById('successiveinstallmentamt').value=map.successiveinstallmentamt;
        //            document.getElementById('fileno').value=map.fileno;
        //            document.getElementById('loanid').value=map.loanid;
        //            document.getElementById('loanbalance').value=map.loanbalance;
        //            document.getElementById('completedins').value=map.completedins;
        //            $('#newearningform').dialog('open');
        //            return false;
        //        }

    </script>
</html>

