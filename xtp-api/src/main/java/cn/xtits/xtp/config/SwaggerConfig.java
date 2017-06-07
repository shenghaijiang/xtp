//package cn.xtits.xtp.config;
//
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * Created by ShengHaiJiang on 2017/3/10.
// */
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {// extends WebMvcConfigurerAdapter{
////    @Bean
////    public Docket platformApi() {
////        return new Docket(DocumentationType.SWAGGER_2)
////                .apiInfo(apiInfo())
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.any())
////                .build();
////    }
////
////    private ApiInfo apiInfo() {
////        return new ApiInfoBuilder().title("xtp-api").
////                description("©2016 Copyright. Powered By iBase4J.")
////                .contact(new Contact("xtp-api", "", "xtp-api@163.com"))
////                .license("Apache License Version 2.0")
////                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
////                .version("2.0")
////                .build();
////    }
////
////    @Override
////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        registry.addResourceHandler("swagger-ui.html")
////                .addResourceLocations("classpath:/META-INF/resources/");
////        registry.addResourceHandler("webjars/**")
////                .addResourceLocations("classpath:/META-INF/resources/webjars/");
////    }
//}
