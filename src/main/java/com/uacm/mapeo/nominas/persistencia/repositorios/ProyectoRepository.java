// src/main/java/com/uacm/mapeo/nominas/persistencia/repositorios/ProyectoRepository.java
package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.Proyecto;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    // Buscar por clave única
    Optional<Proyecto> findByClave(String clave);

    // Buscar por estatus
    List<Proyecto> findByEstatus(EstatusProyecto estatus);

    // Buscar por cliente (búsqueda parcial case-insensitive)
    List<Proyecto> findByClienteContainingIgnoreCase(String cliente);

    // Buscar por nombre (búsqueda parcial)
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar proyectos en ejecución
    List<Proyecto> findByEstatusAndFechaFinEstimadaGreaterThanEqual(
            EstatusProyecto estatus,
            java.time.LocalDate fechaLimite
    );

    // Verificar si existe proyecto con clave
    boolean existsByClave(String clave);
}