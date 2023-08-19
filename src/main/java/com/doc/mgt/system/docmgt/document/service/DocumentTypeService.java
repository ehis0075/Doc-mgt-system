package com.doc.mgt.system.docmgt.document.service;



import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeListDTO;
import com.doc.mgt.system.docmgt.document.model.DocumentType;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;

import java.util.List;

public interface DocumentTypeService {

    DocumentTypeDTO save(CreateDocumentTypeDTO request, String user);

    DocumentTypeListDTO getAll(PageableRequestDTO requestDTO);
}
