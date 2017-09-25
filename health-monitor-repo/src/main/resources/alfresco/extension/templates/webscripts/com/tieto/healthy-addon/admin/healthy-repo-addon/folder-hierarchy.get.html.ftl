<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.folder-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js", "js/folder-hierarchy.js"] customCSSFiles=["css/admin.css"] readonly=true>

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
	        var serviceContext = '${url.serviceContext}';
	

	</script>
</@page>
