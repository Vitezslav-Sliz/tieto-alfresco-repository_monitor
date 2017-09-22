<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script.lib.js">
<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script-users.js">
<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script-groups.js">
<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script-nested-groups.js">
<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script-sites.js">
<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script-sitemembers.js">
<import resource="classpath:alfresco/extension/templates/webscripts/alfresco/bootstrap/bootstrap-script-structure.js">

var userImport = getUrlParameter("user", "false");
var groupImport = getUrlParameter("group", "false");
var structureImport = getUrlParameter("structure", "false");
var membershipImport = getUrlParameter("membership", "false");
var nestedgroupsImport = getUrlParameter("nestedgroups", "false");
var sitesImport = getUrlParameter("sites", "false");
var sitemembersImport = getUrlParameter("sitemembers", "false");
var addedUsers = [];
var addedGroups = [];
var assignedMembers = [];
var nestedGroups = [];
var sites = [];
var sitemembers = [];
var structure = [];

if (("true".equals(userImport)) && (usersToBeCreated.length > 0)) {
	for (var i=0; i < usersToBeCreated.length; i++) {
		logger.debug("bootstrap-script.get.js || usersToBeCreated[" + i + "] =  " + usersToBeCreated[i]);
		addedUsers.push(createUser(usersToBeCreated[i]));
	}
} 

if (("true".equals(groupImport)) && (groupsToBeCreated.length > 0)) {
	for (var i=0; i < groupsToBeCreated.length; i++) {
		logger.debug("bootstrap-script.get.js || groupsToBeCreated[" + i + "] =  " + groupsToBeCreated[i]);
		addedGroups.push(createGroup(groupsToBeCreated[i]));
	}
} 

if (("true".equals(membershipImport)) && (groupsToBeCreated.length > 0)) {
	for (var i=0; i < groupsToBeCreated.length; i++) {
		logger.debug("bootstrap-script.get.js || groupsToBeCreated[" + i + "] =  " + groupsToBeCreated[i]);
		assignedMembers.push(assignMember(groupsToBeCreated[i]));
	}
} 

if (("true".equals(nestedgroupsImport)) && (groupsToBeNested.length > 0)) {
	for (var i=0; i < groupsToBeNested.length; i++) {
		logger.debug("bootstrap-script.get.js || groupsToBeNested[" + i + "] =  " + groupsToBeNested[i]);
		nestedGroups.push(nestGroups(groupsToBeNested[i]));
	}
} 

if (("true".equals(sitesImport)) && (sitesToBeCreated.length > 0)) {
	for (var i=0; i < sitesToBeCreated.length; i++) {
		logger.debug("bootstrap-script.get.js || sitesToBeCreated[" + i + "] =  " + sitesToBeCreated[i]);
		sites.push(createSites(sitesToBeCreated[i]));
	}
} 

if (("true".equals(sitemembersImport)) && (sitemembersToBeAssigned.length > 0)) {
	for (var i=0; i < sitemembersToBeAssigned.length; i++) {
		logger.debug("bootstrap-script.get.js || sitemembersToBeAssigned[" + i + "] =  " + sitemembersToBeAssigned[i]);
		sitemembers.push(addSiteMembers(sitemembersToBeAssigned[i]));
	}
} 

if (("true".equals(structureImport)) && (structureToBeImported.length > 0)) {
	for (var i=0; i < structureToBeImported.length; i++) {
		logger.debug("bootstrap-script.get.js || structureToBeImported[" + i + "] =  " + structureToBeImported[i]);
		structure.push(createStructure(structureToBeImported[i]));
	}
} 

model.userImport = userImport;
model.groupImport = groupImport;
model.membershipImport = membershipImport;
model.structureImport = structureImport;
model.nestedgroupsImport = nestedgroupsImport;
model.sitesImport = sitesImport;
model.sitemembersImport = sitemembersImport;
model.addedUsers = addedUsers;
model.addedGroups = addedGroups;
model.nestedGroups = nestedGroups;
model.sites = sites;
model.sitemembers = sitemembers;
model.structure = structure;