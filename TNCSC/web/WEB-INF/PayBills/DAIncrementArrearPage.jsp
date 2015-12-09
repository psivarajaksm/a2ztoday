<%-- 
    Document   : DAIncrementArrearPage
    Created on : Dec 13, 2012, 11:33:55 AM
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
        <title>DA Increment Arrear Process</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="dwr/interface/DAIncrementArrearAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var empmaster=new Array;
            var serialno="0";
            var progressstatus=0;
            $(function() {
                $('#fromdate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: "dd/mm/yy" }).val();
                $('#todate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: "dd/mm/yy" }).val();
                $('#asondate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: "dd/mm/yy" }).val();

                $("#dabutton").click(function(){                         
                    var chkboxes=document.getElementsByName("epfnos");
                    for (i=0;i<chkboxes.length;i++){
                        if (chkboxes[i].checked==true){
                            empmaster.push(chkboxes[i].value);                           
                        }

                    }  
                    if(chkboxes.length>0){
                        var fromdate=document.getElementById('fromdate').value;
                        var todate=document.getElementById('todate').value;
                        var asondate=document.getElementById('asondate').value;
                        var batchno=document.getElementById('batchno').value;
                        var incrementpercentage=document.getElementById('incrementpercentage').value;
                        if(asondate==""){
                            alert("Please Select the As On Date");
                            document.getElementById('asondate').focus();
                            return false;
                        }else if(incrementpercentage==""){
                            alert("Please Enter the DA Percentage");
                            document.getElementById('incrementpercentage').focus();
                            return false;
                        }else if(fromdate==""){
                            alert("Please Select the From Date");
                            document.getElementById('fromdate').focus();
                            return false;
                        }else if(todate==""){
                            alert("Please Select the To Date");
                            document.getElementById('todate').focus();
                            return false;
                        }else{
                            var answer = confirm("Do You Want to Continue?");
                            if (answer){
                                getBlanket('continueDIV');                                                        
                                DAIncrementArrearAction.saveDAIncrementProcess(fromdate,todate,asondate,incrementpercentage,serialno,empmaster[serialno],batchno,daProcessResult);
                            }
                        }
                    
                    }else{
                        alert("Please select the employee");                        
                        return false;
                    }
                });

                $("#progressbar").progressbar({
                    value: progressstatus
                });
            });
            function daProcessResult(map){
                progressstatus=progressstatus+1;
                $("#progressbar").progressbar({
                    value: progressstatus
                });
                if(progressstatus>=100){
                    progressstatus=0;
                }
                if(map.proceed == "yes"){
                    serialno=map.serialno;
                    document.getElementById('batchno').value=map.batchno;
                    var fromdate=document.getElementById('fromdate').value;
                    var todate=document.getElementById('todate').value;
                    var asondate=document.getElementById('asondate').value;
                    var batchno=document.getElementById('batchno').value;
                    var incrementpercentage=document.getElementById('incrementpercentage').value;
                    
                    DAIncrementArrearAction.saveDAIncrementProcess(fromdate,todate,asondate,incrementpercentage,serialno,empmaster[serialno],batchno,daProcessResult);
                }
                else
                {
                    $("#progressbar").progressbar({
                        value: 100
                    });
                    getBlanket('continueDIV');
                    document.getElementById('batchno').value="";
                    alert(map.reason);
                    document.getElementById('employeedetails').style.display="none";
                }
            }


        </script>
    </head>
    <body>
        <form method="post">
            <div id="blanket" style="display:none;"></div>
            <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
                <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
            </div>
            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                    <!--                    <tr>
                                            <td width="100%" align="center" class="headerdata">DA Increment Arrear Process</td>
                                        </tr>                   -->
                    <td width="100%" class="mainheader">DA Increment Arrear Process</td>
                </table>
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">                    
                    <tr class="darkrow">

                        <td width="4%" class="textalign">DA</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%"  >
                            <input type="text" class="textbox" name="daname" id="daname" readonly>
                        </td>


                        <td width="8%" class="textalign">As On Date</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%"  >
                            <input type="text" class="textbox" name="asondate" id="asondate" readonly>
                        </td>

                        <td width="8%" class="textalign">DA Inc %</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%" >
                            <input type="text" class="amounttextbox" name="incrementpercentage" id="incrementpercentage" maxlength="5">
							<!--                            <input type="text" class="amounttextbox" name="incrementpercentage" id="incrementpercentage" maxlength="5"  onblur="checkPercentage(this.id,'DA Percentage');">-->
                        </td>

                        <td width="8%" class="textalign">From</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%"  >
                            <input type="text" class="textbox" name="fromdate" id="fromdate" readonly>
                        </td>

                        <td width="8%" class="textalign">To</td>
                        <td width="2%" class="mandatory">*</td>
                        <td width="8%" >
                            <input type="text" class="textbox" name="todate" id="todate" readonly>
                        </td>                         
                        <td width="8%" >
                            <input type="button" CLASS="submitbu" name="showdet" id="showdet" value="Show Emp" onclick="getEmployeeListHTML();" >                           
                        </td>

                    </tr>
                </table>
                <div id="employeedetails" style="display:none;height:330px;overflow:auto;"></div>
                <table width="80%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">                      
                        <td width="65%" colspan="2" class="textfieldalign">Batch 
                            <select class="combobox" name="dabatch" id="dabatch" onchange="daBatchChange(this.value)"></select>
                        </td>
                    </tr>  

                    <tr>
                        <td align="center">
                            <input type="button" CLASS="submitbu" name="dabutton" id="dabutton" value="DA Process">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="progressbar"></div>
                        </td>
                    </tr>
                </table>
                <br>
                <div id="processedepfnos">                    
                </div>
            </div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="batchno" id="batchno">

        </form>
    </body>

    <script>
        bodyOnLoad();

        function bodyOnLoad(){
            DAIncrementArrearAction.loadSectionDetails(fillRegionCombo);
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("section");
            dwr.util.addOptions("section",map.sectionlist);
        }
        function getEmployeeListHTML(){
            DAIncrementArrearAction.getEmployeeListHTML("",fillEmployeemap);
        }
        function fillEmployeemap(map){               
            document.getElementById('employeedetails').style.display="block";
            document.getElementById("employeedetails").innerHTML=map.employeelist;
            document.getElementById('daname').value=map.daname;
            document.getElementById('batchno').value="";      
            
            dwr.util.removeAllOptions("dabatch");
            dwr.util.addOptions("dabatch",map.dabatchlist);
            
            
            oTable = $('#employeetable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"235px",
                "bSort": true,
                "bFilter": true,
                "bPaginate": false
                //                "sPaginationType": "full_numbers"
                //                'iDisplayLength': 20,
                //                "aLengthMenu": [[20, 40, 100, -1], [20, 50, 100, "All"]]
            });
        }
        function daBatchChange(batchno){
            document.getElementById('batchno').value=batchno;              
        }

        function checkAll(a)
        {
            var receiptAll=document.getElementsByName("daselectall");
            var epfnocheckbox=document.getElementsByName("epfnos");
            //            alert("recLen.length==="+recLen.length);
            if(receiptAll[0].checked==true)
            {
                for(var i=0; i<epfnocheckbox.length;i++)
                {
                    epfnocheckbox[i].checked=true;
                }
            }
            else
            {
                for (var i=0; i<epfnocheckbox.length;i++)
                {
                    epfnocheckbox[i].checked=false;
                }
            }
        }
    </script>
</html>



