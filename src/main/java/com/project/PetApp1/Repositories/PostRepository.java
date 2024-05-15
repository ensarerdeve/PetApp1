package com.project.PetApp1.Repositories;

import com.project.PetApp1.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query(value = "select id from post where user_id = :userId order by create_date desc limit 5",
            nativeQuery = true)
    List<Long> findTopByUserId(@Param("userId") Long userId);

}
