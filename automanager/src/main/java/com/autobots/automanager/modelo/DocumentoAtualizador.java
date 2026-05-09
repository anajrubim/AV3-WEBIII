package com.autobots.automanager.modelo;

import java.util.List;
import com.autobots.automanager.entidades.Documento;

public class DocumentoAtualizador {

    public void atualizar(Documento documento, Documento atualizacao) {
        if (atualizacao != null) {
            if (atualizacao.getTipo() != null) {
                documento.setTipo(atualizacao.getTipo());
            }
            if (atualizacao.getNumero() != null && !atualizacao.getNumero().isBlank()) {
                documento.setNumero(atualizacao.getNumero());
            }
            if (atualizacao.getDataEmissao() != null) {
                documento.setDataEmissao(atualizacao.getDataEmissao());
            }
        }
    }

    public void atualizar(List<Documento> documentos, List<Documento> atualizacoes) {
        if (atualizacoes != null && documentos != null) {
            for (Documento atualizacao : atualizacoes) {
                if (atualizacao.getId() != null) {
                    for (Documento documento : documentos) {
                        if (documento.getId() != null && atualizacao.getId().equals(documento.getId())) {
                            atualizar(documento, atualizacao);
                        }
                    }
                }
            }
        }
    }
}
