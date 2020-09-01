package com.diegomazega.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.diegomazega.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
