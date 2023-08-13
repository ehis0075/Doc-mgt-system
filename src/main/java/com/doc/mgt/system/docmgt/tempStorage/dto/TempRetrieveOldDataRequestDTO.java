package com.doc.mgt.system.docmgt.tempStorage.dto;

import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
import lombok.Data;

@Data
public class TempRetrieveOldDataRequestDTO {
    private Long oldDataId;

    private TableName tableName;

    private String serviceName;
}
