package com.uacm.mapeo.nominas.controller;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import com.uacm.mapeo.nominas.servicios.NominaService;
import org.springframework.http.MediaType;


import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/nominas")
public class NominaController {

    private final NominaService nominaService;
    private final XmlMapper xmlMapper = new XmlMapper();

    public NominaController(NominaService nominaService) {
        this.nominaService = nominaService;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Nomina n) {
        try {
            Nomina creado = nominaService.crearNomina(n);
            return ResponseEntity.status(201).body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error interno: " + ex.getMessage()));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Nomina> listar() {
        return nominaService.listarNominas();
    }

    @GetMapping("/{reciboId}")
    public ResponseEntity<?> obtener(@PathVariable String reciboId) {
        Nomina n = nominaService.obtenerPorReciboId(reciboId);
        if (n == null)
            return ResponseEntity.status(404).body(new ErrorResponse("No encontrado"));
        return ResponseEntity.ok(n);
    }

    @PutMapping("/{reciboId}")
    public ResponseEntity<?> actualizar(@PathVariable String reciboId,
                                        @RequestBody Nomina cambios) {
        try {
            Nomina actualizado = nominaService.actualizarNomina(reciboId, cambios);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(new ErrorResponse("Error interno: " + ex.getMessage()));
        }
    }

    @DeleteMapping("/{reciboId}")
    public ResponseEntity<?> eliminar(@PathVariable String reciboId) {
        boolean ok = nominaService.eliminarPorReciboId(reciboId);
        if (!ok)
            return ResponseEntity.status(404).body(new ErrorResponse("No encontrado"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reciboId}/xml")
    public ResponseEntity<?> exportarXml(@PathVariable String reciboId) {
        Nomina n = nominaService.obtenerPorReciboId(reciboId);
        if (n == null)
            return ResponseEntity.status(404).body(new ErrorResponse("No encontrado"));

        try {
            String xml = xmlMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(n);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.set(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=recibo_" + reciboId + ".xml"
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(xml.getBytes(StandardCharsets.UTF_8));

        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(new ErrorResponse(
                            "Error serializando a XML: " + ex.getMessage()
                    ));
        }
    }

    // Clase simple para errores
    static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}
