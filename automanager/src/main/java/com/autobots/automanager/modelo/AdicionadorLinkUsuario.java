package com.autobots.automanager.modelo;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Usuario;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {
    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            Link selfLink = WebMvcLinkBuilder.linkTo(UsuarioControle.class).slash(usuario.getId()).withSelfRel();
            usuario.add(selfLink);
        }
    }
    @Override
    public void adicionarLink(Usuario objeto) {
        Link selfLink = WebMvcLinkBuilder.linkTo(UsuarioControle.class).slash(objeto.getId()).withSelfRel();
        Link listaLink = WebMvcLinkBuilder.linkTo(UsuarioControle.class).withRel("usuarios");
        objeto.add(selfLink);
        objeto.add(listaLink);
    }
}
