<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<!-- <LINK REL ="StyleSheet" HREF="CustomizeStyle1.css" TYPE="text/css">-->
<%
            String staticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
            String message = "";
            if ((request.getAttribute("message") != null)) {
                message = (String) request.getAttribute("message");
            }
            request.removeAttribute("message");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <!--        <META http-equiv="Content-Style-Type" content="text/css">-->
        <link href="<%=staticPath%>css/onward.css" rel="stylesheet" type="text/css" />

        <link href="<%=staticPath%>css/global.css" rel="stylesheet" type="text/css" />
        <style type="text/css">
            /*demo page css*/
            body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
            .demoHeaders { margin-top: 2em; }
            ul#icons {margin: 0; padding: 0;}
            ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
            ul#icons span.ui-icon {float: left; margin: 0 4px;}
        </style>
    </head>
    <body>
        <form method="post">

            <div id="content">
                <h2><%=message%></h2>
<!--                <h2>No Record(s) Found for the Given Input</h2>-->
            </div>
            <div id="footer"><!--
                <%@ include file="/common/footer.jsp" %>
                -->            </div>
        </form>
    </body>
</html>
