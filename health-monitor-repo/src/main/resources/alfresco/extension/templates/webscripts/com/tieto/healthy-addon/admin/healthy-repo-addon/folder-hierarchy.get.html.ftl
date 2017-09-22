<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.folder-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js"] customCSSFiles=["css/admin.css"] readonly=true>

	Time: <span id="time"></span></br>
	Server: <span id="server"></span></br>
	
	<div class="column-full">
	
	<div class="column-left">
	<h2>Count headline:</h2>
	<div id="count"></div>
	</div>
	
	<div class="column-right">
	<h2>Depth headline:</h2>
	<div id="depth"></div>
	</div>
	
	</div>
	
	<h2>Select job:</h2>
	<select id="versionSelect"></select>

<script>
var selected;
var versionInJson;

function render() {
	var currentVersion = versionInJson[selected];
	//alert("currentVersion  = " + currentVersion);
	renderSelect(versionInJson);
	$("#time").text(currentVersion.data.time);
	$("#server").text(currentVersion.data.server);
	renderCount(currentVersion.data.data.count);
	//alert("renderCount = " + currentVersion.data.count);
	renderDepth(currentVersion.data.data.depth);
}

$(function() {
	selected = 0;
	versionInJson = $.parseJSON(${versions});
	render();
});

function renderCount(data) {
	//alert("renderCount = " + data);
	var htmlCount = "";
	for (var i=0; i < data.length; i++) {
		htmlCount += "NodeRef: " + data[i].nodeRef + "<br\>";
		htmlCount += "Path: " + data[i].path + "<br\>";
		htmlCount += "Node count: " + data[i].nodeCount + "<br\>";
	}
	htmlCount += "<br\>"; 
	$("#count").html(htmlCount);
}

function renderDepth(data) {
	var htmlDepth = "";
	for (var i=0; i < data.length; i++) {
		htmlDepth += "NodeRef: " + data[i].nodeRef + "<br\>";
		htmlDepth += "Path: " + data[i].path + "<br\>";
		htmlDepth += "Node count: " + data[i].nodeCount + "<br\>";
	}
	htmlDepth += "<br\>"; 
	$("#depth").html(htmlDepth);
}

function renderSelect(data) {
	var htmlSelect = "";
	for (var i=0; i < data.length; i++) {
		//alert("Select = " + data[i].nodeRef);
		htmlSelect += "<option value=\"" + i + "\">" + data[i].nodeRef + "</option>"
	}
	$("#versionSelect").html(htmlSelect);
}

$("#versionSelect").change(function() {
	selected = $("#versionSelect").val();
	render();
	$("#versionSelect option[value="+ selected + "]").prop('selected',true);
})

</script>
</@page>
