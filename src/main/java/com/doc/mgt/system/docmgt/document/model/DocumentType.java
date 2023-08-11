package com.doc.mgt.system.docmgt.document.model;

import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Data
@Entity
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String type;

    public static CreateDocumentTypeDTO getDocumentTypeDTO(DocumentType documentType) {
        CreateDocumentTypeDTO createDocumentTypeDTO = new CreateDocumentTypeDTO();
        BeanUtils.copyProperties(documentType, createDocumentTypeDTO);
        return createDocumentTypeDTO;
    }

}
