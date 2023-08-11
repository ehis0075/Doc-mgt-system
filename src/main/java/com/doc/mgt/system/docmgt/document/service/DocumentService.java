package com.doc.mgt.system.docmgt.document.service;


import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentListDTO;
import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;

public interface DocumentService {

    DocumentDTO uploadDocument(UploadDocumentDTO request, String username);

    DocumentDTO getDocumentDTO(Long id);

    DocumentListDTO getAllDocuments(PageableRequestDTO requestDTO);
}
