package com.petkov.spr_final_1.repository;

import com.petkov.spr_final_1.model.aviation_library_entity.DocumentEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.DocumentSubchapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentSubchapterRepository extends JpaRepository<DocumentSubchapterEntity, Long> {

    Optional<DocumentSubchapterEntity> findByDocumentAndName(DocumentEntity document,
                                                                          String subchapterName);

}
