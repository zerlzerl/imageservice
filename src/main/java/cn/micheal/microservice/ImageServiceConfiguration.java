package cn.micheal.microservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置静态资源映射
 * Created by micheal on 2017/6/26.
 */
@Configuration
public class ImageServiceConfiguration extends WebMvcConfigurerAdapter {
    //图片原图本地存放地址
    @Value("${path.image.origin.location}")
    private String originImageLocation;
    //图片原图映射地址
    @Value("${path.image.origin.mapping}")
    private String originImageMapping;

    //图片压缩图本机存放地址
    @Value("${path.image.compressed.location}")
    private String compressedImageLocation;
    //图片压缩图映射地址
    @Value("${path.image.compressed.mapping}")
    private String compressedImageMapping;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(originImageMapping + "**").addResourceLocations("file:" + originImageLocation);
        registry.addResourceHandler(compressedImageMapping + "**").addResourceLocations("file:" + compressedImageLocation);
        super.addResourceHandlers(registry);
    }
}
