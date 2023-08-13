package com.doc.mgt.system.docmgt.tempStorage.dto;

import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import lombok.Data;

@Data
public class TempPerformActionDTO {
    private String reason;

    private TempStatus status;
}
