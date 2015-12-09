<%-- 
    Document   : Employeebonusdetailspage
    Created on : 12 Mar, 2015, 12:07:26 PM
    Author     : Onward
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
            #toolbar {
                margin: 20px 0 20px 24px;
            }

            button#selectFirst {
                float: left;
                margin-right: 10px;
            }
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
            <div id="content">
                <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                    <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
                </div>
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <td width="100%" class="mainheader">Employee Bonus Detail</td>
                </table>
                <div align="center" id="toolbar">
                    <table width="800px" border="0" cellspacing="0" cellpadding="0">
                        <tr class="darkrow">
                            <td class="textalign">Month</td>
                            <td><input type="text" class="textbox" name="attendancemonth" id="attendancemonth" readonly></td>
                            <td class="textalign">Bonus Type</td>
<!--                            <td><input type="text" id="bonustype" size="20" maxlength="3" /></td>-->
                            <td><select class="combobox" name="bonustype" id="bonustype">
                                    <option id="BON">BON</option>
                                    <option id="DA1">DA1</option>
                                    <option id="DA2">DA2</option>
                                    <option id="INC">INC</option>
                                    <option id="SUL">SUL</option>
                                    <option id="SUP">SUP</option>                                    
                                    <option id="OTH">OTH</option>                                    
                                </select></td>
                            <td > <input id="showButton" type="button" class="submitbu" value="Show" >&nbsp;&nbsp;&nbsp;</td>
                            <td > </td>
                        </tr>
                        <tr>
                            <td colspan="6">
                                <div  id="attendanceGrid" class="dataTable" class="dataTable" style="width: 800px;
                                      height: 280px; overflow: scroll" ></div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6" align="center">
                                <input id="saveButton" type="button" class="submitbu" value="Save" tabindex="26" >&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6">
                                <div  id="displayGrid" class="dataTable" class="dataTable" style="width: 800px;
                                      height: 250px; overflow: scroll" ></div>
                            </td>
                        </tr>
                    </table>                 
                </div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="periodsele" id="periodsele"  value="0" >
            <input type="hidden" name="filePath" id="filePath">
            <input type="hidden" name="fileName" id="fileName">
        </form>   
    </body>
    <script type="text/javascript">
        var totalrecords=0;
        var progressstatus=0;
        var smonth;
        var syear; 
        var container = $("#attendanceGrid");
        $(function() {
            $('#attendancemonth').datepicker({
                dateFormat: "dd/mm/yy",
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,
                dateFormat: 'dd/mm/yy',
                //                    minDate: "+0m" ,
                onClose: function(dateText, inst) {
                    smonth = eval($("#ui-datepicker-div .ui-datepicker-month :selected").val());
                    syear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                    $(this).datepicker('setDate', new Date(syear,smonth, 1));
                }
            });                
            $("#showButton").click(function(){
                llpView();                                                       
            });                        
            $("#saveButton").click(function(){
                saveBonusDetails();                          
            });
        }); 
        $("input#rowHeaders").change(function () {
            container.handsontable("updateSettings", {rowHeaders: $(this).is(':checked')});
        });
        $("input#colHeaders").change(function () {
            container.handsontable("updateSettings", {colHeaders: $(this).is(':checked')});
        });
        
        function llpView(){
            var attendancemonth=document.getElementById('attendancemonth').value;
            var bonustype=document.getElementById('bonustype').value;
            getBlanket('continueDIV');
            EmployeePayBillAction.getEmployeeMiscDeductions(attendancemonth,bonustype,fillLLPSheet);
        }
         function fillLLPSheet(map){
            bondetails = map.bondetails;
            table = bondetails.table;            
            document.getElementById('displayGrid').innerHTML = table;
            fillLLPEntrySheet(map);
        }
        function fillLLPEntrySheet(map){
            var slno=0;
            var first=true;
            var autoComEmployeeArray = new Array(map.length);
            for (var i = 0; i < map.length; i++) {
                autoComEmployeeArray[i]=map[i];
            }
            empcodes=map;
            llpdetails= map.llpdetails;            
            row=llpdetails.length;
            totalrecords=map.length;            
            container.handsontable({
                rows: 25,
                cols: 3,
                rowHeaders: false, //turn off 1, 2, 3, ..  
                //minSpareRows: 1,                
                rowHeaders: true,
                colHeaders: true,
                colHeaders: ["EMPLOYEE NAME", "Earning Amount", "Deduction Amount"],
                fillHandle: false, //fillHandle can be turned off

                contextMenu: ["row_above", "row_below", "remove_row"],
                onChange: function (data){
                    if (first) {
                        first = false;
                        return;
                    }
                    if(parseInt(data[0][1])==0){
                        container.handsontable('setDataAtCell', data[0][0], 2, map["ACC"+data[0][3]], false);
                    }
                },                
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
                            if (col==0){                                
                            }
                        }
                    },
                    {
                        match: function (row, col, data) {
                            return (col === 2); //if it is first row
                        },
                        style: {
                            color: 'green', //make the text green and bold
                            fontWeight: 'bold'
                        },
                        title: 'Heading', //make some tooltip
                        readOnly: true //make it read-only
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
                            return autoComEmployeeArray
                        }
                    }                    
                ]               
            });
            getBlanket('continueDIV');
        }
        function saveBonusDetails(){
            if(smonth==null ||smonth.length==0 || syear==null || syear.length==0){
                alert("Please Select the Bonus Month and Year");
            }else{
                getBlanket('continueDIV');
                llpData = container.handsontable("getData");                               
                var curRec=0;
                var totRec=llpData.length-1;
                var strValidChars = "0123456789. ";
                var strChar;
                var proceed= true;
                for (var kj=0;kj<totRec;kj++)
                {
                    for (var i = 0; i < llpData[kj][1].length; i++){                    
                        strChar = llpData[kj][1].charAt(i);
                        if (strValidChars.indexOf(strChar) == -1){
                            proceed=false;
                        }
                    }
                }
                if(proceed)
                {
                    var code=empcodes[llpData[curRec][0]];
                    var earningAmount =llpData[curRec][1];
                    var deductionAmount =llpData[curRec][2];
                    //var attendancemonth=document.getElementById('attendancemonth').value;
                    var bonustype=document.getElementById('bonustype').value;
                    EmployeePayBillAction.saveEmployeeBonusdetails(smonth,syear,bonustype,code,earningAmount,deductionAmount,curRec,totRec,saveBonusDetailSucc);
                }
                else
                {
                    getBlanket('continueDIV');
                    alert("Some non numeric values in grid, Correct it before save!");
                }
            }
        }
        function saveBonusDetailSucc(mapsta){                                 
            llpData = container.handsontable("getData");
            //var attendancemonth=document.getElementById('attendancemonth').value;
            var bonustype=document.getElementById('bonustype').value;
            var curRec=mapsta.currentRecords;
            var totRec=mapsta.totalrecords;            
            if(curRec<=totRec){                
                var code=empcodes[llpData[curRec][0]];
                var earningAmount =llpData[curRec][1];
                var deductionAmount =llpData[curRec][2];
                EmployeePayBillAction.saveEmployeeBonusdetails(smonth,syear,bonustype,code,earningAmount,deductionAmount,curRec,totRec,saveBonusDetailSucc);                                           
            }else{
                alert("saved succesfully");
                container.handsontable("clear");
                llpView();
                getBlanket('continueDIV');
            }
        }
    </script>
</html>
