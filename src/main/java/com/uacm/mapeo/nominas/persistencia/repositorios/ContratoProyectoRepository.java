package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.ContratoProyecto;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratoProyectoRepository extends JpaRepository<ContratoProyecto, Long> {

    // Buscar contratos por empleado (usando la relación)
    List<ContratoProyecto> findByEmpleadoId(Long empleadoId);

    // Buscar contratos por proyecto (usando la relación)
    List<ContratoProyecto> findByProyectoId(Long proyectoId);

    // Buscar contratos por empleado y estatus
    @Query("SELECT c FROM ContratoProyecto c WHERE c.empleado.id = :empleadoId AND c.estatus = :estatus")
    List<ContratoProyecto> findByEmpleadoIdAndEstatus(
            @Param("empleadoId") Long empleadoId,
            @Param("estatus") EstatusContrato estatus
    );

    // Verificar si existe contrato vigente
    @Query("SELECT COUNT(c) > 0 FROM ContratoProyecto c WHERE c.empleado.id = :empleadoId AND c.proyecto.id = :proyectoId AND c.estatus = :estatus")
    boolean existsByEmpleadoIdAndProyectoIdAndEstatus(
            @Param("empleadoId") Long empleadoId,
            @Param("proyectoId") Long proyectoId,
            @Param("estatus") EstatusContrato estatus
    );

    // Contar contratos activos por proyecto
    @Query("SELECT COUNT(c) FROM ContratoProyecto c WHERE c.proyecto.id = :proyectoId AND c.estatus = :estatus")
    Long countByProyectoIdAndEstatus(
            @Param("proyectoId") Long proyectoId,
            @Param("estatus") EstatusContrato estatus
    );

    @Query("SELECT SUM(c.salarioAsignado) FROM ContratoProyecto c WHERE c.proyecto.id = :proyectoId AND c.estatus = :estatus")
    Double sumSalariosByProyectoAndEstatus(
            @Param("proyectoId") Long proyectoId,
            @Param("estatus") EstatusContrato estatus
    );
}