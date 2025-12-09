package com.uacm.mapeo.nominas.servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uacm.mapeo.nominas.persistencia.entidades.*;
import com.uacm.mapeo.nominas.persistencia.repositorios.*;

import java.util.List;
import java.util.Optional;

@Service
public class NominaService {

    private final NominaRepo nominaRepo;
    private final EmpleadoRepo empleadoRepo;

    public NominaService(NominaRepo nominaRepo, EmpleadoRepo empleadoRepo) {
        this.nominaRepo = nominaRepo;
        this.empleadoRepo = empleadoRepo;
    }

    private double calcularTotalNeto(Nomina n) {
        double base = n.getDevengado() != null ? n.getDevengado().getBaseAmount() : 0.0;
        double extras = n.getDevengado() != null ? n.getDevengado().getExtras() : 0.0;
        double isr = n.getDeducciones() != null ? n.getDeducciones().getIsr() : 0.0;
        return (base + extras) - isr;
    }

    private void validar(Nomina n) {
        if (n.getReciboId() == null || n.getReciboId().trim().isEmpty()) {
            throw new IllegalArgumentException("reciboId es obligatorio");
        }
        if (n.getEmpleado() == null) {
            throw new IllegalArgumentException("empleado es obligatorio");
        }
        if (n.getPeriodo() == null || n.getPeriodo().trim().isEmpty()) {
            throw new IllegalArgumentException("periodo es obligatorio");
        }

        double base = n.getDevengado() != null ? n.getDevengado().getBaseAmount() : 0.0;
        double extras = n.getDevengado() != null ? n.getDevengado().getExtras() : 0.0;
        double isr = n.getDeducciones() != null ? n.getDeducciones().getIsr() : 0.0;

        if (base <= 0)
            throw new IllegalArgumentException("devengado.base debe ser mayor a 0");
        if (extras < 0)
            throw new IllegalArgumentException("devengado.extras no puede ser negativo");
        if (isr < 0)
            throw new IllegalArgumentException("deducciones.isr no puede ser negativo");
    }

    // ================== CREAR ==================
    @Transactional
    public Nomina crearNomina(Nomina n) {

        if (nominaRepo.existsByReciboId(n.getReciboId())) {
            throw new IllegalArgumentException("reciboId ya existe");
        }

        Empleado emp = n.getEmpleado();

        if (emp.getId() != null) {
            emp = empleadoRepo.findById(emp.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado por id"));

        } else if (emp.getNumeroEmpleado() != null) {

            Optional<Empleado> ex = empleadoRepo.findByNumeroEmpleado(emp.getNumeroEmpleado());
            if (ex.isPresent()) {
                emp = ex.get();
            } else {
                emp = empleadoRepo.save(emp);
            }

        } else {
            emp = empleadoRepo.save(emp);
        }

        n.setEmpleado(emp);

        validar(n);
        n.setTotalNeto(calcularTotalNeto(n));

        return nominaRepo.save(n);
    }

    // ================== LISTAR ==================
    public List<Nomina> listarNominas() {
        return nominaRepo.findAll();
    }

    // ================== OBTENER ==================
    public Nomina obtenerPorReciboId(String reciboId) {
        return nominaRepo.findByReciboId(reciboId).orElse(null);
    }

    // ================== ACTUALIZAR ==================
    @Transactional
    public Nomina actualizarNomina(String reciboId, Nomina cambios) {

        Nomina existente = nominaRepo.findByReciboId(reciboId)
                .orElseThrow(() -> new IllegalArgumentException("No encontrado"));

        // No permitir modificar totalNeto manualmente
        cambios.setTotalNeto(0);

        if (cambios.getPeriodo() != null)
            existente.setPeriodo(cambios.getPeriodo());

        if (cambios.getDevengado() != null)
            existente.setDevengado(cambios.getDevengado());

        if (cambios.getDeducciones() != null)
            existente.setDeducciones(cambios.getDeducciones());

        // Resolver empleado
        if (cambios.getEmpleado() != null) {

            Empleado e = cambios.getEmpleado();

            if (e.getId() != null) {
                e = empleadoRepo.findById(e.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

            } else if (e.getNumeroEmpleado() != null) {

                Optional<Empleado> ex = empleadoRepo.findByNumeroEmpleado(e.getNumeroEmpleado());
                if (ex.isPresent()) {
                    e = ex.get();
                } else {
                    e = empleadoRepo.save(e);
                }

            } else {
                e = empleadoRepo.save(e);
            }

            existente.setEmpleado(e);
        }

        validar(existente);
        existente.setTotalNeto(calcularTotalNeto(existente));

        return nominaRepo.save(existente);
    }

    // ================== ELIMINAR ==================
    @Transactional
    public boolean eliminarPorReciboId(String reciboId) {
        if (!nominaRepo.existsByReciboId(reciboId))
            return false;

        nominaRepo.deleteByReciboId(reciboId);
        return true;
    }
}
