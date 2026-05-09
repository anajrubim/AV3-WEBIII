package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Service
public class ServicoDocumento {

    @Autowired
    private DocumentoRepositorio repositorio;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;

    public List<Documento> listar() {
        List<Documento> lista = repositorio.findAll();
        adicionadorLink.adicionarLink(lista);
        return lista;
    }

    public Documento buscar(long id) {
        Documento doc = repositorio.findById(id).orElse(null);
        if (doc != null) adicionadorLink.adicionarLink(doc);
        return doc;
    }

    public void cadastrar(Documento documento) {
        repositorio.save(documento);
    }

    public void atualizar(Documento atualizacao) {
        Documento doc = repositorio.getById(atualizacao.getId());
        DocumentoAtualizador atualizador = new DocumentoAtualizador();
        atualizador.atualizar(doc, atualizacao);
        repositorio.save(doc);
    }

    public void excluir(long id) {
        repositorio.deleteById(id);
    }
}
