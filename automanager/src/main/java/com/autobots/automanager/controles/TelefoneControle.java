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

import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.repositorios.RepositorioTelefone;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private RepositorioTelefone repositorioTelefone;

    private EntityModel<Telefone> toModel(Telefone telefone) {
        EntityModel<Telefone> model = EntityModel.of(telefone);
        model.add(linkTo(methodOn(TelefoneControle.class).obterTelefone(telefone.getId())).withSelfRel());
        model.add(linkTo(methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Telefone>>> obterTelefones() {
        List<EntityModel<Telefone>> telefones = repositorioTelefone.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Telefone>> collection = CollectionModel.of(telefones,
                linkTo(methodOn(TelefoneControle.class).obterTelefones()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Telefone>> obterTelefone(@PathVariable Long id) {
        Optional<Telefone> telefone = repositorioTelefone.findById(id);
        if (telefone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(telefone.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Telefone>> cadastrarTelefone(@RequestBody Telefone telefone) {
        telefone.setId(null);
        Telefone salvo = repositorioTelefone.save(telefone);
        EntityModel<Telefone> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Telefone>> atualizarTelefone(
            @PathVariable Long id, @RequestBody Telefone dados) {
        Optional<Telefone> optTelefone = repositorioTelefone.findById(id);
        if (optTelefone.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Telefone telefone = optTelefone.get();
        if (dados.getDdd() != null) telefone.setDdd(dados.getDdd());
        if (dados.getNumero() != null) telefone.setNumero(dados.getNumero());
        Telefone salvo = repositorioTelefone.save(telefone);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTelefone(@PathVariable Long id) {
        if (!repositorioTelefone.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioTelefone.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
