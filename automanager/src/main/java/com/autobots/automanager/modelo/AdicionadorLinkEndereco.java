package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.entidades.Endereco;

@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco> {

    @Override
    public void adicionarLink(List<Endereco> lista) {
        for (Endereco endereco : lista) {
            long id = endereco.getId();
            Link selfLink = WebMvcLinkBuilder
                    .linkTo(EnderecoControle.class)
                    .slash(id)
                    .withSelfRel();
            endereco.add(selfLink);
        }
    }

    @Override
    public void adicionarLink(Endereco objeto) {
        long id = objeto.getId();
        Link selfLink = WebMvcLinkBuilder
                .linkTo(EnderecoControle.class)
                .slash(id)
                .withSelfRel();
        Link listaLink = WebMvcLinkBuilder
                .linkTo(EnderecoControle.class)
                .withRel("enderecos");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
