package com.diegomazega.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diegomazega.cursomc.domain.Cliente;
import com.diegomazega.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResources {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> getAll(){
		List<Cliente> clienteLista = clienteService.findAll();
		return ResponseEntity.ok(clienteLista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> getById(@PathVariable Integer id){
		Cliente cliente = clienteService.find(id);
		return ResponseEntity.ok(cliente);
	}
}
