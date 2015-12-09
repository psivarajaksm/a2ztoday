<%-- 
    Document   : LoginPage
    Created on : Jul 6, 2012, 11:12:45 AM
    Author     : root
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.onward.util.AppProps"%>
<%@ page import="com.onward.common.ApplicationConstants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    String loginStaticPath = AppProps.getInstance().getProperty(ApplicationConstants.STATIC_IP_PATH);
    System.out.println("loginStaticPath====" + loginStaticPath);
%>

<!--<link rel="shortcut icon" href="<%=loginStaticPath%>images/onwardlogo.gif" type="image/x-icon">-->
<%
    String path = request.getContextPath();
    String message = "";
    if ((request.getAttribute("message") != null)) {
        message = (String) request.getAttribute("message");
    }
    request.removeAttribute("message");
%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge, chrome=1" />
        <meta name="description" content="JavaScript desktop environment built with jQuery." />
        <title>jQuery Desktop</title>
        <!--[if lt IE 7]>
        <script>
        window.top.location = 'http://desktop.sonspring.com/ie.html';
        </script>
        <![endif]-->
        <link rel="stylesheet" href="<%=loginStaticPath%>assets/css/reset.css" />
        <link rel="stylesheet" href="<%=loginStaticPath%>assets/css/desktop.css" />
        <script type="text/javascript" src="<%=loginStaticPath%>scripts/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=loginStaticPath%>scripts/ddaccordion.js"></script>

       
        <!--[if lt IE 9]>
        <link rel="stylesheet" href="<%=loginStaticPath%>assets/css/ie.css" />
        <![endif]-->
        <script>
            $(function() {
               
                $("#processbutton").click(function(){
                    alert("ssss");
                    $.getScript('<%=loginStaticPath%>scripts/common.js',function(){ sairam();});
                  
                });
             
              
            });
    
    
            //Initialize Arrow Side Menu:
            ddaccordion.init({
                headerclass: "menuheaders", //Shared CSS class name of headers group
                contentclass: "menucontents", //Shared CSS class name of contents group
                revealtype: "clickgo", //Reveal content when user clicks or onmouseover the header? Valid value: "click", or "mouseover"
                mouseoverdelay: 200, //if revealtype="mouseover", set delay in milliseconds before header expands onMouseover
                collapseprev: true, //Collapse previous content (so only one open at any time)? true/false 
                defaultexpanded: [0], //index of content(s) open by default [index1, index2, etc]. [] denotes no content.
                onemustopen: false, //Specify whether at least one header should be open always (so never all headers closed)
                animatedefault: false, //Should contents open by default be animated into view?
                persiststate: true, //persist state of opened contents within browser session?
                toggleclass: ["unselected", "selected"], //Two CSS classes to be applied to the header when it's collapsed and expanded, respectively ["class1", "class2"]
                togglehtml: ["none", "", ""], //Additional HTML added to the header when it's collapsed and expanded, respectively  ["position", "html1", "html2"] (see docs)
                animatespeed: 500, //speed of animation: integer in milliseconds (ie: 200), or keywords "fast", "normal", or "slow"
                oninit:function(expandedindices){ //custom code to run when headers have initalized
                    //do nothing
                },
                onopenclose:function(header, index, state, isuseractivated){ //custom code to run whenever a header is opened or closed
                    //do nothing
                }
            })

            
        </script>


    </head>
    <body>
        <div class="abs" id="wrapper">
            <div class="abs" id="desktop">
                <a class="abs icon" style="left:20px;top:20px;" href="#icon_dock_computer">
                    <img src="<%=loginStaticPath%>assets/images/icons/icon_32_computer.png" />
                    Computer
                </a>
                <a class="abs icon" style="left:20px;top:100px;" href="#icon_dock_drive">
                    <img src="<%=loginStaticPath%>assets/images/icons/icon_32_drive.png" />
                    Hard Drive
                </a>
                <a class="abs icon" style="left:20px;top:180px;" href="#icon_dock_disc">
                                        <img src="<%=loginStaticPath%>assets/images/icons/icon_32_disc.png" />
                                        Audio CD
                                    </a>
                                    <a class="abs icon" style="left:20px;top:260px;" href="#icon_dock_network">
                                        <img src="<%=loginStaticPath%>assets/images/icons/icon_32_network.png" />
                                        Network
                                    </a>
                                    <div id="window_computer" class="abs window">
                                        <div class="abs window_full">
                                            <div class="window_top">
                                                <span class="float_left">
                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                    Computer
                                                </span>
                                                <span class="float_right">
                                                    <a href="#" class="window_min"></a>
                                                    <a href="#" class="window_resize"></a>
                                                    <a href="#icon_dock_computer" class="window_close"></a>
                                                </span>
                                            </div>
                                            <div class="abs window_content">
                                                <div class="window_aside" style="width:250px">
                                                    <div class="arrowsidemenu">

                                                        <div><a href="#" title="Home">Home</a></div>
                                                        <div class="menuheaders"><a href="# title="CSS">CSS Library</a></div>
                                                        <ul class="menucontents">
                                                            <li><a href="#">Horizontal CSS Menus</a></li>
                                                            <li><a href="#">Vertical CSS Menus</a></li>
                                                            <li><a href="#">Image CSS</a></li>
                                                            <li><a href="#">Form CSS</a></li>
                                                            <li><a href="#">DIVs and containers</a></li>
                                                            <li><a href="#">Links & Buttons</a></li>
                                                            <li><a href="#">Other</a></li>
                                                            <li><a href="#">Browse All</a></li>
                                                        </ul>
                                                        <div><a href="#" title="Forums">Forums</a></div>
                                                        <div class="menuheaders"><a href="#" title="JavaScript">JavaScript</a></div>
                                                        <ul class="menucontents">
                                                            <li><a href="#">JavaScript Reference</a></li>
                                                            <li><a href="#">Free JavaScripts</a></li>
                                                        </ul>
                                                        <div><a href="#" title="Tools">Webmaster Tools</a></div>

                                                    </div>
                                                </div>
                                                <div class="window_main">
                                                    <table class="data">
                                                        <thead>
                                                            <tr>
                                                                <th class="shrink">
                                                                    <input id="processbutton" type="button" class="submitbu" value="Submit" tabindex="26" >
                                                                </th>
                                                                <th>
                                                                    Name
                                                                </th>
                                                                <th>
                                                                    Date Modified
                                                                </th>
                                                                <th>
                                                                    Date Created
                                                                </th>
                                                                <th>
                                                                    Size
                                                                </th>
                                                                <th>
                                                                    Kind
                                                                </th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_drive.png" />
                                                                </td>
                                                                <td>
                                                                    Hard Drive
                                                                </td>
                                                                <td>
                                                                    Today
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    200 GB
                                                                </td>
                                                                <td>
                                                                    Volume
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_disc.png" />
                                                                </td>
                                                                <td>
                                                                    Audio CD
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    2.92 GB
                                                                </td>
                                                                <td>
                                                                    Media
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_network.png" />
                                                                </td>
                                                                <td>
                                                                    Network
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    LAN
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder_remote.png" />
                                                                </td>
                                                                <td>
                                                                    Shared Project Files
                                                                </td>
                                                                <td>
                                                                    Yesterday
                                                                </td>
                                                                <td>
                                                                    12/29/08
                                                                </td>
                                                                <td>
                                                                    524 MB
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_documents.png" />
                                                                </td>
                                                                <td>
                                                                    Documents
                                                                </td>
                                                                <td>
                                                                    Yesterday
                                                                </td>
                                                                <td>
                                                                    12/29/08
                                                                </td>
                                                                <td>
                                                                    524 MB
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_system.png" />
                                                                </td>
                                                                <td>
                                                                    Preferences
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    System
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_trash.png" />
                                                                </td>
                                                                <td>
                                                                    Trash
                                                                </td>
                                                                <td>
                                                                    Today
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    Bin
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="abs window_bottom">
                                                Build: TK421
                                            </div>
                                        </div>
                                        <span class="abs ui-resizable-handle ui-resizable-se"></span>
                                    </div>
                                    <div id="window_drive" class="abs window">
                                        <div class="abs window_full">
                                            <div class="window_top">
                                                <span class="float_left">
                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_drive.png" />
                                                    Hard Drive
                                                </span>
                                                <span class="float_right">
                                                    <a href="#" class="window_min"></a>
                                                    <a href="#" class="window_resize"></a>
                                                    <a href="#icon_dock_drive" class="window_close"></a>
                                                </span>
                                            </div>
                                            <div class="abs window_content">
                                                <div class="window_aside">

                                                    <ul>
                                                        <li>
                                                            <a class="menu_trigger" href="#">jQuery Desktop</a>
                                                            <ul class="menu">
                                                                <li>
                                                                    <a href="http://www.amazon.com/dp/0596159773?tag=sons-20">jQuery Cookbook</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://jqueryenlightenment.com/">jQuery Enlightenment</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://jquery.com/">jQuery Home</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://jquerymobile.com/">jQuery Mobile</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://jqueryui.com/">jQuery UI</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://learningjquery.com/">Learning jQuery</a>
                                                                </li>
                                                            </ul>
                                                        </li>
                                                        <li>
                                                            <a class="menu_trigger" href="#">HTML5 Resources</a>
                                                            <ul class="menu">
                                                                <li>
                                                                    <a href="http://diveintohtml5.info/">Dive Into HTML5</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://www.alistapart.com/articles/get-ready-for-html-5/">Get Ready for HTML5</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://html5boilerplate.com/">HTML5 Boilerplate</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://html5doctor.com/">HTML5 Doctor</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://html5.org/">HTML5 Intro</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://www.zeldman.com/superfriends/">HTML5 Super Friends</a>
                                                                </li>
                                                            </ul>
                                                        </li>
                                                        <li>
                                                            <a class="menu_trigger" href="#">Code</a>
                                                            <ul class="menu">
                                                                <li>
                                                                    <a href="<%=loginStaticPath%>assets/css/desktop.css">Desktop - CSS</a>
                                                                </li>
                                                                <li>
                                                                    <a href="<%=loginStaticPath%>assets/js/jquery.desktop.js">Desktop - JavaScript</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://github.com/nathansmith/jQuery-Desktop">GitHub Repository</a>
                                                                </li>
                                                            </ul>
                                                        </li>
                                                        <li>
                                                            <a class="menu_trigger" href="#">Credits</a>
                                                            <ul class="menu">
                                                                <li>
                                                                    <a href="http://sonspring.com/journal/jquery-desktop">Demo built by Nathan Smith</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://adrian-rodriguez.net/">Wallpaper by Adrian Rodriguez</a>
                                                                </li>
                                                                <li>
                                                                    <a href="http://tango.freedesktop.org/Tango_Desktop_Project">Icons - Tango Desktop Project</a>
                                                                </li>
                                                            </ul>
                                                        </li>
                                                    </ul>


                                                    Storage in use: 119.1 GB

                                                </div>
                                                <div class="window_main">
                                                    <table class="data">
                                                        <thead>
                                                            <tr>
                                                                <th class="shrink">
                                                                    &nbsp;
                                                                </th>
                                                                <th>
                                                                    Name
                                                                </th>
                                                                <th>
                                                                    Date Modified
                                                                </th>
                                                                <th>
                                                                    Date Created
                                                                </th>
                                                                <th>
                                                                    Size
                                                                </th>
                                                                <th>
                                                                    Kind
                                                                </th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_page.png" />
                                                                </td>
                                                                <td>
                                                                    .DS_Store
                                                                </td>
                                                                <td>
                                                                    Yesterday
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    6 KB
                                                                </td>
                                                                <td>
                                                                    Hidden
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder_home.png" />
                                                                </td>
                                                                <td>
                                                                    Default User
                                                                </td>
                                                                <td>
                                                                    Today
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder.png" />
                                                                </td>
                                                                <td>
                                                                    Applications
                                                                </td>
                                                                <td>
                                                                    Yesterday
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder.png" />
                                                                </td>
                                                                <td>
                                                                    Developer
                                                                </td>
                                                                <td>
                                                                    12/29/08
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder.png" />
                                                                </td>
                                                                <td>
                                                                    Library
                                                                </td>
                                                                <td>
                                                                    09/11/09
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder.png" />
                                                                </td>
                                                                <td>
                                                                    System
                                                                </td>
                                                                <td>
                                                                    Yesterday
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    &mdash;
                                                                </td>
                                                                <td>
                                                                    Folder
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="abs window_bottom">
                                                Free: 80.9 GB
                                            </div>
                                        </div>
                                        <span class="abs ui-resizable-handle ui-resizable-se"></span>
                                    </div>
                                    <div id="window_disc" class="abs window">
                                        <div class="abs window_inner">
                                            <div class="window_top">
                                                <span class="float_left">
                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_disc.png" />
                                                    Audio CD - Title of Album
                                                </span>
                                                <span class="float_right">
                                                    <a href="#" class="window_min"></a>
                                                    <a href="#" class="window_resize"></a>
                                                    <a href="#icon_dock_disc" class="window_close"></a>
                                                </span>
                                            </div>
                                            <div class="abs window_content">
                                                <div class="window_aside align_center">
                                                    <img src="<%=loginStaticPath%>assets/images/misc/album_cover.jpg" />
                                                    <br />
                                                    <em>Title of Album</em>
                                                </div>
                                                <div class="window_main">
                                                    <table class="data">
                                                        <thead>
                                                            <tr>
                                                                <th class="shrink">
                                                                    &nbsp;
                                                                </th>
                                                                <th class="shrink">
                                                                    Track
                                                                </th>
                                                                <th>
                                                                    Song Name
                                                                </th>
                                                                <th class="shrink">
                                                                    Length
                                                                </th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    01
                                                                </td>
                                                                <td>
                                                                    Track One
                                                                </td>
                                                                <td class="align_right">
                                                                    3:50
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    02
                                                                </td>
                                                                <td>
                                                                    Track Two
                                                                </td>
                                                                <td class="align_right">
                                                                    3:50
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    03
                                                                </td>
                                                                <td>
                                                                    Track Three
                                                                </td>
                                                                <td class="align_right">
                                                                    4:02
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    04
                                                                </td>
                                                                <td>
                                                                    Track Four
                                                                </td>
                                                                <td class="align_right">
                                                                    3:47
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    05
                                                                </td>
                                                                <td>
                                                                    Track Five
                                                                </td>
                                                                <td class="align_right">
                                                                    4:38
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    06
                                                                </td>
                                                                <td>
                                                                    Track Six
                                                                </td>
                                                                <td class="align_right">
                                                                    3:16
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    07
                                                                </td>
                                                                <td>
                                                                    Track Seven
                                                                </td>
                                                                <td class="align_right">
                                                                    3:53
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    08
                                                                </td>
                                                                <td>
                                                                    Track Eight
                                                                </td>
                                                                <td class="align_right">
                                                                    1:41
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    09
                                                                </td>
                                                                <td>
                                                                    Track Nine
                                                                </td>
                                                                <td class="align_right">
                                                                    3:40
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    10
                                                                </td>
                                                                <td>
                                                                    Track Ten
                                                                </td>
                                                                <td class="align_right">
                                                                    4:33
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    11
                                                                </td>
                                                                <td>
                                                                    Track Eleven
                                                                </td>
                                                                <td class="align_right">
                                                                    3:49
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    12
                                                                </td>
                                                                <td>
                                                                    Track Twelve
                                                                </td>
                                                                <td class="align_right">
                                                                    1:11
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td class="shrink">
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_music.png" />
                                                                </td>
                                                                <td class="align_center">
                                                                    13
                                                                </td>
                                                                <td>
                                                                    Track Thirteen
                                                                </td>
                                                                <td class="align_right">
                                                                    6:17
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="abs window_bottom">
                                                Genre: Rock/Rap
                                            </div>
                                        </div>
                                        <span class="abs ui-resizable-handle ui-resizable-se"></span>
                                    </div>
                                    <div id="window_network" class="abs window">
                                        <div class="abs window_inner">
                                            <div class="window_top">
                                                <span class="float_left">
                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_network.png" />
                                                    Network
                                                </span>
                                                <span class="float_right">
                                                    <a href="#" class="window_min"></a>
                                                    <a href="#" class="window_resize"></a>
                                                    <a href="#icon_dock_network" class="window_close"></a>
                                                </span>
                                            </div>
                                            <div class="abs window_content">
                                                <div class="window_aside">
                                                    Local Network Resources
                                                </div>
                                                <div class="window_main">
                                                    <table class="data">
                                                        <thead>
                                                            <tr>
                                                                <th class="shrink">
                                                                    &nbsp;
                                                                </th>
                                                                <th>
                                                                    Name
                                                                </th>
                                                                <th class="shrink">
                                                                    Operating System
                                                                </th>
                                                                <th class="shrink">
                                                                    Version
                                                                </th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_server.png" />
                                                                </td>
                                                                <td>
                                                                    Urban Terror - <em>Game Server</em>
                                                                </td>
                                                                <td>
                                                                    Linux
                                                                </td>
                                                                <td>
                                                                    Ubuntu
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_folder_remote.png" />
                                                                </td>
                                                                <td>
                                                                    Shared Project Files
                                                                </td>
                                                                <td>
                                                                    Linux
                                                                </td>
                                                                <td>
                                                                    Red Hat
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_vpn.png" />
                                                                </td>
                                                                <td>
                                                                    Remote Desktop VPN
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    XP
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Michael Scott
                                                                </td>
                                                                <td>
                                                                    Mac OS
                                                                </td>
                                                                <td>
                                                                    10.5
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Dwight Schrute
                                                                </td>
                                                                <td>
                                                                    Mac OS
                                                                </td>
                                                                <td>
                                                                    10.6
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Jim Halpert
                                                                </td>
                                                                <td>
                                                                    Mac OS
                                                                </td>
                                                                <td>
                                                                    10.6
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Pam Beesly
                                                                </td>
                                                                <td>
                                                                    Mac OS
                                                                </td>
                                                                <td>
                                                                    10.5
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Ryan Howard
                                                                </td>
                                                                <td>
                                                                    Mac OS
                                                                </td>
                                                                <td>
                                                                    10.5
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Jan Levinson
                                                                </td>
                                                                <td>
                                                                    Mac OS
                                                                </td>
                                                                <td>
                                                                    10.5
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Roy Anderson
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    7
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Stanley Hudson
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Kevin Malone
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Angela Martin
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Oscar Martinez
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Phyllis Lapin
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Creed Bratton
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    7
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Meredith Palmer
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Toby Flenderson
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Darryl Philbin
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Kelly Kapoor
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <img src="<%=loginStaticPath%>assets/images/icons/icon_16_computer.png" />
                                                                </td>
                                                                <td>
                                                                    Andy Bernard
                                                                </td>
                                                                <td>
                                                                    Windows
                                                                </td>
                                                                <td>
                                                                    Vista
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="abs window_bottom">
                                                LAN: Corporate Intranet
                                            </div>
                                        </div>
                                        <span class="abs ui-resizable-handle ui-resizable-se"></span>
                                    </div>
                </div>
                <div class="abs" id="bar_top">
                    <span class="float_right" id="clock"></span>
                    <ul>
                        <li>
                            <a class="menu_trigger" href="#">jQuery Desktop</a>
                            <ul class="menu">
                                <li>
                                    <a href="http://www.amazon.com/dp/0596159773?tag=sons-20">jQuery Cookbook</a>
                                </li>
                                <li>
                                    <a href="http://jqueryenlightenment.com/">jQuery Enlightenment</a>
                                </li>
                                <li>
                                    <a href="http://jquery.com/">jQuery Home</a>
                                </li>
                                <li>
                                    <a href="http://jquerymobile.com/">jQuery Mobile</a>
                                </li>
                                <li>
                                    <a href="http://jqueryui.com/">jQuery UI</a>
                                </li>
                                <li>
                                    <a href="http://learningjquery.com/">Learning jQuery</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a class="menu_trigger" href="#">HTML5 Resources</a>
                            <ul class="menu">
                                <li>
                                    <a href="http://diveintohtml5.info/">Dive Into HTML5</a>
                                </li>
                                <li>
                                    <a href="http://www.alistapart.com/articles/get-ready-for-html-5/">Get Ready for HTML5</a>
                                </li>
                                <li>
                                    <a href="http://html5boilerplate.com/">HTML5 Boilerplate</a>
                                </li>
                                <li>
                                    <a href="http://html5doctor.com/">HTML5 Doctor</a>
                                </li>
                                <li>
                                    <a href="http://html5.org/">HTML5 Intro</a>
                                </li>
                                <li>
                                    <a href="http://www.zeldman.com/superfriends/">HTML5 Super Friends</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a class="menu_trigger" href="#">Code</a>
                            <ul class="menu">
                                <li>
                                    <a href="<%=loginStaticPath%>assets/css/desktop.css">Desktop - CSS</a>
                                </li>
                                <li>
                                    <a href="<%=loginStaticPath%>assets/js/jquery.desktop.js">Desktop - JavaScript</a>
                                </li>
                                <li>
                                    <a href="http://github.com/nathansmith/jQuery-Desktop">GitHub Repository</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a class="menu_trigger" href="#">Credits</a>
                            <ul class="menu">
                                <li>
                                    <a href="http://sonspring.com/journal/jquery-desktop">Demo built by Nathan Smith</a>
                                </li>
                                <li>
                                    <a href="http://adrian-rodriguez.net/">Wallpaper by Adrian Rodriguez</a>
                                </li>
                                <li>
                                    <a href="http://tango.freedesktop.org/Tango_Desktop_Project">Icons - Tango Desktop Project</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <div class="abs" id="bar_bottom">
                    <a class="float_left" href="#" id="show_desktop" title="Show Desktop">
                        <img src="<%=loginStaticPath%>assets/images/icons/icon_22_desktop.png" />
                    </a>
                    <ul id="dock">
                        <li id="icon_dock_computer">
                            <a href="#window_computer">
                                <img src="<%=loginStaticPath%>assets/images/icons/icon_22_computer.png" />
                                Computer
                            </a>
                        </li>
                        <li id="icon_dock_drive">
                            <a href="#window_drive">
                                <img src="<%=loginStaticPath%>assets/images/icons/icon_22_drive.png" />
                                Hard Drive
                            </a>
                        </li>
                        <li id="icon_dock_disc">
                            <a href="#window_disc">
                                <img src="<%=loginStaticPath%>assets/images/icons/icon_22_disc.png" />
                                Audio CD
                            </a>
                        </li>
                        <li id="icon_dock_network">
                            <a href="#window_network">
                                <img src="<%=loginStaticPath%>assets/images/icons/icon_22_network.png" />
                                Network
                            </a>
                        </li>
                    </ul>
                    <a class="float_right" href="http://www.firehost.com/?ref=spon_nsmith_desktop-sonspring" title="Secure Hosting">
                        <img src="<%=loginStaticPath%>assets/images/misc/firehost.png" />
                    </a>
                </div>
            </div>
            <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
            <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
            <script>
                    !window.jQuery && document.write(unescape('%3Cscript src="<%=loginStaticPath%>assets/js/jquery.js"%3E%3C/script%3E'));
                    !window.jQuery.ui && document.write(unescape('%3Cscript src="<%=loginStaticPath%>assets/js/jquery.ui.js"%3E%3C/script%3E'));
            </script>
            <script src="<%=loginStaticPath%>assets/js/jquery.desktop.js"></script>
            <script>
                JQD.window_resize(this);
                var _gaq = [['_setAccount', 'UA-166674-8'], ['_trackPageview']];

                (function(d, t) {
                    var g = d.createElement(t),
                    s = d.getElementsByTagName(t)[0];
                    g.async = true;
                    g.src = '//www.google-analytics.com/ga.js';
                    s.parentNode.insertBefore(g, s);
                })(this.document, 'script');
            </script>
        </body>
    </html>