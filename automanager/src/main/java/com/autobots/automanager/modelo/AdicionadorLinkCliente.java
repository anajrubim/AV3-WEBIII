package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

    @Override
    public void adicionarLink(List<Cliente> lista) {
        for (Cliente cliente : lista) {
            long id = cliente.getId();
            Link selfLink = WebMvcLinkBuilder
                    .linkTo(ClienteControle.class)
                    .slash(id)
                    .withSelfRel();
            cliente.add(selfLink);
        }
    }

    @Override
    public void adicionarLink(Cliente objeto) {
        long id = objeto.getId();
        Link selfLink = WebMvcLinkBuilder
                .linkTo(ClienteControle.class)
                .slash(id)
                .withSelfRel();
        Link listaLink = WebMvcLinkBuilder
                .linkTo(ClienteControle.class)
                .withRel("clientes");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
