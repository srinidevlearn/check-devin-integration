package com.srinidevlearn.userservice.repository;

import com.srinidevlearn.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByRole(String role);

    User findByEmail(String email);
}
