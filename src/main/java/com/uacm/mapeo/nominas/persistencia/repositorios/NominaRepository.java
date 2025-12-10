package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, Long> {

    // Buscar nóminas por empleado
    List<Nomina> findByEmpleadoId(Long empleadoId);

    // Buscar nóminas por periodo
    List<Nomina> findByPeriodo(String periodo);

    // Buscar nómina por recibo ID - IMPORTANTE: Debe retornar Optional
    Optional<Nomina> findByReciboId(String reciboId);

    // Verificar si existe nómina por recibo ID
    boolean existsByReciboId(String reciboId);

    // Eliminar por recibo ID
    void deleteByReciboId(String reciboId);
}