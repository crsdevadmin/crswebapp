package com.source.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user", schema="servmain")
public class User {
	

@Id
@Column(name="uname")
public String uname;

@Column(name="pass")
public String pass;

@Column(name="scope")
public int scope;


public String getUsername() {
	return uname;
}


public void setUsername(String username) {
	this.uname = username;
}


public String getPassword() {
	return pass;
}


public void setPassword(String password) {
	this.pass = password;
}


public int getScope() {
	return scope;
}


public void setScope(int scope) {
	this.scope = scope;
}


@Override
public String toString() {
    return "User Details [loginUsername=" + uname + ", loginPassword=" + pass;
}


}
