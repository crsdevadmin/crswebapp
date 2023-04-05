package com.source.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.source.entities.serverIPs;



@Repository
public interface ServerIPRepository
        extends JpaRepository<serverIPs, String> {
	
	 public serverIPs findByserverip(String serverip);
	 public boolean existsByserverip(String serverip);
	
 
}


