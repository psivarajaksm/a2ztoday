<%-- 
    Document   : jquerys
    Created on : Jul 5, 2012, 10:26:37 AM
    Author     : Sivaraja. P
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
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="content">
                <div align="center" id="toolbar">
                    <table width="80%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="darkrow">
                            <td class="textalign">Month</td>
                            <td><input type="text" id="attendancemonth"  size="20" /></td>
                            <!--                            <td class="textalign">Region  :</td>
                                                        <td ><select class="combobox" name="region" id="region" class="textfieldalign" ></select></td>
                                                        <td class="textalign">Section :</td>
                                                        <td ><select class="combobox" name="section" id="section"></select></td>-->
                            <td > <input id="Attendance" type="button" class="submitbu" value="show" tabindex="26"  >&nbsp;&nbsp;&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="7">
                                <table width="80%" align="center"  border="0" cellpadding="0" cellspacing="0">                                  
                                    <tr>
                                        <td colspan="13">
                                            <div  id="attendanceGrid" class="dataTable" class="dataTable" style="width: 800px;
                                                  height: 300px; overflow: scroll" ></div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td width="380px" colspan="7" align="center">
                                <input id="saveAttendance" type="button" class="submitbu" value="Save" tabindex="26" >&nbsp;&nbsp;&nbsp;
                            </td>

                        </tr>
                    </table>                   
                </div>

                <script>
                    var totalrecords=0;                  
                    var container = $("#attendanceGrid");
                    $(function() {
                        $('#attendancemonth').datepicker({ dateFormat: "dd/mm/yy" }).val();
                        $("#Attendance").click(function(){
                            var region=0;
                            var section=0;
                            var epfno=0;
                            var attendancemonth=document.getElementById('attendancemonth').value;
                            EmployeePayBillAction.getEmployeeListForLLP(region,section,epfno,attendancemonth,fillLLPExcellSheet);                           
                        });
                       

                        $("#saveAttendance").click(function(){
                            llpSave();                          
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
        </form>
    </body>
    <script type="text/javascript">
//        bodyonload();
//        function bodyonload(){
//            EmployeePayBillAction.loadRegionDetails(fillRegionCombo);
//        }
//        function fillRegionCombo(map){
//            dwr.util.removeAllOptions("region");
//            dwr.util.addOptions("region",map.regionlist);
//
//        }       
        
        
        function saveEmployeeLLPSucc(mapsta){              
                    
            llpData = container.handsontable("getData");   
            var attendancemonth=document.getElementById('attendancemonth').value;

                     
            var curRec=mapsta.currentRecords;
            var totRec=mapsta.totalrecords;          
            var code=empcodes[llpData[curRec][0]];                               
            var llpdays =llpData[curRec][1];
            if(curRec<totRec){
                EmployeePayBillAction.saveEmployeeLLP(attendancemonth,code,llpdays,curRec,totRec,saveEmployeeLLPSucc);                                           
            }   
            else
            {
                getBlanket('continueDIV');
                alert("Saved Successfully");
                    
            }
            
        }
        

        function llpSave(){    
            getBlanket('continueDIV');
            llpData = container.handsontable("getData");                               
            var curRec=0; 
            var totRec=llpData.length-1;  
            var code=empcodes[llpData[curRec][0]];             
            // alert(llpData[curRec][1]);
            var llpdays =llpData[curRec][1];           
            var attendancemonth=document.getElementById('attendancemonth').value;     
            //alert(code+llpdays);
            EmployeePayBillAction.saveEmployeeLLP(attendancemonth,code,llpdays,curRec,totRec,saveEmployeeLLPSucc);                                                      
        }
        function fillLLPExcellSheet(map){
            var slno=0;
            var autoComEmployeeArray = new Array(map.length);
            for (var i = 0; i < map.length; i++) {
                autoComEmployeeArray[i]=map[i];
            }
            empcodes=map;
            llpdetails= map.llpdetails;
            
            row=llpdetails.length;
            totalrecords=map.length;
            container.handsontable({
                rows: row,
                cols: 2,
                rowHeaders: false, //turn off 1, 2, 3, ..  
                minSpareRows: 1,                
                rowHeaders: true,
                colHeaders: true,
                colHeaders: ["EMPLOYEE NAME", "LEAVE LOSS OF PAY DAYS"],
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
                            if (parseInt(data()[row][col], 10) < 0) { //if row contains negative number
                                container.handsontable('getCell', row, col).className = 'negative'; //add class "negative"
                            }
                            else {
                                container.handsontable('getCell', row, col).className = '';
                                if (col==1){
                                    if(xx.length)
                                    {
                                        container.handsontable('getCell', row, col).className = '';  
                                        var strValidChars = "0123456789,.-";
                                        var strChar;
                                        for (var i = 0; i < xx.length; i++){
                                            strChar = xx.charAt(i);
                                            if (strValidChars.indexOf(strChar) == -1){
                                                container.handsontable('getCell', row, col).className = 'negative';
                                            }
                                        }
                                    }
                                }
                                if(slno>map.length){
                                    setTimeout(function () {
                                        //timeout is needed because Handsontable normally deselects
                                        //current cell when you click outside the table
                                        container.handsontable("selectCell", row+1, 0);
                                    }, 10);
                                }
                            }
                            if (col==0){
                                var x=(data()[row][col]);
                                //                                alert(x);
                                //                                alert(autoComEarningsArray.indexOf(x));
                                if (autoComEmployeeArray.indexOf(x) == -1){                                   
                                   
                                    container.handsontable('getCell', row, col).className = 'negative'; //add class "negative"                                        
                                }
                                else
                                {
                                    container.handsontable('getCell', row, col).className = '';    
                                    
                                }   
                                slno=slno+1;
                                if(slno>map.length){
                                    setTimeout(function () {
                                        //timeout is needed because Handsontable normally deselects
                                        //current cell when you click outside the table
                                        container.handsontable("selectCell", row, 1);
                                    }, 10);
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
                            return autoComEmployeeArray
                        }
                    }
                    
                ]  
               
            });

            llpdetails= map.llpdetails;
            var x = new Array(llpdetails.length);
            for (var i = 0; i < llpdetails.length; i++) {
                x[i] = new Array(1);
            }
            
            for (var j = 0; j < llpdetails.length; j++)
            {
                x[j][0]=llpdetails[j];
                var e=parseInt(llpdetails.length)+j;
                x[j][1]=llpdetails[e];
            }
            container.handsontable("loadData", x);
        }

    </script>
</html>

