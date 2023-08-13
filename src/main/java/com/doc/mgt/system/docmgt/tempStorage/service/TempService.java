package com.doc.mgt.system.docmgt.tempStorage.service;


import com.doc.mgt.system.docmgt.tempStorage.dto.*;
import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempAction;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;

public interface TempService {

    TempResponseDTO saveToTemp(Object newData, Long oldDataId, TempAction action, TableName tableName,
                               Object service, String username, String fileId);

    TempResponseDTO saveToTemp(Object newData, Long oldDataId, TempAction action, TableName tableName, Object service, String username);

    TempListDTO getTempListByTableName(TempListRequestDTO requestDTO, TableName tableName, TempStatus status);

    TempDTO getTempDTO(Long tempId);

    Object getOldData(Long tempId);

    TempResponseDTO performTempAction(TempPerformActionDTO performActionDTO, Long tempId, String username);

}
