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
        <title>Party Ledger Master Creation</title>
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
                    var partyname=document.getElementById('partyname').value;
                    var tinno=document.getElementById('tinno').value;
                    if(region=="0"){
                        alert("Please Select the Region")
                        document.getElementById('region').focus();
                        return false;
                    }else if(partyname==""){
                        alert("Please Enter the Bank Name")
                        document.getElementById('partyname').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');
                            var partycode=document.getElementById('partycode').value;
                            AccountsMastersAction.savePartyLedgerMaster(region,partyname,partycode,tinno,partyMasterSaveDetails);
                        }
                    }
                });
            });

            function partyMasterSaveDetails(map){
                if(map.ERROR!=null && map.ERROR!=""){
                    getBlanket('continueDIV');
                    alert(map.ERROR);
                    return false;
                } else {
                    getBlanket('continueDIV');
                    alert(map.success);
                    document.getElementById('region').value="0";
                    document.getElementById('partycode').value="0";
                    document.getElementById('partyname').value="";
                    document.getElementById("partyDetails").innerHTML=map.partyDetails;
                    oTable = $('#partyledgertable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"175px",
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
                        <td width="100%" colspan="4" align="center" class="headerdata">Party Ledger Master</td>
                    </tr>
                    <tr>
                        <td colspan="4" class="mainheader">Party Ledger Master</td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Region</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <select class="combobox" name="region" id="region" class="textfieldalign" onchange="loadRegionParties(this);" ></select>
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="30%" class="textalign">Party Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="partyname" id="partyname">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="30%" class="textalign">Tin No</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="tinno" id="tinno">
                        </td>
                    </tr>
                    <tr class="lightrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="partyDetails" style="height:270px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="partycode" id="partycode" value="0">
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
        function loadRegionParties(obj){
            var regionid=obj.value;
            if(regionid=="0"){
                alert("Please Select the Region");
                document.getElementById("partyDetails").style.display="none";
                document.getElementById('partyname').value="";
                return false;
            }else{
                document.getElementById('partycode').value="0";
                document.getElementById('partyname').value="";
                AccountsMastersAction.getRegionPartiesDetails(regionid,fillRegionPartiessList)
            }

        }
        function fillRegionPartiessList(map){
            document.getElementById("partyDetails").style.display="block";
            document.getElementById("partyDetails").innerHTML=map.partyDetails;
            oTable = $('#partyledgertable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"175px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers"
            });
        }

        function ModifyPartyName(partycode,regionCode,bankName,tinno){
            document.getElementById('partycode').value=partycode;
            document.getElementById('region').value=regionCode;
            document.getElementById('partyname').value=bankName;
            document.getElementById('tinno').value=tinno;
        }
    </script>
</html>