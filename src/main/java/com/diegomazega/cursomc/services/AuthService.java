package com.diegomazega.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.diegomazega.cursomc.domain.Cliente;
import com.diegomazega.cursomc.repositories.ClienteRepository;
import com.diegomazega.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	private Random rand = new Random();
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		String newPass = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPass));
		clienteRepository.save(cliente);
		emailService.sendNewPassword(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i = 0; i < vet.length; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if(opt == 0){
			return (char) (rand.nextInt(10) + 48); 
		// gera digito
		} else if(opt == 1) {
			//gera letra maiúscula
			return (char) (rand.nextInt(26) + 65);
		}else {
			//gera letra minuscula 
			return (char) (rand.nextInt(10) + 97);
		}
	}
}
