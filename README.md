# Alfresco Hackathon 2017
This project was created during Alfresco Hackathon 2017 by Tieto people (located in Ostrava, Czech Republic) working with Alfresco. Please note the project is not fully working and should be completed (maybe in next Hackathon :-))

## Idea
Let's implement a new addon which will review a repository health. There are several Alfresco recommendations which should be followed. For example:
* Limit the maximum number of nodes in a folder
* Limit Groups hierarchy to 5
* Limit the folder hierarchy depth
* ...

Those healthy checks are inspired by [Luis Cabaceira](https://community.alfresco.com/people/lcabaceira) [blog](https://community.alfresco.com/people/lcabaceira/blog/2017/04/26/alfresco-best-practices) (chapter 8)

## Ideas for DevCon 2018
### General tasks
	- "Select job" with useful data:
		- Date + Folder which was used as start location
	- Option to start job directly from Admin console
		- Is possible already to run the job from Support Tools (Scheduled jobs)
	- Job cleaner
	- Display more data about job execution
		- For example who initiated the run, how long it took, location "top" folder
### Folder hierarchy 
	- Combine part "Nodes that exceeded node depth" to cover 16 + 17 + 18 + 19 + 20 in one record
	- Try to use "SQL approach" instead of NodeService
	- Adjust job to be run against specific location, for example only for specific site (/app:company_home/st:sites/cm:swsdp)
### Group hierarchy
	- Combine group list in the same manner as previous task

## Results from DevCon 2018
	- Created jobs for each webscript with default values in alfresco-global.properties
	- Folder hierarchy depth shows only "top" folder and nested folders are not displayed
	- Add more props to Folder hierarchy section. For example (Run By, Job parameters)
	- Add datetime to "Select job" drop-down list
	- Other fixes and refactoring
	- Solution to fix a problem with many nodes under a folder		
		- At the moment, it is ased on type of node
		- Only in organize-nodes branch
		- It could be separated as different addon

## Implementation

### Jobs

### Admin console
We were inspired by [OOTBee Support Tools](https://github.com/OrderOfTheBee/ootbee-support-tools)

