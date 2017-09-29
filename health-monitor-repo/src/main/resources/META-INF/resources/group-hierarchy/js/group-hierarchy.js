var nodeRef;

$(function() {
	selected = 0;
	$.ajax({
		url : serviceContext + "/api/monitor/job/acl_hierarchy",
	}).done(function(data) {
		nodeRef = data.monitorJob;
		render();
	}).fail(function(jqXHR, textStatus) {
		console.log("Error while loading list of jobs");
	});
});

function render() {
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
			console.log("Error while loading list of nodes for job");
		});
	}, 2000);
}
