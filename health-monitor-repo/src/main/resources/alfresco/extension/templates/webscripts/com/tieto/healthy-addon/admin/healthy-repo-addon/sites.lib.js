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
function getSitesCount()
{
 
	var node = monitorService.
 	var node = search.findNode('workspace://SpacesStore/ccfa3a3e-3905-4c6b-aed0-3770e6084e00'); 

 	if (!node) {
 		model.node = {'sitesCount': 10};
 	} else {
		model.node = jsonUtils.toObject(node.content);	 		
 	}
	
   
}
