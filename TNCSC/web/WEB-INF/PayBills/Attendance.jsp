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
        <link rel="stylesheet" media="screen" href="<%=staticPath%>css/jquery.handsontable.css">
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
        </style>
    </head>


    <body>
        <form method="post">
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>          
            <div id="content">
                <div align="center" id="toolbar">
                    <table width="80%" border="0" cellspacing="0" cellpadding="0">
                        <tr class="darkrow">
                            <td class="textalign">Month</td>
                            <td><input type="text" id="attendancemonth"  size="20" /></td>
                            <td class="textalign">Region  :</td>
                            <td ><select class="combobox" name="region" id="region" class="textfieldalign" ></select></td>
                            <td class="textalign">Section :</td>
                            <td ><select class="combobox" name="section" id="section"></select></td>
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
                    <table width="80%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                        <tr>
                            <td>
                                <div id="progressbar"></div>
                            </td>
                        </tr>
                    </table>
                </div>

                <script>
                    var totalrecords=0;
                    var progressstatus=0;
                    var container = $("#attendanceGrid");
                    $(function() {
                        $('#attendancemonth').datepicker({ dateFormat: "dd/mm/yy" }).val();
                        $("#Attendance").click(function(){
                            var region=0;
                            var section=0;
                            var epfno=0;
                            EmployeePayBillAction.getEmployeeListForAttendance(region,section,epfno,fillAttendanceExcellSheet);                           
                        });
                        $("#progressbar").progressbar({
                            value: progressstatus
                        });

                        $("#saveAttendance").click(function(){
                            //                            setTimeout(function () {
                            //                                //timeout is needed because Handsontable normally deselects
                            //                                //current cell when you click outside the table
                            //                                //"EPFNO", "NAME", "PRESENT", "WOFF", "ELDAYS","MLDAYS","CLDAYS","NDAYS","SUSPDAYS","OTHERS","LLP","ULEP","TOTALDAYS"
                            //                                container.handsontable("selectCell", 0, 0);
                            //                            }, 10);

                            var attendancemonth=document.getElementById('attendancemonth').value;
                            datax = container.handsontable("getData");
                            
                            var epfno=datax[0][0];
                            var name=datax[0][1];
                            var present=datax[0][2];
                            var woff=datax[0][3];
                            var eldays=datax[0][4];
                            var mldays=datax[0][5];
                            var cldays=datax[0][6];
                            var ndays=datax[0][7];
                            var susdays=datax[0][8];
                            var others=datax[0][9];
                            var llp=datax[0][10];
                            var ulep=datax[0][11];
                            var totdays=datax[0][12];
                            var curRec=0;
                            var totRec=totalrecords;
                            EmployeePayBillAction.saveEmployeeListForAttendance(attendancemonth,epfno,name,present,woff,eldays,mldays,cldays,ndays,susdays,others,llp,ulep,totdays,curRec,totRec,attendanceSaveDetails);
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
        bodyonload();
        function bodyonload(){
            EmployeePayBillAction.loadRegionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);

        }

        function attendanceSaveDetails(map){

            progressstatus=progressstatus+1;
            $("#progressbar").progressbar({
                value: progressstatus
            });
            if(progressstatus>=100){
                progressstatus=0;
            }

            var attendancemonth=document.getElementById('attendancemonth').value;
            var curRec=map.currentRecords;
            var totRec=map.totalrecords;
            if(curRec<totRec){
                datax = container.handsontable("getData");
                var epfno=datax[curRec][0];
                var name=datax[curRec][1];
                var present=datax[curRec][2];
                var woff=datax[curRec][3];
                var eldays=datax[curRec][4];
                var mldays=datax[curRec][5];
                var cldays=datax[curRec][6];
                var ndays=datax[curRec][7];
                var susdays=datax[curRec][8];
                var others=datax[curRec][9];
                var llp=datax[curRec][10];
                var ulep=datax[curRec][11];
                var totdays=datax[curRec][12];
                EmployeePayBillAction.saveEmployeeListForAttendance(attendancemonth,epfno,name,present,woff,eldays,mldays,cldays,ndays,susdays,others,llp,ulep,totdays,curRec,totRec,attendanceSaveDetails);
            }
            else
            {
                $("#progressbar").progressbar({
                    value: 100
                });
                alert(map.message);
            }
        }


        function fillAttendanceExcellSheet(map){
            row=map.length;
            totalrecords=map.length;
            container.handsontable({
                rows: row,
                cols: 13,
                rowHeaders: false, //turn off 1, 2, 3, ...               
                colHeaders: true,
                colHeaders: ["EPFNO", "NAME", "PRESENT", "WOFF", "ELDAYS","MLDAYS","CLDAYS","NDAYS","SUSPDAYS","OTHERS","LLP","ULEP","TOTALDAYS"],
                legend: [
                  
                    {
                        match: function (row, col, data) {
                            return (col === 0); //if it is first row
                        },
                        style: {
                            color: 'green', //make the text green and bold
                            fontWeight: 'bold'
                        },
                        title: 'Heading', //make some tooltip
                        readOnly: true //make it read-only
                    }
                ]
               
            });

            var x = new Array(map.length);
            for (var i = 0; i < map.length; i++) {
                x[i] = new Array(12);
            }

            for (var j = 0; j < row; j++)
            {
                x[j][0]=map[j];
                var e=parseInt(row)+j;
                x[j][1]=map[e];
            }
            container.handsontable("loadData", x);
        }

    </script>
</html>

