package com.diegomazega.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diegomazega.cursomc.domain.Pedido;
import com.diegomazega.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResources {
	
	@Autowired
	private PedidoService pedidoService;
	
	@GetMapping
	public ResponseEntity<List<Pedido>> getAll(){
		return ResponseEntity.ok(pedidoService.findAll());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> getById(@PathVariable Integer id){
		Pedido pedido = pedidoService.find(id);
		return ResponseEntity.ok(pedido);
	}

}
