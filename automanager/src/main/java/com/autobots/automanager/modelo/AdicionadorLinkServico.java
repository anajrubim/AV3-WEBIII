package com.autobots.automanager.modelo;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entidades.Servico;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {
    @Override
    public void adicionarLink(List<Servico> lista) {
        for (Servico servico : lista) {
            Link selfLink = WebMvcLinkBuilder.linkTo(ServicoControle.class).slash(servico.getId()).withSelfRel();
            servico.add(selfLink);
        }
    }
    @Override
    public void adicionarLink(Servico objeto) {
        Link selfLink = WebMvcLinkBuilder.linkTo(ServicoControle.class).slash(objeto.getId()).withSelfRel();
        Link listaLink = WebMvcLinkBuilder.linkTo(ServicoControle.class).withRel("servicos");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
