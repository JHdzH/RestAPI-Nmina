package com.uacm.mapeo.nominas.controller;

import com.uacm.mapeo.nominas.persistencia.entidades.Empleado;
import com.uacm.mapeo.nominas.persistencia.repositorios.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoRepository empleadoRepository;

    @GetMapping
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable Long id) {
        return empleadoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Empleado createEmpleado(@RequestBody Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(
            @PathVariable Long id,
            @RequestBody Empleado empleadoDetails) {

        return empleadoRepository.findById(id)
                .map(empleado -> {
                    if (empleadoDetails.getNombre() != null) {
                        empleado.setNombre(empleadoDetails.getNombre());
                    }
                    if (empleadoDetails.getPuesto() != null) {
                        empleado.setPuesto(empleadoDetails.getPuesto());
                    }
                    if (empleadoDetails.getNumeroEmpleado() != null) {
                        empleado.setNumeroEmpleado(empleadoDetails.getNumeroEmpleado());
                    }
                    if (empleadoDetails.getSalarioMensual() != null) {
                        empleado.setSalarioMensual(empleadoDetails.getSalarioMensual());
                    }
                    if (empleadoDetails.getTipoContrato() != null) {
                        empleado.setTipoContrato(empleadoDetails.getTipoContrato());
                    }
                    if (empleadoDetails.getEstatus() != null) {
                        empleado.setEstatus(empleadoDetails.getEstatus());
                    }

                    Empleado updated = empleadoRepository.save(empleado);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Método para buscar por RFC (corregido)
    @GetMapping("/rfc/{rfc}")
    public ResponseEntity<Empleado> getEmpleadoByRfc(@PathVariable String rfc) {
        Optional<Empleado> empleado = empleadoRepository.findByRfc(rfc);
        return empleado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}