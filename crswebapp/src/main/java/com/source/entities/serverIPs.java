package com.source.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="serverips", schema="servmain")
public class serverIPs {
	

@Id
@Column(name="serverip")
public String serverip;

@Column(name="servertype")
public String servertype;

@Override
public String toString() {
    return "server Details [server ip=" + serverip + ", servertype=" + servertype;
}

public String getServerip() {
	return serverip;
}

public void setServerip(String serverip) {
	this.serverip = serverip;
}

public String getServertype() {
	return servertype;
}

public void setServertype(String servertype) {
	this.servertype = servertype;
}


}
