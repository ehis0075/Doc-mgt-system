package com.doc.mgt.system.docmgt.general.service.implementation;



import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.image.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class GeneralServiceImpl implements GeneralService {

    private final ImageService imageService;


    @Value("${max-pull-size:100}")
    private int maxPullSize;


    public GeneralServiceImpl(ImageService imageService) {

        this.imageService = imageService;
    }

    @Override
    public boolean isStringInvalid(String string) {
        log.info("checking if \"{}\" is valid", string);
        return Objects.isNull(string) || string.trim().equals("");
    }

    @Override
    public Map<String, String> uploadImageToTemp(String base64, String fileName) {
        if (Objects.isNull(base64) || base64.startsWith("https")) {
            return null;
        }

        fileName = fileName.replaceAll(" ", "_");
        return imageService.upload(base64, fileName);
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

    @Override
    public void createDTOFromModel(Object from, Object to) {
        log.info("Creating DTO from Model entity");
        BeanUtils.copyProperties(from, to);
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
