package com.doc.mgt.system.docmgt.image.controller;//package com.xpresspayments.centralvasmain.image.controller;
//
//import com.xpresspayments.centralvasmain.image.service.ImageService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/image")
//public class ImageController {
//
//    private final ImageService imageService;
//
//    public ImageController(ImageService imageService) {
//        this.imageService = imageService;
//
//    }
//
//    @PostMapping("/upload")
//    public String uploadImage(@RequestBody String base64) {
//
//        return imageService.upload(base64);
//    }
//
//    @PostMapping("/delete")
//    public String deleteImage(@RequestBody String url) {
//        imageService.delete(url);
//        return "success";
//    }
//
//}
