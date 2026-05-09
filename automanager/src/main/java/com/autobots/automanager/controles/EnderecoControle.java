package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.servicos.ServicoEndereco;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private ServicoEndereco servico;

    @GetMapping
    public ResponseEntity<List<Endereco>> listar() {
        List<Endereco> lista = servico.listar();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscar(@PathVariable long id) {
        Endereco endereco = servico.buscar(id);
        if (endereco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(endereco);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Endereco endereco) {
        servico.cadastrar(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Endereco atualizacao) {
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
