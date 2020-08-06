package com.diegomazega.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.diegomazega.cursomc.domain.Categoria;
import com.diegomazega.cursomc.repositories.CategoriaRepository;
import com.diegomazega.cursomc.services.exceptions.DataIntegrityException;
import com.diegomazega.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id){
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+", tipo: "+Categoria.class.getName()));
	}
	
	public List<Categoria> findAll(){
		return repo.findAll();
		}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
		repo.deleteById(id);
		} catch(DataIntegrityViolationException event) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possuí produtos");
		}
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		//Paginação. Numero de paginas, linhas por pagina, direção de amostra dos vaores, qual ordenação quer usar
		//Fazer o cast do tipo String pro tipo Direction
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
}
