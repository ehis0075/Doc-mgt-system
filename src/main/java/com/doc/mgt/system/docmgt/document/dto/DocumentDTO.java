package com.doc.mgt.system.docmgt.document.dto;

import com.doc.mgt.system.docmgt.document.model.DocumentType;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import lombok.Data;


@Data
public class DocumentDTO {

    private Long id;

    private String name;

    private DocumentTypeDTO type;

    private String url;

    private String fileId;

    private boolean isApproved;

    private TempStatus status;

    private String createdBy;

    private String modifiedBy;

    private String reason;
}
