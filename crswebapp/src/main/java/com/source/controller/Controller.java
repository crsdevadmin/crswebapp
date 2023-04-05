package com.source.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.source.entities.User;
import com.source.entities.applicationdata;
import com.source.entities.languages;
import com.source.entities.mediapath;
import com.source.entities.serverIPs;
import com.source.service.ApplicationDataService;
import com.source.service.LanguagesService;
import com.source.service.MediaPathService;
import com.source.service.ServerIPsService;
import com.source.service.UserService;
import com.source.util.Utilities;

/**
 * Created by Java Developer Zone on 19-07-2017.
 */
@org.springframework.stereotype.Controller
public class Controller {


	@Autowired
	private UserService userService;

	@Autowired
	private ServerIPsService serverIPsService;

	@Autowired
	private ApplicationDataService appDataService;

	@Autowired
	private LanguagesService languageService;


	@Autowired
	private MediaPathService mediaPathService;

	
	@RequestMapping(value="/getAppDetails", method=RequestMethod.GET)
	@ResponseBody
	public String getAppDetails(){
		String returnValue = null;
		Utilities  util =  new Utilities();
		List<applicationdata> appDetails = appDataService.getApplicationList();
		if(appDetails==null || appDetails.isEmpty()) {
			System.out.println("App Details not found ");
			returnValue = util.setFailureReponse(false, "App Details not Available", "App Details");
		}else {

			returnValue = util.setSucessReponse(true, appDetails);
		}
		return returnValue;
	}


	@RequestMapping(value="/getServerIPs", method=RequestMethod.GET)
	@ResponseBody
	public String getServerIPs(){
		String returnValue = null;
		Utilities  util =  new Utilities();
		List<serverIPs> serverIPAddressList = serverIPsService.getServerIPs();
		if(serverIPAddressList==null || serverIPAddressList.isEmpty()) {
			System.out.println("Server IP not found ");
			returnValue = util.setFailureReponse(false, "Server IP not Available", "Server IP");
		}else {

			returnValue = util.setSucessReponse(true, serverIPAddressList);
		}
		return returnValue;
	}

	@RequestMapping(value="/deleteServerIP/{serverip}", method=RequestMethod.GET)
	@ResponseBody
	public String deleteServerIP(@PathVariable("serverip") String serverIP){
		String returnValue = "sucess";
		try {
			System.out.println("serverip received : "+serverIP);
			returnValue = serverIPsService.deleteServerIP(serverIP);
		} catch (Exception e) {
			returnValue = "Exception : "+e.getMessage();
		}
		return returnValue;
	}


