package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.servicos.ServicoEmpresa;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {
    @Autowired
    private ServicoEmpresa servico;

    @GetMapping
    public ResponseEntity<List<Empresa>> listar() {
        List<Empresa> lista = servico.listar();
        if (lista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscar(@PathVariable long id) {
        Empresa empresa = servico.buscar(id);
        if (empresa == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Empresa empresa) {
        servico.cadastrar(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Empresa atualizacao) {
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
