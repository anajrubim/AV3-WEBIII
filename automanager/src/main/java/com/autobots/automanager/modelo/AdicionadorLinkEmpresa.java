package com.autobots.automanager.modelo;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {
    @Override
    public void adicionarLink(List<Empresa> lista) {
        for (Empresa empresa : lista) {
            Link selfLink = WebMvcLinkBuilder.linkTo(EmpresaControle.class).slash(empresa.getId()).withSelfRel();
            empresa.add(selfLink);
        }
    }
    @Override
    public void adicionarLink(Empresa objeto) {
        Link selfLink = WebMvcLinkBuilder.linkTo(EmpresaControle.class).slash(objeto.getId()).withSelfRel();
        Link listaLink = WebMvcLinkBuilder.linkTo(EmpresaControle.class).withRel("empresas");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
