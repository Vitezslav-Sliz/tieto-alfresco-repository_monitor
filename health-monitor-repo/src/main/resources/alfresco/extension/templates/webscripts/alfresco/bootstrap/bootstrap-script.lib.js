// Helper to return value of the url parameter or fallback to default value if not found.
function getUrlParameter(urlParam, defaultValue) {
	var value = args[urlParam];
	if (!value) {
		logger.debug("getUrlParameter | " + urlParam + " is not set, set it to " + defaultValue);
		value = defaultValue;
	} else {
		logger.debug("getUrlParameter | " + urlParam + " is " + value);
	}
	return value;
}

// Create users from the JSON
function createUser(user) {
	if ((!user.userName) || (user.userName.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "User name missing when creating person");
		return;
	}
	
	if ((!user.firstName) || (user.firstName.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "First name missing when creating person");
		return;
	}

	if ((!user.lastName) || (user.lastName.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "Last name missing when creating person");
		return;
	}
	
	if ((!user.email) || (user.email.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "Email missing when creating person");
		return;
	}
	
	var password = "passwd";
	if (user.password)  {
	  password = user.password;
	}
	
	var enableAccount = false;
	if ("true".equals(user.enableAccount.toString())) {
		enableAccount = true;
	}
	
	logger.debug("createUser(user) - username = " + user.userName + " firstname = " + user.firstName + " lastname = " + user.lastName + " email = " + user.email +  " enable = " + enableAccount);
	var person = people.createPerson(user.userName.toString(), user.firstName.toString(), user.lastName.toString(), user.email.toString(), password.toString(), enableAccount);
	
	// Return error message if a person with that user name could not be created
	if (person === null) {
		status.setCode(status.STATUS_CONFLICT, "User name already exists: " + userName);
		return;
	} else {
		return user.userName.toString();
	}
}

// Create groups from the JSON
function createGroup(group) {
	if ((!group.authorityName) || (group.authorityName.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "Authority name missing when creating group");
		return;
	}
	if ((!group.authorityDisplayName) || (group.authorityDisplayName.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "Display name missing when creating group");
		return;
	}
	
	// Check if this group already exists
	if (!groups.getGroup(group.authorityName)) {
		logger.debug("This group does not exist = " + group.authorityName);
		
		// Let's create the group as a root group
		try {
		 	groups.createRootGroup(group.authorityName.toString(), group.authorityDisplayName.toString());
		}
		catch(e) {
			status.code = status.STATUS_INTERNAL_SERVER_ERROR;
			return;
		}
		logger.debug("createGroup(group) - authorityName = " + group.authorityName + " authorityDisplayName = " + group.authorityDisplayName);
		return group.authorityName.toString();
	}  else {
		logger.debug("This group already exists = " + group.authorityName);
	}
}

// Assign members to created groups
function assignMember(group) {
	logger.debug("We will add " +  group.users.length + " users to " + group.authorityName);
	var foundGroup = groups.getGroup(group.authorityName);

	if (foundGroup) {
		logger.debug(foundGroup.getDisplayName());
		for (var i=0; i < group.users.length; i++) {
			
			// Check if user exists and if already is group member
			var foundPerson = people.getPerson(group.users[i].username);
			if ((foundPerson != null) && ! isMember(group.users[i].username, foundGroup)) {
				foundGroup.addAuthority(group.users[i].username);
			}
		}	
	}	
}

// Check membership
function isMember(username, foundGroup) {
	var groupMembers = foundGroup.getChildUsers();
	for (var i = 0; i < groupMembers.length; i++) {
		if (username.toString().equals(groupMembers[i].userName.toString())) {
			logger.debug("This user is already existing in this group! ");
			return true;
		} 
	}
	return false;
}

// Nest groups predefined in JSON
function nestGroups(group) {
	var children = "GROUP_" + group.children;
	if (children) {
		// Check if group already exists
		var parent = groups.getGroup(group.parent);
		if (parent != null) {
			try {
				parent.addAuthority(children);
			} 
			catch(e) {
				status.code = status.STATUS_INTERNAL_SERVER_ERROR;
				return;
			}
		}
	}
}

