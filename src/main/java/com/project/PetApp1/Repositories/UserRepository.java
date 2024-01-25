package com.project.PetApp1.Repositories;

import com.project.PetApp1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
