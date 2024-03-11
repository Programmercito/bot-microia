package org.osbo.microservice.model.services;

import org.osbo.microservice.model.entities.Servicios;
import org.osbo.microservice.model.repositories.ServicioRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiciosService {
    ServicioRepository servicioRepository;

    public ServiciosService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public void saveServicio(Servicios servicio) {
        servicioRepository.save(servicio);
    }

    public Servicios getServicio(Long id) {
        return servicioRepository.findById(id).orElse(null);
    }

    public void deleteServicio(Long id) {
        servicioRepository.deleteById(id);
    }

    public Servicios getServicioByUserAndService(Long iduser, String servicio) {
        return servicioRepository.findByIduserAndServicio(iduser, servicio);
    }
}