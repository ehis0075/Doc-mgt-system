package com.doc.mgt.system.docmgt.tempStorage.service.implementation;

import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.document.service.DocumentService;
import com.doc.mgt.system.docmgt.image.service.ImageService;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempAction;
import com.doc.mgt.system.docmgt.tempStorage.model.Temp;
import com.doc.mgt.system.docmgt.tempStorage.service.TempActionService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TempActionServiceImpl implements TempActionService {

    private final Gson gson;
    private final ImageService imageService;
    private final ApplicationContext applicationContext;

    public TempActionServiceImpl(Gson gson, ImageService imageService, ApplicationContext applicationContext) {
        this.gson = gson;
        this.imageService = imageService;
        this.applicationContext = applicationContext;
    }

    @Override
    public DocumentDTO getDocumentData(String serviceName, Long oldDataId) {
        DocumentService documentService = getDocumentService(serviceName);
        return documentService.getDocumentDTO(oldDataId);
    }

    @Override
    public void performActionForDocument(Temp temp) {
        DocumentService documentService = getDocumentService(temp.getServiceName());

        if (Objects.requireNonNull(temp.getAction()) == TempAction.CREATE) {
            UploadDocumentDTO uploadDocumentDTO = gson.fromJson(temp.getNewData(), UploadDocumentDTO.class);
            documentService.uploadDocument(uploadDocumentDTO, "");
        }
    }

    private DocumentService getDocumentService(String serviceName) {
        log.info("Getting {} service from context", serviceName);
        return applicationContext.getBean(serviceName, DocumentService.class);
    }


}
