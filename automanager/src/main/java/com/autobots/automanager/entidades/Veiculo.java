package com.autobots.automanager.entidades;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, exclude = { "proprietario", "vendas" })
@Entity
public class Veiculo extends RepresentationModel<Veiculo> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TipoVeiculo tipo;
    @Column(nullable = false)
    private String modelo;
    @Column(nullable = false)
    private String placa;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private Usuario proprietario;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private Set<Venda> vendas = new HashSet<>();
}