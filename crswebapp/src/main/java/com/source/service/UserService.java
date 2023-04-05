package com.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.source.RecordNotFoundException;
import com.source.entities.User;
import com.source.repository.UsersRepository;
import com.source.util.Utilities;


 
@Service
public class UserService {
     
    @Autowired
    UsersRepository userRepository;
    
    public List<User> getAllUsers() {
		List<User> userList = userRepository.findAll();
		System.out.println("getAllUsers: " + userList);
		if (userList.size() > 0) {
			return userList;
		} else {
			return null;
		}
	} 
  
    
    public String getUserDetailsById(String UId,String pass) throws RecordNotFoundException {
		Utilities util = new Utilities(); 
		System.out.println("UID recieved : "+UId);
		if(userRepository.existsById(UId)) { 
			System.out.println("UID found");
			User userDetails = userRepository.findByuname(UId);
			String cUId = userDetails.getUsername(); 
			String password = userDetails.getPassword();
			int scope =  userDetails.getScope();
			if(!util.isNullorWhiteSpaces(cUId)) {
				if(scope==0) {
					System.out.println("User is not yet approved by admin to login"); 
					return util.setFailureReponse(false, "User is not yet approved by admin", "user");
				}else {
					if(!pass.equals(password)) {
						
						System.out.println("Password is not matching the records, received pass :"+pass+" DB pass :"+password); 
						return util.setFailureReponse(false, "Password is not matching the records", "user");
					}else {
						User user = new User();
						user.setUsername(userDetails.getUsername());
						user.setScope(userDetails.getScope());
						System.out.println("UID : "+cUId+" , found in the Repository.. "); 
						return 	util.setSucessReponse(true, user);		
					}
					
				}
			} else {
				System.out.println("There is no such UID found in the Repository.. "); 
				return util.setFailureReponse(false, "Uid does not Exits", "user");
			}
		}else{
			System.out.println("There is no such  UID found in the Repository.. ");
			return util.setFailureReponse(false, "Uid does not Exits", "user");
		}

	}
    public String updateUser(User userDetail) {
    	Utilities util = new Utilities();
		if (userRepository.existsById(userDetail.getUsername())) {
			User userDetails = userRepository.findByuname(userDetail.getUsername());
			userDetail.setPassword(userDetails.getPassword());
			userRepository.save(userDetail);
			return util.setSucessReponse(true, "UserName : '"+userDetail.getUsername()+"', updated Successfully");
		} else {
			System.out.println("User ID does not exits  : " + userDetail.getUsername());
			return util.setFailureReponse(false,"User not available in db","user");
		}
	}
	public String deleteUser(String uid) throws RecordNotFoundException
	{
		Utilities util = new Utilities();
		User user = userRepository.findByuname(uid);   
		if (user != null) {
			System.out.println("User is available to delete");
			userRepository.delete(user);
			return util.setSucessReponse(true, "UserName : '"+uid+"', Deleted Successfully");
		}else {
			System.out.println("User not available in db");
			return util.setFailureReponse(false,"User not available in db","user");
		}

	}
	public String createUser(User user) {
		Utilities util = new Utilities();
		try {

			if (userRepository.existsByuname(user.getUsername())) {
				return util.setFailureReponse(false, "User Email Already Exits", "user");
			}else {
				user = userRepository.save(user);
				return util.setSucessReponse(true, user.getUsername());
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


     
 /*   public UserEntity createUser(UserEntity entityCredential) throws RecordNotFoundException
    {
    	
    	Optional<UserEntity> credential = repository.findById(entityCredential.getUsername());
        if(null!=credential && null!=credential.get().getUsername())
        {
        	UserEntity newCredentials = new UserEntity();
        	newCredentials.setUsername(entityCredential.getUsername());
        	newCredentials.setPassword(entityCredential.getPassword());
 
        	newCredentials = repository.save(newCredentials);
        	
        	System.out.println("credential.getLoginUsername() : "+entityCredential.getUsername());
        	System.out.println("----->>>>"+repository.findById(entityCredential.getUsername()).get().getPassword());
            return newCredentials;
        } else {
        	
        	System.out.println("New credential.getLoginUsername() : "+entityCredential.getUsername());
        	//System.out.println("----->>>>"+repository.findOne(entityCredential.getUsername()).getPassword());
        	entityCredential = repository.save(entityCredential);
            return entityCredential;
        }
    }
     
    public void deleteUser(String name) throws RecordNotFoundException
    {
    	Optional<UserEntity> userCredential = repository.findById(name);   
    	if (userCredential != null) {
    		 String uname = userCredential.get().getUsername();
    	        if(uname != null && !uname.equals(""))
    	        {
    	            repository.deleteById(name);
    	        } else {
    	            throw new RecordNotFoundException("No employee record exist for given username : "+name);
    	        }
		}
    	
    }
   */ 
    
}