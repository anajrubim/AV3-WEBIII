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

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.repositorios.RepositorioDocumento;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private RepositorioDocumento repositorioDocumento;

    private EntityModel<Documento> toModel(Documento documento) {
        EntityModel<Documento> model = EntityModel.of(documento);
        model.add(linkTo(methodOn(DocumentoControle.class).obterDocumento(documento.getId())).withSelfRel());
        model.add(linkTo(methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
        return model;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Documento>>> obterDocumentos() {
        List<EntityModel<Documento>> documentos = repositorioDocumento.findAll()
                .stream().map(this::toModel).collect(Collectors.toList());
        CollectionModel<EntityModel<Documento>> collection = CollectionModel.of(documentos,
                linkTo(methodOn(DocumentoControle.class).obterDocumentos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Documento>> obterDocumento(@PathVariable Long id) {
        Optional<Documento> documento = repositorioDocumento.findById(id);
        if (documento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toModel(documento.get()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Documento>> cadastrarDocumento(@RequestBody Documento documento) {
        documento.setId(null);
        Documento salvo = repositorioDocumento.save(documento);
        EntityModel<Documento> model = toModel(salvo);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Documento>> atualizarDocumento(
            @PathVariable Long id, @RequestBody Documento dados) {
        Optional<Documento> optDocumento = repositorioDocumento.findById(id);
        if (optDocumento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Documento documento = optDocumento.get();
        if (dados.getTipo() != null) documento.setTipo(dados.getTipo());
        if (dados.getNumero() != null) documento.setNumero(dados.getNumero());
        if (dados.getDataEmissao() != null) documento.setDataEmissao(dados.getDataEmissao());
        Documento salvo = repositorioDocumento.save(documento);
        return ResponseEntity.ok(toModel(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDocumento(@PathVariable Long id) {
        if (!repositorioDocumento.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorioDocumento.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
