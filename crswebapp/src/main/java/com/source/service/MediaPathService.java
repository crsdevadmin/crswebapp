package com.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.source.entities.mediapath;
import com.source.repository.MediaPathRepository;
import com.source.util.Utilities;



@Service
public class MediaPathService {
	@Autowired
	MediaPathRepository mediapathRep;
	public String insertAppMediaPath(String appPath) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received app path : "+appPath);
			mediapath mp =  new mediapath();
			mp.setApppath(appPath);
			mediapathRep.save(mp);	
			return util.setSucessReponse(true, mp.getApppath());
		} catch (DataAccessException de) {
			System.out.println("--------------------" + de.getLocalizedMessage());
			System.out.println("--------------------" + de.getMostSpecificCause().getMessage());
			return util.setFailureReponse(false, de);
		} catch (Exception e) {
			e.printStackTrace();
			return util.setFailureReponse(false, e);
		}

	}
	public String updateAppMediaPath(String appPath) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received app path : "+appPath);
			mediapath mp=  getMediaPath();
			mediapathRep.delete(mp);
			String sysPath = mp.getSyspath();
			mediapath mpnew = new mediapath();
			mpnew.setApppath(appPath);
			mpnew.setSyspath(sysPath);
			mediapathRep.save(mpnew);	
			return util.setSucessReponse(true, mp.getApppath());
		} catch (DataAccessException de) {
			System.out.println("--------------------" + de.getLocalizedMessage());
			System.out.println("--------------------" + de.getMostSpecificCause().getMessage());
			return util.setFailureReponse(false, de);
		} catch (Exception e) {
			e.printStackTrace();
			return util.setFailureReponse(false, e);
		}

	}
	public String insertSysMediaPath(String sysPath) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received sys path : "+sysPath);

			mediapath mp= getMediaPath();
			if(mp!=null) {
				mp.setSyspath(sysPath);
				mediapathRep.save(mp);	
				return util.setSucessReponse(true, mp.getSyspath());
			}else {
				return util.setFailureReponse(true,"app Media Path not set","mediapath");
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
	public String getAppMediaPath() {
		String appPath = mediapathRep.findAll().get(0).getApppath();
		System.out.println(" Application Path: " + appPath);
		return appPath;
	} 

	public String getSysMediaPath() {
		String sysPath = mediapathRep.findAll().get(0).getSyspath();
		System.out.println(" System Path: " + sysPath);
		return sysPath;
	} 
	public mediapath getMediaPath() {
		List<mediapath> ls = mediapathRep.findAll();
		System.out.println("Media Path ise"+ls.size());
		if(ls!=null && !ls.isEmpty())
			return ls.get(0);
		else
			return null;
	}
}