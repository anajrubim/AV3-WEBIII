package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.servicos.ServicoDocumento;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private ServicoDocumento servico;

    @GetMapping
    public ResponseEntity<List<Documento>> listar() {
        List<Documento> lista = servico.listar();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> buscar(@PathVariable long id) {
        Documento documento = servico.buscar(id);
        if (documento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(documento);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Documento documento) {
        servico.cadastrar(documento);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Documento atualizacao) {
        atualizacao.setId(id);
        servico.atualizar(atualizacao);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        servico.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
