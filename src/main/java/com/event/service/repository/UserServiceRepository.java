package com.event.service.repository;

import com.event.service.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserServiceRepository extends CrudRepository<User, String> {


    Optional<User> findById(String email);
}
