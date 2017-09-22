<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.folder-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js"] readonly=true>
	<h1>Hello from ftl!</h1>
	Time: <span id="time"></span></br>
	Server: <span id="server"></span></br>
	

<script>
var selected = 0;
var versionInJson = $.parseJSON(${versions});

function render() {
	var currentVersion = versionInJson[selected];
	$("#time").text(currentVersion.time);
	$("#server").text(currentVersion.server);
}

$(function() {
	selected = 0;
	versionInJson = $.parseJSON(${versions});
	alert("function = " + versionInJson);
	render();
});



$("#versionSelect").change(function() {
	alert("Ahoj " + $("#versionSelect").val());
})




</script>
</@page>
