
var AdminFH = AdminFH || {};

$(function() {
	var selected, versionInJson;
	selected = 0;
	$.ajax({
		url: serviceContext + "/tieto/healthy-addon/folder-hierarchy/list-jobs.json",
	}).done(function( data ) {
		versionInJson = data; 
		AdminFH.renderSelect(versionInJson);
		AdminFH.render();
	}).fail(function(jqXHR, textStatus) {
		console.log("Error while loading list of jobs!");
	});
	
	$("#versionSelect").change(function() {
		selected = $("#versionSelect").val();
		$("#versionSelect option[value="+ selected + "]").prop('selected',true);
		AdminFH.render();
	})
	
	AdminFH.render = function render() {
		var currentVersion = versionInJson[selected];
		var nodeRef = $("#versionSelect option:selected").text(); 
		$.ajax({
			url: serviceContext + "/tieto/healthy-addon/util/get-content",
			method: "GET",
			data: {"nodeRef" : nodeRef}
		}).done(function( data ) {
			$("#time").text(data.time);
			$("#server").text(data.server);
			AdminFH.renderCount(data.data.count);
			AdminFH.renderDepth(data.data.depth);
			$(".path").shorten();
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while loading list of nodes for job!");
		});
	}
	
	AdminFH.renderCount = function renderCount(data) {
		var htmlCount = "";
		for (var i=0; i < data.length; i++) {
			htmlCount += "<div class=\"node-info\"><table>";
			htmlCount += "<tr><td class=\"node-header\">NodeRef:</td><td class=\"value\">" + data[i].nodeRef + "</td></tr>";
			htmlCount += "<tr><td class=\"node-header\">Path:</td><td><span class=\"path value\">" + data[i].path + "</td></tr>";
			htmlCount += "<tr><td class=\"node-header\">Node count:</td><td class=\"value\">" + data[i].nodeCount + "</td></tr>";
			htmlCount += "</table></div>"; 
			htmlCount += "<hr/>";
		}
		
		$("#count").html(htmlCount);
	}

	AdminFH.renderDepth = function renderDepth(data) {
		var htmlDepth = "";
		for (var i=0; i < data.length; i++) {
			htmlDepth += "<div class=\"node-info\"><table>"
			htmlDepth += "<tr><td class=\"node-header\">NodeRef:</td><td>" + data[i].nodeRef + "</td></tr>";
			htmlDepth += "<tr><td class=\"node-header\">Path:</td><td><span class=\"path\">" + data[i].path + "</td></tr>";
			htmlDepth += "<tr><td class=\"node-header\">Node count:</td><td>" + data[i].nodeCount + "</td></tr>";
			htmlDepth += "</table></div>"; 
			htmlDepth += "<hr/>";
		}
		$("#depth").html(htmlDepth);
	}

	AdminFH.renderSelect = function renderSelect(data) {
		var htmlSelect = "";
		for (var i=0; i < data.length; i++) {
			htmlSelect += "<option value=\"" + i + "\">" + data[i].nodeRef + "</option>"
		}
		$("#versionSelect").html(htmlSelect);
	}

});
