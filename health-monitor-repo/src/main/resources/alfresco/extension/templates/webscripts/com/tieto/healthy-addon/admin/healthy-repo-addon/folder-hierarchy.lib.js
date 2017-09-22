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

function getAllVersions () {
	logger.error("Find all JSONs ");
}

function getFolderHierarchyReport() {
	var node = search.findNode('workspace://SpacesStore/834f738c-7822-4af0-a939-497a2b10ffb9');
	logger.debug("Node = " + node.content);
	
	model.node = jsonUtils.toObject(node.content);
}
