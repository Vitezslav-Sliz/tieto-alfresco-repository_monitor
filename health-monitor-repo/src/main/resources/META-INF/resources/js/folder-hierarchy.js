var selected;
var versionInJson;

function render() {
	var currentVersion = versionInJson[selected];
	var nodeRef = $("#versionSelect option:selected").text(); 
	$.ajax({
		url: serviceContext + "/tieto/healthy-addon/folder-hierarchy/list-nodes-for-job.json",
		method: "GET",
		data: {"nodeRef" : nodeRef}
	}).done(function( data ) {
		$("#time").text(data.time);
		$("#server").text(data.server);
		renderCount(data.data.count);
		renderDepth(data.data.depth);
	}).fail(function(jqXHR, textStatus) {
		console.log("Error while loading list of nodes for job");
	});
	
}

$(function() {
	selected = 0;
	$.ajax({
		url: serviceContext + "/tieto/healthy-addon/folder-hierarchy/list-jobs.json",
	}).done(function( data ) {
		versionInJson = data; 
		renderSelect(versionInJson);
		render();
	}).fail(function(jqXHR, textStatus) {
		console.log("Error while loading list of jobs");
	});
	
	$("#versionSelect").change(function() {
		selected = $("#versionSelect").val();
		$("#versionSelect option[value="+ selected + "]").prop('selected',true);
		render();
	})

});

function renderCount(data) {
	var htmlCount = "";
	for (var i=0; i < data.length; i++) {
		htmlCount += "NodeRef: " + data[i].nodeRef + "<br\>";
		htmlCount += "Path: " + data[i].path + "<br\>";
		htmlCount += "Node count: " + data[i].nodeCount + "<br\>";
	}
	htmlCount += "<br\>"; 
	$("#count").html(htmlCount);
}

function renderDepth(data) {
	var htmlDepth = "";
	for (var i=0; i < data.length; i++) {
		htmlDepth += "NodeRef: " + data[i].nodeRef + "<br\>";
		htmlDepth += "Path: " + data[i].path + "<br\>";
		htmlDepth += "Node count: " + data[i].nodeCount + "<br\>";
	}
	htmlDepth += "<br\>"; 
	$("#depth").html(htmlDepth);
}

function renderSelect(data) {
	var htmlSelect = "";
	for (var i=0; i < data.length; i++) {
		htmlSelect += "<option value=\"" + i + "\">" + data[i].nodeRef + "</option>"
	}
	$("#versionSelect").html(htmlSelect);
}

