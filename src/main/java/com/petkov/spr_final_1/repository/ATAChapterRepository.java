package com.petkov.spr_final_1.repository;

import com.petkov.spr_final_1.model.aviation_library_entity.ATAChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ATAChapterRepository extends JpaRepository<ATAChapterEntity, Long> {

    Optional<ATAChapterEntity> findByAtaCode(Integer ataCode);

    Optional<ATAChapterEntity> findByName(String name);

}
