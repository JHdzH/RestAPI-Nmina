// src/main/java/com/uacm/mapeo/nominas/controller/NominaController.java
package com.uacm.mapeo.nominas.controller;

import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import com.uacm.mapeo.nominas.servicios.NominaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nominas")
@RequiredArgsConstructor
public class NominaController {

    private final NominaService nominaService;

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

    // Endpoints adicionales por recibo ID
    @GetMapping("/recibo/{reciboId}")
    public ResponseEntity<Nomina> getNominaByReciboId(@PathVariable String reciboId) {
        Optional<Nomina> nomina = nominaService.getNominaByReciboId(reciboId);
        return nomina.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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

    // Endpoints de filtrado
    @GetMapping("/empleado/{empleadoId}")
    public List<Nomina> getNominasByEmpleado(@PathVariable Long empleadoId) {
        return nominaService.getNominasByEmpleado(empleadoId);
    }

    @GetMapping("/periodo/{periodo}")
    public List<Nomina> getNominasByPeriodo(@PathVariable String periodo) {
        return nominaService.getNominasByPeriodo(periodo);
    }
}