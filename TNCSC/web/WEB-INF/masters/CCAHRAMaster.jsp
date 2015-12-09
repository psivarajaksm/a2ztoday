<%-- 
    Document   : CCAHRAMaster
    Created on : Jul 20, 2012, 11:12:05 AM
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
        <title>CCAHRA Master Creation</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>  
        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/PayBillMasterAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>        
    </head>
    <script>
        function remove(el) {            
            var paycodeIds=document.getElementById("paycodeIds").value;
            //                var paycodeid=paycode+"$";
            //                paycodeIds=paycodeIds.replace(paycodeid, "");
            //                alert("paycodeIds=="+paycodeIds);
            //                document.getElementById("paycodeIds").value=paycodeIds;
            $(el).hide();
            var currentSelectedColumnId = $(el).attr('id')+"$";            
            paycodeIds = paycodeIds.replace(currentSelectedColumnId, "");
            document.getElementById("paycodeIds").value=paycodeIds;

            setTimeout(function() {
                $(el).parent().remove();
                //                    $(el).parent().parent().remove();
            }, 100);
        }
            
        $(function() {
            $("#addbutton").click(function(){
                var paytype=document.getElementById("paytype").value;
                var itemid=document.getElementById("paycodetypes").value;
                   
                if(paytype=="0"){
                    alert("Please Select the Pay Type")
                    document.getElementById('paytype').focus();
                    return false;
                }
                else if(itemid=="0"){
                    alert("Please Select the Pay Name")
                    document.getElementById('paycodetypes').focus();
                    return false;
                }else{
                    var paycodeIds=document.getElementById("paycodeIds").value;
                    paycodeIds=paycodeIds+itemid+"$";
                    document.getElementById("paycodeIds").value=paycodeIds;
                    var item=document.getElementById("paycodetypes").options[document.getElementById("paycodetypes").selectedIndex].text;
                    var html = '<div class="item icart">';
                    html = html + '<div class="divrm">';
                    html = html + item+" ";
                    html = html + '&nbsp;&nbsp;<a onclick="remove(this)" class="remove '+itemid+'"  id="'+itemid+'"   >&times;</a>';
                    html = html + '<div/></div>';
                    $("#cart_items").append(html);
                }
                    

            });

            $("#savebutton").click(function(){
                var paytype=document.getElementById("paytype").value;
                var itemid=document.getElementById("paycodetypes").value;
                var paycodeIds=document.getElementById("paycodeIds").value;
                if(paytype=="0"){
                    alert("Please Select the Pay Type")
                    document.getElementById('paytype').focus();
                    return false;
                }
                else if(itemid=="0"){
                    alert("Please Select the Pay Name")
                    document.getElementById('paycodetypes').focus();
                    return false;
                }else{
                    var answer = confirm("Do You Want to Continue?");
                    if (answer){
                        getBlanket('continueDIV');
                        PayBillMasterAction.saveCCAHRAMaster(paytype,paycodeIds,
                        masterSaveDetails);
                    }
                }

            });
        });

        function masterSaveDetails(map){
            if(map.ERROR!=null && map.ERROR!=""){
                getBlanket('continueDIV');
                alert(map.ERROR);
                return false;
            } else {
                getBlanket('continueDIV');
                alert(map.success);
                document.getElementById('paytype').value="0";
                document.getElementById('paycodetypes').value="0";
                document.getElementById('paycodeIds').value="";
                document.getElementById('cart_items').innerHTML="";
                  
            }
        }
    </script>
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
                <table width="100%" align="center" class="tableBorder2" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td colspan="4" class="mainheader">CCA And HRA Master Creation</td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Type</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >

                            <select class="combobox" name="paytype" id="paytype" onchange="getAssignedPays(this)">
                                <option value="0">--Select--</option>
                                <option value="E07">CCA</option>
                                <option value="E06">HRA</option>
                                <option value="E04">DA</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Pay</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="paycodetypes" id="paycodetypes" class="textfieldalign" ></select>
                            <input type="button" CLASS="submitbu" name="addbutton" id="addbutton" value="Add">
                        </td>
                    </tr>                    
                    <tr class="lightrow">
                        <td width="30%" class="textalign"></td>
                        <td width="5%" class="mandatory"></td>
                        <td width="65%" colspan="2"  >
                            <div id="cart_items" style="border:4px;border-color:#0497c6;"></div>
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
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
            PayBillMasterAction.getLoadEarningsDetails(fillEarningsCombo)
        }
        function fillEarningsCombo(map){
            dwr.util.removeAllOptions("paycodetypes");
            dwr.util.addOptions("paycodetypes",map.earningsList);
        }        
        function getAssignedPays(obj){
            var paytype=obj.value;
            if(paytype=="0"){
                alert("Please Select the Type");
                document.getElementById('paytype').focus();
                return false;
            }else{
                PayBillMasterAction.getLoadAssignedPayCodes(paytype,fillAssignedPayCodes)
            }
        }
        function fillAssignedPayCodes(map){
            document.getElementById('cart_items').innerHTML=map.paycodeDetails;
        }
        
    </script>
</html>




