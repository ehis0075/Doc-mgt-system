package com.doc.mgt.system.docmgt.tempStorage.dto;


import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempAction;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import lombok.Data;

@Data
public class TempDTO {
    private Long id;

    private TableName tableName;

    private Object newData;

    private Long oldDataId;

    private TempAction action;

    private TempStatus status;

    private String reason;
}
