<!--
   index.jsp
   
   Copyright 2017 ZeeK <avD.ZeeK@gmail.com>
   
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
   MA 02110-1301, USA.
   
   
-->
<%@page import="com.zeek.fwk.ZDBConnection"%>
<%@page import="com.zeek.services.*"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>ZeeK | Work</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta name="generator" content="Geany 1.22" />
	<meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=device-dpi" />
	<link rel="stylesheet" type="text/css" href="css/zeek.css" />
	<script>
	function loadPageData(){
			<%
			HashMap<String, Integer> _modules = ZeeKServices.getInstance().fetchModule("MODULES");
			for (String i: _modules.keySet()){
                                int j = _modules.get(i);
				%>moduleList.push('<%= i%>');<%
                                %>accessMap['<%= i%>']='<%= j%>';<%
			}
			%>
			populateModule();
		}
	</script>
	<script src="js/zeek.js"></script>
	<style>
table, th, td {
    border: 1px solid black;
}
</style>
</head>
<body onload="loadPageData()">
	<h1>AMAN VERMA<br>
	<span>Performance Consultant</span>
	</h1>
	<hr>
	<div id="menu">
		<li>	<a style="color: Lime;" href="index.jsp">HOME</a></li>
		<li>	<a href="about.jsp">ABOUT</a></li>
	</div>
	<hr>
	<div id="content">
	<table style="width:100%; table-layout:fixed;">
	<tr>
	<td valign="top" width="40%" align="left" style="overflow:hidden;" id="launchsec">
		Launch:<br>
		<input type="text" placeholder="[FILTER]" style='width:80%;' id = "filter" />
		<input id = "go_launch" type="button" value="GO" onclick="launchModule();"/>
	<br>
	<div id="modules">Loading...</div>
	</td>
	<td width="60%" align="left">
	<div id="display">
                <input type="button" value="toggle screen" onclick="hideshow(document.getElementById('launchsec'))"<br>
		<div id="display_inner">
		This is a platform to showcase various code. Of course, with a very stupid user interface :)<br><br>
		In general code sitting idle with no purpose is intended to be uploaded and that way it could have some purpose<br><br>
		Most of the effort is to convert code into SSCCE. Thanks to @openshift to provide a great platform to launch<br><br>
		Left hand side panel is used to launch particular code and boom it goes here. Most of the code gets a generic jsp template and dynamically loads here. Some privileged code have their own page.<br><br>
		Most of the resources can be downloaded (at users' risk)<br><br>
		Some of the dynamic content might be blocked (or have an access code) because of one or more of the following reasons
		<ul>
			<li>Its under testing</li>
			<li>Its personal</li>
			<li>Its confidential</li>
		</ul>
		More to come!
		</div>
	</div>
	</td>
	</tr>
	</table>
		
	</div>
	<hr>
	<table width=100%>
	<tr>
			<td>
				Feedback/Hello/Suggestions? <a style="font: 80% Arial,sans-serif; color:#0783B6;" href="mailto:avD.ZeeK@gmail.com">Email Me</a>
				<br>
				<a href="https://www.linkedin.com/pub/aman-verma/49/b49/830" style="text-decoration:none;"><span style="font: 80% Arial,sans-serif; color:#0783B6;"><img src="https://static.licdn.com/scds/common/u/img/webpromo/btn_in_20x15.png" width="20" height="15" alt="View Aman Verma's LinkedIn profile" style="vertical-align:middle;" border="0">&nbsp;View my LinkedIn profile</span></a>
			</td>
		</tr>	
		<tr>
			<td>
				Twitter : <img src="img/twitter.png" width=100px height=20px; onclick="hideshow(document.getElementById('tweet_div'))"/>
				<div id="tweet_div" style="display:none;">
				<a class="twitter-timeline"  href="https://twitter.com/aman_zeek_verma" data-widget-id="597553391536058368">Tweets by @aman_zeek_verma</a>
		
		
            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
            </div>
          </td>
		</tr>
	</table>
	Created by Aman Verma, ZeeK.
</body>
</html>
