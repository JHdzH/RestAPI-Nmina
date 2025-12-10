package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.Empleado;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.EstatusEmpleado;
import com.uacm.mapeo.nominas.persistencia.entidades.enums.TipoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    // Buscar por tipo de contrato
    List<Empleado> findByTipoContrato(TipoContrato tipoContrato);

    // Buscar por estatus
    List<Empleado> findByEstatus(EstatusEmpleado estatus);

    // Método para buscar por número de empleado
    Optional<Empleado> findByNumeroEmpleado(String numeroEmpleado);

    // Método para buscar por RFC
    Optional<Empleado> findByRfc(String rfc);

    // Buscar empleados activos
    List<Empleado> findByEstatusAndTipoContrato(EstatusEmpleado estatus, TipoContrato tipoContrato);

    // Buscar por email
    Empleado findByEmail(String email);
}