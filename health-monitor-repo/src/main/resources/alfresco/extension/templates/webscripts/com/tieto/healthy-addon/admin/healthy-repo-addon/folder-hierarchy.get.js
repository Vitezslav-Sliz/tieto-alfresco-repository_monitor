<import resource="classpath:alfresco/templates/webscripts/org/alfresco/repository/admin/admin-common.lib.js">
<import resource="classpath:alfresco/extension/templates/webscripts/com/tieto/healthy-addon/admin/healthy-repo-addon/folder-hierarchy.lib.js">

loadSolrSummaryAndStatus();

model.tools = Admin.getConsoleTools("folder-hierarchy");
model.metadata = Admin.getServerMetaData();