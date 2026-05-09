package com.autobots.automanager.modelo;

import java.util.List;

import com.autobots.automanager.entidades.Telefone;

public class TelefoneAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Telefone telefone, Telefone atualizacao) {

        if (telefone != null && atualizacao != null) {

            if (!verificador.verificar(atualizacao.getDdd())) {
                telefone.setDdd(atualizacao.getDdd());
            }

            if (!verificador.verificar(atualizacao.getNumero())) {
                telefone.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(List<Telefone> telefones, List<Telefone> atualizacoes) {

        if (telefones != null && atualizacoes != null) {

            for (Telefone atualizacao : atualizacoes) {

                if (atualizacao.getId() != null) {

                    for (Telefone telefone : telefones) {

                        if (telefone.getId() != null &&
                            atualizacao.getId().equals(telefone.getId())) {

                            atualizar(telefone, atualizacao);
                        }
                    }
                }
            }
        }
    }
}