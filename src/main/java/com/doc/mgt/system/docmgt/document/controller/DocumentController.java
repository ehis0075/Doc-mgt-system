package com.doc.mgt.system.docmgt.document.controller;

import com.doc.mgt.system.docmgt.document.dto.DocumentListDTO;
import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.document.service.DocumentService;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempResponseDTO;
import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempAction;
import com.doc.mgt.system.docmgt.tempStorage.service.TempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final TempService tempService;
    private final DocumentService documentService;
    private final GeneralService generalService;

    public DocumentController(TempService tempService, DocumentService documentService, GeneralService generalService) {
        this.tempService = tempService;
        this.documentService = documentService;
        this.generalService = generalService;
    }

//    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping("/create")
    public Response create(@RequestBody UploadDocumentDTO request, Principal principal) {

        String user = principal.getName();

        log.info("request to create upload a doc with payload ----> {}, by {}", request.getDocumentTypId(), user);
        String fileId = uploadDocToTemp(request);

        TempResponseDTO data = tempService.saveToTemp(request, null, TempAction.CREATE, TableName.DOCUMENT, documentService, user, fileId);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);

    }

//    @PreAuthorize("hasAuthority('VIEW_DOCUMENT')")
    @PostMapping()
    public Response getAllDocuments(@RequestBody PageableRequestDTO request, Principal principal) {

        String user = principal.getName();
        DocumentListDTO data = documentService.getAllDocuments(request);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);

    }

    private String uploadDocToTemp(UploadDocumentDTO request) {
        Map<String, String> fileIdAndImage = generalService.uploadImageToTemp(request.getBase64Image(), request.getFileName());
        if (Objects.nonNull(fileIdAndImage)) {
            log.info("uploading document to temp");

            String fieldId = fileIdAndImage.get("fileId");
            String url = fileIdAndImage.get("url");

            request.setBase64Image(url);

            return fieldId;
        } else {
            if (Objects.nonNull(request.getBase64Image()) && request.getBase64Image().length() > 200) {
                request.setBase64Image(null);
            }
            return null;
        }
    }
}
