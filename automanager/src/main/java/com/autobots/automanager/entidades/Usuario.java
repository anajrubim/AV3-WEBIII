package com.autobots.automanager.entidades;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, exclude = { "mercadorias", "vendas", "veiculos" })
@Entity
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column
    private String nomeSocial;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PerfilUsuario> perfis = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Telefone> telefones = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Documento> documentos = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Email> emails = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Credencial> credenciais = new HashSet<>();
    @JsonIgnore
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    private Set<Mercadoria> mercadorias = new HashSet<>();
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private Set<Venda> vendas = new HashSet<>();
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private Set<Veiculo> veiculos = new HashSet<>();
}