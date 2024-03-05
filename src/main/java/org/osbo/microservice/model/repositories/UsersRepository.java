package org.osbo.microservice.model.repositories;

import org.springframework.stereotype.Repository;
import org.osbo.microservice.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
 
}
