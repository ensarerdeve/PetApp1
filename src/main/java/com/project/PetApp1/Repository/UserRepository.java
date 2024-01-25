package com.project.PetApp1.Repository;

import com.project.PetApp1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event;

public interface UserRepository extends JpaRepository<User, Long> {
}
