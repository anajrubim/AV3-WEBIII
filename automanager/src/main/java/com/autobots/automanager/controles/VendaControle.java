package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.servicos.ServicoVenda;

@RestController
@RequestMapping("/venda")
public class VendaControle {
    @Autowired
    private ServicoVenda servico;

    @GetMapping
    public ResponseEntity<List<Venda>> listar() {
        List<Venda> lista = servico.listar();
        if (lista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscar(@PathVariable long id) {
        Venda venda = servico.buscar(id);
        if (venda == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(venda);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Venda venda) {
        servico.cadastrar(venda);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Venda atualizacao) {
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
