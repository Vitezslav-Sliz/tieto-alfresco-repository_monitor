<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.folder-hierarchy.section") customJSFiles=["js/jquery-3.2.1.min.js"] readonly=true>
	<h1>Hello from ftl!</h1>
	Time: ${node.time}</br>
	Server: ${node.server}</br>
	Nodes that exceeded nodes count:</br>
	<#list node.data.count as item>
		NodeRef:${item.nodeRef}</br>
		Path:${item.path}</br>
		NodeCount:${item.nodeCount}</br>
	</#list>

	Nodes that exceeded depth from root:</br>
	<#list node.data.depth as item>
		NodeRef:${item.nodeRef}</br>
		Path:${item.path}</br>
		NodeCount:${item.nodeCount}</br>
	</#list>
</@page>