package com.doc.mgt.system.docmgt.config;

import io.imagekit.sdk.ImageKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class OtherConfig {

    @Value("${imageKit.publicKey}")
    private String imageKitPublicKey;

    @Value("${imageKit.privateKey}")
    private String imageKitPrivateKey;

    @Value("${imageKit.urlEndPoint}")
    private String imageKitUrlEndpoint;


    @Bean
    public ImageKit getImageKit() {
        ImageKit imageKit = ImageKit.getInstance();
        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration(imageKitPublicKey, imageKitPrivateKey, imageKitUrlEndpoint);
        imageKit.setConfig(config);

        return imageKit;
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
