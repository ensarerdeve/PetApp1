package com.project.PetApp1.Repositories;


import com.project.PetApp1.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);
}
