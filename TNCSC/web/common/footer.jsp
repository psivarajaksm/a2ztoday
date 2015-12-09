<%-- 
    Document   : footer
    Created on : Apr 27, 2011, 8:21:42 AM
    Author     : Jagan Mohan. B
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<%
    String footerStaticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
%>

<div style="width:100%; margin-top:0px;">
    <%--table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td class="footerTopRow">&nbsp;</td>
        </tr>
        <tr>
            <td valign="top">
                <table width="100%" border="0" cellspacing="4" cellpadding="4" background="images/bgMiddTddfop.gif" >
                    <tr class="footerPage">
                        <td width="30%" align="center">&nbsp;</td>
                        <td width="40%" align="center" class="homefooter_m">                            
                            <a href="#">Feedback</a> |
                            <a href="#">Disclaimer</a> <br>
                            This site is best viewed in Firefox 3.x and Internet Explorer 6.x, 7.x, 8.x
                        </td>
                        <td width="30%">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr class="footerPage">
                                    <td align="right">Powered by&nbsp;</td>
                                    <td align="left"><a href="http://www.onwardgroup.com" target="_blank"><img src="images/oesl_logo.gif" width="120" height="50" border="0" /></a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table--%>
    <table class="footerTopRow" border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>
            <tr>
                <td width="14%">&nbsp;</td>
                <td width="67%"><div align="center"><span class="footer">© Copyright 2011-12 Tamil Nadu Civil Supplies Corporation. All rights Reserved.</span></div></td>
                <td width="19%">&nbsp;</td>
            </tr>
        </tbody>
    </table>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" background="images/bgMiddTddfop.gif" >
        <tr class="footerPage">
            <td width="10%" align="center">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr class="footerPage">
                        <td align="center"><a href="http://www.elcot.in" target="_blank"><img src="<%=footerStaticPath %>images/elcot_logo.png" border="0" title="www.elcot.in" /></a></td>
                    </tr>
                </table>
            </td>
            <td width="75%" align="center" class="homefooter_m">
                <a href="#">Feedback</a> |
                <a href="#">Disclaimer</a> <br>
                This site is best viewed in Firefox 3.x and Internet Explorer 6.x, 7.x, 8.x
            </td>
            <td width="15%">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr class="footerPage">
                        <td align="right">Powered by&nbsp;&nbsp;</td>
                        <td align="left"><a href="http://www.onwardgroup.com" target="_blank"><img src="<%=footerStaticPath %>images/onward_logo.png" border="0" title="www.onwardgroup.com" /></a></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
