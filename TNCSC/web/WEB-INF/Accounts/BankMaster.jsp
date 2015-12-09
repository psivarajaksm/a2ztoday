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
        <title>Bank Master Creation</title>
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
        <script src="dwr/interface/AccountsMastersAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $("#savebutton").click(function(){

                    var region=document.getElementById('region').value;
                    var bankname=document.getElementById('bankname').value;
                    if(region=="0"){
                        alert("Please Select the Region")
                        document.getElementById('region').focus();
                        return false;
                    }else if(bankname==""){
                        alert("Please Enter the Bank Name")
                        document.getElementById('bankname').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                                getBlanket('continueDIV');
                                var bankcode=document.getElementById('bankcode').value;
                                AccountsMastersAction.saveBankLedgerMaster(region,bankname,bankcode,bankMasterSaveDetails);
                        }
                    }
                });
            });

            function bankMasterSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.getElementById('region').value="0";
                    document.getElementById('bankcode').value="0";
                    document.getElementById('bankname').value="";
                    document.getElementById("bankDetails").innerHTML=map.bankDetails;
                    oTable = $('#banksledgertable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"180px",
                        "bSort": true,
                        "bFilter": true,
                        "sPaginationType": "full_numbers"
                    });

                }
            }
        </script>
    </head>
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
                        <td width="100%" colspan="4" align="center" class="headerdata">Bank Master</td>
                    </tr>
                    <tr>
                        <td colspan="4" class="mainheader">Bank Master</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Region</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="region" id="region" class="textfieldalign" onchange="loadRegionBanks(this);" ></select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Bank Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="bankname" id="bankname">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="bankDetails" style="height:280px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="bankcode" id="bankcode" value="0">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            AccountsMastersAction.loadRegionDetails(fillRegionCombo)
        }
        function fillRegionCombo(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
        }
        function loadRegionBanks(obj){
            var regionid=obj.value;
            if(regionid=="0"){
                alert("Please Select the Region");
                document.getElementById("bankDetails").style.display="none";
                document.getElementById('bankname').value="";
                return false;
            }else{
                document.getElementById('bankcode').value="0";
                document.getElementById('bankname').value="";
                AccountsMastersAction.getRegionBankDetails(regionid,fillRegionBanksList)
            }

        }
        function fillRegionBanksList(map){
            document.getElementById("bankDetails").style.display="block";
            document.getElementById("bankDetails").innerHTML=map.bankDetails;
            oTable = $('#banksledgertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"180px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers"
            });
        }

         function ModifyBankName(bankcode,regionCode,bankName){
             document.getElementById('bankcode').value=bankcode;
            document.getElementById('region').value=regionCode;
            document.getElementById('bankname').value=bankName;
        }
    </script>
</html>