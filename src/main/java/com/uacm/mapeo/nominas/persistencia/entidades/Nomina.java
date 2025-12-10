// src/main/java/com/uacm/mapeo/nominas/persistencia/entidades/Nomina.java
package com.uacm.mapeo.nominas.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nominas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recibo_id")
    private String reciboId;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    private String periodo;

    @Column(name = "total_neto")
    private Double totalNeto;  // Cambiado a Double (objeto wrapper) para comparar con numm en nomina service

    private Double devengado;
    private Double deducciones;

    @Column(name = "fecha_registro")
    private java.time.LocalDate fechaRegistro;
}