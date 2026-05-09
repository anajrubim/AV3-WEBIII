package com.autobots.automanager.entidades;

import java.util.Date;
import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CredencialUsuarioSenha.class, name = "usuarioSenha"),
    @JsonSubTypes.Type(value = CredencialCodigoBarra.class, name = "codigoBarra")
})
public abstract class Credencial extends RepresentationModel<Credencial> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Date criacao;
    @Column
    private Date ultimoAcesso;
    @Column(nullable = false)
    private boolean inativo;
}