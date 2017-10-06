
var AdminGH = AdminGH || {};

$(function() {
	var nodeRef, serviceContext;
	
	AdminGH.setServiceContext = function setServiceContext(context){
		serviceContext = context;
	}
	
	AdminGH.initialize = function initialize(){
		$.ajax({
			url : serviceContext + "/api/monitor/job/acl_hierarchy",
		}).done(function(data) {
			nodeRef = data.monitorJob;
			AdminGH.render();
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while calling group hierarchy webscript!");
		});
	}

	AdminGH.render = function render() {
		//This is only temporary solution!
		setTimeout(function() {
			$.ajax({
				url : serviceContext + "/tieto/healthy-addon/util/get-content.json",
				method : "GET",
				data : {
					"nodeRef" : nodeRef
				}
			}).done(function(data) {
				var htmlGroups = "";
				for (i = 0; i < data.length; i++) {
					for (j = 0; j < data.length; j++) {
						htmlGroups += data[i][j] + "/";
					}
					htmlGroups += "<hr/>";
				}
				$("#groups").html(htmlGroups);
			}).fail(function(jqXHR, textStatus) {
				console.log("Error while loading groups!");
			});
		}, 3000);
	}
});
