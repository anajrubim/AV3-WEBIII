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
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private EntityModel<Veiculo> toModel(Veiculo veiculo) {
        EntityModel<Veiculo> model = EntityModel.of(veiculo);
        model.add(linkTo(methodOn(VeiculoControle.class).obterVeiculo(veiculo.getId())).withSelfRel());
        model.add(linkTo(methodOn(VeiculoControle.class).obterVeiculos()).withRel("veiculos"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Veiculo>>> obterVeiculos() {
        List<EntityModel<Veiculo>> veiculos = repositorioVeiculo.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Veiculo>> collection = CollectionModel.of(veiculos,
                linkTo(methodOn(VeiculoControle.class).obterVeiculos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Veiculo>> obterVeiculo(@PathVariable Long id) {
        Optional<Veiculo> veiculo = repositorioVeiculo.findById(id);
        if (veiculo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(veiculo.get()));
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<EntityModel<Veiculo>> cadastrarVeiculo(
            @PathVariable Long idUsuario, @RequestBody Veiculo veiculo) {
        Optional<Usuario> optUsuario = repositorioUsuario.findById(idUsuario);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        veiculo.setId(null);
        veiculo.setProprietario(optUsuario.get());
        Veiculo salvo = repositorioVeiculo.save(veiculo);
        Usuario usuario = optUsuario.get();
        usuario.getVeiculos().add(salvo);
        repositorioUsuario.save(usuario);
        EntityModel<Veiculo> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Veiculo>> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo dados) {
        Optional<Veiculo> optVeiculo = repositorioVeiculo.findById(id);
        if (optVeiculo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Veiculo veiculo = optVeiculo.get();
        if (dados.getTipo() != null) veiculo.setTipo(dados.getTipo());
        if (dados.getModelo() != null) veiculo.setModelo(dados.getModelo());
        if (dados.getPlaca() != null) veiculo.setPlaca(dados.getPlaca());
        Veiculo salvo = repositorioVeiculo.save(veiculo);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        if (!repositorioVeiculo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioVeiculo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
