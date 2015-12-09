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
        <title>Employee Provident Fund Loan Application Receipt Entry</title>
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
                        EPFLoanAction.getEpfLoanApplReceipt(amonth+1,ayear,fillLoanReceiptDetails);
                        //                        $(this).dialog("close");
                    }

                });
                

                $('#loanappform').dialog({
                    autoOpen: false,
                    width: 550,
                    height: 450,
                    modal: true,
                    buttons: {
                        "Save": function() { 
                            var isfinalsettle=document.getElementById('isfinalsettle').value;
                            var epfnumber=document.getElementById('epfnumber').value;
                            var applicationdate=document.getElementById('applicationdate').value;
                            var loantype=document.getElementById('loantype').value;
                            var loanamount=document.getElementById('loanamount').value;
                            var loaninstallment=document.getElementById('loaninstallment').value;
                            var tapalno=document.getElementById('tapalno').value;
                            var epfloanappreceiptid=document.getElementById('epfloanappreceiptid').value;                              
                            
                            if(isfinalsettle=="0"){
                                alert("Please Select the IS FINAL SETTLEMENT OR NOT");
                                document.getElementById('isfinalsettle').focus();
                                return false;
                            }else if(epfnumber==""){
                                alert("Please enter the epfno");
                                document.getElementById('monthyear').focus();
                                return false;
                            }else if(applicationdate==""){
                                alert("Please select the Loan Application Date");
                                document.getElementById('applicationdate').focus();
                                return false;
                            }else if(loantype=="0"){
                                alert("Please select the Loan Type");
                                document.getElementById('loantype').focus();
                                return false;
                            }else {
                                if(isfinalsettle=="N"){
                                    if(loanamount=="" || loanamount=="0"){
                                        alert("Please Enter the Loan Amount");
                                        document.getElementById('loanamount').focus();
                                        return false;
                                    }
                                    if(loanamount=="" || loaninstallment=="0"){
                                        alert("Please Enter the Loan Installments");
                                        document.getElementById('loaninstallment').focus();
                                        return false;
                                    }
                                }
                                getBlanket('continueDIV');
                                EPFLoanAction.saveLoanApplicationReceiptEntry(epfloanappreceiptid,epfnumber,applicationdate,loantype,loanamount,tapalno,amonth+1,ayear,isfinalsettle,loaninstallment,saveStatusOfApplicationReceipt);
                                $(this).dialog("close");
                            }
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
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
                        <td width="100%" align="center" class="headerdata">Loan Application Entry</td>
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
                <div id="loanappform" title="Employee Providentfund Loan Application Receipt Entry Form" >
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Is Final Settlement</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign">
                                <select class="combobox" name="isfinalsettle" id="isfinalsettle" onchange="loadDiv(this.value)">
                                    <option value="0">--Select--</option>
                                    <option value="Y">Yes</option>
                                    <option value="N">No</option>
                                </select>
                            </td>
                        </tr>
                    </table>
                    <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Epfno</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="epfnumber" name="epfnumber"  onblur="checkEPFno();" /></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Employee Name</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="empname" name="empname" size="20" readonly /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">FBF Number</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="fbfno" name="fbfno" size="20" readonly /></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Father Name</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="fathername" name="fathername" size="20" readonly /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Date Of Birth</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="dateofbirth" name="dateofbirth" size="20" readonly /></td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Designation</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><input type="text"  class="textbox" id="empdesignation" name="empdesignation" size="20" readonly/></td>
                        </tr>

                        <tr class="darkrow">
                            <td width="20%" class="textalign">Region</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="empregion" id="empregion" disabled></select>
                                <!--                                <input type="text" id="empregion" name="empregion" size="20" readonly />-->
                            </td>
                        </tr>

                        <tr class="lightrow">
                            <td width="20%" class="textalign">Application Date</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="applicationdate" name="applicationdate" size="20" readonly /></td>
                        </tr>
                        <tr class="darkrow">
                            <td width="20%" class="textalign">Loan Type</td>
                            <td width="5%" class="mandatory">*</td>
                            <td width="25%"class="textfieldalign"><select class="combobox" name="loantype" id="loantype"></select></td>
                        </tr>
                        <tr class="lightrow">
                            <td width="20%" class="textalign">Tapal No</td>
                            <td width="5%" class="mandatory"></td>
                            <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="tapalno" name="tapalno"  size="20" /></td>
                        </tr>
                    </table>
                    <div id="loanamountdiv" style="display:none;">
                        <table table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                            <tr class="darkrow">
                                <td width="20%" class="textalign">Loan Amount</td>
                                <td width="5%" class="mandatory">*</td>
                                <td width="25%"class="textfieldalign"><input type="text" class="amounttextbox" id="loanamount" name="loanamount" value="0" onblur="checkFloat(this.id,'Loan Amount');" size="20"/></td>
                            </tr>
                            <tr class="lightrow">
                                <td width="20%" class="textalign">No of Installments</td>
                                <td width="5%" class="mandatory"></td>
                                <td width="25%"class="textfieldalign"><input type="text" class="textbox" id="loaninstallment" name="loaninstallment" value="0"  size="20" /></td>
                            </tr>
                        </table>
                    </div>
                </div>               
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="epfloanappreceiptid" id="epfloanappreceiptid">

        </form>
    </body>
    <script type="text/javascript">     
        bodyonload();
        function bodyonload(){
            EPFLoanAction.loadLoanTypes(fillRegionCombo);
        }
        
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("loantype");
            dwr.util.addOptions("loantype",map.epfloantypes);
            dwr.util.removeAllOptions("empregion");
            dwr.util.addOptions("empregion",map.regionlist);
            
        }
        function loadDiv(isfs){
            if(isfs=="0"){
                document.getElementById('loanamountdiv').style.display="none";
            }else if(isfs=="Y"){
                document.getElementById('loanamountdiv').style.display="none";
            }else{
                document.getElementById('loanamountdiv').style.display="block";
            }

        }
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
            document.getElementById('loantype').value="0";
            document.getElementById('loanamount').value="";
            document.getElementById('tapalno').value="";    
            document.getElementById('empdesignation').value="";
            document.getElementById('empregion').value="0";
            document.getElementById('empname').value="";
            document.getElementById('isfinalsettle').value="0";
            document.getElementById('fbfno').value="";
            document.getElementById('fathername').value="";
            document.getElementById('dateofbirth').value="";
            $('#loanappform').dialog('open');  
        }
        
        function modifyEPFLoanApplicationReceiptForm(loanappreceiptid){
            document.getElementById('epfloanappreceiptid').value=loanappreceiptid;
            EPFLoanAction.getEPFLoanApplicationReceiptDetails(loanappreceiptid,setEPFLoanApplicationReceiptDetails);
        }
        
        function setEPFLoanApplicationReceiptDetails(map){
            EPFLoanAction.loadLoanTypes(fillRegionCombo);
            document.getElementById('epfnumber').value=map.epfnumber;
            document.getElementById('applicationdate').value=map.applicationdate;
            document.getElementById('loantype').value=map.loantype;
            document.getElementById('loanamount').value=map.loanamount;
            document.getElementById('tapalno').value=map.tapalno; 
            document.getElementById('empdesignation').value=map.designation;
            document.getElementById('empregion').value=map.region;                    
            document.getElementById('empname').value=map.employeename;
            document.getElementById('fbfno').value=map.fbfno;
            document.getElementById('fathername').value=map.fathername;
            document.getElementById('dateofbirth').value=map.dateofbirth;
            document.getElementById('isfinalsettle').value=map.isfinalsettle;
            $('#loanappform').dialog('open');  
        }
        
        function saveStatusOfApplicationReceipt(map){
            getBlanket('continueDIV');
            if(map.ERROR!=null && map.ERROR!=""){
                alert(map.ERROR);                
            }else{
                getBlanket('continueDIV');
                document.getElementById('isfinalsettle').value="0";
                document.getElementById('fbfno').value="";
                document.getElementById('fathername').value="";
                document.getElementById('dateofbirth').value="";
                EPFLoanAction.getEpfLoanApplReceipt(amonth+1,ayear,fillLoanReceiptDetails);
            }            
        }
        
        function checkEPFno(){   
            var epfnumber=document.getElementById('epfnumber').value;
            
            if(epfnumber!=""){
                getBlanket('continueDIV');
                EPFLoanAction.getEmployeeDetails(epfnumber,setEmployeeDetails);
            }
        }
        function setEmployeeDetails(map){
            getBlanket('continueDIV');
            if(map.ERROR!=null && map.ERROR!=""){
                document.getElementById('empdesignation').value="";
                document.getElementById('empregion').value="";
                document.getElementById('empname').value="";
                document.getElementById('fbfno').value="";
                document.getElementById('fathername').value="";
                document.getElementById('dateofbirth').value="";
                alert(map.ERROR);                
            }else{
                document.getElementById('empdesignation').value=map.designation;
                document.getElementById('empregion').value=map.region;                    
                document.getElementById('empname').value=map.employeename;
                document.getElementById('fbfno').value=map.fbfno;
                document.getElementById('fathername').value=map.fathername;
                document.getElementById('dateofbirth').value=map.dateofbirth;
            }
        }
    </script>
</html>

