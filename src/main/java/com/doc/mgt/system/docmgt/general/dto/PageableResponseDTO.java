package com.doc.mgt.system.docmgt.general.dto;

import lombok.Data;

@Data
public class PageableResponseDTO {
    private boolean hasNextRecord;
    private int totalCount;
}
