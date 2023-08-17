package com.doc.mgt.system.docmgt.tempStorage.service;


import com.doc.mgt.system.docmgt.tempStorage.dto.TempPerformActionDTO;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempResponseDTO;

public interface TempService {

//    TempResponseDTO saveToTemp(Object newData, Long oldDataId, TempAction action, TableName tableName,
//                               Object service, String username,  Map<String, String> result);
//
//    TempResponseDTO saveToTemp(Object newData, Long oldDataId, TempAction action, TableName tableName, Object service, String username);
//
//    TempListDTO getTempListByTableName(TempListRequestDTO requestDTO, TableName tableName, TempStatus status);
//
//    TempDTO getTempDTO(Long tempId);
//
//    Object getOldData(Long tempId);

//    TempResponseDTO performTempAction(TempPerformActionDTO performActionDTO, Long tempId, String username);

    TempResponseDTO performTempActionForDoc(TempPerformActionDTO performActionDTO, Long docId, String username);
}
