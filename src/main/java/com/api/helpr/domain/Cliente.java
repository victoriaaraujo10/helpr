package com.api.helpr.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.api.helpr.domain.dtos.ClienteDTO;
import com.api.helpr.domain.dtos.TecnicoDTO;
import com.api.helpr.domain.enums.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cliente extends Pessoa {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Chamado> chamados = new ArrayList<>();

	public Cliente() {
		super();
		addPerfils(Perfil.CLIENTE);
	}

	public Cliente(Integer id, String nome, String cpf, String email, String senha) {
		super(id, nome, cpf, email, senha);
		addPerfils(Perfil.CLIENTE);
	}

	public Cliente(ClienteDTO objDto) {
		super();
		this.id = objDto.getId();
		this.nome = objDto.getNome();
		this.cpf = objDto.getCpf();
		this.email = objDto.getEmail();
		this.senha = objDto.getSenha();
		this.perfils = objDto.getPerfils().stream().map(x -> x.getCodigo()).collect(Collectors.toSet());
    }

    public List<Chamado> getChamados() {
		return chamados;
	}

	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}
}
