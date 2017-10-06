<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.group-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js", "group-hierarchy/js/group-hierarchy.js","js/jquery.shorten.min.js"] customCSSFiles=["group-hierarchy/css/admin.css"] readonly=true>
	
	<h2>Goups:</h2><br/>
	<span id="job-select-label">Select job:</span><br/>
			<select id="versionSelect"></select>
	<div id="groups"></div>

	<script>
		$(function() {
			AdminGH.setServiceContext('${url.serviceContext}');
			AdminGH.setSelectElementId("versionSelect");
			AdminGH.initialize();
		});
	</script>
	
</@page>