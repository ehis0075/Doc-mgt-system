package com.doc.mgt.system.docmgt.document.repository;

import com.doc.mgt.system.docmgt.document.model.Document;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByName(String name);

    Page<Document> findAllByStatus(TempStatus status, Pageable pageable);

}
