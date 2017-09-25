/**
 * 
 */
var path = "Data Dictionary/Tieto";
var tietoFolder = companyhome.childByNamePath(path).getChildren();
var versionsList = "[";
for (var i = 0; i < tietoFolder.length; i++) {
	versionsList +=("{\"nodeRef\":\"" + tietoFolder[i].getNodeRef() + "\" , \"path\":\"" + tietoFolder[i].displayPath + "/" + tietoFolder[i].name + "\", \"count\": \"1521\" }");
	if (i < tietoFolder.length - 1) {
		versionsList += ",";
	}
	//logger.debug(versionsList[i]);
}
versionsList += "]";

// Remove new lines
//return jsonUtils.toJSONObject(versionsList.replace(/(\r\n|\n|\r)/gm,""));
model.jobs =  versionsList.replace(/(\r\n|\n|\r)/gm,"");
