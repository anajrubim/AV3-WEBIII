package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.servicos.ServicoVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
    @Autowired
    private ServicoVeiculo servico;

    @GetMapping
    public ResponseEntity<List<Veiculo>> listar() {
        List<Veiculo> lista = servico.listar();
        if (lista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable long id) {
        Veiculo veiculo = servico.buscar(id);
        if (veiculo == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(veiculo);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Veiculo veiculo) {
        servico.cadastrar(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Veiculo atualizacao) {
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
