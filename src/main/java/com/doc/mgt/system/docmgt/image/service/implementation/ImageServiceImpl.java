package com.doc.mgt.system.docmgt.image.service.implementation;

import com.doc.mgt.system.docmgt.image.service.ImageService;
import com.doc.mgt.system.docmgt.imageKit.ImageKitService;
import com.doc.mgt.system.docmgt.util.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageKitService imageKitService;

    public ImageServiceImpl(ImageKitService imageKitService) {
        this.imageKitService = imageKitService;
    }

    @Override
    public Map<String, String> upload(String base64, String fileName) {
        try {
            if (GeneralUtil.stringIsNullOrEmpty(base64)) {
                log.error("Base64 cannot be null or empty");
                return null;
            }

            Map<String, String> uploadResult = imageKitService.upload(base64, fileName);
            if (Objects.isNull(uploadResult)) {
                log.error("Image upload to ImageKit failed");
                return null;
            }

            return uploadResult;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean moveFileFromTemp(String url) {
        if (GeneralUtil.stringIsNullOrEmpty(url)) {
            log.error("Image url cannot be empty");
            return false;
        }

        return imageKitService.moveFileFromTemp(url);
    }

    public static void main(String[] args) {

    }

    @Override
    public void delete(String fileID) {
        if (GeneralUtil.stringIsNullOrEmpty(fileID)) {
            log.error("file ID cannot be empty");
            return;
        }

        imageKitService.delete(fileID);
    }

}
