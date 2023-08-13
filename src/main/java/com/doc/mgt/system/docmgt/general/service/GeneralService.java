package com.doc.mgt.system.docmgt.general.service;



import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface GeneralService {

    boolean isStringInvalid(String string);

    Map<String, String> uploadImageToTemp(String base64, String fileName);

    //used to format response body
    Response prepareResponse(ResponseCodeAndMessage codeAndMessage, Object data);

    Response prepareResponse(String responseCode, String responseMessage, Object data);


    Pageable getPageableObject(int size, int page);

    void createDTOFromModel(Object from, Object to);
}
