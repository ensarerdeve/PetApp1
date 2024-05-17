package com.project.PetApp1.Repositories;

import com.project.PetApp1.Models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository <Pet, Long> {
    List<Pet> findByUserId(Long userId);
    Pet findPetById(Long id);
}
