package com.doc.mgt.system.docmgt.document.service;


import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentListDTO;
import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.document.model.Document;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;

import java.util.Map;

public interface DocumentService {

    DocumentDTO saveDocument(UploadDocumentDTO request, String username, Map<String, String> result );

    DocumentDTO getDocumentDTO(Long id);

    DocumentListDTO getAllDocuments(PageableRequestDTO requestDTO);

    Document getDocumentById(Long id);
}
