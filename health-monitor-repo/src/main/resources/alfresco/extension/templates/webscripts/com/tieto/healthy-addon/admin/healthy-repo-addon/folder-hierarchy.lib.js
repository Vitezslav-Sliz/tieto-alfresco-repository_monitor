/**
 * Copyright (C) 2017 Order of the Bee
 *
 * This file is part of Community Support Tools
 *
 * Community Support Tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Community Support Tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Community Support Tools. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Linked to Alfresco
 * Copyright (C) 2005-2017 Alfresco Software Limited.
 */

function getAllVersions(path) {
	var path = companyhome.childByNamePath(path);
	var versionsList = "\'[";
	if(path == null){
		versionsList += "]\'";
		return versionList;
	}		
	var tietoFolder = path.getChildren();
	for (var i = 0; i < tietoFolder.length; i++) {
		versionsList +=("{\"nodeRef\":\"" + tietoFolder[i].getNodeRef() + "\" , \"path\":\"" + tietoFolder[i].displayPath + "/" + tietoFolder[i].name + "\", \"count\": \"1521\", \"data\": " + tietoFolder[i].content + " }");
		if (i < tietoFolder.length - 1) {
			versionsList += ",";
		}
		//logger.debug(versionsList[i]);
	}
	versionsList += "]\'";

	// Remove new lines
	//return jsonUtils.toJSONObject(versionsList.replace(/(\r\n|\n|\r)/gm,""));
	return versionsList.replace(/(\r\n|\n|\r)/gm,"");
}

/*
function getFolderHierarchyReport() {
	var array = model.allVersions;
	logger.debug("Node = " + node.content);
	
	model.node = jsonUtils.toObject(node.content);
}
*/