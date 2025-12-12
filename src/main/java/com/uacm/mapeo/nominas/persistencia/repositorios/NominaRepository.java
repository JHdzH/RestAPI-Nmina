package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, Long> {

    // ========== MÉTODOS BÁSICOS DE BÚSQUEDA ==========

    // Buscar nóminas por empleado
    List<Nomina> findByEmpleadoId(Long empleadoId);

    // Buscar nóminas por periodo (formato: "2024-12")
    List<Nomina> findByPeriodo(String periodo);

    // Buscar nómina por recibo ID
    Optional<Nomina> findByReciboId(String reciboId);

    // Verificar si existe nómina por recibo ID
    boolean existsByReciboId(String reciboId);

    // Buscar nóminas por estatus
    List<Nomina> findByEstatusNomina(String estatusNomina);

    // Buscar nóminas por tipo de nómina
    List<Nomina> findByTipoNomina(String tipoNomina);

    // Buscar nóminas por fecha de pago
    List<Nomina> findByFechaPago(LocalDate fechaPago);

    // Buscar nóminas por fecha de pago entre fechas
    List<Nomina> findByFechaPagoBetween(LocalDate inicio, LocalDate fin);

    // ========== MÉTODOS POR RANGO DE FECHAS ==========

    // Método 1: Usando Between (recomendado para tu servicio)
    List<Nomina> findByPeriodoInicioBetween(LocalDate inicio, LocalDate fin);

    // Método 2: Usando consulta JPQL personalizada
    @Query("SELECT n FROM Nomina n WHERE n.periodoInicio >= :inicio AND n.periodoFin <= :fin ORDER BY n.periodoInicio DESC")
    List<Nomina> findByPeriodoInicioAndPeriodoFin(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // ========== MÉTODOS COMBINADOS ==========

    // Buscar nóminas por empleado y período
    @Query("SELECT n FROM Nomina n WHERE n.empleado.id = :empleadoId " +
            "AND n.periodoInicio >= :inicio AND n.periodoFin <= :fin " +
            "ORDER BY n.periodoInicio DESC")
    List<Nomina> findByEmpleadoIdAndPeriodo(
            @Param("empleadoId") Long empleadoId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // Buscar nóminas que contengan un empleado específico y estén en cierto estatus
    @Query("SELECT n FROM Nomina n WHERE n.empleado.id = :empleadoId " +
            "AND n.estatusNomina = :estatus " +
            "ORDER BY n.fechaPago DESC")
    List<Nomina> findByEmpleadoIdAndEstatusNomina(
            @Param("empleadoId") Long empleadoId,
            @Param("estatus") String estatus
    );

    // Método requerido por NominaService: verificar si existe nómina para empleado en período
    @Query("SELECT COUNT(n) > 0 FROM Nomina n WHERE n.empleado.id = :empleadoId " +
            "AND n.periodoInicio >= :inicio AND n.periodoFin <= :fin")
    boolean existsByEmpleadoIdAndPeriodoInicioBetween(
            @Param("empleadoId") Long empleadoId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // Método requerido por NominaService: nóminas por estatus y período
    @Query("SELECT n FROM Nomina n WHERE n.estatusNomina = :estatus " +
            "AND n.periodoInicio >= :inicio AND n.periodoFin <= :fin")
    List<Nomina> findByEstatusNominaAndPeriodoInicioBetween(
            @Param("estatus") String estatus,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // ========== MÉTODOS ESPECIALIZADOS ==========

    // Buscar nóminas por proyecto (si aplica)
    @Query("SELECT n FROM Nomina n WHERE n.proyecto.id = :proyectoId")
    List<Nomina> findByProyectoId(@Param("proyectoId") Long proyectoId);

    // Buscar nóminas que tengan horas extras
    @Query("SELECT n FROM Nomina n WHERE n.horasExtras > 0")
    List<Nomina> findNominasConHorasExtras();

    // ========== MÉTODOS DE CONSULTA ==========

    // Contar nóminas por período
    Long countByPeriodoStartingWith(String periodoPrefijo);

    // Contar nóminas por empleado
    Long countByEmpleadoId(Long empleadoId);

    // Contar nóminas por estatus
    Long countByEstatusNomina(String estatusNomina);

    // Sumar total neto por empleado en un período
    @Query("SELECT COALESCE(SUM(n.totalNeto), 0) FROM Nomina n " +
            "WHERE n.empleado.id = :empleadoId " +
            "AND n.periodoInicio >= :inicio AND n.periodoFin <= :fin")
    Double sumTotalNetoByEmpleadoAndPeriodo(
            @Param("empleadoId") Long empleadoId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // ========== MÉTODOS PARA GENERACIÓN DE ID ==========

    // Obtener el último recibo ID por prefijo (para generación automática)
    @Query(value = "SELECT n.recibo_id FROM nominas n WHERE n.recibo_id LIKE :prefijo% ORDER BY n.recibo_id DESC LIMIT 1",
            nativeQuery = true)
    Optional<String> findUltimoReciboIdByPrefijo(@Param("prefijo") String prefijo);

    // Obtener el máximo ID de recibo para un prefijo (alternativa)
    @Query("SELECT MAX(n.reciboId) FROM Nomina n WHERE n.reciboId LIKE :prefijo%")
    Optional<String> findMaxReciboIdByPrefijo(@Param("prefijo") String prefijo);

    // ========== MÉTODOS ADICIONALES PARA REPORTES ==========

    // Buscar nóminas por mes y año
    @Query("SELECT n FROM Nomina n WHERE YEAR(n.periodoInicio) = :year AND MONTH(n.periodoInicio) = :month")
    List<Nomina> findByYearAndMonth(
            @Param("year") int year,
            @Param("month") int month
    );

    // Sumar total neto por período
    @Query("SELECT COALESCE(SUM(n.totalNeto), 0) FROM Nomina n " +
            "WHERE n.periodoInicio >= :inicio AND n.periodoFin <= :fin")
    Double sumTotalNetoByPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // Obtener estadísticas por empleado en período
    @Query("SELECT n.empleado.id, SUM(n.totalNeto), AVG(n.totalNeto), COUNT(n) " +
            "FROM Nomina n " +
            "WHERE n.periodoInicio >= :inicio AND n.periodoFin <= :fin " +
            "GROUP BY n.empleado.id")
    List<Object[]> getEstadisticasPorEmpleadoEnPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // ========== MÉTODOS DE ELIMINACIÓN ==========

    // Eliminar por recibo ID
    void deleteByReciboId(String reciboId);

    // Eliminar nóminas por empleado
    void deleteByEmpleadoId(Long empleadoId);

    // Eliminar nóminas por período
    @Query("DELETE FROM Nomina n WHERE n.periodoInicio >= :inicio AND n.periodoFin <= :fin")
    void deleteByPeriodoInicioBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}