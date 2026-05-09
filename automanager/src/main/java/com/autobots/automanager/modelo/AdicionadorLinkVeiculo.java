package com.autobots.automanager.modelo;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entidades.Veiculo;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {
    @Override
    public void adicionarLink(List<Veiculo> lista) {
        for (Veiculo veiculo : lista) {
            Link selfLink = WebMvcLinkBuilder.linkTo(VeiculoControle.class).slash(veiculo.getId()).withSelfRel();
            veiculo.add(selfLink);
        }
    }
    @Override
    public void adicionarLink(Veiculo objeto) {
        Link selfLink = WebMvcLinkBuilder.linkTo(VeiculoControle.class).slash(objeto.getId()).withSelfRel();
        Link listaLink = WebMvcLinkBuilder.linkTo(VeiculoControle.class).withRel("veiculos");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
