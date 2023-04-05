package com.source.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.source.entities.mediapath;



@Repository
public interface MediaPathRepository  extends JpaRepository<mediapath, String> {
}


