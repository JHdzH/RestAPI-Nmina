// src/main/java/com/uacm/mapeo/nominas/persistencia/entidades/Empleado.java
package com.uacm.mapeo.nominas.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_empleado")
    private String numeroEmpleado;

    private String nombre;
    private String puesto;

    // Nuevos campos
    private String rfc;
    private String curp;
    private String nss;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato")
    @Builder.Default
    private TipoContrato tipoContrato = TipoContrato.INDETERMINADO;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @Column(name = "salario_diario")
    private Double salarioDiario;

    @Column(name = "salario_mensual")
    private Double salarioMensual;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RegimenContratacion regimen = RegimenContratacion.SUELDOS;

    private String banco;

    @Column(name = "cuenta_bancaria")
    private String cuentaBancaria;

    @Column(name = "clabe_interbancaria")
    private String clabeInterbancaria;

    private String email;
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstatusEmpleado estatus = EstatusEmpleado.ACTIVO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones - IGNORAR en JSON para evitar recursión
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContratoProyecto> contratosProyecto = new ArrayList<>();
}