
var AdminGH = AdminGH || {};

$(function() {
	var nodeRef, serviceContext, selectElementId;
	
	AdminGH.setServiceContext = function setServiceContext(context){
		serviceContext = context;
	}
	
	AdminGH.setSelectElementId = function setSelectElementId(id){
		selectElementId = id;
	}
	
	AdminGH.initialize = function initialize(){
		$.ajax({
			url: serviceContext + "/api/monitor/history/jobs_actions",
			method: "GET",
			data: {
				"limit" : 10, 
				"type" : "PERMISSION_GROUPS_HIERARCHY"
			}
		}).done(function( data ) {
			AdminGH.renderSelect(data.jobs);
			AdminGH.render();
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while loading list of jobs!");
		});
		
		$("#" + selectElementId).change(function() {
			selectedIndex = $("#" + selectElementId).val();
			$("#" + selectElementId + " option[value="+ selectedIndex + "]").prop('selected',true);
			AdminGH.render();
		})
	}
	
	AdminGH.renderSelect = function renderSelect(data) {
		var htmlSelect = "";
		for (var i=0; i < data.length; i++) {
			htmlSelect += "<option value=\"" + i + "\">" + data[i].nodeRef + "</option>"
		}
		$("#" + selectElementId).html(htmlSelect);
	}

	AdminGH.render = function render() {
		var nodeRef = $("#versionSelect option:selected").text(); 
		$.ajax({
			url: serviceContext + "/tieto/healthy-addon/util/get-content",
			method: "GET",
			data: {"nodeRef" : nodeRef}
		}).done(function( data ) {
			var htmlGroups = "";
			for (i = 0; i < data.length; i++) {
				for (j = 0; j < data.length; j++) {
					htmlGroups += data[i][j] + "/";
				}
				htmlGroups += "<hr/>";
			}
			$("#groups").html(htmlGroups);
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while loading list of nodes for job!");
		});
	}
});
