<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.folder-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js", "folder-hierarchy/js/folder-hierarchy.js","js/jquery.shorten.min.js"] customCSSFiles=["folder-hierarchy/css/admin.css"] readonly=true>

	<section class="info-container">
	    <div class="info-container-block">
	    	Time: <span id="time"></span></br>
			Server: <span id="server"></span></br>
	    </div>
	    <div class="info-container-block">
	    	<span id="job-select-label">Select job:</span></br>
			<select id="versionSelect"></select>
	    </div>
	</section>
	
	<div class="column-full">
		<div class="column-left">
			<h2>Nodes that exceeded node count:</h2>
			<div id="count"></div>
		</div>
		
		<div class="column-right">
			<h2>Nodes that exceeded node depth:</h2>
			<div id="depth"></div>
		</div>
	</div>
	
	<script>
		$(function() {
			AdminFH.setServiceContext('${url.serviceContext}');
			AdminFH.initialize();
		});
	</script>

</@page>
