package com.autobots.automanager.modelo;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {
    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            Link selfLink = WebMvcLinkBuilder.linkTo(MercadoriaControle.class).slash(mercadoria.getId()).withSelfRel();
            mercadoria.add(selfLink);
        }
    }
    @Override
    public void adicionarLink(Mercadoria objeto) {
        Link selfLink = WebMvcLinkBuilder.linkTo(MercadoriaControle.class).slash(objeto.getId()).withSelfRel();
        Link listaLink = WebMvcLinkBuilder.linkTo(MercadoriaControle.class).withRel("mercadorias");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
