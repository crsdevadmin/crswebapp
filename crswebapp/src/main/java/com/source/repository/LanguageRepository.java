package com.source.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.source.entities.languages;



@Repository
public interface LanguageRepository
        extends JpaRepository<languages, String> {
	
	 public languages findByLanguagecode(String languagecode);
	 public boolean existsByLanguagecode(String languagecode);
	
 
}


