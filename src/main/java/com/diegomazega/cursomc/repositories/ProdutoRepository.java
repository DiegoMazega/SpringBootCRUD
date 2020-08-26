package com.diegomazega.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.diegomazega.cursomc.domain.Categoria;
import com.diegomazega.cursomc.domain.Produto;

@Repository // habilita um objeto desse tipo a realizar acesso a dados.
//Passar a tabela e o tipo do identificador unico.
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	
	//A forma escrita do metodo esta usando o padrão Spring Data, sendo assim,
	//a utilização da notação "@Query" passa a ser opcional.
	//Uma vez que ela esteja sobre o metodo, o spring irá sobrescrever o JPQL da função
	//pelo que estiver dentro do parametro da notação.
}
