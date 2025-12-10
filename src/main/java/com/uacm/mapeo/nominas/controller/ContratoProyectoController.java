package com.uacm.mapeo.nominas.controller;

import com.uacm.mapeo.nominas.persistencia.dto.ContratoRequest;
import com.uacm.mapeo.nominas.persistencia.entidades.ContratoProyecto;
import com.uacm.mapeo.nominas.persistencia.entidades.Empleado;
import com.uacm.mapeo.nominas.persistencia.entidades.Proyecto;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusContrato;
import com.uacm.mapeo.nominas.persistencia.repositorios.ContratoProyectoRepository;
import com.uacm.mapeo.nominas.persistencia.repositorios.EmpleadoRepository;
import com.uacm.mapeo.nominas.persistencia.repositorios.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contratos-proyecto")
@RequiredArgsConstructor
public class ContratoProyectoController {

    private final ContratoProyectoRepository contratoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProyectoRepository proyectoRepository;

    @GetMapping
    public List<ContratoProyecto> getAllContratos() {
        return contratoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoProyecto> getContratoById(@PathVariable Long id) {
        return contratoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearContrato(@RequestBody ContratoRequest request) {
        // Validar empleado
        Optional<Empleado> empleado = empleadoRepository.findById(request.getEmpleadoId());
        if (empleado.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Empleado no encontrado"));
        }

        // Validar proyecto
        Optional<Proyecto> proyecto = proyectoRepository.findById(request.getProyectoId());
        if (proyecto.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Proyecto no encontrado"));
        }

        // Validar fechas
        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "La fecha de inicio debe ser anterior a la fecha de fin"));
        }

        // Verificar si ya existe contrato vigente
        boolean existeContratoVigente = contratoRepository
                .existsByEmpleadoIdAndProyectoIdAndEstatus(
                        request.getEmpleadoId(),
                        request.getProyectoId(),
                        EstatusContrato.VIGENTE
                );

        if (existeContratoVigente) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ya existe un contrato vigente para este empleado en el proyecto"));
        }

        // Crear contrato
        ContratoProyecto contrato = ContratoProyecto.builder()
                .empleado(empleado.get())
                .proyecto(proyecto.get())
                .motivo(request.getMotivo())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .salarioAsignado(request.getSalarioAsignado())
                .horasContratadas(request.getHorasContratadas())
                .descripcionActividades(request.getDescripcionActividades())
                .build();

        ContratoProyecto savedContrato = contratoRepository.save(contrato);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedContrato);
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<ContratoProyecto> getContratosByEmpleado(@PathVariable Long empleadoId) {
        return contratoRepository.findByEmpleadoId(empleadoId);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public List<ContratoProyecto> getContratosByProyecto(@PathVariable Long proyectoId) {
        return contratoRepository.findByProyectoId(proyectoId);
    }
}