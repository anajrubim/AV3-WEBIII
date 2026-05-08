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

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private RepositorioServico repositorioServico;

    private EntityModel<Servico> toModel(Servico servico) {
        EntityModel<Servico> model = EntityModel.of(servico);
        model.add(linkTo(methodOn(ServicoControle.class).obterServico(servico.getId())).withSelfRel());
        model.add(linkTo(methodOn(ServicoControle.class).obterServicos()).withRel("servicos"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Servico>>> obterServicos() {
        List<EntityModel<Servico>> servicos = repositorioServico.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Servico>> collection = CollectionModel.of(servicos,
                linkTo(methodOn(ServicoControle.class).obterServicos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Servico>> obterServico(@PathVariable Long id) {
        Optional<Servico> servico = repositorioServico.findById(id);
        if (servico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(servico.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Servico>> cadastrarServico(@RequestBody Servico servico) {
        servico.setId(null);
        Servico salvo = repositorioServico.save(servico);
        EntityModel<Servico> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Servico>> atualizarServico(
            @PathVariable Long id, @RequestBody Servico dados) {
        Optional<Servico> optServico = repositorioServico.findById(id);
        if (optServico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Servico servico = optServico.get();
        if (dados.getNome() != null) servico.setNome(dados.getNome());
        if (dados.getDescricao() != null) servico.setDescricao(dados.getDescricao());
        if (dados.getValor() > 0) servico.setValor(dados.getValor());
        Servico salvo = repositorioServico.save(servico);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        if (!repositorioServico.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioServico.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
