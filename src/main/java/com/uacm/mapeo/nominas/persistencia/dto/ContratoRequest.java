package com.uacm.mapeo.nominas.persistencia.dto;

import com.uacm.mapeo.nominas.persistencia.entidades.enums.MotivoContrato;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ContratoRequest {
    private Long empleadoId;
    private Long proyectoId;
    private MotivoContrato motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double salarioAsignado;
    private Integer horasContratadas = 48;
    private String descripcionActividades;
}