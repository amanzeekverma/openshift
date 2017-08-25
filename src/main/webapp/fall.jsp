<!--
   fall.jsp
   
   Copyright 2015 ZeeK <avD.ZeeK@gmail.com>
   
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
<%@page import="com.zeek.services.*"%>
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
	<script src="js/zeek.js"></script>
	<script>
		function callSerivice(service){
				var xmlhttp=new XMLHttpRequest();
				xmlhttp.onreadystatechange=function() {
					if (xmlhttp.readyState==4 && xmlhttp.status==200) {
						 /*  var container = document.getElementById("content");
						  var content = container.innerHTML;
						  container.innerHTML= content;	 */
							window.location.reload(true);
					}
				}
				xmlhttp.open("GET",service,true);
				xmlhttp.send();
		}
		function showPlaylist(plist){
			var disp = "";
			<% HashMap<String, List> all_playlist = ZeeKServices.getInstance().fetchSongs();
			   for (String playlist : all_playlist.keySet()){
				   %>
				   if (plist === "<%= playlist%>") {
					   <%
					   List songs = all_playlist.get(playlist);
					   for (int i=0; i< songs.size(); i++){
						   %>
						   disp=disp + '<%= songs.get(i)%>' + '<br>';
						   <%
					   }
					   %>
				   }
				   <%
			   }
			%>
			document.getElementById('playlist_items').innerHTML = disp;
		}
		
		
		function addPlaylist(plist){
			plist=plist.replace(/ /g,"_");
			//window.location.href = window.location.href + "?app=fall&AddPlaylist=" + plist ;
			callSerivice("/LaunchModule?app=fall&AddPlaylist=" + plist);
		}
		function addSong(plist, song){
			plist=plist.replace(/ /g,"_");
			song=song.replace(/ /g,"_");
			//window.location.href = window.location.href + "?app=fall&AddSong=" + song +"&p="+plist ;
			callSerivice("/LaunchModule?app=fall&AddSong=" + song +"&p="+plist);
			
		}
	</script>
	<style>
table, th, td {
    border: 1px solid black;
}
</style>
</head>
<body>
	<h1>DUAVERS - Fall Playlist<br>
	<span>Oct 23 - 25, 2015</span>
	</h1>
	<hr>
	<div id="menu">
		<li>	<a style="color: Lime;" href="fall.jsp">PLAYLIST</a></li>
		<li>	<a href="fall.jsp">[What!]</a></li>
	</div>
	<hr>
	<div id="content">
	<table style=" width:100%;">
	<tr>
	<td valign="top" width="40%" align="left">
		Playlist :<select id="show_playlist">
		 <%
		 	List<String> playlist = ZeeKServices.getInstance().fetchList("PLAYLIST");
		 	for (int i=0; i< playlist.size(); i++){
		 		%>
		 		<option value="<%= playlist.get(i)%>"><%= playlist.get(i)%></option>
		 		<%		
		 	}
		 %>
		</select>
		<input type ="button" value = "show"
				onclick="showPlaylist(document.getElementById('show_playlist').value)"/>
		<br><br>
		<div id="playlist_items">First select playlist!</div>
	</td>
	<td width="60%" align="center">
	<input type="text" placeholder = "new playlist" id="new_playlist" size="15" /> <input type="button" value="add playlist" 
							onclick="addPlaylist(document.getElementById('new_playlist').value)"/>
	<br><br><h3>OR,</h3><br><br>
	<input type="text" placeholder = "new song here" id = "new_song" size="15" />
	&nbsp;to&nbsp;
	<select id="plist">
		 <%
		 	for (int i=0; i< playlist.size(); i++){
		 		%>
		 		<option value="<%= playlist.get(i)%>"><%= playlist.get(i)%></option>
		 		<%		
		 	}
		 %>
	</select>
	<input type="button" value="Add" 
				onclick="addSong(document.getElementById('plist').value, document.getElementById('new_song').value)"/>
	</td>
	</tr>
	</table>
		
	</div>
	<hr>
	Footer!
</body>
</html>
