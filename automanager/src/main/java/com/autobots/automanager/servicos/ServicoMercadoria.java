package com.autobots.automanager.servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.AdicionadorLinkMercadoria;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@Service
public class ServicoMercadoria {
    @Autowired
    private MercadoriaRepositorio repositorio;
    @Autowired
    private AdicionadorLinkMercadoria adicionadorLink;

    public List<Mercadoria> listar() {
        List<Mercadoria> lista = repositorio.findAll();
        adicionadorLink.adicionarLink(lista);
        return lista;
    }

    public Mercadoria buscar(long id) {
        Mercadoria mercadoria = repositorio.findById(id).orElse(null);
        if (mercadoria != null) adicionadorLink.adicionarLink(mercadoria);
        return mercadoria;
    }

    public void cadastrar(Mercadoria mercadoria) {
        repositorio.save(mercadoria);
    }

    public void atualizar(Mercadoria atualizacao) {
        Mercadoria mercadoria = repositorio.findById(atualizacao.getId()).orElseThrow();
        if (atualizacao.getNome() != null && !atualizacao.getNome().isBlank())
            mercadoria.setNome(atualizacao.getNome());
        if (atualizacao.getDescricao() != null)
            mercadoria.setDescricao(atualizacao.getDescricao());
        if (atualizacao.getValor() > 0)
            mercadoria.setValor(atualizacao.getValor());
        if (atualizacao.getQuantidade() > 0)
            mercadoria.setQuantidade(atualizacao.getQuantidade());
        if (atualizacao.getValidade() != null)
            mercadoria.setValidade(atualizacao.getValidade());
        repositorio.save(mercadoria);
    }

    public void excluir(long id) {
        repositorio.deleteById(id);
    }
}
