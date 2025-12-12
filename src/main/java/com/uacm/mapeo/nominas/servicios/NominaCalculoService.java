package com.uacm.mapeo.nominas.servicios;

import com.uacm.mapeo.nominas.persistencia.dto.CalculoNominaRequest;
import com.uacm.mapeo.nominas.persistencia.dto.NominaDetalleDTO;
import com.uacm.mapeo.nominas.persistencia.dto.NominaResumenDTO;
import com.uacm.mapeo.nominas.persistencia.entidades.*;
import com.uacm.mapeo.nominas.persistencia.repositorios.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NominaCalculoService {

    private final EmpleadoRepository empleadoRepositorio;
    private final NominaRepository nominaRepositorio;
    private final ParametroNominaRepository parametroNominaRepositorio;
    private final TablaISRRepository tablaISRRepositorio;
    private final ProyectoRepository proyectoRepositorio;

    @Transactional
    public Nomina calcularNomina(CalculoNominaRequest request) {
        // Validar que request no sea null
        if (request == null) {
            log.error("Request de cálculo de nómina es null");
            throw new RuntimeException("Request de cálculo de nómina es null");
        }

        // Aplicar valores por defecto si existen
        request.aplicarValoresPorDefecto();

        log.info("Iniciando cálculo de nómina para empleado ID: {}", request.getEmpleadoId());

        // 1. Validar y obtener empleado
        Empleado empleado = empleadoRepositorio.findById(request.getEmpleadoId())
                .orElseThrow(() -> {
                    log.error("Empleado no encontrado con ID: {}", request.getEmpleadoId());
                    return new RuntimeException("Empleado no encontrado");
                });

        log.info("Empleado encontrado: {} (ID: {}), Salario diario: ${}, Tipo contrato: {}",
                empleado.getNombre(), empleado.getId(),
                empleado.getSalarioDiario(), empleado.getTipoContrato());

        // 2. Validar proyecto si se especifica
        Proyecto proyecto = null;
        if (request.getProyectoId() != null) {
            proyecto = proyectoRepositorio.findById(request.getProyectoId())
                    .orElse(null);
            if (proyecto != null) {
                log.info("Proyecto asignado: {}", proyecto.getNombre());
            }
        }

        // 3. Crear objeto nómina
        Nomina nomina = Nomina.builder()
                .empleado(empleado)
                .proyecto(proyecto)
                .periodoInicio(request.getPeriodoInicio())
                .periodoFin(request.getPeriodoFin())
                .fechaPago(request.getFechaPago() != null ? request.getFechaPago() : LocalDate.now())
                .diasTrabajados(request.getDiasTrabajados())
                .horasExtras(request.getHorasExtras())
                .diasDescansoTrabajados(request.getDiasDescansoTrabajados())
                .diasIncapacidad(request.getDiasIncapacidad())
                .diasVacaciones(request.getDiasVacaciones())
                .tipoNomina(request.getTipoNomina())
                .metodoPago(request.getMetodoPago())
                .estatusNomina("CALCULADA")
                .fechaRegistro(LocalDate.now())
                .build();

        // 4. Realizar cálculos
        calcularPercepciones(nomina, empleado);
        calcularDeducciones(nomina, empleado);
        calcularTotales(nomina);

        // 5. Generar recibo ID
        String reciboId = generarReciboId(nomina);
        nomina.setReciboId(reciboId);

        // 6. Generar periodo automático si no existe
        if (nomina.getPeriodo() == null && nomina.getPeriodoInicio() != null) {
            nomina.setPeriodo(String.format("%04d-%02d",
                    nomina.getPeriodoInicio().getYear(),
                    nomina.getPeriodoInicio().getMonthValue()));
        }

        log.info("Nómina calculada - Recibo: {}, Neto: ${}",
                reciboId, nomina.getTotalNeto());

        return nominaRepositorio.save(nomina);
    }

    // ========== MÉTODOS DE CÁLCULO ==========

    private void calcularPercepciones(Nomina nomina, Empleado empleado) {
        Double salarioDiario = empleado.getSalarioDiario();
        if (salarioDiario == null) {
            salarioDiario = 0.0;
            log.warn("Empleado {} tiene salario diario NULL, usando 0.0", empleado.getNombre());
        }

        Integer diasTrabajados = nomina.getDiasTrabajados() != null ? nomina.getDiasTrabajados() : 15;

        // 1. Sueldo base
        Double sueldoBase = salarioDiario * diasTrabajados;
        nomina.setSueldoBase(redondear(sueldoBase));
        log.debug("Sueldo base: ${} × {} días = ${}",
                salarioDiario, diasTrabajados, sueldoBase);

        // 2. Horas extras
        Double montoHorasExtras = calcularHorasExtras(salarioDiario, nomina.getHorasExtras());
        nomina.setMontoHorasExtras(redondear(montoHorasExtras));
        log.debug("Horas extras: {} hrs = ${}",
                nomina.getHorasExtras(), montoHorasExtras);

        // 3. Prima dominical
        Double primaDominical = calcularPrimaDominical(salarioDiario, nomina.getDiasDescansoTrabajados());
        nomina.setPrimaDominical(redondear(primaDominical));
        log.debug("Prima dominical: {} días = ${}",
                nomina.getDiasDescansoTrabajados(), primaDominical);

        // 4. Total percepciones
        Double totalPercepciones = sueldoBase + montoHorasExtras + primaDominical;
        nomina.setTotalPercepciones(redondear(totalPercepciones));
        nomina.setDevengado(redondear(totalPercepciones));
        log.info("Total percepciones: ${}", totalPercepciones);
    }

    private Double calcularHorasExtras(Double salarioDiario, Integer horasExtras) {
        if (horasExtras == null || horasExtras == 0 || salarioDiario == null || salarioDiario == 0) {
            return 0.0;
        }

        Double valorHoraNormal = salarioDiario / 8.0;
        Double monto = 0.0;

        // Primeras 9 horas: dobles
        if (horasExtras <= 9) {
            monto = valorHoraNormal * 2 * horasExtras;
        } else {
            // Primeras 9 dobles, resto triples
            Double primeras9 = valorHoraNormal * 2 * 9;
            Double resto = valorHoraNormal * 3 * (horasExtras - 9);
            monto = primeras9 + resto;
        }

        return monto;
    }

    private Double calcularPrimaDominical(Double salarioDiario, Integer diasDescansoTrabajados) {
        if (diasDescansoTrabajados == null || diasDescansoTrabajados == 0 || salarioDiario == null || salarioDiario == 0) {
            return 0.0;
        }

        // Obtener porcentaje de prima dominical (25%)
        Double porcentajePrima = obtenerParametro("PRIMA_DOMINICAL").orElse(0.25);

        return salarioDiario * porcentajePrima * diasDescansoTrabajados;
    }

    private void calcularDeducciones(Nomina nomina, Empleado empleado) {
        // 1. Calcular IMSS
        Double imss = calcularIMSS(nomina, empleado);
        nomina.setImssEmpleado(redondear(imss));

        // 2. Calcular ISR
        Double isr = calcularISR(nomina);
        nomina.setIsrRetenido(redondear(isr));

        // 3. Aplicar subsidio al empleo
        Double subsidio = calcularSubsidioEmpleo(nomina);
        nomina.setSubsidioEmpleo(redondear(subsidio));

        // 4. Total deducciones
        Double totalDeducciones = imss + isr - subsidio;

        if (totalDeducciones < 0) {
            totalDeducciones = 0.0;
        }

        nomina.setTotalDeducciones(redondear(totalDeducciones));
        nomina.setDeducciones(redondear(totalDeducciones));

        log.info("Deducciones - IMSS: ${}, ISR: ${}, Subsidio: ${}, Total: ${}",
                imss, isr, subsidio, totalDeducciones);
    }

    private Double calcularISR(Nomina nomina) {
        if (nomina.getTotalPercepciones() == null || nomina.getTotalPercepciones() == 0) {
            log.debug("ISR: Ingreso 0, no aplica retención");
            return 0.0;
        }

        // Convertir ingreso quincenal a mensual para búsqueda en tabla
        Double ingresoQuincenal = nomina.getTotalPercepciones();
        Double ingresoMensual = ingresoQuincenal * 2;

        log.debug("Calculando ISR para ingreso mensual: ${}", ingresoMensual);

        // Buscar en tabla ISR
        Optional<TablaISR> tablaOpt = tablaISRRepositorio.findByIngresoTipoYFecha(
                ingresoMensual, "MENSUAL", nomina.getFechaPago());

        if (tablaOpt.isPresent()) {
            TablaISR tabla = tablaOpt.get();

            // Calcular ISR mensual
            Double excedente = ingresoMensual - tabla.getLimiteInferior();
            Double isrMensual = tabla.getCuotaFija() + (excedente * tabla.getPorcentajeExcedente());

            log.debug("ISR cálculo - Límite inferior: ${}, Cuota fija: ${}, Excedente: ${}, %: {}, ISR mensual: ${}",
                    tabla.getLimiteInferior(), tabla.getCuotaFija(), excedente,
                    tabla.getPorcentajeExcedente(), isrMensual);

            // Aplicar subsidio si aplica
            if (tabla.getSubsidioEmpleo() != null && tabla.getSubsidioEmpleo() > 0) {
                log.debug("Aplicando subsidio: ${}", tabla.getSubsidioEmpleo());
                isrMensual = isrMensual - tabla.getSubsidioEmpleo();
                if (isrMensual < 0) isrMensual = 0.0;
            }

            // Convertir a quincenal
            Double isrQuincenal = redondear(isrMensual / 2);
            log.debug("ISR quincenal final: ${}", isrQuincenal);
            return isrQuincenal;
        }

        log.warn("No se encontró tabla ISR para ingreso: ${}", ingresoMensual);
        return 0.0;
    }

    private Double calcularIMSS(Nomina nomina, Empleado empleado) {
        // Solo aplicar a empleados con contrato indeterminado
        if (empleado.getTipoContrato() == null ||
                !empleado.getTipoContrato().equals("INDETERMINADO")) {
            log.info("No se calcula IMSS para empleado {} - Tipo contrato: {}",
                    empleado.getNombre(), empleado.getTipoContrato());
            return 0.0;
        }

        // Calcular Salario Base de Cotización (SBC)
        Double sbc = calcularSBC(nomina, empleado);

        // Obtener porcentaje de enfermedad y maternidad (0.25%)
        Double porcentajeIMSS = obtenerParametro("IMSS_ENFERMEDAD_MATERNIDAD")
                .orElse(0.0025);

        // Calcular IMSS diario y multiplicar por días trabajados
        Double imssDiario = sbc * porcentajeIMSS;
        Double imssTotal = imssDiario * nomina.getDiasTrabajados();

        log.info("Cálculo IMSS para {} - SBC: ${}, Porcentaje: {}%, Diario: ${}, Total: ${}",
                empleado.getNombre(), sbc, porcentajeIMSS * 100, imssDiario, imssTotal);

        return redondear(imssTotal);
    }

    private Double calcularSubsidioEmpleo(Nomina nomina) {
        if (nomina.getTotalPercepciones() == null) {
            return 0.0;
        }

        // Solo aplicar si el ingreso es bajo
        Double ingresoMensual = nomina.getTotalPercepciones() * 2;
        Double limiteBajo = 10000.0;

        log.debug("Evaluando subsidio - Ingreso mensual: ${}, Límite: ${}",
                ingresoMensual, limiteBajo);

        if (ingresoMensual <= limiteBajo) {
            Double subsidio = obtenerParametro("SUBSIDIO_EMPLEO_QUINCENAL")
                    .orElse(203.51);
            log.info("Aplicando subsidio al empleo: ${}", subsidio);
            return subsidio;
        }

        log.debug("No aplica subsidio - Ingreso mayor a límite");
        return 0.0;
    }

    private Double calcularSBC(Nomina nomina, Empleado empleado) {
        if (empleado.getSalarioDiario() == null) {
            log.warn("Salario diario NULL para cálculo SBC");
            return 0.0;
        }

        // Fórmula simplificada: SDI = Salario diario * factor de integración
        Double factorIntegracion = obtenerParametro("FACTOR_INTEGRACION_15")
                .orElse(1.0226);

        Double sdi = empleado.getSalarioDiario() * factorIntegracion;
        nomina.setSalarioDiarioIntegrado(redondear(sdi));
        nomina.setSalarioBaseCotizacion(redondear(sdi));

        log.debug("SBC calculado: ${} × {} = ${}",
                empleado.getSalarioDiario(), factorIntegracion, sdi);

        return sdi;
    }

    private void calcularTotales(Nomina nomina) {
        Double totalPercepciones = nomina.getTotalPercepciones() != null ? nomina.getTotalPercepciones() : 0.0;
        Double totalDeducciones = nomina.getTotalDeducciones() != null ? nomina.getTotalDeducciones() : 0.0;

        Double netoAPagar = totalPercepciones - totalDeducciones;
        nomina.setTotalNeto(redondear(netoAPagar));

        log.info("Totales - Percepciones: ${}, Deducciones: ${}, Neto: ${}",
                totalPercepciones, totalDeducciones, netoAPagar);
    }

    // ========== NUEVOS MÉTODOS PARA ENDPOINTS ==========

    public Optional<NominaDetalleDTO> obtenerNominaPorId(Long id) {
        log.info("Buscando nómina con ID: {}", id);
        return nominaRepositorio.findById(id)
                .map(this::convertirADetalleDTO);
    }

    public List<NominaResumenDTO> listarNominasPorEmpleado(Long empleadoId) {
        log.info("Listando nóminas para empleado ID: {}", empleadoId);
        return nominaRepositorio.findByEmpleadoId(empleadoId).stream()
                .map(this::convertirAResumenDTO)
                .collect(Collectors.toList());
    }


    public List<NominaResumenDTO> listarNominasPorPeriodo(LocalDate inicio, LocalDate fin) {
        log.info("Listando nóminas del período: {} al {}", inicio, fin);
        // Usen el método que prefieran:
        // Opción 1: Consulta personalizada
        return nominaRepositorio.findByPeriodoInicioAndPeriodoFin(inicio, fin).stream()
                .map(this::convertirAResumenDTO)
                .collect(Collectors.toList());

        // Opción 2: Con Between (está implementado)
        // return nominaRepositorio.findByPeriodoInicioBetween(inicio, fin).stream()
        //         .map(this::convertirAResumenDTO)
        //         .collect(Collectors.toList());
    }

    public List<NominaResumenDTO> listarNominasPorMes(int año, int mes) {
        log.info("Listando nóminas del mes: {}/{}", mes, año);
        LocalDate inicio = LocalDate.of(año, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

        return nominaRepositorio.findByPeriodoInicioAndPeriodoFin(inicio, fin).stream()
                .map(this::convertirAResumenDTO)
                .collect(Collectors.toList());
    }

    public List<NominaResumenDTO> listarTodasNominas() {
        log.info("Listando todas las nóminas");
        return nominaRepositorio.findAll().stream()
                .map(this::convertirAResumenDTO)
                .sorted((n1, n2) -> {
                    // Manejar posibles nulls en IDs
                    Long id1 = n1.getId() != null ? n1.getId() : 0L;
                    Long id2 = n2.getId() != null ? n2.getId() : 0L;
                    return id2.compareTo(id1); // Más recientes primero
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<Nomina> cambiarEstatusNomina(Long id, String nuevoEstatus) {
        log.info("Cambiando estatus de nómina {} a: {}", id, nuevoEstatus);
        return nominaRepositorio.findById(id)
                .map(nomina -> {
                    String estatusAnterior = nomina.getEstatusNomina();
                    nomina.setEstatusNomina(nuevoEstatus);
                    Nomina actualizada = nominaRepositorio.save(nomina);
                    log.info("Estatus cambiado: {} → {}", estatusAnterior, nuevoEstatus);
                    return actualizada;
                });
    }

    @Transactional
    public boolean eliminarNomina(Long id) {
        log.info("Intentando eliminar nómina ID: {}", id);
        return nominaRepositorio.findById(id)
                .map(nomina -> {
                    if (!"PAGADA".equals(nomina.getEstatusNomina())) {
                        nominaRepositorio.delete(nomina);
                        log.info("Nómina eliminada correctamente");
                        return true;
                    }
                    log.warn("No se puede eliminar nómina pagada");
                    return false;
                })
                .orElse(false);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Optional<Double> obtenerParametro(String clave) {
        return parametroNominaRepositorio.findByClaveAndActivoTrue(clave)
                .map(ParametroNomina::getValor);
    }

    private String generarReciboId(Nomina nomina) {
        // Formato: NOM-YYYYMMDD-SSSS
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefijo = "NOM-" + fecha + "-";

        // Buscar el último recibo con este prefijo
        Optional<String> ultimoRecibo = nominaRepositorio.findUltimoReciboIdByPrefijo(prefijo);

        if (ultimoRecibo.isPresent()) {
            // Extraer el número secuencial e incrementarlo
            String ultimoId = ultimoRecibo.get();
            String numeroStr = ultimoId.substring(prefijo.length());
            try {
                int numero = Integer.parseInt(numeroStr) + 1;
                return String.format("%s%04d", prefijo, numero);
            } catch (NumberFormatException e) {
                log.warn("Error al parsear número de recibo: {}, usando timestamp", numeroStr);
            }
        }
        // Si no hay recibo previo o hay error, usamos timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        int length = timestamp.length();
        String subTimestamp = length > 4 ? timestamp.substring(length - 4) : timestamp;
        return prefijo + subTimestamp;
    }

    private Double redondear(Double valor) {
        if (valor == null) return 0.0;
        return Math.round(valor * 100.0) / 100.0;
    }

    // ========== MÉTODOS DE CONVERSIÓN DTO ==========

    private NominaDetalleDTO convertirADetalleDTO(Nomina nomina) {
        if (nomina == null) {
            return null;
        }

        Long empleadoId = null;
        String empleadoNombre = null;
        if (nomina.getEmpleado() != null) {
            empleadoId = nomina.getEmpleado().getId();
            empleadoNombre = nomina.getEmpleado().getNombre();
        }

        return NominaDetalleDTO.builder()
                .id(nomina.getId())
                .reciboId(nomina.getReciboId())
                .empleadoId(empleadoId)
                .empleadoNombre(empleadoNombre)
                .periodoInicio(nomina.getPeriodoInicio())
                .periodoFin(nomina.getPeriodoFin())
                .fechaPago(nomina.getFechaPago())
                .tipoNomina(nomina.getTipoNomina())
                .diasTrabajados(nomina.getDiasTrabajados())
                .horasExtras(nomina.getHorasExtras())
                .sueldoBase(nomina.getSueldoBase())
                .montoHorasExtras(nomina.getMontoHorasExtras())
                .primaDominical(nomina.getPrimaDominical())
                .vacaciones(nomina.getVacaciones())
                .primaVacacional(nomina.getPrimaVacacional())
                .aguinaldo(nomina.getAguinaldo())
                .otrosBonos(nomina.getOtrosBonos())
                .totalPercepciones(nomina.getTotalPercepciones())
                .imssEmpleado(nomina.getImssEmpleado())
                .isrRetenido(nomina.getIsrRetenido())
                .subsidioEmpleo(nomina.getSubsidioEmpleo())
                .infonavit(nomina.getInfonavit())
                .fonacot(nomina.getFonacot())
                .otrosDescuentos(nomina.getOtrosDescuentos())
                .totalDeducciones(nomina.getTotalDeducciones())
                .totalNeto(nomina.getTotalNeto())
                .devengado(nomina.getDevengado())
                .deducciones(nomina.getDeducciones())
                .salarioDiarioIntegrado(nomina.getSalarioDiarioIntegrado())
                .salarioBaseCotizacion(nomina.getSalarioBaseCotizacion())
                .estatusNomina(nomina.getEstatusNomina())
                .metodoPago(nomina.getMetodoPago())
                .createdAt(nomina.getCreatedAt())
                .updatedAt(nomina.getUpdatedAt())
                .build();
    }

    private NominaResumenDTO convertirAResumenDTO(Nomina nomina) {
        if (nomina == null) {
            return null;
        }

        Long empleadoId = null;
        String empleadoNombre = null;
        if (nomina.getEmpleado() != null) {
            empleadoId = nomina.getEmpleado().getId();
            empleadoNombre = nomina.getEmpleado().getNombre();
        }

        return NominaResumenDTO.builder()
                .id(nomina.getId())
                .reciboId(nomina.getReciboId())
                .empleadoId(empleadoId)
                .empleadoNombre(empleadoNombre)
                .periodo(nomina.getPeriodo())
                .periodoInicio(nomina.getPeriodoInicio())
                .periodoFin(nomina.getPeriodoFin())
                .fechaPago(nomina.getFechaPago())
                .tipoNomina(nomina.getTipoNomina())
                .diasTrabajados(nomina.getDiasTrabajados())
                .horasExtras(nomina.getHorasExtras())
                .totalPercepciones(nomina.getTotalPercepciones())
                .totalDeducciones(nomina.getTotalDeducciones())
                .totalNeto(nomina.getTotalNeto())
                .estatusNomina(nomina.getEstatusNomina())
                .fechaRegistro(nomina.getFechaRegistro())
                .build();
    }
}