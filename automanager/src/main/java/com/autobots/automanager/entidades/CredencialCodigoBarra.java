package com.autobots.automanager.entidades;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class CredencialCodigoBarra extends Credencial {
    @Column(nullable = false, unique = true)
    private long codigo;
}
