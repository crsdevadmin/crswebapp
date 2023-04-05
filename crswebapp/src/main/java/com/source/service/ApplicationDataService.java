package com.source.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.source.RecordNotFoundException;
import com.source.entities.applicationdata;
import com.source.entities.languages;
import com.source.repository.ApplicationDataRepository;
import com.source.util.Utilities;



@Service
public class ApplicationDataService {

	@Autowired
	ApplicationDataRepository appDataRep;
	@Autowired
	private ApplicationDataService appDataService;

	@Autowired
	private LanguagesService languageService;

	public List<applicationdata> getApplicationList() {
		List<applicationdata> appNameList = appDataRep.findAll();
		System.out.println("Applicaiton Names, " + appNameList);
		if (appNameList.size() > 0) {
			return appNameList;
		} else {
			return null;
		}
	} 
	public void deleteApplicationLanguage() throws Exception {
			List<applicationdata> appDetails = appDataService.getApplicationList();
			List<languages> languageList = languageService.getLanguages();
			List<String> languageListinString = new ArrayList<String>();
			for(languages lang:languageList) {
				languageListinString.add(lang.getLanguagecode());
			}
			System.out.println("App Details :"+appDetails +" Languages :"+languageListinString);
			ObjectMapper mapper = new ObjectMapper();
			for(applicationdata appData:appDetails) {
				
				List<String> appLangList = mapper.readValue(appData.getLanguages(), new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});	
				List<String> newAppLanguages = new ArrayList<String>();
				newAppLanguages.addAll(appLangList);
				System.out.println("Mapper List"+appLangList);
				for(String lang:appLangList) {
					if(!languageListinString.contains(lang)) {
						newAppLanguages.remove(appLangList.indexOf(lang));
						System.out.println("removing :"+newAppLanguages);
					}

				}
				String appLanguages = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newAppLanguages);
				System.out.println("Mapper Writer Languages "+appLanguages);
				appData.setLanguages(appLanguages);
				System.out.println(appData);
				appDataService.updateApplicationDetails(appData);

			}
	}
	public String deleteApplicationName(int id) throws RecordNotFoundException
	{
		Utilities util = new Utilities();
		applicationdata applicaitonData = appDataRep.getById(id);  
		if (applicaitonData != null) {
			System.out.println(applicaitonData.getAppname()+" application name is available to delete");
			appDataRep.delete(applicaitonData);
			return util.setSucessReponse(true, "application name: '"+applicaitonData.getAppname()+"', Deleted Successfully");
		}else {
			System.out.println("app name not available in db");
			return util.setFailureReponse(false,"app name not available in db","appname");
		}

	}
	public String insertApplicationDetails(applicationdata lclapplicationdata) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received application name data :"+lclapplicationdata);

			if (appDataRep.existsByappname(lclapplicationdata.getAppname()) && appDataRep.findByappname(lclapplicationdata.getAppname()).getServertype().equalsIgnoreCase(lclapplicationdata.getServertype())) {
				return util.setFailureReponse(false, "app name Already Exits", "appname");
			}else {
				applicationdata appData =  new applicationdata();
				appData.setAppname(lclapplicationdata.getAppname());
				appData.setLanguages(lclapplicationdata.getLanguages());
				appData.setServertype(lclapplicationdata.getServertype());
				System.out.println("Saving application Name "+appData.getAppname());
				lclapplicationdata = appDataRep.save(appData);	
				return util.setSucessReponse(true, appData.getAppname());
			}
		} catch (DataAccessException de) {
			System.out.println("--------------------" + de.getLocalizedMessage());
			System.out.println("--------------------" + de.getMostSpecificCause().getMessage());
			return util.setFailureReponse(false, de);
		} catch (Exception e) {
			e.printStackTrace();
			return util.setFailureReponse(false, e);
		}

	}
	public String updateApplicationDetails(applicationdata lclapplicationdata) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received application name data :"+lclapplicationdata);
			lclapplicationdata = appDataRep.save(lclapplicationdata);	
			return util.setSucessReponse(true, lclapplicationdata.getAppname());
		} catch (DataAccessException de) {
			System.out.println("--------------------" + de.getLocalizedMessage());
			System.out.println("--------------------" + de.getMostSpecificCause().getMessage());
			return util.setFailureReponse(false, de);
		} catch (Exception e) {
			e.printStackTrace();
			return util.setFailureReponse(false, e);
		}

	}


}