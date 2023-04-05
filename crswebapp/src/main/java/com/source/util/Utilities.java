package com.source.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Utilities {

	public boolean isNullorWhiteSpaces(String value){
		boolean flag = true;
		try {
			if (value != null || !value.trim().equals("")) {
				flag = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}	
		return flag;
	}
	public <Generic> Generic  setJsonToObject(String userData,Class<Generic> clazz) {
		Generic genericData = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		try {
			genericData = objectMapper.readValue(userData, clazz);

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return genericData;
	}

	public <Generic> Generic setObjectToJson(Generic response) {
		String responseJson = null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	//	objectMapper.setSerializationInclusion(Include.NON_NULL);
		try {
			responseJson = objectMapper.writeValueAsString(response);	
			System.out.println(responseJson);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return (Generic)responseJson;
	}
	public String setFailureReponse(boolean success, String message,String type) {
		response  res =  new response();
		res.setSuccess(success);
		exception exc =new exception();
		exc.setMessage(message);
		exc.setType(type);
		res.setException(exc);

		return String.valueOf(setObjectToJson(res));

	}
	public String setSucessReponse(boolean success,Object user) {
		response  res =  new response();
		res.setSuccess(success);
		res.setData(user);
		return String.valueOf(setObjectToJson(res));

	}
	public String setFailureReponse(boolean success, Exception e) {
		response  res =  new response();
		res.setSuccess(success);
		res.setException(setErrorDetails(e));
		return String.valueOf(setObjectToJson(res));

	}
	public String setFailureReponse(boolean success, DataAccessException e) {
		response  res =  new response();
		res.setSuccess(success);
		res.setException(setErrorDetails(e));
		return String.valueOf(setObjectToJson(res));

	}
	private exception setErrorDetails(final Throwable cause)
	{
		Throwable rootCause = cause;
		while(rootCause.getCause() != null &&  rootCause.getCause() != rootCause)
			rootCause = rootCause.getCause();

		exception exc =new exception();
		exc.setMessage(cause.getMessage());
		exc.setLineNumber(rootCause.getStackTrace()[0].getLineNumber());
		exc.setMethod(rootCause.getStackTrace()[0].getClassName()+"."+rootCause.getStackTrace()[0].getMethodName());
		exc.setType("System");
		return exc;
	}
	private exception setErrorDetails(final DataAccessException cause)
	{

		exception exc =new exception();
		exc.setMessage(cause.getMostSpecificCause().getMessage());
		exc.setLineNumber(cause.getStackTrace()[0].getLineNumber());
		exc.setMethod(cause.getStackTrace()[0].getClassName()+"."+cause.getStackTrace()[0].getMethodName());
		exc.setType("System");
		return exc;
	}
	public List<String> getHeaders(MultiValueMap<String, String> headers,String path){
		List<String> listOfRoutePaths  = null;
		List<String> routePaths = headers.get("multiple");
		System.out.println(routePaths);
		if(null!=routePaths && !routePaths.isEmpty()) {
			listOfRoutePaths= new ArrayList(Arrays.asList(routePaths.get(0).split(",")));
			System.out.println(listOfRoutePaths);
		}
		if(null== listOfRoutePaths) {
			listOfRoutePaths  = new ArrayList<String>();
			listOfRoutePaths.add(path);
		}else {
			listOfRoutePaths.add(path);
		}
		return listOfRoutePaths;
	}

}
