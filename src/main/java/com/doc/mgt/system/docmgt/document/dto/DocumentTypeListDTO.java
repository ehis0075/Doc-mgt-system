package com.doc.mgt.system.docmgt.document.dto;

import com.doc.mgt.system.docmgt.document.model.DocumentType;
import com.doc.mgt.system.docmgt.general.dto.PageableResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentTypeListDTO extends PageableResponseDTO {

    @JsonProperty("documentType")
    private List<DocumentTypeDTO> documentTypeList;
}
