package com.source.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.source.entities.User;



@Repository
public interface UsersRepository
        extends JpaRepository<User, String> {
	 public User findByuname(String uname);
	 public boolean existsByuname(String uname);
	
 
}


