package com.autobots.automanager.entidades;

import java.util.Date;
import javax.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Documento extends RepresentationModel<Documento> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TipoDocumento tipo;
    @Column(nullable = false)
    private Date dataEmissao;
    @Column(unique = true, nullable = false)
    private String numero;
}
