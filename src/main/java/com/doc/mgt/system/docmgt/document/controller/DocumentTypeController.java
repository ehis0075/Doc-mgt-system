package com.doc.mgt.system.docmgt.document.controller;


import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeListDTO;
import com.doc.mgt.system.docmgt.document.service.DocumentTypeService;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/documentTypes")
@CrossOrigin(origins = {"http://*", "https://*", "https://document-management.vercel.app/", "https://document-management.vercel.app", "http://127.0.0.1:5500/", "http://127.0.0.1:5500", "http://localhost:5500/"}, maxAge = 3600)
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    private final UserService userService;

    private final GeneralService generalService;

    public DocumentTypeController(DocumentTypeService documentTypeService, UserService userService, GeneralService generalService) {
        this.documentTypeService = documentTypeService;
        this.userService = userService;
        this.generalService = generalService;
    }

    //    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping("/create")
    public Response createDocumentType(@RequestBody CreateDocumentTypeDTO requestDTO) {

        String user = userService.getLoggedInUser();

        DocumentTypeDTO data = documentTypeService.save(requestDTO, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    //    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping()
    public Response getAll(@RequestBody PageableRequestDTO requestDTO) {

        DocumentTypeListDTO data = documentTypeService.getAll(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