	@RequestMapping(value="/deleteAppName/{id}", method=RequestMethod.GET)
	@ResponseBody
	public String deleteAppName(@PathVariable("id") int id){
		String returnValue = "sucess";
		try {
			System.out.println("appName received : "+id);
			returnValue = appDataService.deleteApplicationName(id);
		} catch (Exception e) {
			returnValue = "Exception : "+e.getMessage();
		}
		return returnValue;
	}
	@RequestMapping(value="/insertServerIP", method=RequestMethod.POST , headers = "Accept=application/json") 
	@ResponseBody
	public String insertServerIP(@RequestBody String strServerIPs){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {

			serverIPs serverIpAddress  = util.setJsonToObject(strServerIPs,serverIPs.class);
			returnValue = serverIPsService.insertServerIPs(serverIpAddress);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "User Email Already Exits", "user");
		}
		return returnValue;
	}

	@RequestMapping(value="/getLangByApps", method=RequestMethod.POST , headers = "Accept=application/json")
	@ResponseBody
	private String setLanguageWiseApps(@RequestBody String strlangByApps) {
		String returnValue = "Error";
		System.out.println("Received Values for Lang by Apps :"+strlangByApps);
		Utilities  util =  new Utilities();
		
		Map langByAppsValues = util.setJsonToObject(strlangByApps,HashMap.class);
		String serverType =(String)langByAppsValues.get("servertype");
		String language =(String)langByAppsValues.get("language");
		System.out.println("Received Values for Lang by Apps :"+langByAppsValues.get("servertype"));
		Map<String,Map<String,List<String>>> languageWiseServerType = new HashMap<String,Map<String,List<String>>>();
		try {
			List<applicationdata> appDetails = appDataService.getApplicationList();
			List<languages> languageLIst = languageService.getLanguages();
			Map<String,List<String>> languageWiseLanguagesProd = new HashMap<String,List<String>>();
			Map<String,List<String>> languageWiseLanguagesUAT = new HashMap<String,List<String>>();
			List<String> languageWiseAppsProd =  new ArrayList<String>();
			List<String> languageWiseAppsUAT =  new ArrayList<String>();
			boolean isProdServer = false;
			ObjectMapper mapper = new ObjectMapper();
			for(languages lang : languageLIst) {
				languageWiseAppsProd =  new ArrayList<String>();
				languageWiseAppsUAT =  new ArrayList<String>();
				isProdServer = false;
				for(applicationdata data : appDetails) {
					List<String> myLanguagesLIst = mapper.readValue(data.getLanguages(), new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});	
					System.out.println("my Languages LIst : "+myLanguagesLIst+" --- "+ data.getAppname()+" -- "+data.getServertype()+" --"+lang.getLanguagecode());
					if(myLanguagesLIst.contains(lang.getLanguagecode())) {
						if(data.getServertype().equalsIgnoreCase("1")) {
							isProdServer=true;
							languageWiseAppsProd.add(data.getAppname());
							System.out.println("Inside Prod" +languageWiseAppsProd);
						}else {
							languageWiseAppsUAT.add(data.getAppname());
							System.out.println("Inside UAT" +languageWiseAppsUAT);
						}
					}
				}

				languageWiseLanguagesProd.put(lang.getLanguagecode(), languageWiseAppsProd);
				languageWiseServerType.put("1", languageWiseLanguagesProd);
				languageWiseLanguagesUAT.put(lang.getLanguagecode(), languageWiseAppsUAT);
				languageWiseServerType.put("2", languageWiseLanguagesUAT);
				System.out.println("--------------------> "+languageWiseServerType);

			}
			if(!languageWiseServerType.isEmpty()) {
				if(languageWiseServerType.containsKey(serverType)) {
					Map<String,List<String>> mapLangByApps = languageWiseServerType.get(serverType);
					if(mapLangByApps.containsKey(language)) {
						List<String> listLanguages = mapLangByApps.get(language);
						returnValue = util.setSucessReponse(true,listLanguages);			
					}else {
						returnValue = util.setFailureReponse(false, "Language not applicable to  Apps, Select a other language", "user");
					}	
				}else {
					returnValue = util.setFailureReponse(false, "Server Type not applicable to load languaes", "user");
				}
				
			}else {
				returnValue = util.setFailureReponse(false, "Language by Application is empty", "user");
			}

		}catch(Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false,"Exception while setting Laguage by Apps", "user");
		}
		return returnValue;
	}
	@RequestMapping(value="/insertAppDetails", method=RequestMethod.POST , headers = "Accept=application/json") 
	@ResponseBody
	public String insertAppDetails(@RequestBody String strApplicationDetails){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {
			System.out.println(strApplicationDetails);
			applicationdata appData  = util.setJsonToObject(strApplicationDetails,applicationdata.class);
			returnValue = appDataService.insertApplicationDetails(appData);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "Exception while inserting App Details"+e.getMessage(), "user");
		}
		return returnValue;
	}
	@RequestMapping(value="/updateAppData", method=RequestMethod.POST , headers = "Accept=application/json") 
	@ResponseBody
	public String updateAppData(@RequestBody String strApplicationDetails){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {
			System.out.println(strApplicationDetails);
			applicationdata appData  = util.setJsonToObject(strApplicationDetails,applicationdata.class);
			returnValue = appDataService.updateApplicationDetails(appData);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "exception while updating app Data"+e.getMessage(), "user");
		}
		return returnValue;
	}

	@RequestMapping(value="/insertLanguage/{languageCode}", method=RequestMethod.GET) 
	@ResponseBody
	public String insertLanguage(@PathVariable("languageCode") String languageCode){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {
			returnValue = languageService.insertLanguageCode(languageCode);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "Exception while inserting Language "+e.getMessage(), "user");
		}
		return returnValue;
	}
	@RequestMapping(value="/insertAppMediaPath", method=RequestMethod.POST, headers = "Accept=application/json") 
	@ResponseBody
	public String insertAppMediaPath(@RequestBody String appPathJson){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {
			mediapath mediapathObj  = util.setJsonToObject(appPathJson,mediapath.class);

			mediapath mp= mediaPathService.getMediaPath();
			if(mp!=null) {
				returnValue = mediaPathService.updateAppMediaPath(mediapathObj.getApppath());
			}
			else {
				returnValue = mediaPathService.insertAppMediaPath(mediapathObj.getApppath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "Exception whlie inserting app path"+e.getMessage(), "user");
		}
		return returnValue;
	}
	@RequestMapping(value="/insertSysMediaPath", method=RequestMethod.POST, headers = "Accept=application/json") 
	@ResponseBody
	public String insertSysMediaPath(@RequestBody String sysPathJson){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {
			mediapath mediapathObj  = util.setJsonToObject(sysPathJson,mediapath.class);
			returnValue = mediaPathService.insertSysMediaPath(mediapathObj.getSyspath());

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "Exception whlie inserting sys path"+e.getMessage(), "user");
		}
		return returnValue;
	}

	@RequestMapping(value="/getLanguages", method=RequestMethod.GET)
	@ResponseBody
	public String getLanguges(){
		String returnValue = null;
		Utilities  util =  new Utilities();
		List<languages> languageList = languageService.getLanguages();
		if(languageList==null || languageList.isEmpty()) {
			System.out.println("Languages not found ");
			returnValue = util.setFailureReponse(false, "Languages not Available", "Languages");
		}else {
			returnValue = util.setSucessReponse(true, languageList);
		}
		return returnValue;
	}
	@RequestMapping(value="/getAppPath", method=RequestMethod.GET)
	@ResponseBody
	public String getAppPath(){
		String returnValue = null;
		Utilities  util =  new Utilities();
		String appPath = mediaPathService.getAppMediaPath();
		System.out.println(appPath);
		if(appPath==null || appPath.isEmpty()) {
			System.out.println("appPath not found ");
			returnValue = util.setFailureReponse(false, "appPath not Available", "appPath");
		}else {
			returnValue = util.setSucessReponse(true, mediaPathService.getMediaPath());
		}
		return returnValue;
	}
	@RequestMapping(value="/getSysPath", method=RequestMethod.GET)
	@ResponseBody
	public String getSysPath(){
		String returnValue = null;
		Utilities  util =  new Utilities();
		String sysPath = mediaPathService.getSysMediaPath();
		if(sysPath==null || sysPath.isEmpty()) {
			System.out.println("sysPath not found ");
			returnValue = util.setFailureReponse(false, "sysPath not Available", "appPath");
		}else {
			returnValue = util.setSucessReponse(true, sysPath);
		}
		return returnValue;
	}

	@RequestMapping(value="/deleteLanguage/{languageCode}", method=RequestMethod.GET)
	@ResponseBody
	public String deleteLanguage(@PathVariable("languageCode") String languageCode){
		String returnValue = "sucess";
		Utilities  util =  new Utilities();
		try {
			System.out.println("languageCode received : "+languageCode);
			returnValue = languageService.deleteLanguageCode(languageCode);
			appDataService.deleteApplicationLanguage();
		} catch (Exception e) {
			returnValue = "Exception : "+e.getMessage();
			returnValue = util.setFailureReponse(false, "issue while deleting language", "Languages");
			e.printStackTrace();
		}
		return returnValue;
	}

	@RequestMapping(value="/getUsers", method=RequestMethod.GET)
	@ResponseBody
	public String getUsers(){
		String returnValue = null;
		Utilities  util =  new Utilities();
		List<User> getAllUser = userService.getAllUsers();
		if(getAllUser==null || getAllUser.isEmpty()) {
			System.out.println("User Details not found "+returnValue);
			returnValue = util.setFailureReponse(false, "All the users are appoved", "user");
		}else {
			List<User> getNonApprovedUsers = new ArrayList<>(); 
			System.out.println(getAllUser.size()+" "+getNonApprovedUsers.size());
			for(int i=0; i<getAllUser.size();i++) {
				User user = getAllUser.get(i);
				System.out.println(user.getUsername());
				if(user.getScope()==0) {
					getNonApprovedUsers.add(getAllUser.get(i));
				}
			}
			if(getNonApprovedUsers.isEmpty()) {
				System.out.println("User Details not found "+returnValue);
				returnValue = util.setFailureReponse(false, "All the users are appoved", "user");			
			}else {
				returnValue = util.setSucessReponse(true, getNonApprovedUsers);
			}
		}
		return returnValue;
	}

	@RequestMapping(value="/login", method=RequestMethod.POST , headers = "Accept=application/json")  
	@ResponseBody
	public String getUser(@RequestBody String value) {
		String returnValue = null;
		Utilities  util =  new Utilities();
		System.out.println("Json Value :"+value);

		try {
			if(null!=value && !"".equals(value)) {
				Gson gson = new Gson();
				User newUserEntity = gson.fromJson(value, User.class);
				System.out.println("usernaem "+newUserEntity.getUsername());
				String username = newUserEntity.getUsername();
				String password = newUserEntity.getPassword();
				returnValue = userService.getUserDetailsById(username,password);
				if(null==returnValue) {
					System.out.println("User Details not found "+returnValue);
					returnValue = util.setFailureReponse(false, "Uid does not Exits", "user");
				}
			}else {
				System.out.println("uid is empty or null "+returnValue);
				returnValue = util.setFailureReponse(false, "uid is empty or null", "user");
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "uid is empty or null", "user");
		}

		return returnValue;  
	}

	@RequestMapping(value="/register", method=RequestMethod.POST , headers = "Accept=application/json") 
	@ResponseBody
	public String createNewUser(@RequestBody String userData){
		String returnValue = "Error";
		Utilities  util =  new Utilities();
		try {

			User user  = util.setJsonToObject(userData,User.class);
			returnValue = userService.createUser(user);
			if("Error".equalsIgnoreCase(returnValue)) {
				returnValue = util.setFailureReponse(false, "User Email Already Exits", "user");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "User Email Already Exits", "user");
		}
		return returnValue;
	}

	@RequestMapping(value="/deleteuser/{uname}", method=RequestMethod.GET)
	@ResponseBody
	public String deleteUser(@PathVariable("uname") String uid){
		String returnValue = "sucess";
		try {
			System.out.println("UID received : "+uid);
			returnValue = userService.deleteUser(uid);
		} catch (Exception e) {
			returnValue = "Exception : "+e.getMessage();
		}
		return returnValue;
	}
	@RequestMapping(value="/approveuser", method=RequestMethod.POST , headers = "Accept=application/json")  
	@ResponseBody
	public String approveUser(@RequestBody String value) {
		String returnValue = null;
		Utilities  util =  new Utilities();
		System.out.println("Json Value :"+value);

		try {
			if(null!=value && !"".equals(value)) {
				Gson gson = new Gson();
				User newUserEntity = gson.fromJson(value, User.class);
				System.out.println("usernaem "+newUserEntity.getUsername());
				returnValue =	userService.updateUser(newUserEntity);
			}else {
				System.out.println("uid is empty or null "+returnValue);
				returnValue = util.setFailureReponse(false, "uid is empty or null", "user");
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = util.setFailureReponse(false, "uid is empty or null", "user");
		}

		return returnValue;  
	}

}