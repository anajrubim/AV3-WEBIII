package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private EntityModel<Usuario> toModel(Usuario usuario) {
        EntityModel<Usuario> model = EntityModel.of(usuario);
        model.add(linkTo(methodOn(UsuarioControle.class).obterUsuario(usuario.getId())).withSelfRel());
        model.add(linkTo(methodOn(UsuarioControle.class).obterUsuarios()).withRel("usuarios"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obterUsuarios() {
        List<EntityModel<Usuario>> usuarios = repositorioUsuario.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Usuario>> collection = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioControle.class).obterUsuarios()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obterUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = repositorioUsuario.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(usuario.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> cadastrarUsuario(@RequestBody Usuario usuario) {
        usuario.setId(null);
        Usuario salvo = repositorioUsuario.save(usuario);
        EntityModel<Usuario> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario dados) {
        Optional<Usuario> optUsuario = repositorioUsuario.findById(id);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = optUsuario.get();
        if (dados.getNome() != null) usuario.setNome(dados.getNome());
        if (dados.getNomeSocial() != null) usuario.setNomeSocial(dados.getNomeSocial());
        if (dados.getPerfis() != null && !dados.getPerfis().isEmpty()) usuario.setPerfis(dados.getPerfis());
        if (dados.getEndereco() != null) usuario.setEndereco(dados.getEndereco());
        Usuario salvo = repositorioUsuario.save(usuario);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (!repositorioUsuario.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioUsuario.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
