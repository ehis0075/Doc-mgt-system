package com.doc.mgt.system.docmgt.image.service;

import java.util.Map;

public interface ImageService {
    Map<String, String> upload(String base64, String fileName);

    boolean moveFileFromTemp(String url);

    void delete(String fileID);
}
