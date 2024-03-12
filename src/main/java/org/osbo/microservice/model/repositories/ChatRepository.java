package org.osbo.microservice.model.repositories;

import org.osbo.microservice.model.entities.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository  extends JpaRepository<Chats, Long>{
    public Chats findByIduserAndTipo(Long iduser, String tipo);

}
