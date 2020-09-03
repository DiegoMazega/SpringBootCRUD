package com.diegomazega.cursomc.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.diegomazega.cursomc.domain.Pedido;
import com.diegomazega.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResources {
	
	@Autowired
	private PedidoService pedidoService;
	
	@GetMapping(value="/all")
	public ResponseEntity<List<Pedido>> getAll(){
		return ResponseEntity.ok(pedidoService.findAll());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> getById(@PathVariable Integer id){
		Pedido pedido = pedidoService.find(id);
		return ResponseEntity.ok(pedido);
	}
	
	@GetMapping
	public ResponseEntity<Page<Pedido>> findPerPage(
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue = "data") String orderBy, 
			@RequestParam(value="direction", defaultValue = "DESC") String direction){
		Page<Pedido> listPedido = pedidoService.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok(listPedido);
	}
	
	
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){
		obj = pedidoService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

}
