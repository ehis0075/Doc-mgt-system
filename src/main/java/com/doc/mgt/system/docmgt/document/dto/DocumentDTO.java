package com.doc.mgt.system.docmgt.document.dto;

import com.doc.mgt.system.docmgt.user.dto.AdminUserDTO;
import lombok.Data;


@Data
public class DocumentDTO {

    private String name;

    private CreateDocumentTypeDTO type;

    private String fileId;

    private String url;

    private AdminUserDTO adminUser;
}
