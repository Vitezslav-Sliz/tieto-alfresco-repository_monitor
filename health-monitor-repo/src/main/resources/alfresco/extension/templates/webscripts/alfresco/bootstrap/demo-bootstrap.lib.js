function createStructure(){

	// Check if Tieto folder exists
	if(!companyhome.childByNamePath("Data Dictionary/Tieto")){
		// Create Tieto folder if it doesn't exist 
		companyhome.childByNamePath("Data Dictionary").createFolder("Tieto");
	}
	
	for(var i = 0; i<contents.length; i++) {
		companyhome.childByNamePath("Data Dictionary/Tieto").createNode("job" + i +".json", "cm:content")
															.setContent(contents[i]);
	}
}