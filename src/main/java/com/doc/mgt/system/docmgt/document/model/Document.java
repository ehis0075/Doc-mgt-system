package com.doc.mgt.system.docmgt.document.model;

import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne
    private DocumentType type;

    private String url;

    private String fileId;

    private boolean isApproved = false;

    @Enumerated(EnumType.STRING)
    private TempStatus status = TempStatus.PENDING;

    private String createdBy;

    private String modifiedBy;

    private String reason;

    public static DocumentDTO getDocumentDTO(Document document) {
        DocumentDTO documentDTO = new DocumentDTO();
        BeanUtils.copyProperties(document, documentDTO);

        if (Objects.nonNull(document.getType())) {
            CreateDocumentTypeDTO createDocumentTypeDTO = DocumentType.getDocumentTypeDTO(document.getType());
            documentDTO.setType(createDocumentTypeDTO);
        }

        return documentDTO;
    }

}