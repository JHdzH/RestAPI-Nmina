// src/main/java/com/uacm/mapeo/nominas/controller/ProyectoController.java
package com.uacm.mapeo.nominas.controller;

import com.uacm.mapeo.nominas.persistencia.entidades.Proyecto;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusProyecto;
import com.uacm.mapeo.nominas.persistencia.repositorios.ContratoProyectoRepository;
import com.uacm.mapeo.nominas.persistencia.repositorios.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoRepository proyectoRepository;
    private final ContratoProyectoRepository contratoRepository;

    @GetMapping
    public List<Proyecto> getAllProyectos() {
        return proyectoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable Long id) {
        return proyectoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProyecto(@RequestBody Proyecto proyecto) {
        // Validación básica
        if (proyecto.getClave() == null || proyecto.getClave().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "La clave del proyecto es requerida"));
        }

        // Validar clave única
        if (proyectoRepository.findByClave(proyecto.getClave()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "La clave del proyecto ya existe"));
        }

        try {
            Proyecto savedProyecto = proyectoRepository.save(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProyecto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear proyecto: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(
            @PathVariable Long id,
            @RequestBody Proyecto proyectoDetails) {

        return proyectoRepository.findById(id)
                .map(proyecto -> {
                    if (proyectoDetails.getNombre() != null) {
                        proyecto.setNombre(proyectoDetails.getNombre());
                    }
                    if (proyectoDetails.getDescripcion() != null) {
                        proyecto.setDescripcion(proyectoDetails.getDescripcion());
                    }
                    if (proyectoDetails.getCliente() != null) {
                        proyecto.setCliente(proyectoDetails.getCliente());
                    }
                    if (proyectoDetails.getPresupuestoTotal() != null) {
                        proyecto.setPresupuestoTotal(proyectoDetails.getPresupuestoTotal());
                    }
                    if (proyectoDetails.getPresupuestoNomina() != null) {
                        proyecto.setPresupuestoNomina(proyectoDetails.getPresupuestoNomina());
                    }
                    if (proyectoDetails.getEstatus() != null) {
                        proyecto.setEstatus(proyectoDetails.getEstatus());
                    }
                    if (proyectoDetails.getFechaFinReal() != null) {
                        proyecto.setFechaFinReal(proyectoDetails.getFechaFinReal());
                    }

                    Proyecto updated = proyectoRepository.save(proyecto);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProyecto(@PathVariable Long id) {
        if (proyectoRepository.existsById(id)) {
            proyectoRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Proyecto eliminado exitosamente");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/empleados")
    public ResponseEntity<?> getEmpleadosByProyecto(@PathVariable Long id) {
        if (!proyectoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var contratos = contratoRepository.findByProyectoId(id);
        var respuesta = contratos.stream()
                .map(contrato -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("contratoId", contrato.getId());
                    item.put("empleadoId", contrato.getEmpleado() != null ? contrato.getEmpleado().getId() : null);
                    item.put("empleadoNombre", contrato.getEmpleado() != null ? contrato.getEmpleado().getNombre() : null);
                    item.put("empleadoNumero", contrato.getEmpleado() != null ? contrato.getEmpleado().getNumeroEmpleado() : null);
                    item.put("salarioAsignado", contrato.getSalarioAsignado());
                    item.put("fechaInicio", contrato.getFechaInicio());
                    item.put("fechaFin", contrato.getFechaFin());
                    item.put("estatusContrato", contrato.getEstatus());
                    item.put("horasContratadas", contrato.getHorasContratadas());
                    return item;
                })
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/resumen")
    public ResponseEntity<?> getResumenProyecto(@PathVariable Long id) {
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(id);
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proyecto proyecto = proyectoOpt.get();
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("proyecto", proyecto);

        // Estadísticas de contratos
        long totalContratos = contratoRepository.countByProyectoIdAndEstatus(
                id, com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusContrato.VIGENTE);

        // Calcular suma manualmente
        var contratos = contratoRepository.findByProyectoId(id);
        double totalSalarios = contratos.stream()
                .filter(c -> c.getEstatus() == com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusContrato.VIGENTE)
                .mapToDouble(c -> c.getSalarioAsignado() != null ? c.getSalarioAsignado() : 0.0)
                .sum();

        resumen.put("contratosActivos", totalContratos);
        resumen.put("totalSalariosMensual", totalSalarios);
        resumen.put("presupuestoRestante",
                proyecto.getPresupuestoNomina() != null ?
                        proyecto.getPresupuestoNomina() - totalSalarios : 0.0);

        return ResponseEntity.ok(resumen);
    }
}