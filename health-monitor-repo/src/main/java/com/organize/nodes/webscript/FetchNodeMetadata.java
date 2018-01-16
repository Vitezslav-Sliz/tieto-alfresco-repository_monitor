package com.organize.nodes.webscript;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;


public class FetchNodeMetadata extends DeclarativeWebScript {

	private DictionaryService dictionaryService;
	private ServiceRegistry serviceRegistry;
	private Repository repositoryHelper;


	public Repository getRepositoryHelper() {
		return repositoryHelper;
	}

	public void setRepositoryHelper(Repository repositoryHelper) {
		this.repositoryHelper = repositoryHelper;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public DictionaryService getDictionaryService() {
		return dictionaryService;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}


	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		Map<String, Object> model = new HashMap<>();
		String modelParam=req.getParameter("typeofdata");

		if(modelParam.equals("model")) {
			Collection<QName> allModels=dictionaryService.getAllModels();
			Map<String,String> mapModels=new HashMap<>();
			for(QName md:allModels){
				mapModels.put(md.getNamespaceURI(),md.getLocalName());
			}
			model.put("types", mapModels);
		}else if(modelParam.equals("type")) {
			String selectedModel=req.getParameter("model");
			Collection<QName> allTypes=dictionaryService.getAllTypes();
			Map<String,String> mapType=new HashMap<>();
			for(QName type:allTypes){
				if(type.getNamespaceURI().equals(selectedModel)){
					mapType.put(dictionaryService.getType(type).getTitle()==null?type.toPrefixString():dictionaryService.getType(type).getTitle(),type.toPrefixString());
				}
			}
			System.out.println(mapType);
			model.put("types", mapType);
		}else if(modelParam.equals("changeType")) {
			NodeRef folder=new NodeRef("workspace://SpacesStore/17e6e868-272c-4c42-b7a5-7d30e75333e1");
			List<ChildAssociationRef> lst=serviceRegistry.getNodeService().getChildAssocs(folder);
			int i=0;
			for(ChildAssociationRef child:lst) {
				if(i%2==0) {
					serviceRegistry.getNodeService().setType(child.getChildRef(), MonitorModel.TYPE_INVOICE);			
				}else if(i%3==0 ) {
					serviceRegistry.getNodeService().setType(child.getChildRef(), MonitorModel.TYPE_REPORT_DOCUMENT);	
				}else if(i%5==0) {
					serviceRegistry.getNodeService().setType(child.getChildRef(), MonitorModel.TYPE_STANDARD_DOCUMENT);	
				}
				i++;
			}
		}else if(modelParam.equals("organizeNodes")) {
			String newFolderName=req.getParameter("folderName");
			String path[]=req.getParameter("path").split("/");
			String type=req.getParameter("selectedType");
			NodeRef refactoringFolder=null;
			if(path!=null) {
				try {
					refactoringFolder=serviceRegistry.getFileFolderService().resolveNamePath(repositoryHelper.getCompanyHome(),  Arrays.asList(path)).getNodeRef();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
			//System.out.println(selectedFolder);
			//Creating new folder with specified name
			Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
			props.put(ContentModel.PROP_NAME, newFolderName); 
			List<String> pathList=new ArrayList<>();
			NodeRef node = serviceRegistry.getNodeService().createNode(refactoringFolder, 
                     ContentModel.ASSOC_CONTAINS, 
                     QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, newFolderName),
                     ContentModel.TYPE_FOLDER, 
                     props).getChildRef();
			
			//Logic for moving nodes
			List<ChildAssociationRef> nodes=serviceRegistry.getNodeService().getChildAssocs(refactoringFolder);
			for(ChildAssociationRef child:nodes) {
				System.out.println(serviceRegistry.getNodeService().getType(child.getChildRef()).toPrefixString()+"  "+type);
				if(serviceRegistry.getNodeService().getType(child.getChildRef()).toPrefixString().equals(type)) {
					serviceRegistry.getNodeService().moveNode(child.getChildRef(),node, ContentModel.ASSOC_CONTAINS, null);
				}
			}
		
			
		}
		return model;
	}

	

}