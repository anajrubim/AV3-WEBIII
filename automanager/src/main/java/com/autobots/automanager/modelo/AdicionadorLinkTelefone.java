package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Telefone;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

    @Override
    public void adicionarLink(List<Telefone> lista) {
        for (Telefone telefone : lista) {
            long id = telefone.getId();
            Link selfLink = WebMvcLinkBuilder
                    .linkTo(TelefoneControle.class)
                    .slash(id)
                    .withSelfRel();
            telefone.add(selfLink);
        }
    }

    @Override
    public void adicionarLink(Telefone objeto) {
        long id = objeto.getId();
        Link selfLink = WebMvcLinkBuilder
                .linkTo(TelefoneControle.class)
                .slash(id)
                .withSelfRel();
        Link listaLink = WebMvcLinkBuilder
                .linkTo(TelefoneControle.class)
                .withRel("telefones");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
