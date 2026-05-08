package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private EntityModel<Empresa> toModel(Empresa empresa) {
        EntityModel<Empresa> model = EntityModel.of(empresa);
        model.add(linkTo(methodOn(EmpresaControle.class).obterEmpresa(empresa.getId())).withSelfRel());
        model.add(linkTo(methodOn(EmpresaControle.class).obterEmpresas()).withRel("empresas"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Empresa>>> obterEmpresas() {
        List<EntityModel<Empresa>> empresas = repositorioEmpresa.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Empresa>> collection = CollectionModel.of(empresas,
                linkTo(methodOn(EmpresaControle.class).obterEmpresas()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Empresa>> obterEmpresa(@PathVariable Long id) {
        Optional<Empresa> empresa = repositorioEmpresa.findById(id);
        if (empresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(empresa.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Empresa>> cadastrarEmpresa(@RequestBody Empresa empresa) {
        empresa.setId(null);
        empresa.setCadastro(new Date());
        Empresa salva = repositorioEmpresa.save(empresa);
        EntityModel<Empresa> model = toModel(salva);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Empresa>> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa dados) {
        Optional<Empresa> optEmpresa = repositorioEmpresa.findById(id);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = optEmpresa.get();
        if (dados.getRazaoSocial() != null) empresa.setRazaoSocial(dados.getRazaoSocial());
        if (dados.getNomeFantasia() != null) empresa.setNomeFantasia(dados.getNomeFantasia());
        if (dados.getEndereco() != null) empresa.setEndereco(dados.getEndereco());
        if (dados.getTelefones() != null && !dados.getTelefones().isEmpty())
            empresa.setTelefones(dados.getTelefones());
        Empresa salva = repositorioEmpresa.save(empresa);
        return ResponseEntity.ok(toModel(salva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        if (!repositorioEmpresa.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioEmpresa.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Associar usuário a uma empresa
    @PostMapping("/{idEmpresa}/usuario/{idUsuario}")
    public ResponseEntity<EntityModel<Empresa>> adicionarUsuario(
            @PathVariable Long idEmpresa, @PathVariable Long idUsuario) {
        Optional<Empresa> optEmpresa = repositorioEmpresa.findById(idEmpresa);
        Optional<Usuario> optUsuario = repositorioUsuario.findById(idUsuario);
        if (optEmpresa.isEmpty() || optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = optEmpresa.get();
        empresa.getUsuarios().add(optUsuario.get());
        Empresa salva = repositorioEmpresa.save(empresa);
        return ResponseEntity.ok(toModel(salva));
    }

    // Remover usuário de uma empresa
    @DeleteMapping("/{idEmpresa}/usuario/{idUsuario}")
    public ResponseEntity<EntityModel<Empresa>> removerUsuario(
            @PathVariable Long idEmpresa, @PathVariable Long idUsuario) {
        Optional<Empresa> optEmpresa = repositorioEmpresa.findById(idEmpresa);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = optEmpresa.get();
        empresa.getUsuarios().removeIf(u -> u.getId().equals(idUsuario));
        Empresa salva = repositorioEmpresa.save(empresa);
        return ResponseEntity.ok(toModel(salva));
    }
}
