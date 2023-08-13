package com.doc.mgt.system.docmgt.document.dto;


import lombok.Data;

@Data
public class UploadDocumentDTO {

    private String fileName;

    private Long documentTypId;

    private String base64Image;

}
