package com.uacm.mapeo.nominas.persistencia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uacm.mapeo.nominas.persistencia.entidades.Nomina;
import java.util.Optional;

public interface NominaRepo extends JpaRepository<Nomina, Long> {
    Optional<Nomina> findByReciboId(String reciboId);
    void deleteByReciboId(String reciboId);
    boolean existsByReciboId(String reciboId);
}
