package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.servicos.ServicoTelefone;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private ServicoTelefone servico;

    @GetMapping
    public ResponseEntity<List<Telefone>> listar() {
        List<Telefone> lista = servico.listar();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> buscar(@PathVariable long id) {
        Telefone telefone = servico.buscar(id);
        if (telefone == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(telefone);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Telefone telefone) {
        servico.cadastrar(telefone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Telefone atualizacao) {
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
