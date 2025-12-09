package com.uacm.mapeo.nominas.persistencia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uacm.mapeo.nominas.persistencia.entidades.Empleado;
import java.util.Optional;

public interface EmpleadoRepo extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByNumeroEmpleado(String numeroEmpleado);
}
