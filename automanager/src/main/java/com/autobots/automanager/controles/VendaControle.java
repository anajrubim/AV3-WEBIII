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

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private RepositorioVenda repositorioVenda;

    private EntityModel<Venda> toModel(Venda venda) {
        EntityModel<Venda> model = EntityModel.of(venda);
        model.add(linkTo(methodOn(VendaControle.class).obterVenda(venda.getId())).withSelfRel());
        model.add(linkTo(methodOn(VendaControle.class).obterVendas()).withRel("vendas"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Venda>>> obterVendas() {
        List<EntityModel<Venda>> vendas = repositorioVenda.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Venda>> collection = CollectionModel.of(vendas,
                linkTo(methodOn(VendaControle.class).obterVendas()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venda>> obterVenda(@PathVariable Long id) {
        Optional<Venda> venda = repositorioVenda.findById(id);
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(venda.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Venda>> cadastrarVenda(@RequestBody Venda venda) {
        venda.setId(null);
        venda.setCadastro(new Date());
        Venda salva = repositorioVenda.save(venda);
        EntityModel<Venda> model = toModel(salva);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Venda>> atualizarVenda(
            @PathVariable Long id, @RequestBody Venda dados) {
        Optional<Venda> optVenda = repositorioVenda.findById(id);
        if (optVenda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        if (dados.getIdentificacao() != null) venda.setIdentificacao(dados.getIdentificacao());
        if (dados.getCliente() != null) venda.setCliente(dados.getCliente());
        if (dados.getFuncionario() != null) venda.setFuncionario(dados.getFuncionario());
        if (dados.getVeiculo() != null) venda.setVeiculo(dados.getVeiculo());
        if (dados.getMercadorias() != null && !dados.getMercadorias().isEmpty())
            venda.setMercadorias(dados.getMercadorias());
        if (dados.getServicos() != null && !dados.getServicos().isEmpty())
            venda.setServicos(dados.getServicos());
        Venda salva = repositorioVenda.save(venda);
        return ResponseEntity.ok(toModel(salva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable Long id) {
        if (!repositorioVenda.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioVenda.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
