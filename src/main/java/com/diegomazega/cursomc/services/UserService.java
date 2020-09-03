package com.diegomazega.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.diegomazega.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
			//retorna o usuario logado casa haja um.
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
		}catch(Exception event) {
			return null; 
		}
	}
}
