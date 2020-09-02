package com.diegomazega.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.diegomazega.cursomc.domain.Categoria;
import com.diegomazega.cursomc.dto.CategoriaDTO;
import com.diegomazega.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias") //nome do endpoint
public class CategoriaResources {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> getAll(){
		List<Categoria> catList = categoriaService.findAll();
		List<CategoriaDTO> catDtoList = catList.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(catDtoList);
	}
	
	@GetMapping(value="/{id}") //Variavel que vem da url. 
	public ResponseEntity<Categoria> GetById(@PathVariable Integer id) {
		Categoria categoria = categoriaService.find(id);
		return ResponseEntity.ok(categoria);
	}
	
	//Requisição com paginação.
	//A classe Page já é java 8 Com. ele nao precisa do ".stream()" ou do ".collect()" para percorrer toda a lista
	//como foi feito na função getAll()
	@GetMapping(value="/page")
	public ResponseEntity<Page<CategoriaDTO>> findPerPage(
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value="direction", defaultValue = "ASC") String direction){
		Page<Categoria> listCategoria = categoriaService.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listCategoriaDTO = listCategoria.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok(listCategoriaDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = categoriaService.fromDto(objDto);
		obj = categoriaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto ,@PathVariable Integer id ){
		Categoria obj = categoriaService.fromDto(objDto);
		obj.setId(id);
		obj  = categoriaService.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		categoriaService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
