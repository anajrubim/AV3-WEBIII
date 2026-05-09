package com.autobots.automanager.servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
public class ServicoVenda {
    @Autowired
    private VendaRepositorio repositorio;
    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    public List<Venda> listar() {
        List<Venda> lista = repositorio.findAll();
        adicionadorLink.adicionarLink(lista);
        return lista;
    }

    public Venda buscar(long id) {
        Venda venda = repositorio.findById(id).orElse(null);
        if (venda != null) adicionadorLink.adicionarLink(venda);
        return venda;
    }

    public void cadastrar(Venda venda) {
        repositorio.save(venda);
    }

    public void atualizar(Venda atualizacao) {
        Venda venda = repositorio.findById(atualizacao.getId()).orElseThrow();
        if (atualizacao.getIdentificacao() != null && !atualizacao.getIdentificacao().isBlank())
            venda.setIdentificacao(atualizacao.getIdentificacao());
        if (atualizacao.getCadastro() != null)
            venda.setCadastro(atualizacao.getCadastro());
        repositorio.save(venda);
    }

    public void excluir(long id) {
        repositorio.deleteById(id);
    }
}
