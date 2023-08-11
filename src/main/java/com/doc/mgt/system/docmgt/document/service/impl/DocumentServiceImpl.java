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
import com.doc.mgt.system.docmgt.user.model.AdminUser;
import com.doc.mgt.system.docmgt.user.service.UserService;
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

    private final UserService userService;

    private final GeneralService generalService;

    private final ImageService imageService;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentTypeRepository documentTypeRepository, UserService userService, GeneralService generalService, ImageService imageService) {
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.userService = userService;
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

        //validate file name
        boolean exist = documentRepository.existsByName(request.getFileName());

        if(exist){
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseMessage, "file name already");
        }

        // get Doc type
        Optional<DocumentType> documentType = documentTypeRepository.findById(request.getDocumentTypId());

        if (!documentType.isPresent()) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseMessage, "DocumentType cannot be null");
        }

        // get user
        AdminUser user = userService.getUserForLogin(username);

        Document document = new Document();
        document.setType(documentType.get());
        document.setName(request.getFileName());
        document.setAdminUser(user);

        // upload document
        migrateTempDocument(document, request.getBase64Image());

        document = documentRepository.save(document);

        // get document dto
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
        log.info("Getting Document List ");

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
            image = image.replace("TEMP", "DOC");
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
