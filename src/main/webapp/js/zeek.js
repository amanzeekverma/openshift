var moduleList = [];
var accessMap = {};
/*
 * FILTER THE LIST
 * document.getElementById('filter').addEventListener('keyup', function () {
	var filterText = this.value,
       lis = document.querySelectorAll('#modules_ul li'),
       x;
   
	for (x = 0; x < lis.length; x++) {
       if (filterText === '' || lis[x].innerHTML.indexOf(filterText) > -1) {
           lis[x].removeAttribute('hidden');
       }
       else {
           lis[x].setAttribute('hidden', true);
       }
   }
 });
 */
function populateModule() {
	var module = "";
	var arrayLength = moduleList.length;
	for (var i = 0; i < arrayLength; i++) {
		module = module.concat("<li id=\'modules_li\' onclick=\"document.getElementById(\'filter\').value=\'"+moduleList[i]+"\'; document.getElementById(\'go_launch\').click()\">").concat(moduleList[i]).concat("</li>");
	}
	document.getElementById("modules").innerHTML = "<ul id = \'modules_ul\'>"+module+"</ul>";
}

var hideshow = function(id) {
		  var mydiv = id
		  if (mydiv.style.display == 'block' || mydiv.style.display == '')
			mydiv.style.display = 'none';
		  else
			mydiv.style.display = ''
}

var cheap_margin = "<br><br><br><br><br><br>";
function launchModule() {
	if (document.getElementById('filter').value == '')
		return;
	var target = document.getElementById('filter').value;
        var accessCode = accessMap[target];
       if (accessCode !== "0"){
        var inputAccessCode = prompt("Please enter your access code:","");
        if (inputAccessCode == accessCode){
             //do nothing. Its a good thing.
        } else {
             alert ("XXXXXXX UNAUTHORIZED ACCESS. Please contact Aman Verma   XXXXXXX");
             return;
        }
       } 
	var xmlhttp=new XMLHttpRequest();
	xmlhttp.onreadystatechange=function() {
	document.getElementById("display_inner").innerHTML = "<br><br>Loading...";
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		document.getElementById("display_inner").innerHTML="Response Received: "+xmlhttp.responseText;
		//var pre_page_settings = "<style>body{color:  white;}</style>"
		var pageToLoad = xmlhttp.responseText;
		//pageToLoad = pre_page_settings+pageToLoad;
		document.getElementById("display_inner").innerHTML="<iframe width=\"100%\" height=\"450px\" display=\"block\" vertical-align=\"bottom\" frameBorder=\"0\" src=\""+pageToLoad+"\"></iframe>"
		//document.getElementById("display_inner").innerHTML = "<object type=\"text/html\" data=\""+pageToLoad+"\" style=\"width:100%; height:100%; margin:1%;\"></object>";
		//document.getElementById("display_inner").innerHTML=pageToLoad;
		//document.getElementById("display_inner").innerHTML=document.getElementById("display_inner").innerHTML+cheap_margin;
	 }
	}
	xmlhttp.open("GET","/LaunchModule?target="+target,true);
	xmlhttp.send();
}

