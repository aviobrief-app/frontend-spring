package com.petkov.spr_final_1.repository;

import com.petkov.spr_final_1.model.app_entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

}
