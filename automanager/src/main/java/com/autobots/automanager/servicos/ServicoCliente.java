package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@Service
public class ServicoCliente {

    @Autowired
    private ClienteRepositorio repositorio;

    @Autowired
    private ClienteSelecionador selecionador;

    @Autowired
    private AdicionadorLinkCliente adicionadorLink;

    public Cliente obterCliente(long id) {
        List<Cliente> clientes = repositorio.findAll();
        Cliente cliente = selecionador.selecionar(clientes, id);
        if (cliente != null) {
            adicionadorLink.adicionarLink(cliente); 
        }

        return cliente;
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = repositorio.findAll();

        adicionadorLink.adicionarLink(clientes); 

        return clientes;
    }

    public void cadastrar(Cliente cliente) {
        repositorio.save(cliente);
    }

    public void atualizar(Cliente atualizacao) {
        Cliente cliente = repositorio.getById(atualizacao.getId());
        ClienteAtualizador atualizador = new ClienteAtualizador();
        atualizador.atualizar(cliente, atualizacao);
        repositorio.save(cliente);
    }

    public void excluir(long id) {
        Cliente cliente = repositorio.getById(id);
        repositorio.delete(cliente);
    }
}
