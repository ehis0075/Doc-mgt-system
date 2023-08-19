package com.doc.mgt.system.docmgt.tempStorage.service.implementation;

import com.doc.mgt.system.docmgt.document.model.Document;
import com.doc.mgt.system.docmgt.document.repository.DocumentRepository;
import com.doc.mgt.system.docmgt.document.service.DocumentService;
import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempPerformActionDTO;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempResponseDTO;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import com.doc.mgt.system.docmgt.tempStorage.service.TempService;
import com.doc.mgt.system.docmgt.user.model.AdminUser;
import com.doc.mgt.system.docmgt.user.service.UserService;
import com.doc.mgt.system.docmgt.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TempServiceImpl implements TempService {

    private final DocumentRepository documentRepository;
    private final UserService adminService;
    private final DocumentService documentService;

    public TempServiceImpl(DocumentRepository documentRepository, UserService adminService, DocumentService documentService) {
        this.documentRepository = documentRepository;
        this.adminService = adminService;
        this.documentService = documentService;
    }

    @Override
    public TempResponseDTO performTempActionForDoc(TempPerformActionDTO performActionDTO, Long docId, String username) {
        log.info("Request to approve document upload{}, by {}", performActionDTO, username);

        TempStatus status = performActionDTO.getStatus();
        String reason = performActionDTO.getReason();

        if (Objects.isNull(status)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91);
        } else {
            if (status.equals(TempStatus.DECLINED) && GeneralUtil.stringIsNullOrEmpty(reason)) {
                throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode,
                        "Every declined record must have a reason");
            }
        }

        log.info("Performing Temp action on document id => {} with status => {}", docId, status.name());

        //validate that id exist
        Document document = documentService.getDocumentById(docId);

        //check if user is not one who created
        if (document.getCreatedBy().equals(username)) {
            throw new GeneralException(ResponseCodeAndMessage.OPERATION_NOT_SUPPORTED_93.responseCode, "User who uploaded cannot perform action on it");
        }

        // get the user for approval
        AdminUser user = adminService.getUserByUsername(username);

        //verifies that status is approved and it is currently pending
        if (!document.getStatus().equals(TempStatus.PENDING)) {
            log.info("Record already treated");

            throw new GeneralException(ResponseCodeAndMessage.AN_ERROR_OCCURRED_96.responseCode,
                    "Record had previously been " + document.getStatus().name());

        } else if (status.equals(TempStatus.APPROVED)) {
            log.info("Performing approved action");

            try {
                document.setApproved(true);

                updateDocument(TempStatus.APPROVED, reason, username, document);
            } catch (Exception e) {
                updateDocument(TempStatus.DECLINED, e.getCause().getMessage(), username, document);

                throw new GeneralException(e.getMessage(), e.getCause().getMessage());
            }

        } else if (status.equals(TempStatus.DECLINED)) {
            updateDocument(TempStatus.DECLINED, reason, username, document);
        } else {
            throw new GeneralException(ResponseCodeAndMessage.AN_ERROR_OCCURRED_96.responseCode,
                    "Record is already " + document.getStatus().name());
        }

        return getTempResponse(status);
    }

    private void updateDocument(TempStatus status, String reason, String user, Document document) {
        log.info("Updating document with id => {}, status => {} and reason => {}",
                document.getId(), status.name(), reason);

        document.setModifiedBy(user);
        document.setStatus(status);
        document.setReason(reason);

        documentRepository.save(document);
    }

    private TempResponseDTO getTempResponse(TempStatus status) {
        return TempResponseDTO.builder()
                .result(status.name() + " Successfully").build();
    }

}
