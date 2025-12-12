package com.uacm.mapeo.nominas.persistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculoNominaRequest {
    private Long empleadoId;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
    private LocalDate fechaPago;
    private Integer diasTrabajados;
    private Integer horasExtras;
    private Integer diasDescansoTrabajados;
    private Integer diasIncapacidad;
    private Integer diasVacaciones;
    private String tipoNomina;
    private Long proyectoId;
    private String metodoPago;

    // Método para establecer valores por defecto si son nulos
    public void aplicarValoresPorDefecto() {
        if (diasTrabajados == null) diasTrabajados = 15;
        if (horasExtras == null) horasExtras = 0;
        if (diasDescansoTrabajados == null) diasDescansoTrabajados = 0;
        if (diasIncapacidad == null) diasIncapacidad = 0;
        if (diasVacaciones == null) diasVacaciones = 0;
        if (tipoNomina == null) tipoNomina = "QUINCENAL";
        if (metodoPago == null) metodoPago = "EFECTIVO";
        if (fechaPago == null) fechaPago = LocalDate.now();
    }
}