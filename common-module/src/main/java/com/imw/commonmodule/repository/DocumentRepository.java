package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByUserId(Long userId);
    Document findByDocumentTypeAndUserId(String documentType, Long userId);

    void deleteAllByUserId(Long userId);
}
