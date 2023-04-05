package com.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.source.RecordNotFoundException;
import com.source.entities.languages;
import com.source.repository.LanguageRepository;
import com.source.util.Utilities;



@Service
public class LanguagesService {
	@Autowired
	LanguageRepository languageRep;
	public String insertLanguageCode(String lclLanguageCode) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received Language Code : "+lclLanguageCode);
			if (languageRep.existsByLanguagecode(lclLanguageCode)) {
				return util.setFailureReponse(false, "Language Code Already Exits", "Server IP");
			}else {
				languages languageCode = new languages();
				languageCode.setLanguagecode(lclLanguageCode);
				languageCode = languageRep.save(languageCode);	
				return util.setSucessReponse(true, languageCode.getLanguagecode());
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
	public List<languages> getLanguages() {
		List<languages> languageList = languageRep.findAll();
		System.out.println("Language Codes: " + languageList);
		if (languageList.size() > 0) {
			return languageList;
		} else {
			return null;
		}
	} 

	public String deleteLanguageCode(String languageCode) throws RecordNotFoundException
	{
		Utilities util = new Utilities();
		languages language = languageRep.getById(languageCode);   
		if (language != null) {
			System.out.println("Language Code  is available to delete");
			languageRep.delete(language);
			return util.setSucessReponse(true, "Language Code : '"+languageCode+"', Deleted Successfully");
		}else {
			System.out.println("Lanauge Code not available in db");
			return util.setFailureReponse(false,"Lanauge Code not available in db","serverIP");
		}

	}
}