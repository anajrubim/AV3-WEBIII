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

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.repositorios.RepositorioEmail;

@RestController
@RequestMapping("/email")
public class EmailControle {

    @Autowired
    private RepositorioEmail repositorioEmail;

    private EntityModel<Email> toModel(Email email) {
        EntityModel<Email> model = EntityModel.of(email);
        model.add(linkTo(methodOn(EmailControle.class).obterEmail(email.getId())).withSelfRel());
        model.add(linkTo(methodOn(EmailControle.class).obterEmails()).withRel("emails"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Email>>> obterEmails() {
        List<EntityModel<Email>> emails = repositorioEmail.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Email>> collection = CollectionModel.of(emails,
                linkTo(methodOn(EmailControle.class).obterEmails()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Email>> obterEmail(@PathVariable Long id) {
        Optional<Email> email = repositorioEmail.findById(id);
        if (email.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(email.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Email>> cadastrarEmail(@RequestBody Email email) {
        email.setId(null);
        Email salvo = repositorioEmail.save(email);
        EntityModel<Email> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Email>> atualizarEmail(
            @PathVariable Long id, @RequestBody Email dados) {
        Optional<Email> optEmail = repositorioEmail.findById(id);
        if (optEmail.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Email email = optEmail.get();
        if (dados.getEndereco() != null) email.setEndereco(dados.getEndereco());
        Email salvo = repositorioEmail.save(email);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmail(@PathVariable Long id) {
        if (!repositorioEmail.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioEmail.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
