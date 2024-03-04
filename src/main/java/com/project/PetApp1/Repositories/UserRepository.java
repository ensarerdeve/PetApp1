package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);


    List<User> findByUserNameStartingWith(String username);
}
