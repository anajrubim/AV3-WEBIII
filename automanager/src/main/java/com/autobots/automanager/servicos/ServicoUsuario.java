package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Service
public class ServicoUsuario {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;

    public List<Usuario> listar() {

        List<Usuario> lista = repositorio.findAll();

        adicionadorLink.adicionarLink(lista);

        return lista;
    }

    public Usuario buscar(long id) {

        Usuario usuario = repositorio.findById(id).orElse(null);

        if (usuario != null) {
            adicionadorLink.adicionarLink(usuario);
        }

        return usuario;
    }

    public void cadastrar(Usuario usuario) {

        repositorio.save(usuario);
    }

    public void atualizar(Usuario atualizacao) {

        Usuario usuario = repositorio
                .findById(atualizacao.getId())
                .orElseThrow();

        if (atualizacao.getNome() != null &&
                !atualizacao.getNome().isBlank()) {

            usuario.setNome(atualizacao.getNome());
        }

        if (atualizacao.getNomeSocial() != null &&
                !atualizacao.getNomeSocial().isBlank()) {

            usuario.setNomeSocial(atualizacao.getNomeSocial());
        }

        if (atualizacao.getPerfis() != null &&
                !atualizacao.getPerfis().isEmpty()) {

            usuario.setPerfis(atualizacao.getPerfis());
        }

        repositorio.save(usuario);
    }

    public void excluir(long id) {

        Usuario usuario = repositorio
                .findById(id)
                .orElseThrow();

        List<Empresa> empresas = empresaRepositorio.findAll();

        for (Empresa empresa : empresas) {

            empresa.getUsuarios().remove(usuario);

            empresaRepositorio.save(empresa);
        }

        repositorio.delete(usuario);
    }
}