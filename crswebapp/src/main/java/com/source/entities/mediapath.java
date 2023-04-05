package com.source.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mediapath", schema="servmain")
public class mediapath {
	

@Id
@Column(name="apppath")
public String apppath;

@Column(name = "syspath")
private String syspath;

public String getApppath() {
	return apppath;
}

public void setApppath(String apppath) {
	this.apppath = apppath;
}

public String getSyspath() {
	return syspath;
}

public void setSyspath(String syspath) {
	this.syspath = syspath;
}



}
