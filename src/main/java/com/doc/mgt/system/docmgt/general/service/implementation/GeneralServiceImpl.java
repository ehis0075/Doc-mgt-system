package com.doc.mgt.system.docmgt.general.service.implementation;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class GeneralServiceImpl implements GeneralService {


    @Value("${max-pull-size:100}")
    private int maxPullSize;


    public GeneralServiceImpl() {

    }

    @Override
    public boolean isStringInvalid(String string) {
        log.info("checking if \"{}\" is valid", string);
        return Objects.isNull(string) || string.trim().equals("");
    }

    @Override
    public Response prepareResponse(ResponseCodeAndMessage codeAndMessage, Object data) {
        return getResponse(codeAndMessage.responseCode, codeAndMessage.responseMessage, data);
    }

    @Override
    public Response prepareResponse(String responseCode, String responseMessage, Object data) {
        return getResponse(responseCode, responseMessage, data);
    }

    @Override
    public Pageable getPageableObject(int size, int page) {
        log.info("Getting pageable object, initial size => {} and page {}", size, page);

        Pageable paged;

        if (size > maxPullSize) {
            log.info("{} greater than max size of {}, defaulting to max", size, maxPullSize);

            size = maxPullSize;
        }

        if (size > 0 && page >= 0) {
            paged = PageRequest.of(page, size, Sort.by("id").descending());
        } else {
            paged = PageRequest.of(0, size, Sort.by("id").descending());
        }

        return paged;
    }

    private Response getResponse(String responseCode, String responseMessage, Object data) {
        Response response = new Response();
        response.setResponseCode(responseCode);
        response.setResponseMessage(responseMessage);
        response.setData(data);

        log.info("ResponseCode => {}, message => {} and data => {}", responseCode, responseMessage, data);

        return response;
    }
}
