package com.diegomazega.cursomc.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diegomazega.cursomc.domain.Cliente;
import com.diegomazega.cursomc.domain.ItemPedido;
import com.diegomazega.cursomc.domain.PagamentoComBoleto;
import com.diegomazega.cursomc.domain.Pedido;
import com.diegomazega.cursomc.domain.enums.EstadoPagamento;
import com.diegomazega.cursomc.domain.enums.Perfil;
import com.diegomazega.cursomc.repositories.ItemPedidoRepository;
import com.diegomazega.cursomc.repositories.PagamentoRepository;
import com.diegomazega.cursomc.repositories.PedidoRepository;
import com.diegomazega.cursomc.security.UserSS;
import com.diegomazega.cursomc.services.exceptions.AuthorizationException;
import com.diegomazega.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException(
				"Objeto não encontrado! ID: "+id+", tipo: "+Pedido.class.getName()));
	}
	
	public List<Pedido> findAll(){
		UserSS userSS = UserService.authenticated();
		if(userSS == null || !userSS.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso Negado");
		}
		return pedidoRepository.findAll();
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setData(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getData());
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS userSS = UserService.authenticated();
		if(userSS == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(userSS.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}
	
}
