package com.diegomazega.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diegomazega.cursomc.dto.EmailDTO;
import com.diegomazega.cursomc.security.JWTUtil;
import com.diegomazega.cursomc.security.UserSS;
import com.diegomazega.cursomc.services.AuthService;
import com.diegomazega.cursomc.services.UserService;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService authService;

	@PostMapping(value="/refresh_token")
	public ResponseEntity<Void> resreshToken(HttpServletResponse response){
		UserSS userSS = UserService.authenticated();
		String token = jwtUtil.generateToken(userSS.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(value="/forgot")
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO){
		authService.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
}
