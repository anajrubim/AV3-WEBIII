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

import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.repositorios.RepositorioEndereco;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private RepositorioEndereco repositorioEndereco;

    private EntityModel<Endereco> toModel(Endereco endereco) {
        EntityModel<Endereco> model = EntityModel.of(endereco);
        model.add(linkTo(methodOn(EnderecoControle.class).obterEndereco(endereco.getId())).withSelfRel());
        model.add(linkTo(methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Endereco>>> obterEnderecos() {
        List<EntityModel<Endereco>> enderecos = repositorioEndereco.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Endereco>> collection = CollectionModel.of(enderecos,
                linkTo(methodOn(EnderecoControle.class).obterEnderecos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Endereco>> obterEndereco(@PathVariable Long id) {
        Optional<Endereco> endereco = repositorioEndereco.findById(id);
        if (endereco.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(endereco.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Endereco>> cadastrarEndereco(@RequestBody Endereco endereco) {
        endereco.setId(null);
        Endereco salvo = repositorioEndereco.save(endereco);
        EntityModel<Endereco> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Endereco>> atualizarEndereco(
            @PathVariable Long id, @RequestBody Endereco dados) {
        Optional<Endereco> optEndereco = repositorioEndereco.findById(id);
        if (optEndereco.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Endereco endereco = optEndereco.get();
        if (dados.getEstado() != null) endereco.setEstado(dados.getEstado());
        if (dados.getCidade() != null) endereco.setCidade(dados.getCidade());
        if (dados.getBairro() != null) endereco.setBairro(dados.getBairro());
        if (dados.getRua() != null) endereco.setRua(dados.getRua());
        if (dados.getNumero() != null) endereco.setNumero(dados.getNumero());
        if (dados.getCodigoPostal() != null) endereco.setCodigoPostal(dados.getCodigoPostal());
        if (dados.getInformacoesAdicionais() != null)
            endereco.setInformacoesAdicionais(dados.getInformacoesAdicionais());
        Endereco salvo = repositorioEndereco.save(endereco);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id) {
        if (!repositorioEndereco.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioEndereco.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
