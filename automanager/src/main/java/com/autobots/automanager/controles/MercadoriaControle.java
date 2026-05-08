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

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

    @Autowired
    private RepositorioMercadoria repositorioMercadoria;

    private EntityModel<Mercadoria> toModel(Mercadoria mercadoria) {
        EntityModel<Mercadoria> model = EntityModel.of(mercadoria);
        model.add(linkTo(methodOn(MercadoriaControle.class).obterMercadoria(mercadoria.getId())).withSelfRel());
        model.add(linkTo(methodOn(MercadoriaControle.class).obterMercadorias()).withRel("mercadorias"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Mercadoria>>> obterMercadorias() {
        List<EntityModel<Mercadoria>> mercadorias = repositorioMercadoria.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Mercadoria>> collection = CollectionModel.of(mercadorias,
                linkTo(methodOn(MercadoriaControle.class).obterMercadorias()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mercadoria>> obterMercadoria(@PathVariable Long id) {
        Optional<Mercadoria> mercadoria = repositorioMercadoria.findById(id);
        if (mercadoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(mercadoria.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Mercadoria>> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        mercadoria.setId(null);
        mercadoria.setCadastro(new Date());
        Mercadoria salva = repositorioMercadoria.save(mercadoria);
        EntityModel<Mercadoria> model = toModel(salva);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Mercadoria>> atualizarMercadoria(
            @PathVariable Long id, @RequestBody Mercadoria dados) {
        Optional<Mercadoria> optMercadoria = repositorioMercadoria.findById(id);
        if (optMercadoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Mercadoria mercadoria = optMercadoria.get();
        if (dados.getNome() != null) mercadoria.setNome(dados.getNome());
        if (dados.getDescricao() != null) mercadoria.setDescricao(dados.getDescricao());
        if (dados.getValor() > 0) mercadoria.setValor(dados.getValor());
        if (dados.getQuantidade() > 0) mercadoria.setQuantidade(dados.getQuantidade());
        if (dados.getValidade() != null) mercadoria.setValidade(dados.getValidade());
        if (dados.getFabricao() != null) mercadoria.setFabricao(dados.getFabricao());
        Mercadoria salva = repositorioMercadoria.save(mercadoria);
        return ResponseEntity.ok(toModel(salva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMercadoria(@PathVariable Long id) {
        if (!repositorioMercadoria.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioMercadoria.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
