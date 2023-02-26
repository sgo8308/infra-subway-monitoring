package nextstep.subway.common;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
@EnableWebMvc
public class WebMcvConfig implements WebMvcConfigurer {

//    @Override // 정적 자원 외에 캐싱시 사용
//    public void addInterceptors(InterceptorRegistry registry) {
//        WebContentInterceptor interceptor = new WebContentInterceptor();
//        interceptor.addCacheMapping(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic(),
//                "/paths");
//        registry.addInterceptor(interceptor);
//    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter etagFilter = new ShallowEtagHeaderFilter();
        registration.setFilter(etagFilter);
        registration.addUrlPatterns("/js/*", "/images/*");

        return registration;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**", "/images/**") //이 URL로 오는 모든 애들을
                .addResourceLocations("classpath:/static/js/", "classpath:/static/images/") //이 위치로 매핑시킨다.
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic());
    }

}
