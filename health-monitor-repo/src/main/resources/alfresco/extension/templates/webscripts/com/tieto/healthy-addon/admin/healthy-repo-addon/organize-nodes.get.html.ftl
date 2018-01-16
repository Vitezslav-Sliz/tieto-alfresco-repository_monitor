<#include "../admin-template.ftl" />

<@page title=msg("admin-console.tool.organize-folder.section") customJSFiles=["js/jquery-3.2.1.min.js", "organize-nodes/js/organize-nodes.js","js/jquery.shorten.min.js"] customCSSFiles=["organize-nodes/css/organize-nodes.css"] readonly=true>
	
	<div class="column-full">
		<div class="column-left">
			<h2>Path of Folder</h2>
			<div id="count">
				<input type="text" id="folderPath"></input>
			</div>
		</div>
		<div class="column-right">
			<h2>New Folder Name</h2>
			<div id="count">
				<input type="text" id="newName"></input>
			</div>
		</div>
		<br/><br/><br/>
		<div class="column-left">
			<h2>Select Model</h2>
			<div id="count">
				<select id="selectModel"></select>
			</div>
		</div>
		
		<div class="column-right">
			<h2>Select Type</h2>
			<div id="depth">
				<select id="selectType"></select>
			</div>
		</div><br/><br/>
		
		<div class="column-left">
			<div id="depth">
				<button value="organize" id="organize" class="organizebutton">Organize</button>
			</div>
		</div>
		
	</div>
	
	<script>
						
var OrganizeNodes = OrganizeNodes || {};

$(function() {
	var modelSelectElementId,typeSelectElementId,propertySelectElementId;
	
	OrganizeNodes.setServiceContext = function setServiceContext(context){
		serviceContext = context;
	}
	
	OrganizeNodes.setModelSelectElementId = function setModelSelectElementId(id){
		modelSelectElementId=id;
	}
	OrganizeNodes.setTypeSelectElementId = function setTypeSelectElementId(id){
		typeSelectElementId = id;
		console.log(id);
	}
	
	OrganizeNodes.initialize = function initialize(){
		$.ajax({
			url: serviceContext + "/fetch/node/metadata?typeofdata=model",
			method: "GET"
		}).done(function( json ) {
			var htmlSelect = "";
			for (var key in json){
				htmlSelect += "<option value=\"" + key + "\">" + json[key].charAt(0).toUpperCase() + json[key].slice(1);+ "</option>";
			}
			$("#" + modelSelectElementId).html(htmlSelect);
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while loading list of jobs!");
		});
		
		$("#" + modelSelectElementId).change(function() {			
			OrganizeNodes.renderTypes(this.value);
		});
		
		$( "#organize" ).click(function() {
					$.ajax({
						url: serviceContext + "/fetch/node/metadata?typeofdata=organizeNodes&&folderName="+$("#newName").val()+"&path="+$("#folderPath").val()+"&selectedType="+$("#selectType").val(),
						method: "GET"
					}).done(function( json ) {
						var htmlSelect = "";
						for (var key in json){
							htmlSelect += "<option value=\"" + key + "\">" + json[key].charAt(0).toUpperCase() + json[key].slice(1);+ "</option>";
						}
						$("#" + typeSelectElementId).html(htmlSelect);
					}).fail(function(jqXHR, textStatus) {
						console.log("Error while loading list of jobs!");
					});
					
					return false;
		});
	}
	
	OrganizeNodes.renderTypes= function renderTypes(value) {
		$.ajax({
			url: serviceContext + "/fetch/node/metadata?typeofdata=type&model="+value,
			method: "GET"
		}).done(function( json ) {
			var htmlSelect = "";
			for (var key in json){
				htmlSelect += "<option value=\"" + json[key] + "\">" + key.charAt(0).toUpperCase() + key.slice(1) + "</option>";
			}
			$("#" + typeSelectElementId).html(htmlSelect);
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while loading list of jobs!");
		});
			
	}

});
		
	
		$(function() {
			OrganizeNodes.setServiceContext('${url.serviceContext}');
			OrganizeNodes.setModelSelectElementId("selectModel");
			OrganizeNodes.setTypeSelectElementId("selectType");
			OrganizeNodes.initialize();
		});
		
	</script>
</@page>
