package com.uacm.mapeo.nominas.persistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominaDetalleDTO {
    private Long id;
    private String reciboId;
    private Long empleadoId;
    private String empleadoNombre;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
    private LocalDate fechaPago;
    private String tipoNomina;
    private Integer diasTrabajados;
    private Integer horasExtras;

    // Percepciones
    private Double sueldoBase;
    private Double montoHorasExtras;
    private Double primaDominical;
    private Double vacaciones;
    private Double primaVacacional;
    private Double aguinaldo;
    private Double otrosBonos;
    private Double totalPercepciones;

    // Deducciones
    private Double imssEmpleado;
    private Double isrRetenido;
    private Double subsidioEmpleo;
    private Double infonavit;
    private Double fonacot;
    private Double otrosDescuentos;
    private Double totalDeducciones;

    // Totales
    private Double totalNeto;
    private Double devengado;
    private Double deducciones;

    // Información adicional
    private Double salarioDiarioIntegrado;
    private Double salarioBaseCotizacion;
    private String estatusNomina;
    private String metodoPago;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
