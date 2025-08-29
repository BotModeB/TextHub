package com.TextHub.TextHub.Repository;
import com.TextHub.TextHub.Entity.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long>{
    List<Comments> findByPostId(Long id);
    Optional<Comments> findById(Long id);
}
