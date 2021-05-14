package com.petkov.spr_final_1.repository;

import com.petkov.spr_final_1.model.app_entity.CompletedTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedTestRepository extends JpaRepository<CompletedTestEntity, Long> {
}
