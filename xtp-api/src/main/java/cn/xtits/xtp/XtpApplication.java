package cn.xtits.xtp;

import cn.xtits.xtf.web.springmvc.ExceptionHandlerResolver;
import cn.xtits.xtf.web.springmvc.JsonMessageConverter;
import cn.xtits.xtf.web.springmvc.RequestContextInterceptor;
import cn.xtits.xtf.web.springmvc.RequestLogInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "cn.xtits.xtp.mapper")
public class XtpApplication extends WebMvcConfigurerAdapter {

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    public static void main(String[] args) {
        SpringApplication.run(XtpApplication.class, args);
    }

    /**
     * 配置拦截器
     *
     * @param registry
     * @author lance
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestContextInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new RequestLogInterceptor()).addPathPatterns("/**");
    }


    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new ExceptionHandlerResolver());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(new JsonMessageConverter());
    }

    /**
     * 添加 CORS 跨域处理
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE");

    }
}
