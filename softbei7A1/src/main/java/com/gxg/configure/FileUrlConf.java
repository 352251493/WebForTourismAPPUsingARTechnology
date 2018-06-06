package com.gxg.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 郭欣光 on 2018/5/31.
 */

@Configuration
public class FileUrlConf extends WebMvcConfigurerAdapter {

    @Value("${user.information.base.dir}")
    private String userResourcesUrl;

    @Value("${scenic.area.information.base.dir}")
    private String scenicAreaInformationUrl;

    @Value("${post.card.base.dir}")
    private String postCardUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user_information/**").addResourceLocations("file:" + userResourcesUrl);
        registry.addResourceHandler("/scenic_area_information/**").addResourceLocations("file:" + scenicAreaInformationUrl);
        registry.addResourceHandler("/post_card_information/**").addResourceLocations("file:" + postCardUrl);
    }
}
