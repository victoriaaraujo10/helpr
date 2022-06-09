package com.api.helpr.services;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.helpr.domain.Cliente;
import com.api.helpr.domain.Pessoa;
import com.api.helpr.domain.dtos.ClienteDTO;
import com.api.helpr.repositories.ClienteRepository;
import com.api.helpr.repositories.PessoaRepository;
import com.api.helpr.services.exceptions.DataIntegrityViolationException;
import com.api.helpr.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	// Método de busca por um ID no banco:
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não foi encontrado: " + id));
	}

	// Método de busca por n ID's no banco:
	public List<Cliente> findAllClientes() {
		return repository.findAll();
	}

	// Método de criação - novo cliente:
	public Cliente create(ClienteDTO objDto) {
		objDto.setId(null);
		validaCpfEEmail(objDto);
		Cliente newObj = new Cliente(objDto);
		return repository.save(newObj);
	}

	// Método que valida: CPF & e-mail:
	private void validaCpfEEmail(ClienteDTO objDto) {

		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDto.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDto.getId()) {
			throw new DataIntegrityViolationException("Erro: CPF já cadastrado no sistema");
		}

		obj = pessoaRepository.findByEmail(objDto.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDto.getId()) {
			throw new DataIntegrityViolationException("Erro: e-mail já cadastrado no sistema");
		}
	}

	// Método que modifica os clientes existentes:
	public Cliente update(Integer id, ClienteDTO objDto) {
		objDto.setId(id);
		Cliente oldObj = findById(id);
		validaCpfEEmail(objDto);
		oldObj = new Cliente(objDto);
		return repository.save(oldObj);
	}

	// Método que deleta o ID do cliente:
	public void delete(Integer id) {
		Cliente obj = findById(id);
		if(obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente: " + id
					+ " tem os seguintes chamados processados em sistema: " + obj.getChamados().size());
		}
		repository.deleteById(id);
	}

}