// Create sites only on repository level from the JSON
function createSites(site) {
	if ((!site.sitePreset) || (!site.shortName) || (!site.title) || (!site.visibility) || (site.sitePreset.length == 0) || (site.shortName.length == 0) || (site.title.length == 0) || (site.visibility.length == 0)) {
		status.setCode(status.STATUS_BAD_REQUEST, "User name missing when creating person");
		return;
	} else {
		var sitePreset = site.sitePreset.toString();
		var shortName = site.shortName.toString();
		var title = site.title.toString();
		var description = site.description.toString();
		var visibility = site.visibility.toString();

		// Create PUBLIC site
		if ("public".equals(visibility) && visibility != null && visibility.length != 0) {
			var createdSite = siteService.createSite(sitePreset,shortName,title,description,siteService.PUBLIC_SITE);
		}
		
		// Create PRIVATE site
		if ("private".equals(visibility) && visibility != null && visibility.length != 0) {
			var createdSite = siteService.createSite(sitePreset,shortName,title,description,siteService.PRIVATE_SITE);
		}
		
		// Create MODERATED site
		if ("moderated".equals(visibility) && visibility != null && visibility.length != 0) {
			var createdSite = siteService.createSite(sitePreset,shortName,title,description,siteService.MODERATED_SITE);
		} 
		logger.debug("Site has been created.");
	}
}

// Assign site members predefined in JSON
function addSiteMembers(sitemembers){
	var site = siteService.getSite(sitemembers.siteShortName);
	if (site) {
		
		// Assign Site Managers
   		if (sitemembers.SiteManager.length != 0){
   			for (var a=0; a < sitemembers.SiteManager.length; a++){
   				var authorityName = sitemembers.SiteManager[a].authorityName;
   				var role = "SiteManager";
   				site.setMembership(authorityName, role);
   				}
   			}
   		
   		// Assign Site Collaborators
   		if (sitemembers.SiteCollaborator.length != 0){
   			for (var b=0; b < sitemembers.SiteCollaborator.length; b++){
   				var authorityName = sitemembers.SiteCollaborator[b].authorityName;
   				var role = "SiteCollaborator";
   				site.setMembership(authorityName, role);
   				}
   			}
   		
   		// Assign Site Contributors
   		if (sitemembers.SiteContributor.length != 0){
   			for (var c=0; c < sitemembers.SiteContributor.length; c++){
   				var authorityName = sitemembers.SiteContributor[c].authorityName;
   				var role = "SiteContributor";
   				site.setMembership(authorityName, role);
   				}
   			}
   		
   		// Assign Site Consumers
   		if (sitemembers.SiteConsumer.length != 0){
   			for (var d=0; d < sitemembers.SiteConsumer.length; d++){
   				var authorityName = sitemembers.SiteConsumer[d].authorityName;
   				var role = "SiteConsumer";
   				site.setMembership(authorityName, role);
   				}
			}
		}
}

// Create folder and nodes structure predefined in the JSON
function createStructure(structure){
	
	// Check if Document Library exists
	if (!companyhome.childByNamePath("Sites/" + structure.siteShortName + "/documentLibrary")) {
		
		// Create document library folder if it doesn't exist 
		var documentLibraryRoot = companyhome.childByNamePath("Sites/" + structure.siteShortName);
		var createDocumentLibrary = documentLibraryRoot.createFolder("documentLibrary");
	}
	
	// Create folder hierarchy
	var path = "Sites/" + structure.siteShortName.toString() + "/documentLibrary";
	for (var i=1; i < structure.folderHierarchyDepth; i++) {
		var folderHome = companyhome.childByNamePath(path);
		var folderName = "FolderLevel" + i.toString();
		var createfolderDepth = folderHome.createFolder(folderName);
		path = path + "/" + folderName;
		
		// Create nodes in defined folderLevelForNodes in hierarchy
		if (i == structure.folderLevelForNodes) {
			for (var y=1; y < structure.numberOfNodes; y++)
				{
					 var createNodes = folderHome.createNode("NodeNumber" + y.toString(),structure.nodeTypes);
				}
			}
		}
	}