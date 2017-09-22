<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.sites.section") customJSFiles=["js/jquery-3.2.1.min.js"] readonly=true>
	
	<div style="padding:30px 50px 30px 50px;">
		<h1>Sites count</h1>
	</div>
	
	
	<div style="padding:30px 50px 30px 50px;font-size: medium;">
		<span>There is currently <b>${node.sitesCount}</b> sites in total.</span>
	</div>
	
	<div style="padding:30px 50px 30px 50px; font-style: italic; font-size: 14px;">
		NOTE: Maximum recomended sites in a repository is 5000. Count of sites has impact on performance.
	</div>
	
</@page>