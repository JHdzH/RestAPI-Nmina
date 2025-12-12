package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.TablaISR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TablaISRRepository extends JpaRepository<TablaISR, Long> {

    @Query("SELECT t FROM TablaISR t WHERE :ingreso >= t.limiteInferior AND :ingreso <= t.limiteSuperior " +
            "AND t.tipo = :tipo AND t.vigenciaInicio <= :fecha " +
            "AND (t.vigenciaFin IS NULL OR t.vigenciaFin >= :fecha)")
    Optional<TablaISR> findByIngresoTipoYFecha(
            @Param("ingreso") Double ingreso,
            @Param("tipo") String tipo,
            @Param("fecha") LocalDate fecha
    );
}
