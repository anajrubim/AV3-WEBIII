package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.servicos.ServicoMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {
    @Autowired
    private ServicoMercadoria servico;

    @GetMapping
    public ResponseEntity<List<Mercadoria>> listar() {
        List<Mercadoria> lista = servico.listar();
        if (lista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> buscar(@PathVariable long id) {
        Mercadoria mercadoria = servico.buscar(id);
        if (mercadoria == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(mercadoria);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Mercadoria mercadoria) {
        servico.cadastrar(mercadoria);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Mercadoria atualizacao) {
        atualizacao.setId(id);
        servico.atualizar(atualizacao);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        servico.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
