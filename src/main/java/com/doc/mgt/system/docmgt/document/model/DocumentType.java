package com.doc.mgt.system.docmgt.document.model;

import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentTypeDTO;
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

    public static DocumentTypeDTO getDocumentTypeDTO(DocumentType documentType) {
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        BeanUtils.copyProperties(documentType, documentTypeDTO);
        return documentTypeDTO;
    }

}
