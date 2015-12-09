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
        <title>Employee Earnings Slap Details</title>
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <script src="dwr/interface/PayBillMasterAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $('#effectdate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: "dd/mm/yy" }).val();

                $("#savebutton").click(function(){
                    var amtrangefrom=new Array();
                    var amtrangeto=new Array();
                    var slapamount=new Array();
                    var slappercentage=new Array();
                    var hiddeniarray=new Array();
                    var funtype=document.getElementById('funtype').value;
                    var earningstypes=document.getElementById('earningstypes').value;
                    var effectdate=document.getElementById('effectdate').value;
                    //                    var amountrangefrom=document.getElementById('amountrangefrom').value;
                    //                    var amountrangeto=eval(document.getElementById('amountrangeto').value);
                    //                    var slapamount=document.getElementById('slapamount').value;
                    //                    var slappercentage=document.getElementById('slappercentage').value;
                    var orderno=document.getElementById('orderno').value;
                    var totalrows=document.getElementById('totalrows').value;
                    var answer = confirm("Do You Want to Continue?");
                    for (rowId=1;rowId<=totalrows;rowId++){
                        amtrangefrom[rowId]=document.getElementById('amountrangefrom'+rowId).value;
                        amtrangeto[rowId]=document.getElementById('amountrangeto'+rowId).value;
                        slapamount[rowId]=document.getElementById('slapamount'+rowId).value;
                        slappercentage[rowId]=document.getElementById('slappercentage'+rowId).value;
                        hiddeniarray[rowId]=document.getElementById('hiddedid'+rowId).value;
                    }

                    if(funtype=="0"){
                        alert("Please Select the function Type");
                        document.getElementById('funtype').focus();
                        return false;
                    }else if(earningstypes=="0"){
                        alert("Please Enter the Employee Earnings Type");
                        document.getElementById('earningstypes').focus();
                        return false;
                    }else if(effectdate==""){
                        alert("Please Select the Effect Date");
                        document.getElementById('effectdate').focus();
                        return false;
                    }
                    //                    else if(amountrangeto=="0"){
                    //                        alert("Please Enter the Amount To Range");
                    //                        document.getElementById('amountrangeto').focus();
                    //                        return false;
                    //                    }else if(amountrangeto==""){
                    //                        alert("Please Enter the Amount To Range");
                    //                        document.getElementById('amountrangeto').focus();
                    //                        return false;
                    //                    }
                    else if(orderno==""){
                        alert("Please Enter the Order Number");
                        document.getElementById('orderno').focus();
                        return false;
                    }else {
                        if (answer){
                            getBlanket('continueDIV');
                            PayBillMasterAction.saveEarningsSlapDetails(earningstypes,effectdate,amtrangefrom,amtrangeto,slapamount
                            ,slappercentage,orderno,totalrows,hiddeniarray,funtype,saveSlapDetails);
                        }
                    }
                });
            });
            function saveSlapDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    alert(map.success);
                    getBlanket('continueDIV');
                    document.getElementById('funtype').value="0";
                    document.getElementById('earningstypes').value="0";
                    document.getElementById('effectdate').value="";
                    document.getElementById('orderno').value="0";
                    document.getElementById('totalrows').value="";
                    document.getElementById("displayrowinHTML").innerHTML = "";
                    document.getElementById('displayrows').style.display="none";
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

                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">Employee Earnings Slap Details</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Function Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="funtype" id="funtype" onchange="clearDivs()">
                                <option value="0">--Select--</option>
                                <option value="1">Addition</option>
                                <option value="2">Modification</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Earnings Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="earningstypes" id="earningstypes" class="textfieldalign"  onchange="getDatasforModification()"></select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Effect From</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" id="effectdate" name="effectdate"  size="20" />                            
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Order Number</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%"class="textfieldalign"><input type="text" class="amounttextbox"  id="orderno"  size="20"/></td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Total Number of Rows</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" class="textfieldalign"><input type="text" class="amounttextbox"  id="totalrows" onblur="createRows(this.value)"  size="20"/></td>
                    </tr>
                </table>
                <div id="displayrows" align="center" style="width:70%;display:none;padding-left:250px;">
                    <div id="displayrowinHTML" style="height:135px;overflow:auto;"></div>
                    <div>
                        <table width="100%" align="center"  border="0" cellpadding="2" cellspacing="0">
                            <tr >
                                <td width="100%" colspan="4" align="center">
                                    <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            PayBillMasterAction.getLoadEarningsTypes(fillEarningsCombo);
        }
        function fillEarningsCombo(map){
            dwr.util.removeAllOptions("earningstypes");
            dwr.util.addOptions("earningstypes",map.earningsList);
        }
        function createRows(noofrows){
            var funtype=document.getElementById('funtype').value;
            var earningstypes=document.getElementById('earningstypes').value;
            var effectdate=document.getElementById('effectdate').value;
            var orderno=document.getElementById('orderno').value;
            if(funtype=="0"){
                alert("Please Select the function Type");
                document.getElementById('funtype').focus();
                return false;
            }else if(earningstypes=="0"){
                alert("Please Enter the Employee Earnings Type");
                document.getElementById('earningstypes').focus();
                return false;
            }else if(effectdate==""){
                alert("Please Select the Effect Date");
                document.getElementById('effectdate').focus();
                return false;
            }else if(orderno==""){
                alert("Please Enter the Order Number");
                document.getElementById('orderno').focus();
                return false;
            }else{
                if(noofrows!=0){
                    getBlanket('continueDIV');
                    PayBillMasterAction.createRowinHTML(noofrows,fillcreateRows);
                }
            }
        }

        function fillcreateRows(map){
            if(map.ERROR!=null && map.ERROR!=""){
                getBlanket('continueDIV');
                alert(map.ERROR);
                return false;
            } else {
                getBlanket('continueDIV');
                document.getElementById("displayrowinHTML").innerHTML = map.createrows;
                document.getElementById('displayrows').style.display="block";
                    
            }
        }
        function clearDivs(){
            document.getElementById("displayrowinHTML").innerHTML = "";
            document.getElementById('displayrows').style.display="none";
            document.getElementById('earningstypes').value="0";
            document.getElementById('effectdate').value="";
            document.getElementById('orderno').value="0";
            document.getElementById('totalrows').value="";
        }
        function getDatasforModification(){
            var funtype=document.getElementById('funtype').value
            var earningstypes=document.getElementById('earningstypes').value
             if(funtype=="0"){
                alert("Please Select the function Type");
                document.getElementById('funtype').focus();
                return false;
            }else if(earningstypes=="0"){
                alert("Please Enter the Employee Earnings Type");
                document.getElementById('earningstypes').focus();
                return false;
            }else if(funtype=="2"){
                getBlanket('continueDIV');
                PayBillMasterAction.getDatasForModify(earningstypes,fillDataforModify);
            }
        }
        function fillDataforModify(map){
            if(map.ERROR!=null && map.ERROR!=""){
                getBlanket('continueDIV');
                alert(map.ERROR);
                return false;
            } else {
                getBlanket('continueDIV');
                document.getElementById('orderno').value=map.orderNo;
                document.getElementById('effectdate').value=map.effectDate;
                document.getElementById('totalrows').value=map.totalrows;
                document.getElementById("displayrowinHTML").innerHTML = map.createrows;
                document.getElementById('displayrows').style.display="block";

            }
        }
    </script>
</html>

