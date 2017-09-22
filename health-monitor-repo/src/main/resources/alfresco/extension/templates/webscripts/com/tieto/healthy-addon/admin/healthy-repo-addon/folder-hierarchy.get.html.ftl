<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.filip-test.section") readonly=true>

    <#list coreNames as coreName>
        <div class="column-full">
            <#assign coreLabel= msg("solr-tracking.section." + coreName + ".title") />
            <#if coreLabel == "solr-tracking.section." + coreName + ".title">
                <#assign coreLabel= msg("solr-tracking.section.genericCore.title", coreName) />
            </#if>
    
            <@section label=coreLabel />
            <div class="column-left">
                <@field value="${trackingStatus[coreName]['index']['numDocs']?c}"  label=msg("solr-tracking.section.numdocs.title") description=msg("solr-tracking.section.numdocs.description") />  
            </div>
           
            <div class="column-right">   
                <@field value="${trackingStatus[coreName]['index']['maxDoc']?c}"  label=msg("solr-tracking.section.maxdocs.title") description=msg("solr-tracking.section.maxdocs.description") />  
            </div>
        </div>
    </#list>
</@page>