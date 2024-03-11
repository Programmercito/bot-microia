package org.osbo.microservice.model.repositories;

import org.osbo.microservice.model.entities.Servicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository  extends JpaRepository<Servicios, Long>{
    public Servicios findByIduserAndServicio(Long iduser, String servicio);

}
