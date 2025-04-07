//package edu.config;
//
////import org.springframework.context.annotation.Configuration;
////import org.springframework.web.reactive.config.CorsRegistry;
////import org.springframework.web.reactive.config.EnableWebFlux;
////import org.springframework.web.reactive.config.WebFluxConfigurer;
//
////@Configuration
////@EnableWebFlux
////public class WebConfig implements WebFluxConfigurer {
////    @Override
////    public void addCorsMappings(CorsRegistry registry) {
////        registry.addMapping("/**")
////                .allowedOrigins("http://localhost:5173")
////                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
////                .allowedHeaders("*")
////                .allowCredentials(true);
////    }
////}
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173")  // разрешает запросы с этого домена
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // разрешает конкретные методы
//                .allowedHeaders("*")  // разрешает все заголовки
//                .allowCredentials(true);  // если нужно разрешить cookies
//    }
//}
//
