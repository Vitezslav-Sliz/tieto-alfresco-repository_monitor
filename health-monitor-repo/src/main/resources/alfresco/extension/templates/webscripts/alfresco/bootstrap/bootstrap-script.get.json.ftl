{
	"userImport": ${userImport},
	"groupImport": ${groupImport},
	"membershipImport": ${membershipImport},
	"nestedgroupsImport": ${nestedgroupsImport},
	"sites": ${sitesImport},
	"sitemembersImport": ${sitemembersImport},
	"structureImport": ${structureImport},
	"addedUsers": [
<#list addedUsers as person>
	{"name": "${person}"}<#if person_has_next>,</#if>
</#list>
	],
	"addedGroups": [
<#list addedGroups as group>
	{"name": "${group}"}<#if group_has_next>,</#if>
</#list>
	],
	"nestedGroups": [
<#list nestedGroups as nestedGroup>
	{"name": "${nestedGroup}"}<#if nestedGroup_has_next>,</#if>
</#list>
	],
	"sites": [
<#list sites as site>
	{"name": "${site}"}<#if site_has_next>,</#if>
</#list>
	],
	"sitemembers": [
<#list sitemembers as sitemember>
	{"name": "${sitemember}"}<#if sitemember_has_next>,</#if>
</#list>
	]		
}