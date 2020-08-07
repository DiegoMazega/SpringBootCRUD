package com.diegomazega.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diegomazega.cursomc.domain.Cliente;
import com.diegomazega.cursomc.dto.ClienteDTO;
import com.diegomazega.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResources {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> getAll(){
		List<Cliente> clienteLista = clienteService.findAll();
		List<ClienteDTO> clienteDtoLista = clienteLista.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(clienteDtoLista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> getById(@PathVariable Integer id){
		Cliente cliente = clienteService.find(id);
		return ResponseEntity.ok(cliente);
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<ClienteDTO>> findPerPage(
			@RequestParam(name="page", defaultValue = "0") Integer page,
			@RequestParam(name="linesPerPage", defaultValue = "1")Integer linesPerPage,
			@RequestParam(name="orderBy", defaultValue = "nome")String orderBy,
			@RequestParam(name="direction", defaultValue = "ASC")String direction) {
		Page<Cliente> clienteLista = clienteService.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> clienteDto = clienteLista.map(obj -> new ClienteDTO(obj));
		return ResponseEntity.ok(clienteDto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDto, @PathVariable Integer id){
		Cliente obj = clienteService.fromDto(clienteDto);
		obj.setId(id);
		obj = clienteService.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@Valid @PathVariable Integer id){
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
