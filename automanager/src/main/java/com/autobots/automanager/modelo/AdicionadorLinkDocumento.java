package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.entidades.Documento;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

    @Override
    public void adicionarLink(List<Documento> lista) {
        for (Documento documento : lista) {
            long id = documento.getId();
            Link selfLink = WebMvcLinkBuilder
                    .linkTo(DocumentoControle.class)
                    .slash(id)
                    .withSelfRel();
            documento.add(selfLink);
        }
    }

    @Override
    public void adicionarLink(Documento objeto) {
        long id = objeto.getId();
        Link selfLink = WebMvcLinkBuilder
                .linkTo(DocumentoControle.class)
                .slash(id)
                .withSelfRel();
        Link listaLink = WebMvcLinkBuilder
                .linkTo(DocumentoControle.class)
                .withRel("documentos");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
