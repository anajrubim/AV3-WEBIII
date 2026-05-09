package com.autobots.automanager.entidades;

import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Endereco extends RepresentationModel<Endereco> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String estado;
    @Column(nullable = false)
    private String cidade;
    @Column
    private String bairro;
    @Column(nullable = false)
    private String rua;
    @Column(nullable = false)
    private String numero;
    @Column
    private String codigoPostal;
    @Column
    private String informacoesAdicionais;
}
