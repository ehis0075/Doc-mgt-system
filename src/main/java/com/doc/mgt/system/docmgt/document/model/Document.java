package com.doc.mgt.system.docmgt.document.model;

import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.document.dto.CreateDocumentTypeDTO;
import com.doc.mgt.system.docmgt.user.dto.AdminUserDTO;
import com.doc.mgt.system.docmgt.user.model.AdminUser;
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

    private String fileId;

    private String url;

    @ManyToOne
    private AdminUser adminUser;

    public static DocumentDTO getDocumentDTO(Document document) {
        DocumentDTO documentDTO = new DocumentDTO();
        BeanUtils.copyProperties(document, documentDTO);

        if (Objects.nonNull(document.getAdminUser())) {
            AdminUserDTO adminUserDTO = AdminUser.getUserDTO(document.getAdminUser());
            documentDTO.setAdminUser(adminUserDTO);
        }

        if (Objects.nonNull(document.getType())) {
            CreateDocumentTypeDTO createDocumentTypeDTO = DocumentType.getDocumentTypeDTO(document.getType());
            documentDTO.setType(createDocumentTypeDTO);
        }

        return documentDTO;
    }

}