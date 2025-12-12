package com.uacm.mapeo.nominas.servicios;

import com.uacm.mapeo.nominas.persistencia.entidades.Empleado;
import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import com.uacm.mapeo.nominas.persistencia.repositorios.EmpleadoRepository;
import com.uacm.mapeo.nominas.persistencia.repositorios.NominaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NominaService {

    private final NominaRepository nominaRepository;
    private final EmpleadoRepository empleadoRepository;

    // Obtener todas las nóminas
    public List<Nomina> getAllNominas() {
        return nominaRepository.findAll();
    }

    // Obtener nómina por ID
    public Optional<Nomina> getNominaById(Long id) {
        return nominaRepository.findById(id);
    }

    // Obtener nómina por recibo ID
    public Optional<Nomina> getNominaByReciboId(String reciboId) {
        return nominaRepository.findByReciboId(reciboId);
    }

    // Obtener nóminas por empleado
    public List<Nomina> getNominasByEmpleado(Long empleadoId) {
        return nominaRepository.findByEmpleadoId(empleadoId);
    }

    // Obtener nóminas por periodo (string)
    public List<Nomina> getNominasByPeriodo(String periodo) {
        return nominaRepository.findByPeriodo(periodo);
    }

    // Crear nueva nómina
    public Nomina createNomina(Nomina nomina) {
        // Verificar que el recibo ID no exista
        if (nomina.getReciboId() != null &&
                nominaRepository.existsByReciboId(nomina.getReciboId())) {
            throw new IllegalArgumentException("Ya existe una nómina con ese recibo ID");
        }

        // Verificar que el empleado existe
        if (nomina.getEmpleado() != null && nomina.getEmpleado().getId() != null) {
            Optional<Empleado> empleado = empleadoRepository.findById(nomina.getEmpleado().getId());
            if (empleado.isEmpty()) {
                throw new IllegalArgumentException("Empleado no encontrado");
            }
            // Asignar el empleado completo
            nomina.setEmpleado(empleado.get());
        }

        return nominaRepository.save(nomina);
    }

    // Actualizar nómina
    public Optional<Nomina> updateNomina(Long id, Nomina nominaDetails) {
        return nominaRepository.findById(id)
                .map(nomina -> {
                    // Actualizar campos
                    if (nominaDetails.getReciboId() != null) {
                        nomina.setReciboId(nominaDetails.getReciboId());
                    }
                    if (nominaDetails.getPeriodo() != null) {
                        nomina.setPeriodo(nominaDetails.getPeriodo());
                    }
                    if (nominaDetails.getTotalNeto() != null) {
                        nomina.setTotalNeto(nominaDetails.getTotalNeto());
                    }
                    if (nominaDetails.getDevengado() != null) {
                        nomina.setDevengado(nominaDetails.getDevengado());
                    }
                    if (nominaDetails.getDeducciones() != null) {
                        nomina.setDeducciones(nominaDetails.getDeducciones());
                    }
                    if (nominaDetails.getFechaRegistro() != null) {
                        nomina.setFechaRegistro(nominaDetails.getFechaRegistro());
                    }
                    if (nominaDetails.getEstatusNomina() != null) {
                        nomina.setEstatusNomina(nominaDetails.getEstatusNomina());
                    }

                    return nominaRepository.save(nomina);
                });
    }

    // Actualizar nómina por recibo ID
    public Optional<Nomina> updateNominaByReciboId(String reciboId, Nomina nominaDetails) {
        return nominaRepository.findByReciboId(reciboId)
                .map(nomina -> {
                    if (nominaDetails.getPeriodo() != null) {
                        nomina.setPeriodo(nominaDetails.getPeriodo());
                    }
                    if (nominaDetails.getTotalNeto() != null) {
                        nomina.setTotalNeto(nominaDetails.getTotalNeto());
                    }
                    if (nominaDetails.getDevengado() != null) {
                        nomina.setDevengado(nominaDetails.getDevengado());
                    }
                    if (nominaDetails.getDeducciones() != null) {
                        nomina.setDeducciones(nominaDetails.getDeducciones());
                    }
                    if (nominaDetails.getFechaRegistro() != null) {
                        nomina.setFechaRegistro(nominaDetails.getFechaRegistro());
                    }
                    if (nominaDetails.getEstatusNomina() != null) {
                        nomina.setEstatusNomina(nominaDetails.getEstatusNomina());
                    }

                    return nominaRepository.save(nomina);
                });
    }

    // Eliminar nómina por ID
    public boolean deleteNomina(Long id) {
        if (nominaRepository.existsById(id)) {
            nominaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Eliminar nómina por recibo ID
    public boolean deleteNominaByReciboId(String reciboId) {
        Optional<Nomina> nomina = nominaRepository.findByReciboId(reciboId);
        if (nomina.isPresent()) {
            nominaRepository.delete(nomina.get());
            return true;
        }
        return false;
    }

    // Obtener nóminas por período (rango de fechas)
    public List<Nomina> getNominasPorPeriodo(LocalDate inicio, LocalDate fin) {
        return nominaRepository.findByPeriodoInicioBetween(inicio, fin);
    }

    // Obtener nóminas por mes y año
    public List<Nomina> getNominasPorMes(int year, int month) {
        LocalDate inicio = LocalDate.of(year, month, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        return nominaRepository.findByPeriodoInicioBetween(inicio, fin);
    }

    // Cambiar estatus de nómina
    public Nomina cambiarEstatus(Long id, String estatus) {
        Nomina nomina = nominaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nómina no encontrada con ID: " + id));

        // Validar que el estatus sea válido
        List<String> estatusValidos = List.of("CALCULADA", "PAGADA", "CANCELADA", "BORRADOR");
        if (!estatusValidos.contains(estatus.toUpperCase())) {
            throw new IllegalArgumentException("Estatus no válido. Valores permitidos: " + estatusValidos);
        }

        nomina.setEstatusNomina(estatus);
        return nominaRepository.save(nomina);
    }

    // Obtener nóminas por estatus
    public List<Nomina> getNominasByEstatus(String estatus) {
        return nominaRepository.findByEstatusNomina(estatus);
    }

    // Verificar si existe nómina para un empleado en un período
    public boolean existsNominaForEmpleadoInPeriod(Long empleadoId, LocalDate inicio, LocalDate fin) {
        return nominaRepository.existsByEmpleadoIdAndPeriodoInicioBetween(
                empleadoId, inicio, fin);
    }

    // Obtener nóminas pendientes de pago
    public List<Nomina> getNominasPendientes() {
        return nominaRepository.findByEstatusNomina("CALCULADA");
    }

    // Obtener total de nóminas pagadas en un período
    public double getTotalPagadoEnPeriodo(LocalDate inicio, LocalDate fin) {
        List<Nomina> nominas = nominaRepository.findByEstatusNominaAndPeriodoInicioBetween(
                "PAGADA", inicio, fin);
        return nominas.stream()
                .mapToDouble(Nomina::getTotalNeto)
                .sum();
    }
}