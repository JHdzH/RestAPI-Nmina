package com.uacm.mapeo.nominas.persistencia.repositorios;

import com.uacm.mapeo.nominas.persistencia.entidades.ParametroNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParametroNominaRepository extends JpaRepository<ParametroNomina, Long> {
    Optional<ParametroNomina> findByClave(String clave);
    Optional<ParametroNomina> findByClaveAndActivoTrue(String clave);
}