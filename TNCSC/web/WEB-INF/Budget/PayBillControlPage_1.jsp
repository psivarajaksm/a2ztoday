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
<html lang="en">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title>sound squirt pricing table</title>
        <style type="text/css">
            .cssguycomments {background:#eee;border:#ddd;padding:8px;margin-bottom:40px;}
            .cssguycomments p {font:normal 12px/18px verdana;}

            a img {border:0;vertical-align:text-bottom;}
            table {border-collapse:collapse;}
            th.side {
                background: transparent url("<%=staticPath%>images/i/bg_th_side.gif") no-repeat bottom left;
            }
            td.side {
                text-align:right;
                background: transparent url("<%=staticPath%>images/i/bg_td_side.gif") no-repeat bottom left;
                width:180px;
                font:bold 12px/15px verdana;
                color:#6e6f37;
                padding-right:8px;
            }

            th {
                height:64px;
                border-right:1px solid #fff;
                vertical-align:bottom;
                color:#fff;
                font:normal 21px/27px arial;
                letter-spacing:2px;
                background:transparent url("<%=staticPath%>images/i/bg_th.gif") no-repeat bottom left;
            }
            td {
                text-align:center;
                background:transparent url("<%=staticPath%>images/i/bg_td.gif") no-repeat bottom left;
                border-right:1px solid #fff;
                color:#fff;
                width:108px;
                height:40px;
                font:bold 12px/18px verdana;
            }
            td.on {background:transparent url("<%=staticPath%>images/i/bg_td_on.gif") no-repeat bottom left;}
            th.on {
                background:transparent url("<%=staticPath%>images/i/bg_th_on.gif") no-repeat bottom left;
                padding-bottom:9px;
                width:148px;
            }

            tfoot td {
                background:transparent url("<%=staticPath%>images/i/bg_foot_td.gif") no-repeat top left;
                height:64px;
                vertical-align:top;
                padding-top:8px;
            }
            tfoot td.on {
                background:transparent url("<%=staticPath%>images/i/bg_foot_td_on.gif") no-repeat top left;
                padding-top:16px;
            }
            tfoot td.side {background: transparent url("<%=staticPath%>images/i/bg_foot_td_side.gif") no-repeat top left;}



        </style>

        <script type="text/javascript">
            /*
                For functions getElementsByClassName, addClassName, and removeClassName
                Copyright Robert Nyman, http://www.robertnyman.com
                Free to use if this text is included
             */
            function getElementsByClassName(className, tag, elm){
                var testClass = new RegExp("(^|\\s)" + className + "(\\s|$)");
                var tag = tag || "*";
                var elm = elm || document;
                var elements = (tag == "*" && elm.all)? elm.all : elm.getElementsByTagName(tag);
                var returnElements = [];
                var current;
                var length = elements.length;
                for(var i=0; i<length; i++){
                    current = elements[i];
                    if(testClass.test(current.className)){
                        returnElements.push(current);
                    }
                }
                return returnElements;
            }

            function addClassName(elm, className){
                var currentClass = elm.className;
                if(!new RegExp(("(^|\\s)" + className + "(\\s|$)"), "i").test(currentClass)){
                    elm.className = currentClass + ((currentClass.length > 0)? " " : "") + className;
                }
                return elm.className;
            }

            function removeClassName(elm, className){
                var classToRemove = new RegExp(("(^|\\s)" + className + "(\\s|$)"), "i");
                elm.className = elm.className.replace(classToRemove, "").replace(/^\s+|\s+$/g, "");
                return elm.className;
            }

            function activateThisColumn(column) {
                var table = document.getElementById('pricetable');
	
                // first, remove the 'on' class from all other th's
                var ths = table.getElementsByTagName('th');
                for (var g=0; g<ths.length; g++) {
                    removeClassName(ths[g], 'on');
                }
                // then, remove the 'on' class from all other td's
                var tds = table.getElementsByTagName('td');
                for (var m=0; m<tds.length; m++) {
                    removeClassName(tds[m], 'on');
                }
	
                // now, add the class 'on' to the selected th
                var newths = getElementsByClassName(column, 'th', table);
                for (var h=0; h<newths.length; h++) {
                    addClassName(newths[h], 'on');
                }
                // and finally, add the class 'on' to the selected td
                var newtds = getElementsByClassName(column, 'td', table);
                for (var i=0; i<newtds.length; i++) {
                    addClassName(newtds[i], 'on');
                }
            }
        </script>
    </head>
    <body>

        <div class="cssguycomments">
            <p>Background images and fixed dimensions applied to the cells.</p>
        </div>

        <table id="pricetable">
            <thead>
                <tr>
                    <th class="side">&nbsp;</th>
                    <th class="choiceA">$1000</th>
                    <th class="choiceB">$100</th>
                    <th class="choiceC on">$10</th>
                    <th class="choiceD">$1</th>
                    <th class="choiceE">Free</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <td class="side">&nbsp;</td>
                    <td class="choiceA"><a href="#" onclick="activateThisColumn('choiceA');return false;"><img src="i/choose.gif" alt="Choose" /></a></td>
                    <td class="choiceB"><a href="#" onclick="activateThisColumn('choiceB');return false;"><img src="i/choose.gif" alt="Choose" /></a></td>
                    <td class="choiceC on"><a href="#" onclick="activateThisColumn('choiceC');return false;"><img src="i/choose.gif" alt="Choose" /></a></td>
                    <td class="choiceD"><a href="#" onclick="activateThisColumn('choiceD');return false;"><img src="i/choose.gif" alt="Choose" /></a></td>
                    <td class="choiceE"><a href="#" onclick="activateThisColumn('choiceE');return false;"><img src="i/choose.gif" alt="Choose" /></a></td>
                </tr>
            </tfoot>
            <tbody>
                <tr>
                    <td class="side">Number of quarters</td>
                    <td class="choiceA">4,000</td>
                    <td class="choiceB">400</td>
                    <td class="choiceC on">40</td>
                    <td class="choiceD">4</td>
                    <td class="choiceE">None</td>
                </tr>
                <tr>
                    <td class="side">Number of zeros</td>
                    <td class="choiceA">3 zeros</td>
                    <td class="choiceB">2 zeros</td>
                    <td class="choiceC on">1 zero</td>
                    <td class="choiceD">No zeros</td>
                    <td class="choiceE">None</td>
                </tr>
                <tr>
                    <td class="side">Checks on this row</td>
                    <td class="choiceA"><img src="<%=staticPath%>images/i/check.png" alt="yes" /></td>
                    <td class="choiceB"><img src="<%=staticPath%>images/i/check.png" alt="yes" /></td>
                    <td class="choiceC on"><img src="<%=staticPath%>images/i/check.png" alt="yes" /></td>
                    <td class="choiceD"><img src="<%=staticPath%>images/i/check.png" alt="yes" /></td>
                    <td class="choiceE">&nbsp;</td>
                </tr>
                <tr>
                    <td class="side">Checks on another row</td>
                    <td class="choiceA"><img src="<%=staticPath%>images/i/check.png" alt="yes" /></td>
                    <td class="choiceB"><img src="<%=staticPath%>images/i/check.png" alt="yes" /></td>
                    <td class="choiceC on">&nbsp;</td>
                    <td class="choiceD">&nbsp;</td>
                    <td class="choiceE">&nbsp;</td>
                </tr>
            </tbody>
        </table>

    </body>
</html>
