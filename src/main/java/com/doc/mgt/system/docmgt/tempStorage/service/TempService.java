package com.doc.mgt.system.docmgt.tempStorage.service;


import com.doc.mgt.system.docmgt.document.dto.DocumentListDTO;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempPerformActionDTO;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempResponseDTO;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;

public interface TempService {

    TempResponseDTO performTempActionForDoc(TempPerformActionDTO performActionDTO, Long docId, String username);
}
