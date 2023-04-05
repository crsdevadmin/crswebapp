package com.source.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.source.RecordNotFoundException;
import com.source.entities.serverIPs;
import com.source.repository.ServerIPRepository;
import com.source.util.Utilities;



@Service
public class ServerIPsService {

	@Autowired
	ServerIPRepository serverIPRepository;

	public List<serverIPs> getServerIPs() {
		List<serverIPs> serverIPList = serverIPRepository.findAll();
		System.out.println("Server IPS: " + serverIPList);
		if (serverIPList.size() > 0) {
			return serverIPList;
		} else {
			return null;
		}
	} 

	public String deleteServerIP(String serverIP) throws RecordNotFoundException
	{
		Utilities util = new Utilities();
		serverIPs serverIPAddress = serverIPRepository.getById(serverIP);   
		if (serverIPAddress != null) {
			System.out.println("server IP is available to delete");
			serverIPRepository.delete(serverIPAddress);
			return util.setSucessReponse(true, "server IP : '"+serverIP+"', Deleted Successfully");
		}else {
			System.out.println("server IP not available in db");
			return util.setFailureReponse(false,"server IP not available in db","serverIP");
		}

	}
	public String insertServerIPs(serverIPs lclServerIPs) {
		Utilities util = new Utilities();
		try {
			System.out.println(" Received Server IP :"+lclServerIPs);
			if (serverIPRepository.existsByserverip(lclServerIPs.getServerip())) {
				return util.setFailureReponse(false, "ServerIP Already Exits", "Server IP");
			}else {
				
				if(sendPingRequest(lclServerIPs.getServerip())) {
					lclServerIPs = serverIPRepository.save(lclServerIPs);	
					return util.setSucessReponse(true, lclServerIPs.getServerip());
				}else {
					System.out.println("Sorry ! We can't reach to this host");
					return util.setFailureReponse(false, "Sorry ! We can't reach to this host","");
				}
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
	
	public boolean sendPingRequest(String ipAddress)throws UnknownHostException, IOException
	{
		InetAddress geek = InetAddress.getByName(ipAddress);
		System.out.println("Sending Ping Request to " + ipAddress);
		if (geek.isReachable(5000)) {
			return true;
		}else {
			return false;
		}
	}
}