package com.security.web.sys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadImagePath;
		
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");

		// 업로드 이미지
		registry.addResourceHandler("/upload/images/**")
			.addResourceLocations("file:///"+uploadImagePath)    // 웹에서 이미지 호출시 'file:///' 설정됨
			.setCachePeriod(3600)
			.resourceChain(true)
			.addResolver(new PathResourceResolver());
	}

}
