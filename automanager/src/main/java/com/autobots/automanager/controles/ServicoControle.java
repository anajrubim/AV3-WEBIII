package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.servicos.ServicoServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {
    @Autowired
    private ServicoServico servico;

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        List<Servico> lista = servico.listar();
        if (lista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscar(@PathVariable long id) {
        Servico s = servico.buscar(id);
        if (s == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(s);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Servico s) {
        servico.cadastrar(s);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Servico atualizacao) {
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
