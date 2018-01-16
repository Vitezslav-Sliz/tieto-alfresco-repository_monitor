{
	"limit": "${limit!}",
	"jobOperation": "${jobOperation!}",
	"jobStatus": "${jobStatus!}",
	"jobs" : 
		[
			<#list jobs as job>
				{
				   	"nodeRef": "${job.nodeRef!}",
				   	"operation": "${job.operation!}",
				   	"status": "${job.status!}",
				   	"date": "${job.date?datetime}"
				}<#if job_has_next>,</#if>
			</#list>
		]
}