package com.source.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.source.entities.applicationdata;



@Repository
public interface ApplicationDataRepository
        extends JpaRepository<applicationdata, Integer> {
	
	 public applicationdata findByappname(String appname);
	 public boolean existsByappname(String appname);
	 public boolean existsByservertype(String servertype);
	 
 
}


