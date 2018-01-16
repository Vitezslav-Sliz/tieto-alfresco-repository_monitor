
var AdminFH = AdminFH || {};

$(function() {
	var selectedIndex = 0, serviceContext, selectElementId;
	
	AdminFH.setServiceContext = function setServiceContext(context)
    {
        serviceContext = context;
    };
    
    AdminFH.setSelectElementId = function setSelectElementId(id){
		selectElementId = id;
	}
    
	AdminFH.initialize = function initialize(){
		$.ajax({
			url: serviceContext + "/api/monitor/history/jobs_actions",
			method: "GET",
			data: {
				"limit" : 10, 
				"jobOperation" : "NODES_HIERARCHY",
				"jobStatus" : "FINISHED"
			}
		}).done(function( data ) {
			AdminFH.renderSelect(data.jobs);
			AdminFH.render();
		}).fail(function(jqXHR, textStatus) {
			console.log("Error while loading list of jobs!");
		});
		
		$("#" + selectElementId).change(function() {
			selectedIndex = $("#" + selectElementId).val();
			$("#" + selectElementId + " option[value="+ selectedIndex + "]").prop('selected',true);
			AdminFH.render();
		})
	}
	
	AdminFH.render = function render() {
		// Let's display date when the job was started
		var optionValueArray = $("#" + selectElementId + " option:selected").text().split("|");
		var nodeRef = optionValueArray[1].trim();
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
			htmlDepth += "<tr><td class=\"node-header\">Node depth:</td><td>" + data[i].nodeCount + "</td></tr>";
			htmlDepth += "</table></div>"; 
			htmlDepth += "<hr/>";
		}
		$("#depth").html(htmlDepth);
	}

	AdminFH.renderSelect = function renderSelect(data) {
		var htmlSelect = "";
		for (var i=0; i < data.length; i++) {
			htmlSelect += "<option value=\"" + i + "\">" + data[i].date + " | " +  data[i].nodeRef + "</option>"
		}
		$("#" + selectElementId).html(htmlSelect);
	}

});
