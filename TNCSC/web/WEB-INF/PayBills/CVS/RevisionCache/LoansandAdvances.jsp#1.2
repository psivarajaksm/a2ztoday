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
        <title>Employee Loans and Advances Details</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/blanket.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/common.js"></script>
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
                
                $('#newloanform').dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    buttons: {
                        "Ok": function() {
                            var epfno=document.getElementById('epfno').value;
                            var loanname=document.getElementById('loanname').value;
                            var loandate=document.getElementById('loandate').value;
                            var loanamount=document.getElementById('loanamount').value;
                            var noofinstallment=document.getElementById('noofinstallment').value;
                            var firstinstallmentamt=document.getElementById('firstinstallmentamt').value;
                           // var successiveinstallmentamt=document.getElementById('successiveinstallmentamt').value;
                            var fileno=document.getElementById('fileno').value;
                            var loanid=document.getElementById('loanid').value;
                            var loanbalance=document.getElementById('loanbalance').value;
                            var completedins =document.getElementById('completedins').value;
                            if(loanname=="0"){
                                alert("Please Select the Loan")
                                document.getElementById('loanname').focus();
                                return false;
                            }else if(loandate==""){
                                alert("Please Select the Loan Date")
                                document.getElementById('loandate').focus();
                                return false;
                            }else if(loanamount==""){
                                alert("Please Enter the Loan Amount")
                                document.getElementById('loanamount').focus();
                                return false;
                            }else if(noofinstallment==""){
                                alert("Please Enter the total Number of Installment Loan")
                                document.getElementById('noofinstallment').focus();
                                return false;
                            }else if(firstinstallmentamt==""){
                                alert("Please Enter the First Installment Loan Amount")
                                document.getElementById('firstinstallmentamt').focus();
                                return false;
                            }else if(fileno==""){
                                alert("Please Enter the File Number")
                                document.getElementById('fileno').focus();
                                return false;
                            }else{
                                //                            document.getElementById("employeeloandetails").innerHTML="";
                                getBlanket('continueDIV');
                                EmployeeLoansandAdvancesAction.saveEmployeeLoan(loanid,epfno,loanname,loandate,loanamount,noofinstallment,firstinstallmentamt,firstinstallmentamt,fileno,completedins,loanbalance,employeeLoanSaveStatus);
                                $(this).dialog("close");
                            }
                             
                        },
                        "Cancel": function() {                            
                            $(this).dialog("close");
                        }
                    }
                });
                $('#newloanbutton').click(function(){
                    document.getElementById('loanid').value="";
                    $('#newloanform').dialog('open');
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
                    document.getElementById("employeeloandetails").innerHTML = map.loandetails;
                }
                
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
                        <td width="100%" align="center" class="headerdata">Loans and Advances</td>
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
                            <td class="mainheader">Running Loan Details</td>
                        </tr>
                        <tr>
                            <td>
                                <div id="employeeloandetails" style="height:180px; overflow:auto;"></div>
                            </td>
                        </tr>
                    </table>
                    <input id="newloanbutton" type="button" class="submitbu" value="New Loan"  >
                </div>

                <div id="newloanform" title="Loan Form" >
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Loan Name</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="loanname" id="loanname"></select></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Loan Date</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="loandate" name="loandate"  size="20" /></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Loan Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="loanamount" onblur="checkFloat(this.id,'Loan Amount');" size="20"/></td>
                        </tr>

                        <tr class="darkrow">
                            <td width="20%" class="textalign">No. of Installments</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="noofinstallment" onkeypress="isNumeric(this);" size="20"/></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Installment Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="firstinstallmentamt"  onblur="checkFloat(this.id,'First Installment Amount');" size="20"/></td>
                        </tr>
<!--                        <tr class="darkrow">
                            <td width="20%" class="textalign">Successive Installment Amount</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="successiveinstallmentamt" onblur="checkFloat(this.id,'Successive Installment Amount');" size="20"/></td>
                        </tr>-->

                        <tr class="lightrow">
                            <td width="20%" class="textalign">File Number</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="fileno"  size="20"/></td>
                        </tr>
                         <tr class="darkrow">
                            <td width="20%" class="textalign">completed Installments</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="completedins"  size="20"/></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Loan Balance</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" id="loanbalance" onblur="checkFloat(this.id,'Loan Balance');" size="20"/></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="loanid" id="loanid">
        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            var paycodetype='L';
            EmployeeLoansandAdvancesAction.loadLoanNames(paycodetype,fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("loanname");
            dwr.util.addOptions("loanname",map.loanlist);
        }
        function loadEmployeeDetailsNLoanDetails(){
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                    EmployeeLoansandAdvancesAction.getEmployeeDetailsNLoanDetails(epfno,fillEmployeeDetailsNLoanDetails);
            }else{
                document.getElementById('epfno').value="";
                document.getElementById("employeeloandetails").style.display = 'none';
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
                document.getElementById("employeeloandetails").style.display = 'none';
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
                document.getElementById("employeeloandetails").innerHTML = map.loandetails;
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
        function setLoanId(loanId,process){            
            var epfno=document.forms[0].epfno.value;
            EmployeeLoansandAdvancesAction.getLoanDetailsForModification(loanId,epfno,fillLoadDetailsForModification)            
        }
        function fillLoadDetailsForModification(map){
            document.getElementById('loanname').value=map.loanname;
            document.getElementById('loandate').value=map.loandate;
            document.getElementById('loanamount').value=map.loanamount;
            document.getElementById('noofinstallment').value=map.noofinstallment;
            document.getElementById('firstinstallmentamt').value=map.firstinstallmentamt;
            //document.getElementById('successiveinstallmentamt').value=map.successiveinstallmentamt;
            document.getElementById('fileno').value=map.fileno;
            document.getElementById('loanid').value=map.loanid;
            document.getElementById('loanbalance').value=map.loanbalance;
            document.getElementById('completedins').value=map.completedins;
            $('#newloanform').dialog('open');
            return false;
        }

    </script>
</html>

