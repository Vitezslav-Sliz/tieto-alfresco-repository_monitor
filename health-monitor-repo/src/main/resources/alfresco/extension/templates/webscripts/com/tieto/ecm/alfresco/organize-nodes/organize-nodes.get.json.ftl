<#if types??>
	{
		<#list types?keys as key>
								"${key}":"${types[key]}"
								<#if key_has_next>,</#if>
		</#list>
	}
</#if>
<#if models??>
	{
		<#list models?keys as key>
								"${key}":"${models[key]}"
								<#if key_has_next>,</#if>
		</#list>
	}
</#if>