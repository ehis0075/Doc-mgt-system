package com.doc.mgt.system.docmgt.document.service.impl;


import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentListDTO;
import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.document.model.Document;
import com.doc.mgt.system.docmgt.document.model.DocumentType;
import com.doc.mgt.system.docmgt.document.repository.DocumentRepository;
import com.doc.mgt.system.docmgt.document.repository.DocumentTypeRepository;
import com.doc.mgt.system.docmgt.document.service.DocumentService;
import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.image.service.ImageService;
import com.doc.mgt.system.docmgt.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentTypeRepository documentTypeRepository;

    private final GeneralService generalService;

    private final ImageService imageService;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentTypeRepository documentTypeRepository, GeneralService generalService, ImageService imageService) {
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.generalService = generalService;
        this.imageService = imageService;
    }

    @Override
    public DocumentDTO uploadDocument(UploadDocumentDTO request, String username) {
        log.info("Request to upload document {}", request.getFileName());

        // check null values
        if (GeneralUtil.stringIsNullOrEmpty(request.getFileName())) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseMessage, "file name cannot be null");
        }

        if (GeneralUtil.stringIsNullOrEmpty(request.getBase64Image())) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseMessage, "base 64 cannot be null");
        }

        // get Doc type
        Optional<DocumentType> documentType = documentTypeRepository.findById(request.getDocumentTypId());

        if (!documentType.isPresent()) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseMessage, "DocumentType cannot be null");
        }

        Document document = new Document();
        document.setType(documentType.get());
        document.setName(request.getFileName());

        // upload doc
        migrateTempDocument(document, document.getUrl());

//        document.setFileId(fileId);
//        document.setUrl(url);


        document = documentRepository.save(document);

        // get doc dto

        return getDocumentDTO(document.getId());
    }

    @Override
    public DocumentDTO getDocumentDTO(Long id) {
        log.info("Getting Document using document ID => {}", id);

        Document document = getDocumentById(id);

        return Document.getDocumentDTO(document);
    }

    @Override
    public DocumentListDTO getAllDocuments(PageableRequestDTO requestDTO) {
        log.info("Getting Biller List ");

        Pageable paged = generalService.getPageableObject(requestDTO.getSize(), requestDTO.getPage());
        Page<Document> billerPage = documentRepository.findAll(paged);

        return getDocumentListDTO(billerPage);
    }

    private Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.DOCUMENT_NOT_FOUND_89));
    }

    private void migrateTempDocument(Document document, String image) {
        boolean result = imageService.moveFileFromTemp(image);

        if (result) {
            image = image.replace("TEMP", "VAS");
            document.setUrl(image);
        }
    }

    private DocumentListDTO getDocumentListDTO(Page<Document> documentPage) {
        DocumentListDTO listDTO = new DocumentListDTO();

        List<Document> documentList = documentPage.getContent();
        if (!documentPage.getContent().isEmpty()) {
            listDTO.setHasNextRecord(documentPage.hasNext());
            listDTO.setTotalCount((int) documentPage.getTotalElements());
        }

        List<DocumentDTO> billerDTOList = convertToDocumentDTOList(documentList);
        listDTO.setDocumentDTOList(billerDTOList);

        return listDTO;
    }

    private List<DocumentDTO> convertToDocumentDTOList(List<Document> documentList) {
        log.info("Converting Document List to Document DTO List");

        return documentList.stream().map(Document::getDocumentDTO).collect(Collectors.toList());
    }
}
