package com.vians.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName WebResourceConfigurerAdapter
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 16:32
 * @Version 1.0
 **/
@Configuration
public class WebResourceConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**").addResourceLocations("file:D://resources/app/");
        registry.addResourceHandler("/doc/**").addResourceLocations("file:D://resources/doc/");
        registry.addResourceHandler("/AppNote/**").addResourceLocations("file:D://resources/AppNote/");
        registry.addResourceHandler("/EDA/**").addResourceLocations("file:D://resources/EDA/");
        registry.addResourceHandler("/Firmware/**").addResourceLocations("file:D://resources/Firmware/");
        registry.addResourceHandler("/Policy/**").addResourceLocations("file:D://resources/Policy/");
        registry.addResourceHandler("/Protocol/**").addResourceLocations("file:D://resources/Protocol/");
        registry.addResourceHandler("/SDK/**").addResourceLocations("file:D://resources/SDK/");
        registry.addResourceHandler("/Spec/**").addResourceLocations("file:D://resources/Spec/");
        registry.addResourceHandler("/Tools/**").addResourceLocations("file:D://resources/Tools/");
        registry.addResourceHandler("/彩页/**").addResourceLocations("file:D://resources/彩页/");
        registry.addResourceHandler("/内部资料/**").addResourceLocations("file:D://resources/内部资料/");
        registry.addResourceHandler("/images/**").addResourceLocations("file:D://resources/images/");
        super.addResourceHandlers(registry);
    }
}
