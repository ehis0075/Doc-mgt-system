package com.doc.mgt.system.docmgt.document.repository;

import com.doc.mgt.system.docmgt.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
