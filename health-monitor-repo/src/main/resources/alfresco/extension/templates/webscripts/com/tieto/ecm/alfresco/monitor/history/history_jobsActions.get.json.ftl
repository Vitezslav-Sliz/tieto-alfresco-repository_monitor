[
<#list jobs as job>
  {
    "nodeRef": "${job.nodeRef!}",
    "type": "${job.type!}",
    "status": "${job.status!}"
  }<#if job_has_next>,</#if>
</#list>
]