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
    String region = "";
    String month = "";
    String year = "";

    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");
        region = (String) request.getAttribute("region");
        month = (String) request.getAttribute("month");
        year = (String) request.getAttribute("year");
    }
    request.removeAttribute("message");
    request.removeAttribute("region");
    request.removeAttribute("month");
    request.removeAttribute("year");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>File Upload</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>        
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>       
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/PayBillAction.js"></script>     
        <script src="dwr/interface/FileUploadAction.js"></script>             
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>           

        <script type="text/javascript">
            var month;
            var year;
            $(function() {                
                
                $('#monthyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'mm/yy',
                    onClose: function(dateText, inst) {
                        month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(year, month, 1));
                        var mon=parseInt(month)+1;
                        document.getElementById('month').value=mon;
                        document.getElementById('year').value=year;
                    }
                });
                PayBillAction.getUploadedDetails(fillUploadedDetails);
                
                
            });
            
            function readUpLoadFile(obj){
               
            }
            function displayFile(){
                var region=document.getElementById('region').value;
                var year=0;
                var month=0;               
                PayBillAction.checkuploadexists(region,month,year,existingDatas);              
                         
            }
            function existingDatas(map){              
                                  
                document.forms[0].action="PayBillAction.htm";
                document.forms[0].method.value="uploadFile";
                document.forms[0].submit();                                    
            }
        </script>

        <style>
            .ui-datepicker-calendar {
                display: none;
            }
        </style>
    </head>
    <body topmargin="0" marginheight="0" leftmargin="0" marginleft="0" valign="center" >
        <div id="blanket" style="display:none;"></div>
        <div id="continueDIV" style="display:none;" align="center" class="loadingRequest">
            <img src="images/loading_1.gif" align="middle" border="0"><br /><bean:message key="reqinprogress" />
        </div>
        <form method="post" enctype="multipart/form-data">

            <div id="header">
                <%@ include file="/common/header.jsp" %>
            </div>
            <div id="content">
                <table id="commonBodyTable" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" class="headerdata">File Upload</td>
                                </tr>
                            </table>
                            <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="100%" align="center" valign="top" style="background:transparent">
                                        <div id="uploadeddetails" style="display:none;height:350px;overflow:auto;"></div>                                        
                                    </td>
                                </tr>
                            </table>     
                            <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">

                                <tr class="darkrow">
                                    <td width="30%" class="textalign">Region</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="65%" colspan="2" class="textfieldalign" >
                                        <select class="combobox" name="region" id="region" class="textfieldalign" ></select>
                                    </td>
                                </tr>                                           
                                <tr class="lightrow">
                                    <td width="30%" class="textalign">Select Month</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="65%" colspan="2" class="textfieldalign" >
                                        <input type="text" class="textbox" name="monthyear" id="monthyear" readonly>                                                                                                        
                                    </td>
                                </tr>
                                <tr class="darkrow">
                                    <td width="30%" class="textalign">File</td>
                                    <td width="5%" class="mandatory">*</td>
                                    <td width="65%" colspan="2" align="left">                                                    
                                        <input type="file" name="fileuploadname" id="fileuploadname" onchange="readUpLoadFile(this)">&nbsp;&nbsp;&nbsp;                                                    
                                    </td>
                                </tr>    
                                <tr tr class="lightrow">
                                    <td colspan="3" class="textalign"><input type="button" class="submitbu" name="save" id="save" value="Upload" onclick="displayFile()"></td>
                                </tr>   
                            </table>
                            <br>
                            <%if (message.equalsIgnoreCase("success")) {%>
                            <script type="text/javascript">
                                bodyonload();
                                function bodyonload(){
                                    getBlanket('continueDIV');
                                    var region=document.getElementById('region').value;
                                    var region='<%=region%>';
                                    var month='<%=month%>';
                                    var year='<%=year%>';    
                                    PayBillAction.displayTextFileDatas(region,month,year,fillDisplayTextFileDatas);
                                }
                                function fillDisplayTextFileDatas(map){                                    
                                    if(map.ERROR!=null && map.ERROR!=""){
                                        alert(map.ERROR);
                                        getBlanket('continueDIV');
                                        return false;
                                    }else {
                                        alert(map.display);
                                        PayBillAction.getUploadedDetails(fillUploadedDetails);
                                        getBlanket('continueDIV');
                                        //                                        document.getElementById('textFileDetails').style.display="block";
                                        //                                        document.getElementById("textFileDetails").innerHTML=map.display;
                                    }
                                }
                            </script>
                            <% } else if (message.equalsIgnoreCase("Failed")) {%>
                            <script type="text/javascript">
                                alert("Given Text File is Failed to upload");
                            </script>
                            <% }%>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="textFileDetails" style="display:none;height:300px;overflow:auto;"></div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="isolddata" id="isolddata"  value="0" >
            <input type="hidden" name="month" id="month"  >
            <input type="hidden" name="year" id="year"   >
            <input type="hidden" name="todaydate" id="todaydate" value="<%=AppProps.getInstance().getToDayDate()%>" maxlength="10">

        </form>
    </body>
    <script type="text/javascript"> 
        onloadData1();
        function onloadData1(){                   
            FileUploadAction.loadRegionDetails(fillRegionCombo);            
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);           
        }
        function fillUploadedDetails(map){
            document.getElementById('uploadeddetails').style.display="block";
            document.getElementById("uploadeddetails").innerHTML=map.uploadeddetails;
            
            oTable = $('#paydet').dataTable({
                "bJQueryUI": true,
                "sScrollY":"250px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers"
            }); 
            
        }
    </script>
</html>

