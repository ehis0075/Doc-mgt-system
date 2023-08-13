package com.doc.mgt.system.docmgt.tempStorage.service;


import com.doc.mgt.system.docmgt.document.dto.DocumentDTO;
import com.doc.mgt.system.docmgt.tempStorage.model.Temp;

public interface TempActionService {

    DocumentDTO getDocumentData(String serviceName, Long oldDataId);

    void performActionForDocument(Temp temp);

}
