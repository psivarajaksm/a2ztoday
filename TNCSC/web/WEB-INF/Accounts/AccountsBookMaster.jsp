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
        <title>Accounts Books</title>
        <link type="text/css" href="<%=staticPath%>css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery-ui-1.8.21.custom.min.js"></script>
        <link type="text/css" href="<%=staticPath%>css/shCore.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="<%=staticPath%>css/demo_table.css" rel="stylesheet" />        
        <link type="text/css" href="<%=staticPath%>css/demo_table_jui.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/shCore.js"></script>
        <script type="text/javascript" src="<%=staticPath%>scripts/jquery.dataTables.js"></script>

<!--        <link type="text/css" href="<%=staticPath%>ckeditor/sample.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=staticPath%>scripts/ckeditor.js"></script>-->


        <script src="<%=staticPath%>scripts/blanket.js"></script>
        <script src="dwr/interface/AccountsMastersAction.js"></script>
        <script src="dwr/engine.js"></script>
        <script src="dwr/util.js"></script>
        <script type="text/javascript">

            $(function() {
                $("#savebutton").click(function(){
                    
                    var accbookname=document.getElementById('accbookname').value;
                    if(accbookname==""){
                        alert("Please Enter Account Book Name")
                        document.getElementById('accbookname').focus();
                        return false;
                    }else{
                        var answer = confirm("Do You Want to Continue?");
                        if (answer){
                            getBlanket('continueDIV');                                
                            AccountsMastersAction.saveAccountsBook(accbookname,fillAccountsBookList);
                        }
                    }
                });
            });
           
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
                        <td colspan="4" class="mainheader">Accounts Books Master</td>
                    </tr>                    
                    <tr class="lightrow">
                        <td width="30%" class="textalign">New Account Book Name</td>
                        <td width="5%" class="mandatory">*</td>
                        <td width="65%" colspan="2" class="textfieldalign" >
                            <input type="text" class="textbox" name="accbookname" id="accbookname">
                        </td>
                    </tr>
                    <tr class="darkrow">
                        <td width="100%" colspan="4" align="center">
                            <input type="button" CLASS="submitbu" name="savebutton" id="savebutton" value="Save">
                        </td>
                    </tr>
                </table>
            </div>
<!--            <textarea class="ckeditor" cols="80" id="editor1" name="editor1" rows="10">-->

<!--            </textarea>-->

            <div id="bookDetails" style="height:370px;overflow:auto;"> </div>

            <div id="footer">
                <%@ include file="/common/footer.jsp" %>
            </div>
            <input type="hidden" name="method">            
        </form>
    </body>
    <script type="text/javascript">
        bodyOnload();
        function bodyOnload(){
            getBlanket('continueDIV');    
            AccountsMastersAction.getAccountBooksDetails(fillAccountsBookList);
        }      
      
        function fillAccountsBookList(map){
            getBlanket('continueDIV');    
            document.getElementById("bookDetails").style.display="block";
            document.getElementById("bookDetails").innerHTML=map.bookDetails;
            oTable = $('#booktable').dataTable({
                "bJQueryUI": true,
                "sScrollY":"275px",
                "bSort": true,
                "bFilter": true,
                "sPaginationType": "full_numbers"
            });
        }
       
    </script>
</html>