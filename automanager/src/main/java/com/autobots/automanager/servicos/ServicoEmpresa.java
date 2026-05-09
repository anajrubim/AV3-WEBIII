package com.autobots.automanager.servicos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@Service
public class ServicoEmpresa {
    @Autowired
    private EmpresaRepositorio repositorio;
    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    public List<Empresa> listar() {
        List<Empresa> lista = repositorio.findAll();
        adicionadorLink.adicionarLink(lista);
        return lista;
    }

    public Empresa buscar(long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);
        if (empresa != null) adicionadorLink.adicionarLink(empresa);
        return empresa;
    }

    public void cadastrar(Empresa empresa) {
        repositorio.save(empresa);
    }

    public void atualizar(Empresa atualizacao) {
        Empresa empresa = repositorio.findById(atualizacao.getId()).orElseThrow();
        if (atualizacao.getRazaoSocial() != null && !atualizacao.getRazaoSocial().isBlank())
            empresa.setRazaoSocial(atualizacao.getRazaoSocial());
        if (atualizacao.getNomeFantasia() != null && !atualizacao.getNomeFantasia().isBlank())
            empresa.setNomeFantasia(atualizacao.getNomeFantasia());
        if (atualizacao.getCadastro() != null)
            empresa.setCadastro(atualizacao.getCadastro());
        repositorio.save(empresa);
    }

    public void excluir(long id) {
        repositorio.deleteById(id);
    }
}
