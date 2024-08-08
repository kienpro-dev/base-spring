package com.example.projectbase.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.projectbase.base.CustomAuthenticationEntryPoint;
import com.example.projectbase.base.MyAccessDeniedHandler;
import com.example.projectbase.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    CustomUserDetailsServiceImpl customUserDetailsService;

    @Bean
    public MyAccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                //Tất cả có thể truy cập
                .authorizeRequests()
                .antMatchers(
                        "/uploads/**",
                        "/error/**",
                        "/admin/assets/**",
                        "/auth/admin/**",
                        "/client/**",
                        "/client/assets",
                        "/auth/**",
                        "/car/layout/**",
                        "/car/auth/**",
                        "/car/home/**",
                        "/car/about/**",
                        "/car/client/**",
                        "/car/contact/**",
                        "/car/product-detail/**",
                        "/car/cart/**",
                        "/car/account/**",
                        "/car/check-out/**",
                        "/car/like/**",
                        "/car/order/**")
                .permitAll().and()
                //Chỉ có admin mới có thể truy cập
                .authorizeRequests()
                .antMatchers(
                        "/home/**",
                        "/admin/**",
                        "/users/**",
                        "/categories/**",
                        "/brands/**",
                        "/products/**",
                        "/orders/**",
                        "/statistics/**")
                .access("hasRole('ROLE_ADMIN')").and()
                //Chỉ có user mới có thể truy cập
                .authorizeRequests()
                .antMatchers()
                .access("hasRole('ROLE_USER')").and()
                .authorizeRequests()
                .antMatchers(
                        "/car-owner/**"
                )
                .access("hasRole('ROLE_CAR_OWNER')")
                .anyRequest().authenticated().and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(customAuthenticationEntryPoint());
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "uploads";
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../"))
            dirName = dirName.replace("../", "");
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/404").setViewName("auth/error/admin/404");
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                    "/error/404"));
        };
    }

}

