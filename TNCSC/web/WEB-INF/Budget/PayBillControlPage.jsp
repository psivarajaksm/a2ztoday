<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
    String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    String message = "";
    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");
    }
    request.removeAttribute("message");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">        
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/highcharts.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/exporting.js"></script>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/BudgetAction.js"></script>        
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">
            var startyear=0;
            var endyear=0;
            $(function () {
                
                var chart = new Highcharts.Chart({
                    chart: {
                        renderTo:'containerbubble',
                        defaultSeriesType:'scatter',
                        borderWidth:1,
                        borderColor:'#ccc',
                        marginLeft:110,
                        marginRight:50,
                        backgroundColor:'#eee',
                        plotBackgroundColor:'#fff'
                    },
                    title:{
                        text:'Chart Title'
                    },
                    legend:{
                                     
                    },
                    plotOptions: {
                        series: {
                            shadow:false
                        }
                    },
                    xAxis:{
                        minPadding:.075,
                        maxPadding:.075,
                        lineColor:'#999',
                        lineWidth:1,
                        tickColor:'#666',
                        tickLength:3,
                        title:{
                            text:'X Axis Title'
                        }
                    },
                    yAxis:{
                        lineColor:'#999',
                        lineWidth:1,
                        tickColor:'#666',
                        tickWidth:1,
                        tickLength:3,
                        gridLineColor:'#ddd',
                        title:{
                            text:'Y Axis Title',
                            rotation:0,
                            margin:50
                        }
                    },
                    series: [{
                            marker:{
                                symbol:'circle',
                                fillColor:'rgba(24,90,169,.5)',
                                lineColor:'rgba(24,90,169,.75)',
                                lineWidth:1,
                                color:'rgba(24,90,169,1)',
                                states:{
                                    hover:{
                                        enabled:false
                                    }
                                }
                            },
                            data: [{x:2,y:3,marker:{radius:5}},
                                {x:3,y:12,marker:{radius:10}},
                                {x:5,y:6,marker:{radius:15}},
                                {x:1,y:7,marker:{radius:25}},
                                {x:7,y:18,marker:{radius:18}},
                                {x:4,y:10,marker:{radius:14}},
                                {x:4,y:3,marker:{radius:16}}]
                        },{
                            marker:{
                                symbol:'circle',
                                fillColor:'rgba(238,46,47,.5)',
                                lineColor:'rgba(238,46,47,.75)',
                                lineWidth:1,
                                color:'rgba(238,46,47,1)',
                                states:{
                                    hover:{
                                        enabled:false
                                    }
                                }
                            },
                            data: [{x:3,y:5,marker:{radius:7}},
                                {x:2,y:9,marker:{radius:11}},
                                {x:5,y:7,marker:{radius:23}},
                                {x:4,y:4,marker:{radius:14}},
                                {x:7,y:8,marker:{radius:28}},
                                {x:8,y:13,marker:{radius:6}},
                                {x:6,y:5,marker:{radius:12}}]
                        }]
    
    
    
                });
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                var chart;
                $('#endyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: false,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',                  
                    onClose: function(dateText, inst) {                       
                        startyear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();                        
                        $(this).datepicker('setDate', new Date(startyear, 3, 1));
                    }
                });
                
                $('#startyear').datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: false,
                    changeYear: true,
                    showButtonPanel: true,
                    dateFormat: 'yy',                    
                    onClose: function(dateText, inst) {                       
                        endyear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();                       
                        $(this).datepicker('setDate', new Date(endyear, 4, 1));
                    }
                });
                
                $("#show").click(function() {
                    var startyear=document.getElementById("startyear").value;
                    var endyear=document.getElementById("endyear").value;
                    BudgetAction.getBudgetUpdationDetailsDetails(startyear,endyear,populateDetails); 
                });
    
            });
            function populateDetails(map){                
                document.getElementById('tobecontainer').style.display="block";
                document.getElementById("tobecontainer").innerHTML=map.tobecompleted;
                var detailsArray = new Array(map.length);
                var detailsDataArray = new Array(map.length);
                for (var i = 0; i < map.length; i++) {
                    detailsArray[i]=map[i];
                    detailsDataArray[i]=map[map.length+i];
                }
                chart = new Highcharts.Chart({
                    chart: {
                        renderTo: 'container',
                        type: 'bar'
                    },
                    title: {
                        text: 'Budget Details'
                    },
                    subtitle: {
                        text: 'Year'
                    },
                    xAxis: {
                        //                        categories: ['R01', 'R02', 'R03', 'R04', 'R05', 'R06', 'R07', 'R08', 'R09', 'R10', 'R11', 'R12', 'R13', 'R14', 'R15', 'R16', 'R17', 'R18', 'R19', 'R20', 'R21', 'R22', 'R23', 'R24', 'R25', 'R26', 'R27', 'R28', 'R29', 'R30', 'R31', 'R32', 'R33', 'R34', 'R35', 'R36', 'R37'],
                        categories:   detailsArray,
                        title: {
                            text: null
                        }
                            
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Budget Reguest (in Lakhs)',
                            align: 'high'
                        },
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    tooltip: {
                        formatter: function() {
                            return ''+
                                this.series.name +': '+ this.y +'Lakhs';
                        }
                    },  
                        
                    scrollbar: {
                        enabled: true
                    },
                    plotOptions: {
                        bar: {
                            dataLabels: {
                                enabled: true
                            }
                        }
                    },
                    legend: {
                        layout: 'vertical',
                        align: 'right',
                        verticalAlign: 'top',
                        x: -100,
                        y: 100,
                        floating: true,
                        borderWidth: 1,
                        backgroundColor: '#FFFFFF',
                        shadow: true
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{                                
                            //data: [107.23, 31, 635, 203]
                            data: detailsDataArray
                        }]
                });
            }
        </script>
        <style>
            .ui-datepicker-calendar {
                display: none;
            }
            .ui-datepicker-month{
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
                <table width="100%" align="center" class="tableBorder" border="0" cellpadding="0" cellspacing="0">                  
                    <tr class="darkrow">
                        <td width="29%" class="textalign">A/c Start Month & Year</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="10%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="startyear" id="startyear" readonly>                                                    
                        </td>

                        <td width="29%" class="textalign">A/c End Month & Year</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="10%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="endyear" id="endyear" readonly>                                                    
                        </td>
                        <td width="1%" class="textalign"></td>
                        <td width="1%" class="mandatory"></td>
                        <td width="29%" colspan="2" class="textfieldalign" >
                            <input type="button" class="submitbu" id="show" value="Show Details">
                        </td>
                    </tr>                    
                </table>
                <table width="100%" >
                    <tr>
                        <td width="80%">
                            <div id="container" style="min-width: 400px; height: 800px;"></div>

                            <div id="containerbubble" style="height: 400px"></div>

                        </td>
                        <td width="20%">
                            <div id="tobecontainer" style="display:none;height:500px;overflow:auto;"></div>                            
                        </td>

                    </tr>
                </table>
            </div>
            <div id="budgetdetails" style="display:none;height:300px;overflow:auto;"></div>
            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>   
    <script type="text/javascript">       
    </script>
</html>


