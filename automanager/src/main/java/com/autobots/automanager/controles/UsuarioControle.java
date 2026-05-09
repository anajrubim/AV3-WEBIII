package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.ServicoUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {
    @Autowired
    private ServicoUsuario servico;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> lista = servico.listar();
        if (lista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable long id) {
        Usuario usuario = servico.buscar(id);
        if (usuario == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Usuario usuario) {
        servico.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable long id, @RequestBody Usuario atualizacao) {
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
