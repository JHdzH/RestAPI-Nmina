package com.uacm.mapeo.nominas.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusProyecto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clave;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    private String cliente;

    @Column(name = "presupuesto_total")
    private Double presupuestoTotal;

    @Column(name = "presupuesto_nomina")
    private Double presupuestoNomina;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada", nullable = false)
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstatusProyecto estatus = EstatusProyecto.PLANEACION;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Empleado responsable;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContratoProyecto> contratos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getter para ID del responsable (sin exponer todo el objeto)
    @Transient
    public Long getResponsableId() {
        return responsable != null ? responsable.getId() : null;
    }

    @Transient
    public String getResponsableNombre() {
        return responsable != null ? responsable.getNombre() : null;
    }
}