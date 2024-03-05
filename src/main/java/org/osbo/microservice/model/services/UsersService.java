package org.osbo.microservice.model.services;

import java.util.Optional;

import org.osbo.microservice.model.entities.Users;
import org.osbo.microservice.model.repositories.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void saveUser(Users user) {
        usersRepository.save(user);
    }

    public Users getUser(Long id) {
        Optional<Users> us = usersRepository.findById(id);
        return us.orElse(null);
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

}
