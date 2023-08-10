package com.doc.mgt.system.docmgt.general.service;



import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import org.springframework.data.domain.Pageable;

public interface GeneralService {

    boolean isStringInvalid(String string);

    //used to format response body
    Response prepareResponse(ResponseCodeAndMessage codeAndMessage, Object data);

    Response prepareResponse(String responseCode, String responseMessage, Object data);


    Pageable getPageableObject(int size, int page);

    void createDTOFromModel(Object from, Object to);
}
