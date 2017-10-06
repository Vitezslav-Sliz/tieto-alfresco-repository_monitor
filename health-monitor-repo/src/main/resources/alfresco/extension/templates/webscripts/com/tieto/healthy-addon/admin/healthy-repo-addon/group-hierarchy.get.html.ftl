<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.group-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js", "group-hierarchy/js/group-hierarchy.js","js/jquery.shorten.min.js"] customCSSFiles=["group-hierarchy/css/admin.css"] readonly=true>
	
	<h2>Goups:</h2>
	<div id="groups"></div>

	<script>
		$(function() {
			AdminGH.setServiceContext('${url.serviceContext}');
			AdminGH.initialize();
		});
	</script>
	
</@page>