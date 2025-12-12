package com.uacm.mapeo.nominas.persistencia.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    private String periodo;

    // Nuevos campos para períodos
    @Column(name = "periodo_inicio")
    private LocalDate periodoInicio;

    @Column(name = "periodo_fin")
    private LocalDate periodoFin;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "tipo_nomina")
    private String tipoNomina = "QUINCENAL";

    // Campos de días
    @Column(name = "dias_trabajados")
    private Integer diasTrabajados = 15;

    @Column(name = "dias_incapacidad")
    private Integer diasIncapacidad = 0;

    @Column(name = "dias_vacaciones")
    private Integer diasVacaciones = 0;

    @Column(name = "horas_extras")
    private Integer horasExtras = 0;

    @Column(name = "dias_descanso_trabajados")
    private Integer diasDescansoTrabajados = 0;

    // Percepciones
    @Column(name = "sueldo_base")
    private Double sueldoBase = 0.0;

    @Column(name = "monto_horas_extras")
    private Double montoHorasExtras = 0.0;

    @Column(name = "prima_dominical")
    private Double primaDominical = 0.0;

    @Column(name = "vacaciones")
    private Double vacaciones = 0.0;

    @Column(name = "prima_vacacional")
    private Double primaVacacional = 0.0;

    @Column(name = "aguinaldo")
    private Double aguinaldo = 0.0;

    @Column(name = "otros_bonos")
    private Double otrosBonos = 0.0;

    @Column(name = "total_percepciones")
    private Double totalPercepciones = 0.0;

    // Deducciones
    @Column(name = "imss_empleado")
    private Double imssEmpleado = 0.0;

    @Column(name = "isr_retenido")
    private Double isrRetenido = 0.0;

    @Column(name = "subsidio_empleo")
    private Double subsidioEmpleo = 0.0;

    @Column(name = "infonavit")
    private Double infonavit = 0.0;

    @Column(name = "fonacot")
    private Double fonacot = 0.0;

    @Column(name = "otros_descuentos")
    private Double otrosDescuentos = 0.0;

    @Column(name = "total_deducciones")
    private Double totalDeducciones = 0.0;

    // Campos para cálculo
    @Column(name = "salario_diario_integrado")
    private Double salarioDiarioIntegrado = 0.0;

    @Column(name = "salario_base_cotizacion")
    private Double salarioBaseCotizacion = 0.0;

    // Totales existentes (mantener compatibilidad)
    @Column(name = "total_neto")
    private Double totalNeto = 0.0;

    private Double devengado = 0.0;
    private Double deducciones = 0.0;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "estatus_nomina")
    private String estatusNomina = "CALCULADA";

    @Column(name = "metodo_pago")
    private String metodoPago = "EFECTIVO";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
        // Generar periodo automático si no está definido
        if (periodo == null && periodoInicio != null) {
            periodo = String.format("%04d-%02d",
                    periodoInicio.getYear(),
                    periodoInicio.getMonthValue());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}