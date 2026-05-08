package com.autobots.automanager.controles;

import java.util.Date;
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

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioCredencial;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/credencial")
public class CredencialControle {

    @Autowired
    private RepositorioCredencial repositorioCredencial;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private EntityModel<Credencial> toModel(Credencial credencial) {
        EntityModel<Credencial> model = EntityModel.of(credencial);
        model.add(linkTo(methodOn(CredencialControle.class).obterCredencial(credencial.getId())).withSelfRel());
        model.add(linkTo(methodOn(CredencialControle.class).obterCredenciais()).withRel("credenciais"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Credencial>>> obterCredenciais() {
        List<EntityModel<Credencial>> credenciais = repositorioCredencial.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Credencial>> collection = CollectionModel.of(credenciais,
                linkTo(methodOn(CredencialControle.class).obterCredenciais()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Credencial>> obterCredencial(@PathVariable Long id) {
        Optional<Credencial> credencial = repositorioCredencial.findById(id);
        if (credencial.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(credencial.get()));
    }

    // Adicionar credencial a um usuário
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<EntityModel<Credencial>> adicionarCredencial(
            @PathVariable Long idUsuario, @RequestBody CredencialUsuarioSenha credencial) {
        Optional<Usuario> optUsuario = repositorioUsuario.findById(idUsuario);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        credencial.setId(null);
        credencial.setCriacao(new Date());
        credencial.setInativo(false);
        Credencial salva = repositorioCredencial.save(credencial);
        Usuario usuario = optUsuario.get();
        usuario.getCredenciais().add(salva);
        repositorioUsuario.save(usuario);
        EntityModel<Credencial> model = toModel(salva);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Credencial>> atualizarCredencial(
            @PathVariable Long id, @RequestBody CredencialUsuarioSenha dados) {
        Optional<Credencial> optCredencial = repositorioCredencial.findById(id);
        if (optCredencial.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Credencial credencial = optCredencial.get();
        if (credencial instanceof CredencialUsuarioSenha cus) {
            if (dados.getNomeUsuario() != null) cus.setNomeUsuario(dados.getNomeUsuario());
            if (dados.getSenha() != null) cus.setSenha(dados.getSenha());
            cus.setUltimoAcesso(new Date());
        }
        Credencial salva = repositorioCredencial.save(credencial);
        return ResponseEntity.ok(toModel(salva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCredencial(@PathVariable Long id) {
        if (!repositorioCredencial.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioCredencial.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
