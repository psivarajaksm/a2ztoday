<%--
    Document   : PayCodeMaster
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
        <title>Section Master Creation</title>
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
        <script src="dwr/interface/PayBillMasterAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $("#clearbutton").click(function(){
                    document.getElementById('sectionname').value="";
                    document.getElementById('region').value="0";
                    document.getElementById('section').value="0";
                    document.getElementById('modifysectioncode').value="0";
                    bodyOnload();
                });
                $("#savebutton").click(function(){
                    
                    var region;
                    var sectionname;
                    var section;
                    var sectioncode;
                    

                    //                    sectiontype=document.getElementById('sectiontype').value;
                    region=document.getElementById('region').value;
                    section=document.getElementById('section').value;
                    sectionname=document.getElementById('sectionname').value;
                    sectioncode=document.getElementById('sectioncode').value;
                    
                    if(region=="0"){
                        alert("Please Select The Region");
                        document.getElementById('region').focus();
                        return false;
                    }else if(section=="0"){
                        alert("Please Select the Type of Section");
                        document.getElementById('section').focus();
                        return false;
                    }else if(sectionname==""){
                        alert("Please Enter the Section Name");
                        document.getElementById('sectionname').focus();
                        return false;
                    }else{

                        
                        var modifysectioncode=document.getElementById('modifysectioncode').value;
                        if(modifysectioncode!="0"){
                            var answer = confirm("Do You Want to Continue to modified to save?");
                            if (answer){
                                sectioncode=modifysectioncode;
                                getBlanket('continueDIV');
                                PayBillMasterAction.saveSectionMaster(region,section,sectioncode,sectionname,masterSaveDetails);    
                            }else{
                                document.getElementById('sectionname').value="";
                                document.getElementById('region').value="0";
                                document.getElementById('section').value="0";
                                document.getElementById('modifysectioncode').value="0";
                                bodyOnload();
                            }
                        }else{
                            getBlanket('continueDIV');
                            PayBillMasterAction.saveSectionMaster(region,section,sectioncode,sectionname,masterSaveDetails);
                        }
                        
                    }

                    //                    if(sectiontype=="0"){
                    //                        alert("Please Select Type of Section/SubSection")
                    //                        document.getElementById('sectiontype').focus();
                    //                        return false;
                    //                    }else if(region=="0"){
                    //                        alert("Please Select The Region")
                    //                        document.getElementById('region').focus();
                    //                        return false;
                    //                    }else{
                    //                        if(sectiontype=="s"){
                    //                            section="0";
                    //                            if(sectionname==""){
                    //                                alert("Please Enter the Section Name")
                    //                                document.getElementById('sectionname').focus();
                    //                                return false;
                    //                            }else{
                    //                                var sectioncode=document.getElementById('sectioncode').value;
                    //                                getBlanket('continueDIV');
                    //                                //                            alert()
                    //                                PayBillMasterAction.saveSectionMaster(sectiontype,region,sectioncode,sectionname,section,masterSaveDetails);
                    //                            }
                    //                        }else if(sectiontype=="ss"){
                    //                            section=document.getElementById('section').value;
                    //                            sectionname=document.getElementById('subsectionname').value;
                    //
                    //                            if(section=="0"){
                    //                                alert("Please Select the Section for add new Sub section")
                    //                                document.getElementById('section').focus();
                    //                                return false;
                    //                            }else if(sectionname==""){
                    //                                alert("Please Enter the Sub Section Name")
                    //                                document.getElementById('subsectionname').focus();
                    //                                return false;
                    //                            }else{
                    //                                var sectioncode=document.getElementById('sectioncode').value;
                    //                                getBlanket('continueDIV');
                    //                                //                            alert()
                    //                                PayBillMasterAction.saveSectionMaster(sectiontype,region,sectioncode,sectionname,section,masterSaveDetails);
                    //                            }
                    //                        }
                    //                    }
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
                    document.getElementById('sectioncode').value=map.sectioncode;
                    document.getElementById("sectionDetails").innerHTML=map.sectionDetails;
                    document.getElementById('sectionname').value="";
                    //                    document.getElementById('subsectionname').value="";
                    document.getElementById('region').value="0";
                    document.getElementById('section').value="0";
                    document.getElementById('modifysectioncode').value="0";
                    //                    document.getElementById('sectiontype').value="0";
                    //                    document.getElementById('sectiondivid').style.display="none";
                    //                    document.getElementById('subsectiondivid').style.display="none";
                    oTable = $('#sectiontable').dataTable({
                        "bJQueryUI": true,
                        "sScrollY":"215px",
                        "bSort": true,
                        "bFilter": true,
                        'iDisplayLength': 100,
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
                        <td colspan="4" class="mainheader"> Section Master Creation</td>
                    </tr>
                </table>
                <table width="70%" align="center" class="tableBorder2"  border="0" cellpadding="2" cellspacing="0">
                    <tr class="darkrow">
                        <td width="29%" class="textalign">Region</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="region" id="region" class="textfieldalign" onchange="loadregionsections();" ></select>
                        </td>
                        <td width="29%" class="textalign">Type of Section</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="20%" colspan="4" class="textfieldalign" >
                            <select class="combobox" name="section" id="section" class="textfieldalign" onchange="loadregionsections();"></select>
                        </td>
                        <!--                        <td width="29%" class="textalign">Add Section / Sub Section</td>
                                                <td width="1%" class="mandatory">*</td>
                                                <td width="20%" colspan="4" class="textfieldalign" >
                                                    <select class="combobox" name="sectiontype" id="sectiontype" onchange="changeSectionType(this.value);">
                                                        <option value="0">--Select--</option>
                                                        <option value="s">Section</option>
                                                        <option value="ss">SubSection</option>
                                                    </select>
                                                </td>
                                                <td width="29%" class="textalign">Region</td>
                                                <td width="1%" class="mandatory">*</td>
                                                <td width="20%" colspan="4" class="textfieldalign" >
                                                    <select class="combobox" name="region" id="region" class="textfieldalign" onchange="loadregionsections(this.value);" ></select>
                                                </td>-->
                    </tr>
                    <tr class="lightrow">
                        <td width="74%" colspan="10"  class="textalign">Section Name</td>
                        <td width="1%" class="mandatory">*</td>
                        <td width="25%" class="textfieldalign" >
                            <input type="text" class="textbox" name="sectionname" id="sectionname">
                        </td>
                    </tr>
                    <!--                    <tr class="lightrow">
                                            <td colspan="12" >
                                                <div id="sectiondivid" style="display:none;">
                                                    <table width="100%" align="center"   border="0" cellpadding="2" cellspacing="0">
                                                        <tr class="lightrow">
                                                            <td width="79%" colspan="10"  class="textalign">Section Name</td>
                                                            <td width="1%" class="mandatory">*</td>
                                                            <td width="20%" class="textfieldalign" >
                                                                <input type="text" class="textbox" name="sectionname" id="sectionname">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div id="subsectiondivid" style="display:none;">
                                                    <table width="100%" align="center"   border="0" cellpadding="2" cellspacing="0">
                                                        <tr class="lightrow">
                                                            <td width="29%" class="textalign">Section</td>
                                                            <td width="1%" class="mandatory">*</td>
                                                            <td width="20%" colspan="4" class="textfieldalign" >
                                                                <select class="combobox" name="section" id="section" class="textfieldalign" ></select>
                                                            </td>
                                                            <td width="29%" class="textalign">Sub Section Name</td>
                                                            <td width="1%" class="mandatory">*</td>
                                                            <td width="20%" colspan="4" class="textfieldalign" >
                                                                <input type="text" class="textbox" name="subsectionname" id="subsectionname">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>-->


                    <!--                    <tr class="lightrow">
                                            <td width="29%" class="textalign">Region</td>
                                            <td width="1%" class="mandatory">*</td>
                                            <td width="20%" colspan="4" class="textfieldalign" >
                                                <select class="combobox" name="region" id="region" class="textfieldalign" ></select>
                                            </td>
                                            <td width="29%" class="textalign">Section Name</td>
                                            <td width="1%" class="mandatory">*</td>
                                            <td width="20%" colspan="4" class="textfieldalign" >
                                                <select class="combobox" name="regionname" id="regionname"></select>
                                            </td>
                                        </tr>-->
                    <!--                    <tr class="darkrow">
                                            <td width="30%" class="textalign"> Section Code</td>
                                            <td width="5%" class="mandatory">*</td>
                                            <td width="65%" colspan="2" class="textfieldalign" >
                                                <input type="text" class="textbox" name="regioncode" id="regioncode" readonly>
                                            </td>
                                        </tr>
                                        <tr class="lightrow">
                                            <td width="30%" class="textalign"> Section Name</td>
                                            <td width="5%" class="mandatory">*</td>
                                            <td width="65%" colspan="2" class="textfieldalign" >
                                                <input type="text" class="textbox" name="regionname" id="regionname">
                                            </td>
                                        </tr>-->
                    <tr class="darkrow">
                        <td width="100%" colspan="12" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                            <input type="button" CLASS="submitbu" name="clearbutton" id="clearbutton" value="Clear">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="sectionDetails" style="height:310px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">
            <input type="hidden" name="sectioncode" id="sectioncode">
            <input type="hidden" name="modifysectioncode" id="modifysectioncode" value="0">
            <input type="hidden" name="modifysectionname" id="modifysectionname" value="">
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            PayBillMasterAction.getSectionDetails(displaySectionDetails)
        }

        function displaySectionDetails(map){
            dwr.util.removeAllOptions("region");
            dwr.util.addOptions("region",map.regionlist);
            dwr.util.removeAllOptions("section");
            dwr.util.addOptions("section",map.sectionlist);
            document.getElementById("sectionDetails").innerHTML=map.sectionDetails;
            document.getElementById('sectioncode').value=map.sectioncode;
            oTable = $('#sectiontable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"215px",
                "bSort": true,
                "bFilter": true,
                'iDisplayLength': 100,
                "sPaginationType": "full_numbers"
            });
        }
        function getSectionMasterDetailsForModify(sectionCode,sectionName,parentcode,region){
            document.getElementById('modifysectioncode').value=sectionCode;
            document.getElementById('region').value=region;
            document.getElementById('sectionname').value=sectionName;
            document.getElementById('modifysectionname').value=sectionName;
            document.getElementById('section').value=parentcode;

            //            if(parentcode=="0"){
            //                document.getElementById('sectiondivid').style.display="block";
            //                document.getElementById('subsectiondivid').style.display="none";
            //                document.getElementById('sectionname').value=sectionName;
            //                document.getElementById('sectiontype').value="s";
            //            }else{
            //                document.getElementById('sectiondivid').style.display="none";
            //                document.getElementById('subsectiondivid').style.display="block";
            //                document.getElementById('section').value=parentcode;
            //                document.getElementById('subsectionname').value=sectionName;
            //                document.getElementById('sectiontype').value="ss";
            //            }
        }

        //        function changeSectionType(sectype){
        //            if(sectype=="0"){
        //                document.getElementById('sectiondivid').style.display="none";
        //                document.getElementById('subsectiondivid').style.display="none";
        //            }else if(sectype=="s"){
        //                document.getElementById('sectiondivid').style.display="block";
        //                document.getElementById('subsectiondivid').style.display="none";
        //            }else if(sectype=="ss"){
        //                //                PayBillMasterAction.getLoadSections(displaySections);
        //                document.getElementById('sectiondivid').style.display="none";
        //                document.getElementById('subsectiondivid').style.display="block";
        //            }
        //        }
        //        function displaySections(map){
        //            document.getElementById('sectiondivid').style.display="none";
        //            document.getElementById('subsectiondivid').style.display="block";
        //            dwr.util.removeAllOptions("section");
        //            dwr.util.addOptions("section",map.sectionlist);
        //        }
        function loadregionsections(){
            var regionid=document.getElementById('region').value;
            var sectionid=document.getElementById('section').value;
            if(regionid!="0"){
                PayBillMasterAction.getLoadRegionSections(regionid,sectionid,displayregionSections);
            }
            
        }

        function displayregionSections(map){
            if(map.sectionid=="0"){
                dwr.util.removeAllOptions("section");
                dwr.util.addOptions("section",map.sectionlist);
            }
            document.getElementById("sectionDetails").innerHTML=map.sectionDetails;
            oTable = $('#sectiontable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"215px",
                "bSort": true,
                "bFilter": true,
                'iDisplayLength': 100,
                "sPaginationType": "full_numbers"
            });
        }
    </script>
</html>