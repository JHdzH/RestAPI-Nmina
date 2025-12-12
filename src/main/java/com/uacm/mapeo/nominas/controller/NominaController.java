package com.uacm.mapeo.nominas.controller;

import com.uacm.mapeo.nominas.persistencia.dto.CalculoNominaRequest;
import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import com.uacm.mapeo.nominas.servicios.NominaService;
import com.uacm.mapeo.nominas.servicios.NominaCalculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/nominas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NominaController {

    private final NominaService nominaService;
    private final NominaCalculoService nominaCalculoService;

    // ========== CRUD BÁSICO ==========
    @GetMapping
    public List<Nomina> getAllNominas() {
        return nominaService.getAllNominas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nomina> getNominaById(@PathVariable Long id) {
        Optional<Nomina> nomina = nominaService.getNominaById(id);
        return nomina.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Nomina> createNomina(@RequestBody Nomina nomina) {
        try {
            Nomina created = nominaService.createNomina(nomina);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nomina> updateNomina(
            @PathVariable Long id,
            @RequestBody Nomina nominaDetails) {
        Optional<Nomina> updated = nominaService.updateNomina(id, nominaDetails);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNomina(@PathVariable Long id) {
        if (nominaService.deleteNomina(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== CÁLCULO DE NÓMINAS ==========
    @PostMapping("/calcular")
    public ResponseEntity<?> calcularNomina(@RequestBody CalculoNominaRequest request) {
        try {
            Nomina nominaCalculada = nominaCalculoService.calcularNomina(request);
            return ResponseEntity.ok(nominaCalculada);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al calcular nómina: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/calcular-masiva")
    public ResponseEntity<?> calcularNominaMasiva(
            @RequestParam String periodoInicio,
            @RequestParam String periodoFin,
            @RequestParam(defaultValue = "QUINCENAL") String tipoNomina) {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cálculo masivo programado para el período " + periodoInicio + " al " + periodoFin);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error en cálculo masivo: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ========== BÚSQUEDAS Y FILTROS ==========
    @GetMapping("/recibo/{reciboId}")
    public ResponseEntity<Nomina> getNominaByReciboId(@PathVariable String reciboId) {
        Optional<Nomina> nomina = nominaService.getNominaByReciboId(reciboId);
        return nomina.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<Nomina> getNominasByEmpleado(@PathVariable Long empleadoId) {
        return nominaService.getNominasByEmpleado(empleadoId);
    }

    @GetMapping("/periodo/{periodo}")
    public List<Nomina> getNominasByPeriodo(@PathVariable String periodo) {
        return nominaService.getNominasByPeriodo(periodo);
    }

    // ========== NUEVOS ENDPOINTS (que te faltan) ==========

    // 1. Obtener nóminas por rango de fechas
    @GetMapping("/por-fechas")
    public ResponseEntity<List<Nomina>> getNominasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            List<Nomina> nominas = nominaService.getNominasPorPeriodo(inicio, fin);
            return ResponseEntity.ok(nominas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 2. Obtener nóminas por mes y año
    @GetMapping("/mes/{year}/{month}")
    public ResponseEntity<List<Nomina>> getNominasPorMes(
            @PathVariable int year,
            @PathVariable int month) {
        try {
            List<Nomina> nominas = nominaService.getNominasPorMes(year, month);
            return ResponseEntity.ok(nominas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 3. Cambiar estatus de nómina
    @PutMapping("/{id}/estatus")
    public ResponseEntity<Nomina> cambiarEstatus(
            @PathVariable Long id,
            @RequestParam String estatus) {
        try {
            Nomina nominaActualizada = nominaService.cambiarEstatus(id, estatus);
            return ResponseEntity.ok(nominaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Obtener nóminas por estatus
    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Nomina>> getNominasByEstatus(@PathVariable String estatus) {
        try {
            List<Nomina> nominas = nominaService.getNominasByEstatus(estatus);
            return ResponseEntity.ok(nominas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== ACTUALIZACIONES POR RECIBO ID ==========
    @PutMapping("/recibo/{reciboId}")
    public ResponseEntity<Nomina> updateNominaByReciboId(
            @PathVariable String reciboId,
            @RequestBody Nomina nominaDetails) {
        Optional<Nomina> updated = nominaService.updateNominaByReciboId(reciboId, nominaDetails);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/recibo/{reciboId}")
    public ResponseEntity<?> deleteNominaByReciboId(@PathVariable String reciboId) {
        if (nominaService.deleteNominaByReciboId(reciboId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}