package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@Service
public class ServicoEndereco {

    @Autowired
    private EnderecoRepositorio repositorio;

    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;

    public List<Endereco> listar() {
        List<Endereco> lista = repositorio.findAll();
        adicionadorLink.adicionarLink(lista);
        return lista;
    }

    public Endereco buscar(long id) {
        Endereco endereco = repositorio.findById(id).orElse(null);
        if (endereco != null) adicionadorLink.adicionarLink(endereco);
        return endereco;
    }

    public void cadastrar(Endereco endereco) {
        repositorio.save(endereco);
    }

    public void atualizar(Endereco atualizacao) {
        Endereco endereco = repositorio.getById(atualizacao.getId());
        EnderecoAtualizador atualizador = new EnderecoAtualizador();
        atualizador.atualizar(endereco, atualizacao);
        repositorio.save(endereco);
    }

    public void excluir(long id) {
        repositorio.deleteById(id);
    }
}
