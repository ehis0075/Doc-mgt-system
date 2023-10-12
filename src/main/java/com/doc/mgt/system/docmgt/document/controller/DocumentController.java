package com.doc.mgt.system.docmgt.document.controller;

import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentListDTO;
import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.document.model.Document;
import com.doc.mgt.system.docmgt.document.service.DocumentService;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import com.doc.mgt.system.docmgt.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = {"http://*", "https://*", "https://document-management.vercel.app/", "https://document-management.vercel.app", "http://127.0.0.1:5500/", "http://127.0.0.1:5500", "http://localhost:5500/"}, maxAge = 3600)
public class DocumentController {

    private final DocumentService documentService;
    private final GeneralService generalService;

    private final UserService userService;

    public DocumentController(DocumentService documentService, GeneralService generalService, UserService userService) {
        this.documentService = documentService;
        this.generalService = generalService;
        this.userService = userService;
    }

    //    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping("/create")
    public Response create(@RequestBody UploadDocumentDTO request) {

        String user = userService.getLoggedInUser();

        log.info("request to upload a document of type {} and file name {}, by {}", request.getDocumentTypId(), request.getFileName(), user);
        Map<String, String> result = uploadDocument(request);

        DocumentDTO data = documentService.saveDocument(request, user, result);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);

    }

    //    @PreAuthorize("hasAuthority('VIEW_DOCUMENT')")
    @PostMapping()
    public Response getAllDocuments(@RequestBody PageableRequestDTO request) {

        DocumentListDTO data = documentService.getAllDocuments(request);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);

    }

    @PostMapping("getAll/{status}")
    public Response getDocumentListByStatus(@RequestBody PageableRequestDTO request, @PathVariable TempStatus status) {

        String user = userService.getLoggedInUser();

        DocumentListDTO data = documentService.getDocumentListByStatus(status, request, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);

    }

    private Map<String, String> uploadDocument(UploadDocumentDTO request) {
        Map<String, String> fileIdAndImage = generalService.uploadImageToTemp(request.getBase64Image(), request.getFileName());
        if (Objects.nonNull(fileIdAndImage)) {
            log.info("uploading document");

            return fileIdAndImage;
        } else {
            if (Objects.nonNull(request.getBase64Image()) && request.getBase64Image().length() > 200) {
                request.setBase64Image(null);
            }
            return null;
        }
    }
}
