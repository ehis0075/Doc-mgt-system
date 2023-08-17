package com.doc.mgt.system.docmgt.document.dto;

import lombok.Data;


@Data
public class DocumentDTO {

    private String name;

    private CreateDocumentTypeDTO type;

    private String fileId;

    private String url;

    private String createdBy;

    private String modifiedBy;

    private String reason;
}
