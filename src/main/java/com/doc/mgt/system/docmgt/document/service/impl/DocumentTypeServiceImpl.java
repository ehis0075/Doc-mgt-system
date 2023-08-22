package com.doc.mgt.system.docmgt.document.service.impl;

import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeListDTO;
import com.doc.mgt.system.docmgt.document.model.DocumentType;
import com.doc.mgt.system.docmgt.document.repository.DocumentTypeRepository;
import com.doc.mgt.system.docmgt.document.service.DocumentTypeService;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final GeneralService generalService;

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(GeneralService generalService, DocumentTypeRepository documentTypeRepository) {
        this.generalService = generalService;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public DocumentTypeDTO save(CreateDocumentTypeDTO request, String user) {
        log.info("Request to save Document Type with request {} by user {}", request, user);

        DocumentType documentType = new DocumentType();
        documentType.setType(request.getType());

        documentType = documentTypeRepository.save(documentType);

        return getDocumentTypeDTO(documentType);
    }

    @Override
    public DocumentTypeListDTO getAll(PageableRequestDTO requestDTO) {
        log.info("Getting DocumentType List");

        Pageable paged = generalService.getPageableObject(requestDTO.getSize(), requestDTO.getPage());
        Page<DocumentType> superUserPage = documentTypeRepository.findAll(paged);

        DocumentTypeListDTO documentTypeListDTO = new DocumentTypeListDTO();

        List<DocumentType> documentTypeList = superUserPage.getContent();
        if (superUserPage.getContent().size() > 0) {
            documentTypeListDTO.setHasNextRecord(superUserPage.hasNext());
            documentTypeListDTO.setTotalCount((int) superUserPage.getTotalElements());
        }

        List<DocumentTypeDTO> documentTypeDTOList = convertToDocumentTypeDTOList(documentTypeList);
        documentTypeListDTO.setDocumentTypeList(documentTypeDTOList);

        return documentTypeListDTO;
    }

    private List<DocumentTypeDTO> convertToDocumentTypeDTOList(List<DocumentType> documentTypeList) {
        log.info("Converting DocumentType List to DocumentType DTO List");

        return documentTypeList.stream().map(this::getDocumentTypeDTO).collect(Collectors.toList());
    }


    public DocumentTypeDTO getDocumentTypeDTO(DocumentType documentType) {

        DocumentTypeDTO dto = new DocumentTypeDTO();
        dto.setId(documentType.getId());
        dto.setType(documentType.getType());

        return dto;
    }
}
