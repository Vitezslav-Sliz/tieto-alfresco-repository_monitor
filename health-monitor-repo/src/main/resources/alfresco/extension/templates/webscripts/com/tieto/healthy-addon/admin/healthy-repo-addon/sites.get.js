<import resource="classpath:alfresco/templates/webscripts/org/alfresco/repository/admin/admin-common.lib.js">
<import resource="classpath:alfresco/extension/templates/webscripts/com/tieto/healthy-addon/admin/healthy-repo-addon/sites.lib.js">

getSitesCount();

model.tools = Admin.getConsoleTools("sites");
model.metadata = Admin.getServerMetaData();