package com.source.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRawValue;

@Entity
@Table(name="languages", schema="servmain")
public class languages {
	

@Id
@Column(name="languagecode")
public String languagecode;

@Column(name = "appnames", columnDefinition = "json")
@JsonRawValue
private String appnames;


public String getAppnames() {
	return appnames;
}

public void setAppnames(String appnames) {
	this.appnames = appnames;
}

public String getLanguagecode() {
	return languagecode;
}

public void setLanguagecode(String languagecode) {
	this.languagecode = languagecode;
}


}
