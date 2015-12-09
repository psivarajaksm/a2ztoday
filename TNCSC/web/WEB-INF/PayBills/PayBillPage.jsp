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
        <script src="<%=staticPath%>scripts/jquery.handsontable.js"></script>
        <script src="<%=staticPath%>scripts/bootstrap-typeahead.js"></script>
        <script src="<%=staticPath%>scripts/jquery.autoresize.js"></script>
        <script src="<%=staticPath%>scripts/jquery.contextMenu.js"></script>
        <script src="<%=staticPath%>scripts/common.js"></script>
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.contextMenu.css">
        <script src="dwr/interface/EmployeePayBillAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <style type="text/css">
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}            

            .negative {
                color: red;
            }

        </style>
    </head>


    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>  
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">                
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="100%" align="center" class="headerdata">Employee Pay Bill</td>
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
                            <input type="text" class="textbox" name="epfno" id="epfno" onkeypress="specialCharNotAllowed(this)" onblur="loadEmployeeDetails(this);" >                            
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
                <div id="earningsdisplay" style="height:275px;overflow: auto;">
                    <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr>                            
                            <td colspan="3" class="mainheader">Employee Running Earnings and Deductions</td>
                        </tr>
                    </table>   
                    <table>
                        <tr>
                            <td>
                                <div  id="earningsGrid" class="dataTable" class="dataTable" style="width: 300px;height:215px; overflow: auto" ></div>
                            </td>
                            <td>
                                <div  id="deductionsGrid" class="dataTable" class="dataTable" style="width: 450px;height:215px; overflow: auto" ></div>
                            </td>
                            <td valign="top">
                                <div id="efforderdiv" style="width: 400px;display:none;">
                                    <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                                        <tr class="lightrow">
                                            <td width="40%" class="textalign">Order Number</td>
                                            <td width="20%" class="mandatory"></td>
                                            <td width="40%"class="textfieldalign" ><input type="text" class="textbox" name="orderno" id="orderno" ></td>

                                        </tr>
                                        <tr class="darkrow">
                                            <td width="40%" class="textalign">Effect From</td>
                                            <td width="20%" class="mandatory"></td>
                                            <td width="40%"class="textfieldalign" ><input type="text" class="textbox" id="salarystruceffeftfrom" name="salarystruceffeftfrom"  size="20" /></td>
                                        </tr>
                                        <tr class="lightrow">
                                            <td colspan="3" align="center">
                                                <input id="savebutton" type="button" class="submitbu" value="Save"  >             
                                            </td>
                                        </tr>
                                    </table>
                                </div>

                            </td>
                        </tr>
                    </table>  
                </div>

                <script>
                    var newsalarystrutureid;
                    var newsuplesalarystructureid;
                    var totalrecords=0;
                    var progressstatus=0;
                    var earningsContainer = $("#earningsGrid");
                    var deductionscontainer = $("#deductionsGrid");
                    $(function() {                        
                        $('#salarystruceffeftfrom').datepicker({ dateFormat: "dd/mm/yy" }).val();
                        $('#dialog_newsalary').dialog({
                            autoOpen: false,
                            width: 900,
                            modal: true,
                            buttons: {
                                "Close": function() {
                                    $(this).dialog("close");
                                }                              
                            }
                        });                        
                       
                        
                        $("#newsalarystructure").click(function(){                        
                            $('#dialog_newsalary').dialog('open');
                            var epfno=document.getElementById('epfno').value;                
                            EmployeePayBillAction.getEmployeeEarningsAndDeductions(epfno,fillEarningsDeductionsExcellSheet);                           
                        });
                        $("#progressbar").progressbar({
                            value: progressstatus
                        });

                        $("#savebutton").click(function(){     
                            var continuesave = true;
                            
                            earningsData = earningsContainer.handsontable("getData");    
                            deductionsData = deductionscontainer.handsontable("getData");                            
                            for (var i = 0; i < earningsData.length; i++){                                 
                                if(earningsContainer.handsontable('getCell', i, 0).className=='negative'){
                                    continuesave=false;
                                }
                                if(earningsContainer.handsontable('getCell', i, 1).className=='negative'){
                                    continuesave=false;
                                }
                            }
                            
                            for (var i = 0; i < deductionsData.length; i++){                                 
                                if(deductionscontainer.handsontable('getCell', i, 0).className=='negative'){
                                    continuesave=false;
                                }
                                if(deductionscontainer.handsontable('getCell', i, 1).className=='negative'){
                                    continuesave=false;
                                }
                            }
            
                            if(continuesave){
                                saveEarningsDet();       
                            }else
                                {
                                    alert("Error in entered earning or deduction, Correct and try!")
                                }
                        });

                    });
                   

                    $("input#rowHeaders").change(function () {
                        container.handsontable("updateSettings", {rowHeaders: $(this).is(':checked')});
                    });

                    $("input#colHeaders").change(function () {
                        container.handsontable("updateSettings", {colHeaders: $(this).is(':checked')});
                    });
                </script>

            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">  
            <input type="hidden" name="ded" id="ded" readonly >
            <input type="hidden" name="ear" id="ear" readonly >

        </form>
    </body>
    <script type="text/javascript">
        bodyonload();
        function bodyonload(){
            EmployeePayBillAction.getEmptyarningsAndDeductions(fillRegionCombo);
        }
        function fillRegionCombo(map){           
            
            earningslist=map.earningslist;            
            deductionslist=map.deductionslist;
            
            var autoComEarningsArray = new Array(earningslist.earningslistlength);
            for (var i = 0; i < earningslist.earningslistlength; i++) {
                autoComEarningsArray[i]=earningslist[i];
            }
            
            var autoComDeductionsArray = new Array(deductionslist.deductionslength);
            for (var i = 0; i < deductionslist.deductionslength; i++) {
                autoComDeductionsArray[i]=deductionslist[i];
            }
         
            earningsContainer.handsontable({
                rows: 25,
                cols: 2,
                rowHeaders: false, //turn off 1, 2, 3, ..  
                // minSpareRows: 1,
                
                rowHeaders: true,
                colHeaders: true,                
                colHeaders: ["EARNINGS", "AMOUNT"],
                fillHandle: false, //fillHandle can be turned off

                contextMenu: ["row_above", "row_below", "remove_row"],
                //contextMenu will only allow inserting and removing rows
                legend: [
                  
                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first row
                        },
                        style: {
                            //color: 'green', //make the text green and bold
                            //fontWeight: 'bold'
                        },
                        title: 'Heading', //make some tooltip
                        readOnly: false //make it read-only
                    },
                    {
                        match: function (row, col, data) {
                            var xx=data()[row][col];                           
                        
                            if(col==1){
                                if(xx!=""){
                                    if(!$.isNumeric(xx)){
                                        earningsContainer.handsontable('getCell', row, col).className = 'negative'; 
                                    }
                                }
                                
                            }  
                            if(col==0){                                
                                if(xx!=""){    
                                    document.getElementById('ear').value=earningslist[xx];
                                    var xxx=document.getElementById('ear').value;                                
                                    if(xxx=='undefined'){                                       
                                        earningsContainer.handsontable('getCell', row, col).className = 'negative'; 
                                    }  
                                }                                
                            } 
                        
                        }
                    }
                ],
                autoComplete: [
                    {
                        match: function (row, col, data) {
                            if (col == 2 || col == 3) {
                                return true;
                            }
                            return false;
                        },
                        highlighter: function (item) {
                            var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
                            var label = item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
                                return '<strong>' + match + '</strong>';
                            });
                            return '<span style="margin-right: 10px; background-color: ' + item + '">&nbsp;&nbsp;&nbsp;</span>' + label;
                        },
                        source: function () {
                            return ["yellow", "red", "orange", "green", "blue", "gray", "black", "white"]
                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first column
                        },
                        source: function () {
                            //return ["BMW", "Chrysler", "Nissan", "Suzuki", "Toyota", "Volvo"]
                            return autoComEarningsArray
                        }
                    }
                    
                ]                
               
            });           
            
          

            deductionscontainer.handsontable({
                rows: 25,
                cols: 3,
                //minSpareRows: 1,  
                
                rowHeaders: true,
                colHeaders: true,                
                colHeaders: ["DEDUCTIONS", "AMOUNT","DEDUCTION A/C NO"],
                fillHandle: false, //fillHandle can be turned off
                contextMenu: ["row_above", "row_below", "remove_row"],
                //contextMenu will only allow inserting and removing rows
                legend: [
                    {
                        match: function (row, col, data) {
                            if (col == 0 || col == 2 || col == 3) {
                                return true;
                            }
                            return false;
                        },
                        style: {
                            fontStyle: 'italic' //make the text italic
                        },
                        title: "Type to show the list of options"
                    },
                    {
                        match: function (row, col, data) {
                            var xx=data()[row][col];   
                            if(col==1){
                                if(xx!=""){
                                    if(!$.isNumeric(xx)){
                                        deductionscontainer.handsontable('getCell', row, col).className = 'negative'; 
                                    }
                                }
                                
                            }    

                            if(col==0){                                
                                if(xx!=""){    
                                    document.getElementById('ded').value=deductionslist[xx];
                                    var xxx=document.getElementById('ded').value;                                
                                    if(xxx=='undefined'){                                       
                                        deductionscontainer.handsontable('getCell', row, col).className = 'negative'; 
                                    }  
                                }                                
                            } 
                      
                        }
                    }
                ],
                autoComplete: [
                    {
                        match: function (row, col, data) {
                            if (col == 2 || col == 3) {
                                return true;
                            }
                            return false;
                        },
                        highlighter: function (item) {
                            var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
                            var label = item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
                                return '<strong>' + match + '</strong>';
                            });
                            return '<span style="margin-right: 10px; background-color: ' + item + '">&nbsp;&nbsp;&nbsp;</span>' + label;
                        },
                        source: function () {
                            return [""]
                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 0); 
                        },
                        source: function () {                            
                            return autoComDeductionsArray
                        }
                    }
                ]
               
            });          

        }      


        function fillEarningsDeductionsExcellSheet(map){

            employeeearningslist=map.employeeearningslist;
            row=employeeearningslist.employeeearningslength;
            var ord=employeeearningslist.salaryeffectorderno;
            var dat=employeeearningslist.salaryeffectdat;           
            document.getElementById("orderno").value=ord;                       
            document.getElementById("salarystruceffeftfrom").value=dat;            
           
            
            employeedeductionslist=map.employeedeductionslist;
            row=employeedeductionslist.employeedeductionslength;

       
            var x = new Array(employeeearningslist.employeeearningslength);
            for (var i = 0; i < employeeearningslist.employeeearningslength; i++) {
                x[i] = new Array(1);
            }

            for (var j = 0; j < employeeearningslist.employeeearningslength; j++)
            {
                x[j][0]=employeeearningslist[j];
                var e=parseInt(employeeearningslist.employeeearningslength)+j;                
                x[j][1]=employeeearningslist[e];
            }
                       
            
            var y = new Array(employeedeductionslist.employeedeductionslength);
            for (var i = 0; i < employeedeductionslist.employeedeductionslength; i++) {
                y[i] = new Array(2);
            }

            for (var j = 0; j < employeedeductionslist.employeedeductionslength; j++)
            {
                y[j][0]=employeedeductionslist[j];
                var e=parseInt(employeedeductionslist.employeedeductionslength)+j;
                y[j][1]=employeedeductionslist[e];
                var f=parseInt(employeedeductionslist.employeedeductionslength)+parseInt(employeedeductionslist.employeedeductionslength)+j;
                y[j][2]=employeedeductionslist[f];
            }
            
            
            earningsContainer.handsontable("loadData", x);
            deductionscontainer.handsontable("loadData", y);
        }
        
        function fillEmployeeDeductionDetails(map){                
            if(map.ERROR!=null && map.ERROR!=""){
                alert("Please Enter the Valid Employee EPF No");
                document.forms[0].epfno.value = "";                 
                document.getElementById("efforderdiv").style.display = 'none';
                document.getElementById("earningsdisplay").style.display = 'none';
                document.getElementById("branchname").value="";
                document.getElementById("section").value="";
                document.getElementById("employeename").value="";
                document.getElementById("designation").value="";
                document.getElementById("fathername").value="";
                document.getElementById("dateofbirth").value="";
                document.getElementById("doa").value="";
                document.getElementById("dateofprobation").value="";
                document.getElementById('epfno').focus();
                return false;
            } else {               
                document.getElementById("earningsdisplay").style.display = 'block';                
                document.getElementById("branchname").value=map.branchname;
                document.getElementById("section").value=map.section;
                document.getElementById("employeename").value=map.employeename;
                document.getElementById("designation").value=map.designation;
                document.getElementById("fathername").value=map.fathername;
                document.getElementById("dateofbirth").value=map.dateofbirth;
                document.getElementById("doa").value=map.doa;
                document.getElementById("dateofprobation").value=map.dateofprobation;
                document.getElementById("efforderdiv").style.display = '';
                var epfno=document.getElementById('epfno').value;    
                EmployeePayBillAction.getEmployeeEarningsAndDeductions(epfno,fillEarningsDeductionsExcellSheet);                                           
                
            }
                
        }
            
        function loadEmployeeDetails(obj){                
            var epfno=document.getElementById('epfno').value;
            if(epfno.length>0){
                EmployeePayBillAction.getEmployeeDeductionDetails(epfno,fillEmployeeDeductionDetails);
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
        
        function saveEarningsDet(){
            
            
            var type="earnings";
            var epfno=document.getElementById('epfno').value;  
            var orderno=document.getElementById('orderno').value;
            var salarystruceffeftfrom=document.getElementById('salarystruceffeftfrom').value;            
            var salarystructureid = null;              
            earningsData = earningsContainer.handsontable("getData");                
           
            var curRec=0; 
            var totRec=earningsData.length-1;                    
            var code=earningslist[earningsData[curRec][0]];                               
            var amt =earningsData[curRec][1];
            var actcode=0;
            if(salarystruceffeftfrom!=""){
                getBlanket('continueDIV');
                EmployeePayBillAction.saveEmployeeSalaryStructure(type,epfno,salarystructureid,orderno,salarystruceffeftfrom,code,amt,actcode,curRec,totRec,saveEmployeeSalaryStructureEarSucc);                                
            }else{
                alert("Enter Effect ");
            }
             
        }
        
        function saveEmployeeSalaryStructureEarSucc(map){              
                    
            earningsData = earningsContainer.handsontable("getData"); 
            var type="earnings";
            var epfno=document.getElementById('epfno').value;  
            var orderno=document.getElementById('orderno').value;
            var salarystruceffeftfrom=document.getElementById('salarystruceffeftfrom').value;

            var salarystructureid = map.salarystructureid;             
            var curRec=map.currentRecords;
            var totRec=map.totalrecords;          
            var code=earningslist[earningsData[curRec][0]];                               
            var amt =earningsData[curRec][1];
            var actcode=0;
            if(curRec<totRec){
                EmployeePayBillAction.saveEmployeeSalaryStructure(type,epfno,salarystructureid,orderno,salarystruceffeftfrom,code,amt,actcode,curRec,totRec,saveEmployeeSalaryStructureEarSucc);                                
            }
            else{
                saveDeductionDet(map);       
            }            
            
        }
        
        function saveDeductionDet(map){
            
            deductionsData = deductionscontainer.handsontable("getData");
            var type="deductions";
            var epfno=document.getElementById('epfno').value;  
            var orderno=document.getElementById('orderno').value;
            var salarystruceffeftfrom=document.getElementById('salarystruceffeftfrom').value;

            var salarystructureid = map.salarystructureid;             
            var curRec=0; 
            var totRec=deductionsData.length-1;           
            var code=deductionslist[deductionsData[curRec][0]];                               
            var amt =deductionsData[curRec][1];
            var actcode =deductionsData[curRec][2];
            
            EmployeePayBillAction.saveEmployeeSalaryStructure(type,epfno,salarystructureid,orderno,salarystruceffeftfrom,code,amt,actcode,curRec,totRec,saveEmployeeSalaryStructureDeducSucc);                                
        }
        
        function saveEmployeeSalaryStructureDeducSucc(map){  
            
            deductionsData = deductionscontainer.handsontable("getData");
            var type="deductions";
            var epfno=document.getElementById('epfno').value; 
            var orderno=document.getElementById('orderno').value;
            var salarystruceffeftfrom=document.getElementById('salarystruceffeftfrom').value;

            var salarystructureid = map.salarystructureid;             
            var curRec=map.currentRecords;
            var totRec=map.totalrecords;            
            var code=deductionslist[deductionsData[curRec][0]];                               
            var amt =deductionsData[curRec][1];
            var actcode =deductionsData[curRec][2];
            
            if(curRec<totRec){
                EmployeePayBillAction.saveEmployeeSalaryStructure(type,epfno,salarystructureid,orderno,salarystruceffeftfrom,code,amt,actcode,curRec,totRec,saveEmployeeSalaryStructureDeducSucc);                                
            }else{
                getBlanket('continueDIV');
                alert("Saved Successfully");              
                
            }           
            
        }
        
        function isNum(obj){
            var strValidChars = "0123456789,.-";
            var strChar;
            var blnResult = true;
            var strString = obj;

            if (strString.length == 0) return false;

            for (var i = 0; i < strString.length && blnResult == true; i++){
                strChar = strString.charAt(i);
                if (strValidChars.indexOf(strChar) == -1){
                    blnResult = false;
                }
            }
            if(!blnResult){
                alert("Only Numbers Are Allowed")
                obj.value="";
                return false;
            }
            return blnResult;
        }               
              
    </script>
</html>

