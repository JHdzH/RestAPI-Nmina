package com.uacm.mapeo.nominas.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tablas_isr")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TablaISR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vigencia_inicio", nullable = false)
    private LocalDate vigenciaInicio;

    @Column(name = "vigencia_fin")
    private LocalDate vigenciaFin;

    @Column(name = "limite_inferior", nullable = false)
    private Double limiteInferior;

    @Column(name = "limite_superior", nullable = false)
    private Double limiteSuperior;

    @Column(name = "cuota_fija", nullable = false)
    private Double cuotaFija;

    @Column(name = "porcentaje_excedente", nullable = false)
    private Double porcentajeExcedente;

    @Column(name = "subsidio_empleo")
    private Double subsidioEmpleo = 0.0;

    private String tipo = "MENSUAL";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
