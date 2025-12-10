package com.uacm.mapeo.nominas.persistencia.entidades;

import com.uacm.mapeo.nominas.persistencia.entidades.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contratos_proyecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELACIONES PRINCIPALES (sin campos id separados)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proyecto_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Proyecto proyecto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoContrato motivo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "salario_asignado", nullable = false)
    private Double salarioAsignado;

    @Column(name = "horas_contratadas")
    @Builder.Default
    private Integer horasContratadas = 48;

    @Column(name = "descripcion_actividades", columnDefinition = "TEXT")
    private String descripcionActividades;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstatusContrato estatus = EstatusContrato.VIGENTE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Métodos helpers para obtener IDs (sin ser campos de la base de datos)
    @Transient
    public String getEmpleadoNombre() {
        return empleado != null ? empleado.getNombre() : null;
    }

    @Transient
    public String getEmpleadoNumero() {
        return empleado != null ? empleado.getNumeroEmpleado() : null;
    }
}